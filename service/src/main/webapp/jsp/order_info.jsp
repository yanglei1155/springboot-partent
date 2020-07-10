<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html>

	<head>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>下订单</title>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/tour/bootstrap.min.css" type="text/css" />
		<script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/js/order.js" type="text/javascript"></script>
		<!-- 引入自定义css文件 style.css -->
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css"/>
		<style>
			body {
				margin-top: 20px;
				margin: 0 auto;
			}
			
			.carousel-inner .item img {
				width: 100%;
				height: 300px;
			}
		</style>
	</head>

	<body>

	<%@ include file="/jsp/header.jsp" %>


		<div class="container" style="height: 771px; border: 2px solid #3a7fb7;border-radius: 17px;margin: auto;margin-top: 70px;">
		
		 <form id="orderForm" method="post" action="${pageContext.request.contextPath}/store/skill/order/saveOrder">
			<div class="row">

				<div style="margin:0 auto;margin-top:10px;width:950px;">
					<strong>订单详情</strong>
					<table class="table table-bordered">
						<tbody>
							<tr class="warning">
								<th colspan="5">订单编号:123</th>
							</tr>
							<tr class="warning">
								<th>图片</th>
								<th>名称</th>
								<th>价格</th>
								<th>数量</th>
								<th>小计</th>
							</tr>
							<tr class="active">
								<td width="60" width="40%">
									<input type="hidden" name="seckill_id" value="${product.id}">
									<input type="hidden" name="create_time" value="${product.create_time}">
									<input type="hidden" name="user_id" value="${user.uid}">
									<input type="hidden" name="money" value="${product.coat_price}">
									<img src="${pageContext.request.contextPath}/img/products/${product.small_pice}" width="70" height="60">
								</td>
								<td width="30%">
									<a target="_blank">${product.name}</a>
								</td>
								<td width="20%">
									￥${product.coat_price}
								</td>
								<td width="10%">
									1
								</td>
								<td width="15%">
									<span class="subtotal">￥${product.coat_price}</span>
								</td>
							</tr>
						</tbody>
					</table>
				</div>

			</div>

			<div>
				<hr/>
				
					<div class="form-group">
						<label class="col-sm-1 control-label" style="font-size: 18px;">地址</label>
						<div class="col-sm-5">
							<select id="address" name="receiver_address" style="font-size: 17px;width: 50%;height: 35px;border-radius: 5px">
								<c:forEach items="${addressList}" var="al">
									<option value="${al.id}">${al.address}</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label  class="col-sm-1 control-label" style="font-size: 18px;">电话</label>
						<div class="col-sm-5" >
							<input type="text" id="phone" name="receiver_phone" style="width: 50%;height: 35px;border-radius: 5px;font-size: 17px;" value="${addressList[0].phone}" />
						</div>
					</div>
				

				<hr/>

				<div style="margin-top:5px;margin-left:30px;height: 100px">
					<p>
						<br/>
						<input type="radio" name="pd_way" value="工商银行" checked="checked" /><strong style="font-size: 18px">工商银行</strong>
						<img src="${pageContext.request.contextPath}/img/bank_img/icbc.bmp" align="middle" />&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="pd_way" value="中国银行" /><strong style="font-size: 18px">中国银行</strong>
						<img src="${pageContext.request.contextPath}/img/bank_img/bc.bmp" align="middle" />&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="pd_way" value="农业银行" /><strong style="font-size: 18px">农业银行</strong>
						<img src="${pageContext.request.contextPath}/img/bank_img/abc.bmp" align="middle" />
						<br/>
						<br/>
						<input type="radio" name="pd_way" value="交通银行" /><strong style="font-size: 18px">交通银行</strong>
						<img src="${pageContext.request.contextPath}/img/bank_img/bcc.bmp" align="middle" />&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="pd_way" value="平安银行" /><strong style="font-size: 18px">平安银行</strong>
						<img src="${pageContext.request.contextPath}/img/bank_img/pingan.bmp" align="middle" />&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="pd_way" value="建设银行" /><strong style="font-size: 18px">建设银行</strong>
						<img src="${pageContext.request.contextPath}/img/bank_img/ccb.bmp" align="middle" />
						<br/>
						<br/>
						<input type="radio" name="pd_way" value="光大银行" /><strong style="font-size: 18px">光大银行</strong>
						<img src="${pageContext.request.contextPath}/img/bank_img/guangda.bmp" align="middle" />&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="pd_way" value="招商银行" /><strong style="font-size: 18px">招商银行</strong>
						<img src="${pageContext.request.contextPath}/img/bank_img/cmb.bmp" align="middle" />

					</p>
					<hr/>
					<p style="text-align:right;margin-right:100px;">
						<a href="javascript:document.getElementById('orderForm').submit();">
							<img src="${pageContext.request.contextPath}/img/finalbutton.gif" width="204" height="51" border="0" />
						</a>
					</p>
					<hr/>

				</div>
			</div>
		  </form>
		</div>

		
		
		<%@ include file="/jsp/footer.jsp" %>
		
	</body>

</html>