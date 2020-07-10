<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
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
    <%@include file="/jsp/header.jsp" %>
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
                <a href="${pageContext.request.contextPath}/store/skill/product/getProductForInfo?id=79fe779d850c462eb34452508173249d"><img
                        src="${pageContext.request.contextPath}/img/ad/1.jpg"></a>
            </div>
            <div class="carousel-item">
                <a href="${pageContext.request.contextPath}/store/skill/product/getProductForInfo?id=e5ad6936c2fb413aa6deaeeb8dc5dbc2"><img
                        src="${pageContext.request.contextPath}/img/ad/2.jpg"></a>
            </div>
            <div class="carousel-item">
                <a href="${pageContext.request.contextPath}/store/skill/product/getProductForInfo?id=9e980a2d628f4b419f99fdb7e0d9185c"><img
                        src="${pageContext.request.contextPath}/img/ad/3.jpg"></a>
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
                        <a href="${pageContext.request.contextPath}/store/skill/product/getProductForInfo?id=${p.id}"><img
                                src="${pageContext.request.contextPath}/img/products/${p.small_pice}"></a>
                    </div>
                    <div class="index_body_co_a">
                        <a href="${pageContext.request.contextPath}/store/skill/product/getProductForInfo?id=${p.id}"><strong
                                style="color: #795548;margin: auto">${p.name}</strong><br></strong><strong
                                style="color: red;font-size: 19px">秒杀价:${p.coat_price}￥</strong> 原价
                            <del>${p.price}￥</del>
                            <br><strong style="color:#ff5722">库存:${p.stock_count}</strong></a>
                        <div style="background-color: red;width: 30%;height: 40%;float: right;color: white;text-align: center;line-height:40px"
                             class="skill"><a style="color: white"
                                              href="${pageContext.request.contextPath}/store/skill/product/getProductForInfo?id=${p.id}">立即抢购</a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>
</div>
<%@include file="page.jsp"%>
<%@include file="/jsp/foot.jsp" %>
</div>
</body>
</html>