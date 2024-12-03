<ns-numericTextbox-angular
      id="txtEducationalAmount"
      [setting]="cashAmountSetting"
      [(ngModel)]="objEducationalEventDetails.cashAmount"
      (formatValueChanged)="objEducationalEventDetails.displayCashAmount = $event;"
      #txtEducationalAmount
    >
    </ns-numericTextbox-angular>

    cashAmountSetting: INSNumericTextBoxSetting = null;
    comp[propSetting] = {type:"usd",decimals:2,min:0,max:1000};
