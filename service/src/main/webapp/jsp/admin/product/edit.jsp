<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<HTML>
<HEAD>
	<meta http-equiv="Content-Language" content="zh-cn">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<LINK href="${pageContext.request.contextPath}/css/Style1.css" type="text/css" rel="stylesheet">
</HEAD>

<body>
<!--  -->
<form id="userAction_save_do" name="Form1" action="${pageContext.request.contextPath}/store/skill/product/editProduct"  method="post" enctype="multipart/form-data">
	&nbsp;
	<table cellSpacing="1" cellPadding="5" width="100%" align="center" bgColor="#eeeeee" style="border: 1px solid #8ba7e3" border="0">
		<tr>
			<td class="ta_01" align="center" bgColor="#afd1f3" colSpan="4"
				height="26">
				<STRONG>添加商品</STRONG>
			</td>
		</tr>
		<tr>
			<td width="18%" align="center" bgColor="#f5fafe" class="ta_01">
				开始时间：
			</td>
			<td class="ta_01" bgColor="#ffffff" colspan="1">
				<input type="datetime-local" name="start_time" value="${product.start_time}">
			</td>
			<td width="18%" align="center" bgColor="#f5fafe" class="ta_01">
				结束时间：
			</td>
			<td class="ta_01" bgColor="#ffffff" colspan="1">
				<input type="datetime-local" name="end_time" value="${product.end_time}">
			</td>
		</tr>
		<tr>
			<td width="18%" align="center" bgColor="#f5fafe" class="ta_01">
				商品名称：
			</td>
			<td class="ta_01" bgColor="#ffffff">
				<input type="text" name="name" value="${product.name}" id="userAction_save_do_logonName" class="bg"/>
			</td>
			<td width="18%" align="center" bgColor="#f5fafe" class="ta_01">
				商品库存：
			</td>
			<td class="ta_01" bgColor="#ffffff">
				<input type="text" name="num" value="${product.num}" class="bg"/>
				<input type="hidden" name="id" value="${product.id}">
			</td>
		</tr>
		<tr>
			<td width="18%" align="center" bgColor="#f5fafe" class="ta_01">
				原价：
			</td>
			<td class="ta_01" bgColor="#ffffff">
				<input type="text" name="price" value="${product.price}"  class="bg"/>
			</td>
			<td width="18%" align="center" bgColor="#f5fafe" class="ta_01">
				秒杀价格：
			</td>
			<td class="ta_01" bgColor="#ffffff">
				<input type="text" name="coat_price"  value="${product.coat_price}"  class="bg"/>
			</td>
		</tr>
		<tr>
			<td width="10%" align="center" bgColor="#f5fafe" class="ta_01">
				商品图片：
			</td>
			<td class="ta_01" bgColor="#ffffff" colspan="1">
				<input type="file" name="filePath"/>
				<input type="hidden" name="small_pice" value="${product.small_pice}">
			</td>
			<td width="10%" align="center" bgColor="#f5fafe" class="ta_01">
				状态：
			</td>
			<td class="ta_01" bgColor="#ffffff" colspan="1">

				<select name="status">
					<option value="1" <c:if test="${product.status==1}">selected</c:if>>售卖</option>
					<option value="0" <c:if test="${product.status==0}">selected</c:if>>下架</option>
				</select>
			</td>
		</tr>
		<tr>
			<td width="18%" align="center" bgColor="#f5fafe" class="ta_01">
				商品描述：
			</td>
			<td class="ta_01" bgColor="#ffffff" colspan="3">
				<textarea name="introduction" rows="5" cols="30" >${product.introduction}</textarea>
			</td>
		</tr>
		<tr>
			<td class="ta_01" style="WIDTH: 100%" align="center"
				bgColor="#f5fafe" colSpan="4">
				<button type="submit" id="userAction_save_do_submit" value="确定" class="button_ok">
					&#30830;&#23450;
				</button>

				<FONT face="宋体">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</FONT>
				<button type="reset" value="重置" class="button_cancel">&#37325;&#32622;</button>

				<FONT face="宋体">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</FONT>
				<INPUT class="button_ok" type="button" onclick="history.go(-1)" value="返回"/>
				<span id="Label1"></span>
			</td>
		</tr>
	</table>
</form>
</body>
</HTML>