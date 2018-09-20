var myDataTable;
        $(function () {
        	
        	_init_select('recruitType', dictJson.recruitType);
        	_init_select('grade', dictJson.grade);
        	$("#recruitType").change(function () {
                _init_select({
                    selectId: 'grade',
                    ext1: $(this).val()
                }, dictJson.grade);
            });
        	
        	 //初始化专业层次下拉框
            _init_select("pfsnLevel", dictJson.pfsnLevel);
            //初始化院校名称下拉框
            _simple_ajax_select({
                selectId: "unvsId",
                searchUrl: '/bdUniversity/findAllKeyValue.do',
                sData: {},
                showText: function (item) {
                    return item.unvs_name;
                },
                showId: function (item) {
                    return item.unvs_id;
                },
                placeholder: '--请选择--'
            });
            $("#unvsId").append(new Option("", "", false, true));
            $("#unvsId").change(function () {
                $("#pfsnId").removeAttr("disabled");
                init_pfsn_select();
            });
            $("#grade").change(function () {
                $("#pfsnId").removeAttr("disabled");
                init_pfsn_select();
            });
            $("#pfsnLevel").change(function () {
                $("#pfsnId").removeAttr("disabled");
                init_pfsn_select();
            });
            $("#pfsnId").append(new Option("", "", false, true));
            $("#pfsnId").select2({
                placeholder: "--请先选择院校--"
            });

            //收费方式
            _init_select("paymentType", dictJson.paymentType);
            
          //初始审批状态
            _init_select("checkStatus",[
                {"dictValue":"1","dictName":"是"},
                {"dictValue":"0","dictName":"否"}]);

            myDataTable = $('.table-sort').dataTable(
                {
                    "serverSide": true,
                    "dom": 'rtilp',
                    "ajax": {
                        url: "/imageCapture/list.do",
                        data: {
                            "unvsId": function () {
                                return $("#unvsId").val();
                            }, "recruitType": function () {
                                return $("#recruitType").val();
                            }, "grade": function () {
                                return $("#grade").val();
                            }, "pfsnId": function () {
                                return $("#pfsnId").val();
                            }, "stdName": function () {
                                return $("#stdName").val();
                            }, "idCard": function () {
                                return $("#idCard").val();
                            }, "mobile": function () {
                                return $("#mobile").val();
                            }, "pfsnLevel": function () {
                                return $("#pfsnLevel").val();
                            }, "checkStatus": function () {
                                return $("#checkStatus").val();
                            }, "paymentType": function () {
                                return $("#paymentType").val();
                            }, "orderNo": function () {
                                return $("#orderNo").val();
                            }, "outSerialNo": function () {
                                return $("#outSerialNo").val();
                            }, "payStartTime": function () {
                                return $("#payStartTime").val();
                            }, "payEndTime": function () {
                                return $("#payEndTime").val();
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
                        "mData": "stdName"
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
                        "mData": "orderNo"
                    }, {
                        "mData": "outSerialNo"
                    }, {
                        "mData": null
                    }, {
                        "mData": null
                    }, {
                        "mData": "checkUser"
                    }, {
                        "mData": null   	
                    }],
                    "columnDefs": [
                        {
                            "render": function (data, type, row, meta) {
                                return '<input type="checkbox" value="' + row.orderNo + '" name="orderNos"/>';
                            },
                            "targets": 0
                        },
                        {
                            "render": function (data, type, row, meta) {
                            	var dom =row.stdName;
                                if(row.stdType ==='2'){
                                    dom += ' <sup style="color:#f00">外</sup>';
                                }
                                return '<a class="stuName" onclick="toEidt(\''+ row.learnId +'\',\''+ row.stdId +'\',\''+ row.recruitType +'\')">'+ dom +'</a>';
                            },
                            "targets": 1
                        },
                        {
                            "render": function (data, type, row, meta) {
                                return row.grade + '级';
                            },
                            "targets": 2
                        },
                        {
                            "render": function (data, type,
                                                row, meta) {
                                var dom = '';
                                dom += row.unvsName+"<br>";
                                dom+=_findDict("pfsnLevel", row.pfsnLevel).indexOf("高中")!=-1?"[专科]":"[本科]";
                                dom += row.pfsnName;
                                dom += '(' + row.pfsnCode + ')';
                                return dom;
                            },
                            "targets": 3,"class":"text-l"
                        },
                        {
                            "render": function (data, type, row, meta) {
                            	var payItem = _findDict('payItem', row.payItem);
                                return payItem;
                            },
                            "targets": 4
                        },
                        {
                            "render": function (data, type, row, meta) {
                            	var amount= row.amount;
                                if(amount!=null && amount!="0.00" && amount!="0"){
                                    return amount;
                                }
                                return "-";
                            },
                            "targets": 5
                        },
                        {
                            "render": function (data, type, row, meta) {
                            	var payTime = row.payTime;
                                if(!payTime){
                                    return '-'
                                }
                                var date=payTime.substring(0,10)
                                var time=payTime.substring(11,19)
                                return date+'<br>'+time
                            },
                            "targets": 6
                        },
                        {
                            "render": function (data, type, row, meta) {
                            	var paymentType= _findDict('paymentType', row.paymentType);
                                return paymentType;
                            },
                            "targets": 9
                        },
                        {
                            "render": function (data, type, row, meta) {
                            	var dom = '';
                            	if(row.checkStatus=="0"){
                            		dom +='<span class="label radius">否</span>';
                            	}else if(row.checkStatus=="1"){
                            		dom +='<span class="label label-success radius">是</span>';
                            	}
                            	return dom
                            },
                            "targets": 10
                        }, {
                            "render": function (data, type, row, meta) {
                            	var checkStatus= row.checkStatus;
                                var dom = '';
                                if ('0' == row.checkStatus) {
                                	dom += '<a title="审核" class="tableBtn blue" onclick="reviewCheck(\'' + row.orderNo + '\')">审核</a>';
                                } 
                                return dom;
                            },
                            "targets": 12
                        }
                    ]
                });

        });

        function searchDep() {
            myDataTable.fnDraw(true);
        }

        
        var bool = false;  //加个锁
        function reviewCheck(orderNo) {
            layer.confirm('确认审核通过？', function (index) {
            	if(!bool){
            		bool = true;//锁住
            		$.ajax({
                        type: 'POST',
                        url: '/imageCapture/reviewCheck.do',
                        data: {
                        	orderNo: orderNo
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
        //审核
        function reviewFees() {
            var chk_value = [];
            var $input = $("input[name=orderNos]:checked");

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
                    url: '/imageCapture/reviewFees.do',
                    data: {
                    	orderNos: chk_value
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

        
        function imageCaptureExport() {
            $("#export-form").submit();
        }
        
        function init_pfsn_select() {
            _simple_ajax_select({
                selectId: "pfsnId",
                searchUrl: '/baseinfo/sPfsn.do',
                sData: {
                    sId: function () {
                        return $("#unvsId").val() ? $("#unvsId").val() : '';
                    },
                    ext1: function () {
                        return $("#pfsnLevel").val() ? $("#pfsnLevel").val() : '';
                    },
                    ext2: function () {
                        return $("#grade").val() ? $("#grade").val() : '';
                    }
                },
                showText: function (item) {
                    var text = '(' + item.pfsnCode + ')' + item.pfsnName;
                    text += '[' + _findDict('pfsnLevel', item.pfsnLevel) + ']';
                    return text;
                },
                showId: function (item) {
                    return item.pfsnId;
                },
                placeholder: '--请选择专业--'
            });
            $("#pfsnId").append(new Option("", "", false, true));
        }
        
        /*查看学员详细信息*/
        function toEidt(learnId, stdId ,recruitType) {
            var url = '/studentBase/toEdit.do' + '?learnId=' + learnId + '&stdId=' + stdId + '&t=MODI&recruitType='+recruitType;
            layer_show('学员信息', url, null, null, function() {
                //myDataTable.fnDraw(true);
            });
        }