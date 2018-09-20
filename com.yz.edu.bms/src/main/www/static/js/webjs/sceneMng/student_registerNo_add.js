$(function() {
    $("#form-member-add").validate({
        rules : {
        	username : {
                required : true,
            },
            password : {
                required : true,
            }
        },
        onkeyup : false,
        focusCleanup : true,
        success : "valid",
        submitHandler : function(form) {
            $(form).ajaxSubmit({
                type : "post", //提交方式  
                dataType : "json", //数据类型  
                url : '/bdSceneConfirm/insertRegisterNo.do', //请求url 
                success : function(data) { //提交成功的回调函数     
                    if (data.code == _GLOBAL_SUCCESS) {
                        layer.msg('操作成功！', {
                            icon : 1,
                            time : 1000
                        },function () {
                            layer_close(window.parent.registerAdd);
                        });
                    }
                }
            })
        }
    });
});

            