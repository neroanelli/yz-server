var myDataTable;
        $(function () {

            //初始状态
            _init_select("isAllow", [
                {
                    "dictValue": "0", "dictName": "否"
                },
                {
                    "dictValue": "1", "dictName": "是"
                }
            ]);
            _init_select("taskType", [
                {
                    "dictValue": "16", "dictName": "报考信息确认"
                },
                {
                    "dictValue": "17", "dictName": "现场确认预约"
                }
            ]);
            _init_select("taskStatus", [
	              {
	                  "dictValue": "0", "dictName": "未开始"
	              },
	              {
	                  "dictValue": "1", "dictName": "进行中"
	              },
	              {
	                  "dictValue": "2", "dictName": "已结束"
	              }
	        ]);
            myDataTable = $('.table-sort').dataTable(
                {
                    "processing": true,
                    "serverSide": true,
                    "dom": 'rtilp',
                    "ajax": {
                        url: '/studentTask/list.do',
                        data: {
                        	"taskType" : function(){
                        		return $("#taskType").val();
                        	},
                            "taskTitle": function () {
                                return $("#taskTitle").val();
                            },
                            "taskStatus": function () {
                                return $("#taskStatus").val();
                            },
                            "startTimeStart": function () {
                                return $("#startTimeStart").val();
                            },
                            "startTimeEnd": function () {
                                return $("#startTimeEnd").val();
                            },
                            "endTimeStart": function () {
                                return $("#endTimeStart").val();
                            },
                            "endTimeEnd": function () {
                                return $("#endTimeEnd").val();
                            },
                            "isAllow": function () {
                                return $("#isAllow").val();
                            },
                            "creater": function () {
                                return $("#creater").val();
                            }
                        }
                    },
                    "pageLength": 10,
                    "pagingType": "full_numbers",
                    "ordering": false,
                    "searching": false,
                    "createdRow": function (row, data, dataIndex) {
                        $(row).addClass('text-c');
                    },
                    "language": _my_datatables_language,

                    columns: [{
                        "mData": null
                    }, {
                        "mData": null
                    }, {
                        "mData": "taskTitle"
                    }, {
                        "mData": "creater"
                    }, {
                        "mData": null
                    }, {
                        "mData": null
                    }, {
                        "mData": null
                    }, {
                        "mData": null
                    }, {
                        "mData": null
                    }, {
                        "mData": null
                    }, {
                        "mData": null
                    }, {
                        "mData": null
                    }],
                    "columnDefs": [
                        {
                            "render": function (data, type, row, meta) {
                                return '<input type="checkbox" value="' + row.taskId + '" name="taskIds"/>';
                            },
                            "targets": 0
                        },{
                            "render": function (data, type, row, meta) {
                                var dom = '';
                                if(row.taskType== 16){
                                	dom +='报考信息确认';
                                }else if(row.taskType== 17){
                                	dom +='现场确认预约';
                                }
                                return dom;
                            },
                            "targets": 1
                        }, {
                            "render": function (data, type, row, meta) {
                                if(!row.startTime){
                                    return '-'
                                }
                                var date=row.startTime.substring(0,10)
                                var time=row.startTime.substring(11)
                                return date+'<br>'+time
                            },
                            "targets": 4,"class":"text-l no-warp"
                        },{
                            "render": function (data, type, row, meta) {
                                if(!row.endTime){
                                    return '-'
                                }
                                var date=row.endTime.substring(0,10)
                                var time=row.endTime.substring(11)
                                return date+'<br>'+time
                            },
                            "targets": 5,"class":"text-l no-warp"
                        },
                        {
                            "render": function (data, type, row, meta) {
                                return 0 == row.isAllow ? '<span class="label radius">不可用</span>' : '<span class="label label-success radius">可用</span>';
                            },
                            "targets": 6
                        },
                        {
                            "render": function (data, type, row, meta) {
                            	var dom = '';
                            	if(row.taskStatus == 0){
                            		dom +='<span class="label radius">未开始</span>';
                            	}else if(row.taskStatus == 1){
                            		dom +='<span class="label label-success radius">进行中</span>';
                            	}else if(row.taskStatus == 2){
                            		dom +='<span class="label radius">已结束</span>';
                            	}
                            	return dom;
                            },
                            "targets": 7
                        },
                        {
                            "render": function (data, type, row, meta) {
                            	return row.totalCount;
                            },
                            "targets": 8
                        },
                        {
                            "render": function (data, type, row, meta) {
                                var dom = '';
								if(row.taskStatus == 2){
									dom +='<a class="tableBtn normal" title="查看学员" href="javascript:;" onclick="lookStudent(\''+row.taskId+'\',\''+row.taskType+'\')">查看学员</a>';
								}else{
									dom +='<a class="tableBtn blue" title="选择学员" href="javascript:;" onclick="chooseStudent(\''+row.taskId+'\',\''+row.taskType+'\')">选择学员</a>';
								}
                                return dom;
                            },
                            "targets": 9
                        },
                        {
                            "render": function (data, type, row, meta) {
                            	var dom = '';
								dom +='<a class="tableBtn normal" title="查看结果" href="javascript:;" onclick="lookResult(\''+row.taskId+'\',\''+row.taskStatus+'\')">查看结果</a>';
								return dom;
                            },
                            "targets": 10
                        },
                        {
                            "render": function (data, type, row, meta) {
                                var dom = '';
                                if(row.taskStatus == 2){
                                	dom += '<a title="查看" href="javascript:void(0)" onclick="lookTask(\''+ row.taskId+ '\')" class="ml-5" style="text-decoration:none"><i class="iconfont icon-chakan"></i></a>';	
                                }else{
                                	dom += '<a class="maincolor" href="javascript:;" title="编辑" onclick="taskEdit(\'' + row.taskId + '\')"><i class="iconfont icon-edit"></i></a>';	
                                }
                                if (row.isAllow == '0') {
                                    dom += '<a onClick="taskStartOrStop(this,\'' + row.taskId + '\',\''+ 1 +'\')" href="javascript:;" title="启用" style="text-decoration:none"><i class="iconfont icon-qiyong"></i></a>';
                                } else if (row.isAllow == '1') {
                                	dom += '<a onClick="taskStartOrStop(this,\'' + row.taskId + '\',\''+ 0 +'\')" href="javascript:;" title="停用" style="text-decoration:none"><i class="iconfont icon-tingyong"></i></a>';
                                }
                                return dom;
                            },
                            //指定是第三列
                            "targets": 11
                        }]
                });
        });

        /*招生管理-学员任务-添加*/
        function studentTaskAdd() {
            var url = '/studentTask/toEdit.do' + '?exType=Add&v=1.0';
            layer_show('添加学员任务', url, null, null, function () {
                myDataTable.fnDraw(true);
            });
        }
        /*招生管理-学员任务-编辑*/
        function taskEdit(taskId) {
            var url = '/studentTask/toEdit.do' + '?taskId=' + taskId + '&exType=UPDATE';
            layer_show('修改学员任务', url, null, null, function () {
                myDataTable.fnDraw(false);
            });
        }
        /*招生管理-学员任务-查看*/
        function lookTask(taskId) {
            var url = '/studentTask/toEdit.do' + '?taskId=' + taskId + '&exType=LOOK';
            layer_show('查看学员任务', url, null, null, function () {
                myDataTable.fnDraw(false);
            });
        }
        //选择学员
        function chooseStudent(taskId,taskType) {
            var url = '/studentTask/toStu.do' + '?taskId=' + taskId+'&taskType='+taskType+'&exType=CHOOSE';
            layer_show('选择分配学员', url, null, null, function () {
                myDataTable.fnDraw(false);
            }, true);
        }
        //查看学员
        function lookStudent(taskId,taskType){
        	var url = '/studentTask/toStu.do' + '?taskId='+ taskId+'&taskType='+taskType+'&exType=LOOK';
        	layer_show('查看分配学员', url, null, null, function() {
        	},true);
        }
        //查看结果
        function lookResult(taskId,taskStatus){
        	var url = '/studentTask/toTargetStu.do' + '?taskId='+ taskId+'&taskStatus='+taskStatus;
        	layer_show('完成情况', url, null, null, function() {
        	},true);
        }
        //停用或者启用
        function taskStartOrStop(obj, id,taskStatus) {
        	var msg = '';
        	if(taskStatus == 1){
        		msg +='确认要启用吗？';
        	}else if(taskStatus == 0){
        		msg +='确认要停用吗？';
        	}
            layer.confirm(msg, function (index) {
                //此处请求后台程序，下方是成功后的前台处理……
                $.ajax({
                    type: 'POST',
                    url: '/studentTask/startOrStop.do',
                    data: {
                        taskId: id,
                        taskStatus: taskStatus
                    },
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == _GLOBAL_SUCCESS) {
                            myDataTable.fnDraw(false);
                            layer.msg('操作成功!', {icon: 6, time: 1000});
                        }
                    }
                });
            });
        }
        
        /*搜素*/
        function searchTask() {
            myDataTable.fnDraw(true);
        }
      