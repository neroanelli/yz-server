var myDataTable;
    $(function() {
        _init_select('sex', dictJson.sex);
        _init_select('grade', dictJson.grade);
        _init_select('recruitType', dictJson.recruitType);
        _init_select('pfsnLevel', dictJson.pfsnLevel);
        _init_select('scholarship', dictJson.scholarship);
        //是否外校
        _init_select("stdType", dictJson.stdType);
        //优惠分组
        _init_select('sg', dictJson.sg);
        //入围状态
        _init_select("inclusionStatus",dictJson.inclusionStatus);
        //优惠分组联动入围状态
        $("#sg").change(function() {
            _init_select({
                selectId : 'scholarship',
                ext1 : $(this).val()
            }, dictJson.scholarship);
        });
        $("#recruitType").change(function () {
            _init_select({
                selectId: 'grade',
                ext1: $(this).val()
            }, dictJson.grade);
        });
        //是否欠费
        _init_select("isArrears", [
            {"dictValue" : "0","dictName" : "否"},
            {"dictValue" : "1","dictName" : "是"}
        ]);

        addTr();
        myDataTable = $('.table-sort').dataTable({
            "processing": true,
            "serverSide": true,
            "dom": 'rtilp',
            "ajax": {
                url: "/stdStudyFeeStat/stdStudyFeeStatList.do",
                type: "post",
                data: function (pageData) {
                    return searchData(pageData);
                }
            },
            "pageLength": 10,
            "pagingType": "full_numbers",
            "ordering": false,
            "searching": false,
            "lengthMenu": [10, 20],
            "createdRow": function (row, data, dataIndex) {
                $(row).addClass('text-c');
            },
            "language": _my_datatables_language,
            columns: columns,
            "columnDefs" : [{"render" : function(data, type, row, meta) {
                            return row.countAll-row.countFinish
                        },
                        "targets" : -1
                    }],
            "footerCallback": function ( tfoot, data, start, end, display ) {
                if(data.length>0){
                    $(tfoot).show(0)
                    var len=$(tfoot).find('th').length;
                    var countAll=0;
                    var countFinish=0;
                    var countUnFinish=0;
                    data.forEach(function (e,i) {
                        countAll+=e.countAll;
                        countFinish+=e.countFinish;
                        countUnFinish+=e.countAll-e.countFinish;
                    });

                    var finishRate=((countFinish/countAll)*100).toFixed(2)+ '%';

                    $(tfoot).find('th').eq(len-1).html(countUnFinish)
                    $(tfoot).find('th').eq(len-2).html(finishRate)
                    $(tfoot).find('th').eq(len-3).html(countFinish)
                    $(tfoot).find('th').eq(len-4).html(countAll)
                }else {
                    $(tfoot).hide(0)
                }
            }
        });
        //myDataTable.fnDraw(true);
    });


    function _search() {
        var statGroupArray = new Array();
        $.each($("input[name='statGroup']:checked"),function(){
            statGroupArray.push($(this).val());
        });
        if(statGroupArray == '' || statGroupArray == null){
            layer.msg('请选分组条件！', {
                icon : 1,
                time : 2000
            });
            return false;
        }
        myDataTable.fnDestroy(true);
        addTr();
        myDataTable = $('.table-sort').dataTable({
            "processing": true,
            "serverSide": true,
            "dom": 'rtilp',
            "ajax": {
                url: "/stdStudyFeeStat/stdStudyFeeStatList.do",
                type: "post",
                data: function (pageData) {
                    return searchData(pageData);
                }
            },
            "pageLength": 10,
            "pagingType": "full_numbers",
            "ordering": false,
            "searching": false,
            "lengthMenu": [10,100,1000],
            "createdRow": function (row, data, dataIndex) {
                $(row).addClass('text-c');
            },
            "language": _my_datatables_language,
            "columns":columns,
            "columnDefs" : [{"render" : function(data, type, row, meta) {
                            return row.countAll-row.countFinish
                        },
                        "targets" : -1}],
            "footerCallback": function ( tfoot, data, start, end, display ) {
                if(data.length>0){
                    $(tfoot).show(0)
                    var len=$(tfoot).find('th').length;
                    var countAll=0;
                    var countFinish=0;
                    var countUnFinish=0;
                    data.forEach(function (e,i) {
                        countAll+=e.countAll;
                        countFinish+=e.countFinish;
                        countUnFinish+=e.countAll-e.countFinish;
                    });

                    var finishRate=((countFinish/countAll)*100).toFixed(2)+ '%';

                    $(tfoot).find('th').eq(len-1).html(countUnFinish)
                    $(tfoot).find('th').eq(len-2).html(finishRate)
                    $(tfoot).find('th').eq(len-3).html(countFinish)
                    $(tfoot).find('th').eq(len-4).html(countAll)
                }else {
                    $(tfoot).hide(0)
                }
            }
        });
    }


    function searchData(pageData) {
        var statGroupArray = new Array();
        $.each($("input[name='statGroup']:checked"),function(){
            statGroupArray.push($(this).val());
        });
        var statGroup = statGroupArray ? statGroupArray.join(','):'';
        pageData = $.extend({},{statGroup:statGroup, start:pageData.start, length:pageData.length},$("#searchForm").serializeObject());
        return pageData;
    }


    function addTr() {
        $("#statTable").empty();
        var thLen=$("input[name='statGroup']:checked").length;
        $("#statTable").html("<table class='table table-border table-bordered table-hover table-bg table-sort'><thead><tr id='statTableTr' class='text-c'></tr></thead><tfoot><tr><th colspan='"+thLen+"'>总计</th><th></th><th></th><th></th><th></th></tr></tfoot><tbody></tbody><tbody></tbody></table>");
        var tr = $("#statTableTr");
        columns = new Array();
        var statGroupArray = new Array();
        $.each($("input[name='statGroup']:checked"),function(){
            statGroupArray.push($(this).val());
        });
        for(var i=0; i<statGroupArray.length; i++){
            if(statGroupArray[i] == 'tab.grade'){
                tr.append("<th width='90'>年级</th>");
                var columnData = {};
                columnData['mData']='grade';
                columns.push(columnData);
            }
            if(statGroupArray[i] == 'tab.unvs_id'){
                tr.append("<th>院校</th>")
                var columnData = {};
                columnData['mData']='unvs_name';
                columns.push(columnData);
            }
            if(statGroupArray[i] == 'tab.pfsn_id'){
                tr.append("<th>专业</th>")
                var columnData = {};
                columnData['mData']='pfsn_name';
                columns.push(columnData);
            }
            if(statGroupArray[i] == 'tab.pfsn_level'){
                tr.append("<th>专业层次</th>")
                var columnData = {};
                columnData['mData']='pfsn_level';
                columns.push(columnData);
            }
            if(statGroupArray[i] == 'tab.tutor_name'){
                tr.append("<th>班主任</th>")
                var columnData = {};
                columnData['mData']='tutor_name';
                columns.push(columnData);
            }
        }

        $("#statTableTr").append("<th>总人数</th>");
        var columnDataAll = {};
        columnDataAll['mData']='countAll';
        columns.push(columnDataAll);

        $("#statTableTr").append("<th>已缴清人数</th>");
        var columnDataFinish = {};
        columnDataFinish['mData']='countFinish';
        columns.push(columnDataFinish);

        $("#statTableTr").append("<th>完成率(%)</th>");
        var columnDataRate = {};
        columnDataRate['mData']='finishRate';
        columns.push(columnDataRate);

        $("#statTableTr").append("<th>未缴清人数</th>");
        var columnDataRate = {};
        columnDataRate['mData']=null;
        columns.push(columnDataRate);

    }
