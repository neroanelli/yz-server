$(function(){

            var text = '';
            if (recruitType) {
                if(_findDict("recruitType", recruitType).indexOf("成人")!=-1){
                    text += "[成教]";
                }else {
                    text += "[国开]";
                }
            }
            if(unvsName) text += unvsName;
            if(pfsnLevel) {
                if(_findDict("pfsnLevel", pfsnLevel).indexOf("高中")!=-1){
                    text += "[专科]";
                }else {
                    text += "[本科]";
                }
            }
            if(pfsnName) text += pfsnName;
            if(pfsnCode) text += "(" + pfsnCode + ")";
            $("#unvsInfo").text(text);
            $("#stdStage").text(_findDict('stdStage', stdStage));
            $("#scholarship").text(_findDict('scholarship', scholarship));
            $("#remark").text(remark);
            $("#grade").text(grade+"级");
            if (startTime) {
                var weekday=['星期日','星期一','星期二','星期三','星期四','星期五','星期六'];
                var week = weekday[new Date(startTime.substring(0,19).replace(/-/g,'/')).getDay()];
                $("#date").text(startTime.substring(0, 10)+ "("+week+")");
            }
            if (startTime && endTime) {
                var hour =  startTime.substring(11, 13);
                var hour =  startTime.substring(11, 13);
                var str=''
                if(hour<12){
                    str='上午'
                }
                else if(hour>=12 && hour<=18){
                    str='下午'
                }else {
                    str='晚上'
                }
                var time= str+startTime.substring(11, 16) + "-" + endTime.substring(11, 16) ;
                $("#time").text(time);
            }

            var url = '/sceneConfirm/updateConfirmInfo.do';

			$("#form-xuexin-edit").validate({
				onkeyup : false,
				focusCleanup : true,
				success : "valid",
				submitHandler : function(form) {
					$(form).ajaxSubmit({
						type : "post", //提交方式
						dataType : "json", //数据类型
						url : url, //请求url
						success : function(data) { //提交成功的回调函数
							if(data.code == _GLOBAL_SUCCESS){
								layer.msg('操作成功！', {icon : 1, time : 1000},function(){
									window.parent.myDataTable.fnDraw(false);
									layer_close();
								});
							}
						}
					})
				}
			});

		});