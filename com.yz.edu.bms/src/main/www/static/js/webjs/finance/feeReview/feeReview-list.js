 		_init_select("paymentType", dictJson.paymentType, null);
        _init_select("recruitType",dictJson.recruitType,null);
        _init_select('grade', dictJson.grade);
        $("#recruitType").change(function() {
	          _init_select({
	            selectId : 'grade',
	            ext1 : $(this).val()
	          }, dictJson.grade);
	    });
        _init_select('serialStatus', [
                                      {"dictValue" : "3","dictName" : "未审核"},
                                      {"dictValue" : "2","dictName" : "已审核"}
                                  ]);
        //初始校区下拉框
        _init_campus_select("campusId", "dpId", "groupId", '/campus/selectAllList.do', '/dep/selectAllList.do', '/group/selectAllList.do');
        var myDataTable;
      
        $(function () {
        	
            myDataTable = $('.table-sort').dataTable(
                {
                    "processing": true,
                    "serverSide": true,
                    "dom": 'rtilp',
                    "ajax": {
                        url: "/feeReview/list.do",
                        type: "post",
                        data: function (data) {
                            return searchData(data);
                        }
                    },
                    "pageLength": 10,
                    "pagingType": "full_numbers",
                    "ordering": false,
                    "searching": false,
                    "drawCallback": function( settings ) {
                    	//countAmount();
                    },
                    "createdRow": function (row, data,
                                            dataIndex) {
                        $(row).addClass('text-c');
                    },
                    "language": _my_datatables_language,
                    columns: [{
                        "mData": null
                    }, {
                        "mData": null
                    }, {
                        "mData": null
                    }, {
                        "mData": "amount"
                    }, {
                        "mData": null
                    }, {
                        "mData": null
                    }, {
                        "mData": null
                    }, {
                        "mData": null
                    }, {
                        "mData": "orderNo"
                    }, {
                        "mData": "outSerialNo"
                    }, {
                        "mData": null
                    }, {
                        "mData": null
                    }, {
                        "mData": null
                    }, {
                        "mData": "empName"
                    }, {
                        "mData": null
                    }, {
                        "mData": "checkUser"
                    }, {
                        "mData": null
                    }], //[成教]汕头大学:(0003)会计学[专升本]
                    "columnDefs": [
                        {
                            "render": function (data, type,
                                                row, meta) {
                                var dom = '';
                                var payInfos = row.payInfos;
                                for (var i = 0; i < payInfos.length; i++) {
                                    var payInfo = payInfos[i];
                                    dom += payInfo.itemCode + ':';
                                    
                                    var itemName = getItemName(payInfo.itemName,row.grade);
                                    
                                    dom += itemName;
                                    if (i != (payInfos.length - 1)) {
                                        dom += '</br>';
                                    }
                                }

                                return dom;
                            },
                            "targets": 2
                        }, {
                            "render": function (data, type, row, meta) {
                                return '<a onclick="toEidt(\''+ row.learnId +'\',\''+ row.stdId +'\',\''+ row.recruitType+'\')">'+ row.stdName +'</a>';
                            },
                            "targets": 1
                        }, {
                            "render": function (data, type, row, meta) {
                            	var zmDeduction = row.zmDeduction;
                            	if(null != zmDeduction){
                            		return zmDeduction;
                            	}else{
                            		return "0";
                            	}
                            },
                            "targets": 4
                        }, {
                            "render": function (data, type, row, meta) {
                            	var accDeduction = row.accDeduction;
                            	if(null != accDeduction){
                            		return accDeduction;
                            	}else{
                            		return "0";
                            	}
                            },
                            "targets": 5
                        }, {
                            "render": function (data, type, row, meta) {
                            	var couponDeduction = row.couponDeduction;
                            	if(null != couponDeduction){
                            		return couponDeduction;
                            	}else{
                            		return "0";
                            	}
                            },
                            "targets": 6
                        }, {
                            "render": function (data, type, row, meta) {

                                var dateTime= row.payTime;
                                if(!dateTime){
                                    return '-'
                                }
                                var date=dateTime.substring(0,10)
                                var time=dateTime.substring(11)
                                return date+'<br>'+time
                            },
                            "targets": 7,"class":"text-l no-warp"
                        },{
                            "render": function (data, type, row, meta) {
                                var dom = '';
                                dom += '<input type="checkbox" value="' + row.serialNo + '" name="serialNos"/>';

                                return dom;
                            },
                            "targets": 0
                        }, {
                            "render": function (data, type, row, meta) {
                                var dom = '';
                                dom += _findDict("stdStage", row.stdStage) + '(';
                                dom += row.grade + '级)'
                                return dom;
                            },
                            "targets": 2
                        }, {
                            "render": function (data, type, row, meta) {

                                return _findDict("paymentType", row.paymentType);
                            },
                            "targets": 10
                        }, {
                            "render": function (data, type, row, meta) {
								
                                return row.payeeName;
                            },
                            "targets": 11
                        }, {
                            "render": function (data, type, row, meta) {
                                var dom = '';
                                if ('3' == row.serialStatus) {
                                    dom += '<a title="审核" class="tableBtn normal" onclick="reviewFee(\'' + row.serialNo + '\')">审核</a>';
                                } else if ('2' == row.serialStatus) {
                                    dom += '<a title="撤销审核" class="tableBtn blue" onclick="backReviewFee(\'' + row.serialNo + '\')">撤销审核</a>';
                                } else {
                                    dom += '';
                                }
                                return dom;
                            },
                            "targets": 16
                        }, {
                            "render": function (data, type, row, meta) {
                                var status = row.serialStatus;
                                if ('3' == status) {
                                    return '否';
                                } else {
                                    return '是';
                                }
                            },
                            "targets": 14
                        },
                        {
							"render" : function(data, type,
									row, meta) {
								var dom = '';
								dom += '<a title="学员账户" class="tableBtn normal"  type="button" onclick="toAccount(\''+ row.stdId + '\')">学员账户</a>';
								return dom;
							},
							"targets" : 12
						}]
                });
            
        })
        function searchReview() {
            myDataTable.fnDraw(true);
            countAmount();
        }
        
        function toAccount(stdId) {
			var url = '/stdFee/toAccount.do' + '?stdId=' + stdId;
			layer_show('学员账户', url, null, null, function() {
//				myDataTable.fnDraw(false);
			}, true);
		}
        
        function countAmount(){
        	$.ajax({
                type: 'POST',
                url: '/feeReview/count.do',
                data: {
                	orderNo: function (){
                		return $("#orderNo").val() ? $("#orderNo").val() : '';	
                	},
                	serialNo: function (){
                		return $("#serialNo").val() ? $("#serialNo").val() : '';	
                	},
                	outSerialNo: function (){
                		return $("#outSerialNo").val() ? $("#outSerialNo").val() : '';	
                	},
                	stdName: function (){
                		return $("#stdName").val() ? $("#stdName").val() : '';	
                	},
                	idCard: function (){
                		return $("#idCard").val() ? $("#idCard").val() : '';	
                	},
                	mobile: function (){
                		return $("#mobile").val() ? $("#mobile").val() : '';	
                	},
                	paymentType: function (){
                		return $("#paymentType").val() ? $("#paymentType").val() : '';	
                	},
                	serialStatus: function (){
                		return $("#serialStatus").val() ? $("#serialStatus").val() : '';	
                	},
                	startTime: function (){
                		return $("#startTime").val() ? $("#startTime").val() : '';	
                	},
                	endTime: function (){
                		return $("#endTime").val() ? $("#endTime").val() : '';	
                	},
                	recruitType : function (){
                		return $("#recruitType").val() ? $("#recruitType").val() : '';
                	},
                	grade : function (){
                		return $("#grade").val() ? $("#grade").val() : '';
                	},
                	campusId : function (){
                		return $("#campusId").val() ? $("#campusId").val() : '';
                	},
                	dpId : function(){
                		return $("#dpId").val() ? $("#dpId").val() : '';
                	},
                	recruitName : function (){
                		return $("#recruitName").val() ? $("#recruitName").val() : '';
                	}
                },
                dataType: 'json',
                success: function (data) {
                    if (data.code == _GLOBAL_SUCCESS) {
                        $("#countAmount").text(data.body);
                    }
                }
        	});
        }

        function searchData(data) {
            return {
                orderNo: $("#orderNo").val() ? $("#orderNo").val() : '',
                serialNo: $("#serialNo").val() ? $("#serialNo").val() : '',
                outSerialNo: $("#outSerialNo").val() ? $("#outSerialNo").val() : '',
                stdName: $("#stdName").val() ? $("#stdName").val() : '',
                idCard: $("#idCard").val() ? $("#idCard").val() : '',
                mobile: $("#mobile").val() ? $("#mobile").val() : '',
                paymentType: $("#paymentType").val() ? $("#paymentType").val() : '',
                serialStatus: $("#serialStatus").val() ? $("#serialStatus").val() : '',
                startTime: $("#startTime").val() ? $("#startTime").val() : '',
                endTime: $("#endTime").val() ? $("#endTime").val() : '',
                recruitType: $("#recruitType").val() ? $("#recruitType").val() : '',
                grade: $("#grade").val() ? $("#grade").val() : '',
                campusId :$("#campusId").val() ? $("#campusId").val() : '',
                dpId :$("#dpId").val() ? $("#dpId").val() : '',
                recruitName : $("#recruitName").val() ? $("#recruitName").val() : '',		
                start: data.start,
                length: data.length
            };
        }

        var bool = false;  //加个锁
        function reviewFee(serialNo) {
            layer.confirm('确认审核通过？', function (index) {
            	if(!bool){
            		bool = true;//锁住
            		$.ajax({
                        type: 'POST',
                        url: '/feeReview/reviewFee.do',
                        data: {
                            serialNo: serialNo
                        },
                        dataType: 'json',
                        success: function (data) {
                            if (data.code == _GLOBAL_SUCCESS) {
                                layer.msg('审核通过!', {
                                    icon: 1,
                                    time: 1000
                                });
                                bool = false;
                                myDataTable.fnDraw(false);
                            }
                        }
                    });
            	}
               
            });
        }

        function backReviewFee(serialNo) {
            layer.confirm('确认撤销审核？', function (index) {
                $.ajax({
                    type: 'POST',
                    url: '/feeReview/rollBackReview.do',
                    data: {
                        serialNo: serialNo
                    },
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == _GLOBAL_SUCCESS) {
                            layer.msg('撤销成功!', {
                                icon: 1,
                                time: 1000
                            });
                            myDataTable.fnDraw(false);
                        }
                    }
                });
            });
        }


        function reviewFees() {
            var chk_value = [];
            var $input = $("input[name=serialNos]:checked");

            $input.each(function () {
                chk_value.push($(this).val());
            });
            if (chk_value == null || chk_value.length <= 0) {
                layer.msg('未选择任何数据!', {
                    icon: 5,
                    time: 1000
                });
                return;
            }
            layer.confirm('确认审核？', function (index) {
                $.ajax({
                    type: 'POST',
                    url: '/feeReview/reviewFees.do',
                    data: {
                        serialNos: chk_value
                    },
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == _GLOBAL_SUCCESS) {
                            layer.msg('审核成功!', {
                                icon: 1,
                                time: 1000
                            });
                            myDataTable.fnDraw(false);
                            $("input[name=all]").attr("checked", false);
                        }
                    }
                });
            });
        }

        function backReviews() {
            var chk_value = [];
            var $input = $("input[name=serialNos]:checked");

            $input.each(function () {
                chk_value.push($(this).val());
            });
            if (chk_value == null || chk_value.length <= 0) {
                layer.msg('未选择任何数据!', {
                    icon: 5,
                    time: 1000
                });
                return;
            }
            layer.confirm('确认撤回审核？', function (index) {
                $.ajax({
                    type: 'POST',
                    url: '/feeReview/rollBackReviews.do',
                    data: {
                        serialNos: chk_value
                    },
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == _GLOBAL_SUCCESS) {
                            layer.msg('审核成功!', {
                                icon: 1,
                                time: 1000
                            });
                            myDataTable.fnDraw(false);
                            $("input[name=all]").attr("checked", false);
                        }
                    }
                });
            });

        }
        
        /*用户-编辑*/
		function toEidt(learnId, stdId,recruitType) {
			var url = '/studentBase/toEdit.do' + '?learnId=' + learnId + '&stdId=' + stdId+"&recruitType="+recruitType;
			layer_show('学员信息', url, null, null, function() {
				myDataTable.fnDraw(false);
			}, true);
		}
        
		function toZMAccount(learnId,stdId){
			var url = '/stdFee/toZMAccount.do'+ '?learnId=' + learnId + '&stdId=' + stdId;
			layer_show('智米流水', url, null, null, function() {
//				myDataTable.fnDraw(true);
			}, true);
		}


        function excel_export() {

            	var startTime = $("#startTime").val();
            	var endTime = $("#endTime").val();
            	
            	
            	if(null == startTime || startTime == ''){
            		layer.msg('时间段不能为空！', {icon : 2, time : 1000},function(){
					});
            		return false;
            	}
            	
            	if(null == endTime || endTime == ''){
            		layer.msg('时间段不能为空！', {icon : 2, time : 1000},function(){
					});
            		return false;
            	}
            	
            $("#export-form").submit();

        }
        
        $("#export-form").validate({
    		rules : {
    			
    		},
    		onkeyup : false,
    		focusCleanup : true,
    		success : "valid",
    		submitHandler : function(form) {
    			layer.prompt({
                    title: '填写excel文件名：',
                    formType: 3
    			}, function (text, index) {
    				$('#excelName').val(text);
	    			$(form).ajaxSubmit({
	    				type : "post", //提交方式  
	    				dataType : "json", //数据类型  
	    				url : '/feeReview/export.do', //请求url  
	    				success : function(data) { //提交成功的回调函数  
	    					if(data.code == _GLOBAL_SUCCESS){
	    						layer.msg('导出处理中', {icon : 1, time : 1000},function(){
	    							
	    						});
	    					}
	    				}
	    			})
    			});
    		}
    	});