var myDataTable;
$(function() {

	//初始化年级下拉框
	 _init_select("grade",dictJson.grade);
	//初始招生类型下拉框
	_init_select("recruitType",dictJson.recruitType);
 	
	//初始化报考层次下拉框
    _init_select("pfsnLevel", dictJson.pfsnLevel);
	
    //初始化院校名称下拉框
    _simple_ajax_select({
        selectId: "unvsId",
        searchUrl: '/bdUniversity/findAllKeyValue.do',
        sData: {},
        showText: function (item) {
            return item.unvs_name;
        },
        showId: function (item) {
            return item.unvs_id;
        },
        placeholder: '--请选择--'
    });
    
    
    
    
    
    
    
    $("#unvsId").append(new Option("", "", false, true));
    $("#unvsId").change(function () {
        $("#pfsnId").removeAttr("disabled");
        init_pfsn_select();
    });
    $("#grade").change(function () {
        $("#pfsnId").removeAttr("disabled");
        init_pfsn_select();
    });
    $("#pfsnLevel").change(function () {
        $("#pfsnId").removeAttr("disabled");
        init_pfsn_select();
    });
    $("#pfsnId").append(new Option("", "", false, true));
    $("#pfsnId").select2({
        placeholder: "--请先选择院校--"
    });
    
    
    
    
    _init_select("taskStatus", [
                            {"dictValue": "0", "dictName": "未完成"},
                            {"dictValue": "1", "dictName": "已完成"}
                        ]);
	myDataTable = $('.table-sort').dataTable(
			{
                "processing": true,
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : "/taskfollow/getTaskList.do",
					data:{
						"unvsId" : function() {
							return $("#unvsId").val();
						},"recruitType" : function() {
							return $("#recruitType").val();
						},"grade" : function() {
							return $("#grade").val();
						},"pfsnId" : function() {
							return $("#pfsnId").val();
						},"stdName" : function() {
							return $("#stdName").val();
						},"idCard" : function() {
							return $("#idCard").val();
						},"mobile" : function() {
							return $("#mobile").val();
						},"pfsnLevel" : function() {
							return $("#pfsnLevel").val();
						},"taskTitle" : function() {
							return $("#taskTitle").val();
						},"empName" : function() {
							return $("#empName").val();
						},"taskStatus" : function() {
							return $("#taskStatus").val();
						}
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
					"mData" : "taskTitle"
				}, {
					"mData" : "schoolRoll"
				},  {
					"mData" : "stdNo"
				},{
					"mData" : "stdName"
				}, {
					"mData" : null
				}, {
					"mData" : null
				}, {
					"mData" : null
				}, {
					"mData" : null
				}, {
					"mData" : "empName"
				}, {
					"mData" : null
				}, {
					"mData" : null
				}, {
					"mData" : null
				}],
				"columnDefs" : [
			            {"render" : function(data, type, row, meta) {
							return '<input type="checkbox" value="'+ row.taskId+';'+row.learnId + '" name="taskIds"/>';
						   },
						"targets" : 0
						},
						{"render" : function(data, type, row, meta) {
							    var taskN = "";
								if(row.taskType == "1"){
									taskN = "通知型";
								}else if(row.taskType == "12"){
									taskN = "青书上课通知";
								}else if(row.taskType == "11"){
									taskN = "新生学籍资料收集";
								}else if(row.taskType == "10"){
									taskN = "开课通知";
								}else if(row.taskType == "9"){
									taskN = "毕业论文及报告通知";
								}else if(row.taskType == "8"){
									taskN = "学位英语报名通知";
								}else if(row.taskType == "7"){
									taskN = "学信网信息核对";
								}else if(row.taskType == "6"){
									taskN = "毕业资料提交";
								}else if(row.taskType == "2"){
									taskN = "跳转型URL";
								}else if(row.taskType == "3"){
									taskN = "地址确认";
								}else if(row.taskType == "15"){
									taskN = "毕业证发放";
								}else if(row.taskType == "14"){
									taskN = "国家开放大学本科统考";
								}else if(row.taskType == "13"){
									taskN = "国开考场城市确认任务";
								}else if(row.taskType == "5"){
									taskN = "国开考试通知";
								}else if(row.taskType == "4"){
									taskN = "考场确认";
								}
								return taskN;
						   },
						   "targets" : 1
						},
						{"render" : function(data, type, row, meta) {
								return row.grade+'级';
						   },
						   "targets" : 6
					     },
						{
							"render" : function(data, type,row, meta) {

								var dom = '';

                                if(_findDict("recruitType", row.recruitType).indexOf("成人")!=-1){
                                    dom += "[成教]";
                                }else {
                                    dom += "[国开]";
                                }
								dom += row.unvsName+'</br>';

                                if(_findDict("pfsnLevel", row.pfsnLevel).indexOf("高中")!=-1){
                                    dom += "[专科]";
                                }else {
                                    dom += "[本科]";
                                }
								dom += row.pfsnName;
                                dom += '(' + row.pfsnCode + ')';

								return dom;
							},
							"targets" : 7,
                            "class":"text-l"
						},
						{"render" : function(data, type, row, meta) {
							return _findDict(
									"stdStage",
									row.stdStage);
							},
							"targets" : 8
						},
						{"render" : function(data, type, row, meta) {
								return '';
							},
							"targets" : 9
						},
						{"render" : function(data, type, row, meta) {
							return 0 == row.taskStatus ? '<span class="label radius">未完成</span>' : '<span class="label label-success radius">已完成</span>';
							},
							"targets" : 11
						},{"render" : function(data, type, row, meta) {
							  return ''
							},
							"targets" : 12
						},{"class":"text-c","render" : function(data, type, row, meta) {
		                    var remarkesCount = row.remarkesCount ? row.remarkesCount : "", taskId = row.taskId ?　row.taskId : "", learnId = row.learnId ?　row.learnId : "";
		                    if(!!remarkesCount){
		                    	var dom = '<a href="javascript:void(0);" style="text-decoration: none;color:#5485C7;" onclick="showEditRemark(\'' + taskId + '\', \''+learnId+'\')">备注信息</a><span>(' + row.remarkesCount + ')</span>';
		                    }else {
		                        var dom = '<a title="添加备注" href="javascript:void(0);" onclick="showEditRemark(\'' + taskId + '\', \''+learnId+'\')" style="text-decoration: none">';
		                        dom += '<i class="iconfont icon-shenhe"></i></a>';
		                    }
		                    return dom;
						},
						"targets" : 13},
						{"render" : function(data, type, row, meta) {
							var dom = '';
							dom += '<a class="tableBtn normal" href="javascript:;" title="手动完成" onclick="finishTask(\'' + row.taskId+'\',\''+row.learnId+ '\')">手动完成</a>';
							
							return dom;
						  },
						  "targets" : 14
					}
				  ]
			});

	});

	function searchTask(){
		myDataTable.fnDraw(true);
	}

	function finishTask(taskId,learnId){
		layer.confirm('确认要完成吗？',function(index){
    		//此处请求后台程序，下方是成功后的前台处理……
    		$.ajax({
    			type : 'POST',
    			url : '/taskfollow/finishTask.do'+'?taskId='+taskId+'&learnId='+learnId,
    			dataType : 'json',
    			success : function(data) {
    				myDataTable.fnDraw(false);
    				layer.msg('已完成!', {icon: 6,time:1000});
    			},
    			error : function(data) {
    				layer.msg('失败！', {
    					icon : 1,
    					time : 1000
    				});
    				myDataTable.fnDraw(false);
    			},
    		});
    		
    	
    	});
	}
    function batchFinish() {
		var chk_value = [];
		$("input[name=taskIds]:checked").each(function() {
			var data={'taskId':$(this).val().split(";")[0],'learnId':$(this).val().split(";")[1]}
			chk_value.push(data);
		});
		if(chk_value.length ==0){
			layer.msg('请选择要操作的数据！', {
				icon : 2,
				time : 2000
			});
			return;
		}
		layer.confirm('确认要完成吗？', function(index) {
			$.ajax({
				type : 'POST',
				url : '/taskfollow/batchFinish.do',
				data : {
					taskIds : JSON.stringify(chk_value)
				},
				dataType : 'json',
				success : function(data) {
					layer.msg('已完成!', {
						icon : 1,
						time : 1000
					});
					myDataTable.fnDraw(false);
					$("input[name=all]").attr("checked", false);
				},
				error : function(data) {
					layer.msg('操作失败！', {
						icon : 1,
						time : 1000
					});
					myDataTable.fnDraw(false);
					$("input[name=all]").attr("checked", false);
				},
			});
		});
	}
    
    
    function showEditRemark(taskId, learnId) {
        var url = '/taskfollow/toEditRemark.do' + '?taskId=' + taskId + '&learnId=' + learnId;
        layer_show('添加备注', url, 600, 300, function () {

        }, false);
    }
    
    function init_pfsn_select() {
        _simple_ajax_select({
            selectId: "pfsnId",
            searchUrl: '/baseinfo/sPfsn.do',
            sData: {
                sId: function () {
                    return $("#unvsId").val() ? $("#unvsId").val() : '';
                },
                ext1: function () {
                    return $("#pfsnLevel").val() ? $("#pfsnLevel").val() : '';
                },
                ext2: function () {
                    return $("#grade").val() ? $("#grade").val() : '';
                }
            },
            showText: function (item) {
                var text = '(' + item.pfsnCode + ')' + item.pfsnName;
                text += '[' + _findDict('pfsnLevel', item.pfsnLevel) + ']';
                return text;
            },
            showId: function (item) {
                return item.pfsnId;
            },
            placeholder: '--请选择专业--'
        });
        $("#pfsnId").append(new Option("", "", false, true));
    }
    