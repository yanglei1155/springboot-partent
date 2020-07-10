<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
</head>
<body>
  <div class="icontainer">
    <%@include file="/jsp/header.jsp"%>
      <table border="1"class="order">
       <tr>
         <td>名称</td>
           <td>库存</td>
         <td>图片</td>
         <td>秒杀价</td>
         <td>开始时间</td>
         <td>结束时间</td>
       </tr>
       <c:forEach items="${collectList}" var="collect">
       <tr>
           <td width="15%">${collect.name}</td>
           <td width="5%">${collect.stock_count}</td>
         <td width="20%"><a href="${pageContext.request.contextPath}/store/skill/product/getProductForInfo?id=${collect.id}"><img class="orderImg" style="width: 100%;height:80px;" src="${pageContext.request.contextPath}/img/products/${collect.small_pice}"></a></td>
         <td width="15%">${collect.coat_price}</td>
         <td width="20%">${collect.start_time}</td>
           <td width="20%">${collect.end_time}</td
       </tr>
       </c:forEach>
      </table>
    <%@include file="/jsp/foot.jsp"%>
  </div>
</body>
</html>