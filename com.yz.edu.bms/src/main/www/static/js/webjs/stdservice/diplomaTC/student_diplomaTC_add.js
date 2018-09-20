$(function() {

    //初始化下拉框
    $.ajax({
        type: "POST",
        dataType : "json", //数据类型
        url: '/taskprovide/getTaskName.do',
        success: function(data){
            var diplomaIdJson = data.body;
            if(data.code=='00'){
                _init_select("diplomaId",diplomaIdJson);
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
                _init_select("placeId",placeIdJson);
            }
        }
    });

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
                }
            },
            messages : {
                diplomaId : {
                    required : "发放任务必选",
                },
                placeId:{
                    required : "省必选",
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
                    if (!$(this).find('#number' + index + '').val()) {
                        validate=false;
                        layer.msg('请输入容量！', {icon: 5, time: 1000});
                        return;
                    }else {
                        var re = /^(0|[1-9][0-9]{0,2})$/;
                        if (!re.test($(this).find('#number' + index + '').val())) {
                            layer.msg('容量请输入3位以内的正整数!', {
                                icon: 2,
                                time: 1000
                            });
                            validate=false;
                            return;
                        }
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
                    url : '/diplomaTC/add.do', //请求url
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
    });
    
    function addPlace() {
        var len=$('#TCConfig > .item').length;
        var html='<div class="item" style="margin-top: 10px">\n' +
            '                        <input type="text" onfocus="WdatePicker({ dateFmt:\'yyyy-MM-dd\'})" id="date'+len+'" name="StuDiplomaConfigs['+len+'].date" class="input-text Wdate" style="width: 150px;"  placeholder="日期" />\n' +
            '                        &nbsp; <input type="text" placeholder="开始时间"  onfocus="WdatePicker({ dateFmt:\'HH:mm\',minDate:\'#F{$dp.$D(\\\'date'+len+'\\\',{d:-1})}\'})" id="startTime'+len+'" name="StuDiplomaConfigs['+len+'].startTime" class="input-text Wdate" style="width: 100px;" /> -\n' +
            '                        <input type="text" placeholder="结束时间"  onfocus="WdatePicker({ dateFmt:\'HH:mm\', minDate:\'#F{$dp.$D(\\\'startTime'+len+'\\\');}\'})" id="endTime'+len+'" name="StuDiplomaConfigs['+len+'].endTime" class="input-text Wdate" style="width: 100px;" />\n' +
            '                        <input type="number" class="input-text" maxlength="3" id="number'+len+'" name="StuDiplomaConfigs['+len+'].number" style="width: 80px;" placeholder="考场容量" min="1">\n' +
            '                    </div>'
        html=$(html);
        $('#TCConfig').append(html);
    }
    function delPlace() {
        var len=$('#TCConfig > .item').length;
        if(len===1) return;
        $('#TCConfig > .item:last-child').remove();
    }

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
        dels.push($(this).data("id"))
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