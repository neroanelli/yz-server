var myDataTable;
    $(function () {

        _init_select("recruitType", dictJson.recruitType);
        _init_select('scholarship', dictJson.scholarship);
        _init_select('stdStage', dictJson.stdStage);
        myDataTable = $('.table-sort')
            .dataTable({
            "processing": true,
			"serverSide" : true,
			"dom" : 'rtilp',
			"ajax" : {
				url : "/editpayable/list.do",
				type : "post",
				dataType : 'json',
				data : function(data) {
	                  return searchData(data);
                }
			},
			"pageLength" : 10,
			"pagingType" : "full_numbers",
			"ordering" : false,
			"searching" : false,
			"createdRow" : function(row, data,
					dataIndex) {
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
			} ], //[成教]汕头大学:(0003)会计学[专升本]
			"columnDefs" : [
			{
			"render" : function(data, type,
					row, meta) {
				var dom ='';
            	dom += '<a title="查看学员信息" onclick="toLook(\''+ row.learnId +'\',\''+ row.stdId +'\',\''+ row.recruitType+'\')"><span class="c-blue">'+ row.stdName +'</span></a>'
            	if(row.stdType ==='2'){
            		dom += ' <sup style="color:#f00">外</sup>';
            	}
				return dom;
			},
			"targets" : 0,"class":"text-l"
			},
			{
				"render" : function(data, type,
						row, meta) {

					return row.grade + '级';
				},
				"targets" : 1
			},{
				"render" : function(data, type, row, meta) {
                    var dom = '';
                    dom+=_findDict("recruitType", row.recruitType).indexOf("成人")!=-1?"[成教]":"[国开]";
                    dom += row.unvsName+"<br>";
                    dom+=_findDict("pfsnLevel", row.pfsnLevel).indexOf("高中")!=-1?"[专科]":"[本科]";
                    dom += row.pfsnName;
                    dom += '(' + row.pfsnCode + ')';
                    return dom;
				},
				"targets" : 2,
                "class":"text-l"
			},
			{
				"render" : function(data, type,
						row, meta) {
					return _findDict(
							"stdStage",
							row.stdStage);
				},
				"targets" : 3
			},
			{
			"render" : function(data, type,
					row, meta) {
				var dom = '';
				dom += '<table class="table table-border table-bordered radius">';
				dom += '<thead>';
				dom += '<tr>';
				dom += '<th class="td-s" width="80">科目</th>';
				dom += '<th class="td-s" width="40">应缴</th>';
				dom += '<th class="td-s" width="40">缴费状态</th>';
				dom += '</tr>';
				dom += '</thead><tbody>';

				var amount = 0.00;
				if(null != row.payInfos && row.payInfos.length > 0){
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
						if('2' == status){
							dom += '' + _findDict("orderStatus", status) + '';
						}else{
							dom += '<span>' + _findDict("orderStatus", status) + '</span>';
						}
						dom += '</td>';
						dom += '</tr>';
						amount = amount
								+ parseFloat(payInfo.payable);
					}
				}
				dom += '<tr >';
				dom += '<td class="td-s">合计：</td>';
				amount = amount.toFixed(2);
				dom += '<td class="td-s">' + amount
						+ '</td>';
				dom += '<td class="td-s"></td>';
				dom += '</tr></tbody>';
				dom += '</table>';
				return dom;
			},
			"targets" : 4
			},
			{
			"render" : function(data, type,
					row, meta) {
				var dom = '';
				var canpay = false;
				var orderStatus = row.orderStatus;
				if('1' == orderStatus && null!=row.payInfos){
					for (var i = 0; i < row.payInfos.length; i++) {
						var payInfo = row.payInfos[i];
						if(payInfo.subOrderStatus == 1){
							canpay = true;
						}
					}
					if(canpay){
						dom += '<a class="tableBtn normal mb-10" style="display: inline-block;padding: 2px 5px;margin-bottom: 5px;"  onclick="toEdit(\''+ row.learnId + '\')">变更应缴</a><br>';
					}
				}
				return dom;
				},
				"targets" : 5
			} ]
		});
    });
	function searchData(data){
				return {
					studentName : $("#studentName").val() ? $("#studentName").val() : '',
					idCard : $("#idCard").val() ? $("#idCard").val() : '',
					mobile : $("#mobile").val() ? $("#mobile").val() : '',
					recruitType : $("#recruitType").val() ? $("#recruitType").val() : '',
					scholarship : $("#scholarship").val() ? $("#scholarship").val() : '',
					stdStage : $("#stdStage").val() ? $("#stdStage").val() : '',
					start : data.start,
					length : data.length
				};
			}

    function tosearch() {
        myDataTable.fnDraw(true);
    }
    
    function toEdit(learnId){
    	var url = '/editpayable/toEdit.do' + '?learnId=' + learnId;
				layer_show('变更应缴', url, null, 510, function() {
//					myDataTable.fnDraw(false);
				}, true);
    }
    
    /*用户-编辑*/
			function toLook(learnId, stdId,recruitType) {
				var url = '/studentBase/toEdit.do' + '?learnId='
						+ learnId + '&stdId=' + stdId+"&recruitType="+recruitType;
				layer_show('学员信息', url, null, null, function() {
//					myDataTable.fnDraw(false);
				}, true);
			}
 