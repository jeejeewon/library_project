<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
String contextPath = request.getContextPath();
%>

<div class="container">
	<div class="emailForm-form-wrap">
		<h3>인증코드 발급하기</h3>
		<form action="<%=contextPath%>/member/forgotPwPro.do" method="post"
			name="emailForm" novalidate="novalidate">
			<div>
				<input type="text" id="id" name="id" placeholder="아이디를 입력하세요">
			</div>
			<div>
				<input type="email" id="email" name="email"
					placeholder="이메일을 입력하세요">
			</div>
			<div>
				<button type="submit" value="임시 비밀번호 발급받기">전송하기</button>
			</div>
		</form>
	</div>
</div>
