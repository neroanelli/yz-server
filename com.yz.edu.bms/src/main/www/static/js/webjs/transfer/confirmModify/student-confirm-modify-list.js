var myDataTable;
		$(function() {
			$('select').select2({
	               placeholder : "--请选择--",
	               allowClear : true,
	               width : "59%"
	           });
			
			_init_select("webRegisterStatus",[
                  {"dictValue":"0","dictName":"待网报"},
                  {"dictValue":"1","dictName":"网报成功"}
              ]);
			
		  //申请进度saStatus
		  _init_select("checkStatus",[
		           				{
		           						"dictValue":"1","dictName":"已提交"
		           					},
		           		  	 {
		           		  	 			"dictValue":"2","dictName":"已初审"
		           		  	 		},
		           		  	 	{
		           		  	 			"dictValue":"3","dictName":"已修改"
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
							url : "/confirmModify/findConfirmModify.do",
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
							"mData" : null
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
										     return '<input type="checkbox" value="'+ row.modifyId+ '" name="modifyIds"  onclick="dealCheck('+row.checkStatus+',this)"/>';
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
										var dom =row.ext1;
										return dom;
									},
									"targets" : 6
								},
								{
									"render" : function(data, type, row, meta) {
										var dom ="";
										if(row.webRegisterStatus == "1"){
											dom ='<label class="label label-success radius">网报成功</label>';
										}else if(row.webRegisterStatus == "0"){
											dom ='<label class="label label-danger radius">待网报</label>';
										}
										return dom;
									},
									"targets" : 7
								},
								{
									"render" : function(data, type, row, meta) {
										var dom ="";
										if(row.examPayStatus == "1"){
											dom ='<label class="label label-success radius">已缴费</label>';
										}else if(row.examPayStatus == "0"){
											dom ='<label class="label label-danger radius">未缴费</label>';
										}
										return dom;
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
										var dom = row.recruitName;
						                if(row.empStatus ==='2'){
						                    dom += '<span class="name-mark-out">离</span>';
						                }
						                return dom;
									},
									"targets" : 11
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
											dom = row.remark;
										}
										return dom;
									},
									"targets" : 13
								},{
									"render" : function(data, type, row, meta) {
										var dom = '';
											dom = '<a title="查看" href="javascript:;" onclick="member_show(\'' + row.modifyId + '\')" class="ml-5" style="text-decoration: none">';
  										    dom += '<i class="iconfont icon-chakan"></i></a>';
										return dom;
									},
									//指定是第三列
									"targets" : 14
								} ]
					});

		});

		/*用户-添加*/
		function member_add() {
			var url = '/confirmModify/add.do' + '?exType=ADD';
			layer_show('添加成考网报后异动学员', url, null, 585, function() {
				// myDataTable.fnDraw(true);
			},true);
		}
		
		/*用户-查看*/
		function member_show(modifyId) {
			var url = '/confirmModify/view.do' + '?modifyId=' + modifyId + '&exType=SHOW';
			layer_show('查看详情', url, null, 510, function() {
                myDataTable.fnDraw(false);
			},true);
		}
		
		function delAll() {
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
			
			layer.confirm('确认删除吗？', function(index) {
				$.ajax({
					type : 'POST',
					url : '/confirmModify/deleteStudentModify.do',
					data : {
						idArray : chk_value
					},
					dataType : 'json',
					success : function(data) {
						if(data.code == _GLOBAL_SUCCESS){
							
							layer.msg('已删除!', {
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
		
		function dealCheck(checkStatus,e){
			if("1"!=checkStatus){
				$(e).attr("checked", false);
				layer.msg('此条目已提交审核不能删除！', {
					icon : 1,
					time : 1000
				});
			}
		}
		
		function _search() {
			myDataTable.fnDraw(true);
		}