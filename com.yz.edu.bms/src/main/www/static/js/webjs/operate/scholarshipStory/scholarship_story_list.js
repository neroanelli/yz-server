var myDataTable;
		$(function() {
			 _init_select("isAllow",[
			                        {"dictValue":"1","dictName":"启用"},
			                        {"dictValue":"2","dictName":"禁用"}
			                    ]);
       
			
			myDataTable = $('.table-sort').dataTable({
				"processing": true,
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : '/scholarshipStory/list.do',
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
					{"mData" : "articleTitle"},
					{"mData" : "entranceText"},
					{"mData" : "createUser"},
					{"mData" : null},
					{"mData" : null},
                    {"mData" : null}
				],
				"columnDefs" : [
                    	{"targets": 0,"render": function (data, type, row, meta) {
                            return '<input type="checkbox" value="' + row.scholarshipId + '" name="scholarshipId"/>';
                        }},
                        {"targets": 4,"render": function (data, type, row, meta) {
                            return row.createTime.substring(0,19);
                        }},
						{"targets" : 5,"render" : function(data, type, row, meta) {
							if(row.isAllow && row.isAllow =='1'){
								return "<label class='label label-success radius'>启用</label>";
							}else{
								return "<label class='label  label-danger radius'>禁用</label>";
							}
						}},
						{"targets" : 6,"render" : function(data, type, row, meta) {
							var allow = '启用';
							var no = '禁用';
							var dom = "";
							if(row.isAllow && row.isAllow =='2'){
								dom += '<a class="" href="javascript:;" title="启用" onclick="updateIsAllow(\'' + row.scholarshipId + '\',1,\'' + allow + '\')"><i class="iconfont icon-qiyong"></i></a>&nbsp;';
							}else{
								dom += '<a class="" href="javascript:;" title="禁用" onclick="updateIsAllow(\'' + row.scholarshipId + '\',2,\'' + no + '\')"><i class="iconfont icon-tingyong"></i></a>&nbsp;';
							}
							    dom += '<a class="" href="javascript:;" title="修改" onclick="editScholarshipStory(\'' + row.scholarshipId + '\')"><i class="iconfont icon-edit"></i></a>&nbsp;';
                                dom += '<a class="" href="javascript:;" title="删除" onclick="deleteScholarshipStory(\'' + row.scholarshipId + '\')"><i class="iconfont  icon-shanchu"></i></a>&nbsp;';
							return dom;
						}}
				]
			});
		});

		function searchResult(){
			myDataTable.fnDraw(true);
		}

		/**
		 * 新增发放任务
		 */
		function addScholarshipStory() {
	        var url = '/scholarshipStory/editOrAdd.do' + '?tjType=ADD';
	        layer_show('新增奖学金故事', url, null, 510, function () {
	            myDataTable.fnDraw(true);
	        },true);
	    }
		
		/**
		 * 修改
		 * @param scholarshipId
		 * @returns
		 */
		function editScholarshipStory(scholarshipId){
			 var url = '/scholarshipStory/editOrAdd.do' + '?scholarshipId=' + scholarshipId + '&tjType=UPDATE';
		        layer_show('修改奖学金故事', url, null, 510, function () {
		            myDataTable.fnDraw(false);
		        },true);
		}

		
		/**
		 * 修改   禁用和启用
		 * @param scholarshipId
		 * @returns
		 */
		function updateIsAllow(scholarshipId,isAllow,isAllowDes){
			
			layer.confirm('确认要' + isAllowDes + '吗？', function(index) {
				$.ajax({
					type : 'POST',
					url : '/scholarshipStory/updateIsAllow.do',
					data : {
						scholarshipId : scholarshipId,
						isAllow : isAllow
					},
					dataType : 'json',
					success : function(data) {
						
							layer.msg('已' + isAllowDes + '!', {
								icon : 1,
								time : 1000
							});
							
							myDataTable.fnDraw(false);
					},
					error : function(data) {
						layer.msg('' + isAllowDes + ' 失败！', {
							icon : 1,
							time : 1000
						});
						myDataTable.fnDraw(false);
					},
				});
			});
		}
		
		/**
		 * 删除
		 * @param scholarshipId
		 * @returns
		 */
		function deleteScholarshipStory(scholarshipId){
			layer.confirm('确认要删除吗？', function(index) {
				$.ajax({
					type : 'POST',
					url : '/scholarshipStory/deleteScholarshipStory.do',
					data : {
						scholarshipId : scholarshipId
					},
					dataType : 'json',
					success : function(data) {
						
							layer.msg('已删除!', {
								icon : 1,
								time : 1000
							});
							
							myDataTable.fnDraw(false);
					},
					error : function(data) {
						layer.msg('删除失败！', {
							icon : 1,
							time : 1000
						});
						myDataTable.fnDraw(false);
					},
				});
			});
		}
		
		//批量删除
		function batchDelete() {
            var chk_value = [];
            $("input[name=scholarshipId]:checked").each(function () {
                chk_value.push($(this).val());
            });

            if (chk_value == null || chk_value.length <= 0) {
                layer.msg('未选择任何数据!', {
                    icon: 5,
                    time: 1000
                });
                return;
            }
          
            layer.confirm('确认要删除所选数据吗？', function (index) {
                //此处请求后台程序，下方是成功后的前台处理……
                $.ajax({
                    type: 'POST',
                    url: '/scholarshipStory/deleteScholarshipStoryByIdArr.do',
                    data: {
                    	scholarshipIds: chk_value
                    },
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == _GLOBAL_SUCCESS) {
                            myDataTable.fnDraw(false);
                            $("input[name=all]").attr("checked", false);
                            layer.msg('删除成功!', {
                                icon: 1,
                                time: 1000
                            });
                        }
                    }
                });
            });
        }


		