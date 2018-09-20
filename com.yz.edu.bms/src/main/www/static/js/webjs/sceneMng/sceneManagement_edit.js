$(function() {

	//初始考试区县下拉框
	$.ajax({
		type: "POST",
		dataType : "json", //数据类型
		url: '/sceneManagement/getExamDicName.do',
		success: function(data){
			var examDicJson = data.body;
			if(data.code=='00'){
				_init_select("taId",examDicJson,sceneManagement.taId);
			}
		}
	});
	
	var ueditorPreContent = UE.getEditor('requiredMaterials');
	ueditorPreContent.addListener("ready", function () {
        // editor准备好之后才可以使用
        if(sceneManagement.requiredMaterials !=null){
        	ueditorPreContent.setContent(sceneManagement.requiredMaterials);	
        }
    });
	
        $("#form-sceneManagement-edit").validate({
        	
            rules : {
            	taId : {
                    required : true,
                },
                confirmCity : {
		            required : true,
		            maxlength : 20
		        },
		        confirmName : {
		            required : true,
		            maxlength : 50
		        },
		        address : {
		            required : true,
		            maxlength : 100
		        }
            },
            messages : {
            	taId : {
                    required : "该字段必填",
                },
                confirmCity : {
                    required : "该字段必填且最大长度为20",
                },
                confirmName : {
                    required : "该字段必填且最大长度为50",
                },
                address : {
                    required : "该字段必填且最大长度为100",
                }
            },
            onkeyup : false,
            focusCleanup : true,
            success : "valid",
            submitHandler : function(form) {
            	
            	$("#taName").val($("#taId").find("option:selected").text());
                var validate=true;
                
                if(!$("#confirmAddressLevel-0").is(':checked')&&!$("#confirmAddressLevel-1").is(':checked')){
                	validate=false;
                    layer.msg('请确认学员层次！', {icon: 5, time: 1000});
                    return;
                }
                if(!isEmpty(ueditorPreContent.getContent())){
                	validate=false;
                    layer.msg('请填写所需材料！', {icon: 5, time: 1000});
                    return;
                }else if(isEmpty(ueditorPreContent.getContent())){
                	if(ueditorPreContent.getContentTxt().length>800){
                		validate=false;
                        layer.msg('所需材料最多800个字！', {icon: 5, time: 1000});
                        return;
                	}
                }
                if(isEmpty($("#chargePerson").val())){
                	if($("#chargePerson").val().length>30){
                		validate=false;
                        layer.msg('负责人最多不能超过30个字！', {icon: 5, time: 1000});
                        return;
                	}
                	
                }
               /* if(isEmpty($("#chargePersonTel").val())){
                	var myreg=/^[1][3,4,5,7,8][0-9]{9}$/;  
                    if (!myreg.test($("#chargePersonTel").val())) {  
                    	validate=false;
                        layer.msg('请输入正确的手机号码', {icon: 5, time: 1000});
                        return;
                    }
                	
                	
                }*/
                $('#TCConfig > .item').each(function (index) {
                	
                    if (!$(this).find('#date' + index + '').val()) {
                        validate=false;
                        layer.msg('请选择日期！', {icon: 5, time: 1000});
                        return;
                    }
                    if (!$(this).find('#startTime' + index + '').val()) {
                        validate=false;
                        layer.msg('请选择开始时间！', {icon: 5, time: 1000});
                        return;
                    }
                    if (!$(this).find('#endTime' + index + '').val()) {
                        validate=false;
                        layer.msg('请选择结束时间！', {icon: 5, time: 1000});
                        return;
                    }
                    if (!$(this).find('#number' + index + '').val()) {
                        validate=false;
                        layer.msg('请输入确认点容量！', {icon: 5, time: 1000});
                        return;
                    }else{
                    	 
                          
                    	  var re = /^[0-9]{0,4}$/;
                    	 
                          if (!re.test($(this).find('#number' + index + '').val())) {
                        	  validate=false;
                              layer.msg('容量请输入4位以内的正整数!', {
                                  icon: 2,
                                  time: 1000
                              });
                           return;
                          }
                    }
               
                })
                if(isEmpty($("#appointmentNum").val())){
                	if(parseInt($("#number0").val())<parseInt($("#appointmentNum").val())){
                		validate=false;
                        layer.msg('人数容量必须大于等于已预约人数！', {icon: 5, time: 1000});
                        return;
                	}
                }
                if(isEmpty($("#remark").val())){
                	if($("#remark").val().length>50){
                		validate=false;
                        layer.msg('备注最多不能超过50个字！', {icon: 5, time: 1000});
                        return;
                	}
                	
                }
                ;
               
                if(!validate) return;

                $(form).ajaxSubmit({
                    type : "post", //提交方式
                    dataType : "json", //数据类型
                    url : $("#tjType").val() == "UPDATE" ? '/sceneManagement/updateSceneManagement.do' : '/sceneManagement/insert.do',//请求url
                    success : function(data) { //提交成功的回调函数
                        if (data.code == _GLOBAL_SUCCESS) {
                            layer.msg('操作成功！', {
                                icon : 1,
                                time : 1000
                            },function () {
                                window.parent.myDataTable.fnDraw(false);
                                layer_close();
                            });
                        }
                    }
                })
            }
        });

      
       if(sceneManagement.confirmAddressLevel){
			if(sceneManagement.confirmAddressLevel=='3'){
				$("#confirmAddressLevel-0").attr("checked",'true');
				$("#confirmAddressLevel-1").attr("checked",'true');
			}else if(sceneManagement.confirmAddressLevel=='1'){
				$("#confirmAddressLevel-1").attr("checked",'true');
			}else if(sceneManagement.confirmAddressLevel=='5'){
				$("#confirmAddressLevel-0").attr("checked",'true');
			}
		}
        
       
       if($("#tjType").val() == "UPDATE"){
    	    if(sceneManagement.remark!=null){
    	    	$("#remark").text(sceneManagement.remark);
    	    }
	    	$("#addItems").hide();
	    	$("#delItems").hide();
	    	$("#address").text(sceneManagement.address);
	    	$("#number0").val(sceneManagement.number);
	    	$("#appointmentNum").val(parseInt(sceneManagement.number) - parseInt(sceneManagement.availableNumbers));
	    	/*if(parseInt(sceneManagement.availableNumbers)<parseInt(sceneManagement.number)){
	    		$('#address').attr("disabled",true);
	    		$('#date0').attr("disabled",true);
	    		$('#startTime0').attr("disabled",true);
	    		$('#endTime0').attr("disabled",true);
	    		$('#chargePersonTel').attr("disabled",true);
	    		$('#chargePerson').attr("disabled",true);
	    		ueditorPreContent.addListener('ready',function(){
	    			ueditorPreContent.setDisabled();
	    	     });
	    		$("input[type=checkbox]").attr("disabled",true);   
	    		$('#confirmName').attr("disabled",true);
	    		$('#confirmCity').attr("disabled",true);
	    		$('#taId').attr("disabled",true);
	    	}*/
	    }
       
     
    });

   



    function addPlace() {
        var len=$('#TCConfig > .item').length;
        var html='<div class="item" style="margin-top: 10px">\n' +
            '                        <input type="text" onfocus="WdatePicker({ dateFmt:\'yyyy-MM-dd\'})" id="date'+len+'" name="configs['+len+'].date" class="input-text Wdate" style="width: 150px;"  placeholder="日期" />\n' +
            '                        &nbsp; <input type="text" placeholder="开始时间"  onfocus="WdatePicker({ dateFmt:\'HH:mm\',minDate:\'#F{$dp.$D(\\\'date'+len+'\\\',{d:-1})}\'})" id="startTime'+len+'" name="configs['+len+'].startTime" class="input-text Wdate" style="width: 100px;" /> -\n' +
            '                        <input type="text" placeholder="结束时间"  onfocus="WdatePicker({ dateFmt:\'HH:mm\', minDate:\'#F{$dp.$D(\\\'startTime'+len+'\\\');}\'})" id="endTime'+len+'" name="configs['+len+'].endTime" class="input-text Wdate" style="width: 100px;" />\n' +
            '                        <input type="number" class="input-text" maxlength="4" id="number'+len+'" name="configs['+len+'].number" style="width: 80px;" placeholder="考场容量" min="1">\n' +
            '                    </div>'
        html=$(html);
        $('#TCConfig').append(html);
    }
    function delPlace() {
        var len=$('#TCConfig > .item').length;
        if(len===1) return;
        $('#TCConfig > .item:last-child').remove();
    }
    
    function isEmpty(StringVal){
    	
    	if(StringVal=="" || StringVal==undefined || StringVal==null){
    		return false;
    	}else{
    		return true;
    	}
    	
    }