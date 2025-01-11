import React, { ChangeEvent, useEffect } from 'react';
import { GlobalContext } from '../../app/GlobalContext';
import { NSGridReact, INSGridReactSettings, INSGridColumn, INSGridCustomClassSetting, INSGridMasterDetailSetting
      } from 'nscomponentsreact';

import { BaseProp, CustomMap } from '../helper/types';
import { ExportService } from '../helper/exportService';
import { TradeDetailRenderer } from '../childScreens/TradeDetailRenderer';
import { PositionSnapshotRenderer } from '../childScreens/PositionSnapshotRenderer';
import { POSITION_SNAPSHOT_GRID_COLUMNS, REFRESH_POSITION_GRID } from '../helper/constants';
import Moment from 'moment'
import { removeGrid } from '../helper/commonService';
import { STATUS_COMPLETED, STATUS_FAILED, STATUS_PENDING } from '../../app/constants';


const viewOptions: CustomMap<string>[] = [
  {label: "Snapshot View", value: "snapshot"},
  {label: "Aggregate View", value: "aggregate"}
];

const filterOptions: CustomMap<string>[] = [
  {label: "Latest Snapshot", value: "latest"},
  {label: "Submissions", value: "submission"},
  {label: "Positions with Trades", value: "withtrades"},
  {label: "Manual Submissions", value: "manual"},
];

type Props = BaseProp & {
 
};

const PositionsSnapshot: React.FC<Props> = (props:Props) => {
  const context = React.useContext(GlobalContext);
  const gridAggregateRef = React.useRef<NSGridReact>(null);
  const gridSnapshotRef = React.useRef<NSGridReact>(null);
  const filterValueRef = React.useRef<string>('');
  const viewTypeValueRef = React.useRef<string>('');
  const fileInput = React.useRef<HTMLInputElement>(null);

  const [gridAggregateSetting,setGridAggregateSetting] = React.useState<INSGridReactSettings>();
  const [gridSnapshotSetting,setGridSnapshotSetting] = React.useState<INSGridReactSettings>();
  const [viewTypeValue,setViewTypeValue] = React.useState<string>(viewOptions[0].value);
  const [filterValue,setFilterValue] = React.useState<string>(filterOptions[0].value);
  const [businessDate,setBusinessDate] = React.useState<string>('');
  const [selectedFile, setSelectedFile] = React.useState<File | undefined>();

  useEffect(() => {
    filterValueRef.current = filterValue;
    viewTypeValueRef.current = viewTypeValue;
  })

  useEffect(() => {
    getPositions();
    window.addEventListener(REFRESH_POSITION_GRID,handleRefreshPositionGrid);
    return () => {
      window.removeEventListener(REFRESH_POSITION_GRID,handleRefreshPositionGrid);
      removeGrid(getGrid());
    };
  },[]);

  useEffect(() => {
    return () => {
      ///removeGrid(getGrid());
    };
  }, [viewTypeValue]);

  const handleRefreshPositionGrid = (event: Event) => {
    //if(!isAggregateView()) {
      getPositions();
    //}
  };

  const getPositions = () => {
    let grid: NSGridReact | null = getGrid();
    if(grid) {
      grid.dataSource([]);
      context.ajax.post(context.getURL("getPositionSnapshot"), {businessDate: null,filter: filterValueRef.current,viewType: viewTypeValueRef.current,aggregateId: null}, null)
      .then((response: any) => {
          context.globalIntercept(response, (response: any) => {
          if (response) {
              setTimeout(() => {
                grid?.dataSource(response);
                if(response?.length && (businessDate === null || businessDate.length === 0)) {
                  setBusinessDate(Moment(response[0].BUSINESS_DATE).format('MM/DD/YYYY'));
                }
              },100);
          }
          });
      })
      .catch((err: any) => context.globalCatchBlock(err));
    }
  };
  

  const hasItemChildren = (item: CustomMap<any>) => {
    if(isAggregateView()) {
      return true;
    }
    else {
      return (item.HAS_TRADE_RECORD === true || item.HAS_TRADE_RECORD === 1);
    }    
  };
  

  const initializeAggergateGridSetting = () => {
    if(!gridAggregateSetting) {
        const gridDetailColumn: INSGridColumn[] = getColumnByViewType('aggregate');
        const customClass: INSGridCustomClassSetting = {headerCell:"columnClass",firstBodyColumn:"columnClass", nonFirstBodyColumn:"columnClass"};
        let masterDetailSetting: INSGridMasterDetailSetting = {hasChildCallback: hasItemChildren,detailRenderer: PositionSnapshotRenderer,detailRendererParam: getDetailRendererParam,
        detailHeight: 500};
        const gridSettings: INSGridReactSettings = {type: NSGridReact.GRID_TYPE_MASTER_DETAIL, masterDetailSetting: masterDetailSetting,enableVirtualScroll:true,
           enableFilter:true,enableAdvancedFilter:true,rowKeyField:"id",enableVariableRowHeight:true,enableRowSelection: false,
           columns: gridDetailColumn, customClass: customClass,heightOffset: 300};
        
        setGridAggregateSetting(gridSettings);
    }
  };

  const initializeSnapshotGridSetting = () => {
    if(!gridAggregateSetting) {
        const gridDetailColumn: INSGridColumn[] = getColumnByViewType('snapshot');
        const customClass: INSGridCustomClassSetting = {headerCell:"columnClass",firstBodyColumn:"columnClass", nonFirstBodyColumn:"columnClass"};
        let masterDetailSetting: INSGridMasterDetailSetting = {hasChildCallback: hasItemChildren,detailRenderer: TradeDetailRenderer,detailRendererParam: getDetailRendererParam,
        detailHeight: 350};
        const gridSettings: INSGridReactSettings = {type: NSGridReact.GRID_TYPE_MASTER_DETAIL, masterDetailSetting: masterDetailSetting,enableVirtualScroll:true,
           enableFilter:true,enableAdvancedFilter:true,rowKeyField:"id",enableVariableRowHeight:true,enableRowSelection: false,
           columns: gridDetailColumn, customClass: customClass,heightOffset: 300};
        
        setGridSnapshotSetting(gridSettings);
    }
  };

  const getDetailRendererParam = (item: CustomMap<any>): CustomMap<string> => {
    return {filter: filterValueRef.current};
  }

  const searchClickHandler = () => {
    const gridDetailColumn: INSGridColumn[] = getColumnByViewType();
    let grid: NSGridReact | null = getGrid();
    grid?.setColumn(gridDetailColumn);
    getPositions();
  }

  const getColumnByViewType = (viewType?: string): INSGridColumn[] => {
    if(isAggregateView(viewType)) {
      return [
        {headerText:"Group",dataField:"GROUP_LABEL_DISPLAY",itemRenderer: groupRenderer},//obligor name or account id
        {headerText:"Symbol",dataField:"UNDERLYING_SYMBOL"},
        {headerText:"Side of Market",dataField:"LONG_SHORT_LABEL"},
        {headerText:"Put_Call",dataField:"PUT_CALL_LABEL"},
        {headerText:"Reportable Qty",dataField:"REPORTABLE_QTY"}
      ];
    }
    return POSITION_SNAPSHOT_GRID_COLUMNS;
  }

  const groupRenderer = (item: CustomMap<any>,dataField: string,index: number,colIndex: number,row: HTMLTableRowElement): HTMLSpanElement | null => {
    if(item) {
      let span: HTMLSpanElement = document.createElement('span');
      span.style.paddingLeft = "5px";
      let spanText = document.createElement("span");
      spanText.innerHTML = item[dataField];
      span.appendChild(spanText);
      if(item.HAS_MANUAL_SUBMISSION) {
        row.style.backgroundColor = "orange";
      } else if(item.HAS_PENDING_SUBMISSION || item.HAS_SUBMITTED_POSITION) {
        row.style.backgroundColor = "green";
      }
      if(item.HAS_TRADE_RECORD) {
        let spanIcon = document.createElement("span");
        spanIcon.innerHTML = '&nbsp;&nbsp;&nbsp;&nbsp;<i class="fa fa-star" aria-hidden="true" title="Trade Record Present"></i>';
        span.appendChild(spanIcon);
      }
      return span;
    }
    return null;
  }

  const getGrid = (): NSGridReact | null => {
    let grid: NSGridReact | null = isAggregateView() ? gridAggregateRef.current : gridSnapshotRef.current;
    return grid;
  }

  const isAggregateView = (viewType?: string): boolean => {
    viewType = viewType || viewTypeValueRef.current || viewTypeValue;
    return (viewType === 'aggregate');
  }

  const viewChangeHandler = (event: ChangeEvent<HTMLSelectElement>) => {
    setViewTypeValue(event.target.value);
    viewTypeValueRef.current = event.target.value;
  }

  const filterChangeHandler = (event: ChangeEvent<HTMLSelectElement>) => {
    setFilterValue(event.target.value);
    filterValueRef.current = event.target.value;
  }

  const handleUploadFile = (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    let isValid: boolean = true;
    if(file)
    {
        let message: string = "";
        const fileType = file.type;
        const fileName = file.name;
        if(fileType !== 'text/csv' && !fileName.endsWith('csv')) {
            message += file.name + " is not a valid format.<br/>";
            isValid = false;
        }
        else if(file.size > 20971520)
        {
            message += file.name + " - Maximum size of an attachment you can upload is (20 MB).<br/>";
            isValid = false;
        } 
        if(message !== "")
        {
            context.popUpService.showAlertPopup(message);
            event.target.value = '';
        }
    }
    if(isValid) {
        setSelectedFile(file);
    }
  };

  const validateUpload = (): boolean => {
    let retValue: boolean = true;
    let arrMsg: string[] = [];
    if(!selectedFile) {
        arrMsg.push("Please Upload a File");
        retValue = false;
    }
    if(!retValue)
    {
        const msg: string = arrMsg.join("<br/>");
        context.popUpService.showAlertPopup(msg);
    }
    return retValue;
  };

  const uploadSubmitHandlerUpload = (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
    event.preventDefault();
    if(validateUpload()) {
        const formData: FormData = new FormData();
        formData.append('uploadedFile', selectedFile as Blob);
        context.callPostAPI("uploadSnapshotFile", formData, (response: any) => {
            if(response?.isSuccess) {
              console.log("Status is Completed.");
              context.popUpService.showSuccessAlertPopup("Uploaded File is Processed");
              reset();
              searchClickHandler();
                /*const fileDetailsId: number = response?.data?.fileDetailsId;
                if(fileDetailsId) {
                    checkFileStatusWithPolling(fileDetailsId, 10, 500);
                }*/
            }
        });
    }
  };

  const getFileDetails = (fileDetailsId: number, callback?: any): Promise<any> => {
    return new Promise((resolve) => {
        context.callGetAPI("uploadedFileDetails/" + fileDetailsId, (response: any) => {
            console.log(response);
            callback && callback(response);
            resolve(response);
        });
    });
  }

  const checkFileStatusWithPolling = async (fileDetailsId: number,maxAttempts: number, delay: number) => {
    let attempt = 1;
    while (attempt <= maxAttempts) {
      console.log(`Attempt #${attempt}`);
      const fileDetails: any = await getFileDetails(fileDetailsId);
      if(fileDetails?.file_status && fileDetails.file_status !== STATUS_PENDING) {
        if(fileDetails.file_status === STATUS_COMPLETED) {
            console.log("Status is Completed, stopping polling.");
            context.popUpService.showSuccessAlertPopup("Uploaded File is Processed");
            reset();
            searchClickHandler();
        }
        else if(fileDetails.file_status === STATUS_FAILED) {
            console.log("Status is Failed, stopping polling.");
            context.popUpService.showFailureAlertPopup("Uploaded File processeing has failed. Please try again later");
        }
        break;
      }

      if (attempt === maxAttempts) {
        console.log("Max attempts reached, status is still Pending.");
      }

      attempt++;

      await new Promise((resolve) => setTimeout(resolve, delay));
    }
  };

  const reset = () => {
    setSelectedFile(undefined);
    if(fileInput.current) {
        fileInput.current.value = '';
    }
  };


  const exportToExcel = () => {
    let fileName: string = "Trade Details";
    let exportService: ExportService = new ExportService();
    let grid: NSGridReact | null = getGrid();
    exportService.initialize(grid,grid?.getFilteredData(),getColumnByViewType(),"normal","normal",context,null,null);
    exportService.excel(fileName,fileName,fileName);
  }

  initializeAggergateGridSetting();
  initializeSnapshotGridSetting();

  const selectStyle: CustomMap<string> = {width: "100%"};
  const labelStyle: CustomMap<string> = {minWidth: "90px"};
  const buttonStyle: CustomMap<string> = {"marginRight": "10px"};
  return (
          <div className="box box-nomura-red">
               <section className="content">
				<div>
					<div className="separator-header with-border fill-box-header">
						<strong>Search Criteria:</strong>
					</div>
                <div className="box-body">
                  <div className="row">
                      <div className="row col-sm-5">
                          <label className='col-sm-2 padding-right-0' style={labelStyle}>Select View:</label>
                          <div className="col-sm-3 padding-left-0">                                  
                                <select style={selectStyle} value={viewTypeValue} onChange={viewChangeHandler}>
                                  {
                                    viewOptions.map((view: CustomMap<string>) => {
                                      return <option value={view.value}>{view.label}</option>
                                    })
                                  }
                                </select>					     
                          </div>
                          <label className='col-sm-2 padding-right-0' style={labelStyle}>Select Filter:</label>
                          <div className="col-sm-3 padding-left-0 padding-right-0">                                  
                            <select value={filterValue} onChange={filterChangeHandler}>
                              {
                                filterOptions.map((filter: CustomMap<string>) => {
                                  return <option value={filter.value}>{filter.label}</option>
                                })
                              }
                            </select>					     
                          </div>
                          <div className="col-sm-1">
                              <div>
                                  <button className="btn btn-nomura-green" onClick={(e) => searchClickHandler()} style={buttonStyle}>Search</button>
                              </div>
                          </div>
                      </div>
                      <div className='line-container col-sm-1'>
                          <div className='line'></div>
                          <span className='or-text'>or</span>
                          <div className='line'></div>
                      </div>
                      <div className="row col-sm-5">
                          <label className="control-label col-sm-2" style={labelStyle}>Upload File:</label>
                          <div className="col-sm-6">
                              <input type="file" ref={fileInput} className="form-control" accept='.csv' onChange={handleUploadFile} />
                          </div>
                          <div className="col-sm-4">
                              <button className="btn btn-nomura-green" onClick={uploadSubmitHandlerUpload}>Submit</button>
                          </div>
                        </div>
                  </div>
                  
                </div>
			  </div>
			  <div className="separator-header with-border fill-box-header"></div>
              <div className="box-header">
                    <h3 className="box-title">Positions Snapshot as on {businessDate}</h3>
                    <div className="pull-right box-tools">
                        <button className="btn btn-nomura-red btn-sm" onClick={(e) => searchClickHandler()}>
                            <i className='fa fa-refresh text-white'></i>&nbsp;&nbsp;&nbsp;Refresh Grid
                        </button>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
                        <button className="btn btn-nomura-default btn-sm" data-toggle="tooltip" data-placement="top" title="Export to Excel"
                                onClick={exportToExcel}>
                            <i className="fa fa-file-excel-o text-nomura-green"></i>
                        </button>
                    </div>
              </div>
              <div className="box-body">
                  {(gridAggregateSetting && isAggregateView()) && <NSGridReact setting={gridAggregateSetting} dataSource={[]} ref={gridAggregateRef}>
                  </NSGridReact>}
                  {(gridSnapshotSetting && !isAggregateView()) && <NSGridReact setting={gridSnapshotSetting} dataSource={[]} ref={gridSnapshotRef}>
                  </NSGridReact>}
              </div>
            </section>  
          </div>
    );
   
};

export default PositionsSnapshot;

 
import React from 'react';

import { NSGridReact, INSGridColumn, INSGridSetting, INSGridCustomClassSetting, INSGridDetailRendererComponent, INSGridDetailRendererComponentParam } from 'nscomponentsreact';
import { GlobalContext } from '../../app/GlobalContext';
import { CustomMap } from '../helper/types';

export const TradeDetailRenderer = React.memo(React.forwardRef<any, INSGridDetailRendererComponent>((props, ref) => {
    const context = React.useContext(GlobalContext);
    const [data,setData] = React.useState({request_id: -1,buz_date: ""});

    const refCon = React.useRef<HTMLDivElement>(null);
    const gridRef = React.useRef<NSGridReact>(null);

    /* Component Editor Lifecycle methods */
    React.useImperativeHandle(ref, () => {
        return {
            init(param: INSGridDetailRendererComponentParam) {
                setData(param.masterData);
            },

            elementAdded(param: INSGridDetailRendererComponentParam) {
                console.log("elementAdded");
                setTradesData(param.masterData);
            },
            renderEverytime(param: INSGridDetailRendererComponentParam) {
                return true;
            }
        };
    });

    const setTradesData = (item: CustomMap<any>) => {
        gridRef.current?.dataSource([]);
        context.ajax.post(context.getURL("getTradeRecordByPosition"), {snapshotId: item.id,businessDate: item.BUSINESS_DATE}, null)
        .then((response: any) => {
            context.globalIntercept(response, (response: any) => {
            if (response) {
                gridRef.current?.dataSource(response);
            }
            });
        })
        .catch((err: any) => context.globalCatchBlock(err));
    };

    const gridDetailColumn: INSGridColumn[] = [
        {headerText: 'Counterparty',dataField:'COUNTERPARTY_SHORT_DESC',width: '200px',showMenu: false,sortable: true},
        {headerText: 'Book Account',dataField:'BOOK_ACCOUNT',width: '200px',showMenu: false,sortable: true},
        {headerText:"Position Created",dataField:"POSITION_CREATED",width:"120px",sortable:true,sortDescending:true,sortType:"date"},
        {headerText:"Trade Date",dataField:"TRADE_DATE",width:"100px",sortable:true,sortDescending:false,sortType:"date"},
        {headerText:"Business Event",dataField:"BUSINESS_EVENT",width:"100px",sortable:true,sortDescending:false,sortType:"number"},
        {headerText:"Trade Status",dataField:"TRADE_STATUS",width:"100px",sortable:false,sortDescending:true},
        {headerText:"Version",dataField:"TRADE_VERSION",width:"80px",sortable:true,sortDescending:true,sortType:"number"},
        {headerText:"Trade Quantity",dataField:"TRADE_QUANTITY",width:"150px",sortable:true,sortDescending:false,sortType:"number"},
        {headerText:"Trade ID",dataField:"TRADE_ID",width:"150px",sortable:true,sortDescending:false,sortType:"number"},
        {headerText:"Position ID",dataField:"POSITION_ID",width:"150px",sortable:true,sortDescending:true,sortType:"number"},
        {headerText: 'User',dataField:'TRADE_USER_NAME',width: '100px',showMenu: false,sortable: true},
        {headerText: 'Created By',dataField:'TRADE_CREATED_BY',width: '100px',showMenu: false,sortable: true},
            {headerText: 'Updated By',dataField:'TRADE_UPDATED_BY',width: '100px',showMenu: false,sortable: true},
    ];
    const customClass: INSGridCustomClassSetting = {headerCell:"columnClass",firstBodyColumn:"columnClass", nonFirstBodyColumn:"columnClass"};
    const gridSetting: INSGridSetting = {type: NSGridReact.GRID_TYPE_NORMAL, 
        enableFilter: true, columns: gridDetailColumn, rowKeyField:"id", customClass: customClass};

    const style: Record<string,string> = {height: "100%", backgroundColor: "#EDF6FF", padding: "20px", boxSizing: "border-box"};
    return (
        <div ref={refCon} style={style}>
            <div style={{height: "10%", padding: "2px", fontWeight: "bold"}}> 
                Trade Activity
		    </div>  
		    <div>
                <NSGridReact setting={gridSetting} dataSource={[]} ref={gridRef} containerStyle={{"width": "100%", height: "350px"}}>
                </NSGridReact>
            </div>
		</div>
    );
}));


import React from 'react';

import { NSGridReact, INSGridColumn, INSGridSetting, INSGridCustomClassSetting, INSGridDetailRendererComponent, INSGridDetailRendererComponentParam, INSGridMasterDetailSetting, INSGridReactSettings } from 'nscomponentsreact';
import { GlobalContext } from '../../app/GlobalContext';
import { CustomMap } from '../helper/types';
import { TradeDetailRenderer } from './TradeDetailRenderer';
import { POSITION_SNAPSHOT_GRID_COLUMNS, REFRESH_POSITION_GRID } from '../helper/constants';

export const PositionSnapshotRenderer = React.memo(React.forwardRef<any, INSGridDetailRendererComponent>((props, ref) => {
    const context = React.useContext(GlobalContext);
    const [data,setData] = React.useState({request_id: -1,buz_date: ""});

    const refCon = React.useRef<HTMLDivElement>(null);
    const gridRef = React.useRef<NSGridReact>(null);
    const dataRef = React.useRef<any>(null);

    /*React.useEffect(() => {
        window.addEventListener(REFRESH_POSITION_GRID,handleRefreshPositionGrid);
        return () => {
          window.removeEventListener(REFRESH_POSITION_GRID,handleRefreshPositionGrid);
        };
    },[]);*/

    /* Component Editor Lifecycle methods */
    React.useImperativeHandle(ref, () => {
        return {
            init(param: INSGridDetailRendererComponentParam) {
                setData(param.masterData);
            },

            elementAdded(param: INSGridDetailRendererComponentParam) {
                console.log("elementAdded",param.filter);
                dataRef.current = {masterData: param.masterData,filter: param.filter};
                getPositions(param.masterData,param.filter);
            },
            renderEverytime(param: INSGridDetailRendererComponentParam) {
                return false;
            }
        };
    });

    const handleRefreshPositionGrid = (event: CustomEvent<{}>) => {
        console.log(1);
        getPositions(dataRef.current.masterData, dataRef.current.filter);
    };

    const getPositions = (item: CustomMap<any>,filter: string) => {
        if(gridRef.current) {
          //gridRef.current?.dataSource([]);
          context.ajax.post(context.getURL("getPositionSnapshot"), {businessDate: null,filter: filter,viewType: 'relatedsnapshot',aggregateId: item.id}, null)
          .then((response: any) => {
              context.globalIntercept(response, (response: any) => {
              if (response) {
                
                  setTimeout(() => {
                    gridRef.current?.dataSource(response);
                  },100);
              }
              });
          })
          .catch((err: any) => context.globalCatchBlock(err));
        }
    };

    const hasItemChildren = (item: CustomMap<any>) => {
        console.log(item);
          return (item.HAS_TRADE_RECORD);
    };

    const gridDetailColumn: INSGridColumn[] = POSITION_SNAPSHOT_GRID_COLUMNS;
    const customClass: INSGridCustomClassSetting = {headerCell:"columnClass",firstBodyColumn:"columnClass", nonFirstBodyColumn:"columnClass"};
    let masterDetailSetting: INSGridMasterDetailSetting = {hasChildCallback: hasItemChildren,detailRenderer: TradeDetailRenderer,detailRendererParam: {},
    detailHeight: 150};
    const gridSettings: INSGridReactSettings = {type: NSGridReact.GRID_TYPE_MASTER_DETAIL, masterDetailSetting: masterDetailSetting,enableVirtualScroll:true,
        enableFilter:true,enableAdvancedFilter:true,rowKeyField:"request_id",enableVariableRowHeight:true,enableRowSelection: false,
        columns: gridDetailColumn, customClass: customClass}

    const style: Record<string,string> = {height: "100%", backgroundColor: "#EDF6FF", padding: "20px", boxSizing: "border-box"};
    return (
        <div ref={refCon} style={style}>
            <div style={{height: "10%", padding: "2px", fontWeight: "bold"}}> 
                Snapshot
		    </div>  
		    <div>
                <NSGridReact setting={gridSettings} dataSource={[]} ref={gridRef} containerStyle={{"width": "100%", height: "500px"}}>
                </NSGridReact>
            </div>
		</div>
    );
}));


