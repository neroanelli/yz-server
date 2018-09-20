var myDataTable;
    $(function () {


        //初始化数据表格
        myDataTable = $("#textbook").dataTable({
            "processing": true,
            "serverSide": true,
            "dom": 'rtilp',
            "ajax": {
                url: "/tutorship/findAllTextbook.do",
                type: "post",
                data: {
                    "stdName": function () {
                        return $("#stdName").val();
                    },
                    "mobile": function () {
                        return $("#mobile").val();
                    },
                    "idCard": function () {
                        return $("#idCard").val();
                    },
                    "logisticsNo": function () {
                        return $("#logisticsNo").val();
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
            }, {
                "mData" : null
            },{
                "mData" : null
            },{
                "mData" : null
            },{
                "mData" : null
            },{
                "mData" : null
            } ],
            "columnDefs": [{
                "render" : function(data, type, row, meta) {
               	 return _findDict("grade", row.grade);
               },
               "targets" : 1
           },{
               "render" : function(data, type, row, meta) {
               	return _findDict("stdStage", row.stdStage);
               },
               "targets" : 2
           }, {
               "render" : function(data, type, row, meta) {
               	return _findDict("semester", row.semester);
               },
               "targets" : 3
           }, {
               "render" : function(data, type, row, meta) {
               	return row.fempName;
               },
               "targets" : 4
           }, {
               "render" : function(data, type, row, meta) {
               	 var dom = "";
                    for (var i = 0; i < row.books.length; i++) {
                        var book = row.books[i];

                        dom += book.textbookId + " " + book.textbookName + "<br>";
                    }
                    return dom;
               },
               "targets" : 5
           }, {
               "render" : function(data, type, row, meta) {
               	return row.batchId;
               },
               "targets" : 6
           }, {
               "render" : function(data, type, row, meta) {
               	var txt='';
                   if(_findDict("orderBookStatus", row.orderBookStatus)=='已发'){
                       txt+='<span class="label label-success radius">已发</span>'
                    	var dateTime = row.sendDate;
                    	if(dateTime!=undefined && dateTime!=""){
                    		var date = dateTime.substring(0, 10);
    						var time = dateTime.substring(11);
                           txt+='<br/>'+date + '<br>' + time;
                    	}
                   }else if(_findDict("orderBookStatus", row.orderBookStatus)=='未定'){
                       txt+='<span class="label radius">未订</span>'
                   }else {
                       txt+='<span class="label label-secondary  radius">已订未发</span>'
                   }
                   return txt
               },
               "targets" : 7,
               "class":"text-l"
           }, {
               "render" : function(data, type, row, meta) {
                   return row.addressUpdateTime;
               },
               "targets" : 8
           },{
               "render" : function(data, type, row, meta) {
               	return _findDict("logisticsName", row.logisticsName);						
               },
               "targets" : 9
           }, {
               "render" : function(data, type, row, meta) {
               	var dom = '';
					if(row.logisticsNo != null && row.logisticsNo != ''){
						
						if(!!row.logisticsName&&row.logisticsName.indexOf("jd")>=0){
							dom += '<a href="http://www.jdwl.com/order/search?waybillCodes='+row.logisticsNo+'" target="_blank" class="tableBtn normal" title="查看物流信息">' + row.logisticsNo + '</a>';
						}else{
							dom += '<a href="http://www.sf-express.com/cn/sc/dynamic_function/waybill/#search/bill-number/'+row.logisticsNo+'" target="_blank" class="tableBtn normal" title="查看物流信息">' + row.logisticsNo + '</a>';
						}
					}
					return dom;
               },
               "targets" : 10,
               "class":"text-l"
           }, {
               "render" : function(data, type, row, meta) {
               	var dom = '';

                   if ("4" == row.addressStatus) {
                       dom = '<label class="label label-secondary radius">待审核</label>';
                   }
                   if ("5" == row.addressStatus) {
                       dom = '<label class="label label-success radius">审核通过</label>';
                   }
                   if ("6" == row.addressStatus) {
                   	var rejectUser,rejectTime,rejectReason;
                       rejectUser=row.rejectUser?row.rejectUser:'无';
                       rejectTime=row.rejectTime?new Date(row.rejectTime).format('yyyy-MM-dd'):'无';
                       rejectReason=row.rejectReason?row.rejectReason:'无';
                       var rejecTxt="驳回人："+rejectUser+"&#10;"+"驳回时间："+rejectTime+"&#10;"+"驳回原因："+rejectReason;
                       dom = '<label class="label label-danger radius" style="cursor: pointer" title="'+rejecTxt+'">驳回</label>';
                   }
                   return dom;
               },
               "targets" : 11,
               "class":"text-l"
           }, {
               "render" : function(data, type, row, meta) {
            	   var dom=""
            	   if(row.provinceName!="" && row.provinceName!=null){
            		   dom+= row.provinceName;
            	   }
            	   if(row.cityName!="" && row.cityName!=null){
            		   dom+= row.cityName; 
            	   }
                   return dom;
               },
               "targets" : 12
           }, {
               "render" : function(data, type, row, meta) {
                   return row.address;
               },
               "targets" : 13
           }, {
               "render" : function(data, type, row, meta) {
                   return row.mobile;
               },
               "targets" : 14
           }, {
               "render" : function(data, type, row, meta) {
            	   if(!row.sendDate){
                       return '-'
                   }
                   var date=row.sendDate.substring(0,10)
                   var time=row.sendDate.substring(11)
                   return date+'<br>'+time
               },
               "targets" : 15
           } 
            ]
        });

    });

    function _search() {
        myDataTable.fnDraw(true);
    }