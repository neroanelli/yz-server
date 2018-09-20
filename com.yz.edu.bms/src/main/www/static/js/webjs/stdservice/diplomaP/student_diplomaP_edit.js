

$(function(){
	
	
	var exType = $("#exType").val();
	var url = '';
	
	if('ADD' == exType){
		url = "/diplomaP/add.do";
	}else if('UPDATE' == exType){
		url = "/diplomaP/update.do";

	}
	var provinceCode = diplomaP.provinceCode;

	if(provinceCode){
        _init_area_select("provinceCode", "cityCode", "districtCode",diplomaP.provinceCode,diplomaP.cityCode,diplomaP.districtCode);
        $("#provinceCode").val(provinceCode);
    }else {
        //初始省、市、区
        _init_area_select("provinceCode", "cityCode", "districtCode","440000");
        $("#districtCode").append("<option value=''>请选择</option>");
	}

    if(null == diplomaP.status){
        diplomaP.status = '1';
	}
    _init_radio_box("statusRadio", "status", dictJson.status, diplomaP.status);
    
	$("#form-charges-add").validate({
		rules : {
            placeName : {
				remote : { //验证发放点是否存在
					type : "POST",
					url : "/diplomaP/validatePlaceName.do", //servlet
					data : {
                        placeName : function() {
							return $("#placeName").val();
						},
                        placeId : function() {
                            return $("#placeId").val();
                        },
						exType : exType
					}
				},
				required : true,
				maxlength : 15
			},
			provinceCode : {
				required : true
			},
			cityCode : {
				required : true
			},
			districtCode : {
				required : true
			},
			address : {
				required : true,
				maxlength : 50	
			},
			status : {
				required : true
			}
		},
		messages : {
            placeName : {
				remote : "发放地址名称已存在！",
                maxlength:'发放地址名称最多可以输入15个字符'
			},
            address:{
                required : "具体地址必填！",
                maxlength:'具体地址最多可以输入50个字符'
			}
		},
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
							layer_close();
						});
					}
				}
			})
		}
	});

});

	

	