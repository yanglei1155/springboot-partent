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
    <link href="${pageContext.request.contextPath}/css/test.css" rel="stylesheet" type="text/css"/>
    <script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/login.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/pageback.js"></script>
</head>
<body>
<div class="fcontainer">
    <div class="head_title">
        <%@include file="/jsp/header2.jsp" %>
    </div>
    <div class="container">
        <div class="head">
            <label class="label">增加地址</label>
        </div>
        <div class="body">
            <form action="${pageContext.request.contextPath}/store/skill/saveAddress" method="post">
                <div class="user_head" id="user_head">
                    <img id="lhead_img">
                </div>
                <input type="hidden" name="uid" value="${user.uid}">
                <label style="font-size:30px;padding-left:35px;margin-top:30px;display:inline-block;">号码</label>
                <input class="input" type="text" name="phone" id="username" placeholder="请输入号码"/>
                <label style="font-size:30px;padding-left:35px;margin-top:70px;display:inline-block;">地址</label>
                <textarea name="address" style="width:68%;height:50px; "placeholder="请输入地址"></textarea>
                <input class="submit" type="submit" value="添加"/>
            </form>
        </div>
        <div class="foot_other">
            <ul id="nav">
                <li><a href="#">联系我们</a></li>
                <li><a href="#">咨询</a></li>
                <li><a href="#">加入我们</a></li>
            </ul>
        </div>
    </div>
</div>
</body>
</html>