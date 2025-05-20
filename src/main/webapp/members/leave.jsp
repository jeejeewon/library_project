<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
request.setCharacterEncoding("UTF-8");
String contextPath = request.getContextPath();
String id = (String) session.getAttribute("id"); // 세션에서 사용자 ID를 가져옴
%>
<div class="container">
	<div id="leaveForm" class="mypage">
		<jsp:include page="mypageMenu.jsp" />
		<div claas="pass-form-wrap">
			<h3 class="page-tit">회원 탈퇴</h3>
			<div class="check-description">
				<p>본인 확인을 위해 비밀번호를 한번 더 입력해 주세요.</p>
				<p class="caution-description"><span>탈퇴가 완료된 계정은 다시 복구할 수 없습니다.</span></p>
			</div>	
	
			<form action="<%=contextPath%>/member/leave.me" method="post">
				<input type="hidden" name="id" id="id" value="<%=id%>">
				<fieldset class="box-pw">
					<input type="password" name="pass" id="pass" required>
				</fieldset>			
				<fieldset class="box-btn-box">
					<button class="ui-btn"><a href="<%=contextPath%>/member/mypage">취소</a></button>
					<input type="submit" value="탈퇴" class="btn_submit">
				</fieldset>
			</form>		
		</div>
	</div>
</div>