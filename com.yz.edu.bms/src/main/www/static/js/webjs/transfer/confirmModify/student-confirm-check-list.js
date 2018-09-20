var myDataTable;
		$(function() {
			 $('select').select2({
	               placeholder : "--请选择--",
	               allowClear : true,
	               width : "59%"
	           });
		 //申请进度saStatus
		 _init_select("checkStatus",[{
			          						"dictValue":"1","dictName":"已提交"
			          				},{
			          		  	 			"dictValue":"4","dictName":"已驳回"
			          		  	 	}]);
		 //学员阶段
		   _init_select("stdStage",[{
						"dictValue":"1","dictName":"意向学员"
				},{
		  	 			"dictValue":"2","dictName":"考前辅导"
		  	 	}, {
		  	 			"dictValue":"3","dictName":"考前确认"
		  	 	}]);
		   
		   
		  _init_select("webRegisterStatus",[{
					"dictValue":"0","dictName":"待网报"
			},{
	  	 			"dictValue":"1","dictName":"网报成功"
	  	 	}]);
		 
		   //校区-部门-组 联动
           _init_campus_select("campusId", "dpId", "groupId", '/campus/selectAllList.do', '/dep/selectAllList.do', '/group/selectAllList.do');
		  
           //初始化考试区县
           $.ajax({
   			type: "POST",
   			dataType : "json", //数据类型
   			url: '/sceneManagement/getExamDicName.do',
   			success: function(data){
   				examDicJson = data.body;
   				if(data.code=='00'){
   					_init_select("taId",examDicJson);
   				}
   			}
   	     	});
			
           //优惠类型
           _init_select('scholarship', dictJson.scholarship);
           //报考层次
           _init_select("pfsnLevel",dictJson.pfsnLevel);
           //考试费
           _init_select("examPayStatus",[
                                         {"dictValue":"0","dictName":"未缴费"},
                                         {"dictValue":"1","dictName":"已缴费"}
            ]);
			myDataTable = $('.table-sort').dataTable(
					{
                        "processing": true,
						"serverSide" : true,
						"dom" : 'rtilp',
						"ajax" : {
							url : "/confirmModifyCheck/findStudentModifyCheck.do",
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
						columns : [ {
							"mData" : null
						}, {
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
							"mData" : "empName"
						} , {
							"mData" : null
						}, {
							"mData" : null
						}, {
							"mData" : null
						}],
						"columnDefs" : [
								{
									"render" : function(data, type, row, meta) {
										 if(row.checkStatus == "4"){
									    	 return '';
										}else{
											 return '<input type="checkbox" value="'+ row.modifyId+ '" name="modifyIds"  onclick="dealCheck('+row.checkStatus+',this)"/>';
										}
									},
									"targets" : 0
								},
								{
									"render" : function(data, type, row, meta) {
										return _findDict("grade",row.grade);
									},
									"targets" : 2
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
									"targets" : 3,
                                    "class":"text-l"
								},
								{
									"render" : function(data, type, row, meta) {
										return _findDict("stdStage",row.stdStage);
									},
									"targets" : 4
								},
								{
									"render" : function(data, type, row, meta) {
										return _findDict("scholarship",row.scholarship);
									},
									"targets" : 5
								},
								{
									"render" : function(data, type, row, meta) {


										return row.ext_1;
									},
									"targets" : 6,
                                    "class":"text-l"
								},
								{
									"render" : function(data, type, row, meta) {
										 if(row.webRegisterStatus && row.webRegisterStatus=='1'){
				                                return "<label class='label label-success radius'>网报成功</label>";
				                            }else{
				                                return "<label class='label label-danger radius'>待网报</label>";
				                            }
									},
									"targets" : 7
								},
								{
									"render" : function(data, type, row, meta) {
										if(row.examPayStatus && row.examPayStatus=='1'){
											return "<label class='label label-success radius'>已缴费</label>";
										}else{
											return "<label class='label label-danger radius'>未缴费</label>";
										}
									},
									"targets" : 8
								},
								{
									"render" : function(data, type, row, meta) {
										return row.createUser;
									},
									"targets" : 9
								},
								{
									"render" : function(data, type, row, meta) {
                                        var dateTime=new Date(row.createTime).format('yyyy-MM-dd hh:mm:ss');
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
										if(row.checkStatus == "1"){
											dom ='<label class="label-secondary label radius">已提交</label>';
										}else if(row.checkStatus == "2"){
											dom ='<label class="label label-success radius">已初审</label>';
										}else if(row.checkStatus == "3"){
											dom ='<label class="label label-danger radius">已修改</label>';
										}else if(row.checkStatus == "4"){
											dom ='<label class="label label-danger radius">已驳回</label>';
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
										}else{
											dom  = row.remark;
										}
										return dom;
									},
									"targets" : 13
								},{
									"render" : function(data, type, row, meta) {
										var dom = '';
									    if(row.checkStatus == "4"){
									    	 dom = '<a title="查看" href="javascript:;" onclick="member_show(\'' + row.modifyId + '\',4)" class="ml-5" style="text-decoration: none">';
									    	 dom += '<i class="iconfont icon-chakan"></i></a>';
										}else{
											 dom = '<a title="进入审核" href="javascript:;" onclick="member_show(\'' + row.modifyId + '\',3)" class="ml-5" style="text-decoration: none">';
											 dom += '<i class="iconfont icon-shenhe"></i></a>';
											 dom += '&nbsp;&nbsp;&nbsp;<a title="审核通过" href="javascript:void(0)" onclick="member_check(\'' + row.modifyId + '\',3)"><img id="u1640_img" class="img " src="../images/checkPass.jpg"/></a>'
											 dom += '&nbsp;&nbsp;&nbsp;<a title="驳回" href="javascript:void(0)" onclick="member_check(\'' + row.modifyId + '\',4)"><img id="u1641_img" class="img " src="../images/bh.jpg"/></a>'
					                        /* dom += '<a class="" href="javascript:;" title="审核通过" onclick="member_check(\'' + row.modifyId + '\',3)"><i class="iconfont icon-edit"></i></a>&nbsp;';
					                         dom += '<a class="" href="javascript:;" title="驳回" onclick="member_check(\'' + row.modifyId + '\',4)"><i class="iconfont  icon-shanchu"></i></a>&nbsp;';*/
										}
			                            return dom;
									},
									"targets" : 14
								}]
			

						});
		});
		
		/*用户-查看*/
		function member_show(modifyId,checkStatus) {
			var url = "";
			if(checkStatus==4){
				url = '/confirmModifyCheck/editToCheck.do' + '?modifyId=' + modifyId + '&exType=VIEW';
			}else{
				url = '/confirmModifyCheck/editToCheck.do' + '?modifyId=' + modifyId + '&exType=SHOW';
			}
		
			layer_show('查看详情', url, null, 510, function() {
                myDataTable.fnDraw(false);
			},true);
		}
		
		function batchOper() {
			var chk_value = [];
			$("input[name=modifyIds]:checked").each(function() {
				chk_value.push($(this).val());
			});
			
			if (chk_value == null || chk_value.length <= 0) {
                layer.msg('未选择任何数据!', {
                    icon: 5,
                    time: 1000
                });
                return;
            }
			
			layer.confirm('确认批量审核吗？', function(index) {
				$.ajax({
					type : 'POST',
					url : '/confirmModifyCheck/checkModifyBatch.do',
					data : {
						modifyIds : chk_value
					},
					dataType : 'json',
					success : function(data) {
						if(data.code == _GLOBAL_SUCCESS){
							
							layer.msg('批量审核成功!', {
								icon : 1,
								time : 1000
							});
							myDataTable.fnDraw(true);
							$("input[name=all]").attr("checked", false);
						}
					}
				});
			});
		}
		
		/*function dealCheck(checkStatus,e){
			if("1"!=checkStatus){
				$(e).attr("checked", false);
				layer.msg('此条目已提交审核不能删除！', {
					icon : 1,
					time : 1000
				});
			}
		}*/
		function member_check(modifyId,checkStatus){
			if(checkStatus==4){
			
					layer.prompt({
	                    title : '填写驳回原因：',
	                    formType : 2,
	                    maxlength : 50
	                }, function(text, index) {
	                    $.ajax({
	                        type : "post", //提交方式
	                        dataType : "json", //数据类型
	                        data : {
								modifyId : modifyId,
								checkStatus : checkStatus,
								reason : text
							},
	                        url : '/confirmModifyCheck/passStudentModifyCheck.do', //请求url
	                        success : function(data) { //提交成功的回调函数
	                            if(data.code == _GLOBAL_SUCCESS){
	                                layer.msg('操作成功！', {icon : 1, time : 1000},function(){
	                                    layer_close();
	                                    myDataTable.fnDraw(true);
	                                });
	                            }
	                        }
	                    });
	                });
			}else{
				layer.confirm('确认通过审核吗？', function(index) {
					$.ajax({
						type : 'POST',
						url : '/confirmModifyCheck/passStudentModifyCheck.do',
						data : {
							modifyId : modifyId,
							checkStatus : checkStatus
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
				});
			}
		}
		function _search() {
			myDataTable.fnDraw(true);
		}