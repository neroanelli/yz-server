var myDataTable;
$(function(){
	
	
	initTaskNameSel();
	myDataTable = $('.table-sort').dataTable({
			"processing": true,
			"serverSide" : true,
			"dom" : 'rtilp',
			"ajax" : {
				url : '/taskprovide/list.do',
				data:{
					"warmTips" : function() {
						return $("#warmTips").val();
					},"warmReminder" : function() {
						return $("#warmReminder").val();
					},"diplomaId" : function() {
						return $("#diplomaId").val();
					},"startTime" : function() {
						return $("#startTime").val();
					},"endTime" : function() {
						return $("#endTime").val();
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
			columns : [
				{"mData" : "taskName"},
				{"mData" : "warmTips"},
				{"mData" : "warmReminder"},
				{"mData" : null},
				{"mData" : null}
			],  
			"columnDefs" : [
				{"targets" : 3,"render" : function(data, type, row, meta) {
					return row.createTime.substring(0,19);
				}},
				{"targets" : 4,"render" : function(data, type, row, meta) {
					var dom = '<a class="" href="javascript:;" title="修改" onclick="updateDiomaTask(\'' + row.diplomaId + '\')"><i class="iconfont icon-edit"></i></a>&nbsp;';
                    dom += '<a class="" href="javascript:;" title="删除" onclick="delDiomaTask(\'' + row.diplomaId + '\')"><i class="iconfont  icon-shanchu"></i></a>';
					return dom;
				}}
			]
		});
});

	function searchTask(){
		myDataTable.fnDraw(true);
	}

	/**
	 * 新增发放任务
	 */
	function addDiplomaTask() {
        var url = '/taskprovide/editOrAdd.do' + '?tjType=ADD';
        layer_show('新增发放任务', url, null, 510, function () {
        	initTaskNameSel();
            myDataTable.fnDraw(true);
        });
    }
	
	/**
	 * 修改
	 * @param diplomaId
	 * @returns
	 */
	function updateDiomaTask(diplomaId){
		 var url = '/taskprovide/editOrAdd.do' + '?diplomaId=' + diplomaId + '&tjType=UPDATE';
	        layer_show('修改发放任务', url, null, 510, function () {
	        	initTaskNameSel();
	            myDataTable.fnDraw(false);
	        });
	}
	
	/**
	 * 删除
	 * @param diplomaId
	 * @returns
	 */
	function delDiomaTask(diplomaId){
		layer.confirm('确认要删除吗？', function(index) {
			$.ajax({
				type : 'POST',
				url : '/taskprovide/deleteOaDiplomaTask.do',
				data : {
					diplomaId : diplomaId
				},
				dataType : 'json',
				success : function(data) {
					if(data.body == "false"){
						layer.msg('该任务已有配置参数，不可以删除!', {
							icon : 1,
							time : 1000
						});
					}else{
						layer.msg('已删除!', {
							icon : 1,
							time : 1000
						});
						initTaskNameSel();
						myDataTable.fnDraw(false);
					}
					
					
				},
				error : function(data) {
					layer.msg('删除失败！', {
						icon : 1,
						time : 1000
					});
					myDataTable.fnDraw(false);
				},
			});
		});
	}
	
	function initTaskNameSel(){
		//初始任务名称下拉框
		$.ajax({
			type: "POST",
			dataType : "json", //数据类型
			url: '/taskprovide/getTaskName.do',
			success: function(data){
				var taskNameJson = data.body;
				if(data.code=='00'){
					_init_select("diplomaId",taskNameJson);
				}
			}
		});
	}
	
	