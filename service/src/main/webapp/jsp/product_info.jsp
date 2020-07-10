<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html>
	<head>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>商品详情信息</title>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" type="text/css" />
		<script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>
		<!-- 引入自定义css文件 style.css -->
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css"/>
		<script>
			$(document).ready(function () {
				setInterval("time()",100);
			})
			function time(){//1天3小时25分钟45秒   98745
				var nowTime=parseInt(new Date().getTime()/1000);
				var startTime=parseInt(parseInt($("#startTime").val())/1000);
				var endTime=parseInt(parseInt($("#endTime").val())/1000);
				var begin=new Array();
				begin=getDifferenceTime(startTime,nowTime);
				end=getDifferenceTime(endTime,nowTime);
				var bday=begin[0];
				var bhour=begin[1];
				var bmin=begin[2];
				var bsecond=begin[3];
				var eday=end[0];
				var ehour=end[1];
				var emin=end[2];
				var esecond=end[3];
				if(startTime<=nowTime){
					$("#btnId").addClass("bSkillBtn");
				}
				if(bday<=0&&bhour<=0&&bmin<=0&&bsecond<=0){
					if(eday>=0&&ehour>=0&&emin>=0&&esecond>=0){
						$("#skillTime").text("距秒杀截至还剩："+eday+"天"+ehour+"小时"+emin+"分钟"+esecond+"秒");
					}if(eday<=0&&ehour<=0&esecond<=0&&emin<=0) {
						$("#skillTime").text("秒杀结束，期待下次您得光临");
						$("#btnId").addClass("eSkillBtn");
					}

				}
				else {
					$("#skillTime").text("倒计时：  "+bday+"天"+bhour+"小时"+bmin+"分钟"+bsecond+"秒");
				}
             function  getDifferenceTime(time1,time2) {
				 var time=new Array();
				 var when=time1-time2
				 var d=24*60*60
				 var h=60*60
				 var m=60
				 var day=parseInt(when/d);
				 var hour=parseInt((when-day*d)/h);
				 var min=parseInt((when-day*d-hour*h)/m);
				 var second=(when-day*d-hour*h-min*m);
				 time[0]=day;
				 time[1]=hour;
				 time[2]=min;
				 time[3]=second;
				 return time;
			 }
			}</script>
		<style>
			body {
				margin-top: 20px;
				margin: 0 auto;
			}
			.bSkillBtn{
				background-color: red !important;
				pointer-events: auto !important;
			}
			.eSkillBtn{
				background-color:dimgrey !important;
				pointer-events:none !important;
			}
			.carousel-inner .item img {
				width: 100%;
				height: 300px;
			}
		</style>
	</head>

	<body>

		
			<%@ include file="/jsp/header.jsp" %>


		<div class="container" style="border: 1px solid #4caf509e">
			<div class="row">
				<div style="margin:0 auto;width:950px;">
				
				  <form id="myForm" method="post" action="${pageContext.request.contextPath}/store/skill/order/clickSkill">
					<div class="col-md-6">
						<img style="opacity: 1;width:400px;height:350px;" title="" class="medium" src="${pageContext.request.contextPath}/img/products/${product.small_pice}">
					</div>

					<div class="col-md-6">
					    <!-- 
					    	${product}  :底层依次调用4个域对象上的*.getAttribute("keyName");
					    	寻找到request可以获取到一个对象 product
					    	${product.name} :通过获取到的product对象调用对象上的getPname()方法.
					     -->
						<input type="hidden" id="startTime" value="${product.startSecond}">
						<input type="hidden" id="endTime" value="${product.endSecond}">
						<input type="hidden" id="uid" name="user_id" value="${user.uid}">
						<input type="hidden" id="pid"  name="seckill_id" value="${product.id}">
						<div style="margin-top: 20px"><strong style="font-size: 18px;font-family:'Comic Sans MS'">${product.name}</strong></div>
						<div style="border-bottom: 1px dotted #dddddd;width:350px;margin-top:15px ;">
							<div id="skillLb" style="color: red;font-size: 18px"><strong style="color: red;font-size: 15px" id="skillTime"></strong></div>
						</div>
						<div style="margin:30px 0 10px 0;font-size: 17px">秒杀价: <strong style="color:#ef0101;font-size: 20px">￥：${product.coat_price}元/份</strong> 原价： <del>￥${product.price}元/份</del>
						</div>

						<div style="margin:20px 0 10px 0;font-size: 17px;">促销: <a target="_blank" title="限时抢购 (2014-07-30 ~ 2015-01-01)" style="background-color: #f07373;">限时抢购</a>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	<strong style="font-size: 17px;font-weight:350">库存：</strong><strong style="color: red">${product.stock_count}</strong>
						</div>
						<div style="padding:10px;border:1px solid #e7dbb1;border-radius:10px;width:350px;margin:15px 0 10px 0;height: 13%;background-color: #4caf509e;position:fixed">
								<!-- 向服务端发送 商品pid-->
								<input type="hidden" name="pid" value="${product.id}"/>
								

							<div style="margin:25px 0 10px 0;;text-align: center;">
								<%--秒杀--%>
								<!-- 取消链接的默认行为 -->
								<a href="javascript:void(0)">
									<input id="btnId" style="background: url('${pageContext.request.contextPath}/img/product.gif') no-repeat scroll 0 -600px rgba(0, 0, 0, 0);height:36px;width:127px;background-color:dimgrey;pointer-events:none;color:whitesmoke;" value="秒杀" type="button">
								</a> &nbsp;<strong style="text-decoration: none;color: #004d40" id="collect">收藏商品</strong></div>
						</div>
					</div>
				   </form>

				</div>
				<div class="clear"></div>
				<div style="width:950px;margin:0 auto;border-radius:10px;background-color: #4caf509e;height:148px">
						<strong style="color:#795548 ;font-size: 24px">商品介绍：</strong><br>
						<strong style="font-size: 18px">${product.introduction}</strong>
					</div>
				</div>
			</div>
		</div>

		<%@include file="/jsp/footer.jsp" %>

	</body>
<script>
$(function(){
	$("#btnId").click(function(){
		var uid=$("#uid").val();
		var gid=$("#pid").val();
		if(uid==null||uid==""){
			alert("您还没登录呢！要不先去登录页登录？");
			return ;
		}else {
			$.post("/store/skill/order/existOrder",{"seckill_id":gid,"user_id":uid},function (data,status) {
				if(data=="exist"){
					alert("您已秒杀过此商品，不能重复秒杀哦！");
				}else {
					var formObj=document.getElementById("myForm");
					//formObj.action="/store_v5/CartServlet";
					//formObj.method="get";
					formObj.submit();
				}
			})
		}
	});
	$("#collect").click(function () {
		var uid=$("#uid").val();
		var gid=$("#pid").val();
		if(uid==null||uid==""){
			alert("您还没登录呢！要不先去登录页登录？");
			return ;
		}else {
			$.post("/store/skill/collect/collectProduct",{"uid":uid,"gid":gid},function(data){
				alert(data)
			});
		}
	});
});
</script>
</html>