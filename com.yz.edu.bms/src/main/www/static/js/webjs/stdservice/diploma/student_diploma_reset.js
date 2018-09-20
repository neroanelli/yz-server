//定义变量，防止重复提交
var isRepetition = 0;
$(function(){
	 
	 $("input[name='followId']").val(dataInfo.followId);
     $("input[name='taskId']").val(dataInfo.taskId);
     $("input[name='learnId']").val(dataInfo.learnId);
     $("#diplomaId").val(dataInfo.diplomaId);
     $("#configId").val(dataInfo.configId);
	 $("#reset-stdName").text(dataInfo.stdName);
     $("#reset-grade").text(_findDict('grade', dataInfo.grade));
     $("#reset-schoolRoll").text(dataInfo.schoolRoll||'');
     $("#reset-unvsInfo").html(getUnvsInfo(dataInfo));
     
     
     
     var weekDay = ["星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"];
     var affirmEndTimeStr="",affirmStartTimeStr="";
    
     if(dataInfo.affirmStartTime){
    	 var day = (new Date(dataInfo.affirmStartTime.substring(0,10))).getDay();
    	 affirmStartTimeStr=new Date(dataInfo.affirmStartTime.substring(0,10)).format('yyyy-MM-dd')+" "+weekDay[day];
     }
     if(dataInfo.affirmStartTime){
    	 affirmEndTimeStr=dataInfo.affirmStartTime.substring(11).replace('AM', '上午').replace('PM', '下午')+ "-" + dataInfo.affirmEndTime; 
     }
    
     //console.log(dataInfo.receiveAddress);
     //console.log(affirmStartTimeStr);
     //console.log(affirmEndTimeStr);
     
     $('#placeId,#affirmStartTime,#affirmEndTime').select2({
 		placeholder : "--请选择--",
 		allowClear : true,
 		width : "59%",
 	 });
     
     //地址的下拉初始化
     _simple_ajax_select({
 		selectId : "placeId",
 		searchUrl : '/diploma/getReceivePlaceList.do',
 		sData : {
 			diplomaId:$("#diplomaId").val(),
 			learnId:$("input[name='learnId']").val()
 		},
 		showText : function(item) {
 			return item.place_name;
 		},					
 		showId : function(item) {
 			return item.place_id;
 		},
 		
 		placeholder : '--请选择地址--'
 	 });
     $("#placeId").append(new Option("", "", false, true));
     if(dataInfo.placeId){
    	 $("#placeId").append(new Option(dataInfo.placeName, dataInfo.placeId, false, true)); 
     }
 	 $("#receiveAddress").val(dataInfo.receiveAddress||'');
 	 
    
     if($("#placeId").val()){
    	 initaffirmStartTime();
    	 if(dataInfo.affirmStartTime){
    		 $("#affirmStartTime").append(new Option(affirmStartTimeStr,new Date(dataInfo.affirmStartTime.substring(0,10)).format('yyyy-MM-dd'),false,true));
    	 }
     }
     if($("#affirmStartTime").val()){
    	 initAffirmEndTime();
    	 $("#affirmEndTime").append(new Option(affirmEndTimeStr,dataInfo.configId,false,true));
    	    
     }
   
     $("#placeId").change(function () {
    	 getAddress($("#placeId").val());
    	 initaffirmStartTime();
    	 initAffirmEndTime();
     });
     
     $("#affirmStartTime").change(function () {
    	 initAffirmEndTime();
     });
     
     
     
     var editCodeUrl = '/diploma/receiveInfoSet.do';
	 $("#form-diploma-reset").validate({
		 rules : {
			 placeId:{required : true},
			 affirmStartTime : {required : true},
			 affirmEndTime : {required : true}
         },
		onkeyup : false,
		focusCleanup : true,
		success : "valid",
		submitHandler : function(form) {
		 	if(isRepetition != 0){
                layer.msg('确认信息已提交，请勿重复提交！', {icon : 2, time : 5000});
				return ;
			}
            isRepetition++;
           	$(form).ajaxSubmit({
				type : "post", //提交方式
				dataType : "json", //数据类型
				url : editCodeUrl, //请求url
				success : function(data) { //提交成功的回调函数
					if(data.code == _GLOBAL_SUCCESS){
						layer.msg('保存成功！', {icon : 1, time : 1000},function(){
                       	 window.parent.myDataTable.fnDraw(false);
							 layer_close();
                        });
					}
				}
			})
		}

	});
	 
	 
	 $("#diploma-reset").click(function () {
         var url = '/diploma/resetTask.do';
         layer.confirm("是否确定重置确认信息！",function () {
             $.ajax({
                 type : "post", //提交方式
                 dataType : "json", //数据类型
                 url : url, //请求url
					data:{ 
						followId:$("input[name='followId']").val(),
						learnId:$("input[name='learnId']").val(),
						taskId:$("input[name='taskId']").val()
					},
					success : function(data) { //提交成功的回调函数
                     if(data.code == _GLOBAL_SUCCESS){
                         layer.msg('重置成功！', {icon : 1, time : 1000},function(){
                        	 window.parent.myDataTable.fnDraw(false);
							 layer_close();
                         });
                     }
                 }
             })
         })
     });
	 
	 
	 
	 
	 
});


function initAddress(){
	//地址的下拉初始化
    _simple_ajax_select({
		selectId : "receiveAddress",
		searchUrl : '/diploma/getReceiveAddress.do',
		sData : {
			diplomaId:$("#diplomaId").val(),
			learnId:$("input[name='learnId']").val()
		},
		showText : function(item) {
			return item.address;
		},					
		showId : function(item) {
			return item.address;
		},
		
		placeholder : '--请选择地址--'
	 });
	 $("#receiveAddress").append(new Option("", "", false, true));
}

function initaffirmStartTime(){
	//毕业证领取日期的下拉初始化
    _simple_ajax_select({
		selectId : "affirmStartTime",
		searchUrl : '/diploma/findAffirmDateListByLearnId.do',
		sData : {
			diplomaId:$("#diplomaId").val(),
			learnId:$("input[name='learnId']").val(),
			placeId:$("#placeId").val(),
		},
		showText : function(item) {
			return item.date;
		},					
		showId : function(item) {
			return item.start_time;
		},
		placeholder : '--请选择日期--'
	 });
	 $("#affirmStartTime").append(new Option("", "", false, true));
}

function initAffirmEndTime(){
	//毕业证领取时间段的下拉初始化
    _simple_ajax_select({
		selectId : "affirmEndTime",
		searchUrl : '/diploma/findAffirmTimeListByLearnId.do',
		sData : {
			diplomaId:$("#diplomaId").val(),
			learnId:$("input[name='learnId']").val(),
			placeId:$("#placeId").val(),
			affirmStartTime:$("#affirmStartTime").val()
		},
		showText : function(item) {
			return item.time;
		},					
		showId : function(item) {
			return item.config_id;
		},
		placeholder : '--请选择时间段--'
	 });
	 $("#affirmEndTime").append(new Option("", "", false, true));
}

function getAddress(placeId){
	var urlData="/diploma/getReceiveAddress.do";
	$.ajax({
        type : "post", //提交方式
        dataType : "json", //数据类型
        url : urlData, //请求url
			data:{ 
				placeId:placeId
			},
			success : function(data) { //提交成功的回调函数
            if(data.code == _GLOBAL_SUCCESS){
               $("#receiveAddress").val(data.body.address||'');
            }
        }
    })
}
