$(function(){
	$("#rimg").mouseenter(function(){
	  $("#rimg").addClass("mouseenter_img");
	});
	$("#rimg").mouseleave(function(){
     $("#rimg").removeClass("mouseenter_img");
	});
	//调节图片一亮度
	$("#rimg1").mouseenter(function(){
	   $("#rimg1").addClass("mouseenter_img");	
		
	});
	$("#rimg1").mouseleave(function(){
	     $("#rimg1").removeClass("mouseenter_img");
	});
	//调节图片二亮度
   $("#rimg2").mouseenter(function(){
	   $("#rimg2").addClass("mouseenter_img");	
		
	});
	$("#rimg2").mouseleave(function(){
	     $("#rimg2").removeClass("mouseenter_img");
	}); 
	//调节图片三亮度
   $("#rimg3").mouseenter(function(){
	   $("#rimg3").addClass("mouseenter_img");	
		
	});
	$("#rimg3").mouseleave(function(){
	     $("#rimg3").removeClass("mouseenter_img");
	});
	//调节图片四亮度
   $("#rimg4").mouseenter(function(){
	   $("#rimg4").addClass("mouseenter_img");	
		
	});
	$("#rimg4").mouseleave(function(){
	     $("#rimg4").removeClass("mouseenter_img");
	});
	  $(window).scroll(function(){//当滚动条滚动时触发事件,当滚动条为值大于484时让div使div与滚动条保持相同高度
		  var tops=$(window).scrollTop();
          if(580<=tops){
        	  $("#roomOrder").css("position","absolute").css("top",tops);
          }	
          if(580>tops){
        	  $("#roomOrder").css("position","absolute").css("top","580px");
          }	
	  });
	  //打折
	  $("#outdate").blur(function(){
		  var rprice=$("#rprice").attr("price");
		  var price=parseInt(0.75*rprice)//折扣后的价格
		  $("#rprice").html(" <h3 style='text-align: center;' id='rprice'price='${room.r_price}'><p style='font-size: 20px;font-weight: 350;color: black;'>￥"+price+"每晚</p></h3>");
	  });
});