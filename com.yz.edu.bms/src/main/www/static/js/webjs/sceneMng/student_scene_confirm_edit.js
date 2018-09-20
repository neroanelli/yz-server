$(function(){

            var text = '';
            var recruitTypeText='';
            if (recruitType) {
                if(_findDict("recruitType", recruitType).indexOf("成人")!=-1){
                	recruitTypeText += "[成教]";
                }else {
                	recruitTypeText += "[国开]";
                }
            }
            text += recruitTypeText;
            if(unvsName) text += unvsName;
            if(pfsnLevel) {
                if(_findDict("pfsnLevel", pfsnLevel).indexOf("高中")!=-1){
                    text += "[专科]";
                }else {
                    text += "[本科]";
                }
            }
            if(pfsnName) text += pfsnName;
            if(pfsnCode) text += "(" + pfsnCode + ")";
            $("#unvsInfo").text(text);
            $("#stdStage").text(_findDict('stdStage', stdStage));
            $("#scholarship").text(_findDict('scholarship', scholarship));
            $("#sceneRemark").text(sceneRemark);
            $("#grade").text(grade+"级");
            if (startTime) {
                $("#date").text(startTime.substring(0, 10));
            }
            if (startTime && endTime) {
                var time= startTime.substring(11, 16) + "-" + endTime.substring(11, 16) ;
                $("#time").text(time);
            }
            
            if(dataInfo.confirmUnvsName){
            	$("#confirmUnvsName").text(recruitTypeText+dataInfo.confirmUnvsName);
            }
            var confirmpfsnLevelTxt='';
            if(dataInfo.confirmPfsnLevel) {
                if(_findDict("pfsnLevel", dataInfo.confirmPfsnLevel).indexOf("高中")!=-1){
                	confirmpfsnLevelTxt += "[专科]";
                }else {
                	confirmpfsnLevelTxt += "[本科]";
                }
            }
            $("#confirmPfsnName").text(confirmpfsnLevelTxt+(dataInfo.confirmPfsnName||''));
            
            var url = '/bdSceneConfirm/updateConfirmInfo.do';

			$("#form-xuexin-edit").validate({
				onkeyup : false,
				focusCleanup : true,
				success : "valid",
				submitHandler : function(form) {
					if($("#examNo").val().length >100){
						layer.msg('考生号长度过大！请输入小于20字符', {icon : 2, time : 4000});
						return;
					}
					$(form).ajaxSubmit({
						type : "post", //提交方式
						dataType : "json", //数据类型
						url : url, //请求url
						success : function(data) { //提交成功的回调函数
							if(data.code == _GLOBAL_SUCCESS){
								layer.msg('操作成功！', {icon : 1, time : 1000},function(){
									window.parent.myDataTable.fnDraw(false);
									layer_close();
								});
							}
						}
					})
				}
			});
			
		   $("input[name=registerStatus]").click(function(){
			   var registerId=$("input[name=registerStatus]:checked").attr("id");
			   var username=$("input[name=registerStatus]:checked").attr("username");
			   layer.confirm('确认设置为有效网报信息吗？',function(index) {
					$.ajax({
						type : 'POST',
						url : '/bdSceneConfirm/setAvailabe.do',
						data : {
							registerId : registerId,
							stdId:$("#stdId").val(),
							username:username,
							learnId:$("#learnId").val()
						},
						dataType : 'json',
						success : function(data) {
							if (data.code == _GLOBAL_SUCCESS) {
								layer.msg('设置成功!', {
									icon : 1,
									time : 2000
								});
								
							}else{
								$("input[name=registerStatus]:checked").prop("checked",false);
								$("input[name='registerStatus'][value='1']").prop("checked",true); 
							}
						}
					});
				});
		   });

});


function insertRegisterNo(){
	var url = '/bdSceneConfirm/toAddRegisterNo.do?learnId='+ $("#learnId").val()+'&stdId='+$("#stdId").val();
	var registerAdd=layer_show('新增预报名号', url, 500, 400, function() {
//        myDataTable.fnDraw(true);
		window.location.reload();
	},false);
}