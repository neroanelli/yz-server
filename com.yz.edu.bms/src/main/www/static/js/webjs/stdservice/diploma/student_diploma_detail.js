$(function(){
		    
            $("#stdName").text(dataInfo.stdName);
            $("#grade").text(_findDict('grade', dataInfo.grade));
            $("#schoolRoll").text(dataInfo.schoolRoll||'');
            $("#unvsInfo").html(getUnvsInfo(dataInfo));
            $("#diplomaCode").text(dataInfo.diplomaCode||'');
            $("#receiveStatus").text(_findDict('diplomaReceiveStatus', dataInfo.receiveStatus));
            $("#receiveTime").text(dataInfo.receiveTime||'');
            $("#unconfirmReason").text(_findDict('unconfirmReason', dataInfo.unconfirmReason));
            $("#invoiceNo").text(dataInfo.invoiceNo||'');     
            $("#receiveAddress").html((dataInfo.placeName||'')+'</br>'+(dataInfo.receiveAddress||''));
            if(dataInfo.unconfirmReason=="5"){
            	$("#otherDiv").show();
            	$("#otherReason").text(dataInfo.otherReason);
            }else{
            	$("#otherDiv").hide();
            }
            var weekDay = ["星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"];
            if(dataInfo.affirmStartTime){
           	 var day = (new Date(dataInfo.affirmStartTime.substring(0,10))).getDay();
           	 var affirmStartTimeStr=new Date(dataInfo.affirmStartTime.substring(0,10)).format('yyyy-MM-dd')+" "+weekDay[day];
           	 var affirmEndTimeStr=dataInfo.affirmStartTime.substring(11).replace('AM', '上午').replace('PM', '下午')+ "-" + dataInfo.affirmEndTime; 
            
           	 $("#affirmStartTime").text(affirmStartTimeStr);
             $("#affirmEndTime").text(affirmEndTimeStr);
            }
           
            _init_radio_box("isMailRadio", "isMail", [
				{"dictValue":"1","dictName":"是"},
				{"dictValue":"0","dictName":"否"}
			], dataInfo.isMail);
           
            var mailInfoTxt=''
            if(dataInfo.addressee){
            	mailInfoTxt+=dataInfo.addressee+" ";
            }
            if(dataInfo.mobile){
            	mailInfoTxt+=dataInfo.mobile+'</br>';
            }
            if(dataInfo.provinceName){
            	mailInfoTxt+=dataInfo.provinceName;
            }
            if(dataInfo.address){
            	mailInfoTxt+=(dataInfo.address||'');
            }
            $("#mailInfo").html(mailInfoTxt);
            $("#logisticsNo").text(dataInfo.logisticsNo||'');
            $("#remark").text(dataInfo.remark||'');
});

function getUnvsInfo(row){
	var pfsnName = row.pfsnName,
	unvsName = row.unvsName,
	recruitType = row.recruitType,
	pfsnCode = row.pfsnCode,
	pfsnLevel = row.pfsnLevel,text = '';
	if (recruitType) {
		if(_findDict("recruitType", recruitType).indexOf("成人")!=-1){
			text += "[成教]";
		}else {
			text += "[国开]";
		}
	}
	if(unvsName) text += unvsName+'</br>';
	if(pfsnLevel) {
		if(_findDict("pfsnLevel", pfsnLevel).indexOf("高中")!=-1){
			text += "[专科]";
		}else {
			text += "[本科]";
		}
	}
	if(pfsnName) text += pfsnName;
	if(pfsnCode) text += "(" + pfsnCode + ")";
	return text;
	
}