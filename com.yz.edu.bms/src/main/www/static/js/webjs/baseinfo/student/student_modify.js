 var modifyListTable;
 $(function () {
        modifyListTable = $('#modifyListTable').dataTable({
            "serverSide": true,
            "dom": 'rtilp',
            "ajax": {
                url: "/allStudent/getModifyList.do",
                type: "post",
                data: {
                    learnId: varLearnId
                }
            },
            "search": {
                "search": "新生信息修改"
            },
            "pageLength": 10,
            "pagingType": "full_numbers",
            "ordering": false,
            "searching": false,
            "lengthMenu": [10, 20],
            "language": _my_datatables_language,
            "createdRow": function (row, data, dataIndex) {
                $(row).addClass('text-c');
            },
            "columns": [{
                "mData": "modifyType"
            }, 
            { "mData": "modifyText"},
            {"mData" : "fileUrl"}, 
            {"mData" : "remark"}, 
            { "mData": "createUser"
            }, {
                "mData": "createTime"
            }, {
                "mData": "isComplete"
            }],
            "columnDefs": [{
                "render": function (data, type, row, meta) {
                    var modifyShowArr=window.parent.modifyShowArr;
                    if(modifyShowArr){
                        for(var i=0;i<modifyShowArr.length;i++){
                            if(row.modifyType==modifyShowArr[i]){
                                return "<p class='studentInfoModify'>"+_findDict('modifyType', row.modifyType)+"</p>"
                            }
                        }
                    }
                    return "<p class='studentInfoModifyHide'>"+_findDict('modifyType', row.modifyType)+"</p>"

                },
                "targets": 0
            },
             {"targets" : 1,"render" : function(data, type, row, meta) {
             	var modifyText=row.modifyText;
             	modifyText=modifyText.replace(/,/g,"<br>");
             	return modifyText;
            }},
             {"targets" : 2,"render" : function(data, type, row, meta) {
                if(row.fileUrl==null||row.fileUrl==""){
            		return '';
            	}else{
            		if(row.fileUrl.endsWith("jpg")||row.fileUrl.endsWith("png")||row.fileUrl.endsWith("jpeg")){
            			return '<span onclick="openPhoto(event,this)"><img class="imgChild" style="max-width: 70px;" layer-src="' + _FILE_URL + row.fileUrl + '"  src="' + _FILE_URL + row.fileUrl + '" onerror="nofind(this)" /></span>';
            		}else{
            			return '<a href="'+ _FILE_URL + row.fileUrl+'">'+row.fileUrl+'</a>';
            		}
            	}
            }},
            {
                "render": function (data, type, row, meta) {
                    return '1' == row.isComplete ? '是' : '否';
                },
                "targets": 6
            }],
            "initComplete":function () {
//                特殊处理，只显示的异动数据
                if(modifyListTable.find(".dataTables_empty").length){
                    return
                }
                var modifyShowArr=window.parent.modifyShowArr;
                if(modifyShowArr){
                    var dom='<tr class="odd"><td valign="top" colspan="5" class="studentInfoModify"><div style="text-align:center;font:bold 13px/22px arial,sans-serif;color:red;">没有检索到数据！</div></td></tr>'
                    if(!$(".studentInfoModify").length){
                        modifyListTable.find('tbody').append(dom)
                    }
                    $(".studentInfoModifyHide").parents('tr').hide();
                }
            }
        });
    });
    
    function openPhoto(e,obj) {
        e.stopPropagation();
        var src = $(obj).find('img').attr('src');
        $('#photoPreview').fadeIn(500).find('img').attr('src', src);
    }

    $(document).on('click',function () {
        $('#photoPreview').fadeOut(500)
    });
