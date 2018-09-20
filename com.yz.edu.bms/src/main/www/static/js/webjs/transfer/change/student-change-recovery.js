var varStdId,varGrade,varlearnId,varoldlearnId;
$(function () {
    // findChangeGradeDict();
    //选择学员下拉框加载
    _simple_ajax_select({
        selectId: "slearnId",
        searchUrl: '/studentChange/findStudentInfo.do',
        sData: {},
        showText: function (item) {
            return item.stdName + " - " + item.grade + "级";
        },
        showId: function (item) {
            return item.learnId;
        },
        placeholder: '--学员--'
    });
        $("#slearnId").on('change', function () {
        studentFind();
    });
    _simple_ajax_select({
        selectId: "oldlearnId",
        searchUrl: '/studentChange/findLearnByStdId.do',
        sData: {
        	stdId: function () {
                return varStdId;
            },
            grade: function () {
                return varGrade;
            }
        },
        showText: function (item) {
           return item.stdName + " - " + item.grade + "级";
        },
        showId: function (item) {
            return item.learnId;
        },
        placeholder: '--学籍--'
    });

    $("#form-member-add").validate({
        messages: {},
        onkeyup: false,
        focusCleanup: true,
        success: "valid",
        submitHandler: function (form) {
        	varoldlearnId=$("#oldlearnId").val();
        	if(varoldlearnId==""){
            	layer.msg('请选择要还原的学籍', {icon: 5, time: 1000}, function () {
                });
                return ;
            }
            $(form).ajaxSubmit({
                type: "post", //提交方式
                dataType: "json", //数据类型
                url: '/studentChange/recoveryByLearnId.do', //请求url
                data:{
                	learnId:varlearnId,
                	ldlearnId:varoldlearnId,
                	varStdId:varStdId
                },
                success: function (data) { //提交成功的回调函数
                    if (data.code == _GLOBAL_SUCCESS) {
                        layer.msg('操作成功！', {icon: 1, time: 1000}, function () {
                        });
                result = data.body;
                varStdId=result.stdId;
               $("#newGrade").val(_findDict("grade", result.grade));
                $("#newIdCard").val(result.idCard);
                $("#newScholarship").val(_findDict("scholarship", result.scholarship));
                $("#newInclusionStatus").val(result.inclusionStatus ? _findDict('inclusionStatus', result.inclusionStatus) : "--");
                $("#newPfsnLevel").val(_findDict("pfsnLevel", result.pfsnLevel));
                $("#newUnvsName").val(result.unvsName);
                $("#newPfsnName").val(result.pfsnName);
                $("#newStdStage").val(_findDict("stdStage", result.stdStage));
                $("#newRecruit").val(result.empName);
                $("#newTaName").val(result.taName);
                    }
                }
            })
        }
    });

});

function studentFind(){
	 if (!$("#slearnId").val()) {
        $(".modifyContent input,.modifyContent select").val("");
        return;
    }
    
    $.ajax({
        type: 'POST',
        url: '/studentChange/findTransferByStdId.do',
        data: {
            learnId: $("#slearnId").val()
        },
        dataType: 'json',
        success: function (data) {
            if (data.code == _GLOBAL_SUCCESS) {
                result = data.body;
                varGrade=data.grade;
                varStdId=result.stdId;
                varlearnId=result.learnId;
                $("#oldGrade").val(_findDict("grade", result.grade));
                $("#oldIdCard").val(result.idCard);
                $("#oldScholarship").val(_findDict("scholarship", result.scholarship));
                $("#oldInclusionStatus").val(result.inclusionStatus ? _findDict('inclusionStatus', result.inclusionStatus) : "--");
                $("#oldPfsnLevel").val(_findDict("pfsnLevel", result.pfsnLevel));
                $("#oldUnvsName").val(result.unvsName);
                $("#oldPfsnName").val(result.pfsnName);
                $("#oldStdStage").val(_findDict("stdStage", result.stdStage));
                $("#oldRecruit").val(result.empName);
                $("#oldTaName").val(result.taName);
            }
        }
    });
}
