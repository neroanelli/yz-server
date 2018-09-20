var myDataTable;
$(function() {
	//跟进人
	_simple_ajax_select({
		selectId: "empId",
		searchUrl: '/employ/getEmpList.do',
		sData: {},
		showText: function (item) {
			return item.empName;
		},
		showId: function (item) {
			return item.empId;
		},
		placeholder: '--请选择跟进人--'
	});

	//校监
	_simple_ajax_select({
		selectId: "dpManager",
		searchUrl: '/invite_admin/schoolSuperKeyValue.do',
		sData: {},
		showText: function (item) {
			return item.emp_name;
		},
		showId: function (item) {
			return item.emp_id;
		},
		placeholder: '--请选择校监--'
	});

	_init_select("inviteType", dictJson.inviteType);
	_init_select("sScholarship", dictJson.scholarship);
	_init_select("dScholarship", dictJson.scholarship);
	_init_select("grade", dictJson.grade);
	_init_select('empStatus', dictJson.empStatus);

	//是否有跟进人
	_init_select("hasFollow", [
		{"dictValue" : "0","dictName" : "否"},
		{"dictValue" : "1","dictName" : "是"}
	]);

	//是否已分配
	_init_select("isAssign", [
		{"dictValue" : "0","dictName" : "否"},
		{"dictValue" : "1","dictName" : "是"}
	]);

	//是否已处理
	_init_select("isHandle", [
		{"dictValue" : "0","dictName" : "否"},
		{"dictValue" : "1","dictName" : "是"}
	]);

	//意向类型
	_init_select("intentionType",[
		{"dictValue": "A", "dictName": "A"},
		{"dictValue": "B", "dictName": "B"},
		{"dictValue": "C", "dictName": "C"}
	]);

	myDataTable = $('.table-sort').dataTable({
		"serverSide" : true,
		"dom" : 'rtilp',
		"ajax" : {
			url : "/invite_user/getList.do",
			type : "post",
			data : function(pageData) {
				pageData = $.extend({},{start:pageData.start, length:pageData.length}, $("#searchForm").serializeObject());
				return pageData;
			}
		},
		"pageLength" : 10,
		"pagingType" : "full_numbers",
		"ordering" : false,
		"searching" : false,
		"lengthMenu" : [ 10, 20 ],
		"createdRow" : function(row, data, dataIndex) {
			$(row).addClass('text-c');
		},
		"language" : _my_datatables_language,
		columns : [
			{"mData" : null},
			{"mData" : null},
			{"mData" : null},
			{"mData" : null},
			{"mData" : null},
			{"mData" : null},
			{"mData" : null},
			{"mData" : null},
			{"mData" : null},
			{"mData" : null}
		],
		"columnDefs" : [
			{ "targets" : 0,"render" : function(data, type, row, meta) {
				return '<input type="checkbox" value="'+ row.userId + '" name="userIds"/>';
			}},
			{"targets" : 1,"class":"text-l","render" : function(data, type, row, meta) {
				return user(row);
			}},
			{"targets" : 2,"class":"text-l","render" : function(data, type, row, meta) {
				return account(row.accList);
			}},
			{ "targets" : 3,"class":"text-l","render" : function(data, type, row, meta) {
				return pUser(row);
			}},
			{"targets" : 4,"render" : function(data, type, row, meta) {
				return source(row);
			}},
			{"targets" : 5,"class":"text-l","render" : function(data, type, row, meta) {
				return dest(row);
			}},
			{ "targets" : 6,"class":"text-l","render" : function(data, type, row, meta) {
				return follow(row);
			}},
			{"targets" : 7,"class":"text-c","render" : function(data, type, row, meta) {
				var intentionType = row.intentionType ? row.intentionType : "";
				var dom = intentionType + '<a title="修改" href="javascript:void(0);" onclick="showEditIntentionType(\'' + row.userId + '\', \''+intentionType+'\')" class="ml-5" style="text-decoration: none">';
				dom += '<i class="iconfont icon-edit"></i></a>';
				return dom;
			}},
			{ "targets" : 8,"class":"text-c","render" : function(data, type, row, meta) {
                    var remark = row.remark ? row.remark : "", userId = row.userId ?　row.userId : "", stdId = row.stdId ?　row.stdId : "";
                    if(!!remark){
                        var dom='<a href="javascript:void(0);" class="remarkBox" style="text-decoration: none">'+remark+'<i title="修改备注" onclick="showEditRemark(\'' + userId + '\', \''+stdId+'\')" class="Hui-iconfont Hui-iconfont-shuru remarkIcon"></i></a>';
                    }else {
                        var dom = '<a title="添加备注" href="javascript:void(0);" onclick="showEditRemark(\'' + userId + '\', \''+stdId+'\')" style="text-decoration: none">';
                        dom += '<i class="iconfont icon-shenhe"></i></a>';
                    }
                    return dom;
			}},
			{"targets" : 9,"render" : function(data, type, row, meta) {
				var dom = '';
				dom += '<a style="padding: 2px 5px;margin-bottom: 5px;display: inline-block" class="tableBtn normal" title="分配" href="javascript:void(0);" onclick="showXJ(\''+ 1+'\', \''+row.userId+ '\')" class="ml-5">';
				dom += '分配校监</a><br/>';
				dom += '<a style="padding: 2px 5px;margin-bottom: 5px;display: inline-block" class="tableBtn normal" title="分配" href="javascript:void(0);" onclick="showZSLS(\''+1+'\',\''+row.userId+'\')" class="ml-5" >';
				dom += '分配跟进人</a><br/>';
				dom += '<a style="padding: 2px 5px;margin-bottom: 5px;display: inline-block" class="tableBtn normal" title="分配记录" href="javascript:void(0);" onclick="showLog(\''+ row.userId+'\')" class="ml-5">';
				dom += '分配记录('+row.allotCount+'条)</a>';
				return dom;
			}}
		]
	});
});

function _search() {
	myDataTable.fnDraw(true);
}

function user(data) {
	var nickname = data.nickname ? data.nickname : '';
	var yzCode = data.yzCode ? data.yzCode : '';
	var name = data.name ? data.name : '';
	var mobile = data.mobile ? data.mobile : '';
	var mobileLocation = data.mobileLocation ? data.mobileLocation : '';
	var idCard = data.idCard ? data.idCard : '';
	var dom = '<ul>';
	dom += '<li>昵称：' + nickname + '</li>';
	dom += '<li>远智编号：' + yzCode + '</li>';
	if(data.assignFlag == '1'){
		dom += '<li class="name-mark-box" style="text-align: left;">真实姓名：' + name + '<span class="name-mark mark-blue">分配</span></li>';
	}else{
		dom += '<li>真实姓名：' + name + '</li>';
	}
	dom += '<li>手机号：' + mobile+"&nbsp;"+ mobileLocation + '</li>';
	dom += '<li>身份证号：' + idCard + '</li>';
	dom += '</ul>';
	return dom;
}

function pUser(data) {
	if (data.pId && '' != data.pId) {
		var nickname = data.pNickname ? data.pNickname : '';
		var yzCode = data.pYzCode ? data.pYzCode : '';
		var name = data.pName ? data.pName : '';
		var mobile = data.pMobile ? data.pMobile : '';
		var idCard = data.pIdCard ? data.pIdCard : '';

		var dom = '<ul>';
		dom += '<li>昵称：' + nickname + '</li>';
		dom += '<li>远智编号：' + yzCode + '</li>';
		dom += '<li>真实姓名：' + name + '</li>';
		dom += '<li>手机号：' + mobile + '</li>';
		dom += '<li>身份证号：' + idCard + '</li>';
		dom += '</ul>';

		return dom;
	} else {
		return '无';
	}
}

function source(data) {
	if (data.regTime) {
		var scholarship = _findDict('scholarship', data.sScholarship);
		var regTime = data.regTime ? data.regTime : '无';

		var dom = '<ul>';
		dom += '<li>' + (scholarship ? scholarship : '无') + '</li>';
		dom += '<li>' + regTime + '</li>';
		dom += '</ul>';

		return dom;
	}
	return '未注册邀约人';
}

function dest(data) {
	if (data.enrollTime) {
		var scholarship = _findDict('scholarship', data.dScholarship);
		var enrollTime = data.enrollTime ? data.enrollTime : '无';
		var grade = _findDict('grade', data.grade);
		var pfsnLevel = _findDict('pfsnLevel', data.pfsnLevel);
		var unvsName = data.unvsName ? data.unvsName : '无';
		var pfsnName = data.pfsnName ? data.pfsnName : '无';

		var dom = '<ul>';
		dom += '<li>优惠类型：' + (scholarship ? scholarship : '无')
				+ '</li>';
		dom += '<li>报名时间：' + enrollTime + '</li>';
		dom += '<li>年级：' + (grade ? grade : '无') + '</li>';
		dom += '<li>报读层次：' + (pfsnLevel ? pfsnLevel : '无') + '</li>';
		dom += '<li>报读院校：' + unvsName + '</li>';
		dom += '<li>报读专业：' + pfsnName + '</li>';
		dom += '</ul>';

		return dom;
	}
	return '未报名';
}

function follow(data) {
	if (data.empName) {
		var name = data.empName ? data.empName : '',
			mobile = data.empMobile ? data.empMobile : '',
			empStatus = _findDict('empStatus', data.empStatus),
			dpManagerName = data.dpManagerName ? data.dpManagerName : '';

		var dom = '<ul>';
		dom += '<li>姓名：' + name + '</li>';
		dom += '<li>手机号：' + mobile + '</li>';
		dom += '<li>状态：' + (empStatus ? empStatus : '未知') + '</li>';
		dom += '<li>校监：' + dpManagerName + '</li>';
		dom += '</ul>';

		return dom;
	}
	return '智哥';
}

function account(data) {
	if (data) {
		var dom = '<ul>';
		$.each(data, function (index, acc) {
			var accName = _findDict('accType',acc.accType);
			accName = accName ? accName : '未知账户';
			var amount = acc.accAmount ? acc.accAmount : '0.00';
			if ('2' == acc.accType) {
				dom += '<li><a href=\'javascript:void(0)\' onclick="showAccount(\''+ acc.userId + '\')" >'+accName+"："+amount+'</a></li>';
			} else {
				dom += '<li>'+accName+"："+amount+'</li>';
			}
		});
		dom += '</ul>';
		return dom;
	} else {
		return '无账户信息';
	}
}

function showLog(userId) {
	layer_show('分配日志', '/invite_user/showLog.do' + '?userId='+userId, 1200, 600, function(){
		myDataTable.fnDraw(false);
	}, false);
}

function showEditIntentionType(userId,intentionType){
	var url = '/invite_user/toIntentionType.do'+ '?userId=' + userId+'&intentionType='+intentionType;
	layer_show('修改意向类型', url, 600, 300, function(){
		// myDataTable.fnDraw(false);
	}, false);
}


function showEditRemark(userId,stdId){
	var url = '/invite_user/toEditRemark.do'+ '?userId=' + userId+'&stdId='+stdId;
	layer_show('修改备注', url, 600, 300, function(){
		// myDataTable.fnDraw(false);
	}, false);
}

function showXJ(type, userId) {
	var url = '/fans/toADistributionPageXJ.do'+ '?addType=' + type;
	showPage(type, userId, url, '校监分配');

}

function showZSLS(type, userId) {
	var url = '/fans/toADistributionPage.do' + '?addType=' + type;
	showPage(type, userId, url, '跟进人分配');
}

function showPage(type, userId, url, title) {
	if (type == '1') {
		url += '&userIds=' + userId;
	} else {
		var userIds = '';
		$("input[name='userIds']:checked").each(function(index, data) {
			var lId = $(this).val();

			if (lId && 'undefined' != lId && 'null' != lId) {
				userIds += $(this).val() + ",";
			}
		});
		if (userIds && userIds.length > 0) {
			userIds = userIds.substring(0, userIds.length - 1);
		} else {
			layer.msg('请选择用户', {
				icon : 5,
				time : 1000
			});
			return false;
		}
		url += '&userIds=' + userIds;
	}
	layer_show(title, url, 1200, 810, function() {
		myDataTable.fnDraw(false);
	}, true);
}

function showAccount(userId) {
	layer_show('账户信息', '/invite_user/toAccount.do' + '?userId='
			+ userId, 1200, 600, function() {
		myDataTable.fnDraw(false);
	}, false);
}