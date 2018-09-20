$(function(){
	
	$("#sex").text(_findDict("sex", dataInfo.sex));
	
	$("#username").text(dataInfo.username==null?'暂无':dataInfo.username);
	$("#password").text(dataInfo.password==null?'暂无':dataInfo.password);
	
	$("#nation").text(_findDict("nation", dataInfo.nation));
	$("#politicalStatus").text(_findDict("politicalStatus", dataInfo.politicalStatus));
	$("#pfsnLevel").text( _findDict("pfsnLevel", dataInfo.pfsnLevel));
	// $("#rprAddress").text(concatAddress(dataInfo.rprProvinceCode, dataInfo.rprCityCode, dataInfo.rprDistrictCode));
	$("#rprAddress").text(_findDict("rprAddressCode", dataInfo.rprAddressCode));
	$("#edcsType").text(_findDict("edcsType", dataInfo.edcsType));
	$("#jobType").text(_findDict("jobType", dataInfo.jobType));
	$("#receiveAddress").text(dataInfo.nowProvinceName+dataInfo.nowCityName+dataInfo.nowDistrictName+(dataInfo.nowStreetName.replace('--请选择--','')||'')+dataInfo.address);
});

function concatAddress(provinceCode, cityCode, districtCode, address) {
    var provinceName = _findProvinceName(provinceCode);
    var cityName = _findCityName(provinceCode, cityCode);
    var districtName = _findDistrictName(provinceCode, cityCode, districtCode);
   
    var text =  provinceName ? provinceName : '';
    text += cityName ? cityName : '';
    text += districtName ? districtName : '';
    text += address ? address : '';
    return text;
  }