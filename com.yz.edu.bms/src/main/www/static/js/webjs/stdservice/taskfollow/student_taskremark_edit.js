$(function () {
	
	
    $("#form-base-info").validate({
        rules: {
            remarks: {required: true, maxlength: 50}
        },
        onkeyup: false,
        focusCleanup: true,
        success: "valid",
        submitHandler: function (form) {
            $(form).ajaxSubmit({
                type: "post", //提交方式
                dataType: "json", //数据类型
                url: '/taskfollow/editTaskRemark.do', //请求url
                success: function (data) {
                    //提交成功的回调函数
                    if ('00' == data.code) {
                        layer.msg('备注信息保存成功', {icon: 1, time: 1000}, function () {
                            window.parent.myDataTable.fnDraw(true);
                            layer_close();
                        });
                    }
                }
            });
        }
    });
    
    var remarksList  = $("#remarksList").val();
	
	if(remarksList != null && remarksList != "" && remarksList != undefined){
		$("#historyRemarks").show();
		var htm = $("#historyRemarks");
		var data = eval(remarksList); 
		var dom = '';
		for(var i = data.length-1 ; i >= 0; i--){
			var remarksInfo = data[i].remarks;  //备注
			var updater = data[i].updater;   //修改人
			var updatetime = data[i].updatetime;   //修改时间
			
			if(i == data.length-1){
				dom = '<div class="row cl" style = "padding-left:20px;"> <div class="col-xs-9 col-sm-9 col-xs-offset-3 col-sm-offset-3 mt-10">';
			}
			dom += '<p>'+ remarksInfo +' </p><p><span>创建人：'+ updater +'</span><span style="padding-left:10px;">创建时间：'+ updatetime +'</span></p>';
			if(i == 0){
				dom += '</div> </div>'
			}
			
			
		}
		htm.append(dom);
	}
});
