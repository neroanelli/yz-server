$(function(){
	
	
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
	_init_select("semester", dictJson.semester,$("#eyIds").val());
	//初始考试年度下拉框
	url = '/examAffirm/getExamYear.do';
	$.ajax({
         type: "POST",
         dataType : "json", //数据类型  
         url: url+'?status=1',
         success: function(data){
      	     var eyJson = data.body;
      	     if(data.code=='00'){
      	    	_init_select("eyId",eyJson,$("#eyIds").val()); 	 
      	     }
         }
    });
 	
	url = '/studyActivity/getGKExamYear.do';
		$.ajax({
	         type: "POST",
	         dataType : "json", //数据类型  
	         url: url,
	         success: function(data){
	      	     var eyJson = data.body;
	      	     if(data.code=='00'){
	      	    	_init_select("gkEyId",eyJson,$("#eyIds").val()); 	 
	      	     }
	         }
	    });
	//国开城市确认	
	url = '/studyActivity/getExamYearForGK.do';
	$.ajax({
         type: "POST",
         dataType : "json", //数据类型  
         url: url+'?status=1',
         success: function(data){
      	     var eyJson = data.body;
      	     if(data.code=='00'){
      	    	_init_select("examEyIdGK",eyJson,$("#eyIds").val()); 	 
      	     }
         }
    });
	//统考设置
	url = '/studyActivity/getGkUnifiedExam.do';
	$.ajax({
         type: "POST",
         dataType : "json", //数据类型  
         url: url+'?status=1',
         success: function(data){
      	     var eyJson = data.body;
      	     if(data.code=='00'){
      	    	_init_select("gkUnifiedId",eyJson,$("#eyIds").val()); 	 
      	     }
         }
    });
	
	//毕业证发放
	url = '/studyActivity/getOaDiplomaTask.do';
	$.ajax({
         type: "POST",
         dataType : "json", //数据类型  
         url: url,
         success: function(data){
      	     var eyJson = data.body;
      	     if(data.code=='00'){
      	    	_init_select("diplomaId",eyJson,$("#eyIds").val()); 	 
      	     }
         }
    });
    if($("#exType").val() == "UPDATE"){
    	
    	$("#taskContent").text(taskInfo.taskContent);
    	$("#endTimeInput").val(taskInfo.endTime);
    	$("#startTimeInput").val(taskInfo.startTime);
    	//显示创建人和创建时间
    	var createUser = taskInfo.createUser;	
    	var createTime = taskInfo.createTime;		
    	var dom = '';
    	dom +='<div class="line-dot"></div>';
		dom += '<div class="row cl" id="createUser">';
		dom += '<label class="form-label col-xs-4 col-sm-3"><span class="c-red"></span>创建人：</label>';
		dom += '<div class="formControls col-xs-8 col-sm-9">';
		dom += '<span>'+createUser+'</span>';
		dom += '</div>';
		dom += '</div>';
		
		dom += '<div class="row cl" id="createTime">';
		dom += '<label class="form-label col-xs-4 col-sm-3"><span class="c-red"></span>创建时间：</label>';
		dom += '<div class="formControls col-xs-8 col-sm-9">';
		dom += '<span>'+createTime+'</span>';
		dom += '</div>';
		dom += '</div>';
		
		
		$("#staffSelect").append(dom);
		
		var ifCanOper = taskInfo.ifCanOper;
		if(ifCanOper && ifCanOper=='N'){
			 $("#eyId").attr("disabled", "disabled");
			 $("input[name='taskType']:not(:checked)").attr("disabled", "disabled");
		}
    }
    
    if($("#exType").val() == 'LOOK'){
    	 //禁用页面所有标签
        $("#taskTitle").attr("disabled", "disabled");
        $("#startTimeInput").attr("disabled", "disabled");
        $("#endTimeInput").attr("disabled", "disabled");
        $("#taskContent").attr("disabled", "disabled");
        $("#taskUrl").attr("disabled", "disabled");
        $("#isAllow").attr("disabled", "disabled");
        $("#isNeedCheck").attr("disabled", "disabled");
        
        $("#taskContent").text(taskInfo.taskContent);
    	$("#endTimeInput").val(taskInfo.endTime);
    	$("#startTimeInput").val(taskInfo.startTime);
    }
	
	$("#form-task-add").validate({
		rules : {
			taskTitle : {
				required : true
			},
			endTimeInput : {
				required : true
			},
			startTimeInput : {
				required : true
			}
		},
		onkeyup : false,
		focusCleanup : true,
		success : "valid",
		submitHandler : function(form) {
			$("#startTime").val($("#startTimeInput").val());
			$("#endTime").val($("#endTimeInput").val());
			if($("input[name='taskType']:checked").val() =='4'){
				if($("#eyId").val()==''){
					layer.msg('考前确认必须选择考试年度！', {icon : 2, time : 4000});
					return;
				}
			}
			if($("input[name='taskType']:checked").val() =='5'){
				if($("#gkEyId").val()==''){
					layer.msg('国开考试通知必须选择考试年度！', {icon : 2, time : 4000});
					return;
				}
			}
			if($("input[name='taskType']:checked").val() =='13'){
				if($("#examEyIdGK").val()==''){
					layer.msg('国开考场城市确认任务必须选择考试年度！', {icon : 2, time : 4000});
					return;
				}
			}
			
			if($("input[name='taskType']:checked").val() =='14'){
				if($("#gkUnifiedId").val()==''){
					layer.msg('国开统考必须选择统考设置！', {icon : 2, time : 4000});
					return;
				}
			}
			
			if($("input[name='taskType']:checked").val() =='3'){
				if($("#semester").val()==''){
					layer.msg('地址确认任务必须选择学期！', {icon : 2, time : 4000});
					return;
				}
			}

			if($("input[name='taskType']:checked").val() =='15'){
				if($("#diplomaId").val()==''){
					layer.msg('毕业证发放必须选择任务！', {icon : 2, time : 4000});
					return;
				}
			}
			$(form).ajaxSubmit({
				type : "post", //提交方式  
				dataType : "json", //数据类型  
				url : '/task/taskUpdate.do', //请求url  
				success : function(data) { //提交成功的回调函数  
					if(data.code == _GLOBAL_SUCCESS){
						layer.msg('操作成功！', {icon : 1, time : 3000},function(){
							layer_close();
						});
					}
				}
			})
		}
	
	});
	
});