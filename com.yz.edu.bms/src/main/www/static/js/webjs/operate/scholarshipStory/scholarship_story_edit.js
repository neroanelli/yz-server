var isTip = true;  //是否提示
var j = '';
var $li1 = '';
$(function() {
	
	var ueditorPreContent = UE.getEditor('textPre2');
	var ueditorLastContent = UE.getEditor('textLast2');
				if($("#tjType").val() == "UPDATE"){
			    	$("#articleLinkDes2").text(scholarshipStory.articleLinkDes);
			    }
				if($("input[name=tjType]").val()=='UPDATE'&&isEmpty($("input[name=articlePicUrl]").val())){
					  var  lj = $("input[name=articlePicUrl]").val();
					  var $list1 = $('#fileList');
			          $li1 = $('<div data-path="" class="file-item thumbnail"><a href="javascript:;" class="cancel">&times;</a></div>');
			          $list1.append($li1);
			          $li1.append('<img src="' + _FILE_URL + lj + '"/>');
			          $li1.on('click', '.cancel', function () {// 删除图片
			        	  $("input[name=articlePicUrl]").val('');
			              $li1.remove();
			             
			          });
				}
				
				
				var validator = $("#form-scholarshipStory-add").submit(function() {
					  $("input[name=textPre3]").val(ueditorPreContent.getContent());
					  $("input[name=textLast3]").val(ueditorLastContent.getContent());
					 
	            }).validate({
	            	ignore: [],
	            	rules : {
						articleTitle : {
							required : true,
							maxlength : 30
						},
						textPre3 : {
							required : true
						},
						textLast3 : {
							required : true
						},
						articlePicUrl : {
							required : true
						},
						articleLinkDes : {
							required : true
						},
						articleLinkTitle : {
							required : true
						},
						entranceLink : {
							required : true
						},
						entranceText : {
							required : true
						}
					},
					messages: {
						articleTitle : {
							required : '这是必填字段且最大长度为30'
						},
						textPre3 : {
							required : '这是必填字段'
						},
						textLast3 : {
							required : '这是必填字段'
						},
						articlePicUrl : {
							required : '这是必填字段'
						},
						articleLinkDes : {
							required : '这是必填字段'
						},
						articleLinkTitle : {
							required : '这是必填字段'
						},
						entranceLink : {
							required : '这是必填字段'
						},
						entranceText : {
							required : '这是必填字段'
						}
					},
					onkeyup : false,
					focusCleanup : true,
					success : "valid",
					submitHandler : function(form) {
						$(form).ajaxSubmit({
							type : "post", //提交方式  
							dataType : "json", //数据类型  
							url : $("#tjType").val() == "UPDATE" ? '/scholarshipStory/updateScholarshipStory.do' : '/scholarshipStory/insertScholarshipStory.do', //请求url  
							success : function(data) { //提交成功的回调函数  
								layer.msg('保存成功！', {icon : 1, time : 1000},function(){
									layer_close();
								});
								
							},
							error : function(data) {
								
								layer.msg('操作失败！', {icon : 1, time : 1000},function(){
									layer_close();
								});
							}
						})
					}
	                    });
				
				
				ueditorPreContent.addListener("ready", function () {
				        // editor准备好之后才可以使用
				        if(scholarshipStory.textPre !=null){
				        	ueditorPreContent.setContent(scholarshipStory.textPre);	
				        }
				    });
				ueditorLastContent.addListener("ready", function () {
			        // editor准备好之后才可以使用
			        if(scholarshipStory.textLast !=null){
			        	ueditorLastContent.setContent(scholarshipStory.textLast);	
			        }
			    });
				$('input[name=file]').on('change', function() {
					
					
					 isTip = true;
					 j = this;
					 ajaxFile(this,'/file/upload.do');
					 
			     });
				
			});



	function addPhoto() {
		 $('input[name=file]').click();
	}
	 
var $li = '';

function ajaxFile(obj,url) {
	
	var $form = $(obj).closest('form');
    var success = function (data) {
    	var src = data.body;
    	if(data.body == 'success' && isTip){
    		 $("input[name=filename]").val('');
             $("input[name=articlePicUrl]").val('');
    		 layer.msg('略缩图删除成功', {
                 icon: 1,
                 time: 1000
             });
    		 return;
    	}else if(data.body == 'success'){
    		return;
    	}
        if ('00' == data.code) {
                layer.msg('略缩图上传成功', {
                    icon: 1,
                    time: 1000
            });
         
          var $list = $('#fileList');
          if($li1 != ''){
        	  $list.html('');
        	  $li1 = '';
          }
          $li = $('<div data-path="" class="file-item thumbnail"><a href="javascript:;" class="cancel">&times;</a></div>');
          $list.append($li);
          $li.append('<img src="' + _FILE_URL + src + '"/>');
          $("input[name=filename]").val(src);
          $("input[name=articlePicUrl]").val("/"+src);
          $li.on('click', '.cancel', function () {// 删除图片
        	  isTip = true;
        	  if(j != ''){
         		 ajaxFile(j,'/scholarshipStory/deletefile.do');
         	 }
              $li.remove();
             
          });
        }
    };
    if (window.FormData) {
    	var formData = new FormData($form[0]);
    	

    
       if(url == '/file/upload.do'){
	    	var chooseFileName = formData.get('file').name;
	       	if(chooseFileName == ""){
	       		return;
	       	}
    	 if (!/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(formData.get('file').name)) {
    	        layer.msg('图片类型必须是[.gif,jpeg,jpg,png]中的一种', {
                    icon: 1,
                    time: 3000
                });
    	        $('#file').val("");
    	        return;
    	 }
    	   
      	 var size = formData.get('file').size;
           if (size > 200 * 1024) {
           	 layer.msg('图片大小不能超过200kb', {
                    icon: 1,
                    time: 1000
                });
           	   $('#file').val("");
               return; 
           }
      }
        if(url == '/file/upload.do' && $li != ''){
        	var chooseFileName = formData.get('file').name;
        	if(chooseFileName == ""){
        		return;
        	}
        	 $li.remove();
        	 isTip = false;
        	 if(j != ''){
        		 ajaxFile(j,'/scholarshipStory/deletefile.do');
        	 }
        	
        }
        $.ajax({
            type: 'post',
            url: url,
            data: formData,
            dataType: 'json',
            processData: false,
            contentType: false,
            success: success
        });
    }
}

function isEmpty(StringVal){
	
	if(StringVal=="" || StringVal==undefined || StringVal==null){
		return false;
	}else{
		return true;
	}
	
}


