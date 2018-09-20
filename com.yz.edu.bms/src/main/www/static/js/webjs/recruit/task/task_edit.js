$(function(){
    _init_select("taskType", [
          {
              "dictValue": "16", "dictName": "报考信息确认"
          },
          {
              "dictValue": "17", "dictName": "现场确认预约"
          }
    ],$("#taskTypes").val());
	
	$('#taskType').on('change', function(event){  
		initTaskAddition($("#taskType").val());
	}); 
	
    if($("#exType").val() == "UPDATE"){
    	if(varTaskInfo.taskStatus == 1){
    		$("#taskType").attr("disabled", "disabled");
    		$("#taskTitle").attr("disabled", "disabled");
    	}
        $("#is_allow_" + varTaskInfo.isAllow).attr('checked', 'checked');
    	initTaskAddition(varTaskInfo.taskType);
    	$("#basicExplain").text(varTaskInfo.basicExplain ==null?"":varTaskInfo.basicExplain);
    	$("#graduateExplain").text(varTaskInfo.graduateExplain == null ?"":varTaskInfo.graduateExplain);
    	$("#attachExplain").text(varTaskInfo.attachExplain ==null ?"":varTaskInfo.attachExplain);
    	$("#warmPrompt").text(varTaskInfo.warmPrompt ==null ? "" : varTaskInfo.warmPrompt);
    	
    	$("#endTimeInput").val(varTaskInfo.endTime);
    	$("#startTimeInput").val(varTaskInfo.startTime);
    	
    }
    if($("#exType").val() == "LOOK"){
    	initTaskAddition(varTaskInfo.taskType);
    	$("#basicExplain").text(varTaskInfo.basicExplain ==null?"":varTaskInfo.basicExplain);
    	$("#graduateExplain").text(varTaskInfo.graduateExplain == null ?"":varTaskInfo.graduateExplain);
    	$("#attachExplain").text(varTaskInfo.attachExplain ==null ?"":varTaskInfo.attachExplain);
    	$("#warmPrompt").text(varTaskInfo.warmPrompt ==null ? "" : varTaskInfo.warmPrompt);
    	
    	$("#endTimeInput").val(varTaskInfo.endTime);
    	$("#startTimeInput").val(varTaskInfo.startTime);
    	
    	$("#admin-role-save").hide();
    }
    if($("#exType").val() == "Add"){
    	 $("#is_allow_1").attr('checked', 'checked');
    }
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
	
	$("#form-task-add").validate({
		rules : {
			taskType : {
				required : true
			},
			taskTitle : {
				required : true
			},
			startTimeInput : {
				required : true
			},
			endTimeInput : {
				required : true
			}
		},
		onkeyup : false,
		focusCleanup : true,
		success : "valid",
		submitHandler : function(form) {
			$("#startTime").val($("#startTimeInput").val());
			$("#endTime").val($("#endTimeInput").val());
			if($("#taskTitle").val().length >30){
				layer.msg('任务名称最多只能输入30字！', {icon : 2, time : 3000});
				return;
			}
			if($("#taskType").val() ==16){
				if($("#basicExplain").val().length >150){
					layer.msg('基本信息说明最多只能输入150字！', {icon : 2, time : 3000});
					return;
				}
				if($("#graduateExplain").val().length >150){
					layer.msg('毕业信息说明最多只能输入150字！', {icon : 2, time : 3000});
					return;
				}
				if($("#attachExplain").val().length >150){
					layer.msg('附件信息说明最多只能输入150字！', {icon : 2, time : 3000});
					return;
				}
			}else if($("#taskType").val() ==17){
				if(UE.getEditor('warmPrompt').getContentTxt().length >150){
					layer.msg('温馨提示最多只能输入150字！', {icon : 2, time : 3000});
					return;
				}
			}
			$(form).ajaxSubmit({
				type : "post", //提交方式  
				dataType : "json", //数据类型  
				url : "/studentTask/taskUpdate.do", //请求url  
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
function initTaskAddition(taskType){
	if(taskType == 16){
		$("#sceneAffirm").find('.sceneAffirm').length&&UE.getEditor('warmPrompt').destroy();
		$("#sceneAffirm").find('.sceneAffirm').remove();
		var dom = '';
		dom +='<div class="messageAffirm">';
		dom +='<div class="row cl">';
		dom +='<label class="form-label col-xs-4 col-sm-3">基本信息说明：</label>';
		dom +='<div class="formControls col-xs-8 col-sm-9">';
		dom +='<textarea name="basicExplain" id="basicExplain" cols="" rows="" class="textarea" placeholder="请输入基本信息说明，最多150个字" onKeyUp="$.Huitextarealength(this,150)"></textarea>';
		dom +='<p class="textarea-numberbar">';
		dom +='<em class="textarea-length">0</em>/150';
		dom +='</p>';
		dom +='</div>';
		dom +='</div>';
		
		dom +='<div class="row cl">';
		dom +='<label class="form-label col-xs-4 col-sm-3">毕业信息说明：</label>';
		dom +='<div class="formControls col-xs-8 col-sm-9">';
		dom +='<textarea name="graduateExplain" id="graduateExplain" cols="" rows="" class="textarea" placeholder="请输入毕业信息说明，最多150个字" onKeyUp="$.Huitextarealength(this,150)"></textarea>';
		dom +='<p class="textarea-numberbar">';
		dom +='<em class="textarea-length">0</em>/150';
		dom +='</p>';
		dom +='</div>';
		dom +='</div>';
		
		dom +='<div class="row cl">';
		dom +='<label class="form-label col-xs-4 col-sm-3">附件信息说明：</label>';
		dom +='<div class="formControls col-xs-8 col-sm-9">';
		dom +='<textarea name="attachExplain" id="attachExplain" cols="" rows="" class="textarea" placeholder="请输入附件信息说明，最多150个字" onKeyUp="$.Huitextarealength(this,150)"></textarea>';
		dom +='<p class="textarea-numberbar">';
		dom +='<em class="textarea-length">0</em>/150';
		dom +='</p>';
		dom +='</div>';
		dom +='</div>';
		
		
		dom +='<div class="row cl">';
		dom +='<label class="form-label col-xs-4 col-sm-3">温馨提示：</label>';
        dom +='<div class="formControls col-xs-8 col-sm-9">';
        dom +='<script id="warmPrompt" name="warmPrompt" type="text/plain" style="width:100%;height:300px;"></script>'; 
        dom +='</div>';
        dom +='</div>';
        dom +='</div>';
      
		$("#messageAffirm").append(dom);
		var ue = UE.getEditor('warmPrompt');
		
	}else if(taskType == 17){
		$("#messageAffirm").find('.messageAffirm').length&&UE.getEditor('warmPrompt').destroy();
		$("#messageAffirm").find('.messageAffirm').remove();
		var dom = '';
		dom +='<div class="row cl sceneAffirm">';
		dom +='<label class="form-label col-xs-4 col-sm-3">温馨提示：</label>';
        dom +='<div class="formControls col-xs-8 col-sm-9">';
        dom +='<script id="warmPrompt" name="warmPrompt" type="text/plain" style="width:100%;height:300px;"></script>'; 
        dom +='</div>';
        dom +='</div>';
        
        
		$("#sceneAffirm").append(dom);
		var ue = UE.getEditor('warmPrompt',{
			maximumWords:800
		});
	}
};