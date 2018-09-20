var myDataTable;
$(function() {

		if(isEmpty($("#isFdy").val())){
			$("#mbxy").hide();
		}
	
	 _init_select("isChecked",[
	                	  	 {
	                	  		"dictValue":"1","dictName":"是"
	                	  	 		},
	                	  	 {
	                	  		"dictValue":"0","dictName":"否"
	                	  	 		}
	                	  	 ]);
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

    _init_select("stdType", dictJson.stdType);
	
	//初始化年级下拉框
	 _init_select("grade",dictJson.grade);
	 _init_select("pfsnLevel",dictJson.pfsnLevel);
	 

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

	myDataTable = $('.table-sort').dataTable(
			{
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : "/pictureCollect/whiteList.do",
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
				},
				{
					"mData" : "idCard"
				},{
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
								return '<input type="checkbox" value="'+ row.learnId+';'+row.stdId + '" name="learnIds"/>';
							   },
							"targets" : 0
							},
							{"render" : function(data, type, row, meta) {
								  if(row.picCollectId){
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
								   if(row.tutorName){
									   dom = row.tutorName;
			                        	if(row.tutorStatus ==='2'){
			                        		dom += ' <sup style="color:#f00">离</sup>';
			                        	} 
								    }
		                        	return dom;
								},
								"targets" : 4
							},
							{"render" : function(data, type, row, meta) {
								return row.grade+'级';
								},
								"targets" : 5
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
								"targets" : 6,"class":"text-l"
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
	    	grade : $("#grade").val() ? $("#grade").val() : '',
	    	pfsnId : $("#pfsnId").val() ? $("#pfsnId").val() : '',
			stdName : $("#stdName").val() ? $("#stdName").val() : '',
			idCard : $("#idCard").val() ? $("#idCard").val() : '',
			stdType : $("#stdType").val() ? $("#stdType").val() : '',
			isChecked : $("#isChecked").val() ? $("#isChecked").val() : '',
			tutorName : $("#tutorName").val() ? $("#tutorName").val() : '',
			pfsnLevel : $("#pfsnLevel").val() ? $("#pfsnLevel").val() : '',
			stdStage : stdStageArray ? stdStageArray.join(',') : '',
			start : pageData.start,
			length : pageData.length
		};
	}
	function addAllStu() {
	    var stdStageArray = new Array();
		
	    $.each($("input[name='stdStage']:checked"),function(){
	        stdStageArray.push($(this).val());
	    });
	    
        var url = '/pictureCollect/addAllWhiteStu.do';
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
					},"grade" : function() {
						return $("#grade").val();
					},"pfsnId" : function() {
						return $("#pfsnId").val();
					},"stdName" : function() {
						return $("#stdName").val();
					},"idCard" : function() {
						return $("#idCard").val();
					},"stdType" : function() {
						return $("#stdType").val();
					},"isChecked" : function(){
						return $("#isChecked").val();
					},"tutorName" : function(){
						return $("#tutorName").val();
					},"pfsnLevel" : function (){
						return $("#pfsnLevel").val();
					},"stdStage" : function (){
						return stdStageArray
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
	
	function delAllStu() {
	    var stdStageArray = new Array();
		
	    $.each($("input[name='stdStage']:checked"),function(){
	        stdStageArray.push($(this).val());
	    });
		    
        var url = '/pictureCollect/delAllWhiteStu.do';
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
                    },"grade" : function() {
                        return $("#grade").val();
                    },"pfsnId" : function() {
                        return $("#pfsnId").val();
                    },"stdName" : function() {
                        return $("#stdName").val();
                    },"idCard" : function() {
                        return $("#idCard").val();
                    },"stdType" : function() {
                        return $("#stdType").val();
                    },"isChecked" : function(){
                        return $("#isChecked").val();
                    },"tutorName" : function(){
                        return $("#tutorName").val();
                    },"pfsnLevel" : function (){
                        return $("#pfsnLevel").val();
                    },"stdStage" : function (){
                        return stdStageArray
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
				url : '/pictureCollect/addWhiteStu.do',
				data : {
					idArray : chk_value
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
				url : '/pictureCollect/delWhiteStu.do',
				data : {
					idArray : chk_value
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
	function whiteStuImport() {
		var url = '/pictureCollect/pictureCollectWhiteImport.do';
		layer_show('白名单导入', url, null, null, function() {
			myDataTable.fnDraw(true);
		});
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
	 
	 function isEmpty(StringVal){
			
			if(StringVal=="" || StringVal==undefined || StringVal==null){
				return false;
			}else{
				return true;
			}
			
		}