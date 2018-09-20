$(function() {
        //年级
        $("#grade").val(_findDict("grade",$("#grade").val()));
        //层次
        $("#pfsnLevel").val(_findDict("pfsnLevel",$("#pfsnLevel").val()));
        //专业
        $("#pfsnName").val("("+$("#pfsnCodev").val()+")"+$("#pfsnNamev").val());
        //学员阶段
        $("#stdStage").val(_findDict("stdStage",$("#stdStage").val()));
        //性别
        $("#sexv").val(_findDict("sex",$("#sexv").val()));
        $("#sex").val(_findDict("sex",$("#sex").val()));
        //民族
        $("#nationv").val(_findDict("nation",$("#nationv").val()));
        $("#nation").val(_findDict("nation",$("#nation").val()));
        //优惠类型
        $("#scholarshipv").val(_findDict("scholarship",$("#scholarshipv").val()));
        $("#scholarship").val(_findDict("scholarship",$("#scholarship").val()));
        $("#scholarship1").val(_findDict("scholarship",$("#scholarship1").val()));
        $("#form-member-add").validate({
            onkeyup : false,
            focusCleanup : true,
            success : true,
            submitHandler : function(form) {
                if('2'==$('#checkStatus').val()){
                    $(form).ajaxSubmit({
                        type : "post", //提交方式
                        dataType : "json", //数据类型
                        url : '/studentModify/passStudentAuditModify.do', //请求url
                        success : function(data) { //提交成功的回调函数
                            if(data.code == _GLOBAL_SUCCESS){
                                layer.msg('操作成功！', {icon : 1, time : 1000},function(){
                                    layer_close();
                                });
                            }
                        }
                    });
                }else{
                    layer.prompt({
                        title : '填写驳回原因：',
                        formType : 2
                    }, function(text, index) {
                        $('#reason').val(text);
                        $(form).ajaxSubmit({
                            type : "post", //提交方式
                            dataType : "json", //数据类型
                            url : '/studentModify/passStudentAuditModify.do', //请求url
                            success : function(data) { //提交成功的回调函数
                                if(data.code == _GLOBAL_SUCCESS){
                                    layer.msg('操作成功！', {icon : 1, time : 1000},function(){
                                        layer_close();
                                    });
                                }
                            }
                        });
                    });
                }
            }
        });
    });