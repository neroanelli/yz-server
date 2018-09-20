var myDataTable,columns,columnDefs;
$(function() {
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
    //初始化年级下拉框
    _init_select("grade", dictJson.grade);
    //初始化专业层次下拉框
    _init_select("pfsnLevel", dictJson.pfsnLevel);

    //初始化数据排序
    _init_select("orderBy",[
        {"dictValue" : "1","dictName" : "总人数降序"},
        {"dictValue" : "2","dictName" : "报考信息确认完成率降序"},
        {"dictValue" : "3","dictName" : "预约完成率降序"},
        {"dictValue" : "4","dictName" : "网报成功率降序"},
        {"dictValue" : "5","dictName" : "网报缴费完成率降序"},
        {"dictValue" : "6","dictName" : "签到完成率降序"},
        {"dictValue" : "7","dictName" : "确认完成率降序"},
        {"dictValue" : "8","dictName" : "考生号占比率降序"}
    ]);
    _init_select("empStatus",[
		{"dictValue":"2","dictName":"是"},
		{"dictValue":"1","dictName":"否"}
	]);

    //优惠类型
    _init_select("scholarship", dictJson.scholarship);

    _simple_ajax_select({
        selectId: "taCityCode",
        searchUrl: '/testArea/findCityKeyValue.do',
        sData: {},
        showId: function (item) {
            return item.city_code;
        },
        showText: function (item) {
            return item.city_name;
        },
        placeholder: '--请选择考试区域--'
    });
    
    _init_campus_select("campusId", "dpId", "groupId", '/campus/selectAllList.do', '/dep/selectAllList.do', '/group/selectAllList.do');
    
    
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

    $("#checkAll").click(function() {
        var checkFlag = $(this).prop("checked");
        $("input:checkbox[name=stdStage]").each(function() {
            $(this).prop("checked", checkFlag);
        });
    });


    myDataTable = $('.table-sort').dataTable({
        "processing": true,
        "serverSide": true,
        "dom": 'rtilp',
        "pageLength": 10,
        "pagingType": "full_numbers",
        "ordering": false,
        "searching": false,
        "lengthMenu": [10,100,1000],
        "createdRow": function (row, data, dataIndex) {
            $(row).addClass('text-c');
        },
        "language": _my_datatables_language,
        "columns":columns
        });
    });

function _search_order() {
	 var statGroupArray = new Array(), stdStageArray = new Array();
     $.each($("input[name='statGroup']:checked"),function(){
         statGroupArray.push($(this).val());
     });

     $.each($("input[name='stdStage']:checked"),function(){
         stdStageArray.push($(this).val());
     });

     if(stdStageArray == '' || stdStageArray == null){
         layer.msg('请选择学员状态！', {
             icon : 1,
             time : 2000
         });
         return false;
     }
     if(statGroupArray == '' || statGroupArray == null){
         layer.msg('请选分组条件！', {
             icon : 1,
             time : 2000
         });
         return false;
     }

     // 汇总统计
     $.ajax({
         url:"/sceneConfirmStat/countSceneConfirmStat.do",
         type:"post",
         data: searchData({start:1,length:10}),
         success:function (data) {
             $("#count").text(data.body.count);
             $("#examInfoConfirmRate").text(data.body.examInfoConfirmRate);
             $("#placeConfirmRate").text(data.body.placeConfirmRate);
             $("#webRegisterRate").text(data.body.webRegisterRate);
             $("#examPayRate").text(data.body.examPayRate);
             $("#signRate").text(data.body.signRate);
             $("#sceneConfirmRate").text(data.body.sceneConfirmRate);
             $("#hasExamNo").text(data.body.hasExamNo);
             $("#empStatus").text(data.body.empStatus);
         }
     })

     myDataTable.fnDestroy(true);
     addTr();
     myDataTable = $('.table-sort').dataTable({
         "processing": true,
         "serverSide": true,
         "dom": 'rtilp',
         "ajax": {
             url: "/sceneConfirmStat/sceneConfirmStatList.do",
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
         "columnDefs" :columnDefs
     });
}

function addTr() {
    $("#statTable").empty();
    $("#statTable").html("<table class='table table-border table-bordered table-hover table-bg table-sort'><thead><tr id='statTableTr' class='text-c'></tr></thead><tbody></tbody></table>");
    var tr = $("#statTableTr");
    columns = new Array();
    columnDefs = new Array();
    columnDefs.push({
        "targets": 2,"class":"text-l"
    })
    var statGroupArray = new Array();
    $.each($("input[name='statGroup']:checked"),function(){
        statGroupArray.push($(this).val());
    });
    for(var i=0; i<statGroupArray.length; i++){

        if(statGroupArray[i] == 'oc.campus_id'){
            tr.append("<th>校区</th>")
            var columnData = {};
            columnData['mData']='campusName';
            columns.push(columnData);
        }

        if(statGroupArray[i] == 'bu.unvs_id'){
            tr.append("<th width='150'>院校</th>")
            var columnData = {};
            columnData['mData']='unvsName';
            columns.push(columnData);
        }

        if(statGroupArray[i] == 'bli.pfsn_id'){
            tr.append("<th>专业</th>")
            var columnData = {};
            columnData['mData']='pfsnName';
            columns.push(columnData);
        }

        if(statGroupArray[i] == 'bup.pfsn_level'){
            tr.append("<th>专业层次</th>")
            var columnData = {};
            columnData['mData']='pfsnLevel';
            columns.push(columnData);
        }

        if(statGroupArray[i] == 'blr.recruit'){
            tr.append("<th>招生老师</th>")
            var columnData = {};
            columnData['mData']='recruit';
            columns.push(columnData);
        }

        if(statGroupArray[i] == 'od.dp_id'){
            tr.append("<th>招生部门</th>")
            var columnData = {};
            columnData['mData']='recruitDpName';
            columns.push(columnData);
        }

        if(statGroupArray[i] == 'bli.std_stage'){
            tr.append("<th>学员状态</th>")
            var columnData = {};
            columnData['mData']='stdStage';
            columns.push(columnData);
            columnDefs.push({"render" : function(data, type, row, meta) {
            	return _findDict("stdStage", row.stdStage);
			},
			"targets" : columns.length-1
			});
            console.log(columns.length);
        }

        if(statGroupArray[i] == 'bta.ta_name'){
            tr.append("<th>考试县区</th>")
            var columnData = {};
            columnData['mData']='taName';
            columns.push(columnData);
        }

        if(statGroupArray[i] == 'bli.scholarship'){
            tr.append("<th>优惠类型</th>")
            var columnData = {};
            columnData['mData']='scholarship';
            columns.push(columnData);
            columnDefs.push({"render" : function(data, type, row, meta) {
                return _findDict("scholarship", row.scholarship);
            },
                "targets" : columns.length-1
            });
            console.log(columns.length);
        }

        if(statGroupArray[i] == 'bta.city_code'){
            tr.append("<th>考试城市</th>")
            var columnData = {};
            columnData['mData']='cityName';
            columns.push(columnData);
        }

    }

    tr.append("<th>总人数</th>");
    var columnData1 = {};
    columnData1['mData']='count';
    columns.push(columnData1);
    
    tr.append("<th>报考信息确认完成率</th>");
    var columnData2 = {};
    columnData2['mData']='examInfoConfirmRate';
    columns.push(columnData2);
    
    tr.append("<th>预约完成率</th>");
    var columnData3 = {};
    columnData3['mData']='placeConfirmRate';
    columns.push(columnData3);
    
    tr.append("<th>网报成功率</th>");
    var columnData4 = {};
    columnData4['mData']='webRegisterRate';
    columns.push(columnData4);
    
    tr.append("<th>网报缴费完成率</th>");
    var columnData5 = {};
    columnData5['mData']='examPayRate';
    columns.push(columnData5);

    tr.append("<th>签到完成率</th>");
    var columnData6 = {};
    columnData6['mData']='signRate';
    columns.push(columnData6);

    tr.append("<th>确认完成率</th>");
    var columnData7 = {};
    columnData7['mData']='sceneConfirmRate';
    columns.push(columnData7);

    tr.append("<th>是否有考生号</th>");
    var columnData8 = {};
    columnData8['mData']='hasExamNo';
    columns.push(columnData8);
}

function searchData(pageData) {
    var statGroupArray = new Array(), stdStageArray = new Array();
    $.each($("input[name='statGroup']:checked"),function(){
        statGroupArray.push($(this).val());
    });

    $.each($("input[name='stdStage']:checked"),function(){
        stdStageArray.push($(this).val());
    });

    return {
    	empStatus : $("#empStatus").val() ? $("#empStatus").val() : '',
        unvsId : $("#unvsId").val() ? $("#unvsId").val() : '',
        pfsnLevel : $("#pfsnLevel").val() ? $("#pfsnLevel").val() : '',
        pfsnId : $("#pfsnId").val() ? $("#pfsnId").val() : '',
        grade : $("#grade").val() ? $("#grade").val() : '',
        recruit : $("#recruit").val() ? $("#recruit").val() : '',
        scholarship: $("#scholarship").val() ? $("#scholarship").val() : '',
        taCityCode : $("#taCityCode").val() ? $("#taCityCode").val() : '',
        campusId : $("#campusId").val() ? $("#campusId").val() : '',
        dpId : $("#dpId").val() ? $("#dpId").val() : '',
        stdStage :stdStageArray ? stdStageArray.join(','):'',
        statGroup : statGroupArray ? statGroupArray.join(','):'',
        orderBy : $("#orderBy").val() ? $("#orderBy").val() : '',
        start : pageData.start,
        length : pageData.length
    };
}

function init_pfsn_select() {
	_simple_ajax_select({
		selectId : "pfsnId",
		searchUrl : '/baseinfo/sPfsn.do',
		sData : {
			sId :  function(){
				return $("#unvsId").val() ? $("#unvsId").val() : '';	
			},
			ext1 : function(){
				return $("#pfsnLevel").val() ? $("#pfsnLevel").val() : '';
			},
			ext2 : function(){
				return $("#grade").val() ? $("#grade").val() : '';
			}
		},
		showText : function(item) {
			var text = '(' + item.pfsnCode + ')' + item.pfsnName;
			text += '[' + _findDict('pfsnLevel', item.pfsnLevel) + ']';
			return text;
		},
		showId : function(item) {
			return item.pfsnId;
		},
		placeholder : '--请选择专业--'
	});
	$("#pfsnId").append(new Option("", "", false, true));
}