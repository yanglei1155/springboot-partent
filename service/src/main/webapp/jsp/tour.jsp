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
<link href="${pageContext.request.contextPath}/css/test.css" rel="stylesheet" type="text/css"/>
 <link rel="stylesheet" href="${pageContext.request.contextPath}/css/tour/bootstrap.min.css">
 <script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js" type="text/javascript"></script>
 <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
 <script src="${pageContext.request.contextPath}/js/tour.js"></script>
 <script src="${pageContext.request.contextPath}/js/pageback.js"></script>
</head>
<body>
 <div class="icontainer">
    <%@include file="/jsp/header.jsp"%>
    <div class="tour_head">
       <h6 style="display: inline;font-weight: 350;">${tour.address}</h6>
        <h6 style="display: inline;font-weight: 350;">${tour.date}</h6>
       <c:if test="${tour.attention eq '关注'}">
        <div class="tour_attention" tourid="${tour.id}" id="attention" >
            <h5 style="text-align: center;line-height: 30px;color: #008489;">${tour.attention}</h5>
        </div>
        </c:if>
        <c:if test="${tour.attention eq '已关注'}">
        <div class="tour_noattention" tourid="${tour.id}" id="noattention" >
            <h5 style="text-align: center;line-height: 30px;color: white;">${tour.attention}</h5>
        </div>
        </c:if>
    </div>
    <c:forEach items="${tour.tourImgs}" var="tour_img">
    <div class="tour_img">
        <img style="width: 100%;height: 100%;" src="${pageContext.request.contextPath}/img/tour/${tour_img.other_img}">
    </div> 
    </c:forEach>
    <div class="tour_address">
        <img style="width: 30%;height: 100%;" src="${pageContext.request.contextPath}/img/logo/address.jpg">
        <div class="tour_address_right">
            <h5 style="text-align: center;line-height: 30px;color: #008489;padding-left: 0px;width: 75%;">${tour.address}</h5>
        </div>
    </div>
    <div class="tour_desc">
       <h2 style="font-weight: 350">${tour.desc}</h2>
       <h3 style="font-weight: 300;font-size: 24px;margin-top: 20px;">${tour.minuteDesc}</h3>
       <h6 style="float: left;color:#008489;margin-top: 35px; ">${tour.zan}人以点赞</h6>
    </div>
    <%@include file="/jsp/foot.jsp" %>
 </div>
</body>
</html>