var myDataTable;
$(function() {
	var arr = [];
	var barWidth;

myDataTable = $('.table-sort').dataTable({
"processing": true,
"serverSide" : true,
"dom" : 'rtilp',
"ajax" : {
	url : "/drawInfo/ticketList.do",
	type : "post",
	data:{
		"userName" : function() {
			return $("#userName").val();
		},"mobile" : function() {
			return $("#mobile").val();
		},"startTime" : function() {
			return $("#startTime").val();
		},"endTime" : function() {
			return $("#endTime").val();
		},"lotteryName" : function(){
			return $("#lotteryName").val();
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
			"mData" : "ticketId"
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
		}, {
			"mData" : null
		} ],
"columnDefs" : [
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
		"targets" : 1
	},
	{
		"render" : function(data, type,row, meta) {
			var text='';
			var startTime = row.startTime;
			var endTime = row.endTime;
			if(!startTime){
				startTime = " "
			}else{
				var date=startTime.substring(0,10)
				var time=startTime.substring(11,19)
				startTime = date+' '+time
			}
			
			if(!endTime){
				endTime = " "
			}else{
				var date=endTime.substring(0,10)
				var time=endTime.substring(11,19)
				endTime = date+' '+time
			}
			text+='活动名称：'
			text+=row.lotteryName+'<br>'
			text+='活动时间：'
			text+=startTime+" -- "+endTime
			return text;
		},
		"sClass":'text-l',
		"targets" : 2
	},
	{
		"render" : function(data, type,row, meta) {
			var userName ="";
			var mobile = "";
			var yzCode = "";
			if(row.yzCode!=null){
				yzCode = row.yzCode
			}
			if(row.userName!=null){
				userName = row.userName;
			}
			if(row.mobile!=null){
				mobile = row.mobile;
			}
			var text='';
			text+='用户姓名：'
			text+=userName+'<br>'
			text+='用户电话：'
			text+=mobile+'<br>'
			text+='用户远智编码：'
			text+=yzCode
			return text
		},
		"sClass":'text-l',
		"targets" : 3
	},
	{
		"render" : function(data, type,row, meta) {
			var triggerUserName ="";
			var triggerUserMobile = "";
			var triggerYzCode = "";
			if(row.triggerYzCode!=null){
				triggerYzCode = row.triggerYzCode
			}
			if(row.triggerUserName!=null){
				triggerUserName = row.triggerUserName;
			}
			if(row.triggerUserMobile!=null){
				triggerUserMobile = row.triggerUserMobile;
			}
			var text='';
			text+='触发人姓名：'
			text+=triggerUserName+'<br>'
			text+='触发人电话：'
			text+=triggerUserMobile+'<br>'
			text+='触发人远智编码：'
			text+=triggerYzCode
			return text
		},
		"sClass":'text-l',
		"targets" : 4
	},
	{
		"render" : function(data, type,row, meta) {
			if(row.isUsed=="0"){
				return "未使用"
			}else if(row.isUsed=="1"){
				return "已使用";
			}
			return ""
		},
		"targets" : 5
	},
	{
		"render" : function(data, type,row, meta) {
			var usedTime = row.usedTime;
			if(!usedTime){
				return '-'
			}
			var date=usedTime.substring(0,10)
			var time=usedTime.substring(11,19)
			return date+'<br>'+time
		},
		"targets" : 6
	} ]
	});

});


	function _search() {
		myDataTable.fnDraw(true);
	}
	function uuid() {
    return Math.random().toString(36).substring(3, 8)
}
	
	function bdStudentOutExport() {
        $("#export-form").submit();
    }
