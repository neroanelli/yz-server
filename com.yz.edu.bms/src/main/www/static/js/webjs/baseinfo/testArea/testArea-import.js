$(function() {
			$('.skin-minimal input').iCheck({
				checkboxClass : 'icheckbox-blue',
				radioClass : 'iradio-blue',
				increaseArea : '20%'
			});

			$("#form-member-add").validate({
				rules : {
					excelPath : {
						required : true,
					}
				},
				onkeyup : false,
				focusCleanup : true,
				success : "valid",
				submitHandler : function(form) {
					$(form).ajaxSubmit({
						type : "post", //提交方式  
						dataType : "json", //数据类型  
						url : '/testArea/upload.do', //请求url  
						success : function(data) { //提交成功的回调函数  
							layer.msg('操作成功', {
								icon : 1,
								time : 1000
							});
							layer_close();
						},
						error : function(data) {
							layer.msg('操作失败', {
								icon : 1,
								time : 1000
							});
							layer_close();
						}
					}); 
				}
			});
		});