var modifyListTable;
$(function() {
    _init_select('modifyType', dictJson.modifyType);

    modifyListTable = $('#modifyListTable').dataTable({
        "serverSide" : true,
        "dom" : 'rtilp',
        "ajax" : {
            url : "/allStudent/getModifyList.do",
            type : "post",
            data : function(pageData) {
                pageData = $.extend({},{start:pageData.start, length:pageData.length}, $("#searchForm").serializeObject());
                console.log('我的学员------'+pageData);
                return pageData;
            }
        },
        "pageLength" : 10,
        "pagingType" : "full_numbers",
        "ordering" : false,
        "searching" : false,
        "lengthMenu" : [ 10, 20 ],
        "language" : _my_datatables_language,
        "createdRow" : function(row, data, dataIndex) {
            $(row).addClass('text-c');
        },
        "columns" : [
            {"mData" : "modifyType" },
            {"mData" : "modifyText"},
            {"mData" : "fileUrl"},
            {"mData" : "remark"},
            {"mData" : "createUser"},
            {"mData" : "createTime"},
            {"mData" : "isComplete"}
        ],
        "columnDefs" : [
            {"targets" : 0,"render" : function(data, type, row, meta) {
                return _findDict('modifyType', row.modifyType);
            }},
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
            { "targets" : 6,"render" : function(data, type, row, meta) {
                return '1' == row.isComplete ? '是' : '否';
            }}
        ]
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
function searchModifyList() {
    modifyListTable.fnDraw(true);
}