var myDataTable;
    $(function () {
    	
    	$('select').select2({
            placeholder: "--请选择--",
            allowClear: true,
            width: "59%"
        });
    	
        myDataTable = $('.table-sort').dataTable(
            {
                "serverSide": true,
                "dom": 'rtilp',
                "ajax": {
                    url: "/netToAccount/list.do",
                    type: "post",
                    dataType: 'json',
                    data: {
                        userName: function () {
                            return $("#userName").val() ? $("#userName").val() : '';
                        },
                        isBlock: function () {
                            return $("#isBlock").val() ? $("#isBlock").val() : '';
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
                    "mData": "userName"
                }, {
                    "mData": "signCount"
                }, {
                    "mData": "signCity"
                }, {
                    "mData": "callNumCity"
                }, {
                    "mData": null
                }, {
                    "mData": null
                }, {
                    "mData": null
                }],
                "columnDefs": [
					{
					    "render": function (data, type, row, meta) {
					        var dom = '';
					        if (row.netValue == '1') {
					            dom = '<span class="label label-success radius">是</span>';
					        } else if (row.netValue == '0') {
					            dom = '<span class="label label-default radius">否</span>';
					        }
					        return dom;
					    },
					    "targets": 4
					},
					{
					    "render": function (data, type, row, meta) {
					        var dom = '';
					        if (row.isBlock == '0') {
					            dom = '<span class="label label-success radius">已启用</span>';
					        } else if (row.isBlock == '1') {
					            dom = '<span class="label label-default radius">已禁用</span>';
					        }
					        return dom;
					    },
					    "targets": 5
					},
                    {
                        "render": function (data, type, row, meta) {
                            var dom = '';
                            if (row.isBlock == '0') {
                                dom += '<a onClick="admin_stop(this,\'' + row.userId + '\',\'' + 1 + '\')" href="javascript:;" title="停用" style="text-decoration:none"><i class="iconfont icon-tingyong"></i></a>';
                            } else if (row.isBlock == '1') {
                                dom += '<a onClick="admin_stop(this,\'' + row.userId + '\',\'' + 0+ '\')" href="javascript:;" title="启用" style="text-decoration:none"><i class="iconfont icon-qiyong"></i></a>';
                            }
                            dom += '&nbsp;&nbsp;&nbsp;';
                            dom += '<a title="编辑" href="javascript:void(0)" onclick="admin_edit(\'' + row.userId + '\')" class="ml-5" style="text-decoration:none">';
                            dom += '<i class="iconfont icon-edit"></i></a>';
                            dom += '<a style="text-decoration:none" class="ml-5" onClick="changePassword(\'' + row.userId + '\')" href="javascript:;" title="修改密码"><i class="Hui-iconfont f-18">&#xe63f;</i></a>';
                            return dom;
                        },
                        "targets": 6
                    }]
            });

    });
  
    /*新增*/
    function admin_add() {
        var url = '/netToAccount/userEdit.do' + '?exType=ADD';
        layer_show('添加用户', url, null, 510, function () {
            myDataTable.fnDraw(true);
        });
    }

    /*编辑*/
    function admin_edit(userId) {
        var url = '/netToAccount/userEdit.do' + '?userId=' + userId + '&exType=UPDATE';
        layer_show('修改用户', url, null, 510, function () {
            myDataTable.fnDraw(false);
        });
    }
    /*管理员-停用*/
    function admin_stop(obj, id,isBlock) {
    	var msg = '';
    	if(isBlock == 1){
    		msg +='确认要停用吗？';
    	}else if(isBlock == 0){
    		msg +='确认要启用吗？';
    	}
        layer.confirm(msg, function (index) {
            //此处请求后台程序，下方是成功后的前台处理……
            $.ajax({
                type: 'POST',
                url: '/netToAccount/userBlock.do',
                data: {
                    userId: id,
                    isBlock: isBlock
                },
                dataType: 'json',
                success: function (data) {
                    if (data.code == _GLOBAL_SUCCESS) {
                        myDataTable.fnDraw(false);
                        layer.msg('操作成功!', {icon: 5, time: 1000});
                    }
                }
            });
        });
    }
    /*密码-修改*/
    function changePassword(id) {
        var url = '/user/toEditPwd.do' + '?userId=' + id;
        layer_show('修改密码', url, 600, 240, function () {
            myDataTable.fnDraw(false);
        });
    }
    function searchAdmin() {
        myDataTable.fnDraw(true);
    }