<%@ page import="Vo.MemberVo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.security.SecureRandom"%>
<%@ page import="java.math.BigInteger"%>
<%@ page import="java.sql.Date"%>

<%
request.setCharacterEncoding("UTF-8");
String contextPath = request.getContextPath();
MemberVo memberVo = (MemberVo) request.getAttribute("memberVo");
%>

<link href="<%=contextPath%>/css/members.css" rel="stylesheet">
<div id="joinForm" class="mypage">
	<jsp:include page="mypageMenu.jsp" />
	<div class="join-form-wrap">
		<div class="inner">
			<form action="<%=contextPath%>/member/modifyPro.me" method="post">
				<input type="hidden" name="id" id="id" value="<%=memberVo.getId()%>">
				<input type="hidden" name="name" id="name"
					value="<%=memberVo.getName()%>">

				<div>
					<label>아이디</label> <input type="text" value="<%=memberVo.getId()%>"
						disabled>
					<p id="idInput"></p>
				</div>
				<div>
					<label>비밀번호</label> <input type="password" id="pass" name="pass"
						placeholder="비밀번호를 입력해주세요" required maxlength="20">
					<p id="passInput"></p>
				</div>
				<div>
					<label>이름</label> <input type="text"
						value="<%=memberVo.getName()%>" disabled>
					<p id="nameInput"></p>
				</div>
				<div>
					<label>주소</label> <input type="text" value="<%=memberVo.getName()%>" disabled>
					<p id="nameInput"></p>
				</div>
				<div>
					<label>성별</label>
					남성 <input type="radio" class="gender" name="gender" value="남성">
					여성 <input type="radio"	class="gender" name="gender" value="여성">
				</div>
				<div>
					<label>이메일</label> <input type="email" id="email" name="email"
						placeholder="이메일을 입력해주세요" required>
					<p id="emailInput"></p>
				</div>
				<div>
					<label>연락처</label> <input type="text" id="tel" name="tel"
						placeholder="연락처를 입력해주세요" required>
					<p id="telInput"></p>
				</div>

				<div class="btn_wrap">
					<a href="#" onclick="check(); return false;" type="button">수정하기</a>
				</div>
			</form>
		</div>
	</div>
</div>

<script src="<%=contextPath%>/js/join.js"></script>