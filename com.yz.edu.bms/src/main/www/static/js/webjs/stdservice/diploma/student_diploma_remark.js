$(function(){
			
	 		$("#stdName").text(dataInfo.stdName);
	 		$("#remark").val(dataInfo.remark);
            var url = '/diploma/setRemark.do';
			$("#form-degree-edit").validate({
				rules : {
					 remark:{maxlength : 100}
		         },
				onkeyup : false,
				focusCleanup : true,
				success : "valid",
				submitHandler : function(form) {
					if($("#remark").val().length >'100'){
						layer.msg('备注长度过大！', {icon : 2, time : 4000});
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
			
			
			
			$("#admin-role-close").click(function () {
				window.parent.myDataTable.fnDraw(false);
                layer_close();
            })
		});