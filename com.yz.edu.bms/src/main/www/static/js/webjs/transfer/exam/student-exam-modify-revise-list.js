var myDataTable;
		$(function() {

            $('select').select2({
                placeholder : "--请选择--",
                allowClear : true,
                width : "59%"
            });

            //申请进度saStatus
            _init_select("checkStatus",[
                {
                    "dictValue":"2","dictName":"已初审"
                },
                {
                    "dictValue":"4","dictName":"已驳回"
                }
            ]);

            //优惠类型
            _init_select('scholarship', dictJson.scholarship);

            //初始校区下拉框
            _simple_ajax_select({
                selectId : "campusId",
                searchUrl : '/campus/selectList.do',
                sData : {},
                showText : function(item) {
                    return item.campusName;
                },
                showId : function(item) {
                    return item.campusId;
                },
                placeholder : '--请选择校区--'
            });
            $("#campusId").append(new Option("", "", false, true));

            //初始部门下拉框
            _simple_ajax_select({
                selectId : "dpId",
                searchUrl : '/dep/selectList.do',
                sData : {},
                showText : function(item) {
                    return item.dpName;
                },
                showId : function(item) {
                    return item.dpId;
                },
                placeholder : '--请选择部门--'
            });
            $("#dpId").append(new Option("", "", false, true));

            //报考层次
            _init_select("pfsnLevel", dictJson.pfsnLevel);
            //学员阶段
            _init_select('stdStage', dictJson.stdStage);

            //考试费
            _init_select("examPayStatus",[
                {
                    "dictValue":"1","dictName":"未缴费"
                },
                {
                    "dictValue":"2","dictName":"已缴费"
                }
            ]);
            //考试费
            _init_select("webRegisterStatus",[
                {
                    "dictValue":"0","dictName":"待网报"
                },
                {
                    "dictValue":"1","dictName":"网报成功"
                }
            ]);

            //考试县区
            _simple_ajax_select({
                selectId: "taId",
                searchUrl: '/testArea/findAllKeyValue.do',
                sData: {},
                showId: function (item) {
                    return item.taId;
                },
                showText: function (item) {
                    return item.taName;
                },
                placeholder: '--请选择考试区域--'
            });



			myDataTable = $('.table-sort').dataTable(
					{
                        "processing": true,
						"serverSide" : true,
						"dom" : 'rtilp',
						"ajax" : {
							url : "/examModifyRevise/findExamModifyRevise.do",
							type : "post",
							data : {
                                "stdName" : function() {
                                    return $("#stdName").val();
                                },"mobile" : function() {
                                    return $("#phone").val();
                                },"idCard" : function() {
                                    return $("#idCard").val();
                                },"unvsName" : function() {
                                    return $("#unvsName").val();
                                },"pfsnName" : function() {
                                    return $("#pfsnName").val();
                                },"stdStage" : function() {
                                    return $("#stdStage").val();
                                },"checkStatus" : function() {
                                    return $("#checkStatus").val();
                                },"recruit" : function() {
                                    return $("#recruit").val();
                                },"campusId" : function() {
                                    return $("#campusId").val();
                                },"dpId" : function() {
                                    return $("#dpId").val();
                                },"pfsnLevel" : function() {
                                    return $("#pfsnLevel").val();
                                },"taId" : function() {
                                    return $("#taId").val();
                                },"examPayStatus" : function() {
                                    return $("#examPayStatus").val();
                                },"scholarship" : function() {
                                    return $("#scholarship").val();
                                },"webRegisterStatus" : function() {
                                    return $("#webRegisterStatus").val();
                                }
							}
						},
						"pageLength" : 10,
						"pagingType" : "full_numbers",
						"ordering" : false,
						"searching" : false,
						"createdRow" : function(row, data, dataIndex) {
							$(row).addClass('text-c');
							$(row).children('td').eq(6).attr(
									'style', 'text-align: left;');
						},
						"language" :_my_datatables_language,
						columns : [ {
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
                            "mData" : 'empName'
                        }, {
                            "mData" : null
                        }, {
                            "mData" : null
                        }, {
                            "mData" : null
                        } ],
						"columnDefs" : [
								{
									"render" : function(data, type, row, meta) {
										return _findDict("grade",row.grade);
									},
									"targets" : 1
								},
								{
									"render" : function(data, type, row, meta) {
                                        var dom ='';
                                        _findDict("recruitType",row.recruitType).indexOf("成人")!=-1?dom+="[成教]":dom+="[国开]";

                                        dom+=row.unvsName+"<br>";
                                        _findDict("pfsnLevel",row.pfsnLevel).indexOf("高中")!=-1?dom+="[专科]":dom+="[本科]";

                                        dom+=row.pfsnName+"("+row.pfsnCode+")";
										return dom;
									},
									"targets" : 2,
                                    "class":"text-l"
								},
								{
									"render" : function(data, type, row, meta) {
										return _findDict("stdStage",row.stdStage);
									},
									"targets" : 3
								},
								{
									"render" : function(data, type, row, meta) {
										return _findDict("scholarship",row.scholarship);
									},
									"targets" : 4
								},
								{
									"render" : function(data, type, row, meta) {
										return row.createUser;
									},
									"targets" : 9
								},
								{
									"render" : function(data, type, row, meta) {
										var dateTime= new Date(row.createTime).format('yyyy-MM-dd hh:mm:ss');
                                        if(!dateTime){
                                            return '-'
                                        }
                                        var date=dateTime.substring(0,10)
                                        var time=dateTime.substring(11)
                                        return date+'<br>'+time
									},
									"targets" : 10,"class":"text-l"
								},
								{
									"render" : function(data, type, row, meta) {
                                        var dom ="";
                                        if(row.username){
                                            dom += '报名号: ' + row.username + '<br>';
                                            dom += '密码: ' + row.password + '<br>';
										}
										return dom;
									},
									"targets" : 6,"class":"text-l"
								},
                                {
                                    "render" : function(data, type, row, meta) {
                                        var dom ="";
                                        if(row.webRegisterStatus == '1'){
                                            dom ='<label class="label-success label radius">网报成功</label>';
                                        }else {
                                            dom ='<label class="label-danger label radius">待网报</label>';
                                        }
                                        return dom;
                                    },
                                    "targets" : 7
                                },
								{
									"render" : function(data, type, row, meta) {
                                        var dom ="";
                                        if(row.examPayStatus == '1'){
                                            dom ='<label class="label-secondary label radius">已缴费</label>';
                                        }else {
                                            dom ='<label class="label-danger label radius">未缴费</label>';
										}
                                        return dom;
                                    },
									"targets" : 8
								},
								{
									"render" : function(data, type, row, meta) {


										return row.ext1;
									},
									"targets" : 5,
                                    "class":"text-l"
								},
								{
									"render" : function(data, type, row, meta) {
                                        var dom ="";
										if(row.checkStatus == '2'){
                                            dom ='<label class="label-secondary label radius">已初审</label>';
                                        }else {
                                            dom ='<label class="label-danger label radius">已驳回</label>';
										}
										return dom;
									},
									"targets" : 12
								},
								{
									"render" : function(data, type, row, meta) {
										var dom ="";
										if(row.remark && row.remark.length>30){
											dom += row.remark.substring(0,30)+'......';
										}else {
                                            return row.remark;
                                        }
										return dom;
									},
									"targets" : 13
								},{
									"render" : function(data, type, row, meta) {
										var dom = '';
                                        if(row.checkStatus == '2'){
                                            dom = '<a title="进入修改" href="javascript:;" onclick="toRevise(\'' + row.modifyId + '\',\'' + row.learnId + '\')" class="ml-5" style="text-decoration: none">';
                                            dom += '<i class="iconfont icon-shenhe"></i></a>';
                                        }else {
                                            dom = '<a title="查看" href="javascript:;" onclick="toRevise(\'' + row.modifyId + '\',\'' + row.learnId + '\')" class="ml-5" style="text-decoration: none">';
                                            dom += '<i class="iconfont icon-chakan"></i></a>';
                                        }

                                        if(row.checkStatus == '2'){
                                            dom += '<a title="修改" href="javascript:;" onclick="passModify(\'' + row.modifyId + '\')" class="ml-5" style="text-decoration: none">';
                                            dom += '<img id="u1640_img" class="img " src="../images/checkPass.jpg"/></a>';
                                            dom += '<a title="驳回" href="javascript:;" onclick="notModify(\'' + row.modifyId + '\')" class="ml-5" style="text-decoration: none">';
                                            dom += '<img id="u1641_img" class="img " src="../images/bh.jpg"/></a>';
                                        }
										return dom;
									},
									//指定是第三列
									"targets" : 14
								} ]
					});

		});

		/*用户-修改*/
		function toRevise(modifyId,learnId) {
			var url = '/examModifyRevise/toRevise.do' + '?modifyId=' + modifyId + '&learnId=' + learnId;
			layer_show('成考网报信息异动修改', url, null, 510, function() {
				myDataTable.fnDraw(false);
			},true);
		}

		
		function _search() {
			myDataTable.fnDraw(true);
		}
		

		//修改事件
var isPassModify = true;//防重操作
		function passModify(modifyId){
            isPassModify = true;
			layer.confirm('确认修改吗？', function(index) {
			    if(isPassModify){
                    isPassModify = false;
                    $.ajax({
                        type : 'POST',
                        url : '/examModifyRevise/passModify.do',
                        data : {
                            modifyId : modifyId,
                            checkStatus : '3'
                        },
                        dataType : 'json',
                        success : function(data) {
                            if(data.code == _GLOBAL_SUCCESS){

                                layer.msg('审核成功!', {
                                    icon : 1,
                                    time : 1000
                                });
                                myDataTable.fnDraw(true);
                            }
                        }
                    });
                }
			});
		}

		//驳回事件
    var isNotModify = true;//防重操作
		function notModify(modifyId) {
            isNotModify = true;
            layer.prompt({
                title : '填写驳回原因：',
                formType : 2
            }, function(text, index) {
                if(isNotModify){
                    isNotModify = false;
                    if(text.length > 50){
                        layer.msg('驳回原因不能超过50字!', {
                            icon : 5,
                            time : 1000
                        });
                        return;
                    }
                    $.ajax({
                        type : 'POST',
                        url : '/examModifyRevise/passModify.do',
                        data : {
                            modifyId : modifyId,
                            checkStatus : '4',
                            reason : text
                        },
                        dataType : 'json',
                        success : function(data) {
                            if(data.code == _GLOBAL_SUCCESS){

                                layer.msg('驳回成功!', {
                                    icon : 1,
                                    time : 1000
                                });
                                myDataTable.fnDraw(true);
                            }
                        }
                    });
                }
            });
        }
