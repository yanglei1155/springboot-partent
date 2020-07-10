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
<script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/room.js" type="text/javascript"></script>
</head>
<body>
 <div class="pageModel">
      <c:if test="${pageModel.nowPage>1}">
       <div class="pageModel_icon"><a style="font-size: 20px;text-decoration: none;color:black " href="${pageContext.request.contextPath}/${pageModel.url}?pageNum=${pageModel.lastPage}"><</a></div>
      </c:if>
      <c:forEach var="page" begin="1" end="${pageModel.pageCount}">
       <div id="a_div" class="pageModel_icon"><a class="pageModel_icon_a" href="${pageContext.request.contextPath}/${pageModel.url}?pageNum=${page}">${page}</a></div>
      </c:forEach>
       <c:if test="${pageModel.nowPage<pageModel.pageCount}">
       <div class="pageModel_icon"><a style="font-size: 20px;text-decoration: none;color: black" href="${pageContext.request.contextPath}/${pageModel.url}?pageNum=${pageModel.nextPage}">></a></div>
      </c:if>
        <div class="pageModel_icon" style="float: right;background-color: #f44336;">${pageModel.nowPage}/${pageModel.pageCount}</div>
      </div>
</body>
</html>