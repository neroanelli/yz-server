var myDataTable;
		$(function() {
			$('select').select2({
				placeholder : "--请选择--",
				allowClear : true,
				width : "59%"
			});
			//初始状态
			_init_select("isPay",[
				{"dictValue":"1","dictName":"是"},
				{"dictValue":"0","dictName":"否"}
			]);
			_init_select("infoStatus",[
				{"dictValue":"1","dictName":"有误"},
				{"dictValue":"0","dictName":"无误"}
			]);
			_init_select("isUploadPicture",[
				{"dictValue":"1","dictName":"是"},
				{"dictValue":"0","dictName":"否"}
			]);
            _init_select("isRemark",[
                {"dictValue":"0","dictName":"否"},
                {"dictValue":"1","dictName":"是"}
            ]);
            _init_select("checkStatus",[
                {"dictValue":"0","dictName":"待审核"},
                {"dictValue":"1","dictName":"审核通过"},
                {"dictValue":"2","dictName":"审核不通过"}
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
					url : '/pictureCollect/findPictureCollectList.do',
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
					{"mData" : "schoolRoll"},
					{"mData" : "idCard"},
					{"mData" : "grade"},
					{"mData" : null},
					{"mData" : "tutor"},
					{"mData" : null},
					{"mData" : "confirmErrorMessage"},
                    {"mData" : "pictureNo"},
					{"mData" : null},
					{"mData" : null},
					{"mData" : null},
					{"mData" : "remark"},
					{"mData" : null}
				],
				"columnDefs" : [
						{"render" : function(data, type, row, meta) {
                            if(row.checkStatus=='0') {
                                return '<input type="checkbox" value="' + row.picCollectId + '" name="ids"/>';
                            }else{
                            	return '';
							}
						},
							"targets" : 0
						},
						{"targets" : 1,"render" : function(data, type, row, meta) {
							var dom = '<a title="查看学员信息" onclick="toEidt(\''+ row.learnId +'\',\''+ row.stdId +'\',\''+ row.recruitType +'\')"><span class="c-blue">'+ row.stdName +'</span></a>'
							return dom;
						}},
						{"targets" : 5, "class" : "text-l", "render" : function(data, type, row, meta) {
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
						{"targets" : 7,"render" : function(data, type, row, meta) {
							if(row.infoStatus && row.infoStatus =='0'){
								return "<label class='label label-success radius'>无误</label>";
							}else{
								return "<label class='label  label-danger radius'>有误</label>";
							}
						}},
						{"targets" : 10,"render" : function(data, type, row, meta) {
							if(!row.pictureFileName){return ''}
							var dom='';
							dom += '<a href='+ _FILE_URL+ row.pictureUrl+' style="color:#069" target="_blank">' + row.pictureFileName+ "</a><br/>";
                            return dom;
						}},
						{"targets" : 11,"render" : function(data, type, row, meta) {
                            if(row.isPay && row.isPay=='1'){
                                return "<label class='label label-success radius'>是</label>";
                            }else{
                                return "<label class='label label-danger radius'>否</label>";
                            }
						}},
						{"targets" : 12,"render" : function(data, type, row, meta) {
							if(row.checkStatus && row.checkStatus=='0'){
                                return "<label class='label'>待审核</label>";
							}else if(row.checkStatus && row.checkStatus=='1'){
                                return "<label class='label label-success radius'>审核通过</label>";
							}
							else{
								return "<label class='label label-danger radius'>审核不通过</label>";
							}
						}},
						{"targets" : 14,"render" : function(data, type, row, meta) {
							var dom = ''
							if(row.checkStatus=='0') {
                                dom += '<a class="" href="javascript:;" title="审核" onclick="picCollectCheck(\'' + row.picCollectId + '\')"><i class="iconfont icon-shenhe"></i></a>';
                            }else{
                                dom += '<a class="" href="javascript:;" title="撤销审核" onclick="picCollectRevoke(\'' + row.picCollectId + '\')"><i class="iconfont icon-tuichu"></i></a>';
                            }
                            dom += '<a title="添加备注" href="javascript:;" onclick="addRemark(\'' + row.picCollectId + '\')" class="ml-5" style="text-decoration: none">';
                            dom += '<i class="iconfont icon-edit"></i></a>';
							return dom;
						}}
				]
			});
		});

		/*图像采集白名单*/
		function whiteList() {
			var url = '/pictureCollect/toWhiteList.do' ;
			layer_show('图像采集白名单设置', url, 800, 700, function() {
                myDataTable.fnDraw(true);
			},true);
		}

		/*审核*/
		function picCollectCheck(id) {
			var url = '/pictureCollect/toEdit.do' + '?picCollectIds='+ id ;
			layer_show('审核', url, 500, 300, function() {
			});
		}
		
		
		/*批量审核*/
		function batchPicCollectCheck(id) {
            var chk_value = [];
            $("input[name=ids]:checked").each(function () {
                chk_value.push($(this).val());
            });
            if (chk_value.length == 0) {
                layer.msg('请选择要审核的数据！', {
                    icon: 2,
                    time: 2000
                });
                return;
            }
			var url = '/pictureCollect/toEdit.do?picCollectIds='+encodeURIComponent(chk_value.join(','));
			layer_show('审核', url, 500, 300, function() {
			});
		}
		/*撤销审核*/
		function picCollectRevoke(id) {
			var url = '/pictureCollect/revoke.do' ;	
			layer.confirm('已将审核结果推送给学员，请确认是否需要撤销重新审核？', function(index) {
	             $.ajax({
	                 type : 'POST',
	                 url : url,
	                 data: {
	                	 picCollectIds:id,
	                	 checkStatus:"0"
	                 },
	                 dataType : 'json',
	                 success : function(data) {
	                     if (data.code == _GLOBAL_SUCCESS) {
	                         layer.msg('撤销审核成功!', {
	                             icon : 1,
	                             time : 3000
	                         });
	                         myDataTable.fnDraw(false);
	                     }
	                 }
	             });
	         });
		}
		
		/*批量撤销审核*/
		function batchPicCollectRevoke() {
            var chk_value = [];
            $("input[name=ids]:checked").each(function () {
                chk_value.push($(this).val());
            });
            if (chk_value.length == 0) {
                layer.msg('请选择要审核的数据！', {
                    icon: 2,
                    time: 2000
                });
                return;
            }
			var url = '/pictureCollect/revoke.do';
			layer.confirm('已将审核结果推送给学员，请确认是否需要撤销重新审核？', function(index) {
	             $.ajax({
	                 type : 'POST',
	                 url : url,
	                 data : {
	                	 picCollectIds : encodeURIComponent(chk_value.join(',')),
	                	 checkStatus:"0"
					 },
	                 dataType : 'json',
	                 success : function(data) {
	                     if (data.code == _GLOBAL_SUCCESS) {
	                         layer.msg('撤销审核成功!', {
	                             icon : 1,
	                             time : 3000
	                         });
	                         myDataTable.fnDraw(false);
	                     }
	                 }
	             });
	         });
		}
		
		

		/*搜素*/
		function searchResult(){
			myDataTable.fnDraw(true);
		}

		/*用户-编辑*/
		function toEidt(learnId, stdId,recruitType) {
			var url = '/studentBase/toEdit.do' + '?learnId='
				+ learnId + '&stdId=' + stdId+"&recruitType="+recruitType;
			layer_show('学员信息', url, null, null, function() {
				myDataTable.fnDraw(false);
			}, true);
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
		
	    function pictureCollectImport() {
	        var url = '/pictureCollect/pictureCollectImport.do';
	        layer_show('导入照片编号', url, null, 510, function () {
	            myDataTable.fnDraw(true);
	        });
	    }
	    
	    function pictureCollectExport(){
			$("#export-form").attr("action","/pictureCollect/exportPictureCollect.do")
        	$("#export-form").submit();
        }
        
        function rarExport() {
            $("#export-form").attr("action","/pictureCollect/exportRar.do")
            $("#export-form").submit();
        }

/*添加备注*/
function addRemark(picCollectId) {
    $("#addRemark").val('');
    var url = '/pictureCollect/updateRemark.do';
    layer.open({
        type: 1,
        btn: ['确定'],
        yes: function (index, layero) {
            var addRemark = $("#addRemark").val();
            if (addRemark.length > 100) {
                layer.msg('最大支持100个字符!', {
                    icon: 2,
                    time: 2000
                });
                return;
            }

            $.ajax({
                type: 'POST',
                url: url,
                data: {
                    picCollectId: picCollectId,
                    addRemark: addRemark
                },
                dataType: 'json',
                success: function (data) {
                    if (data.code == _GLOBAL_SUCCESS) {
                        layer.msg('添加成功!', {
                            icon: 1,
                            time: 1000
                        });
                        myDataTable.fnDraw(false);
                    }
                }
            });
            layer.close(index)
        },
        area: ['500px', '300px'],
        content: $("#addRemarkContent")
    });
}