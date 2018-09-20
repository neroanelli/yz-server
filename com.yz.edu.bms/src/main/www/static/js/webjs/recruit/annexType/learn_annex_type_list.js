var myDataTable;
$(function() {
    _init_select("isRequire", [
        {"dictValue" : "0","dictName" : "否"},
        {"dictValue" : "1","dictName" : "是"}
    ]);
    _init_select("isVisible", [
        {"dictValue" : "0","dictName" : "否"},
        {"dictValue" : "1","dictName" : "是"}
    ]);
    _init_select("isUpload", [
        {"dictValue" : "0","dictName" : "否"},
        {"dictValue" : "1","dictName" : "是"}
    ]);

    myDataTable = $('.table-sort').dataTable({
        "serverSide" : true,
        "dom" : 'rtilp',
        "ajax" : {
            url : "/learnAnnexType/getAnnexTypeList.do",
            type: "post",
            data : function(pageData) {
                pageData = $.extend({},{start:pageData.start, length:pageData.length, recruitType:recruitType}, $("#searchForm").serializeObject());
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
            {"mData" : "annexTypeName"},
            {"mData" : "annexTypeValue"},
            {"mData" : null},
            {"mData" : null},
            {"mData" : null},
            {"mData" : null}
        ],
        "columnDefs" : [
            {"targets" : 2,"render" : function(data, type, row, meta) {
                var dom = '';
                if ('1' === row.isRequire) {
                    dom = '<i class="Hui-iconfont Hui-iconfont-xuanze"></i>';
                } else {
                    dom = '<i class="Hui-iconfont Hui-iconfont-close"></i>';
                }
                return dom;
            }},
            {"targets" : 3,"render" : function(data, type, row, meta) {
                var dom = '';
                if ('1' === row.isVisible) {
                    dom = '<i class="Hui-iconfont Hui-iconfont-xuanze"></i>';
                } else {
                    dom = '<i class="Hui-iconfont Hui-iconfont-close"></i>';
                }
                return dom;
            }},
            {"targets" : 4,"render" : function(data, type, row, meta) {
                var dom = '';
                if ('1' === row.isUpload) {
                    dom = '<i class="Hui-iconfont Hui-iconfont-xuanze"></i>';
                } else {
                    dom = '<i class="Hui-iconfont Hui-iconfont-close"></i>';
                }
                return dom;
            }},
            {"targets" : 5, "render" : function(data, type, row, meta) {
                var dom = '<a title="编辑" href="javascript:void(0);" onclick="annexTypeEdit(\'' + row.id + '\')" class="ml-5" style="text-decoration: none">';

                dom += '<i class="Hui-iconfont">&#xe6df;</i></a>';
                if(row.annexTypeValue<0|| row.annexTypeValue>19){
                    dom += '&nbsp;&nbsp;&nbsp;';
                    dom += '<a title="删除" href="javascript:void(0);" onclick="annexTypeDel(this,\'' + row.id + '\')" class="ml-5" style="text-decoration: none">';
                    dom += '<i class="Hui-iconfont">&#xe6e2;</i></a>'
                };
                return dom;
            }}
        ]
    });
});

function searchAnnexType(){
    myDataTable.fnDraw(true);
}


/*附件类型-添加*/
function addAnnexType() {
    var url = '/learnAnnexType/toEditAnnexType.do' + '?exType=Add&recruitType='+recruitType;
    layer_show('添加附件类型', url, 600, 400, function () {
        myDataTable.fnDraw(true);
    });
}

/*附件类型-编辑*/
function annexTypeEdit(id) {
    var url = '/learnAnnexType/toEditAnnexType.do' + '?id=' + id + '&exType=UPDATE&recruitType='+recruitType;
    layer_show('修改附件类型', url, 600, 400, function () {
        myDataTable.fnDraw(false);
    });
}

/*附件类型-删除*/
function annexTypeDel(obj, id) {
    layer.confirm('确认要删除吗？', function (index) {
        $.ajax({
            type: 'POST',
            url: '/learnAnnexType/deleteAnnexType.do',
            data: {
                id: id
            },
            dataType: 'json',
            success: function (data) {
                if(data.body=="fail"){
                    layer.msg('该附件类型已存在学员数据，不能删除!', {icon: 1,time: 1000});
                    return;
                }
                layer.msg('已删除!', {icon: 1,time: 1000});
                myDataTable.fnDraw(false);
            },
            error: function (data) {
                layer.msg('删除失败！', {icon: 1,time: 1000});
                myDataTable.fnDraw(false);
            }
        });
    });
}

