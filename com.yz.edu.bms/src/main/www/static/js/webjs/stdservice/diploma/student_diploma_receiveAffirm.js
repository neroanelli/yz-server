$(function(){
	
	 $("input[name='followId']").val(dataInfo.followId);
     $("input[name='taskId']").val(dataInfo.taskId);
     $("input[name='learnId']").val(dataInfo.learnId);
	 $("#receiveAffirm-stdName").text(dataInfo.stdName);
     $("#receiveAffirm-grade").text(_findDict('grade', dataInfo.grade));
     $("#receiveAffirm-schoolRoll").text(dataInfo.schoolRoll||'');
     $("#receiveAffirm-unvsInfo").html(getUnvsInfo(dataInfo));
   
     _init_select("receiveStatus",dictJson.diplomaReceiveStatus,dataInfo.receiveStatus);
     var editCodeUrl = '/diploma/receiveAffirmSet.do';
	 $("#form-diploma-receiveAffirm").validate({
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