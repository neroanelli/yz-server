var myDataTable;
$(function() {
	myDataTable = $('.table-sort').dataTable(
	{
		"serverSide" : true,
		"dom" : 'rtilp',
		"ajax" : {
			url : "/lotteryPrize/list.do",
			data : {
				"prizeName" : function(){
					return $("#prizeName").val();
				},
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
			"mData" : "prizeName"
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
		}, {
			"mData" : null
		}],
		"columnDefs" : [
		            {"render" : function(data, type, row, meta) {
						return '<input type="checkbox" value="'+ row.prizeId + '" name="prizeIds"/>';
					},
					"targets" : 0
					},
					{
						 "render" : function(data, type, row, meta) {
							var dom = '';
							if(row.prizeType == '1'){
								dom +='实物奖品';
							}else{
								dom +='优惠券';
							}
							return dom;
						},
						"targets" : 3
					},
					{
						"render" : function(data, type, row, meta) {
							return (row.prizeCount==null?'0':row.prizeCount)+'/'+(row.initPrizeCount==null?'0':row.initPrizeCount);
						},
						"targets" : 4
					} ,
					{
						 "render" : function(data, type, row, meta) {
							var dom = '';
							if(row.isCarousel=='1'){
								dom +='<span class="label label-success radius">是</span>';
							}else{
								dom +='<span class="label radius">否</span>';
							}
							return dom;
						},
						"targets" : 5
					},
					{
						 "render" : function(data, type, row, meta) {
							var dom = '';
							if(row.isAllow=='1'){
								dom +='<span class="label label-success radius">是</span>';
							}else{
								dom +='<span class="label radius">否</span>';
							}
							return dom;
						},
						"targets" : 6
					},
					{
					"render" : function(data, type, row, meta) {
						var dom = '';
						dom += '<a title="编辑" href="javascript:void(0)" onclick="prizeEdit(\'' + row.prizeId + '\')" class="ml-5" style="text-decoration:none">';
						dom += '<i class="Hui-iconfont f-18">&#xe692;</i></a>';
						
						return dom;
					},
					//指定是第三列
					"targets" :7
				} ]
	});

});


/*添加新的抽奖奖品*/
function prizeAdd() {
	var url = '/lotteryPrize/toPrizeEdit.do' + '?exType=ADD';
	layer_show('添加报读抽奖奖品', url, null, 610, function() {
		myDataTable.fnDraw(true);
	});
}
/*编辑新的抽奖奖品*/
function prizeEdit(prizeId) {
	var url = '/lotteryPrize/toPrizeEdit.do' + '?prizeId='+ prizeId +'&exType=UPDATE';
	layer_show('修改报读抽奖奖品', url, null, 610, function() {
		myDataTable.fnDraw(false);
	});
}

function searchLottery(){
	myDataTable.fnDraw(true);
}		