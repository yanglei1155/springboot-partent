<%--
  Created by IntelliJ IDEA.
  User: yanglei
  Date: 2020/1/14
  Time: 10:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/skill.js"></script>
<link href="${pageContext.request.contextPath}/css/common.css" rel="stylesheet" type="text/css">
<html>
<script>
     $(document).ready(function () {
         setInterval("time()",100);
     })
    function time(){//1天3小时25分钟45秒   98745
          var nowTime=parseInt(new Date().getTime()/1000);
          var when=1582013740-nowTime
          var d=24*60*60
          var h=60*60
          var m=60
          var day=parseInt(when/d);
          var hour=parseInt((when-day*d)/h);
          var min=parseInt((when-day*d-hour*h)/m);
          var second=(when-day*d-hour*h-min*m);
         $("#time").text("距离开始时间还剩："+day+"天"+hour+"小时"+min+"分钟"+second+"秒");
         if(nowTime>=1579308420){
             $("#skill").addClass("skill");
         }
     }</script>
<head>
    <title>Title</title>
    <h1 id="time"></h1>
    <button id="skill" style="color:-moz-mac-accentdarkestshadow; pointer-events: none">开抢</button>
</head>
<body>


</body>
</html>
