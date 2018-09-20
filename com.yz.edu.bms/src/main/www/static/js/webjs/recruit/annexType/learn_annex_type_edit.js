$(function(){
    var isRequire = bdLearnAnnexType.isRequire;
    $("#isRequire-" + isRequire).attr("checked", "checked");

    var isVisible = bdLearnAnnexType.isVisible;
    $("#isVisible-" + isVisible).attr("checked", "checked");

    var isUpload = bdLearnAnnexType.isUpload;
    $("#isUpload-" + isUpload).attr("checked", "checked");

    $('input[type="radio"]:checked').removeAttr('disabled');

    initCheck();

	$("#form-annex-type-add").validate({
        rules:{
            annexTypeName:{
                remote:{
                    type : "POST",
                    url : '/learnAnnexType/existsName.do',
                    data:{
                        recruitType:$("#recruitType").val(),
                        annexTypeName:function() {
                            return $("#annexTypeName").val();
                        }
                    }
                },
                required : true
            },
            annexTypeValue:{
                required : true
            }
		},
        messages : {
            annexTypeName : {
                remote : "附件名称已存在"
            }
        },
		onkeyup : false,
		focusCleanup : true,
		success : "valid",
		submitHandler : function(form) {
			$(form).ajaxSubmit({
				type : "post", //提交方式
				dataType : "json", //数据类型
				url : '/learnAnnexType/editAnnexType.do', //请求url
				success : function(data) {
					//提交成功的回调函数
					if(data.code == _GLOBAL_SUCCESS){
						if(data.body=="success"){
                            layer.msg('操作成功！', {icon : 1, time : 1000},function(){
                                layer_close();
                            });
						}else{
                            layer.msg("附件值不能重复，操作失败！", {icon : 5, time : 1000},function(){
                                layer_close();
                            });
						}
					}
				}
			})
		}
	});


    $('input[name="isVisible"]').on('change', function () {
        initCheck();
    });
});

function initCheck() {
    if ($('input[name="isVisible"]:checked').val() == '1') {
        $('input[name="isUpload"]').removeAttr('disabled');
    } else {
        $('input[name="isUpload"][value="0"]').prop('checked',true)
        $('input[name="isUpload"]').attr('disabled', 'disabled');
    }
}

