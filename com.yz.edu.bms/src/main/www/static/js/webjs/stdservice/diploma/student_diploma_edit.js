var my_tab_cache = {
	tab_diplomaCode : false,
	tab_receiveAffirm : false,
	tab_unconfirmReason : false,
	tab_mailinfo : false,
	tab_reset : false
};
$(function() {
	$("#followId").val(dataInfo.followId);
    $("#taskId").val(dataInfo.taskId);
    $("#learnId").val(dataInfo.learnId);
    //tab默认进入页面
    var HuitabIndex=(window.parent.HuitabIndex||0)+"";
    //标签块
    $.Huitab("#tab_demo .tabBar span", "#tab_demo .tabCon", "current", "click", HuitabIndex);
    $(".tabBar").find("span").click(function() {
        var id = $(this).attr('name');
        if ('diplomaCode' == id) {
            loadPage('tab_diplomaCode', '/diploma/toDiplomaCodeSet.do', {
                'followId' : followId
            });
        } else if ('receiveAffirm' == id) {
            loadPage('tab_receiveAffirm', '/diploma/toReceiveAffirmSet.do', {
            	'followId' : followId
            });
        } else if ('unconfirmReason' == id) {
            loadPage('tab_unconfirmReason', '/diploma/toUnconfirmReasonSet.do', {
            	'followId' : followId
            });
        } else if ('mailinfo' == id) {
            loadPage('tab_mailinfo', '/diploma/toMailinfoSet.do', {
            	'followId' : followId
            });
        } else if ('reset' == id) {
            loadPage('tab_reset', '/diploma/toReset.do', {
            	'followId' : followId
            });
        } 
    });

    //点击基本信息选项
    $(".tabBar").find("span[name=diplomaCode]").click();
    
});

function loadPage(id, _url, _data) {
    var _switch = my_tab_cache[id];
    if (_switch && true == _switch) {

    } else {
        var loadurl=_url+"?followId="+_data["followId"];
    	$('#' + id).load(loadurl,function(){
    		my_tab_cache[id] = true;
    	});

    }
}

function getUnvsInfo(row){
	var pfsnName = row.pfsnName,
	unvsName = row.unvsName,
	recruitType = row.recruitType,
	pfsnCode = row.pfsnCode,
	pfsnLevel = row.pfsnLevel,text = '';
	if (recruitType) {
		if(_findDict("recruitType", recruitType).indexOf("成人")!=-1){
			text += "[成教]";
		}else {
			text += "[国开]";
		}
	}
	if(unvsName) text += unvsName+'</br>';
	if(pfsnLevel) {
		if(_findDict("pfsnLevel", pfsnLevel).indexOf("高中")!=-1){
			text += "[专科]";
		}else {
			text += "[本科]";
		}
	}
	if(pfsnName) text += pfsnName;
	if(pfsnCode) text += "(" + pfsnCode + ")";
	return text;
	
}