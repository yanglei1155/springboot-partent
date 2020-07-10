<%--
  Created by IntelliJ IDEA.
  User: yanglei
  Date: 2020/3/4
  Time: 14:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>menu</title>
    <script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/menu.js" type="text/javascript"></script>
</head>
<body bgcolor="#5f9ea0">
  <div style="width: 10%;height: 60%;border: 2px solid black">
    <div id="orderMenu" style="width: 100%;height: 5%;border: 1px solid red;border-radius: 8px;text-align: center;background-color: #ff5722c9">
         <strong style="">订单</strong>
    </div>
      <ul id="orderUl" style="text-decoration: none">
          <li><a style="text-decoration: none" href="${pageContext.request.contextPath}/store/skill/order/getAllOrders?pageNum=1">订单列表</a></li>
      </ul>
      <div id="productMenu" style="width: 100%;height: 5%;border: 1px solid red;border-radius: 8px;text-align: center;background-color: #ff5722c9">
          <strong style="">商品</strong>
      </div>
      <ul id="productUl" style="text-decoration: none">
          <li><a style="text-decoration: none" href="${pageContext.request.contextPath}/store/skill/product/getList?pageNum=1">商品列表</a></li>
          <li style="padding-top: 30px"><a  style="text-decoration: none" href="${pageContext.request.contextPath}/jsp/admin/product/add.jsp">上传商品</a></li>
      </ul>
  </div>
</body>
</html>
