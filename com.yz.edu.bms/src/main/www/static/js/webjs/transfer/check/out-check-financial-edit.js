function initItemDom() {
        var dom = '';
        dom += '<tbody>';
        for (var i = 0; i < row.length; i++) {
            var payInfo = row[i];

            dom += '<tr>';
            var itemName = getItemName(payInfo.itemName,$("#grade").val());
            var index=0;
            for(var a=0;a<row.length;a++){
            	if(row[a].subOrderNo==payInfo.subOrderNo && row[a].itemCode!=payInfo.itemCode)
            	{
            		dom += '<td>' + payInfo.itemCode + ':' + itemName + '</br>'
            		+ row[a].itemCode + ':' + row[a].itemName +'</td>';
            		break;
            	}else{
            		index++;
            	}
            }
            if(index==row.length){
            	dom += '<td>' + payInfo.itemCode + ':' + itemName + '</td>';
            }
            dom += '<td>' + payInfo.payable + '</td>';
            dom += '<td>' + payInfo.xjAmount + '</td>';
            dom += '<td>' + payInfo.zmAmount + '</td>';
            dom += '<td>' + payInfo.zlAmount + '</td>';
            dom += '<td>' + payInfo.yhqAmount + '</td>';
            dom += '<td>' + '<input type="hidden" value="' + payInfo.itemCode + '" name="items[' + i + '].itemCode" id="itemCode" />' + '<input type="number" class="input-text radius" value="' + payInfo.refundAmount + '" name="items[' + i + '].refundAmount" id="refundAmount" min="0" max="' + payInfo.payable + '"/>' + '</td>';
            dom += '</tr>';
        }
        dom += '</tbody>';
        $("#feeItem").append(dom);
        //发书记录
		 var fashudom = '';
        fashudom += '<tbody id="fashujilu">';
        for (var i = 0; i < fashu.length; i++) {
            var payInfo = fashu[i];
            fashudom += '<td>' + payInfo.grade + '级</td>';
            fashudom += '<td>' +  _findDict("semester", payInfo.semester) + '</td>';
            var books='';
            for (var a = 0; a < payInfo.books.length; a++) {
                var book = payInfo.books[a];
                books += book.textbookId + " " + book.textbookName + "<br>";
            }
            fashudom += '<td>' + books + '</td>';
            fashudom += '<td>' + (payInfo.batchId ==null?'':payInfo.batchId) + '</td>';
            fashudom += '<td>' + _findDict("logisticsName", payInfo.logisticsName) + '</td>';
            fashudom += '<td>' + (payInfo.logisticsNo ==null?'':payInfo.logisticsNo)+ '</td>';
            fashudom += '<td>' + _findDict("orderBookStatus", payInfo.orderBookStatus)+ '</td>';
            fashudom += '<td>' + (payInfo.remark ==null?'':payInfo.remark)+ '</td>';
            fashudom += '</tr>';
            
        }
        fashudom += '</tbody>';
        $("#jiaocaifafang").append(fashudom);
    }
    /*用户-编辑*/
	function toEidt(learnId, stdId) {
		var url = '/studentBase/toEdit.do' + '?learnId='
				+ learnId + '&stdId=' + stdId;
		layer_show('学员信息', url, null, null, function() {
			myDataTable.fnDraw(false);
		}, true);
	}
    $(function () {
        $("#postTime").val(new Date($("#postTime").val()).format('yyyy-MM-dd hh:mm:ss'));
        //缴费情况
        initItemDom();
       
        var nameDom = '<span><a title="查看学员信息" onclick="toEidt(\''+ learnId +'\',\''+ stdId +'\')"><span class="c-blue">'+ stdName +'</span></a></span>';
        
        $("#stdNameBox").html(nameDom);
        
        $("#scholarship").val(_findDict('scholarship',scholarship));
        $("#inclusionStatus").val(_findDict('inclusionStatus',inclusionStatus));
        changeCheck();
        $('.skin-minimal input').iCheck({
            checkboxClass: 'icheckbox-blue',
            radioClass: 'iradio-blue',
            increaseArea: '20%'
        });

        $("#form-member-add").validate({
            rules: {
                outId: {
                    required: true,
                }
            },
            onkeyup: false,
            focusCleanup: true,
            success: "valid",
            submitHandler: function (form) {
                if ('2' == $('#checkStatus').val()) {
                    $(form).ajaxSubmit({
                        type: "post", //提交方式
                        dataType: "json", //数据类型
                        url: '/studentOutApproval/passFinancialApproval.do', //请求url
                        success: function (data) { //提交成功的回调函数
                            if (data.code == _GLOBAL_SUCCESS) {
                                layer.msg('操作成功！', {icon: 1, time: 1000}, function () {
                                    layer_close();
                                });
                            }
                        }
                    });
                } else {
                    layer.prompt({
                        title: '填写驳回原因：',
                        formType: 2,
                        maxlength:50
                    }, function (text, index) {
                        $('#reason').val(text);
                        $(form).ajaxSubmit({
                            type: "post", //提交方式
                            dataType: "json", //数据类型
                            url: '/studentOutApproval/passFinancialApproval.do', //请求url
                            success: function (data) { //提交成功的回调函数
                                if (data.code == _GLOBAL_SUCCESS) {
                                    layer.msg('操作成功！', {icon: 1, time: 1000}, function () {
                                        layer_close();
                                    });
                                }
                            }
                        });
                    });
                }
            }
        });
        $.each($("#feeItem").find("input[type='number']"), function (i, data) {
            $(data).rules('add', {required: true, range: [0, 100000], isStdFee: true});
        });
        initCheckProgress();
    });
    function changeCheck() {
        $.post('/studentOutApproval/findStudentOutInfo.do', {
            learnId: $("#learnId").val()
        }, function (result) {
            result = result.body;
            $("#stdStage").val(_findDict("stdStage", result.stdStage));
            $("#stdStageh").val(result.stdStage);
            $("#unvsId").val(
                "[" + _findDict("recruitType", result.recruitType) + "]" + result.unvsName + ":" + result.pfsnName + "[" + _findDict("pfsnLevel", result.pfsnLevel) + "]");
            $("#onUnvsId").val(
                "[" + _findDict("recruitType", result.bsarecruitType) + "]" + result.bsaunvsName + ":" + result.bsapfsnName + "[" + _findDict("pfsnLevel", result.bsapfsnLevel)
                + "]");
           /* $("#unvsId").val(
                "[" + _findDict("recruitType", result.bsarecruitType) + "]" + result.bsaunvsName + ":" + result.bsapfsnName + "[" + _findDict("pfsnLevel", result.bsapfsnLevel)
                + "]");*/
             $("#tutor").val(result.oerName);
            $("#recruit").val(result.oetName);
            $("#learnIdd").val(result.learnId);
        }, "json");
    }
    
 // 进度条
    function initCheckProgress() {
        var isCheck = true;
        var temp = row;
        row = check;
        var step = 2;
        var dom ='<div id="stepBar" class="ui-stepBar-wrap">'
            +'<div class="ui-stepBar">'
            +'<div class="ui-stepProcess"></div>'
            +'</div>'
            +'<div class="ui-stepInfo-wrap">'
            +'<table class="ui-stepLayout" border="0" cellpadding="0" cellspacing="0">'
            +'<tr >';

            dom+='<td class="ui-stepInfo"  style="border: 0px">';

        dom+='<a class="ui-stepSequence"><i class="iconfont icon-queren"></i></a>'
            +'<p class="ui-stepName">提交：'+postUser+'</p>'
            +'<p class="ui-stepName">提交时间：'+ new Date(postTime).format('yyyy-MM-dd hh:mm:ss') +'</p>'
            +'<p class="ui-stepName">备注：'+remark+'</p>'
            +'</td>';
        for(var i =0;i<row.length;i++){
            var empName;
            var updateTime;
            if(row[i].empName == null){
                empName ="-";
            }else{
                empName = row[i].empName;
            }

            if(row[i].updateTime == null){
                updateTime ="-";
            }else{
                updateTime = new Date(row[i].updateTime).format('yyyy-MM-dd hh:mm:ss') ;
            }

            if(row[i].checkStatus==3){
                isCheck=false;
            }else if(row[i].checkStatus==2){
                step++;
            }

            dom += '<td class="ui-stepInfo" style="border: 0px; " >';

            if(row[i].checkStatus == '3'){
                dom += '<a class="ui-stepSequence" title=""><i class="iconfont icon-butongguo"></i></a>';
            }else{
                dom += '<a class="ui-stepSequence""><i class="iconfont icon-queren"></i></a>';
            }
            dom += '<p class="ui-stepName">'+_findDict("jtId",row[i].jtId)+"："+empName+'</p>';
            if(i=='0'){
                dom += '<p class="ui-stepName">初审时间：'+updateTime+'</p>';
            }else {
                dom += '<p class="ui-stepName">复审时间：'+updateTime+'</p>';
            }

            if(row[i].checkStatus == '3'){
                dom += '<p class="ui-stepName">驳回原因：'+row[i].reason+'</p>';
            }

            if(i=='0'){
                if(!!financialRemark)dom += '<p class="ui-stepName">备注：'+financialRemark+'</p>';
            }else {
            	if(!!schoolManagedRemark)dom += '<p class="ui-stepName">备注：'+schoolManagedRemark+'</p>';
            }


            dom += '</td>';
        }

            dom +='<td class="ui-stepInfo" style="border: 0px">';

        dom +='<a class="ui-stepSequence"><i class="iconfont icon-queren"></i></a>'
            +'<p class="ui-stepName">完成</p>'
            +'</td>'
            +'</tr>'
            +'</table>'
            +'</div>'
            +'</div>';
        $("#checkStatusProgress").html(dom);

        var barWidth= $("#stepBar").width();

        var progress = new Progress({
            bar : {},
            item : {},
            barWidth : 0,
            itemCount : 2,
            itemWidth : 0,
            processWidth : 0,
            curProcessWidth : 0,
            step : 1,
            curStep : 0,
            triggerStep : 1,
            change : false,
            animation : false,
            speed : 500,
            stepEasingForward : "easeOutCubic",
            stepEasingBackward : "easeOutElastic",
            width : 504},'stepBar',{
            step : step,
            animation : true,
            change : true,
            isCheck : isCheck,
            width : barWidth
        });
        progress.init();
    }