$(function(){
	 $("input[name='followId']").val(dataInfo.followId);
     $("input[name='taskId']").val(dataInfo.taskId);
     $("input[name='learnId']").val(dataInfo.learnId);
	 $("#stdName").text(dataInfo.stdName);
     $("#grade").text(_findDict('grade', dataInfo.grade));
     $("#schoolRoll").text(dataInfo.schoolRoll||'');
     $("#unvsInfo").html(getUnvsInfo(dataInfo));
     $("#diplomaCode").val(dataInfo.diplomaCode);
     $("#invoiceNo").val(dataInfo.invoiceNo);		
     var editCodeUrl = '/diploma/diplomaCodeSet.do';
	 $("#form-diploma-code").validate({
		 rules : {
			 diplomaCode:{maxlength : 50},
			 invoiceNo : {maxlength : 50}
         },
		onkeyup : false,
		focusCleanup : true,
		success : "valid",
		submitHandler : function(form) {
			if($("#diplomaCode").val().length >'50'){
				layer.msg('毕业证编号长度过大！', {icon : 2, time : 4000});
				return;
			}
			if($("#invoiceNo").val().length >'50'){
				layer.msg('发票编号长度过大！', {icon : 2, time : 4000});
				return;
			}
			$(form).ajaxSubmit({
				type : "post", //提交方式
				dataType : "json", //数据类型
				url : editCodeUrl, //请求url
				success : function(data) { //提交成功的回调函数
					if(data.code == _GLOBAL_SUCCESS){
						layer.msg('保存成功！', {icon : 1, time : 1000},function(){
                       	 window.parent.myDataTable.fnDraw(false);
							 layer_close();
                        });
					}
				}
			})
		}

	});
});