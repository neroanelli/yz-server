  var isMine = false;
  $(function() {

      //初始化户口所在地下拉框
      _init_select("rprAddressCode", dictJson.rprAddressCode, varBaseInfo.rprAddressCode);

      if('1' == isSuppose){
          $("#idBox").html('<input type="text" class="input-text" name="idCard" value='+varBaseInfo.idCard+' />');

          var sexDom = '', sex = varBaseInfo.sex;
          sexDom += '<select class="select" id="sexC" name="sex"></select>';
          $("#sexBox").html(sexDom);
          _init_select('sexC',dictJson.sex,sex);
          $("#stdNameBox").html('<input type="text" class="input-text" name="stdName" value='+varBaseInfo.stdName+' />');
          var mobileDom = '';
          mobileDom += '<input type="text" class="input-text" placeholder="请输入手机" value='+ varBaseInfo.mobile +' id="mobile" name="mobile" />';
          mobileDom += '<input type="hidden" id="s_mobile" name="s_mobile"/>';
          $("#mobileBox").html(mobileDom);

      }
      //        图片上传
      $('#selectPhoto').on('change', function() {
          var file = this.files[0];
          var type = file.type;
          var box = $('#member-photo');
          var fr = new FileReader();
          fr.readAsDataURL(file);
          $(fr).on('load', function() {
              var result = fr.result;
              if (type.indexOf('image/') > -1) {
                  createPhoto(result);
                  $("#isPhotoChange").val('1');
              } else {
                  alert('请选择正确的图片文件格式！');
              }
          });
      });

          
      var headPortrait = varOther.headPortrait;
      if (headPortrait) createPhoto(_FILE_URL + headPortrait + "?" + Date.parse(new Date()));

      $("#stdName").text(varBaseInfo.stdName);     
      $("#idType").text(_findDict('idType', varBaseInfo.idType));
      $("#idCard").text(varBaseInfo.s_idCard);
      $("#sex").text(_findDict('sex', varBaseInfo.sex));
      $("#birthday").text(varBaseInfo.birthday);
      $("#address").val(varBaseInfo.address);
      $("#zipCode").val(varBaseInfo.zipCode);
      $("#mobile").val(varBaseInfo.mobile);
      $("#s_mobile").val(varBaseInfo.s_mobile);

      isMine = $("#s_mobile").val() == $("#mobile").val();
      if(!isMine)  $("#s_mobile").prop('disabled', 'disabled');

      $("#emergencyContact").val(varBaseInfo.emergencyContact);

      $("#telephone").val(varOther.telephone);
      $("#email").val(varOther.email);
      $("#qq").val(varOther.qq);
      $("#wechat").val(varOther.wechat);
      $("#jobTitle").val(varOther.jobTitle);
      $("#workPlace").val(varOther.workPlace);
      $("#remark").val(varOther.remark);
      $("#wpAddress").val(varBaseInfo.wpAddress);
      $("#wpTelephone").val(varBaseInfo.wpTelephone);
      $("#wpTime").val(varBaseInfo.wpTime);
      if (recruitType === '2') {
          // _init_select({"selectId":"nation","ext1":recruitType}, dictJson.nation,varBaseInfo.nation);
          _init_select("nation", dictJson.nation, varBaseInfo.nation);
          _init_select({"selectId":"politicalStatus","ext1":recruitType}, dictJson.politicalStatus,varBaseInfo.politicalStatus);
          var varjobStatus=varBaseInfo.jobStatus;
          if(!varjobStatus){
          	varjobStatus="-请选择-"
          }
          _init_select("jobStatus", dictJson.jobStatus,varjobStatus);
          _init_select({"selectId":"maritalStatus","ext1":recruitType}, dictJson.maritalStatus,varOther.maritalStatus);
      } else {
          _init_select("nation", dictJson.nation, varBaseInfo.nation);
          _init_select("politicalStatus", dictJson.politicalStatus,varBaseInfo.politicalStatus);
          _init_select("jobType", dictJson.jobType,varBaseInfo.jobType);
          _init_select("maritalStatus", dictJson.maritalStatus,varOther.maritalStatus);
      }
      _init_select("rprType", dictJson.rprType, varBaseInfo.rprType);
      //根据地区获取邮编
      var rprZipCode = _init_area_select("rprProvinceCode", "rprCityCode", "rprDistrictCode", varBaseInfo.rprProvinceCode,varBaseInfo.rprCityCode,varBaseInfo.rprDistrictCode);
      $("#zipCode").val($.trim(rprZipCode));


      // var nowZipCode = _init_area_select("nowProvinceCode", "nowCityCode", "nowDistrictCode", varBaseInfo.nowProvinceCode,varBaseInfo.nowCityCode,varBaseInfo.nowDistrictCode,'1');
      var wpZipCode = _init_area_select("wpProvinceCode", "wpCityCode", "wpDistrictCode", varBaseInfo.wpProvinceCode,varBaseInfo.wpCityCode,varBaseInfo.wpDistrictCode);

      $("#rprProvinceCode").change(_zipCodeChange);
      $("#rprCityCode").change(_zipCodeChange);
      $("#rprDistrictCode").change(_zipCodeChange);

      $("#form-base-info").validate({
          rules : {
              address : {maxlength : 125,isAddress:true},
              mobile : { required : true, isMobile : true},
              emergencyContact : {isTel : true},
              telephone : {isTel : true},
              email : {email : true},
              qq : {isQq : true},
              remark : {maxlength : 100},
              zipCode:{maxlength:6},
              rprAddressCode:{ required : true}
          },
          onkeyup : false,
          focusCleanup : true,
          success : "valid",
          submitHandler : function(form) {            	          	
             if($("#nowProvinceCode").val()&&$("#nowProvinceCode").val()!='19'){
                 layer.msg('教材地址必须为广东省内地址！',{icon:2})
                 return
             }
             $("#nowProvinceName").val($('#nowProvinceCode > option:selected').text());
 			 $("#nowCityName").val($('#nowCityCode > option:selected').text());
 			 $("#nowDistrictName").val($('#nowDistrictCode > option:selected').text());
 			 $("#nowStreetName").val($('#nowStreetCode > option:selected').text() =='请选择'?'':$('#nowStreetCode > option:selected').text());
 			   //验证教材地址
 	        if($("#address").val()){
 	        	if(!$("#nowCityCode").val() || !$("#nowDistrictCode").val()){
 	        		layer.msg('教材地址必须选择省市区', {icon: 2, time: 2000});
 	        		return false; 
 	        	}
 	        }
             var url = '/' + updateUrlBase;     
              $(form).ajaxSubmit({
                  type : "post", //提交方式
                  dataType : "json", //数据类型
                  url : url, //请求url
                  success : function(data) { //提交成功的回调函数
                      if ('00' == data.code) {
                          layer.msg('学员基本信息保存成功', {
                              icon : 1,
                              time : 1000
                          },function(){
                              window.parent.myDataTable.fnDraw(false);
                              layer_close();
                          });
                      }
                  }
              });
          }
      });
      
//      身份证校验
	  if(varBaseInfo.idType=='1'){
		  $("#idBox input").rules("add", {required: true, isIdCardNo: true});
	  }else{
		  $("#idBox input").rules("add", {required: true, rangelength:[8,12]});
	  }    
      
    //京东省
      _initJdProvince();

  	$("#nowCityCode").select2({
  		placeholder : "--请选择市--",
  		allowClear : true
  	});
  	$("#nowDistrictCode").select2({
  		placeholder : "--请选择区--",
  		allowClear : true
  	});
  	$("#nowStreetCode").select2({
  		placeholder : "请选择",
  		allowClear : true
  	});
  	//省联动
  	$("#nowProvinceCode").change(function() {
  		var pId = $(this).val();
  		initJdAddress(pId,"/purchasing/getJDCity.do","nowCityCode");
  	});
  	//市联动
  	$("#nowCityCode").change(function() {
  		var pId = $(this).val();
  		initJdAddress(pId,"/purchasing/getJDCounty.do","nowDistrictCode");
  	});
  	//区联动
  	$("#nowDistrictCode").change(function() {
  		var pId = $(this).val();
  		initJdAddress(pId,"/purchasing/getJDTown.do","nowStreetCode");
  	});




  	if(!!varBaseInfo.nowProvinceCode){
  	    if(varBaseInfo.nowProvinceCode=='19'){
            // $("#nowProvinceCode").append(new Option('广东','19',true,false)).change();
            _init_select('nowProvinceCode',[{'dictValue':'19','dictName':'广东'}],'19');
            // $("#nowProvinceCode").append("<option value='19' selected='selected' index='0'>广东</option>");
        }else {
            // $("#nowProvinceCode").append(new Option('广东', '19', false, true));
            // $("#nowProvinceCode").append("<option value='19'>广东</option>");
            // $("#nowProvinceCode").append("<option value='19' index='0'>广东</option>");
            // $("#nowProvinceCode").append("<option value='"+varBaseInfo.nowProvinceCode+"' selected='selected' index='0'>"+varBaseInfo.nowProvinceName+"</option>");
            // $("#nowProvinceCode").append(new Option(varBaseInfo.nowProvinceName, varBaseInfo.nowProvinceCode, false, true)).change();
            _init_select('nowProvinceCode',[{'dictValue':'19','dictName':'广东'},{'dictValue':varBaseInfo.nowProvinceCode,'dictName':varBaseInfo.nowProvinceName}],varBaseInfo.nowProvinceCode);
        }
        initJdAddress(varBaseInfo.nowProvinceCode,"/purchasing/getJDCity.do","nowCityCode",varBaseInfo.nowCityCode);
        initJdAddress(varBaseInfo.nowCityCode,"/purchasing/getJDCounty.do","nowDistrictCode",varBaseInfo.nowDistrictCode);
        if(!!varBaseInfo.nowDistrictCode){
            initJdAddress(varBaseInfo.nowDistrictCode,"/purchasing/getJDTown.do","nowStreetCode",varBaseInfo.nowStreetCode);
        }
    }else{
  	    // debugger
        _init_select('nowProvinceCode',[{'dictValue':'19','dictName':'广东'}]);
        // $("#nowProvinceCode").append("<option value='19'>广东</option>");
        // initJdAddress('',"/purchasing/getJDProvince.do","nowProvinceCode",varBaseInfo.nowProvinceCode);
        // initJdAddress(varBaseInfo.nowProvinceCode,"/purchasing/getJDCity.do","nowCityCode",varBaseInfo.nowCityCode);
        // initJdAddress(varBaseInfo.nowCityCode,"/purchasing/getJDCounty.do","nowDistrictCode",varBaseInfo.nowDistrictCode);
        // initJdAddress(varBaseInfo.nowDistrictCode,"/purchasing/getJDTown.do","nowDistrictCode",varBaseInfo.nowDistrictCode);
    }	   	
  });
  
//初始化京东地址
  function initJdAddress(pId,url,selectId,val){
  	$("#"+selectId).empty();
  	$("#"+selectId).append("<option value=''>--请选择--</option>");
  	$.ajax({
          url: url,
          dataType : 'json',
          data : {
          	"pId":pId
          },
          success: function(data){
              if(!!data.body&&data.body.length>0){
                  var dictArray = [];
                  $.each(data.body, function (index, s) {
                      if (s) {
                          dictArray.push({
                              'dictValue': s.code,
                              'dictName': s.name
                          });
                      }
                  });
                  _init_select(selectId,dictArray,val);
              }
          }
     });
  }
  function _initJdProvince(){
      return
  	   _init_select("nowProvinceCode",[{"dictValue": "19", "dictName": "广东"}],"19");

  	   initJdAddress("19","/purchasing/getJDCity.do","nowCityCode");

  	   $("#nowProvinceCode").append(new Option(varBaseInfo.nowProvinceName,varBaseInfo.nowProvinceCode,false,true));
  	   $("#nowCityCode").append(new Option(varBaseInfo.nowCityName,varBaseInfo.nowCityCode,false,true));
  	   $("#nowDistrictCode").append(new Option(varBaseInfo.nowDistrictName,varBaseInfo.nowDistrictCode,false,true));
  	   if(varBaseInfo.nowStreetCode){
           $("#nowStreetCode").append(new Option(varBaseInfo.nowStreetName,varBaseInfo.nowStreetCode,false,true));
       }

  }
  function deletPhoto() {
      layer.confirm('确定删除？', function() {
          $('#member-photo').empty();
          $('#isPhotoChange').val('1');
          layer.msg('已删除', {
              icon : 1
          });
      })
  }

  function createPhoto(imgSrc) {
      var box = $("#member-photo");
      box.empty();
      var img = document.createElement('img');
      $(img).on('load', function() {
          var height = this.height;
          var width = this.width;
          if (height > width) {
              $(img).css('height', '140px')
          } else {
              $(img).css('width', '140px')
          }
      });
      img.src = imgSrc;
      box.html(img);
  }

  var _zipCodeChange = function() {
      $("#zipCode").val($("#rprDistrictCode").children("option:selected").attr("zipCode"));
  }