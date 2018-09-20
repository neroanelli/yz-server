$(function(){
	
	 $("input[name='followId']").val(dataInfo.followId);
     $("input[name='taskId']").val(dataInfo.taskId);
     $("input[name='learnId']").val(dataInfo.learnId);
	 $("#unconfirmReason-stdName").text(dataInfo.stdName);
     $("#unconfirmReason-grade").text(_findDict('grade', dataInfo.grade));
     $("#unconfirmReason-schoolRoll").text(dataInfo.schoolRoll||'');
     $("#unconfirmReason-unvsInfo").html(getUnvsInfo(dataInfo));  
     $("#otherReason").val(dataInfo.otherReason);
     if(dataInfo.unconfirmReason=="5"){
     	$("#otherDiv").show();	
     }else{
     	$("#otherDiv").hide();
     }
     _init_select("unconfirmReason",dictJson.unconfirmReason,dataInfo.unconfirmReason);
     
     var editCodeUrl = '/diploma/unconfirmReasonSet.do';
	 $("#form-diploma-unconfirmReason").validate({
		onkeyup : false,
		focusCleanup : true,
		success : "valid",
		submitHandler : function(form) {
			if($("#otherReason").val().length >'100'){
				layer.msg('其它原因长度过大！', {icon : 2, time : 4000});
				return;
			}
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
	 
	 
	 $("#unconfirmReason-reset").click(function () {
         var url = '/diploma/resetUnconfirmReason.do';
         layer.confirm("是否确定重置未确认原因！",function () {
             $.ajax({
                 type : "post", //提交方式
                 dataType : "json", //数据类型
                 url : url, //请求url
					data:{ 
						followId:$("input[name='followId']").val(),
						learnId:$("input[name='learnId']").val()
					},
					success : function(data) { //提交成功的回调函数
                     if(data.code == _GLOBAL_SUCCESS){
                    	 layer.msg('操作成功', {
                             icon : 1,
                             time : 1000
                         });
                     }
                 }
             })
         })
     });
	 
	 $("#unconfirmReason").change(function () {
    	 //其它原因
    	 if($("#unconfirmReason").val()=="5"){
    		 $("#otherDiv").show();
    	 }else{
    		 $("#otherDiv").hide();
    	 }
     });
	 
});