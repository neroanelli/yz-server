var myDataTable;
		$(function() {
			$('select').select2({
				placeholder : "--请选择--",
				allowClear : true,
				width : "59%"
			});
            _init_select('stdStage', dictJson.stdStage);
            _init_select('scholarship', dictJson.scholarship);
			//初始状态
            _init_select("isRemark",[
                {"dictValue":"0","dictName":"无"},
                {"dictValue":"1","dictName":"有"}
            ]);
			_init_select("hasUsername",[
				{"dictValue":"1","dictName":"有"},
				{"dictValue":"0","dictName":"无"}
			]);
			_init_select("empStatus",[
				{"dictValue":"2","dictName":"是"},
				{"dictValue":"1","dictName":"否"}
			]);
			_init_select("hasRegisterNo",[
				{"dictValue":"1","dictName":"有"},
				{"dictValue":"0","dictName":"无"}
			]);
			_init_select("hasExamNo",[
				{"dictValue":"1","dictName":"有"},
				{"dictValue":"0","dictName":"无"}
			]);
			_init_select("educationAppraisal",[
				{"dictValue":"0","dictName":"不合格"},
				{"dictValue":"1","dictName":"合格"},
				{"dictValue":"2","dictName":"通过(应届生)"},
				{"dictValue":"3","dictName":"往届待验"},
				{"dictValue":"4","dictName":"无需验证"},
				{"dictValue":"5","dictName":"空"}
			]);
			
            _init_select("signStatus",[
                {"dictValue":"0","dictName":"未签到"},
                {"dictValue":"1","dictName":"已签到"}
            ]);
            _init_select("sceneConfirmStatus",[
                {"dictValue":"0","dictName":"未确认"},
                {"dictValue":"1","dictName":"确认成功"}
            ]);
            _init_select("webRegisterStatus",[
                {"dictValue":"0","dictName":"待网报"},
                {"dictValue":"1","dictName":"网报成功"}
            ]);
            _init_select("examPayStatus",[
                {"dictValue":"0","dictName":"否"},
                {"dictValue":"1","dictName":"是"}
            ]);
            _init_select("placeConfirmStatus",[
                {"dictValue":"0","dictName":"未预约"},
                {"dictValue":"1","dictName":"已预约"},
                {"dictValue":"2","dictName":"已重置"}
            ]);
            _init_select("isUnvsPfsnChange",[
                {"dictValue":"1","dictName":"一致"},
                {"dictValue":"0","dictName":"不一致"}
            ]);
            
            _init_select("workProve",[
                {"dictValue":"0","dictName":"无"},
                {"dictValue":"1","dictName":"有"}
            ]);
            _init_select("mobileBindStatus",[
                {"dictValue":"1","dictName":"未绑定"},
                {"dictValue":"2","dictName":"已绑定"}
            ]);
            _init_select("picCollectStatus",[
                {"dictValue":"1","dictName":"未采集"},
                {"dictValue":"2","dictName":"已采集"},
                {"dictValue":"3","dictName":"人工待验"}
            ]);
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
			//初始化年级下拉框
			_init_select("grade",dictJson.grade);

			//初始化院校类型下拉框
			_init_select("recruitType",dictJson.recruitType);
			_init_select("pfsnLevel",dictJson.pfsnLevel);
			$("#recruitType").change(function() {
				_init_select({
					selectId : 'grade',
					ext1 : $(this).val()
				}, dictJson.grade);
			});
			
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
	          
			myDataTable = $('.table-sort').dataTable({
				"processing": true,
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : '/bdSceneConfirm/findAllSceneConfirm.do',
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
					{"mData" : "grade"},
					{"mData" : null},
					{"mData" : null},
					{"mData" : null},
                    {"mData" : "taName"},
					{"mData" : null},
					{"mData" : null},
                    {"mData" : null},
                    {"mData" : null},
                    {"mData" : null},
                    {"mData" : null},
                    {"mData" : null},
                    {"mData" : null},
                    {"mData" : null},
                    {"mData" : null},
                    {"mData" : "examNo"},
					{"mData" : "workProve"},
                    {"mData" : "sceneRemark"},
					{"mData" : null}
				],
				"columnDefs" : [
						{
							"render" : function(data, type, row, meta) {
								return '<input type="checkbox" value="'+ row.confirmId + '##'+row.learnId+'##'+row.placeConfirmStatus+'" name="confirmIds"/>';
							},
							"targets" : 0
						},
						{"targets" : 3, "class" : "text-l", "render" : function(data, type, row, meta) {
							var pfsnName = row.pfsnName,unvsName = row.unvsName,recruitType = row.recruitType,
								 pfsnCode = row.pfsnCode,pfsnLevel = row.pfsnLevel,text = '';
							if (recruitType) {
								if(_findDict("recruitType", recruitType).indexOf("成人")!=-1){
									text += "[成教]";
								}else {
									text += "[国开]";
								}
							}
							if(unvsName) text += unvsName+'</br>';
							if(pfsnLevel) {
								if(_findDict("pfsnLevel", pfsnLevel).indexOf("高中")!=-1){
									text += "[专科]";
								}else {
									text += "[本科]";
								}
							}
							if(pfsnName) text += pfsnName;
							if(pfsnCode) text += "(" + pfsnCode + ")";
							if(row.isUnvsPfsnChange=='1'){
								text +="<br/><span style='color: red'>"+row.confirmUnvsName+"</span> <br/><span style='color: red'>"+row.confirmPfsnName+"</span>";
							}
							return text ? text : '无';
						}},
						{"targets" : 4,"render" : function(data, type, row, meta) {
                            return _findDict('stdStage', row.stdStage);
						}},
						{"targets" : 5,"render" : function(data, type, row, meta) {
							return _findDict('scholarship', row.scholarship);
						}},
						{"targets" : 7,"render" : function(data, type, row, meta) {
							if(row.placeConfirmStatus && row.placeConfirmStatus =='0'){
								return "<label class='label label-danger radius'>未预约</label>";
							}else if(row.placeConfirmStatus && row.placeConfirmStatus =='1'){
								return "<label class='label  label-success radius'>已预约</label>";
							}else if(row.placeConfirmStatus && row.placeConfirmStatus =='2'){
                                return "<label class='label'>已重置</label>";
                            }
							else {
								return '';
							}
						}},
						{"targets" : 8,"class" : "text-l","render" : function(data, type, row, meta) {
                            var dom = ''
                            dom += (row.confirmCity==null?"":"确认城市：" + row.confirmCity) + "<br/>";
                            dom +=  (row.confirmName==null?"":"确认点：" +row.confirmName) + "<br/>";
                            if (row.startTime) {
                                dom += "日期：" + row.startTime.substring(0, 10) + "<br/>";
                            } else {
                                dom += "";
                            }
                            if (row.startTime && row.endTime) {
                                dom += "时间段：" + row.startTime.substring(11, 16) + "-" + row.endTime.substring(11, 16) + "<br/>";
                            } else {
                                dom += "";
                            }
                            dom += (row.address==null?"":"地址：" +row.address);
							return dom;
						}},
						{"targets" : 9,"render" : function(data, type, row, meta) {
                            if(row.webRegisterStatus && row.webRegisterStatus=='1'){
                                return "<label class='label label-success radius'>网报成功</label>";
                            }else{
                                return "<label class='label label-danger radius'>待网报</label>";
                            }
						}},
                    {"targets" : 10,"render" : function(data, type, row, meta) {
                            if(row.mobileBindStatus && row.mobileBindStatus=='1'){
                                return "<label class='label label-success radius'>已绑定</label>";
                            }else{
                                return "<label class='label label-danger radius'>未绑定</label>";
                            }
                        }},
                    {"targets" : 11,"render" : function(data, type, row, meta) {
                            if(row.picCollectStatus && row.picCollectStatus=='1'){
                                return "<label class='label label-success radius'>已采集</label>";
                            }else if(row.picCollectStatus && row.picCollectStatus=='2'){
                                return "<label class='label label-danger radius'>人工待验</label>";
                            }
                            else{
                                return "<label class='label label-danger radius'>未采集</label>";
                            }
                        }},
						{"targets" : 12,"class" : "text-l","render" : function(data, type, row, meta) {
							var dom = ''
							dom +=  (row.username==null?"":"报名号："+row.username) + "<br/>";
							dom +=  (row.password==null?"":"密码：" +row.password) + "<br/>";
							dom +=  (row.registerNo==null?"":"编号：" +row.registerNo) + "<br/>";
							return dom;
						}},
						{"targets" : 13,"class" : "text-l","render" : function(data, type, row, meta) {
							var dom='';
							if(row.educationAppraisal && row.educationAppraisal=='0'){
								dom +=  "不合格";
							}else if(row.educationAppraisal && row.educationAppraisal =='1'){
								dom +=  "合格";
							}else if(row.educationAppraisal && row.educationAppraisal =='2'){
								dom +=  "通过(应届生)";
                            }else if(row.educationAppraisal && row.educationAppraisal =='3'){
								dom +=  "往届待验";
                            }else if(row.educationAppraisal && row.educationAppraisal =='4'){
								dom +=  "无需验证";
                            }
							return dom;
						}},
						{"targets" : 14,"render" : function(data, type, row, meta) {
							if(row.examPayStatus && row.examPayStatus=='1'){
								return "<label class='label label-success radius'>已缴费</label>";
							}else{
								return "<label class='label label-danger radius'>未缴费</label>";
							}
						}},
						{"targets" : 15,"render" : function(data, type, row, meta) {
							var dom = ''
							if(row.signStatus && row.signStatus=='1'){
								dom +=  "<label class='label label-success radius'>已签到</label><br/>";
								dom +=  (row.signTime==null?"":row.signTime);
							}else{
								dom +=  "<label class='label label-danger radius'>未签到</label>"
							}
							return dom;
						}},
						{"targets" : 16,"render" : function(data, type, row, meta) {
							if(row.sceneConfirmStatus && row.sceneConfirmStatus=='1'){
								return "<label class='label label-success radius'>确认成功</label>";
							}else{
								return "<label class='label label-danger radius'>未确认</label>";
							}
						}},
						{"targets" : 18,"render" : function(data, type, row, meta) {
							if(row.workProve && row.workProve=='1'){
								return "<label class='label label-success radius'>有</label>";
							}else{
								return "<label class='label label-danger radius'>无</label>";
							}
						}},
						{"targets" : 20,"render" : function(data, type, row, meta) {
							var dom = ''
							dom += '<a title="查看学员报考信息" href="javascript:void(0)" onclick="toViewExamInfo(\'' + row.learnId + '\')" class="ml-5" style="text-decoration:none">';
			                dom += '<i class="iconfont icon-chakan"></i></a>';
                            dom += '<a title="编辑" href="javascript:;" onclick="toEdit(\'' + row.confirmId + '\',\''+row.empId+'\')" class="ml-5" style="text-decoration: none">';
                            dom += '<i class="iconfont icon-edit"></i></a>';
							return dom;
						}}
				]
			});
		});

		function toEdit(confirmId,empId) {
			var url = '/bdSceneConfirm/toEdit.do?id='+ confirmId+'&empId='+empId;
			layer_show('学员信息编辑', url, null, null, function() {
                myDataTable.fnDraw(true);
			},true);
		}

		/*搜素*/
		function searchResult(){
			myDataTable.fnDraw(true);
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
		
	 /**
	  * 批量勾选重置
	  * @returns
	  */
	 function checkResetTask(){
			var chk_value = [];
			$("input[name=confirmIds]:checked").each(function() {
				chk_value.push($(this).val());
			});
			if(chk_value == null || chk_value.length <= 0||chk_value=='[]'){
				layer.msg('请选择要重置的预约任务！', {
					icon : 2,
					time : 2000
				});
				return;
			}
			layer.confirm('确认要批量重置预约任务吗？',function(index) {
				$.ajax({
					type : 'POST',
					url : '/bdSceneConfirm/checkResetTask.do',
					data : {
						confirmIds : chk_value
					},
					dataType : 'json',
					success : function(data) {
						if (data.code == _GLOBAL_SUCCESS) {
							layer.msg('重置成功!', {
								icon : 1,
								time : 2000
							});
							myDataTable.fnDraw(false);
							$("input[name=all]").attr("checked",false);
						}
					}
				});
			});
	 }
	 
	 function queryResetTask(){
		 var url = '/bdSceneConfirm/queryResetTask.do';	
		 if($("#placeConfirmStatus").val()==''){
				layer.msg('必须选择确认点预约筛选条件！', {icon : 2, time : 4000});
				return;
		 }
         layer.confirm('确认要批量重置预约任务吗？', function(index) {
             $.ajax({
                 type : 'POST',
                 url : url,
                 data: $.extend({}, $("#export-form").serializeObject()),
                 dataType : 'json',
                 success : function(data) {
                     if (data.code == _GLOBAL_SUCCESS) {
                         layer.msg('重置成功!', {
                             icon : 1,
                             time : 3000
                         });
                         myDataTable.fnDraw(false);
                     }
                 }
             });
         });
	 }
	 
	 /**
	  * 查看学员报考信息
	  * @returns
	  */
	 function toViewExamInfo(learnId){
		 var url = '/bdSceneConfirm/viewExamInfo.do?learnId='+ learnId;
			layer_show('查看学员报考信息', url, 1300, 700, function() {
             myDataTable.fnDraw(true);
			},false);
     }
	 
	function importKsh() {
	        var url = '/bdSceneConfirm/stuExamNoImport.do';
	        layer_show('导入考生号', url, null, 510, function () {
	            myDataTable.fnDraw(true);
	        });
	}
	
	/*现场确认统计*/
	function confirmStat() {
		var url = '/sceneConfirmStat/toSceneSonfirmStat.do' ;
		layer_show('现场确认统计', url, 800, 700, function() {
			myDataTable.fnDraw(true);
		},true);
	}
	
	function exportConfirmStudent(){
		var url = '/bdSceneConfirm/exportConfirmStudent.do';
		 if($("#placeConfirmStatus").val()==''){
			layer.msg('必须要选择的筛选条件是确认点预约为“已预约”或者“已重置”或者“未预约”时，方可导出！', {icon : 2, time : 4000});
			return;
	     }
		 $('<form method="post" accept-charset="UTF-8" action="' + url + '">'
				 + '<input type="text" name="stdName" value="' + $("#stdName").val() + '"/>'
				 + '<input type="text" name="idCard" value="' + $("#idCard").val() + '"/>'
				 + '<input type="text" name="mobile" value="' + $("#mobile").val() + '"/>'
				 + '<input type="text" name="grade" value="' + $("#grade").val() + '"/>'
				 + '<input type="text" name="unvsId" value="' + $("#unvsId").val() + '"/>'
				 + '<input type="text" name="pfsnLevel" value="' + $("#pfsnLevel").val() + '"/>'
				 + '<input type="text" name="pfsnId" value="' + $("#pfsnId").val() + '"/>'
				 + '<input type="text" name="stdStage" value="' + $("#stdStage").val() + '"/>'
				 + '<input type="text" name="scholarship" value="' + $("#scholarship").val() + '"/>'
				 + '<input type="text" name="taId" value="' + $("#taId").val() + '"/>'
				 + '<input type="text" name="recruit" value="' + $("#recruit").val() + '"/>'
				 + '<input type="text" name="dpId" value="' + $("#dpId").val() + '"/>'
				 + '<input type="text" name="placeConfirmStatus" value="' + $("#placeConfirmStatus").val() + '"/>'
				 + '<input type="text" name="content" value="' + $("#content").val() + '"/>'
				 + '<input type="text" name="hasUsername" value="' + $("#hasUsername").val() + '"/>'
				 + '<input type="text" name="hasRegisterNo" value="' + $("#hasRegisterNo").val() + '"/>'
				 + '<input type="text" name="educationAppraisal" value="' + $("#educationAppraisal").val() + '"/>'
				 + '<input type="text" name="signStatus" value="' + $("#signStatus").val() + '"/>'
				 + '<input type="text" name="signStartTime" value="' + $("#signStartTime").val() + '"/>'
				 + '<input type="text" name="signEndTime" value="' + $("#signEndTime").val() + '"/>'
				 + '<input type="text" name="hasExamNo" value="' + $("#hasExamNo").val() + '"/>'
				 + '<input type="text" name="sceneConfirmStatus" value="' + $("#sceneConfirmStatus").val() + '"/>'
				 + '<input type="text" name="isUnvsPfsnChange" value="' + $("#isUnvsPfsnChange").val() + '"/>'
				 + '<input type="text" name="workProve" value="' + $("#workProve").val() + '"/>'
				 + '<input type="text" name="isRemark" value="' + $("#isRemark").val() + '"/>'
				 + '<input type="text" name="empStatus" value="' + $("#empStatus").val() + '"/>'
	                + '<input type="text" name="startTime" value="' + $("#startTime").val() + '"/>'
	                + '<input type="text" name="endTime" value="' + $("#endTime").val() + '"/>'
	                + '<input type="text" name="webRegisterStatus" value="' + $("#webRegisterStatus").val() + '"/>'
	                + '<input type="text" name="mobileBindStatus" value="' + $("#mobileBindStatus").val() + '"/>'
	                + '<input type="text" name="picCollectStatus" value="' + $("#picCollectStatus").val() + '"/>'
	                + '<input type="text" name="examPayStatus" value="' + $("#examPayStatus").val() + '"/>'
	                + '</form>').appendTo('body').submit().remove();
       
	}

var myDataTable;
		$(function() {
			$('select').select2({
				placeholder : "--请选择--",
				allowClear : true,
				width : "59%"
			});
            _init_select('stdStage', dictJson.stdStage);
            _init_select('scholarship', dictJson.scholarship);
			//初始状态
            _init_select("isRemark",[
                {"dictValue":"0","dictName":"无"},
                {"dictValue":"1","dictName":"有"}
            ]);
			_init_select("hasUsername",[
				{"dictValue":"1","dictName":"有"},
				{"dictValue":"0","dictName":"无"}
			]);
			_init_select("empStatus",[
				{"dictValue":"2","dictName":"是"},
				{"dictValue":"1","dictName":"否"}
			]);
			_init_select("hasRegisterNo",[
				{"dictValue":"1","dictName":"有"},
				{"dictValue":"0","dictName":"无"}
			]);
			_init_select("hasExamNo",[
				{"dictValue":"1","dictName":"有"},
				{"dictValue":"0","dictName":"无"}
			]);
			_init_select("educationAppraisal",[
				{"dictValue":"0","dictName":"不合格"},
				{"dictValue":"1","dictName":"合格"},
				{"dictValue":"2","dictName":"通过(应届生)"},
				{"dictValue":"3","dictName":"往届待验"},
				{"dictValue":"4","dictName":"无需验证"},
				{"dictValue":"5","dictName":"空"}
			]);
			
            _init_select("signStatus",[
                {"dictValue":"0","dictName":"未签到"},
                {"dictValue":"1","dictName":"已签到"}
            ]);
            _init_select("sceneConfirmStatus",[
                {"dictValue":"0","dictName":"未确认"},
                {"dictValue":"1","dictName":"确认成功"}
            ]);
            _init_select("webRegisterStatus",[
                {"dictValue":"0","dictName":"待网报"},
                {"dictValue":"1","dictName":"网报成功"}
            ]);
            _init_select("examPayStatus",[
                {"dictValue":"0","dictName":"否"},
                {"dictValue":"1","dictName":"是"}
            ]);
            _init_select("placeConfirmStatus",[
                {"dictValue":"0","dictName":"未预约"},
                {"dictValue":"1","dictName":"已预约"},
                {"dictValue":"2","dictName":"已重置"}
            ]);
            _init_select("isUnvsPfsnChange",[
                {"dictValue":"1","dictName":"一致"},
                {"dictValue":"0","dictName":"不一致"}
            ]);
            
            _init_select("workProve",[
                {"dictValue":"0","dictName":"无"},
                {"dictValue":"1","dictName":"有"}
            ]);
            _init_select("mobileBindStatus",[
                {"dictValue":"1","dictName":"未绑定"},
                {"dictValue":"2","dictName":"已绑定"}
            ]);
            _init_select("picCollectStatus",[
                {"dictValue":"1","dictName":"未采集"},
                {"dictValue":"2","dictName":"已采集"},
                {"dictValue":"3","dictName":"人工待验"}
            ]);
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
			//初始化年级下拉框
			_init_select("grade",dictJson.grade);

			//初始化院校类型下拉框
			_init_select("recruitType",dictJson.recruitType);
			_init_select("pfsnLevel",dictJson.pfsnLevel);
			$("#recruitType").change(function() {
				_init_select({
					selectId : 'grade',
					ext1 : $(this).val()
				}, dictJson.grade);
			});
			
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
	          
			myDataTable = $('.table-sort').dataTable({
				"processing": true,
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : '/bdSceneConfirm/findAllSceneConfirm.do',
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
					{"mData" : "grade"},
					{"mData" : null},
					{"mData" : null},
					{"mData" : null},
                    {"mData" : "taName"},
					{"mData" : null},
					{"mData" : null},
                    {"mData" : null},
                    {"mData" : null},
                    {"mData" : null},
                    {"mData" : null},
                    {"mData" : null},
                    {"mData" : null},
                    {"mData" : null},
                    {"mData" : null},
                    {"mData" : "examNo"},
					{"mData" : "workProve"},
                    {"mData" : "sceneRemark"},
					{"mData" : null}
				],
				"columnDefs" : [
						{
							"render" : function(data, type, row, meta) {
								return '<input type="checkbox" value="'+ row.confirmId + '##'+row.learnId+'##'+row.placeConfirmStatus+'" name="confirmIds"/>';
							},
							"targets" : 0
						},
						{"targets" : 3, "class" : "text-l", "render" : function(data, type, row, meta) {
							var pfsnName = row.pfsnName,unvsName = row.unvsName,recruitType = row.recruitType,
								 pfsnCode = row.pfsnCode,pfsnLevel = row.pfsnLevel,text = '';
							if (recruitType) {
								if(_findDict("recruitType", recruitType).indexOf("成人")!=-1){
									text += "[成教]";
								}else {
									text += "[国开]";
								}
							}
							if(unvsName) text += unvsName+'</br>';
							if(pfsnLevel) {
								if(_findDict("pfsnLevel", pfsnLevel).indexOf("高中")!=-1){
									text += "[专科]";
								}else {
									text += "[本科]";
								}
							}
							if(pfsnName) text += pfsnName;
							if(pfsnCode) text += "(" + pfsnCode + ")";
							if(row.isUnvsPfsnChange=='1'){
								text +="<br/><span style='color: red'>"+row.confirmUnvsName+"</span> <br/><span style='color: red'>"+row.confirmPfsnName+"</span>";
							}
							return text ? text : '无';
						}},
						{"targets" : 4,"render" : function(data, type, row, meta) {
                            return _findDict('stdStage', row.stdStage);
						}},
						{"targets" : 5,"render" : function(data, type, row, meta) {
							return _findDict('scholarship', row.scholarship);
						}},
						{"targets" : 7,"render" : function(data, type, row, meta) {
							if(row.placeConfirmStatus && row.placeConfirmStatus =='0'){
								return "<label class='label label-danger radius'>未预约</label>";
							}else if(row.placeConfirmStatus && row.placeConfirmStatus =='1'){
								return "<label class='label  label-success radius'>已预约</label>";
							}else if(row.placeConfirmStatus && row.placeConfirmStatus =='2'){
                                return "<label class='label'>已重置</label>";
                            }
							else {
								return '';
							}
						}},
						{"targets" : 8,"class" : "text-l","render" : function(data, type, row, meta) {
                            var dom = ''
                            dom += (row.confirmCity==null?"":"确认城市：" + row.confirmCity) + "<br/>";
                            dom +=  (row.confirmName==null?"":"确认点：" +row.confirmName) + "<br/>";
                            if (row.startTime) {
                                dom += "日期：" + row.startTime.substring(0, 10) + "<br/>";
                            } else {
                                dom += "";
                            }
                            if (row.startTime && row.endTime) {
                                dom += "时间段：" + row.startTime.substring(11, 16) + "-" + row.endTime.substring(11, 16) + "<br/>";
                            } else {
                                dom += "";
                            }
                            dom += (row.address==null?"":"地址：" +row.address);
							return dom;
						}},
						{"targets" : 9,"render" : function(data, type, row, meta) {
                            if(row.webRegisterStatus && row.webRegisterStatus=='1'){
                                return "<label class='label label-success radius'>网报成功</label>";
                            }else{
                                return "<label class='label label-danger radius'>待网报</label>";
                            }
						}},
                    {"targets" : 10,"render" : function(data, type, row, meta) {
                            if(row.mobileBindStatus && row.mobileBindStatus=='1'){
                                return "<label class='label label-success radius'>已绑定</label>";
                            }else{
                                return "<label class='label label-danger radius'>未绑定</label>";
                            }
                        }},
                    {"targets" : 11,"render" : function(data, type, row, meta) {
                            if(row.picCollectStatus && row.picCollectStatus=='1'){
                                return "<label class='label label-success radius'>已采集</label>";
                            }else if(row.picCollectStatus && row.picCollectStatus=='2'){
                                return "<label class='label label-danger radius'>人工待验</label>";
                            }
                            else{
                                return "<label class='label label-danger radius'>未采集</label>";
                            }
                        }},
						{"targets" : 12,"class" : "text-l","render" : function(data, type, row, meta) {
							var dom = ''
							dom +=  (row.username==null?"":"报名号："+row.username) + "<br/>";
							dom +=  (row.password==null?"":"密码：" +row.password) + "<br/>";
							dom +=  (row.registerNo==null?"":"编号：" +row.registerNo) + "<br/>";
							return dom;
						}},
						{"targets" : 13,"class" : "text-l","render" : function(data, type, row, meta) {
							var dom='';
							if(row.educationAppraisal && row.educationAppraisal=='0'){
								dom +=  "不合格";
							}else if(row.educationAppraisal && row.educationAppraisal =='1'){
								dom +=  "合格";
							}else if(row.educationAppraisal && row.educationAppraisal =='2'){
								dom +=  "通过(应届生)";
                            }else if(row.educationAppraisal && row.educationAppraisal =='3'){
								dom +=  "往届待验";
                            }else if(row.educationAppraisal && row.educationAppraisal =='4'){
								dom +=  "无需验证";
                            }
							return dom;
						}},
						{"targets" : 14,"render" : function(data, type, row, meta) {
							if(row.examPayStatus && row.examPayStatus=='1'){
								return "<label class='label label-success radius'>已缴费</label>";
							}else{
								return "<label class='label label-danger radius'>未缴费</label>";
							}
						}},
						{"targets" : 15,"render" : function(data, type, row, meta) {
							var dom = ''
							if(row.signStatus && row.signStatus=='1'){
								dom +=  "<label class='label label-success radius'>已签到</label><br/>";
								dom +=  (row.signTime==null?"":row.signTime);
							}else{
								dom +=  "<label class='label label-danger radius'>未签到</label>"
							}
							return dom;
						}},
						{"targets" : 16,"render" : function(data, type, row, meta) {
							if(row.sceneConfirmStatus && row.sceneConfirmStatus=='1'){
								return "<label class='label label-success radius'>确认成功</label>";
							}else{
								return "<label class='label label-danger radius'>未确认</label>";
							}
						}},
						{"targets" : 18,"render" : function(data, type, row, meta) {
							if(row.workProve && row.workProve=='1'){
								return "<label class='label label-success radius'>有</label>";
							}else{
								return "<label class='label label-danger radius'>无</label>";
							}
						}},
						{"targets" : 20,"render" : function(data, type, row, meta) {
							var dom = ''
							dom += '<a title="查看学员报考信息" href="javascript:void(0)" onclick="toViewExamInfo(\'' + row.learnId + '\')" class="ml-5" style="text-decoration:none">';
			                dom += '<i class="iconfont icon-chakan"></i></a>';
                            dom += '<a title="编辑" href="javascript:;" onclick="toEdit(\'' + row.confirmId + '\',\''+row.empId+'\')" class="ml-5" style="text-decoration: none">';
                            dom += '<i class="iconfont icon-edit"></i></a>';
							return dom;
						}}
				]
			});
		});

		function toEdit(confirmId,empId) {
			var url = '/bdSceneConfirm/toEdit.do?id='+ confirmId+'&empId='+empId;
			layer_show('学员信息编辑', url, null, null, function() {
                myDataTable.fnDraw(true);
			},true);
		}

		/*搜素*/
		function searchResult(){
			myDataTable.fnDraw(true);
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
		
	 /**
	  * 批量勾选重置
	  * @returns
	  */
	 function checkResetTask(){
			var chk_value = [];
			$("input[name=confirmIds]:checked").each(function() {
				chk_value.push($(this).val());
			});
			if(chk_value == null || chk_value.length <= 0||chk_value=='[]'){
				layer.msg('请选择要重置的预约任务！', {
					icon : 2,
					time : 2000
				});
				return;
			}
			layer.confirm('确认要批量重置预约任务吗？',function(index) {
				$.ajax({
					type : 'POST',
					url : '/bdSceneConfirm/checkResetTask.do',
					data : {
						confirmIds : chk_value
					},
					dataType : 'json',
					success : function(data) {
						if (data.code == _GLOBAL_SUCCESS) {
							layer.msg('重置成功!', {
								icon : 1,
								time : 2000
							});
							myDataTable.fnDraw(false);
							$("input[name=all]").attr("checked",false);
						}
					}
				});
			});
	 }
	 
	 function queryResetTask(){
		 var url = '/bdSceneConfirm/queryResetTask.do';	
		 if($("#placeConfirmStatus").val()==''){
				layer.msg('必须选择确认点预约筛选条件！', {icon : 2, time : 4000});
				return;
		 }
         layer.confirm('确认要批量重置预约任务吗？', function(index) {
             $.ajax({
                 type : 'POST',
                 url : url,
                 data: $.extend({}, $("#export-form").serializeObject()),
                 dataType : 'json',
                 success : function(data) {
                     if (data.code == _GLOBAL_SUCCESS) {
                         layer.msg('重置成功!', {
                             icon : 1,
                             time : 3000
                         });
                         myDataTable.fnDraw(false);
                     }
                 }
             });
         });
	 }
	 
	 /**
	  * 查看学员报考信息
	  * @returns
	  */
	 function toViewExamInfo(learnId){
		 var url = '/bdSceneConfirm/viewExamInfo.do?learnId='+ learnId;
			layer_show('查看学员报考信息', url, 1300, 700, function() {
             myDataTable.fnDraw(true);
			},false);
     }
	 
	function importKsh() {
	        var url = '/bdSceneConfirm/stuExamNoImport.do';
	        layer_show('导入考生号', url, null, 510, function () {
	            myDataTable.fnDraw(true);
	        });
	}
	
	/*现场确认统计*/
	function confirmStat() {
		var url = '/sceneConfirmStat/toSceneSonfirmStat.do' ;
		layer_show('现场确认统计', url, 800, 700, function() {
			myDataTable.fnDraw(true);
		},true);
	}
	
	function exportConfirmStudent(){
		var url = '/bdSceneConfirm/exportConfirmStudent.do';
		 if($("#placeConfirmStatus").val()==''){
			layer.msg('必须要选择的筛选条件是确认点预约为“已预约”或者“已重置”或者“未预约”时，方可导出！', {icon : 2, time : 4000});
			return;
	     }
		 $('<form method="post" accept-charset="UTF-8" action="' + url + '">'
				 + '<input type="text" name="stdName" value="' + $("#stdName").val() + '"/>'
				 + '<input type="text" name="idCard" value="' + $("#idCard").val() + '"/>'
				 + '<input type="text" name="mobile" value="' + $("#mobile").val() + '"/>'
				 + '<input type="text" name="grade" value="' + $("#grade").val() + '"/>'
				 + '<input type="text" name="unvsId" value="' + $("#unvsId").val() + '"/>'
				 + '<input type="text" name="pfsnLevel" value="' + $("#pfsnLevel").val() + '"/>'
				 + '<input type="text" name="pfsnId" value="' + $("#pfsnId").val() + '"/>'
				 + '<input type="text" name="stdStage" value="' + $("#stdStage").val() + '"/>'
				 + '<input type="text" name="scholarship" value="' + $("#scholarship").val() + '"/>'
				 + '<input type="text" name="taId" value="' + $("#taId").val() + '"/>'
				 + '<input type="text" name="recruit" value="' + $("#recruit").val() + '"/>'
				 + '<input type="text" name="dpId" value="' + $("#dpId").val() + '"/>'
				 + '<input type="text" name="placeConfirmStatus" value="' + $("#placeConfirmStatus").val() + '"/>'
				 + '<input type="text" name="content" value="' + $("#content").val() + '"/>'
				 + '<input type="text" name="hasUsername" value="' + $("#hasUsername").val() + '"/>'
				 + '<input type="text" name="hasRegisterNo" value="' + $("#hasRegisterNo").val() + '"/>'
				 + '<input type="text" name="educationAppraisal" value="' + $("#educationAppraisal").val() + '"/>'
				 + '<input type="text" name="signStatus" value="' + $("#signStatus").val() + '"/>'
				 + '<input type="text" name="signStartTime" value="' + $("#signStartTime").val() + '"/>'
				 + '<input type="text" name="signEndTime" value="' + $("#signEndTime").val() + '"/>'
				 + '<input type="text" name="hasExamNo" value="' + $("#hasExamNo").val() + '"/>'
				 + '<input type="text" name="sceneConfirmStatus" value="' + $("#sceneConfirmStatus").val() + '"/>'
				 + '<input type="text" name="isUnvsPfsnChange" value="' + $("#isUnvsPfsnChange").val() + '"/>'
				 + '<input type="text" name="workProve" value="' + $("#workProve").val() + '"/>'
				 + '<input type="text" name="isRemark" value="' + $("#isRemark").val() + '"/>'
				 + '<input type="text" name="empStatus" value="' + $("#empStatus").val() + '"/>'
	                + '<input type="text" name="startTime" value="' + $("#startTime").val() + '"/>'
	                + '<input type="text" name="endTime" value="' + $("#endTime").val() + '"/>'
	                + '<input type="text" name="webRegisterStatus" value="' + $("#webRegisterStatus").val() + '"/>'
	                + '<input type="text" name="mobileBindStatus" value="' + $("#mobileBindStatus").val() + '"/>'
	                + '<input type="text" name="picCollectStatus" value="' + $("#picCollectStatus").val() + '"/>'
	                + '<input type="text" name="examPayStatus" value="' + $("#examPayStatus").val() + '"/>'
	                + '</form>').appendTo('body').submit().remove();
       
	}
	