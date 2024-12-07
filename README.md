
<div class="form-group">
							      <div class="col-sm-11">
							      		<table nsTableRowMoverAngular [setting]="tableRowMoverSetting" class="ccetTable" #tblData>
										  <tr data-nsDragAllowed="false" data-nsDropAllowed="false">
										  	<th style="width:50px;">
										    </th>
										    <th *ngFor="let col of arrCols" [ngStyle]="{'width': col.width}">{{col.headerText}}</th>
										  </tr>
										  <tr *ngFor="let workflow of arrWorkflow; let in = index" [attr.data-nsDragAllowed]="workflow.isMovable" [attr.data-nsDropAllowed]="workflow.isMovable">
										  	<td>
										  		<a id="anchorClose_{{in}}" href="javascript:void(0);" (click)="removeRecord($event,workflow,in)"
										  			[hidden]="!workflow.isMovable">
							           				<i class='fa fa-close'></i>
								           		</a>
										  	</td>
										    <td *ngFor="let col of arrCols">{{workflow[col.dataField]}}</td>
										  </tr>
										</table>
							      </div>
							</div>

       private initializeRowMover(): void
    {
        if(!this.tableRowMoverSetting)
        {
            let customClass: any = {dragRow:"draggableRow"};
            this.tableRowMoverSetting = {isSameTableMove:true,customClass: customClass,dragEndHandler:this.dropEndHandler.bind(this)};
        }
    };
    
    private dropEndHandler(currentRow: any,item: any)
    {
        console.log(item);
        let util:any = new NSUtil();
        if(item && !util.isUndefinedOrNull(item.oldIndex) && !util.isUndefinedOrNull(item.newIndex) && item.oldIndex != item.newIndex)
        {
            let oldIndex: number = item.oldIndex - 1;
            let newIndex: number = item.newIndex - 1;
            this.arrWorkflow.splice(newIndex, 0, this.arrWorkflow.splice(oldIndex, 1)[0]);
            this.refreshOrder();
        }
    }
    
    private refreshOrder(): void
    {
        if(this.arrWorkflow)
        {
            for(let count: number = 0;count < this.arrWorkflow.length;count++)
            {
                this.arrWorkflow[count].order = count + 1;
            }
        }
    }
