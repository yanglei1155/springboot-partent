$(function(){
	$("#myEvaluate").click(function(){
		$("#myEvaluate_div").fadeToggle();
	});
	$("#myEvaluate_textarea").click(function(){
		$("#myEvaluate_textarea").addClass("myEvaluate_textarea");
	})
	$("#fb").click(function(){
		var myEvaluate=$("#myEvaluate_textarea").val();
		var username=$("#uid").attr("username");
		var rid=$("#rid").attr("roomid");
		var rname=$("#rname").attr("roomName");
		  location.href="RoomServlet?method=addEvaluate&myEvaluate="+myEvaluate+"&username="+username+"&rid="+rid+"&rname="+rname;
	});
});