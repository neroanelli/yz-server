var myDataTable;
$(function() {
	myDataTable = $('.table-sort').dataTable(
	{
		"serverSide" : true,
		"dom" : 'rtilp',
		"ajax" : {
			url : "/enrollLottery/list.do",
			data : {
				"lotteryName" : function(){
					return $("#lotteryName").val();
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
			"mData" : "lotteryName"
		}, {
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
						return '<input type="checkbox" value="'+ row.lotteryId + '" name="lotteryIds"/>';
					},
					"targets" : 0
					},
					{
						 "render" : function(data, type, row, meta) {
							var dom = '';
							if(row.status=='1'){
								dom +='<span class="label label-success radius">在线</span>';
							}else{
								dom +='<span class="label radius">离线</span>';
							}
							return dom;
						},
						"targets" : 2
					},
					{
						 "render" : function(data, type, row, meta) {
						    var dom = '';
						    dom +='开始：'+row.startTime+'<br/>';
						    dom +='结束：'+row.endTime;
							return dom;
						},
						"targets" : 3
					},
					{
						"render" : function(data, type, row, meta) {
							var dom = '';
							if(row.lotteryNum >0){
								dom +='<span class="label label-success radius">活动期间每个用户最多抽'+row.lotteryNum+'次</span>';
							}else{
								dom +='<span class="label radius">无限制</span>';
							}
							return dom;
						},
						"targets" : 4
					} ,
					{
					"render" : function(data, type, row, meta) {
						var dom = '';
						dom += '<a title="编辑" href="javascript:void(0)" onclick="lotteryEdit(\'' + row.lotteryId + '\')" class="ml-5" style="text-decoration:none">';
						dom += '<i class="Hui-iconfont f-18">&#xe692;</i></a>';
						
						return dom;
					},
					//指定是第三列
					"targets" : 5
				} ]
	});

});


/*添加新的抽奖活动*/
function lotteryAdd() {
	var url = '/enrollLottery/toLotteryEdit.do' + '?exType=ADD';
	layer_show('添加报读抽奖活动', url, null, 510, function() {
		myDataTable.fnDraw(true);
	});
}
/*编辑新的抽奖活动*/
function lotteryEdit(lotteryId) {
	var url = '/enrollLottery/toLotteryEdit.do' + '?lotteryId='+ lotteryId +'&exType=UPDATE';
	layer_show('修改报读抽奖活动', url, null, 510, function() {
		myDataTable.fnDraw(false);
	});
}

function searchLottery(){
	myDataTable.fnDraw(true);
}		