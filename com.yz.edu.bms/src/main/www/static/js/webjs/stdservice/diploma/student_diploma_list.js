var myDataTable;
		$(function() {
			$('select').select2({
				placeholder : "--请选择--",
				allowClear : true,
				width : "59%"
			});
			//初始状态
			_init_select("isView",[
				{"dictValue":"1","dictName":"是"},
				{"dictValue":"0","dictName":"否"}
			]);
			_init_select("isAffirm",[
				{"dictValue":"1","dictName":"是"},
				{"dictValue":"0","dictName":"否"}
			]);			
			_init_select("isReset",[
				{"dictValue":"1","dictName":"是"},
				{"dictValue":"0","dictName":"否"}
			]);
            _init_select("isRemark",[
                {"dictValue":"0","dictName":"否"},
                {"dictValue":"1","dictName":"是"}
            ]);
            _init_select("isUnconfirmReason",[
                {"dictValue":"0","dictName":"否"},
                {"dictValue":"1","dictName":"是"}
            ]);
            _init_select("isMail",[
                {"dictValue":"0","dictName":"否"},
                {"dictValue":"1","dictName":"是"}
            ]);
            _init_select("isLogistics",[
                {"dictValue":"0","dictName":"否"},
                {"dictValue":"1","dictName":"是"}
            ]);
            _init_select("isInvoiceNo",[
                {"dictValue":"0","dictName":"无"},
                {"dictValue":"1","dictName":"有"}
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
			
			_init_select("unconfirmReason",dictJson.unconfirmReason);
			_init_select("receiveStatus",dictJson.diplomaReceiveStatus);
			
			_init_select("pfsnLevel",dictJson.pfsnLevel);
			
			
		  	_simple_ajax_select({
				selectId : "taskId",
				searchUrl : '/studyActivity/findTaskInfo.do?taskType=15',
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
			
			$("#taskId").change(function () {
	            $("#affirmTime").removeAttr("disabled");
	            init_affirmTime_select();
		     });
			
			
			
			
			
			myDataTable = $('.table-sort').dataTable({
				"processing": true,
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : '/diploma/findAllDiplomaList.do',
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
					{"mData" : "taskTitle"},
					{"mData" : "stdName"},
					{"mData" : "schoolRoll"},
					{"mData" : "grade"},
					{"mData" : null},
					{"mData" : "diplomaCode"},
					{"mData" : "isAffirm"},
					{"mData" : null},
					{"mData" : "receiveAddress"},
                    {"mData" : "unconfirmReason"},
					{"mData" : "receiveStatus"},
					{"mData" : "receiveTime"},
					{"mData" : "isMail"},
					{"mData" : "invoiceNo"},
					{"mData" : "tutor"},
					{"mData" : "isReset"},
					{"mData" : "remark"},
					{"mData" : null}
				],
				"columnDefs" : [
					{"targets" : 1,"render" : function(data, type, row, meta) {
						var dom = '<a title="查看学员信息" onclick="toEidt(\''+ row.learnId +'\',\''+ row.stdId +'\',\''+ row.recruitType +'\')"><span class="c-blue">'+ row.stdName +'</span></a>'
						return dom;
					}},
					{"targets" : 3,"render" : function(data, type, row, meta) {
						return _findDict("grade", row.grade);
					}},
					{"targets" : 4, "class" : "text-l", "render" : function(data, type, row, meta) {
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
							return text ? text : '无';
						}},
						{"targets" : 6,"render" : function(data, type, row, meta) {
							if(row.isAffirm && row.isAffirm =='1'){
								return "<label class='label label-success radius'>是</label>";
							}else{
								return "<label class='label label-danger radius'>否</label>";
							}
						}},
						{"targets" : 7,"class":"text-l no-warp","render" : function(data, type, row, meta) {
							if(row.affirmStartTime){								
								var dateTime=row.affirmStartTime.replace('AM', '上午').replace('PM', '下午') + "-" + row.affirmEndTime;
		                        if(!dateTime){
		                            return '-'
		                        }
		                        var date=dateTime.substring(0,10)
		                        var time=dateTime.substring(11)
		                        return date+'<br>'+time
							}else{
								return '';
							}
						}},
						{"targets" : 9,"render" : function(data, type, row, meta) {
							return _findDict("unconfirmReason", row.unconfirmReason);
						}},
						{"targets" : 10,"render" : function(data, type, row, meta) {
							return _findDict("diplomaReceiveStatus", row.receiveStatus);
						}},
						{"targets" : 12,"render" : function(data, type, row, meta) {
							if(row.isMail && row.isMail =='1'){
								return "<label class='label label-success radius'>是</label>";
							}else{
								return "<label class='label label-danger radius'>否</label>";
							}
						}},
						{"targets" : 15,"render" : function(data, type, row, meta) {
							if(row.isReset && row.isReset=='1'){
								return "<label class='label label-success radius'>是</label>";
							}else{
								return "<label class='label label-danger radius'>否</label>";
							}
						}},
						{"targets" : 17,"class":"text-l no-warp","render" : function(data, type, row, meta) {
							var dom='';
							if(row.receiveStatus=='0'){
								dom += '<a class="tableBtn normal" href="javascript:;" title="已领" onclick="setReceive(\'' + row.followId + '\')">已领</a>';
							}
							dom += '<a class="tableBtn normal" href="javascript:;" title="设置" onclick="setDiploma(\'' + row.followId + '\')">设置</a>';
							dom += '<a class="tableBtn normal" href="javascript:;" title="查看详情" onclick="toDetails(\'' + row.followId + '\')">查看详情</a>';
							dom += '<a class="tableBtn normal" href="javascript:;" title="备注" onclick="setRemark(\'' + row.followId + '\')">备注</a>';
							return dom;
						}}
				]
			});
		});

		/*设置*/
		function setDiploma(followId) {
			var url = '/diploma/toEdit.do' + '?followId='+ followId ;
			layer_show('编辑', url, 880, 650, function() {
			});
		}
		/*查看详情*/
		function toDetails(followId) {
			var url = '/diploma/toDetails.do' + '?followId='+ followId ;
			layer_show('查看详情', url, 880, 650, function() {
			});
		}
		/*备注*/
		function setRemark(followId) {
			var url = '/diploma/toSetRemark.do' + '?followId='+ followId ;
			layer_show('备注', url, 880, 500, function() {
			});
		}

		/*搜素*/
		function searchResult(){
			myDataTable.fnDraw(true);
		}
		
		function init_affirmTime_select() {
			_simple_ajax_select({
				selectId : "affirmTime",
				searchUrl : '/diploma/findAffirmTimeList.do',
				sData: {
                    taskId: function () {
                        return $("#taskId").val();
                    }
                },
				showText : function(item) {
					return item.time;
				},					
				showId : function(item) {
					return item.config_id;
				},
				placeholder : '--请选择任务--'
			});
			$("#affirmTime").append(new Option("", "", false, true));
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
		/*用户-编辑*/
		function toEidt(learnId, stdId,recruitType) {
			var url = '/studentBase/toEdit.do' + '?learnId='
					+ learnId + '&stdId=' + stdId+"&recruitType="+recruitType;
			layer_show('学员信息', url, null, null, function() {
				myDataTable.fnDraw(false);
			}, true);
		}
	    function diplomaImport() {
	        var url = '/diploma/stuDiplomaImport.do';
	        layer_show('编号导入', url, null, 510, function () {
	            myDataTable.fnDraw(true);
	        });
	    }
	    function diplomaExport(){
        	$("#export-form").submit();
        }
	    /**
	     * 统计
	     * @returns
	     */
	    function diplomaStat(){
	    	var url = '/diploma/toStatistics.do';
			layer_show('筛选统计', url, 1200, 700, function() {
				 myDataTable.fnDraw(true);
			});
	    }
	    
	    function setReceive(followId){
	    	var urlData="/diploma/receiveAffirmSet.do";
	    	$.ajax({
	            type : "post", //提交方式
	            dataType : "json", //数据类型
	            url : urlData, //请求url
	    			data:{ 
	    				followId:followId,
	    				receiveStatus:"1"
	    			},
	    			success : function(data) { //提交成功的回调函数
	                if(data.code == _GLOBAL_SUCCESS){
	                	layer.msg('设置成功！', {icon : 2, time : 4000});
	                }
	            }
	        })
	    }