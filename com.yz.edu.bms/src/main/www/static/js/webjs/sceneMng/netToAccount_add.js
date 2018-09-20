$(function(){
		var items = netUser.attrList;
	    var dom = '';
		if($("#exType").val() == "UPDATE"){
			$("#userName").val(netUser.userName);
			$("#userPwd").val(netUser.userPwd);
            for (var i = 0; i < items.length; i++) {
                var item = items[i];
                if(item.defType =='radio'){ //单选
                	dom +='<div class="row cl">';
                	dom += '<input type="hidden" name="attrList[' + i + '].defLabel" value="' + item.defLabel + '" /></td>';
                	dom += '<input type="hidden" name="attrList[' + i + '].attrSeq" value="' + i + '" /></td>';
                	dom += '<input type="hidden" name="attrList[' + i + '].defName" value="' + item.defName + '" /></td>';
                	dom += '<input type="hidden" name="attrList[' + i + '].defCatalog" value="' + item.defCatalog + '" /></td>';
            		dom +='<label class="form-label col-xs-4 col-sm-3">'+item.defLabel+'：</label>';
					dom +='<div class="formControls col-xs-8 col-sm-9 skin-minimal">';
					dom +='<div class="radio-box">';
					dom +='<input type="radio" '+(item.attrValue=='0'?' checked="checked" ':'')+' name="attrList[' + i + '].attrValue" value="0"> <label>否</label>';
					dom +='</div>';	
					dom +='<div class="radio-box">';		
					dom +='<input type="radio" '+(item.attrValue=='1'?' checked="checked" ':'')+' name="attrList[' + i + '].attrValue" value="1"> <label>是</label>';	
					dom +='</div>';	
					dom +='</div>';		
					dom +='</div>';
                }else if(item.defType =='select'){ //下拉
                	dom +='<div class="row cl selectAttr" data-name="'+item.defName+'" data-ds="'+item.defDs+'" data-attrv="'+item.attrValue+'">';
                	dom += '<input type="hidden" name="attrList[' + i + '].defLabel" value="' + item.defLabel + '" /></td>';
                	dom += '<input type="hidden" name="attrList[' + i + '].attrSeq" value="' + i + '" /></td>';
                	dom += '<input type="hidden" name="attrList[' + i + '].defName" value="' + item.defName + '" /></td>';
                	dom += '<input type="hidden" name="attrList[' + i + '].defCatalog" value="' + item.defCatalog + '" /></td>';
                	dom +='<label class="form-label col-xs-4 col-sm-3">'+item.defLabel+'：</label>';
  	                dom +='<div class="formControls col-xs-8 col-sm-9">';
  	                dom +='<select class="select" size="1" id="'+item.defName+'" name="attrList[' + i + '].attrValue" ></select> ';
	  	      		dom +='</div>';		
					dom +='</div>';
                }else{
                	//其他
                }
           }
		}else{
            for (var i = 0; i < items.length; i++) {
                var item = items[i];
                if(item.defType =='radio'){ //单选
                	dom +='<div class="row cl">';
                	dom += '<input type="hidden" name="attrList[' + i + '].defLabel" value="' + item.defLabel + '" /></td>';
                	dom += '<input type="hidden" name="attrList[' + i + '].attrSeq" value="' + i + '" /></td>';
                	dom += '<input type="hidden" name="attrList[' + i + '].defName" value="' + item.defName + '" /></td>';
                	dom += '<input type="hidden" name="attrList[' + i + '].defCatalog" value="' + item.defCatalog + '" /></td>';
            		dom +='<label class="form-label col-xs-4 col-sm-3">'+item.defLabel+'：</label>';
					dom +='<div class="formControls col-xs-8 col-sm-9 skin-minimal">';
					dom +='<div class="radio-box">';
					dom +='<input type="radio" '+(item.defDefault=='0'?' checked="checked" ':'')+' name="attrList[' + i + '].attrValue" value="0"> <label>否</label>';
					dom +='</div>';	
					dom +='<div class="radio-box">';		
					dom +='<input type="radio" '+(item.defDefault=='1'?' checked="checked" ':'')+' name="attrList[' + i + '].attrValue" value="1"> <label>是</label>';
					dom +='</div>';	
					dom +='</div>';		
					dom +='</div>';
                }else if(item.defType =='select'){ //下拉
                	dom +='<div class="row cl selectAttr" data-name="'+item.defName+'" data-ds="'+item.defDs+'">';
                	dom += '<input type="hidden" name="attrList[' + i + '].defLabel" value="' + item.defLabel + '" /></td>';
                	dom += '<input type="hidden" name="attrList[' + i + '].attrSeq" value="' + i + '" /></td>';
                	dom += '<input type="hidden" name="attrList[' + i + '].defName" value="' + item.defName + '" /></td>';
                	dom += '<input type="hidden" name="attrList[' + i + '].defCatalog" value="' + item.defCatalog + '" /></td>';
                	
                	dom +='<label class="form-label col-xs-4 col-sm-3">'+item.defLabel+'：</label>';
  	                dom +='<div class="formControls col-xs-8 col-sm-9">';
  	                dom +='<select class="select" size="1" id="'+item.defName+'" name="attrList[' + i + '].attrValue"></select> ';
	  	      		dom +='</div>';		
					dom +='</div>';
				
                }else{
                	//其他
                }
           }
		}
		 $("#attrDiv").html(dom);
		  //初始化下拉列表
         $("#attrDiv").find('.selectAttr').each(function(){
           var selectId = $(this).data('name');	
           var value = $(this).data('attrv');
     	   $.ajax({
				type: "POST",
				dataType : "json", //数据类型
				url: '/netToAccount/getCityInfoByProvinceCode.do',
				data : {
			     "repCode":$(this).data('ds'),
			     "provinceCode":"440000"
			    },
				success: function(data){
					cityJson = data.body;
					if(data.code=='00'){
						_init_select(selectId,cityJson,value);
					}
				}
			});
        })
		
		$('.skin-minimal input').iCheck({
			checkboxClass: 'icheckbox-blue',
			radioClass: 'iradio-blue',
			increaseArea: '20%'
		});
	 	$('select').select2({
            placeholder: "--请选择--",
            allowClear: true,
            width: "59%"
        });
		
		
		$("#form-admin-add").validate({
			rules : {
				userName : {
					remote : { //验证角色名称是否存在
						type : "POST",
						url : '/netToAccount/validateUser.do', //servlet
						data : {
							userName : function() {
								return $("#userName").val();
							},
							oldUserName : function(){
								return $("#oldUserName").val();
							}
							
						}
					},
					required : true
				},
				roleIds : {
					required : true
				},
				isStaff : {
					required : true
				},
				realName : {
					required : true
				}
			},
			messages : {
				userName : {
					remote : "用户名已存在"
				}
			},
			onkeyup : false,
			focusCleanup : true,
			success : "valid",
			submitHandler : function(form) {
				$(form).ajaxSubmit({
					type : "post", //提交方式  
					dataType : "json", //数据类型  
					url : '/netToAccount/userUpdate.do', //请求url  
					success : function(data) { //提交成功的回调函数  
						if(data.code == _GLOBAL_SUCCESS){
							layer.msg('操作成功！', {icon : 1, time : 1000},function(){
								layer_close();
							});
						}
					}
				})
			}
		});
});	