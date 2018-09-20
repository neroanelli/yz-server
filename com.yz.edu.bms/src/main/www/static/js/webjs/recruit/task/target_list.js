var myDataTable;
$(function() {
	
	 _init_select("ifFinish",[
  	 {
  		"dictValue":"1","dictName":"已完成"
  	 		},
  	 {
  		"dictValue":"0","dictName":"未完成"
  	 		}
  	 ]);
	 _init_select("webRegisterStatus",[
           {"dictValue":"0","dictName":"待网报"},
           {"dictValue":"1","dictName":"网报成功"}
	  ]);
	 _init_select("sceneConfirmStatus",[
           {"dictValue":"0","dictName":"未确认"},
           {"dictValue":"1","dictName":"已确认"}
	  ]);
	 _init_select("hasExamNo",[
            {"dictValue":"1","dictName":"是"},
            {"dictValue":"2","dictName":"否"}
 	  ]);
	myDataTable = $('.table-sort').dataTable(
		{
			"serverSide" : true,
			"dom" : 'rtilp',
			"ajax" : {
				url : "/studentTask/getTargetStu.do",
				data: function (pageData) {
                     return searchData(pageData);
                }
			},
			"pageLength" : 10,
			"pagingType" : "full_numbers",
			"ordering" : false,
			"searching" : false,
			"createdRow" : function(row, data, dataIndex) {
				$(row).addClass('text-c');
			},
			"language" : _my_datatables_language,

			columns : [ {
				"mData" : null
			}, {
				"mData" : null
			}, {
				"mData" : null
			}, {
				"mData" : null
			}],
			"columnDefs" : [
	            {"render" : function(data, type, row, meta) {
					return '<input type="checkbox" value="'+ row.learnId+';'+row.openId+'" name="learnIds"/>';
				   },
				"targets" : 0
				},
				{"render" : function(data, type, row, meta) {
					   var dom ='';
                    	dom = row.stdName;
                    	if(row.stdType ==='2'){
                    		dom += ' <sup style="color:#f00">外</sup>';
                    	}
                    	return dom;
					},
					"targets" : 1
				},
				{"render" : function(data, type, row, meta) {
					  var dom ='';
					  if(row.taskStatus == '1'){
						  dom += '已完成';
					  }else if(row.taskStatus == '0'){
						  dom += '未完成';
					  }
					  return dom;
				   },
				   "targets" : 2
				},
				{"render" : function(data, type, row, meta) {
					   var dom ='';
					   if(row.empName){
						   dom = row.empName;
                           if(row.empStatus ==='2'){
                        		dom += ' <sup style="color:#f00">离</sup>';
                            } 
					    }
                    	return dom;
					},
					"targets" : 3
				}
			  ]
		});

	});
	//封装搜索条件
	function searchData(pageData) {
	    return {
			stdName : $("#stdName").val() ? $("#stdName").val() : '',
			idCard : $("#idCard").val() ? $("#idCard").val() : '',
			mobile : $("#mobile").val() ? $("#mobile").val() : '',
			ifFinish : $("#ifFinish").val() ? $("#ifFinish").val() : '',
			empName : $("#empName").val() ? $("#empName").val() : '',
			webRegisterStatus : $("#webRegisterStatus").val() ? $("#webRegisterStatus").val() : '',
			hasExamNo : $("#hasExamNo").val() ? $("#hasExamNo").val() : '',
			sceneConfirmStatus : $("#sceneConfirmStatus").val() ? $("#sceneConfirmStatus").val() : '',
			taskId : $("#taskId").val() ? $("#taskId").val() : '',
			start : pageData.start,
			length : pageData.length
		};
	}

	//搜索学员信息
	function searchStudent() {
		myDataTable.fnDraw(true);
	}
	
	//查询提醒未完成
	function queryRemind() {
        var url = '/studentTask/queryRemind.do';
        if($("#ifFinish").val() =='1'){
        	 layer.msg('已完成的不能再提醒!!!', {
                 icon : 5,
                 time : 3000
             });
             return false;
        }
        layer.confirm('确认根据筛选条件提醒未完成学员吗？', function(index) {
            $.ajax({
                type : 'POST',
                url : url,
                data:{
					"stdName" : function() {
						return $("#stdName").val();
					},"idCard" : function() {
						return $("#idCard").val();
					},"mobile" : function() {
						return $("#mobile").val();
					},"empName" : function() {
						return $("#empName").val();
					},"ifFinish" : function() {
						return $("#ifFinish").val();
					},"webRegisterStatus" : function() {
						return $("#webRegisterStatus").val();
					},"hasExamNo" : function() {
						return $("#hasExamNo").val();
					},"sceneConfirmStatus" : function() {
						return $("#sceneConfirmStatus").val();
					},"taskId" : function() {
						return $("#taskId").val();
					}
				},
                dataType : 'json',
                success : function(data) {
                    if (data.code == _GLOBAL_SUCCESS) {
                        layer.msg('提醒成功!', {
                            icon : 1,
                            time : 2000
                        });
                    	myDataTable.fnDraw(true);
                    }
                }
            });
        });
    }

	/*勾选提醒未完成的学员*/
	function checkRemind() {
		var chk_value = [];
		$("input[name=learnIds]:checked").each(function() {
			chk_value.push($(this).val());
		});
		if (chk_value.length == 0) {
			layer.msg('请选择要提醒的数据！', {
				icon : 2,
				time : 2000
			});
			return;
		}
		layer.confirm('确认要提醒勾选的学员吗？', function(index) {
			$.ajax({
				type : 'POST',
				url : '/studentTask/checkRemind.do',
				data : {
					idArray : chk_value,
					taskId : $("#taskId").val()
				},
				dataType : 'json',
				success : function(data) {
					layer.msg('提醒成功!', {
						icon : 1,
						time : 2000
					});
					myDataTable.fnDraw(true);
					$("input[name=all]").attr("checked", false);
				},
				error : function(data) {
					layer.msg('提醒失败！', {
						icon : 1,
						time : 2000
					});
					myDataTable.fnDraw(true);
					$("input[name=all]").attr("checked", false);
				},
			});
		});
	}