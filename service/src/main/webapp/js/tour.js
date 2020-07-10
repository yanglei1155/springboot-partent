$(function(){
	$(".zan_count").click(function(){
		var id=$(this).attr("lay-id");
		var $h=$(this).children("h5");//获取孩子对象h5
		$.post("/store/skill/tour/plusZan",{"id":id},function(data,staus){
			$h.html("<h5 style='display: inline; font-weight: 350'>"+data+"</h5>");
		});
	});
	$(".tour_attention").click(function(){//前提是.tour_attention如果开始为关注就添加以关注的属性
		var id=$(this).attr("tourid");//获取sid
		$att=$(this).children("h5");//获取孩子对象h5
		var attention=$att.text();//获取h5的内容
		if(attention=="关注"){
			$.post("/store/skill/tour/changeAttention",{"id":id,"attention":attention},function(data,staus){
				   $("#attention").addClass("tour_noattention");
				   $att.html("<h5 style='text-align: center;line-height: 30px;color: white;'>"+data+"</h5>");
				});
		}	 
		if(attention=="已关注"){
			$.post("/store/skill/tour/changeAttention",{"id":id,"attention":attention},function(data,staus){
				   $("#attention").removeClass("tour_noattention");
				   $att.html("<h5 style='text-align: center;line-height: 30px;color:#008489;'>"+data+"</h5>");
				});
		}
	});
	$(".tour_noattention").click(function(){
		var id=$(this).attr("tourid");//获取sid
		$att=$(this).children("h5");//获取孩子对象h5
		var attention=$att.text();//获取h5的内容	
		if(attention=="关注"){
			$.post("/store/skill/tour/changeAttention",{"id":id,"attention":attention},function(data,staus){
				 $("#noattention").removeClass("tour_attentionImportant"); 	
				   $att.html("<h5 style='text-align: center;line-height: 30px;color: white;'>"+data+"</h5>");
				});
		}	 
		if(attention=="已关注"){//前提是.tour_noattention如果开始为已关注就添加关注的属性
			$.post("/store/skill/tour/changeAttention",{"id":id,"attention":attention},function(data,staus){
				 $("#noattention").addClass("tour_attentionImportant");	
				   $att.html("<h5 style='text-align: center;line-height: 30px;color:#008489;'>"+data+"</h5>");
				});
		}
	});
});