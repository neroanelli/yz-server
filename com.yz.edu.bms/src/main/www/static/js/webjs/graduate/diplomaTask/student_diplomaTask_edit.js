$(function() {
				$("#form-diplomaTask-add").validate({
					rules : {
						taskName : {
							required : true
						},warmTips : {
							required : true
						},
						warmReminder : {
							required : true
						}
					},
					messages: {
						taskName : {
							required : '这是必填字段'
						},warmTips : {
							required : '这是必填字段'
						},warmReminder : {
							required : '这是必填字段'
						}
					},
					onkeyup : false,
					focusCleanup : true,
					success : "valid",
					submitHandler : function(form) {
						$(form).ajaxSubmit({
							type : "post", //提交方式  
							dataType : "json", //数据类型  
							url : $("#tjType").val() == "UPDATE" ? '/taskprovide/updateOaDiplomaTask.do' : '/taskprovide/insertOaDiplomaTask.do', //请求url  
							success : function(data) { //提交成功的回调函数  
								layer_close();
							},
							error : function(data) {
								layer.msg('操作失败', {
									icon : 1,
									time : 1000
								});
								layer_close();
							}
						})
					}
				});
				
				
				
				if($("#tjType").val() == "UPDATE"){
			    	$("#warmTips2").text(oaDiplomaTask.warmTips);
			    	$("#warmReminder2").text(oaDiplomaTask.warmReminder);
			    }else{
			    	$("#warmTips2").text('恭喜各位同学毕业啦！请各位同学选定领取毕业证时间及地点，按时到达。 \n教育部现行规定毕业证仅此一份，遗失不予补办，领取后请务必妥善保管，建议扫描一份存到邮箱里，如有疑问，请联系班主任。');
			    	$("#warmReminder2").text('①请携带本人二代身份证原件按指定时间，指定地点领取毕业证。 \n②因学校周围车位有限，建议同学们尽量使用交通工具，少开车。');
			    }
			});