<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
          content="user-scalable=no, initial-scale=1, maximum-scale=1,
           minimum-scale=1, width=device-width, height=device-height"/>
<title>Insert title here</title>
<link href="${pageContext.request.contextPath}/css/login.css" rel="stylesheet" type="text/css"/>
</head>
<body>
   <div class="head_title">
     <h4 style="padding-left: 0px;font-size: 24px;font-weight:350" ><a  href="${pageContext.request.contextPath}/store/skill/product/getListForIndex?pageNum=1">首页</a></h4>
     <h4 style="padding-left:4%;color:#9E9E9E;font-size: 24px;font-weight:350"><a  href="#"></a>加盟我们</h4>
     <h4 style="padding-left: 4%;color: #9E9E9E;font-size: 24px;font-weight:350"><a  href="#"></a>投资</h4>
     <h4 style="padding-left: 4%;color: #9E9E9E;font-size: 24px;font-weight:350"><a  href="#"></a>帮忙推销</h4>
     <h4 style="padding-left: 4%;color: #9E9E9E;font-size: 24px;font-weight:350"><a  href="#"></a>点赞</h4>
     <p style="text-align: center;font-size:40px;display: inline;padding-left:5%;">爱彼迎商城</p>
      <c:if test="${not empty user}">
      <div class="head_img">
         <img style="width: 100%;height: 100%;" src="${pageContext.request.contextPath}/img/head/${user.head_img}"/>
      </div>
      <h4 id="zx" style="font-size: 24px;font-weight:350;padding-left: 7%"><a  href="${pageContext.request.contextPath}/store/skill/destorySession">注销</a></h4>
      </c:if>
      <c:if test="${ empty  user}">
       <h4 style="font-size: 24px;font-weight:350;padding-left: 4%"><a   href="${pageContext.request.contextPath}/jsp/login.jsp">您好!请登陆</a></h4>
      </c:if>
        <h4 style="font-size: 24px;font-weight:350;padding-left: 4%"><a  href="${pageContext.request.contextPath}/jsp/register.jsp">注册</a></h4>
        <c:if test="${not empty user  }">
            <h4 style="font-size: 24px;font-weight:350"><a  href="${pageContext.request.contextPath}/store/skill/order/selectOrder?uid=${user.uid}">订单</a></h4>
            <h4  style="font-size: 24px;font-weight:350;padding-left: 4%"><a href="${pageContext.request.contextPath}/jsp/address.jsp">增加地址</a></h4>
        </c:if>
       <h4 style="font-size: 24px;font-weight:350;padding-left: 4%"><a  href="#">客服</a></h4>
    </div>
</body>
</html>