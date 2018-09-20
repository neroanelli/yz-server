var myDataTable;
$(function() {
	
	//初始化院校名称下拉框
	_simple_ajax_select({
				selectId : "unvsIdFee",
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
	$("#unvsIdFee").append(new Option("", "", false, true));
 	
	//初始化年级下拉框
	 _init_select("gradeFee",dictJson.grade);
	
	//初始化院校类型下拉框
	_init_select("recruitTypeFee",dictJson.recruitType);
 	
	myDataTable = $('#feeListTable').dataTable(
			{
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : "/check/feeList.do",
					data:{
					    "gCheckType":"5",
						"unvsId" : function() {
							return $("#unvsIdFee").val();
						},"recruitType" : function() {
							return $("#recruitTypeFee").val();
						},"grade" : function() {
							return $("#gradeFee").val();
						},"pfsnName" : function() {
							return $("#pfsnName").val();
						},"stdName" : function() {
							return $("#stdName").val();
						},"idCard" : function() {
							return $("#idCard").val();
						},"mobile" : function() {
							return $("#mobile").val();
						}
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
				},{
					"mData" : "tutor"
				},{
					"mData" : null
				},{
					"mData" : null
				}, {
					"mData" : "remark"
				}],
				"columnDefs" : [
					            {"render" : function(data, type, row, meta) {
									return '<input type="checkbox" value="'+ row.checkId + '" name="checkIds"/>';
								   },
								"targets" : 0
								},
								{"render" : function(data, type, row, meta) {
									var dom ='';
									dom +='<a class="maincolor" href="javascript:;" onclick="std_look(\'' + row.learnId + '\',\'' + row.stdId + '\')">'+row.stdName+'</a>';
									return dom;
								   },
								   "targets" : 1
								},
								{"render" : function(data, type, row, meta) {
									  return row.grade +'级';
								   },
								   "targets" : 2
								},
								{
									"render" : function(data, type, row, meta) {
                                        var dom = '';
                                        dom+=_findDict("recruitType", row.recruitType).indexOf("成人")!=-1?"[成教]":"[国开]";
                                        dom += row.unvsName+"<br>";
                                        dom+=_findDict("pfsnLevel", row.pfsnLevel).indexOf("高中")!=-1?"[专科]":"[本科]";
                                        dom += row.pfsnName;
                                        dom += '(' + row.pfsnCode + ')';
                                        return dom;
									},
									"targets" : 3,"class":"text-l"
								},{
									"render" : function(data, type,
											row, meta) {
										var dom = '';
										dom += '<table class="table table-border table-bordered radius">';
										dom += '<thead>';
										dom += '<tr>';
										dom += '<th width="80" class="td-s">科目</th>';
										dom += '<th width="40" class="td-s">应缴</th>';
										dom += '<th width="40" class="td-s">缴费状态</th>';
										dom += '</tr>';
										dom += '</thead><tbody>';

										var amount = 0.00;
										for (var i = 0; i < row.payInfos.length; i++) {
											var payInfo = row.payInfos[i];
											dom += '<tr>';
											var itemName = getItemName(payInfo.itemName,row.grade);
											dom += '<td class="td-s">'
													+ payInfo.itemCode
													+ ':'
													+ itemName
													+ '</td>';
											dom += '<td class="td-s">'
													+ payInfo.payable
													+ '</td>';
											var status = payInfo.subOrderStatus;
											dom += '<td class="td-s">';
											dom += '<span>' + _findDict("orderStatus", status) + '</span>';
											dom += '</td>';
											dom += '</tr>';
											amount = amount
													+ parseFloat(payInfo.payable);
										}
										dom += '<tr >';
										dom += '<td class="td-s">合计：</td>';
										dom += '<td class="td-s">' + amount
												+ '</td>';
										dom += '<td class="td-s"></td>';
										dom += '</tr></tbody>';
										dom += '</table>';
										return dom;
									},
									"targets" : 5
								},
								{"render" : function(data, type, row, meta) {
									 var dom ='';
									 for(var i=0;i<dictJson.saStatus.length;i++){
						          		 if(dictJson.saStatus[i].dictValue == row.checkStatus){
						          			 if(row.checkStatus == 2){
						          				dom +='<span class="label label-success radius" style="cursor:pointer;">'+dictJson.saStatus[i].dictName+'</span>';
						          			 }else if(row.checkStatus ==3){
						          				dom +='<span class="label label-danger radius" style="cursor:pointer;" >'+dictJson.saStatus[i].dictName+'</span>'; 
						          			 }else{
						          				 dom +=dictJson.saStatus[i].dictName;
						          			 }
						          			 return dom;
						          		 }
						          	 }
									 return "";
									},
									"targets" : 6
								},{
                        			"targets" : 7,"class":"text-l"
								}, {"render" : function(data, type, row, meta) {
										var dom = '';
										if(row.status ==2){
											var exType="LOOK";
											dom +='<a class=" tableBtn normal" href="javascript:;" onclick="check_data(\'' + row.checkId + '\',\'' + row.learnId + '\',\'' + row.stdName + '\',\'' + exType + '\')">查看核查</a>';
										}else{
											var exType="CHECK";
											dom +='<a class=" tableBtn normal" href="javascript:;" onclick="check_data(\'' + row.checkId + '\',\'' + row.learnId + '\',\'' + row.stdName + '\',\'' + exType + '\')">进入核查</a>';
										}
										
									
										return dom;
									},
									"targets" : 8
								}
					  ]
			});

	});

	function searchDep(){
		myDataTable.fnDraw(true);
	}

	 /*用户-编辑*/
    function check_data(checkId,learnId,stdName,exType) {
      var url = '/check/checkFee.do' + '?checkId=' + checkId+'&learnId='+learnId+'&stdName='+stdName+'&exType='+exType;
      layer_show(' 学费核查', url, 1000, 510, function() {
        myDataTable.fnDraw(false);
      });
    }
	 
    function delAll() {
		var chk_value = [];
		$("input[name=graduateIds]:checked").each(function() {
			chk_value.push($(this).val());
		});
		if(chk_value.length ==0){
			layer.msg('请选择要删除的数据！', {
				icon : 2,
				time : 2000
			});
			return;
		}
		layer.confirm('确认要删除吗？', function(index) {
			$.ajax({
				type : 'POST',
				url : '/graduate/deleteGraduate.do',
				data : {
					idArray : chk_value
				},
				dataType : 'json',
				success : function(data) {
					layer.msg('已删除!', {
						icon : 1,
						time : 1000
					});
					myDataTable.fnDraw(false);
					$("input[name=all]").attr("checked", false);
				},
				error : function(data) {
					layer.msg('删除失败！', {
						icon : 1,
						time : 1000
					});
					myDataTable.fnDraw(false);
					$("input[name=all]").attr("checked", false);
				},
			});
		});
	}