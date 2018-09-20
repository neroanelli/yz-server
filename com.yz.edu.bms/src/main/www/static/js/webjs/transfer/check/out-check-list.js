		var myDataTable;
		var jqueryId = "table-sortfinancial";
		var postUrl = "/studentOutApproval/findFinancialApproval.do";
		var editUrl = "/studentOutApproval/editToFinancial.do";
		var map = {};
		$(function() {
			
			$('select').select2({
				placeholder : "--请选择--",
				allowClear : true,
				width : "59%"
			});
			
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
					
			$("#unvsId").change(function () {
	            $("#pfsnId").removeAttr("disabled");
	            init_pfsn_select();
		     });
			//校区-部门-组 联动
			 _init_campus_select("campusId", "dpId", "groupId", '/campus/selectAllList.do', '/dep/selectAllList.do', '/group/selectAllList.do');
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

			 $("#recruitType").change(function() {
					_init_select({
						selectId : "grade",
						ext1 : $(this).val()
					}, dictJson.grade);

			 });
			 //初始化年级下拉框
			 _init_select('sg', dictJson.sg); //优惠分组
			 _init_select('scholarship', dictJson.scholarship);
	   		 _init_select("inclusionStatus",dictJson.inclusionStatus);
	   		 _init_select('stdStage', dictJson.stdStage);
			 _init_select("grade",dictJson.grade);
			 _init_select("pfsnLevel",dictJson.pfsnLevel);
			 _init_select('recruitType', dictJson.recruitType);
			 _init_select("reason", dictJson.reason);
			 
	   		 $("#sg").change(function() { //联动
	   			_init_select({
	   				selectId : 'scholarship',
	   				ext1 : $(this).val()
	   			}, dictJson.scholarship);
	   		 });
	   		 
	   		 
	   		
			//初始审批状态
			_init_select("checkState", [ {
				"dictValue" : "1",
				"dictName" : "审核中"
			}, {
				"dictValue" : "2",
				"dictName" : "审核通过"
			} ]);
			
			
			
			$("#tab_demo_bar").empty();
			if (!isSuper) {
//				if (isXJ && isXJ == '1') {
//					$("#tab_demo_bar").append("<span id=\"is_xj\">校监/主任审批</span>");
//					jqueryId = "table-sort";
//				}
				if (isCW && isCW == '1') {
					$("#tab_demo_bar").append("<span id=\"is_cw\">退费专员审批</span>");
					jqueryId = "table-sortfinancial";
				}
				if (isXB && isXB == '1') {
					$("#tab_demo_bar").append("<span id=\"is_xb\">客服主管审批</span>");
					jqueryId = "table-sortschoolManaged";
				}
//				if (isJW && isJW == '1') {
//					$("#tab_demo_bar").append("<span id=\"is_jw\">教务审批</span>");
//					jqueryId = "table-sortsenate";
//				}
				
//				if (isXJ && isXJ == '1') {
//					postUrl = "/studentOutApproval/findDirectorApproval.do";
//					editUrl = "/studentOutApproval/editToDirector.do";
//				}else
				 if (isCW && isCW == '1') {
					
					postUrl = "/studentOutApproval/findFinancialApproval.do";
					editUrl = "/studentOutApproval/editToFinancial.do";
				}else
				 if (isXB && isXB == '1') {
					postUrl = "/studentOutApproval/findSchoolManagedApproval.do";
					editUrl = "/studentOutApproval/editToSchoolManaged.do";
				}
//				else 
//				if (isJW && isJW == '1') {
//					postUrl = "/studentOutApproval/findSenateApproval.do";
//					editUrl = "/studentOutApproval/editToSenate.do";
//				}
			} else {
//				$("#tab_demo_bar").append("<span id=\"is_xj\">校监/主任审批</span>");
				$("#tab_demo_bar").append("<span id=\"is_cw\">退费专员审批</span>");
				$("#tab_demo_bar").append("<span id=\"is_xb\">客服主管审批</span>");
//				$("#tab_demo_bar").append("<span id=\"is_jw\">教务审批</span>");
				jqueryId = "table-sortfinancial";
				postUrl = "/studentOutApproval/findFinancialApproval.do";
				editUrl = "/studentOutApproval/editToFinancial.do";
			}

			$("#tab_demo .tabBar span").bind("click",function() {
				var _id = $(this).attr("id");
				var index = $("#tab_demo .tabBar span").index(this);
//				if ('is_xj' == _id) {
//					jqueryId = "table-sort";
//					postUrl = "/studentOutApproval/findDirectorApproval.do";
//					editUrl = "/studentOutApproval/editToDirector.do";
//				} 
//				else 
				if ('is_cw' == _id) {
					jqueryId = "table-sortfinancial";
					postUrl = "/studentOutApproval/findFinancialApproval.do";
					editUrl = "/studentOutApproval/editToFinancial.do";
				} else 
				if ('is_xb' == _id) {
					jqueryId = "table-sortschoolManaged";
					postUrl = "/studentOutApproval/findSchoolManagedApproval.do";
					editUrl = "/studentOutApproval/editToSchoolManaged.do";
				} 
//				else 
//				if ('is_jw' == _id) {
//					jqueryId = "table-sortsenate";
//					postUrl = "/studentOutApproval/findSenateApproval.do";
//					editUrl = "/studentOutApproval/editToSenate.do";
//				}
				if (!map[index]) {
					myDataTable = $('.'+ jqueryId).dataTable({
						"processing": true,
						"serverSide" : true,
						"dom" : 'rtilp',
						"ajax" : {
							url : postUrl,
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
						"createdRow" : function(row, data,dataIndex){
							$(row).addClass('text-c');
						},
						"language" : _my_datatables_language,
						columns : [
							{"mData" : null},
							{"mData" : "stdName"},
							{"mData" : null},
							{"mData" : null},
							{"mData" : null},
							{"mData" : null},
							{"mData" : null},
							{"mData" : null},
							{"mData" : "createUser"},
							{"mData" : null},
							{"mData" : "dpName"},    //招生部门
							{"mData" : "zsName"},    //招生老师
							{"mData" : null},
							{"mData" : null},
							{"mData" : null},
							{"mData" : null}
						],
						"columnDefs" : [
							{"targets" : 0,"render" : function(data,type,row,meta) {
								return '<input type="checkbox" class="sendIdsCheckBox" value="' + row.outTd + '" name="outTds"/>';
							}},
							{"targets" : 2,"render" : function(data, type, row, meta) {
								return _findDict("grade",row.grade);
							}},
							{"targets" : 3, "class":"text-l no-warp","render" : function(data,type,row,meta) {
								var dom='';
								_findDict("recruitType", row.recruitType).indexOf("成人")!=-1?dom+="[成教]":dom+="[国开]";
								dom+=row.unvsName+"<br>";
								_findDict("pfsnLevel", row.pfsnLevel).indexOf("高中")!=-1?dom+="[专科]":dom+="[本科]";
								dom+=row.pfsnName;
								return dom;
							}},
							{"targets" : 4,"render" : function(data, type, row, meta) {
								return _findDict("scholarship",row.scholarship);
							}},
							{"targets" : 5,"render" : function(data, type, row, meta) {
								return _findDict("stdStage",row.stdStage);
							}},
							{"targets" : 6,"render" : function(data,type,row,meta) {
								return _findDict("reason",row.reason);
							}},
							{"targets" : 7,"render" : function(data,type,row,meta) {
								var amount=row.refundAmount;
								if(row.refundAmount==null||row.refundAmount==''||row.refundAmount=='0.00')
								amount='-';
								return amount;
							}},
							{"targets" : 9,"class":"text-l","render" : function(data,type, row, meta) {
								var dateTime = new Date(row.createTime).format('yyyy-MM-dd hh:mm:ss');
								if(dateTime==null||dateTime==''){
									return '-'
								}
								var date=dateTime.substring(0,10)
								var time=dateTime.substring(11)
								return date+'<br>'+time
							}},
							{"targets" : 12,"render" : function(data,type,row,meta) {
								var dom = '<a href="javascript:void(0);" style="text-decoration: none;color:#5485C7;" onclick="showRecordList(\''+ row.learnId+ "@"+ row.grade+ "@"+ row.outTd+ "@"+ row.stdName+  '\')">跟进记录</a><span>('+row.recordCount+')</span>';
								return dom;
								/*return '<a title="跟进记录(0)" href="javascript:;" ' +
										'onclick="member_genjin(\''+  row.outTd+ '\',\''+row.learnId+'\')" ' +
										'class="ml-5" style="text-decoration: none">';*/
							}},
							{"targets" : 13,"render" : function(data,type,row,meta) {
								return 2 == row.checkStatus ? '<span class="label label-success radius">' + _findDict("saStatus",row.checkStatus) + '</span>':
									'<span class="label-secondary label radius">'+ _findDict( "saStatus", row.checkStatus)+ '</span>';
							}},
							{"targets" : 14,"render" : function(data,type,row,meta) {
								if(1 == row.checkStatus){
									return '-'
								}
								if(row.updateTime==null||row.updateTime==''){
									return '-'
								}
								var dateTime = new Date(row.updateTime).format('yyyy-MM-dd hh:mm:ss');
								if(dateTime==null||dateTime==''){
									return '-'
								}
								var date=dateTime.substring(0,10);
								var time=dateTime.substring(11);
								return date+'<br>'+time;
							}},
							{"targets" : 15,"render" : function(data,type,row,meta) {
								var dom = '';
								if (2 == row.checkStatus) {
									dom = '<a title="撤销" href="javascript:;" ' +
										'onclick="member_edit(\''+ row.learnId+ "@"+ row.grade+ "@"+ row.outTd+ "@"+ row.stdName+ "@" + _findDict("reason",row.reason)+ '\',true)" ' +
										'class="ml-5" style="text-decoration: none">';
									dom += '<i class="iconfont icon-tuichu"></i></a>';
								} else {
									dom = '<a title="进入审核" href="javascript:;" ' +
										'onclick="member_edit(\''+ row.learnId+ "@"+ row.grade+ "@"+ row.outTd+ "@"+ row.stdName+ "@"+ _findDict("reason",row.reason)+ '\',false)" ' +
										'class="ml-5" style="text-decoration: none">';
									dom += '<i class="iconfont icon-shenhe"></i></a>';
								}
								return dom;
							}}
						]
					});
					map[index] = myDataTable;
				} else {
					myDataTable = map[index];
				}
			});

			//标签块table-sortfinancial
			$.Huitab("#tab_demo .tabBar span", "#tab_demo .tabCon", "current",
					"click", "0");

			

			myDataTable = $('.table-sortfinancial').dataTable({
				"processing": true,
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : postUrl,
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
					{"mData" : "stdName"},
					{"mData" : null},
					{"mData" : null},
					{"mData" : null},
					{"mData" : null},
					{"mData" : null},
					{"mData" : null},
					{"mData" : "createUser"},
					{"mData" : null},
					{"mData" : "dpName"},    //招生部门
					{"mData" : "zsName"},    //招生老师
					{"mData" : null},
					{"mData" : null},
					{"mData" : null},
					{"mData" : null}
				],
				"columnDefs" : [
					{"targets" : 0,"render" : function(data,type,row,meta) {
						return '<input type="checkbox" class="sendIdsCheckBox" value="' + row.outTd + '" name="outTds"/>';
					}},
					{"targets" : 2,"render" : function(data, type, row, meta) {
						return _findDict("grade",row.grade);
					}},
					{"targets" : 3, "class":"text-l no-warp","render" : function(data,type,row,meta) {
						var dom='';
						_findDict("recruitType", row.recruitType).indexOf("成人")!=-1?dom+="[成教]":dom+="[国开]";
						dom+=row.unvsName+"<br>";
						_findDict("pfsnLevel", row.pfsnLevel).indexOf("高中")!=-1?dom+="[专科]":dom+="[本科]";
						dom+=row.pfsnName;
						return dom
					}},
					{"targets" : 4,"render" : function(data, type, row, meta) {
						return _findDict("scholarship",row.scholarship);
					}},
					{"targets" : 5,"render" : function(data, type, row, meta) {
						return _findDict("stdStage",row.stdStage);
					}},
					{"targets" : 6,"render" : function(data,type,row,meta) {
						return _findDict("reason",row.reason);
					}},
					{"targets" : 7,"render" : function(data,type,row,meta) {
						var amount=row.refundAmount;
						if(row.refundAmount==null||row.refundAmount==''||row.refundAmount=='0.00')
						amount='-';
						return amount;
					}},
					{"targets" : 9,"class":"text-l","render" : function(data,type, row, meta) {
						var dateTime = new Date(row.createTime).format('yyyy-MM-dd hh:mm:ss');
						if(dateTime==null||dateTime==''){
							return '-'
						}
						var date=dateTime.substring(0,10)
						var time=dateTime.substring(11)
						return date+'<br>'+time
					}},
					{"targets" : 12,"render" : function(data,type,row,meta) {
						var dom = '<a href="javascript:void(0);" style="text-decoration: none;color:#5485C7;" onclick="showRecordList(\''+ row.learnId+ "@"+ row.grade+ "@"+ row.outTd+ "@"+ row.stdName+  '\')">跟进记录</a><span>('+row.recordCount+')</span>';
						return dom;
						/*return '<a title="跟进记录(0)" href="javascript:;" ' +
								'onclick="member_genjin(\''+  row.outTd+ '\',\''+row.learnId+'\')" ' +
								'class="ml-5" style="text-decoration: none">';*/
					}},
					{"targets" : 13,"render" : function(data,type,row,meta) {
						return 2 == row.checkStatus ? '<span class="label label-success radius">' + _findDict("saStatus",row.checkStatus) + '</span>':
							'<span class="label-secondary label radius">'+ _findDict( "saStatus", row.checkStatus)+ '</span>';
					}},
					{"targets" : 14,"render" : function(data,type,row,meta) {
						if(1 == row.checkStatus){
                            return '-'
						}
						if(row.updateTime==null||row.updateTime==''){
							return '-'
						}
						var dateTime = new Date(row.updateTime).format('yyyy-MM-dd hh:mm:ss');
						if(dateTime==null||dateTime==''){
							return '-'
						}
						var date=dateTime.substring(0,10);
						var time=dateTime.substring(11);
						return date+'<br>'+time;
					}},
					{"targets" : 15,"render" : function(data,type,row,meta) {
						var dom = '';
						if (2 == row.checkStatus) {
							dom = '<a title="撤销" href="javascript:;" ' +
								'onclick="member_edit(\''+ row.learnId+ "@"+ row.grade+ "@"+ row.outTd+ "@"+ row.stdName+ "@" + _findDict("reason",row.reason)+ '\',true)" ' +
								'class="ml-5" style="text-decoration: none">';
							dom += '<i class="iconfont icon-tuichu"></i></a>';
						} else {
							dom = '<a title="进入审核" href="javascript:;" ' +
								'onclick="member_edit(\''+ row.learnId+ "@"+ row.grade+ "@"+ row.outTd+ "@"+ row.stdName+ "@"+ _findDict("reason",row.reason)+ '\',false)" ' +
								'class="ml-5" style="text-decoration: none">';
							dom += '<i class="iconfont icon-shenhe"></i></a>';
						}
						return dom;
					}}
				]
			});
			map[0] = myDataTable;

            $('.checkAll').on('click',function () {
                if($(this).prop("checked")){
                    $(this).parents('thead').siblings('tbody').find('.sendIdsCheckBox').each(function (i,e) {
                        if(!$(e).prop("checked")){
                            $(e).prop('checked',true)
                        }
                    })
                }else {
                    $(this).parents('thead').siblings('tbody').find('.sendIdsCheckBox').prop('checked',false)
                }
            })
            
            
            
		});
		
		

		/*用户-编辑*/
		function member_edit(outId, isUndo) {
			var url = editUrl + '?outId=' + outId + '&exType=UPDATE' + '&undo='
					+ isUndo;
			layer_show('审核', url, null, 510, function() {
				map[0].fnDraw(false);
				if (map[1]) {
					map[1].fnDraw(false);
				}
				if (map[2]) {
					map[2].fnDraw(false);
				}
				if (map[3]) {
					map[3].fnDraw(false);
				}
			},true);
		}

		/*用户-删除*/
		function member_del(obj, errorCode) {
			layer.confirm('确认要删除吗？', function(index) {
				$.ajax({
					type : 'POST',
					url : '/sysErrorMessage/deleteSysErrorMessage.do',
					data : {
						id : errorCode
					},
					dataType : 'json',
					success : function(data) {
						if (data.code == _GLOBAL_SUCCESS) {
							layer.msg('已删除!', {
								icon : 1,
								time : 1000
							});
							myDataTable.fnDraw(false);
							$("input[name=all]").attr("checked", false);
						}
					}
				});
			});
		}

		function delAll() {
			var chk_value = [];
			$("input[name=errorCodes]:checked").each(function() {
				chk_value.push($(this).val());
			});

			layer.confirm('确认要删除吗？', function(index) {
			    $.ajax({
					type : 'POST',
					url : '/sysErrorMessage/deleteAllSysErrorMessage.do',
					data : {
						idArray : chk_value
					},
					dataType : 'json',
					success : function(data) {
						if (data.code == _GLOBAL_SUCCESS) {
							layer.msg('已删除!', {
								icon : 1,
								time : 1000
							});
							myDataTable.fnDraw(false);
							$("input[name=all]").attr(
									"checked", false);
						}
					}
				});
			});
		}

		function uuid() {
			return Math.random().toString(36).substring(3, 8)
		}

		function _search() {
//			myDataTable.fnDraw(true);
            map[0].fnDraw(true);
            if (map[1]) {
                map[1].fnDraw(true);
            }
            if (map[2]) {
                map[2].fnDraw(true);
            }
            if (map[3]) {
                map[3].fnDraw(true);
            }
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

		function member_genjin(outId,learnId){
			 
		}
		
		/**
		 * 跟进记录
		 * @param outId
		 */
		function showRecordList(outId){
			var url = '/studentOutApproval/lookToRecordApproval.do' + '?outId='+outId;
		layer_show('查看跟进记录', url, null, 510, function() {
//							myDataTable.fnDraw(true);
			},true);
		}