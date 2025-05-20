<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
String contextPath = request.getContextPath();
%>

<div class="container">
	<div class="form-wrap find-idpw">
		<div class="inner">
			<h3>아이디 찾기</h3>
			<form action="<%=contextPath%>/member/forgotIdPro.do" method="post">
				<div class="input-wrap">
					<input type="email" id="email" name="email"
						placeholder="이메일을 입력하세요">
				</div>
				<div class="input-wrap">
					<button type="submit">확인</button>
				</div>
			</form>
		</div>
	</div>
</div>
