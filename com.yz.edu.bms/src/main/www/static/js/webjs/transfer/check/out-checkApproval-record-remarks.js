  
    $(function () {
       
        var nameDom = '<span><a title="查看学员信息" onclick="toEidt(\''+ learnId +'\',\''+ stdId +'\',\''+ recruitType +'\')"><span class="c-blue">'+ stdName +'</span></a></span>';
        
        $("#stdNameBox").html(nameDom);  //学员
        
        $("#scholarship").val(_findDict('scholarship',scholarship));  //优惠类型
        $("#inclusionStatus").val(_findDict('inclusionStatus',inclusionStatus));  //入围状态
        
        changeCheck();
        initItemDom();

        $("#form-member-add").validate({
            rules: {
            	remark: {
                    required: true,
                }
            },
            messages: {
            	remark : {
					required : '该字段不能为空'
				}
			},
            onkeyup: false,
            focusCleanup: true,
            success: "valid",
            submitHandler: function (form) {
                    $(form).ajaxSubmit({
                        type: "post", //提交方式
                        dataType: "json", //数据类型
                        url: '/studentOutApproval/insertRecordRemarkApproval.do', //请求url
                        success: function (data) { //提交成功的回调函数
                            if (data.code == _GLOBAL_SUCCESS) {
                            	$("#remark").val('');
                                layer.msg('操作成功！', {icon: 1, time: 2000});
                                initItemDom();
                            }
                        }
                    });
            }
        });
      
    });
    
    
    function changeCheck() {
        $.post('/studentOutApproval/findStudentOutInfo.do', {
            learnId: $("#learnId").val()
        }, function (result) {
            result = result.body;
            $("#stdStage").val(_findDict("stdStage", result.stdStage));  //学员阶段
            // $("#stdStageh").val(result.stdStage);  
            //志愿
            $("#unvsId").val(
                "[" + _findDict("recruitType", result.recruitType) + "]" + result.unvsName + ":" + result.pfsnName + "[" + _findDict("pfsnLevel", result.pfsnLevel) + "]");
            //录取/在读
            $("#onUnvsId").val(
                "[" + _findDict("recruitType", result.bsarecruitType) + "]" + result.bsaunvsName + ":" + result.bsapfsnName + "[" + _findDict("pfsnLevel", result.bsapfsnLevel)
                + "]");
          
        }, "json");
    }
    
    
    function initItemDom() {
    	
    	 $.post('/studentOutApproval/findBdStudentOutRemarkInfo.do', {
    		 outId: $("#outId").val()
         }, function (result) {
        	
        	 
             result = result.body;
             if(result.length>0){
            	 $("#theadRecord").siblings("#tbodyRecord").html('');
        	 }
             var dom = '';
             dom += '<tbody id="tbodyRecord">';
             for (var i = 0; i < result.length; i++) {
                 var record = result[i];
                 dom += '<tr>';
                 dom += '<td style="text-align:center;">' + (i+1) + '</td>';
                 dom += '<td style="color:red;text-align:center;">' + record.remark + '</td>';
                 dom += '<td style="text-align:center;">' + record.createrUser + '</td>';
                 dom += '<td style="text-align:center;">' + record.createTime.substring(0,19) + '</td>';
                 dom += '</tr>';
             }
             dom += '</tbody>';
             $("#followRecord").append(dom);
         }, "json");
    	
    }
    
    
    /*用户-编辑*/
    function toEidt(learnId, stdId,recruitType) {
    	var url = '/studentBase/toEdit.do?learnId='
    			+ learnId + '&stdId=' + stdId+"&recruitType="+recruitType;
    	layer_show('学员信息', url, null, null, function() {
    		myDataTable.fnDraw(false);
    	}, true);
    }