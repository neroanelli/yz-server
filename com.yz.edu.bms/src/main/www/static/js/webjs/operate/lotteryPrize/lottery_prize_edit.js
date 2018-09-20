$(function() {
        var action = '';
        var url = '';
        //抽奖活动	
    	url = '/enrollLottery/getLotteryInfoByStatus.do';
    	$.ajax({
             type: "POST",
             dataType : "json", //数据类型  
             url: url+'?status=1',
             success: function(data){
          	     var eyJson = data.body;
          	     if(data.code=='00'){
          	    	_init_select("lotteryId",eyJson,$("#lotteryIds").val()); 	 
          	     }
             }
        });
    	
    	$('#prize_type_1').on('ifChecked', function(event){  
			initCoupon(1);
		}); 
		
		$('#prize_type_2').on('ifChecked', function(event){  
			initCoupon(2);
			//优惠券
	    	url = '/enrollLottery/getCouponInfoByCond.do';
	    	$.ajax({
	             type: "POST",
	             dataType : "json", //数据类型  
	             url: url,
	             success: function(data){
	          	     var eyJson = data.body;
	          	     if(data.code=='00'){
	          	    	_init_select("couponId",eyJson,$("#couponIds").val()); 	 
	          	     }
	             }
	        });
		}); 
		
        if('UPDATE' == type) {
            action = '/lotteryPrize/update.do';
            $("#prizeName").val(prizeInfo.prizeName);
            $("#is_allow_" + prizeInfo.isAllow).attr('checked', 'checked');
            $("#prize_type_" + prizeInfo.prizeType).attr('checked', 'checked');
            $("#is_carousel_" + prizeInfo.isCarousel).attr('checked', 'checked');
            $("#prizeCount").val(prizeInfo.prizeCount);
            $("#begin").val(prizeInfo.begin);
            $("#end").val(prizeInfo.end);
            $("#probability").val(prizeInfo.probability);
            $("#orderNum").val(prizeInfo.orderNum);
            $("#remark").val(prizeInfo.remark);
            initCoupon(prizeInfo.prizeType);
            //优惠券
	    	url = '/enrollLottery/getCouponInfoByCond.do';
	    	$.ajax({
	             type: "POST",
	             dataType : "json", //数据类型  
	             url: url,
	             success: function(data){
	          	     var eyJson = data.body;
	          	     if(data.code=='00'){
	          	    	_init_select("couponId",eyJson,$("#couponIds").val()); 	 
	          	     }
	             }
	        });
        } else if('ADD' == type) {
            action = '/lotteryPrize/add.do';
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

function initCoupon(prizeType){
	if(prizeType == 1){
		$("#couponInput").remove();
	}else if(prizeType == 2){
		var dom = '';
		dom +='<div class="row cl" id="couponInput">';
		dom +='<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>优惠券：</label>';
        dom +='<div class="formControls col-xs-8 col-sm-9">';
        dom +='<select class="select js-data-example-ajax" id="couponId" name="couponId" style="width:30%" required>-优惠券-</select>&nbsp;'; 
        dom +='</div>';
        dom +='</div>';
		$("#couponSelect").append(dom);
	}
};