<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

join.jsp

<div id="agree">

	<div class="policy">
		<div class="inner">
			<h3>회원가입이용약관</h3>

			<div class="content">
				<p>이용약관1</p>
				<p>내용내용내용</p>
				<input type="checkbox" name="agree" id="agree"> 이용약관1에
				동의합니다.
			</div>

			<div class="content">
				<p>이용약관2</p>
				<p>내용내용내용</p>
				<input type="checkbox" name="agree" id="agree"> 이용약관2에
				동의합니다.
			</div>
			<input type="checkbox" name="agree" id="agree"> 모든 이용약관에
			동의합니다.
		</div>
	</div>

	<div class="btn_wrap">
		<a href="${contextPath}/member/join.me">동의합니다</a>
		<a>동의하지 않습니다</a>
	</div>

</div>
