$(function() {
            if("VIEW" == $("#exType").val()){
            	 $("#passSubmit").hide();
           	  	 $("#bhSubmit").hide();
            }

            var bcruptime = 'studentModifyInfo.bcruptime';

            if(null == bcruptime || bcruptime == ''){
                $("#bcruptime").val('');
            }else{
                $("#bcruptime").val(bcruptime);
            }

            //学员阶段
            $("#stdStage").text(_findDict("stdStage",$("#stdStage").text()));

            //性别
            $("#sexv").val(_findDict("sex",$("#sexv").val()));
            $("#newSex").text(_findDict("sex",$("#newSex").text()));

            $("#newNation").text(_findDict("nation",$("#newNation").text()));
            $("#nationv").val(_findDict("nation",$("#nationv").val()));
            $("#pfsnLevelv").val(_findDict("pfsnLevel",$("#pfsnLevelv").val()))
            $("#pfsnLevelpre").val($("#pfsnLevelv").val());
            $("#pfsnLevel").text(_findDict("pfsnLevel",$("#pfsnLevel").text()));
        

            $("#newScholarship").text(_findDict("scholarship",$("#newScholarship").text()));

            $("#grade").val(_findDict("grade",$("#grade").val()));

            //优惠类型
            $("#scholarshipv").val(_findDict("scholarship",$("#scholarshipv").val()));
            
            $("#grade").val(_findDict("grade",$("#grade").val()));
          //考试费
            var examPayStatus = $("#examPayStatus").text();
            if(examPayStatus=="0"){
            	$("#examPayStatus").text("未缴费");
            }else if(examPayStatus=="1"){
            	$("#examPayStatus").text("已缴费");
            }
            $("#newGraduateEdcsType").text(_findDict("edcsType", $("#newGraduateEdcsType").text()));
            $("#graduateEdcsType").val(_findDict("edcsType", $("#graduateEdcsType").val()));
            
            
            $("#form-member-add").validate({
                onkeyup : false,
                focusCleanup : true,
                success : true,
                submitHandler : function(form) {
                    if('3'==$('#checkStatus').val()){
                    	layer.confirm('确认通过审核吗？', function(index) {
        					$.ajax({
        						type : 'POST',
        						url : '/confirmModifyCheck/passStudentModifyCheck.do',
        						data : {
        							modifyId :  $('#modifyId').val(),
        							remark :  $('#remark').val(),
        							checkStatus : 3
        						},
        						dataType : 'json',
        						success : function(data) {
        							if(data.code == _GLOBAL_SUCCESS){
        								
        								layer.msg('审核成功!', {
        									icon : 1,
        									time : 1000
        								});
        								window.parent.myDataTable.fnDraw(false);
        								layer_close();
        								
        							}
        						}
        					});
        				});
                    }else{
                        layer.prompt({
                            title : '填写驳回原因：',
                            formType : 2,
                            maxlength : 50
                        }, function(text, index) {
                        	$.ajax({
    	                        type : "post", //提交方式
    	                        dataType : "json", //数据类型
    	                        data : {
    								modifyId : $('#modifyId').val(),
    								checkStatus : 4,
    								reason : text,
    								remark : $('#remark').val()
    							},
    	                        url : '/confirmModifyCheck/passStudentModifyCheck.do', //请求url
    	                        success : function(data) { //提交成功的回调函数
    	                            if(data.code == _GLOBAL_SUCCESS){
    	                                layer.msg('操作成功！', {icon : 1, time : 1000},function(){
    	                                	 window.parent.myDataTable.fnDraw(false);
    	                                	 layer_close();
    	                                   
    	                                });
    	                            }
    	                        }
    	                    });
                        });
                    }
                }
            });
            
      });