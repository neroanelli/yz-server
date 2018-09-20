var myDataTable;
		$(function() {
			$('select').select2({
				placeholder : "--请选择--",
				allowClear : true,
				width : "59%"
			});
            _init_select('stdStage', dictJson.stdStage);
            _init_select('scholarship', dictJson.scholarship);
            
            
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
            
          //校区-部门-组 联动
            _init_campus_select("campusId", "dpId", "groupId", '/campus/selectAllList.do', '/dep/selectAllList.do', '/group/selectAllList.do');
            
			//初始状态
            
			_init_select("hasUsername",[
				{"dictValue":"1","dictName":"有"},
				{"dictValue":"0","dictName":"无"}
			]);
			_init_select("hasRegisterNo",[
				{"dictValue":"1","dictName":"有"},
				{"dictValue":"0","dictName":"无"}
			]);
			
           
		
          
            _init_select("examPayStatus",[
                {"dictValue":"0","dictName":"否"},
                {"dictValue":"1","dictName":"是"}
            ]);
            
            _init_select("educationAppraisal",[
                {"dictValue":"0","dictName":"不合格"},
                {"dictValue":"1","dictName":"合格"},
                {"dictValue":"2","dictName":"通过（应届生）"},
                {"dictValue":"3","dictName":"往届待验"},
                {"dictValue":"4","dictName":"无需验证"},
             ]);
            
            _init_select("placeConfirmStatus",[
                {"dictValue":"0","dictName":"未预约"},
                {"dictValue":"1","dictName":"已预约"},
                {"dictValue":"2","dictName":"已重置"}
            ]);
            _init_select("mobileBindStatus",[
                {"dictValue":"1","dictName":"未绑定"},
                {"dictValue":"2","dictName":"已绑定"}
            ]);
            _init_select("picCollectStatus",[
                {"dictValue":"1","dictName":"未采集"},
                {"dictValue":"2","dictName":"已采集"}
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
			
		  
			
			myDataTable = $('.table-sort').dataTable({
				"processing": true,
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : '/printingForecast/findPrintingForecast.do',
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
					{"mData": null},
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
                    {"mData" : null}
				],
				"columnDefs" : [
						{ "targets": 0,
						    "render": function (data, type, row, meta) {
						    		 return '<input type="checkbox" value="' + row.registerId + '" name = "registerId" "/>';
						    },
						   
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
                            dom += (row.address==null?"":'<div style="display:-webkit-box;height:38px;width:285px;overflow:hidden; text-overflow:ellipsis; word-break:break-all; -webkit-box-orient:vertical; -webkit-line-clamp:2;">地址：' +row.address+'</div>');
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
                            }else{
                                return "<label class='label label-danger radius'>未采集</label>";
                            }
                        }},
						{"targets" : 12,"class" : "text-l","render" : function(data, type, row, meta) {
							var dom = ''
							dom +=  (row.username==null?"":"报名号："+row.username) + "<br/>";
							dom +=  (row.password==null?"":"密码：" +row.password) + "<br/>";
							dom +=  (row.registerNo==null?"":"编号：" +row.registerNo);
							return dom;
						}},{"targets" : 13,"render" : function(data, type, row, meta) {
							if(row.educationAppraisal!=null){
								if(row.educationAppraisal=='0'){
									return "<label class='label label-danger radius'>不合格</label>";
								}else{
									if(row.educationAppraisal=='1'){
										return "<label class='label label-success radius'>合格</label>";
									}else if(row.educationAppraisal=='2'){
										return "<label class='label label-success radius'>通过（应届生）</label>";
									}else if(row.educationAppraisal=='3'){
										return "<label class='label label-success radius'>往届待验</label>";
									}else if(row.educationAppraisal=='4'){
										return "<label class='label label-success radius'>无需验证</label>";
									}
									
								}
							}else{
								return '';
							}
						}},
						{"targets" : 14,"render" : function(data, type, row, meta) {
							if(row.examPayStatus && row.examPayStatus=='1'){
								return "<label class='label label-success radius'>已缴费</label>";
							}else{
								return "<label class='label label-danger radius'>未缴费</label>";
							}
						}},
						{"targets" : 15,"render" : function(data, type, row, meta) {
							/*var dom = ''*/
						/*	dom += '<a href="javascript:void(0)"><img id="u1640_img" class="img " src="../images/u1638.png"/></a>';
							dom += '&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)"><img id="u1640_img" class="img " src="../images/u1622.png"/></a>';*/
						    var dom = '<a href="javascript:void(0)" title="打印" onclick="pdfPrint(\'' + row.registerId + '\')">打印</a>';
							return dom;
						}}
				]
			});
		});

		
		
		
		function pdfPrintByQuery(){
			/*var  a =  $("#export-form").serializeObject();
			var array = [];
		
			debugger;
			for(var i = 0 ;i<a.length;i++){
				array[i] = a[i];
			}*/
			
			var url = '/printingForecast/forecastPrintsByQuery.do' + '?campusId=' + $("input[name='campusId']").val()+'&content='+$("input[name='content']").val()+'&dpId='+$("input[name='dpId']").val()+'&educationAppraisal='+$("input[name='educationAppraisal']").val()+'&endTime='+$("input[name='endTime']").val()+'&examPayStatus='+$("input[name='examPayStatus']").val()+'&grade='+$("input[name='grade']").val()+'&hasRegisterNo='+$("input[name='hasRegisterNo']").val()+'&hasUsername='+$("input[name='hasUsername']").val()+'&idCard='+$("input[name='idCard']").val()+'&mobile='+$("input[name='mobile']").val()+'&pfsnLevel='+$("input[name='pfsnLevel']").val()+'&placeConfirmStatus='+$("input[name='placeConfirmStatus']").val()+'&recruit='+$("input[name='recruit']").val()+'&scholarship='+$("input[name='scholarship']").val()+'&startTime='+$("input[name='startTime']").val()+'&stdName='+$("input[name='stdName']").val()+'&stdStage='+$("input[name='stdStage']").val()+'&taId='+$("input[name='taId']").val()+'&unvsId='+$("input[name='unvsId']").val()+'&webRegisterEndTime='+$("input[name='webRegisterEndTime']").val()+'&webRegisterStartTime='+$("input[name='webRegisterStartTime']").val()+'&pfsnId='+$("input[name='pfsnId']").val();
			window.open(url);
			/*$.ajax({
				type : 'POST',
				url : '/printingForecast/forecastPrintsByQuery.do',
				data : {
					query : $("#export-form").serializeObject()
				},
				dataType : 'json',
				success : function(data) {
					window.open(url);
				},
				error : function(data) {
					layer.msg('打印失败！', {
						icon : 1,
						time : 1000
					});
					myDataTable.fnDraw(false);
				},
			});*/
		}
		
		function pdfPrint(registerId){
			var url = '/printingForecast/forecastPrints.do' + '?registerId=' + registerId;
			window.open(url);
			/*$.ajax({
				type : 'POST',
				url : '/printingForecast/forecastPrints.do',
				data : {
					registerId : registerId
				},
				dataType : 'json',
				success : function(data) {
					window.open(data);
				},
				error : function(data) {
					layer.msg('打印失败！', {
						icon : 1,
						time : 1000
					});
					myDataTable.fnDraw(false);
				},
			});*/
		}
		
		
		function printForecast() {
	        var chk_value = [];
	        var $input = $("input[name=registerId]:checked");

	        $input.each(function () {
	            chk_value.push($(this).val());
	        });

	        if (chk_value == null || chk_value.length <= 0) {
	            layer.msg('未选择任何数据!', {
	                icon: 5,
	                time: 1000
	            });
	            return;
	        }
	        var url = '/printingForecast/forecastPrintsArray.do' + '?registerIds[]=' + chk_value;
			window.open(url);
	      //  var url = "/printingForecast/forecastPrints.do" + '?registerIds[]=' + chk_value;
	 
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