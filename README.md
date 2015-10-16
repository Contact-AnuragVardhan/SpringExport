.nsPinTip {
  position: absolute;
  padding: 5px;
  z-index: 1000;
  top: 0;
  left: 0;
}



.nsPinTip-content {
  background-color: #00000F;
  color: #fff;
  padding: 8px 10px 7px 10px;
  text-align: center;
  border-radius: 3px;
  -moz-border-radius: 3px;
  -webkit-border-radius: 3px;
}


.nsPinTip-arrow {
  position: absolute;
  width: 0;
  height: 0;
  line-height: 0;
  border: 5px dashed rgba(0,0,0,.75);
}

.nsPinTip-arrow-top { border-top-color: #00000F }
.nsPinTip-arrow-bottom { border-bottom-color: #00000F }
.nsPinTip-arrow-left { border-left-color: #00000F }
.nsPinTip-arrow-right { border-right-color: #00000F }

.nsPinTip-top .nsPinTip-arrow,
.nsPinTip-top-left .nsPinTip-arrow,
.nsPinTip-top-right .nsPinTip-arrow {
  bottom: 0;
  left: 50%;
  margin-left: -5px;
  border-top-style: solid;
  border-bottom: none;
  border-left-color: transparent;
  border-right-color: transparent
}

.nsPinTip-top-left .nsPinTip-arrow,
.nsPinTip-top-right .nsPinTip-arrow {
  margin-left: -8px;
}

.nsPinTip-bottom .nsPinTip-arrow,
.nsPinTip-bottom-left .nsPinTip-arrow,
.nsPinTip-bottom-right .nsPinTip-arrow {
  top: 0px;
  left: 50%;
  margin-left: -5px;
  border-bottom-style: solid;
  border-top: none;
  border-left-color: transparent;
  border-right-color: transparent
}

.nsPinTip-bottom-left .nsPinTip-arrow,
.nsPinTip-bottom-right .nsPinTip-arrow {
  margin-left: -8px;
}


.nsPinTip-left .nsPinTip-arrow {
  right: 0;
  top: 50%;
  margin-top: -5px;
  border-left-style: solid;
  border-right: none;
  border-top-color: transparent;
  border-bottom-color: transparent
}

.nsPinTip-right .nsPinTip-arrow {
  left: 0;
  top: 50%;
  margin-top: -5px;
  border-right-style: solid;
  border-left: none;
  border-top-color: transparent;
  border-bottom-color: transparent
}

.nsPinTip-top-left .nsPinTip-arrow,
.nsPinTip-bottom-left .nsPinTip-arrow {
  left: 85%;
}

.nsPinTip-top-right .nsPinTip-arrow,
.nsPinTip-bottom-right .nsPinTip-arrow {
  left: 15px;
}

.nsPinTip.nsPinTip-small {
  font-size: 12px;
}

.nsPinTip.nsPinTip-medium {
  font-size: 13.5px;
}

.nsPinTip.nsPinTip-large {
  font-size: 14px;
}

.nsPinTip-hide {
  opacity: 0;
}
