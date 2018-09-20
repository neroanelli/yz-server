var myDataTable;
		$(function() {
			myDataTable = $('.table-sort').dataTable({
				"processing": true,
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : '/diplomaP/findDiplomaPList.do',
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
                    {"mData" : "placeName"},
					{"mData" : "province"},
					{"mData" : "address"},
					{"mData" : null},
					{"mData" : "createTime"},
                    {"mData" : null}
				],
				"columnDefs" : [
                        {"targets": 2,"class":"text-l"
                        },
						{"targets" : 3,"render" : function(data, type, row, meta) {
							if(row.status && row.status =='1'){
								return "<label class='label label-success radius'>启用</label>";
							}else{
								return "<label class='label  label-danger radius'>禁用</label>";
							}
						}},
						{"targets" : 5,"render" : function(data, type, row, meta) {
							if(row.status && row.status =='1'){
                                var dom = '<a href="javascript:;" title="禁用" onclick="updateStatus(\'' + row.placeId + '\',\''+1+'\')"><i class="iconfont icon-tingyong"></i></a>&nbsp;&nbsp;';
							}else{
                                var dom = '<a href="javascript:;" title="启用" onclick="updateStatus(\'' + row.placeId + '\',\''+2+'\')"><i class="iconfont icon-qiyong"></i></a>&nbsp;&nbsp;';
							}
							dom += '<a href="javascript:;" title="修改" onclick="editDiplomaP(\'' + row.placeId + '\')"><i class="iconfont icon-edit"></i></a></a>';
							return dom;
						}}
				]
			});
		});

		/*编辑*/
		function editDiplomaP(placeId) {
			var url = '/diplomaP/toUpdate.do' + '?placeId='+ placeId ;
			layer_show('编辑地址配置', url, null, null, function() {
                myDataTable.fnDraw(true);
			});
		}


		//打开添加页面
		function addDiplomaP() {
            var url = '/diplomaP/toAdd.do';
            layer_show('新增地址配置', url, null, null, function () {
                myDataTable.fnDraw(true);
            });
        }

        //更新状态
        function updateStatus(placeId,status) {
            $.ajax({
                type: 'POST',
                url: '/diplomaP/updateStatus.do',
                data: {
                    placeId : placeId,
					status : status
                },
                dataType: 'json',
                success: function (data) {
                    if (data.code == _GLOBAL_SUCCESS) {
                        myDataTable.fnDraw(false);
                    }
                }
            });
        }

