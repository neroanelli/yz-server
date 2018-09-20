$(function () {

    _init_select("checkStatus", [
        {"dictValue": "1", "dictName": "通过"},
        {"dictValue": "2", "dictName": "不通过"}
    ]);

    var url = '/pictureCollect/check.do';

    $("#checkStatus").change(function () {
        if($(this).val()=="1"){
            $("#checkRemark").val("您的图像信息已通过初审，等待新华社终审！")
        }else{
            $("#checkRemark").val("您的图像信息审核不通过，请重新上传图像！")
        }
    })

    $("#form-picCollect-edit").validate({
        rules: {
            checkStatus: {
                required: true
            },
            checkRemark: {
                required: true
            }
        },
        onkeyup: false,
        focusCleanup: true,
        success: "valid",
        submitHandler: function (form) {
            $(form).ajaxSubmit({
                type: "post", //提交方式
                dataType: "json", //数据类型
                url: url, //请求url
                success: function (data) { //提交成功的回调函数
                    if (data.code == _GLOBAL_SUCCESS) {
                        layer.msg('操作成功！', {icon: 1, time: 1000}, function () {
                            window.parent.myDataTable.fnDraw(false);
                            layer_close();
                        });
                    }
                }
            })
        }
    });
});
