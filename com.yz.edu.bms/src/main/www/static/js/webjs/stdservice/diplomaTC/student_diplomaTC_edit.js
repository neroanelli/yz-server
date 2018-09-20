$(function() {

    //初始化下拉框
    $.ajax({
        type: "POST",
        dataType : "json", //数据类型
        url: '/taskprovide/getTaskName.do',
        success: function(data){
            var diplomaIdJson = data.body;
            if(data.code=='00'){
                _init_select("diplomaId",diplomaIdJson,diplomaTC.diplomaId);
            }
        }
    });

    //初始化下拉框
    $.ajax({
        type: "POST",
        dataType : "json", //数据类型
        url: '/diplomaP/getPlaceName.do',
        success: function(data){
            var placeIdJson = data.body;
            if(data.code=='00'){
                _init_select("placeId",placeIdJson,diplomaTC.placeId);
            }
        }
    });

    if(null == diplomaTC.status){
        diplomaTC.status = '1';
    }
    _init_radio_box("statusRadio", "StuDiplomaConfigs[0].status", dictJson.status, diplomaTC.status);

    _init_select("grade",dictJson.grade);
    _init_select("pfsnLevel",dictJson.pfsnLevel,null,"层次不限");

    // $("#pfsnLevel").append(new Option("层次不限", "ALL", true, true));
    _simple_ajax_select({
        selectId : "unvsId",
        searchUrl : '/bdUniversity/findAllKeyValue.do',
        sData : {},
        showText : function(item) {
            return item.unvs_name;
        },
        showId : function(item) {
            return item.unvs_id;
        },
        placeholder : '院校不限'
    });

        $("#form-diplomaTC-edit").validate({
            rules : {
                diplomaId : {
                    required : true,
                },
                placeId : {
                    required : true,
                },
                status : {
                    required : true
                }
            },
            messages : {
                diplomaId : {
                    required : "发放任务必选",
                },
                placeId : {
                    required : true,
                }
            },
            onkeyup : false,
            focusCleanup : true,
            success : "valid",
            submitHandler : function(form) {
                var validate=true;
                $('#TCConfig > .item').each(function (index) {
                    if (!$(this).find('#date' + index + '').val()) {
                        validate=false;
                        layer.msg('请选择日期！', {icon: 5, time: 1000});
                        return;
                    }
                    if (!$(this).find('#startTime' + index + '').val()) {
                        validate=false;
                        layer.msg('请选择开始时间！', {icon: 5, time: 1000});
                        return;
                    }
                    if (!$(this).find('#endTime' + index + '').val()) {
                        validate=false;
                        layer.msg('请选择结束时间！', {icon: 5, time: 1000});
                        return;
                    }

                });
                if ($('#TCUConfig > .rule').length == 0) {
                    layer.msg('请添加仅限院校！', {icon: 5, time: 1000});
                    return;
                }
                if(!validate) return;

                $(form).ajaxSubmit({
                    type : "post", //提交方式
                    dataType : "json", //数据类型
                    url : '/diplomaTC/update.do', //请求url
                    success : function(data) { //提交成功的回调函数
                        if (data.code == _GLOBAL_SUCCESS) {
                            layer.msg('操作成功！', {
                                icon : 1,
                                time : 1000
                            },function () {
                                window.parent.myDataTable.fnDraw(true);
                                layer_close();
                            });
                        }
                    }
                })
            }
        });

        //填充院校限定
        $(diplomaTC.stuDiplomaTCU).each(function (i,o) {
            var dom = '';
            dom += '（' + _findDict('grade', o.grade) + '）';
            if(o.unvsId === 'ALL') {
                dom += '院校不限';
            }else {
                dom += o.unvsName;
            }
            dom += '[';
            if(o.pfsnLevel === 'ALL') {
                dom += '层次不限';
            }else{
                dom += _findDict('pfsnLevel', o.pfsnLevel);
            }
            dom += ']';

            var html = '';
            html += '<div class="rule row" id="relu'+i+'" style="min-height: 31px">';
            html += '<input style="display: none" type="text" class="grade" value="'+o.grade+'">';
            html += '<input style="display: none" type="text" class="unvsId" value="'+o.unvsId+'">';
            html += '<input style="display: none" type="text" class="pfsnLevel" value="'+o.pfsnLevel+'">';
            html += dom;
            html += '<input class="btn radius btn-del" type="button" value="删除" data-uid="'+o.id+'" data-id="'+i+'" style="position:absolute;right:0">';
            html += '</div>'
            $('#TCUConfig').append(html);
        })
    ends = $('#TCUConfig > .rule').length;
    });

    //添加方法
    var ends = 0;//记录添加的配置数
    var dels = new Array();//记录删除的数
    var ins = 0;//记录需要添加的配置值
    function addDTC() {
        var grade = $('#grade').val();
        if (!grade){
            layer.msg('请选择年级！', {icon: 5, time: 1000});
            return;
        }
        var unvsId = $('#unvsId').val();
        var unvsIdt = '';
        if (!unvsId){
            unvsId = 'ALL';
            unvsIdt = '院校不限';
        }else{
            unvsIdt = $('#unvsId option:selected').text();
        }
        var pfsnLevel = $('#pfsnLevel').val();
        var pfsnLevelt = '';
        if (!pfsnLevel){
            pfsnLevel = 'ALL';
            pfsnLevelt = '层次不限';
        }else{
            pfsnLevelt = $('#pfsnLevel option:selected').text()
        }

        //校验数据是否有冲突
        var a = 0;
        var tip = '';
        $('#TCUConfig > .rule').each(function (i,o) {
            a = 0;
            if ($(o).find(".grade").val() == grade){
                a++;
                if($(o).find(".unvsId").val() == 'ALL' || unvsId == 'ALL' || $(o).find(".unvsId").val() == unvsId){
                    a++;
                }
                if($(o).find(".pfsnLevel").val() == 'ALL' || pfsnLevel == 'ALL' || $(o).find(".pfsnLevel").val() == pfsnLevel){
                    a++;
                }
            }
            if(a == 3){
                tip = '该配置跟第' + (i + 1) + '条数据有冲突！';
                return false;
            }
        })
        if (a == 3){
            layer.msg(tip, {icon: 5, time: 1000});
            return;
        }
        //获取当前信息,新增一条数据
        if (dels.length > 0) {//简单优化，减少删除后产生的空数据到后台
            ins = dels.shift();
            dels = dels.slice(0);
        }else {
            ins = ends;
        }
        var html = '';
        html += '<div class="rule row" id="relu'+ins+'" style="min-height: 31px">';
        html += '<input style="display: none" type="text" class="grade" name="stuDiplomaConfigUnvis['+ins+'].grade" value="'+grade+'">';
        html += '<input style="display: none" type="text" class="unvsId" name="stuDiplomaConfigUnvis['+ins+'].unvsId" value="'+unvsId+'">';
        html += '<input style="display: none" type="text" class="pfsnLevel" name="stuDiplomaConfigUnvis['+ins+'].pfsnLevel" value="'+pfsnLevel+'">';
        html += '（' + $('#grade option:selected').text() + '）' + unvsIdt + '[' + pfsnLevelt + ']';
        html += '<input class="btn radius btn-del" type="button" value="删除" data-id="'+ins+'" style="position:absolute;right:0">';
        html += '</div>'
        $('#TCUConfig').append(html);
        ends = $('#TCUConfig > .rule').length;
    }

    //删除某一行
    $('#TCUConfig').on('click','.btn-del',function () {
        dels.push($(this).data("id"));
        var uid = $(this).data("uid");
        if (uid){
            var delIds = $('#delIds').val();
            if (delIds){
                $('#delIds').val(delIds + ',' + uid)
            }else{
                $('#delIds').val(uid)
            }
        }
        $(this).closest('.rule').remove();
    })

    //获取地址
    function getAddress() {
        $.ajax({
            type: "POST",
            data:{placeId : $('#placeId').val()},
            dataType : "json", //数据类型
            url: '/diplomaP/getAddress.do',
            success: function(data){
                var address = data.body;
                if(data.code=='00'){
                    $('#address').html(address);
                }
            }
        });
    }



