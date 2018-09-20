$(function () {
    var newCityCode = "";
    //学员
    _simple_ajax_select({
        selectId: "learnId",
        searchUrl: '/confirmModify/findStudentInfo.do',
        sData: {},
        showText: function (item) {
            return item.stdName + " - " + item.grade + "级";
        },
        showId: function (item) {
            return item.learnId;
        },
        placeholder: '--学员--'
    });

    $("#newPfsnLevel").change();
    $("#newPfsnId").change();
    $("#newTaId").change(function () {
        if ($(this).val()) {
            scholarshipSelectChange();
        }
        var sss = $("#newTaId").val();
        if(sss!=""){
            $.ajax({
                url: '/confirmModify/getNewCityCode.do',
                dataType: 'json',
                type: 'post',
                data: {
                    taId: $("#newTaId").val()
                },
                success: function (data) {
                    if (data.code == _GLOBAL_SUCCESS) {
                        newCityCode = data.body.newCityCode;
                    }
                }
            });
        }
    });
    console.log($("#ifCanUpdate").val());
    //年级事件
    $("#learnId").change(function () {
        if ($("#learnId").select2('data')[0].text.indexOf("2018级") != -1) {
            $("#learnId").val('')
            layer.msg('暂时禁止18级学员异动提交!', {icon: 0, time: 2500}, function () {
                $("#learnId").val(null).trigger("change");
                $(".modifyContent input,.modifyContent select").val("");
                $("#stdStage").val("");
                $("#examPayStatus").val("");
            });
            return
        }
        $.post('/confirmModify/findStudentEnrollInfo.do', {
            learnId: $("#learnId").val(),
        }, function (data) {
            data = data.body;
            if (null != data) {
                //首先清楚填写的信息
                $("#newStdName").val(null);
                $("#newIdCard").val(null);
                $("#newGraduateUnvsName").val(null);
                $("#newGraduateTime").val(null);
                $("#newGraduateProfession").val(null);
                $("#newGraduateDiploma").val(null);
                $("#newSex").val(null).trigger("change");
                $("#newNation").val(null).trigger("change");
                $("#newGraduateEdcsType").val(null).trigger("change");
                $("#newHkCode").val(null).trigger("change");
                $("#newRprAddress").val(null).trigger("change");

                $("#newUnvsId").val(null).trigger("change");
                $("#pfsnLevels2").val(null).trigger("change");
                $("#pfsnLevels2").attr("disabled", "disabled");
                $("#newPfsnId").val(null).trigger("change");
                $("#newPfsnId").attr("disabled", "disabled");
                $("#newTaId").val(null).trigger("change");
                $("#newTaId").attr("disabled", "disabled");
                //学员状态
                $("#stdStage").val(_findDict("stdStage", data.stdStage));
                //考试费
                if(data.examPayStatus=="0"){
                	$("#examPayStatus").val("未缴费");
                }else if(data.examPayStatus=="1"){
                	$("#examPayStatus").val("已缴费");
                }
                $("#examPayStatusv").val(data.examPayStatus);
                
                //姓名
                $("#stdName").val(data.stdName);
                //身份证号码
                $("#idCard").val(data.idCard);
                //性别
                $("#sexv").val(_findDict("sex", data.sex));
                $("#sex").val(data.sex);
                //民族
                $("#nationv").val(_findDict("nation", data.nation));
                $("#nation").val(data.nation);
                //志愿
                $("#zyUnvsId").val(data.unvsName + "(" + data.pfsnCode + ")");
                $("#zyPfsnName").val("(" + data.pfsnCode + ")" + data.pfsnName + "[" + _findDict("grade", data.grade) + "]");
                $("#zyPfsnLevel").val(_findDict("pfsnLevel", data.pfsnLevel));
                $("#unvsId").val(data.unvsId);
                $("#pfsnId").val(data.pfsnId);
                //考区
                $("#taIdv").val(data.taName);
                $("#taId").val(data.taId);
                //优惠类型
                $("#scholarshipv").val(_findDict("scholarship", data.scholarship));
                $("#scholarship").val(data.scholarship);
                $("#rprAddress").val(data.rprAddress);
                // var dictArray = [];
                // var oldScholarship = _findDict('scholarship', $("#scholarship").val());
                // dictArray.push({
                //     'dictValue': $("#scholarship").val(),
                //     'dictName': oldScholarship
                // });
                // _init_select("newScholarship", dictArray, $("#scholarship").val());

                $("#stdId").val(data.stdId);
                $("#grade").val(data.grade);
                //原毕业信息
                $("#graduateUnvsName").val(data.graduateUnvsName);
                $("#graduateTime").val(data.graduateTime);
                $("#graduateEdcsType").val(data.graduateEdcsType);
                $("#graduateEdcsTypev").val(_findDict("edcsType", data.graduateEdcsType))
                $("#graduateProfession").val(data.graduateProfession);
                $("#graduateDiploma").val(data.graduateDiploma);
                $("#cityCode").val(data.cityCode);
            }
        }, "json");
    });

    //性别
    _init_select("newSex", dictJson.sex);
    
    //根据地区获取邮编
    
    _simple_ajax_select({
        selectId: "newHkCode",
        searchUrl: '/recruit/findRprAddressCode.do',
        sData: {

        },
        showText: function (item) {
            return item.hk_name;
        },
        showId: function (item) {
            return item.hk_code;
        },
        placeholder: '--请选择户口所在地--'
    });

    //民族
    _init_select("newNation", dictJson.nation);

    //院校
    _simple_ajax_select({
        selectId: "newUnvsId",
        searchUrl: '/baseinfo/sUnvs.do',
        sData: {},
        showText: function (item) {
            return item.unvsName;
        },
        showId: function (item) {
            return item.unvsId;
        },
        placeholder: '--请选择院校--'
    });
    //原学历类型
    _init_select("newGraduateEdcsType", dictJson.edcsType, "", "--请选择原学历类型--");
    //专业层次
    _init_select("pfsnLevels2", dictJson.pfsnLevel, "", "--请选择层次--");

    //初始化专业
    $("#newPfsnId").select2({
        placeholder: "--请选择专业--",
        allowClear: true
    });

    //专业层次事件
    $("#pfsnLevels2").change(function () {
        var pfsnLevels2 = $("#pfsnLevels2").val();
        $("#newPfsnId").val(null).trigger("change");
        $("#newTaId").attr("disabled", "disabled");
        $("#newTaId").val(null).trigger("change");
        $("#newPfsnId").removeAttr("disabled");
        flushPfns();
    });

    //院校事件
    $("#newUnvsId").change(function () {
        var newUnvsId = $("#newUnvsId").val();
        $("#pfsnLevels2").val(null).trigger("change");
        $("#newPfsnId").val(null).trigger("change");
        $("#newPfsnId").attr("disabled", "disabled");
        $("#newTaId").val(null).trigger("change");
        $("#newTaId").attr("disabled", "disabled");
        $("#pfsnLevels2").removeAttr("disabled");
    });

    //专业事件
    $("#newPfsnId").change(function () {
        var newPfsnId = $("#newPfsnId").val();
        $("#newTaId").val(null).trigger("change");
        $("#newTaId").removeAttr("disabled");

        $("#newScholarship").val(null).trigger("change");
        $("#newScholarship").removeAttr("disabled");
        
        //考区
        _simple_ajax_select({
            selectId: "newTaId",
            searchUrl: '/testArea/findBdTestAreaNotStop.do',
            sData: {
                pfsnId: $("#newPfsnId").val()
            },
            showText: function (item) {
                return item.taName;
            },
            showId: function (item) {
                return item.taId;
            },
            placeholder: '--请选择考区--'
        });
    });

    //_init_select("newScholarship", dictJson.scholarship, "", "---请选择优惠类型---");
    _init_select("newTaId", "", "", "---请选择考区---");
    _init_select("newScholarship", null, null);

    $("#form-member-add").validate({
        onkeyup: false,
        focusCleanup: true,
        rules: {
            stdId: {
                required: true
            },
            learnId: {
                required: true
            },
            newIdCard: {
                remote : { //验证身份证是否合格
                    type : "POST",
                    url : '/confirmModify/ifExistInfo.do', //servlet
                    data : {
                        idCard : function() {
                            return $("#newIdCard").val();
                        }
                    }
                },
                required : true
            }
        },
        messages : {
            newIdCard : {
                remote : "身份证格式错误！"
            }
        },
        success: true,
        submitHandler: function (form) {
            if (!checkEnroll()) {
                return false;
            }
            debugger
            //获取修改后的省市区
            var newHkCode =  $("#newHkCode").val();
            var newHkName = $("#select2-newHkCode-container").attr("title");
            if(newHkCode!=""){
                $("#newRprAddress").val(newHkName);
            }

          //优惠类型只能改筑梦25
        	var scholarship = $("#scholarship").val();
        	var newScholarship = $("#newScholarship").val();
        	if(newScholarship!="" && scholarship=="25" && newScholarship!="25"){
        		layer.msg("筑梦计划不能改为其他优惠类型！");
                return;
        	}else if(newScholarship!="" && newScholarship!=scholarship && newScholarship!="25"){
        		layer.msg("其他优惠类型只能改为筑梦和当前优惠类型！");
                return;
        	}
            //缴费状态
            var examPayStatus = $("#examPayStatusv").val();
            //如果缴费状态为已缴费，并且修改内容为考试县区或毕业信息则给出提示
            if(examPayStatus=='1' && updateGraduate()){
            	layer.confirm('学员已缴费，修改此内容需要重新缴纳111元考试费，请再次确认是否提交申请？', function(index) {
            		$(form).ajaxSubmit({
                        type: "post", //提交方式
                        dataType: "json", //数据类型
                        url: '/confirmModify/insertConfirmModify.do', //请求url
                        success: function (data) { //提交成功的回调函数
                            if (data.code == _GLOBAL_SUCCESS) {
                                layer.msg('操作成功！', {icon: 1, time: 1000}, function () {
                                    window.parent.myDataTable.fnDraw(true);
                                    layer_close();
                                });
                            }
                        }
                    });
            	})
            }else{
            	$(form).ajaxSubmit({
                    type: "post", //提交方式
                    dataType: "json", //数据类型
                    url: '/confirmModify/insertConfirmModify.do', //请求url
                    success: function (data) { //提交成功的回调函数
                        if (data.code == _GLOBAL_SUCCESS) {
                            layer.msg('操作成功！', {icon: 1, time: 1000}, function () {
                                window.parent.myDataTable.fnDraw(true);
                                layer_close();
                            });
                        }
                    }
                });
            }
        }
    });
    
    //是否修改毕业信息
    function updateGraduate(){
    	var bool = false;
    	var graduateEdcsType = $("#graduateEdcsType").val();
        var newGraduateEdcsType = $("#newGraduateEdcsType").val();
        if(newGraduateEdcsType!="" && newGraduateEdcsType!=graduateEdcsType){
        	bool = true;
        }
        var graduateUnvsName = $("#graduateUnvsName").val();
        var newGraduateUnvsName = $("#newGraduateUnvsName").val();
        if(newGraduateUnvsName!="" && newGraduateUnvsName!=graduateUnvsName){
        	bool = true;
        }
        var graduateTime = $("#graduateTime").val();
        var newGraduateTime = $("#newGraduateTime").val();
        if(newGraduateTime!="" && newGraduateTime!=graduateTime){
        	bool = true;
        }
        var graduateProfession = $("#graduateProfession").val();
        var newGraduateProfession = $("#newGraduateProfession").val();
        if(newGraduateProfession!="" && newGraduateProfession!=graduateProfession){
        	bool = true;
        }
        var graduateDiploma = $("#graduateDiploma").val();
        var newGraduateDiploma = $("#newGraduateDiploma").val();
        if(newGraduateDiploma!="" && newGraduateDiploma!=graduateDiploma){
        	bool = true;
        }
        //考试区县
        var cityCode = $("#cityCode").val();
        if(newCityCode!="" && cityCode!=newCityCode){
        	bool = true;
        }
        return bool;
    }


    $('#modify').on('click', '.btn-modify', function () {
        // console.log($("#learnId").val())
        if (!$("#learnId").val() || $("#learnId").val() == 'null') {
            layer.msg("请选择学员");
            return;
        }
        var calssName = $(this).data('classname');
        if (calssName) {
            $('.' + calssName).css('display', 'inline');
        }
        $(this).hide().siblings('div').css('display', 'inline');
    })
});

function checkEnroll() {
    var newUnvsId = $("#newUnvsId").val();
    var pfsnLevels2 = $("#pfsnLevels2").val();
    var newPfsnId = $("#newPfsnId").val();
    var newTaId = $("#newTaId").val();
    var newScholarship = $("#newScholarship").val();
    if (newUnvsId || pfsnLevels2 || newPfsnId || newTaId) {
        return checkEmpty(newUnvsId, pfsnLevels2, newPfsnId, newTaId, newScholarship);
    } else {
        return true;
    }

}

function checkEmpty(newUnvsId, pfsnLevels2, newPfsnId, newTaId, newScholarship) {
    if (!newUnvsId) {
        layer.msg('院校不能为空！', {
            icon: 2,
            time: 1000
        });
        return false;
    }
    if (!pfsnLevels2) {
        layer.msg('专业层次不能为空！', {
            icon: 2,
            time: 1000
        });
        return false;
    }
    if (!newPfsnId) {
        layer.msg('专业不能为空！', {
            icon: 2,
            time: 1000
        });
        return false;
    }
    if (!newTaId) {
        layer.msg('考区不能为空！', {
            icon: 2,
            time: 1000
        });
        return false;
    }
    if (!newScholarship) {
        layer.msg('优惠类型不能为空！', {
            icon: 2,
            time: 1000
        });
        return false;
    }
    return true;
}

function flushPfns() {
    //专业
    _simple_ajax_select({
        selectId: "newPfsnId",
        searchUrl: '/unvsProfession/searchProfessionJson.do',
        sData: {
            unvsId: $("#newUnvsId").val(),
            pfsnLevel: $("#pfsnLevels2").val(),
            grade: $("#grade").val()
        },
        showText: function (item) {
            return item.text;
        },
        showId: function (item) {
            return item.id;
        },
        placeholder: '--请选择专业--'
    });
}

function scholarshipSelectChange() {
    var oldScholarship= $("#scholarship").val();
    // 是否有修改优惠类型权限
    var ifCanUpdate=$("#ifCanUpdate").val();

    // 如果有权限
    if(ifCanUpdate=='true'){
        _init_select("newScholarship", dictJson.scholarship, oldScholarship);
    }else {
        var name = _findDict('scholarship', oldScholarship);
        _init_select("newScholarship", [{'dictValue': oldScholarship,'dictName': name}], oldScholarship);
        $("#newScholarship").attr('disabled','true');
    }
    return
    $.ajax({
        url: '/recruit/getScholarships.do',
        dataType: 'json',
        type: 'post',
        data: {
            pfsnId: $("#newPfsnId").val(),
            taId: $("#newTaId").val()
        },
        success: function (data) {
            if (data.code == _GLOBAL_SUCCESS) {
                var dictArray = [];
                var oldScholarship = $("#scholarship").val();

                if (data.body) {
                    $.each(data.body, function (index, s) {
                        var name = _findDict('scholarship', s);
                        // 有修改优惠类型权限的加载全部，没有则只加载存在的老优惠类型
                        if(ifCanUpdate=='true'){
                            dictArray.push({
                                'dictValue': s,
                                'dictName': name
                            });
                        }else {
                            if(name==oldScholarship){
                                dictArray.push({
                                    'dictValue': s,
                                    'dictName': name
                                });
                            }
                        }
                    });
                }
                var sv;
                if (dictArray.length > 0) {
                    sv = dictArray[0].dictValue;
                }
                _init_select("newScholarship", dictArray, sv);
            }
        }
    });
}