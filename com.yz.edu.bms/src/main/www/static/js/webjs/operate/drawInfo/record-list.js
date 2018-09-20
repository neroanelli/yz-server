var myDataTable;
$(function() {
	var arr = [];
	var barWidth;

    myDataTable = $('.table-sort').dataTable(
        {
            "serverSide" : true,
            "dom" : 'rtilp',
            "ajax" : {
                url : "/drawInfo/recordList.do",
                data : {
                    "lotteryName" : function(){
                        return $("#lotteryName").val();
                    },
                    "realName" : function(){
                        return $("#realName").val();
                    },
                    "mobile" : function(){
                        return $("#mobile").val();
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
                "mData" : "lotteryName"
            },{
                "mData" : "prizeName"
            }, {
                "mData" : "realName"
            }, {
                "mData" : "mobile"
            }, {
                "mData" : "winningTime"
            }]
        });

});


	function _search() {
		myDataTable.fnDraw(true);
	}


