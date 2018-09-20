$(function() {
            if("SHOW" == $("#exType").val()){
                $("#updateSubmit").hide();
            }

            var bcruptime = 'studentModifyInfo.bcruptime';

            if(null == bcruptime || bcruptime == ''){
                $("#bcruptime").val('');
            }else{
                $("#bcruptime").val(bcruptime);
            }

            //学员阶段
            $("#stdStage").text(_findDict("stdStage",$("#stdStage").text()));

            //性别
            $("#sexv").val(_findDict("sex",$("#sexv").val()));
            $("#newSex").text(_findDict("sex",$("#newSex").text()));

            $("#newNation").text(_findDict("nation",$("#newNation").text()));
            $("#nationv").val(_findDict("nation",$("#nationv").val()));

            $("#pfsnLevelv").val(_findDict("pfsnLevel",$("#pfsnLevelv").val()));
            $("#pfsnLevel").text(_findDict("pfsnLevel",$("#pfsnLevel").text()));

            $("#newScholarship").text(_findDict("scholarship",$("#newScholarship").text()));

            $("#grade").val(_findDict("grade",$("#grade").val()));

            //优惠类型
            $("#scholarshipv").val(_findDict("scholarship",$("#scholarshipv").val()));
            
            $("#grade").val(_findDict("grade",$("#grade").val()));
          //考试费
            var examPayStatus = $("#examPayStatus").text();
            if(examPayStatus=="0"){
            	$("#examPayStatus").text("未缴费");
            }else if(examPayStatus=="1"){
            	$("#examPayStatus").text("已缴费");
            }
            $("#graduateEdcsType").val(_findDict("edcsType", $("#graduateEdcsType").val()));
            $("#newGraduateEdcsType").text(_findDict("edcsType", $("#newGraduateEdcsType").text()));
      });