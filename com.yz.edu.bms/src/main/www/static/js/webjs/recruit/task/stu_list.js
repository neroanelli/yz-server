var myDataTable;
$(function() {
	
	 _init_select("isChecked",[
    	  	 {
    	  		"dictValue":"1","dictName":"是"
    	  	 		},
    	  	 {
    	  		"dictValue":"0","dictName":"否"
    	  	 		}
    	  	 ]);
	 _init_select("ifSendTask",[
  	  	 {
  	  		"dictValue":"1","dictName":"已发送"
  	  	 		},
  	  	 {
  	  		"dictValue":"0","dictName":"未发送"
  	  	 		}
  	  	 ]);
	 
	 _init_select("webRegisterStatus",[
           {"dictValue":"0","dictName":"待网报"},
           {"dictValue":"1","dictName":"网报成功"}
	  ]);
	 
	    _init_select("isDataCompleted",[
         	  	 {
         	  		"dictValue":"0","dictName":"未完善"
         	  	 		},
         	  	 {
         	  		"dictValue":"1","dictName":"已完善"
         	  	 		}
         	  	 ]);
	    _init_select('myAnnexStatus', dictJson.myAnnexStatus);
	 if($("#taskType").val() =='17'){
		 var dom ='';
		 dom += '<span> 报考信息确认:</span>';
		 dom += '<select class="select" size="1" id="ifAffirmInfo" name="ifAffirmInfo">';
		 dom += '<option value="" selected>--请选择--</option>';
		 dom += '</select>';
	    $("#ifAffirmInfoDiv").append(dom);
	    _init_select("ifAffirmInfo",[
 	  	 {
 	  		"dictValue":"1","dictName":"已确认"
 	  	 		},
 	  	 {
 	  		"dictValue":"0","dictName":"未确认"
 	  	 		}
 	  	 ]);
	 }
     //考试县区
     _simple_ajax_select({
         selectId: "taId",
         searchUrl: '/testArea/findAllKeyValue.do',
         sData: {},
         showText: function (item) {
             return item.taName;
         },
         showId: function (item) {
             return item.taId;
         },
         placeholder: '--请选择--'
     });
	//初始化院校名称下拉框
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
	
	//初始化年级下拉框
	 _init_select("grade",dictJson.grade);
	 _init_select("pfsnLevel",dictJson.pfsnLevel);
	 
	//初始化院校类型下拉框
	_init_select("recruitType",[
	                   	  	 {
	                 	  		"dictValue":"1","dictName":"成人教育"
	                 	  	 }
	                 	  	 ]);
	
	 $("#recruitType").change(function() {
			_init_select({
				selectId : 'grade',
				ext1 : $(this).val()
			}, dictJson.grade);
	 });
  	 
  	 _init_select('sg', dictJson.sg); //优惠分组
  	 _init_select('scholarship', dictJson.scholarship); //优惠类型
  	 _init_select('inclusionStatus',dictJson.inclusionStatus); //入围状态
  	 
  	 $("#sg").change(function() { //联动
		_init_select({
			selectId : 'scholarship',
			ext1 : $(this).val()
		}, dictJson.scholarship);
	 });
  	 
	 if(exType_global =='LOOK'){//如果是修改操作
		  $("#isChecked").attr("disabled", "disabled");
		  _init_select("isChecked",[
	  	 {
	  		"dictValue":"1","dictName":"是"
	  	 		},
	  	 {
	  		"dictValue":"0","dictName":"否"
	  	 		}
	  	 ],1);
     }
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
            placeholder: "--请先选择--"
     });

	myDataTable = $('.table-sort').dataTable(
			{
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : "/studentTask/stuList.do",
					data: function (pageData) {
                         return searchData(pageData);
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

				columns : [ {
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
				}],
				"columnDefs" : [
				            {"render" : function(data, type, row, meta) {
								return '<input type="checkbox" value="'+ row.learnId+';'+row.tutor +';'+row.openId+'" name="learnIds"/>';
							   },
							"targets" : 0
							},
							{"render" : function(data, type, row, meta) {
								  if(row.taskId == $("#taskId").val()){
									  return "是";
								  }
								  return "不是";
							   },
							   "targets" : 1
							},
							{"render" : function(data, type, row, meta) {
								   var dom ='';
		                        	dom = row.stdName;
		                        	if(row.stdType ==='2'){
		                        		dom += ' <sup style="color:#f00">外</sup>';
		                        	}
		                        	return dom;
								},
								"targets" : 2
							},
							{"render" : function(data, type, row, meta) {
								   var dom ='';
								   if(row.empName){
									   dom = row.empName;
			                        	if(row.empStatus ==='2'){
			                        		dom += ' <sup style="color:#f00">离</sup>';
			                        	} 
								    }
		                        	return dom;
								},
								"targets" : 3
							},
							{"render" : function(data, type, row, meta) {
								return row.grade+'级';
								},
								"targets" : 4
							},
							{
								"render" : function(data, type, row, meta) {
									var dom = '';
									if(!isEmpty(_findDict("recruitType",row.recruitType))){
										return '无';
									}
									dom += '['+ _findDict("recruitType",row.recruitType)+ ']';
									dom += row.unvsName;
									dom += ':(' + row.pfsnCode+ ')';
									dom += row.pfsnName;
									dom += '['+ _findDict("pfsnLevel",row.pfsnLevel)+ ']';
									dom +='('+row.grade+'级)';

									return dom;
								},
								"targets" : 5,"class":"text-l"
							},
							{"render" : function(data, type, row, meta) {
								if(row.webRegisterStatus && row.webRegisterStatus=='1'){
	                                return "<label class='label label-success radius'>网报成功</label>";
	                            }else{
	                                return "<label class='label label-danger radius'>待网报</label>";
	                            }
							},
								"targets" : 6
							},
							{"render" : function(data, type, row, meta) {
								return _findDict("stdStage",row.stdStage);
								},
								"targets" : 7
							}
				  ]
			});

	});
	function searchData(pageData) {
	    var stdStageArray = new Array();
	
	    $.each($("input[name='stdStage']:checked"),function(){
	        stdStageArray.push($(this).val());
	    });
	    if(stdStageArray == '' || stdStageArray == null){
            layer.msg('请选择学员状态！', {
                icon : 1,
                time : 3000
            });
            return false;
        }
	    return {
	    	unvsId : $("#unvsId").val() ? $("#unvsId").val() : '',
	    	recruitType : $("#recruitType").val() ? $("#recruitType").val() : '',
	    	grade : $("#grade").val() ? $("#grade").val() : '',
	    	pfsnId : $("#pfsnId").val() ? $("#pfsnId").val() : '',
			stdName : $("#stdName").val() ? $("#stdName").val() : '',
			idCard : $("#idCard").val() ? $("#idCard").val() : '',
			mobile : $("#mobile").val() ? $("#mobile").val() : '',
			isChecked : $("#isChecked").val() ? $("#isChecked").val() : '',
			taskId : $("#taskId").val() ? $("#taskId").val() : '',
			scholarship : $("#scholarship").val() ? $("#scholarship").val() : '',
			sg : $("#sg").val() ? $("#sg").val() : '',
			inclusionStatus : $("#inclusionStatus").val() ? $("#inclusionStatus").val() : '',
			pfsnLevel : $("#pfsnLevel").val() ? $("#pfsnLevel").val() : '',
			stdStage : stdStageArray ? stdStageArray.join(',') : '',
			ifAffirmInfo : $("#ifAffirmInfo").val() ? $("#ifAffirmInfo").val() : '',
			taskType : $("#taskType").val() ? $("#taskType").val () : '',
			isDataCompleted : $("#isDataCompleted").val() ? $("#isDataCompleted").val() : '',
			myAnnexStatus : $("#myAnnexStatus").val() ? $("#myAnnexStatus").val() : '',	
			taId : $("#taId").val() ? $("#taId").val() : '',	
			webRegisterStatus : $("#webRegisterStatus").val() ? $("#webRegisterStatus").val() : '',
			start : pageData.start,
			length : pageData.length
		};
	}
	//根据条件添加
	function addAllStu() {
	    var stdStageArray = new Array();
		
	    $.each($("input[name='stdStage']:checked"),function(){
	        stdStageArray.push($(this).val());
	    });
	    
        var url = '/studentTask/addAllStu.do';
        var grade = $("#grade").val();
        if(grade ==''){
        	 layer.msg('至少要选择一个年级!!!', {
                 icon : 5,
                 time : 3000
             });
             return;
        }
        console.log($("#ifAffirmInfo").val());
        if(stdStageArray == '' || stdStageArray == null){
            layer.msg('请选择学员状态！', {
                icon : 1,
                time : 2000
            });
            return false;
        }
        layer.confirm('确认根据筛选条件全部添加吗？', function(index) {
            $.ajax({
                type : 'POST',
                url : url,
                data:{
					"unvsId" : function() {
						return $("#unvsId").val();
					},"recruitType" : function() {
						return $("#recruitType").val();
					},"grade" : function() {
						return $("#grade").val();
					},"pfsnId" : function() {
						return $("#pfsnId").val();
					},"stdName" : function() {
						return $("#stdName").val();
					},"idCard" : function() {
						return $("#idCard").val();
					},"mobile" : function() {
						return $("#mobile").val();
					},"isChecked" : function(){
						return $("#isChecked").val();
					},"taskId" : function(){
						return $("#taskId").val();
					},"scholarship" : function(){
						return $("#scholarship").val();
					},"sg" : function(){
						return $("#sg").val();
					},"inclusionStatus" : function(){
						return $("#inclusionStatus").val();
					},"ifAffirmInfo" : function(){
						return typeof($("#ifAffirmInfo").val()) == "undefined"?'':$("#ifAffirmInfo").val();
					},"pfsnLevel" : function (){
						return $("#pfsnLevel").val();
					},"stdStage" : function (){
						return stdStageArray
					},"taskType" : function(){
						return $("#taskType").val();
					},"taId" : function (){
						return $("#taId").val();
					},"webRegisterStatus" : function (){
						return $("#webRegisterStatus").val();
					}
				},
                dataType : 'json',
                success : function(data) {
                    if (data.code == _GLOBAL_SUCCESS) {
                        layer.msg('添加成功!', {
                            icon : 1,
                            time : 2000
                        });
                    	myDataTable.fnDraw(true);
                    }
                }
            });
        });
    }
	//根据条件删除
	function delAllStu() {
	    var stdStageArray = new Array();
		
	    $.each($("input[name='stdStage']:checked"),function(){
	        stdStageArray.push($(this).val());
	    });
		    
        var url = '/studentTask/delAllStu.do';
        var grade = $("#grade").val();
        if(grade ==''){
        	 layer.msg('至少要选择一个年级!!!', {
                 icon : 5,
                 time : 3000
             });
             return;
        }
        if(stdStageArray == '' || stdStageArray == null){
            layer.msg('请选择学员状态！', {
                icon : 2,
                time : 2000
            });
            return false;
        }
        layer.confirm('确认根据筛选条件清除全部吗？', function(index) {
            $.ajax({
                type : 'POST',
                url : url,
                data:{
                	"unvsId" : function() {
						return $("#unvsId").val();
					},"recruitType" : function() {
						return $("#recruitType").val();
					},"grade" : function() {
						return $("#grade").val();
					},"pfsnId" : function() {
						return $("#pfsnId").val();
					},"stdName" : function() {
						return $("#stdName").val();
					},"idCard" : function() {
						return $("#idCard").val();
					},"mobile" : function() {
						return $("#mobile").val();
					},"isChecked" : function(){
						return $("#isChecked").val();
					},"taskId" : function(){
						return $("#taskId").val();
					},"scholarship" : function(){
						return $("#scholarship").val();
					},"sg" : function(){
						return $("#sg").val();
					},"inclusionStatus" : function(){
						return $("#inclusionStatus").val();
					},"ifAffirmInfo" : function(){
						return typeof($("#ifAffirmInfo").val()) == "undefined"?'':$("#ifAffirmInfo").val();
					},"pfsnLevel" : function (){
						return $("#pfsnLevel").val();
					},"stdStage" : function (){
						return stdStageArray
					},"taskType" : function(){
						return $("#taskType").val();
					},"taId" : function (){
						return $("#taId").val();
					},"webRegisterStatus" : function (){
						return $("#webRegisterStatus").val();
					}
				},
                dataType : 'json',
                success : function(data) {
                    if (data.code == _GLOBAL_SUCCESS) {
                        layer.msg('清除成功!', {
                            icon : 1,
                            time : 3000
                        });
                    	myDataTable.fnDraw(true);
                    }
                }
            });
        });
    }
	//搜索学员信息
	function searchStudent() {
	
	    var stdStageArray = new Array();

		$.each($("input[name='stdStage']:checked"), function() {
			stdStageArray.push($(this).val());
		});
		if (stdStageArray == '' || stdStageArray == null) {
			layer.msg('请选择学员状态！', {
				icon : 2,
				time : 2000
			});
			return false;
		}
		myDataTable.fnDraw(true);
	}

	/*管理员-目标学员-添加*/
	function addStu() {
		var chk_value = [];
		$("input[name=learnIds]:checked").each(function() {
			chk_value.push($(this).val());
		});
		if (chk_value.length == 0) {
			layer.msg('请选择要添加的数据！', {
				icon : 2,
				time : 2000
			});
			return;
		}
		layer.confirm('确认要添加目标学员吗？', function(index) {
			$.ajax({
				type : 'POST',
				url : '/studentTask/addStu.do',
				data : {
					idArray : chk_value,
					taskId : $("#taskId").val(),
					taskType : $("#taskType").val()
				},
				dataType : 'json',
				success : function(data) {
					layer.msg('添加成功!', {
						icon : 1,
						time : 2000
					});
					myDataTable.fnDraw(true);
					$("input[name=all]").attr("checked", false);
				},
				error : function(data) {
					layer.msg('添加失败！', {
						icon : 1,
						time : 2000
					});
					myDataTable.fnDraw(true);
					$("input[name=all]").attr("checked", false);
				},
			});
		});
	}

	function delStu() {
		var chk_value = [];
		$("input[name=learnIds]:checked").each(function() {
			chk_value.push($(this).val().split(";")[0]);
		});
		if (chk_value.length == 0) {
			layer.msg('请选择要清除的数据！', {
				icon : 2,
				time : 2000
			});
			return;
		}
		layer.confirm('确认要清除目标学员吗？', function(index) {
			$.ajax({
				type : 'POST',
				url : '/studentTask/delStu.do',
				data : {
					idArray : chk_value,
					taskId : $("#taskId").val()
				},
				dataType : 'json',
				success : function(data) {
					layer.msg('清除成功!', {
						icon : 1,
						time : 2000
					});
					myDataTable.fnDraw(true);
					$("input[name=all]").attr("checked", false);
				},
				error : function(data) {
					layer.msg('清除失败！', {
						icon : 1,
						time : 2000
					});
					myDataTable.fnDraw(true);
					$("input[name=all]").attr("checked", false);
				},
			});
		});
	}
	//初始化专业
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
	function isEmpty(StringVal){
			
			if(StringVal=="" || StringVal==undefined || StringVal==null){
				return false;
			}else{
				return true;
			}
			
	}