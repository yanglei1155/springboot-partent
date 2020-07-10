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
           <td>数量</td>
         <td>图片</td>
         <td>金额</td>
           <td>状态</td>
         <td>支付方式</td>
         <td>订单创建时间</td>
         <td>订单支付时间</td>
           <td>订单流水号</td>
       </tr>
       <c:forEach items="${orderList}" var="order">
       <tr>
           <td width="5%">${order.name}</td>
           <td width="5%">1</td>
         <td width="15%"><a href="${pageContext.request.contextPath}/store/skill/product/getProductForInfo?id=${order.seckill_id}"><img class="orderImg" style="width: 100%;height:80px;" src="${pageContext.request.contextPath}/img/products/${order.small_pice}"></a></td>
         <td width="5%">${order.money}</td>
         <td width="5%">${order.status}</td>
           <td width="10%">${order.pd_way}</td>
           <td width="20%">${order.create_time}</td>
           <td width="20%">${order.pay_time}</td>
           <td width="15">${order.transaction_id}</td>
       </tr>
       </c:forEach>
      </table>
    <%@include file="/jsp/foot.jsp"%>
  </div>
</body>
</html>