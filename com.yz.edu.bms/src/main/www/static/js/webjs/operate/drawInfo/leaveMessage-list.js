var myDataTable;
$(function() {
	var arr = [];
	var barWidth;

myDataTable = $('.table-sort').dataTable({
"processing": true,
"serverSide" : true,
"dom" : 'rtilp',
"ajax" : {
	url : "/drawInfo/leaveMessageList.do",
	type : "post",
	data:{
		"realName" : function() {
			return $("#realName").val();
		},"mobile" : function() {
			return $("#mobile").val();
		}
	}
},
"pageLength" : 10,
"pagingType" : "full_numbers",
"ordering" : false,
"searching" : false,
"fnDrawCallback": function() { 
	for(var i =0;i<arr.length;i++){
		if(!barWidth)
			barWidth = $("#"+arr[i].id).width();
		var progress = new Progress({
	      bar : {},
	      item : {},
	      barWidth : 0,
	      itemCount : 2,
	      itemWidth : 0,
	      processWidth : 0,
	      curProcessWidth : 0,
	      step : 1,
	      curStep : 0,
	      triggerStep : 1,
	      change : false,
	      animation : false,
	      speed : 500,
	      stepEasingForward : "easeOutCubic",
	      stepEasingBackward : "easeOutElastic",
	      width : 504},arr[i].id,{
					step : arr[i].steps,
					animation : true,
					change : true,
					isCheck : arr[i].isCheck,
					width : barWidth
				});
		progress.init();
	}
}, 
"createdRow" : function(row, data, dataIndex) {
	$(row).addClass('text-c');
},
"language" : _my_datatables_language,
columns : [ {
	"mData" : "realName"
}, {
	"mData" : "mobile"
}, {
	"mData" : "nickname"
}, {
	"mData" : null
}, {
	"mData" : "msgContent"
}, {
	"mData" : null
}, {
	"mData" : null
}, {
	"mData" : null
} ],
"columnDefs" : [
{
	 "render" : function(data, type, row, meta) {
		 var scholarship= _findDict('scholarship', row.scholarship);
		return scholarship;
	},
	"targets" : 3
},
{
"render" : function(data, type,row, meta) {
	var dom = '';
	if(row.isShow=="0"){
		dom +='<span class="label radius">不显示</span>';
	}else if(row.isShow=="1"){
		dom +='<span class="label label-success radius">显示</span>';
	}
	return dom
},
"targets" : 5
},
{
"render" : function(data, type,row, meta) {
	var createTime = row.createTime;
	if(!createTime){
		return '-'
	}
	var date=createTime.substring(0,10)
	var time=createTime.substring(11,19)
	return date+'<br>'+time
},
"targets" : 6
}
,{
"render" : function(data, type, row, meta) {
	var dom='';
	if('1' == row.isShow) {
		dom += '<a title="不显示" href="javascript:;" onclick="enable(\'' + row.id+ '\',\'0\')" class="ml-5" style="text-decoration: none">';
		dom += '<i class="iconfont icon-tingyong"></i></a>';
	} else {
		dom += '<a title="显示" href="javascript:;" onclick="enable(\'' + row.id + '\',\'1\')" class="ml-5" style="text-decoration: none">';
			dom += '<i class="iconfont icon-wancheng"></i></a>';
	}
	return dom;
},
//指定是第10列是操作列
"targets" : 7
	} ]
	});

});


	function _search() {
		myDataTable.fnDraw(true);
	}
	
	function enable(id,isShow) {
		$.ajax({
			type : 'POST',
			url : '/drawInfo/updateLeaveMessage.do',
			data : {
				id : id,
				isShow:isShow
			},
			dataType : 'json',
			success : function(data) {
				if ('00' == data.code) {
					var text = '操作成功';
					layer.msg(text, {
						icon : 1,
						time : 1000
					}, function(){myDataTable.fnDraw(false);});
				}
			},
		});
	}
