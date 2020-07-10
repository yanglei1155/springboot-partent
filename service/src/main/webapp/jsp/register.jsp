<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
<script src="${pageContext.request.contextPath}/js/register.js" type="text/javascript"></script>
 <script src="${pageContext.request.contextPath}/js/pageback.js"></script>
</head>
<body>
<div class="rcontainer">
    <div class="head_title">
      <%@include file="/jsp/header2.jsp" %>
     </div>
     <div class="container">
       <div class="head">
          <label class="label">锐捷注册</label>
       </div>
       <div class="body">
       <form action="${pageContext.request.contextPath}/store/skill/register" method="post" enctype="multipart/form-data">
         <label >账号</label>
         <input class="input" type="text" name="username" id="username"  placeholder="请输入账号"/>
         <label >密码</label>
         <input  class="input" type="password" name="password" id="password" placeholder="请输入密码"/>
         <label >号码</label>
          <input  class="input" type="text" name="phone" id="nick_name" placeholder="请输入号码"/>
           <label >地址</label>
           <input  class="input" type="text" name="address"  placeholder="请输入地址"/>
          <input type="file" style="opacity: 0;" id="file_head" name="upload"/>
          <div id="but_head" class="upload_img">头像</div>
         <input class="rsubmit" type="submit" value="注册"/>
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