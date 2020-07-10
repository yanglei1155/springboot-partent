$(function(){
	$("#password").focus(function(){
		var username=$("#username").val();
		  var uPattern = /^[a-zA-Z0-9_-]{4,16}$/;
		  if(!uPattern.test(username)){
			  alert("用户名:请输入4到16位(字母，数字，下划线，减号)字符");
			  $("#username").val("");
			  $("#username").focus();
			  return ;
		  }
		  $.post("/store/skill/checkUser",{"username":username},function(data){
			  if(data=="exist"){
				  alert("存在此用户名");
				  $("#username").val("");
				  $("#username").focus();
			  }
		  });
	});
	$("#but_head").click(function(){
		$("#file_head").click();
	})
	$("#username").keypress(function(event){
		var keynum=(event.keyCode ? event.keyCode : event.which);
		if(keynum=="13"){
			event.preventDefault();//阻止按下enter键时自动提交问题
			$("#password").focus();
		}
		
	});
});