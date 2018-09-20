var myDataTable;
		$(function() {
			$('select').select2({
				placeholder : "--请选择--",
				allowClear : true,
				width : "59%"
			});
            _init_select('stdStage', dictJson.stdStage);
            _init_select('scholarship', dictJson.scholarship);
            _init_select('empStatus', dictJson.empStatus);
            
			//初始状态
            _init_select("isRemark",[
                {"dictValue":"0","dictName":"无"},
                {"dictValue":"1","dictName":"有"}
            ]);
            _init_select("isSceneRemark",[
                {"dictValue":"0","dictName":"否"},
                {"dictValue":"1","dictName":"是"}
            ]);
			_init_select("hasUsername",[
				{"dictValue":"1","dictName":"有"},
				{"dictValue":"0","dictName":"无"}
			]);
			_init_select("hasRegisterNo",[
				{"dictValue":"1","dictName":"是"},
				{"dictValue":"0","dictName":"否"}
			]);
			_init_select("empStatus",[
  				{"dictValue":"2","dictName":"是"},
  				{"dictValue":"1","dictName":"否"}
  			]);
			_init_select("hasExamNo",[
				{"dictValue":"1","dictName":"有"},
				{"dictValue":"0","dictName":"无"}
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
                {"dictValue":"0","dictName":"待网待"},
                {"dictValue":"1","dictName":"网报成功"}
            ]);
            _init_select("examPayStatus",[
                {"dictValue":"0","dictName":"未缴费"},
                {"dictValue":"1","dictName":"已缴费"}
            ]);
            _init_select("educationAppraisal",[
                {"dictValue":"0","dictName":"不合格"},
                {"dictValue":"1","dictName":"合格"},
                {"dictValue":"2","dictName":"通过(应届生)"},
                {"dictValue":"3","dictName":"往届待验"},
                {"dictValue":"4","dictName":"无需验证"}
            ]);
            _init_select("placeConfirmStatus",[
                {"dictValue":"0","dictName":"未预约"},
                {"dictValue":"1","dictName":"已预约"},
                {"dictValue":"2","dictName":"已重置"}
            ]);
            _init_select("workProve",[
                {"dictValue":"0","dictName":"无"},
                {"dictValue":"1","dictName":"有"}
            ]);
            _init_select("examInfoConfirmStatus",[
                {"dictValue":"0","dictName":"未确认"},
                {"dictValue":"1","dictName":"已确认"}
            ]);
            _init_select("isUnvsPfsnChange",[
                {"dictValue":"1","dictName":"一致"},
                {"dictValue":"0","dictName":"不一致"}
            ]);
            _init_select("mobileBindStatus",[
                {"dictValue":"2","dictName":"已绑定"},
                {"dictValue":"1","dictName":"未绑定"}
            ]);
            _init_select("picCollectStatus",[
                {"dictValue":"2","dictName":"已采集"},
                {"dictValue":"1","dictName":"未采集"},
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
            $.ajax({
                type: "POST",
                dataType : "json", //鏁版嵁绫诲瀷
                url: '/sceneManagement/getExamDicName.do',
                success: function(data){
                    examDicJson = data.body;
                    if(data.code=='00'){
                        _init_select("taId",examDicJson);
                    }
                }
            });

			//初始化院校类型下拉框
			_init_select("recruitType",dictJson.recruitType);
			_init_select("pfsnLevel",dictJson.pfsnLevel);
			$("#recruitType").change(function() {
				_init_select({
					selectId : 'grade',
					ext1 : $(this).val()
				}, dictJson.grade);
			});
			
		  	_simple_ajax_select({
				selectId : "taskId",
				searchUrl : '/studyActivity/findTaskInfo.do?taskType=7',
				sData : {},
				showText : function(item) {
					return item.task_title;
				},					
				showId : function(item) {
					return item.task_id;
				},
				placeholder : '--请选择任务--'
			});
			$("#taskId").append(new Option("", "", false, true));
			
			myDataTable = $('.table-sort').dataTable({
				"processing": true,
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : '/sceneConfirm/findAllSceneConfirm.do',
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
					{"mData" : "stdName"},
					{"mData" : "grade"},
					{"mData" : null},
					{"mData" : null},
					{"mData" : "recruit"},
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
                    {"mData" : "remark"},
                    {"mData" : "sceneRemark"},
					{"mData" : null}
				],
				"columnDefs" : [
						{"targets" : 2, "class" : "text-l", "render" : function(data, type, row, meta) {
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
						{"targets" : 3,"render" : function(data, type, row, meta) {
                            return _findDict('stdStage', row.stdStage);
						}},
						{"targets" : 5,"render" : function(data, type, row, meta) {
							return _findDict('scholarship', row.scholarship);
						}},
						{"targets" : 7,"render" : function(data, type, row, meta) {
							if(row.examInfoConfirmStatus && row.examInfoConfirmStatus =='1'){
								return "<label class='label label-success radius'>已确认</label>";
							}else if(row.examInfoConfirmStatus && row.examInfoConfirmStatus =='0'){
								return "<label class='label  label-danger radius'>未确认</label>";
							}else {
								return '';
                            }
						}},
						{"targets" : 8,"render" : function(data, type, row, meta) {
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
						{"targets" : 9,"class" : "text-l","render" : function(data, type, row, meta) {
                            var dom = ''
                            dom += (row.confirmCity==null?"":"确认城市：" + row.confirmCity) + "<br/>";
                            dom +=  (row.confirmName==null?"":"确认点：" +row.confirmName) + "<br/>";
                            if (row.startTime) {
                                var weekday=['星期日','星期一','星期二','星期三','星期四','星期五','星期六'];
                                var week = weekday[new Date(row.startTime.substring(0,19).replace(/-/g,'/')).getDay()];
                                dom += "日期：" + row.startTime.substring(0, 10) + "("+week+")<br/>";
                            } else {
                                dom += "";
                            }
                            if (row.startTime && row.endTime) {
                            	var startTime = row.startTime.substring(11, 16);
                            	var hour =  row.startTime.substring(11, 13);
                            	var str=''
								if(hour<12){
                                    str='上午'
								}
								else if(hour>=12 && hour<=18){
									str='下午'
								}else {
									str='晚上'
								}
                                dom += "时间段：" +str+ startTime + "-" + row.endTime.substring(11, 16) + "<br/>";
                            } else {
                                dom += "";
                            }
                            dom += (row.address==null?"":"地址：" +row.address);
							return dom;
						}},
						{"targets" : 10,"render" : function(data, type, row, meta) {
                            if(row.webRegisterStatus && row.webRegisterStatus=='1'){
                                return "<label class='label label-success radius'>网报成功</label>";
                            }else{
                                return "<label class='label label-danger radius'>待网报</label>";
                            }
						}},
                    {"targets" : 11,"render" : function(data, type, row, meta) {
                            if(row.mobileBindStatus && row.mobileBindStatus=='1'){
                                return "<label class='label label-success radius'>已绑定</label>";
                            }else{
                                return "<label class='label label-danger radius'>未绑定</label>";
                            }
                        }},
                    {"targets" : 12,"render" : function(data, type, row, meta) {
                            if(row.picCollectStatus && row.picCollectStatus=='1'){
                                return "<label class='label label-success radius'>已采集</label>";
                            } else if(row.picCollectStatus && row.picCollectStatus=='2'){
                                return "<label class='label label-danger radius'>人工待验</label>";
							}
                            else{
                                return "<label class='label label-danger radius'>未采集</label>";
                            }
                        }},
                    {"targets" : 13,"render" : function(data, type, row, meta) {
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
							if(row.signStatus && row.signStatus=='1'){
								return "<label class='label label-success radius'>已签到</label>";
							}else{
								return "<label class='label label-danger radius'>未签到</label>";
							}
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
						{"targets" : 21,"render" : function(data, type, row, meta) {
							var dom = ''
                            dom += '<a title="编辑" href="javascript:;" onclick="toEdit(\'' + row.confirmId + '\',\''+row.empId+'\')" class="ml-5" style="text-decoration: none">';
                            dom += '<i class="iconfont icon-edit"></i></a>';
                            dom += '<a title="刷新状态" href="javascript:;" onclick="refresh(\'' + row.learnId +'\')" class="ml-5" style="text-decoration: none">';
                            dom += '<i class="iconfont icon-shuaxin"></i></a>';
							return dom;
						}}
				]
			});
		});

		function toEdit(confirmId,empId) {
			var url = '/sceneConfirm/toEdit.do?id='+ confirmId+'&empId='+empId;
			layer_show('学员信息编辑', url, 900, 700, function() {
                //myDataTable.fnDraw(true);
			},false);
		}

		/*现场确认统计*/
		function confirmStat() {
			var url = '/sceneConfirmStat/toSceneSonfirmStat.do' ;
			layer_show('现场确认统计', url, 800, 700, function() {
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

function refresh(learnId) {
    $.ajax({
        type: "POST",
        dataType : "json",
        url: '/exam/regNet/getBaseInfoStatus.do?learnId='+learnId,
        success: function(data){
            examDicJson = data.body;
            if(data.code=='00'){
                layer.msg("刷新成功")
            }
        }
    });
}