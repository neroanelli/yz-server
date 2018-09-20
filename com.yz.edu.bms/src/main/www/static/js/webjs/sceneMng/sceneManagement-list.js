var myDataTable;
var examDicJson;
    $(function () {
    	

    	
    	//初始考试区县下拉框
		$.ajax({
			type: "POST",
			dataType : "json", //数据类型
			url: '/sceneManagement/getExamDicName.do',
			success: function(data){
				examDicJson = data.body;
				if(data.code=='00'){
					_init_select("taId",examDicJson);
				}
			}
		});
    	 //初始状态
        _init_select("isAllow", [
            {
                "dictValue": "1", "dictName": "已启用"
            },
            {
                "dictValue": "2", "dictName": "已禁用"
            }
        ]);
        
        _init_select("confirmAddressLevel", [
                                 {
                                     "dictValue": "1", "dictName": "专升本"
                                 },
                                 {
                                     "dictValue": "5", "dictName": "高起专"
                                 },
                                 {
                                     "dictValue": "3", "dictName": "高起专/专升本"
                                 }
                             ]);
        
        _init_select("isFull", [
                                 {
                                     "dictValue": "1", "dictName": "已约满"
                                 },
                                 {
                                     "dictValue": "2", "dictName": "未约满"
                                 }
                             ]);
        
        
        myDataTable = $('.table-sort').dataTable(
            {
                "processing": true,
                "serverSide": true,
                "dom": 'rtilp',      
                "ajax": {
                    url: "/sceneManagement/list.do",
                    type: "post",
                    data: {  
                        "taId": function () {
                            return $("#taId").val();
                        },
                        "confirmCity": function () {
                            return $("#confirmCity").val();
                        }
                        ,
                        "confirmName": function () {
                            return $("#confirmName").val();
                        }
                        ,
                        "startTime": function () {
                            return $("#startTime").val();
                        }
                        ,
                        "endTime": function () {
                            return $("#endTime").val();
                        }
                        ,
                        "isFull": function () {
                            return $("#isFull").val();
                        }
                        ,
                        "isAllow": function () {
                            return $("#isAllow").val();
                        }
                        ,
                        "confirmAddressLevel": function () {
                            return $("#confirmAddressLevel").val();
                        }
                    }
                },

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
                    "mData": null
                }, {
                    "mData": null
                }, {
                    "mData": "confirmName"
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
                    "mData": null
                }, {
                    "mData": null
                }, {
                    "mData": null
                }, {
                    "mData": "remark"
                }, {
                    "mData": null
                }],
                "columnDefs": [
                    {
                        "render": function (data, type, row, meta) {
                        	
                        	if(parseInt(row.availableNumbers)<parseInt(row.number)){
                        		 return '';
                        	}else{
                        		 return '<input type="checkbox" value="' + row.confirmationId + '" name="confirmationId"/>';
                        	}
                           
                        },
                        "targets": 0
                    },
                    {
                        "render": function (data, type, row, meta) {
                        	return row.taName;
                        },
                        "targets": 1
                    },
                    {
                        "render": function (data, type, row, meta) {
                            return row.confirmCity;
                        },
                        "targets": 2
                    },
                    {
                        "render": function (data, type, row, meta) {
                        	if(row.address){
                        		var dom  = '<div style="display:-webkit-box;height:40px; overflow:hidden; text-overflow:ellipsis; word-break:break-all; -webkit-box-orient:vertical; -webkit-line-clamp:2;">';
                        		dom += row.address;
                        		dom += '</div>';
                        		return dom;
   
                        	}else{
                        		return "";
                        	}
                            
                        },
                        "targets": 4
                    },
                    {
                        "render": function (data, type, row, meta) {
                        		if(row.confirmAddressLevel){
                        			if(row.confirmAddressLevel=='3'){
                        				return "高起专/专升本";
                        			}else if(row.confirmAddressLevel=='5'){
                        				return "高起专";
                        			}else if(row.confirmAddressLevel=='1'){
                        				return "专升本";
                        			}
                        		}
                        		return "--";
                        },
                        "targets": 5 
                    },
                   
                    {
                        "render": function (data, type, row, meta) {
                        	 	var dom  = '<div style="display:-webkit-box;height:78px;width:285px; padding-left:60px;overflow:hidden; text-overflow:ellipsis; word-break:break-all; -webkit-box-orient:vertical; -webkit-line-clamp:4;">';
                        		dom += row.requiredMaterials;
                        		dom += '</div>';
                        		return dom;
                        },
                      "targets": 6,
                      "class":"text-l"
                    } ,
                    {
                        "render": function (data, type, row, meta) {
                        	 	var dom  = '';
                        		if(row.chargePerson){
                        			dom += row.chargePerson;
                        		}
                        		if(row.chargePersonTel){
                        			dom += "</br>" + row.chargePersonTel;
                        		}
                        		return dom;
                        },
                        "targets": 7
                    },
                    {
                        "render": function (data, type, row, meta) {
                        	
							if(!row.startTime){
								return '-';
							}else{
								 var date = row.startTime.substring(0,11);
								 var day = new Date(Date.parse(date.replace(/-/g, '/')));
								 var today = new Array('星期日','星期一','星期二','星期三','星期四','星期五','星期六');
								 var week = today[day.getDay()];
								 return row.startTime.substring(0,11) + "</br>" + week;
							}
							
                        },
                        "targets": 8
                    },
                    {
                        "render": function (data, type, row, meta) {
							if(!row.startTime&&!row.endTime){
								return '-';
							}else{
								return row.startTime.substring(11,16)+ "-" + row.endTime.substring(11,16);
							}
                        },
                        "targets": 9
                    },
                    {
                        "render": function (data, type, row, meta) {
							if(!row.number&&!row.availableNumbers){
								return '-';
							}else{ 
								return "容量"+row.number+ "/余" + row.availableNumbers;
							}
                        },
                        "targets": 10
                    },{
                        "render": function (data, type, row, meta) {
                        	if(row.isAllow && row.isAllow =='1'){
                    			return "<label class='label label-success radius'>启用</label>";
                    		}else{
                    			return "<label class='label  label-danger radius'>禁用</label>";
                    		}
                        },
                        "targets": 11
                    },
                    {
                        "render": function (data, type, row, meta) {
                            var dom = ''; 
                            var allow = '启用';
							var no = '禁用';
							if(row.isAllow && row.isAllow =='2'){
								dom += '<a class="" href="javascript:;" title="启用" onclick="updateIsAllow(\'' + row.confirmationId + '\',1,\'' + allow + '\')"><i class="iconfont icon-qiyong"></i></a>&nbsp;';
							}else{
								dom += '<a class="" href="javascript:;" title="禁用" onclick="updateIsAllow(\'' + row.confirmationId + '\',2,\'' + no + '\')"><i class="iconfont icon-tingyong"></i></a>&nbsp;';
							}
							dom += '<a class="" href="javascript:;" title="修改" onclick="member_edit(\'' + row.confirmationId + '\')"><i class="iconfont icon-edit"></i></a>&nbsp;';
                            dom += '<a class="" href="javascript:;" title="删除" onclick="member_del(\'' + row.confirmationId + '\')"><i class="iconfont  icon-shanchu"></i></a>&nbsp;';
                            return dom;
                        },
                        "targets": 13
                    }]
            });

    });
    
    
    /**
	 * 修改   禁用和启用
	 * @param id
	 * @returns
	 */
	function updateIsAllow(id,isAllow,isAllowDes){
		
		layer.confirm('确认要' + isAllowDes + '吗？', function(index) {
			$.ajax({
				type : 'POST',
				url : '/sceneManagement/updateAllow.do',
				data : {
					confirmationId : id,
					status : isAllow
				},
				dataType : 'json',
				success : function(data) {
					if(data.body == "false"){
						layer.msg('该确认点已有学员预约，不可以禁用!', {
							icon : 1,
							time : 1000
						});
					}else{
						layer.msg('已' + isAllowDes + '!', {
							icon : 1,
							time : 1000
						});
						
						myDataTable.fnDraw(false);
					}
				},
				error : function(data) {
					layer.msg('' + isAllowDes + ' 失败！', {
						icon : 1,
						time : 1000
					});
					myDataTable.fnDraw(false);
				},
			});
		});
	}
    
    
    /*用户-添加*/
    function member_add() {
        var url = '/sceneManagement/editOrAdd.do' + '?tjType=ADD';
        layer_show('添加现场确认点', url,  null, 510, function () {
        },true);
    }

    /*用户-编辑*/
    function member_edit(id) {
    	
    	var url = '/sceneManagement/editOrAdd.do' + '?confirmationId=' + id + '&tjType=UPDATE';
        layer_show('修改现场确认点', url, null, 510, function () {
        },true);
    }

    /*用户-导入*/
    function excel_import() {
        var url = '/sceneManagement/toUploadSceneManagement.do';
        layer_show('导入现场确认点', url, 600, 300, function () {
            myDataTable.fnDraw(true);
        },true);
    }

    /*用户-删除*/
    function member_del(id) {
        layer.confirm('确认要删除吗？', function (index) {
            $.ajax({
                type: 'POST',
                url: '/sceneManagement/delete.do',
                data: {
                	confirmationId: id
                },
                dataType: 'json',
                success: function (data) {
                	if(data.body == "false"){
						layer.msg('该确认点已有学员预约，不可以删除!', {
							icon : 1,
							time : 1000
						});
					}else{
	                    layer.msg('已删除!', {
	                        icon: 1,
	                        time: 1000
	                    });
	                    myDataTable.fnDraw(true);
	                    $("input[name=all]").attr("checked", false);
					}
                },
                error: function (data) {
                    layer.msg('删除失败！', {
                        icon: 1,
                        time: 1000
                    });
                    myDataTable.fnDraw(true);
                    $("input[name=all]").attr("checked", false);
                },
            });
        });
    }

    function delAll() {
        var chk_value = [];
        $("input[name=confirmationId]:checked").each(function () {
            chk_value.push($(this).val());
        });
        if (chk_value == null || chk_value.length <= 0) {
            layer.msg('未选择任何数据!', {
                icon: 5,
                time: 1000
            });
            return;
        };
        layer.confirm('确认要删除吗？', function (index) {
            $.ajax({
                type: 'POST',
                url: '/sceneManagement/deleteByIdArr.do',
                data: {
                	confirmationIds: chk_value
                },
                dataType: 'json',
                success: function (data) {
                    layer.msg('已删除!', {
                        icon: 1,
                        time: 1000
                    });
                    myDataTable.fnDraw(true);
                    $("input[name=all]").attr("checked", false);
                },
                error: function (data) {
                    layer.msg('删除失败！', {
                        icon: 1,
                        time: 1000
                    });
                    myDataTable.fnDraw(true);
                    $("input[name=all]").attr("checked", false);
                },
            });
        });
    }

    function _search() {
        myDataTable.fnDraw(true);
    }

    
    
  //批量禁用和启用
	function batchSceneMngEnable(status) {
        var chk_value = [];
        $("input[name=confirmationId]:checked").each(function () {
            chk_value.push($(this).val());
        });

        if (chk_value == null || chk_value.length <= 0) {
            layer.msg('未选择任何数据!', {
                icon: 5,
                time: 1000
            });
            return;
        }
        var msg = status == "1" ? "启用" : "禁用";
        layer.confirm('确认要' + msg + '吗？', function (index) {
            //此处请求后台程序，下方是成功后的前台处理……
            $.ajax({
                type: 'POST',
                url: '/sceneManagement/updateStatusByIdArr.do',
                data: {
                	confirmationIds: chk_value,
                    status: status
                },
                dataType: 'json',
                success: function (data) {
                    if (data.code == _GLOBAL_SUCCESS) {
                        myDataTable.fnDraw(false);
                        $("input[name=all]").attr("checked", false);
                        layer.msg('已' + msg + '!', {
                            icon: status == "1" ? 6 : 5,
                            time: 1000
                        });
                    }
                }
            });
        });
    }
 