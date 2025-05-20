<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
request.setCharacterEncoding("UTF-8");
String contextPath = request.getContextPath();
String id = (String) session.getAttribute("id"); // 세션에서 사용자 ID를 가져옴
%>

<div class="container">
	<div id="passForm" class="mypage">
		<jsp:include page="mypageMenu.jsp" />
		<div claas="pass-form-wrap">
			<h3 class="page-tit">비밀번호 재확인</h3>
			<p class="check-description">보안을 위해 비밀번호를 한번 더 입력해 주세요.</p>

			<form action="<%=contextPath%>/member/modify.me" method="post">
				<input type="hidden" name="id" id="id" value="<%=id%>">
				<fieldset class="box-pw">
					<input type="password" name="pass" id="pass" required>
				</fieldset>
				<fieldset class="box-btn">
					<input type="submit" value="확인" class="btn_submit ui-btn">
				</fieldset>
			</form>			
		</div>
	</div>
</div>
