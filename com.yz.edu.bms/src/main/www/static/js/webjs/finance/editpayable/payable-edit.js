$(function () {
	 $("#stdName").text(varStudent.stdName+"-"+varStudent.grade+"级"+"-"+varStudent.unvsName+"-"+varStudent.pfsnName+"-"+_findDict("pfsnLevel", varStudent.pfsnLevel));
	 $("#learnId").val(varStudent.learnId);
	 $("#stdId").val(varStudent.stdId);
	 initItemDom(varStudent.payInfos);
$("#form-std-pay").validate({
        messages: {},
        onkeyup: false,
        focusCleanup: true,
        success: "valid",
        submitHandler: function (form) {
            var itemCodes=[] ;
            var amounts=[] ;
             var subOrderNos=[] ;
             $('input[id=refundAmount]').each(function (i) {
		        var amount = $(this).val();
		        if(amount != null&&amount!=""){
		        	 for(var i = 0; i < varStudent.payInfos.length; i++){
		        	 	if(varStudent.payInfos[i].itemCode==$(this).attr("name")){
		        	 		if(amount!=varStudent.payInfos[i].payable){
		        	 		 itemCodes.push($(this).attr("name"));
		        	 		 amounts.push($(this).val());
		        	 		 subOrderNos.push(varStudent.payInfos[i].subOrderNo);
		        	 		}
		        	 	}
		        	 }
		        }
		 	   });
            if(itemCodes.length <= 0){
                layer.msg('请设置应缴金额', {icon: 5, time: 1000}, function () {
                });
                return ;
            }
            if($("#remark").val()==""){
            	layer.msg('请输入备注信息', {icon: 5, time: 1000}, function () {
                });
                return ;
            }
        	$("#itemCodes").val(itemCodes);
        	$("#amounts").val(amounts);
        	$("#subOrderNos").val(subOrderNos);
            $(form).ajaxSubmit({
                type: "post", //提交方式
                dataType: "json", //数据类型
                url: "/editpayable/edit.do", //请求url
                // contentType:"application/json",
                success: function (data) { //提交成功的回调函数
                    if (data.code == _GLOBAL_SUCCESS) {
                    	if(data.body=='SUCCESS'){
                        layer.msg('变更成功！', {icon: 1, time: 1000}, function () {
                                window.parent.myDataTable.fnDraw(false);
                                layer_close();
                        });
                    	}else{
                    		layer.msg(data.body, {icon: 2, time: 1000}, function () {
                        });
                    	}
                    }
                }
            });
        }
    });
});

 function initItemDom(row) {
        var dom = '';
        dom += '<tbody>';
        for (var i = 0; i < row.length; i++) {
            var payInfo = row[i];

            dom += '<tr>';
            dom += '<td >' + payInfo.itemCode + '</td>';
            dom += '<td >' + payInfo.payable + '</td>';
            var status = payInfo.subOrderStatus;
			dom += '<td class="td-s">';
			if('2' == status){
				dom += '' + _findDict("orderStatus", status) + '';
			}else{
				dom += '<span>' + _findDict("orderStatus", status) + '</span>';
			}
			dom += '</td>';
			if('2' == status){
				 dom += '<td ' + payInfo.payable + '</td>';
			}else{
				 dom += '<td>' + '<input type="hidden" value="' + payInfo.itemCode + '" name="itemCode" id="itemCode" />' + '<input type="number" class="input-text" style="padding: 0px 8px; width:90%" value="' + payInfo.refundAmount + '" name="'+payInfo.itemCode+'" id="refundAmount" min="0" />' + '</td>';
			}
            dom += '</tr>';
        }
        dom += '</tbody>';
        $("#feeItem").append(dom);
    }