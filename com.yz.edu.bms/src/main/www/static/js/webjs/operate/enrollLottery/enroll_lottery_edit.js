$(function() {
        var action = '';
        
        if('UPDATE' == type) {
            action = '/enrollLottery/update.do';
            
            $("#lotteryName").val(lotteryInfo.lotteryName);
            var status = lotteryInfo.status;
            $("#is_allow_" + status).attr('checked', 'checked');
            $("#if_limit_" + lotteryInfo.ifLimitRegTime).attr('checked', 'checked');
            
            $("#startTime").val(lotteryInfo.startTime);
            $("#endTime").val(lotteryInfo.endTime);
            $("#remark").val(lotteryInfo.remark);
            $("#lotteryNum").val(lotteryInfo.lotteryNum);
            $("#lotteryUrl").val(lotteryInfo.lotteryUrl);
            
            var items = lotteryInfo.attrList;
            var dom = '';
            for (var i = 0; i < items.length; i++) {
                var item = items[i];
              
                dom += '<tr>';
                dom += '<td>' + item.dictName +'<input type="hidden" name="items[' + i + '].dictValue" value="' + item.dictValue + '" />';
                dom += '<input type="hidden" name="items[' + i + '].dictName" value="' + item.dictName + '" />';
                dom += '<input type="hidden" name="items[' + i + '].attrSeq" value="' + i + '" /></td>';
                var attrValue = item.attrValue ==null?'':item.attrValue;
               // dom += '<td><input type="input" class="input-text radius size-M" style="width:280px" name="items[' + i + '].attrValue" value="'+attrValue+'" /></td>';
                dom +='<td><div class="formControls col-xs-8 col-sm-9 skin-minimal">';
                dom +='<div class="radio-box"><input type="radio"'+(attrValue=='1'?' checked="checked" ':'')+'name="items[' + i + '].attrValue" value="1" /><label>参与</label></div>';
                dom +='<div class="radio-box"><input type="radio"'+(attrValue!='1'?' checked="checked" ':'')+'name="items[' + i + '].attrValue" value="0" /><label>不参与</label></div></td>';
                dom += '</tr>';
            }
            $("#items").html(dom);
            $.each($("#itemDiv").find("input[type='number']"), function (i, data) {
                $(data).rules('add', {required: true, range: [0, 100000], isStdFee: true});
            });
            
        } else if('ADD' == type) {
            action = '/enrollLottery/add.do';
            $("#is_allow_1").attr('checked', 'checked');
            
            var items = lotteryInfo.attrList;
            var dom = '';
            for (var i = 0; i < items.length; i++) {
                var item = items[i];
              
                dom += '<tr>';
                dom += '<td>' + item.dictName +'<input type="hidden" name="items[' + i + '].dictValue" value="' + item.dictValue + '" />';
                dom += '<input type="hidden" name="items[' + i + '].dictName" value="' + item.dictName + '" />';
                dom += '<input type="hidden" name="items[' + i + '].attrSeq" value="' + i + '" /></td>';
                var attrValue = item.attrValue ==null?'':item.attrValue;
                //dom += '<td><input type="input" class="input-text radius size-M" style="width:80px" name="items[' + i + '].attrValue" value="'+attrValue+'" /></td>';
                dom +='<td><div class="formControls col-xs-8 col-sm-9 skin-minimal">';
                dom +='<div class="radio-box"><input type="radio"'+(attrValue=='1'?' checked="checked" ':'')+'name="items[' + i + '].attrValue" value="1" /><label>参与</label></div>';
                dom +='<div class="radio-box"><input type="radio"'+(attrValue!='1'?' checked="checked" ':'')+'name="items[' + i + '].attrValue" value="0" /><label>不参与</label></div></td>';
                
                dom += '</tr>';
            }
            $("#items").html(dom);
            $.each($("#itemDiv").find("input[type='number']"), function (i, data) {
                $(data).rules('add', {required: true, range: [0, 100000], isStdFee: true});
            });
        } else {
            return false;
        }
       
        $('.skin-minimal input').iCheck({
            checkboxClass : 'icheckbox-blue',
            radioClass : 'iradio-blue',
            increaseArea : '20%'
        });
       
        
        $("#lottery-form").validate({
            onkeyup : false,
            focusCleanup : true,
            success : "valid",
            submitHandler : function(form) {
                $(form).ajaxSubmit({
                    type : "post", //提交方式  
                    dataType : "json", //数据类型  
                    url : action, //请求url 
                    success : function(data) { //提交成功的回调函数  
                        var msg = '';
                    	if('UPDATE' == type) {
                    	    msg = '更新成功';
                    	} else if('ADD' == type) {
                    	    msg = '添加成功';
                    	}
                    	if(_GLOBAL_SUCCESS == data.code) {
                    	    layer.msg(msg, {
                                icon : 1,
                                time : 1000
                              }, function(){
                                  layer_close();
                              });
                    	}
                    }
                });
            }
        });
    });