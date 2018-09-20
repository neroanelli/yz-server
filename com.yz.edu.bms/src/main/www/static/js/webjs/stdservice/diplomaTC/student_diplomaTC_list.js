var myDataTable;
		$(function() {
			$('select').select2({
				placeholder : "--请选择--",
				allowClear : true,
				width : "59%"
			});
            //初始化下拉框
            _init_select("grade",dictJson.grade);
            _init_select("pfsnLevel",dictJson.pfsnLevel);

            //初始化下拉框
            $.ajax({
                type: "POST",
                dataType : "json", //数据类型
                url: '/diplomaP/getPlaceName.do',
                success: function(data){
                    var placeIdJson = data.body;
                    if(data.code=='00'){
                        _init_select("placeId",placeIdJson);
                    }
                }
            });

            //初始省、市、区
            _init_area_select("provinceCode", "cityCode", "districtCode","440000");
            $("#districtCode").append("<option value=''>请选择</option>");

            _simple_ajax_select({
				selectId : "unvsId",
				searchUrl : '/bdUniversity/findAllKeyValue.do',
				sData : {},
				showText : function(item) {
					return item.unvs_name;
				},
				showId : function(item) {
					return item.unvs_id;
				},
				placeholder : '--请选择院校--'
			});
			$("#unvsId").append(new Option("", "", false, true));

            _init_select("status",[
                {"dictValue":"1","dictName":"启用"},
                {"dictValue":"2","dictName":"禁用"}
            ]);

            $.ajax({
                type: "POST",
                dataType : "json", //数据类型
                url: '/taskprovide/getTaskName.do',
                success: function(data){
                    var taskNameJson = data.body;
                    if(data.code=='00'){
                        _init_select("diplomaId",taskNameJson);
                    }
                }
            });
			
			myDataTable = $('.table-sort').dataTable({
				"processing": true,
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : '/diplomaTC/findDiplomaTCList.do',
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
                    {"mData" : null},
					{"mData" : "taskName"},
					{"mData" : "placeName"},
					{"mData" : "province"},
					{"mData" : "address"},
					{"mData" : null},
					{"mData" : null},
					{"mData" : null},
					{"mData" : null},
                    {"mData" : null}
				],
				"columnDefs" : [
                    	{"targets": 0,"render": function (data, type, row, meta) {
                            return '<input type="checkbox" value="' + row.configId + '" name="configIds"/>';
                        }},
                        {"targets": 4,"class":"text-l"
                        },

                    	{"targets" : 5, "render" : function(data, type, row, meta) {
                            return row.startTime.substring(0,10);
                        }},
						{"targets" : 6, "render" : function(data, type, row, meta) {
							var dateTime=row.startTime.replace('AM', '上午').replace('PM', '下午') + "-" + row.endTime;
							if(!dateTime){
								return '-';
							}
							return dateTime.substring(11);
						}},
						{"targets" : 7,"render" : function(data, type, row, meta) {
							return '容量 ' + row.number + ' / 余 ' + row.availableNumbers;
						}},
                    	{"targets" : 8,"render" : function(data, type, row, meta) {
                                var dom = '';
                                $.each(row.stuDiplomaTCU,function(i,o){
                                    dom += '（' + _findDict('grade', o.grade) + '）';
									if(o.unvsId === 'ALL') {
										dom += '院校不限';
									}else {
										dom += o.unvsName;
									}
                                    dom += '[';
									if(o.pfsnLevel === 'ALL') {
										dom += '层次不限';
									}else{
										dom += _findDict('pfsnLevel', o.pfsnLevel);
									}
                                    dom += ']';
                                    if(i != row.stuDiplomaTCU.length){
                                        dom += '<br>';
                                    }
                                });
                            return dom;
                        },"class":"text-l"},
						{"targets" : 9,"render" : function(data, type, row, meta) {
							if(row.status && row.status =='1'){
								return "<label class='label label-success radius'>启用</label>";
							}else{
								return "<label class='label  label-danger radius'>禁用</label>";
							}
						}},
						{"targets" : 10,"render" : function(data, type, row, meta) {
							var dom = '<a class="" href="javascript:;" title="修改" onclick="editDiplomaTC(\'' + row.configId + '\')"><i class="iconfont icon-edit"></i></a>&nbsp;';
                                dom += '<a class="" href="javascript:;" title="删除" onclick="deleteConfig(\'' + row.configId + '\')"><i class="iconfont  icon-shanchu"></i></a>&nbsp;';
                                dom += '<a class="" href="javascript:;" title="修改容量" onclick="EditNumber(\'' + row.configId + '\',\''+row.number + '\',\''+row.availableNumbers+'\')"><i class="iconfont  icon-edit"></i></a>';
							return dom;
						}}
				]
			});
		});

		/*编辑*/
		function editDiplomaTC(configId) {

            $.ajax({
                type: 'POST',
                url: '/diplomaTC/isSelect.do',
                data: {
                    configId : configId
                },
                dataType: 'json',
                success: function (data) {
                    if (data.code == _GLOBAL_SUCCESS) {
                        var url = '/diplomaTC/toEdit.do' + '?configId='+ configId ;
                        layer_show('编辑任务配置', url, null, null, function() {
                        },true);
                    }
                }
            });
		}

		/*搜素*/
		function searchResult(){
			myDataTable.fnDraw(true);
		}

		//批量禁用和启用
		function batchDiplomaTCEnable(status) {
            var chk_value = [];
            $("input[name=configIds]:checked").each(function () {
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
                    url: '/diplomaTC/updateStatus.do',
                    data: {
                        configIds: chk_value,
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

        //删除配置
		function deleteConfig(configId,number,availableNumbers) {
            layer.confirm('确认要删除吗？', function (index) {
                $.ajax({
                    type: 'POST',
                    url: '/diplomaTC/deleteByConfigId.do',
                    data: {
                        configId : configId
                    },
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == _GLOBAL_SUCCESS) {
                            myDataTable.fnDraw(false);
                            layer.msg('已删除!', {
                                icon: 6,
                                time: 1000
                            });
                        }
                    }
                });
            });
		}

		//打开添加页面
		function addDiplomaTC() {
            var url = '/diplomaTC/toAdd.do';
            layer_show('新增任务配置', url, null, null, function () {
                myDataTable.fnDraw(true);
            },true);
        }

        //打开修改容量页面
        function EditNumber(configId,curNumber,alNumber) {
            $("#number").val('');
            $("#curNumber").html(curNumber);
            $("#alNumber").html(curNumber - alNumber);
            var url = '/diplomaTC/updateNumber.do';
            layer.open({
                title:'修改容量',
                type: 1,
                btn: ['确定'],
                yes: function (index, layero) {
                    var number = $("#number").val();
                    if (number < 1) {
                        layer.msg('修改容量必须大于0!', {
                            icon: 2,
                            time: 1000
                        });
                        return;
                    }
                    var re = /^(0|[1-9][0-9]{0,2})$/;
                    if (!re.test(number)) {
                        layer.msg('请输入3位以内的正整数!', {
                            icon: 2,
                            time: 1000
                        });
                        return;
                    }

                    $.ajax({
                        type: 'POST',
                        url: url,
                        data: {
                            configId: configId,
                            number: number
                        },
                        dataType: 'json',
                        success: function (data) {
                            if (data.code == _GLOBAL_SUCCESS) {
                                layer.msg('修改成功!', {
                                    icon: 1,
                                    time: 500
                                });
                                myDataTable.fnDraw(false);
                                layer.close(index)
                            }else{
                                layer.msg(data.msg, {
                                    icon: 5,
                                    time: 500
                                });
                            }
                        }
                    });
                },
                area: ['400px', '250px'],
                content: $("#numberContent")
            });
        }

function _resetThis() {
    $(".search input[type='number']").val("");
    $(".search input[type='text']").val("");
    $(".search select").val(null).trigger("change");
}