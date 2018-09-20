$(function() {
        $("#s_sex").val(_findDict("sex",$("#s_sex").val()));
        $("#s_nation").val(_findDict("nation",$("#s_nation").val()));
        $("#s_politicalStatus").val(_findDict("politicalStatus",$("#s_politicalStatus").val()));
        $("#s_pfsnLevel").val(_findDict("pfsnLevel",$("#s_pfsnLevel").val()));
        $("#s_edcsType").val(_findDict("edcsType",$("#s_edcsType").val()));
        $("#s_jobType").val(_findDict("jobType",$("#s_jobType").val()));
        //年级
        //层次
        $("#pfsnLevel").val(_findDict("pfsnLevel",$("#pfsnLevel").val()));
        $("#npfsnLevel").val(_findDict("pfsnLevel",$("#npfsnLevel").val()));
        //户口所在地
        $("#rprAddress").val(_findDict("rprAddressCode",$("#rprAddress").val()));

        //学员阶段

        //性别
        $("#sexv").val(_findDict("sex",$("#sexv").val()));
        $("#sex").val(_findDict("sex",$("#sex").val()));

        //民族
        $("#nationv").val(_findDict("nation",$("#nationv").val()));
        $("#nation").val(_findDict("nation",$("#nation").val()));
        //优惠类型
        $("#scholarshipv").val(_findDict("scholarship",$("#scholarshipv").val()));
        $("#scholarship").val(_findDict("scholarship",$("#scholarship").val()));
        //原学历类型
        $("#graduateEdcsTypev").val(_findDict("edcsType",$("#graduateEdcsTypev").val()));
        $("#graduateEdcsType").val(_findDict("edcsType",$("#graduateEdcsType").val()));
        //防重操作
        var isSub = true;
        $("#form-member-revise").validate({
            onkeyup : false,
            focusCleanup : true,
            success : true,
            submitHandler : function(form) {

                    if('3'==$('#checkStatus').val()){
                        isSub = true;
                        layer.confirm('确认修改吗？', function(index) {
                            if (isSub) {
                                isSub = false;
                                $(form).ajaxSubmit({
                                    type : "post", //提交方式
                                    dataType : "json", //数据类型
                                    url : '/examModifyRevise/passModify.do', //请求url
                                    success : function(data) { //提交成功的回调函数
                                        if(data.code == _GLOBAL_SUCCESS){
                                            layer.msg('操作成功！', {icon : 1, time : 1000},function(){
                                                layer_close();
                                            });
                                        }
                                    }
                                });
                            }
                        });
                    }else{
                        isSub = true;
                        layer.prompt({
                            title : '填写驳回原因：',
                            formType : 2
                        }, function(text, index) {
                            if(text.length > 50){
                                layer.msg('驳回成功!', {
                                    icon : 5,
                                    time : 1000
                                });
                                return;
                            }
                            if (isSub) {
                                isSub = false;
                                $('#reason').val(text);
                                $(form).ajaxSubmit({
                                    type: "post", //提交方式
                                    dataType: "json", //数据类型
                                    url: '/examModifyRevise/passModify.do', //请求url
                                    success: function (data) { //提交成功的回调函数
                                        if (data.code == _GLOBAL_SUCCESS) {
                                            layer.msg('操作成功！', {icon: 1, time: 1000}, function () {
                                                layer_close();
                                            });
                                        }
                                    }
                                });
                            }
                        });
                    }

            }
        });
    });