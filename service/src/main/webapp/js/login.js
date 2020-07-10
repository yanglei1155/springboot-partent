$(function(){
	$("#password").focus(function(){
		var username=$("#username").val();
		  var uPattern = /^[a-zA-Z0-9_-]{4,16}$/;
		  if(!uPattern.test(username)){
			  $("#username").val("");
			  $("#username").focus();
			  alert("请输入4到16位(字母，数字，下划线，减号)字符");
		  }
		  $.post("/store/skill/getHead",{"username":username},function(data){
			  $("#user_head").css({"background-image":"url(../img/head/"+data+")","background-size":"50%","background-repeat":"no-repeat"});
	});
});
	$("#username").keypress(function(event){
		var keynum=(event.keyCode ? event.keyCode : event.which);
		if(keynum=="13"){
			event.preventDefault();//阻止按下enter键时自动提交问题
			$("#password").focus();
		}
		
	});
});