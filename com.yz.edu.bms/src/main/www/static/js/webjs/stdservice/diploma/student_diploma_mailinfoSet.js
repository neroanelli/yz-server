$(function(){
	
	 $("input[name='followId']").val(dataInfo.followId);
     $("input[name='taskId']").val(dataInfo.taskId);
     $("input[name='learnId']").val(dataInfo.learnId);
     
	 $("#mailInfo-stdName").text(dataInfo.stdName);
     $("#mailInfo-grade").text(_findDict('grade', dataInfo.grade));
     $("#mailInfo-schoolRoll").text(dataInfo.schoolRoll||'');
     $("#mailInfo-unvsInfo").html(getUnvsInfo(dataInfo));
     _init_radio_box("isMailRadio", "isMail", [
			{"dictValue":"1","dictName":"是"},
			{"dictValue":"0","dictName":"否"}
		], dataInfo.isMail);
     
     $("#addressee").val(dataInfo.addressee);
     $("#mobile").val(dataInfo.mobile);
     $("#address").val(dataInfo.address);
     $("#logisticsNo").val(dataInfo.logisticsNo);
     
     if(dataInfo.province==null||dataInfo.province==''){
    	 dataInfo.province="440000";
     }
     _init_area_select("province", "city", "district",dataInfo.province,dataInfo.city,dataInfo.district);
 
     var editCodeUrl = '/diploma/mailinfoSet.do';
	 $("#form-diploma-mailInfo").validate({
		 rules : {
			 addressee:{maxlength : 10},
             address : {maxlength : 50,isAddress:true},
             logisticsNo : {maxlength : 50},
             mobile : {isTel : true}
         },
		onkeyup : false,
		focusCleanup : true,
		success : "valid",
		submitHandler : function(form) {
            $(form).ajaxSubmit({
				type : "post", //提交方式
				dataType : "json", //数据类型
				url : editCodeUrl, //请求url
				success : function(data) { //提交成功的回调函数
					if(data.code == _GLOBAL_SUCCESS){
						layer.msg('保存成功', {
                            icon : 1,
                            time : 1000
                        });
					}
				}
			})
		}

	});
	 
});
