<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
          content="user-scalable=no, initial-scale=1, maximum-scale=1,
           minimum-scale=1, width=device-width, height=device-height"/>
<title>Insert title here</title>
<link href="${pageContext.request.contextPath}/css/login.css" rel="stylesheet" type="text/css"/>
 <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css/bootstrap.min.css">
 <link href="${pageContext.request.contextPath}/css/test.css" rel="stylesheet" type="text/css"/>
 <script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js" type="text/javascript"></script>
 <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
 <script src="${pageContext.request.contextPath}/js/tour.js"></script>
  <script src="${pageContext.request.contextPath}/js/pageback.js"></script>
</head>
<body>
 <div class="icontainer">
    <%@include file="/jsp/header.jsp"%>
    <div id="demo" class="carousel slide" data-ride="carousel">
 
  <!-- 指示符 -->
  <ul class="carousel-indicators">
    <li data-target="#demo" data-slide-to="0" class="active"></li>
    <li data-target="#demo" data-slide-to="1"></li>
    <li data-target="#demo" data-slide-to="2"></li>
  </ul>
 
  <!-- 轮播图片 -->
  <div class="carousel-inner">
    <div class="carousel-item active">
      <a href="${pageContext.request.contextPath}/store/skill/product/getProductForInfo?id=79fe779d850c462eb34452508173249d"><img src="${pageContext.request.contextPath}/img/ad/1.jpg"></a>
    </div>
    <div class="carousel-item">
      <a href="${pageContext.request.contextPath}/store/skill/product/getProductForInfo?id=e5ad6936c2fb413aa6deaeeb8dc5dbc2"><img src="${pageContext.request.contextPath}/img/ad/2.jpg"></a>
    </div>
    <div class="carousel-item">
      <a href="${pageContext.request.contextPath}/store/skill/product/getProductForInfo?id=9e980a2d628f4b419f99fdb7e0d9185c"><img src="${pageContext.request.contextPath}/img/ad/3.jpg"></a>
    </div>
  </div>
  
  <!-- 左右切换按钮 -->
  <a class="carousel-control-prev" href="#demo" data-slide="prev">
    <span class="carousel-control-prev-icon"></span>
  </a>
  <a class="carousel-control-next" href="#demo" data-slide="next">
    <span class="carousel-control-next-icon"></span>
  </a>
 
</div>
   <div class="index_body">
    <h3 style="font-weight: bold;padding-left: 23px;">秒杀特价商品</h3>
     <h6 style="padding-left: 23px">最低七折起，可叠加使用优惠券</h6>
       <div class="index_body1">
      <c:forEach items="${pageModel.list}" var="p" varStatus="status">
     <div class="index_body1_co">
          <div class="index_body_co_img">
            <a href="${pageContext.request.contextPath}/store/skill/product/getProductForInfo?id=${p.id}"><img src="${pageContext.request.contextPath}/img/products/${p.small_pice}"></a>
          </div>
          <div class="index_body_co_a">
              <a href="${pageContext.request.contextPath}/store/skill/product/getProductForInfo?id=${p.id}"><strong style="color: #795548;margin: auto">${p.name}</strong><br></strong><strong style="color: red;font-size: 19px">秒杀价:${p.coat_price}￥</strong> 原价<del>${p.price}￥</del><br><strong style="color:#ff5722">库存:${p.stock_count}</strong></a>
              <div style="background-color: red;width: 30%;height: 40%;float: right;color: white;text-align: center;line-height:40px" class="skill"><a style="color: white" href="${pageContext.request.contextPath}/store/skill/product/getProductForInfo?id=${p.id}">立即抢购</a></div>
          </div>
       </div>
      </c:forEach>
     </div>
      <a style="text-decoration: none;" href="${pageContext.request.contextPath}/store/skill/product/getProductList?pageNum=1"><h5 style="padding-left: 15px;color:#008489;font-weight: bold;">查看更多秒杀品></h5></a>
      <h3 style="font-weight: bold; margin-top: 15px;padding-left: 10px;">精彩旅行故事</h3>
   </div>
   <div class="tour_story">
      <div class="tour_story_top">
         <div class="tour_story_top_containter">
            <div class="tour_story_img">
               <a href="${pageContext.request.contextPath}/store/skill/tour/getTourById?id=${tour[0].id}"><img style="width: 100%;height: 100%;" src="${pageContext.request.contextPath}/img/tour/${tour[0].img}"></a>
            </div>
            <div class="tour_story_a">
              <a href="${pageContext.request.contextPath}/store/skill/tour/getTourById?id=${tour[0].id}">${tour[0].desc}</a>
                  <div class="zan_count" lay-id="${tour[0].id}">
                    <img  style="width:30px;height:30px;" src="${pageContext.request.contextPath}/img/logo/zan3.jpg">
                     <h5 style="display: inline; font-weight: 350">${tour[0].zan}</h5>
                  </div>
            </div>
         </div>
          <div class="tour_story_top_containter">
            <div class="tour_story_img">
              <a href="${pageContext.request.contextPath}/store/skill/tour/getTourById?id=${tour[1].id}"><img style="width: 100%;height: 100%;" src="${pageContext.request.contextPath}/img/tour/${tour[1].img}"></a>
            </div>
            <div class="tour_story_a">
               <a href="${pageContext.request.contextPath}/store/skill/tour/getTourById?id=${tour[1].id}">${tour[1].desc}</a>
                 <div class="zan_count"  lay-id="${tour[1].id}" >
                      <img  style="width:30px;height:30px;" src="${pageContext.request.contextPath}/img/logo/zan3.jpg"></a>
                     <h5 style="display: inline; font-weight: 350">${tour[1].zan}</h5>
                  </div>
            </div>
         </div>
          <div class="tour_story_top_containter">
           <div class="tour_story_img">
             <a href="${pageContext.request.contextPath}/store/skill/tour/getTourById?id=${tour[2].id}"><img style="width: 100%;height: 100%;" src="${pageContext.request.contextPath}/img/tour/${tour[2].img}"></a>
            </div>
            <div class="tour_story_a">
              <a href="${pageContext.request.contextPath}/store/skill/tour/getTourById?id=${tour[2].id}">${tour[2].desc}</a>
                <div class="zan_count" lay-id="${tour[2].id}">
                    <img  style="width:30px;height:30px;" src="${pageContext.request.contextPath}/img/logo/zan3.jpg">
                     <h5 style="display: inline; font-weight: 350">${tour[2].zan}</h5>
                  </div>
            </div>
         </div>
          <div class="tour_story_top_containter">
           <div class="tour_story_img">
              <a href="${pageContext.request.contextPath}/store/skill/tour/getTourById?id=${tour[3].id}"><img style="width: 100%;height: 100%;" src="${pageContext.request.contextPath}/img/tour/${tour[3].img}"></a>
            </div>
            <div class="tour_story_a">
              <a href="${pageContext.request.contextPath}/store/skill/tour/getTourById?id=${tour[3].id}">${tour[3].desc}</a>
                 <div class="zan_count" lay-id="${tour[3].id}">
                    <img  style="width:30px;height:30px;" src="${pageContext.request.contextPath}/img/logo/zan3.jpg">
                     <h5 style="display: inline; font-weight: 350">${tour[3].zan}</h5>
                  </div>
            </div>
         </div>
      </div>
      <div class="tour_story_bottom">
         <div class="tour_story_top_containter">
          <div class="tour_story_img">
             <a href="${pageContext.request.contextPath}/store/skill/tour/getTourById?id=${tour[4].id}"><img style="width: 100%;height: 100%;" src="${pageContext.request.contextPath}/img/tour/${tour[4].img}"></a>
            </div>
            <div class="tour_story_a">
               <a href="${pageContext.request.contextPath}/store/skill/tour/getTourById?id=${tour[4].id}">${tour[4].desc}</a>
                 <div class="zan_count" lay-id="${tour[4].id}">
                    <img  style="width:30px;height:30px;" src="${pageContext.request.contextPath}/img/logo/zan3.jpg">
                     <h5 style="display: inline; font-weight: 350">${tour[4].zan}</h5>
                  </div>
            </div>
         </div>
          <div class="tour_story_top_containter">
           <div class="tour_story_img">
              <a href="${pageContext.request.contextPath}/store/skill/tour/getTourById?id=${tour[5].id}"><img style="width: 100%;height: 100%;" src="${pageContext.request.contextPath}/img/tour/${tour[5].img}"></a>
            </div>
            <div class="tour_story_a">
                <a href="${pageContext.request.contextPath}/store/skill/tour/getTourById?id=${tour[5].id}">${tour[5].desc}</a>
                 <div class="zan_count" lay-id="${tour[5].id}">
                    <img  style="width:30px;height:30px;" src="${pageContext.request.contextPath}/img/logo/zan3.jpg">
                     <h5 style="display: inline; font-weight: 350">${tour[5].zan}</h5>
                  </div>
            </div>
         </div>
          <div class="tour_story_top_containter">
           <div class="tour_story_img">
              <a href="${pageContext.request.contextPath}/store/skill/tour/getTourById?id=${tour[6].id}"><img style="width: 100%;height: 100%;" src="${pageContext.request.contextPath}/img/tour/${tour[6].img}"></a>
            </div>
            <div class="tour_story_a">
                <a href="${pageContext.request.contextPath}/store/skill/tour/getTourById?id=${tour[6].id}">${tour[6].desc}</a>
                  <div class="zan_count" lay-id="${tour[6].id}">
                    <img  style="width:30px;height:30px;" src="${pageContext.request.contextPath}/img/logo/zan3.jpg">
                     <h5 style="display: inline; font-weight: 350">${tour[6].zan}</h5>
                  </div>
            </div>
         </div>
          <div class="tour_story_top_containter">
           <div class="tour_story_img">
             <a href="${pageContext.request.contextPath}/store/skill/tour/getTourById?id=${tour[7].id}"><img style="width: 100%;height: 100%;" src="${pageContext.request.contextPath}/img/tour/${tour[7].img}"></a>
            </div>
            <div class="tour_story_a">
                <a href="${pageContext.request.contextPath}/store/skill/tour/getTourById?id=${tour[7].id}">${tour[7].desc}</a>
                  <div class="zan_count" lay-id="${tour[7].id}">
                    <img  style="width:30px;height:30px;" src="${pageContext.request.contextPath}/img/logo/zan3.jpg">
                     <h5 style="display: inline; font-weight: 350">${tour[7].zan}</h5>
                  </div>
            </div>
         </div>
      </div>   
   </div>
   <%@include file="/jsp/foot.jsp" %>
  </div>
</body>
</html>