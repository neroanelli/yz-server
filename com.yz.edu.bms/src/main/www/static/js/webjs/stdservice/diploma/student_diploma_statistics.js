		var myDataTable;
		$(function() {
			$('select').select2({
				placeholder : "--请选择--",
				allowClear : true,
				width : "59%"
			});
			
		  	_simple_ajax_select({
				selectId : "taskId",
				searchUrl : '/studyActivity/findTaskInfo.do?taskType=15',
				sData : {},
				showText : function(item) {
					return item.task_title;
				},					
				showId : function(item) {
					return item.task_id;
				},
				placeholder : '--请选择任务--'
			});
			$("#taskId").append(new Option("", "", false, true));
			
			myDataTable = $('.table-sort').dataTable({
				"processing": true,
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : '/diploma/statistics.do',
					type : "post",
					data : function(pageData){
                        pageData = $.extend({},{start:pageData.start, length:pageData.length},$("#export-form").serializeObject());
                        return pageData;
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
					{"mData" : "taskTitle"},
					{"mData" : "tutor"},
					{"mData" : "finishCount"},
					{"mData" : "unfinishedCount"},
					{"mData" :  null},
					{"mData" : "reason1"},
					{"mData" : "reason2"},
					{"mData" : "reason3"},
					{"mData" : "reason4"},
					{"mData" : "reason5"}
				],
				"columnDefs" : [
					{"targets" : 4,"render" : function(data, type, row, meta) {
						var finishRate=((row.finishCount/row.total)*100).toFixed(2)+ '%';
						return finishRate;
					}},
				],
				"footerCallback": function ( tfoot, data, start, end, display ) {
					if(data.length>0){
	                    $(tfoot).show(0)
	                    var len=$(tfoot).find('th').length;
	                    var countAll=0;
	                    var countFinishALL=0;
	                    var countUnFinishALL=0;
	                    var countReason1ALL=0
	                    var countReason2ALL=0
	                    var countReason3ALL=0
	                    var countReason4ALL=0
	                    var countReason5ALL=0
	                    data.forEach(function (e,i) {
	                        countAll+=e.total;
	                        countFinishALL+=e.finishCount;
	                        countUnFinishALL+=e.unfinishedCount;
	                        countReason1ALL+=e.reason1;
	                        countReason2ALL+=e.reason2;
	                        countReason3ALL+=e.reason3;
	                        countReason4ALL+=e.reason4;
	                        countReason5ALL+=e.reason5;
	                        
	                    });

	                    var finishRate=((countFinishALL/countAll)*100).toFixed(2)+ '%';
	                    var len=$(tfoot).find('th').length;
	                    $(tfoot).find('th').eq(len-8).html(countFinishALL);
	                    $(tfoot).find('th').eq(len-7).html(countUnFinishALL);
	                    $(tfoot).find('th').eq(len-6).html(finishRate);
	                    $(tfoot).find('th').eq(len-5).html(countReason1ALL);
	                    $(tfoot).find('th').eq(len-4).html(countReason2ALL);
	                    $(tfoot).find('th').eq(len-3).html(countReason3ALL);
	                    $(tfoot).find('th').eq(len-2).html(countReason4ALL);
	                    $(tfoot).find('th').eq(len-1).html(countReason5ALL);
	                }
				}
			});
		});


		/*搜素*/
		function searchResult(){
			myDataTable.fnDraw(true);
		}
		
		