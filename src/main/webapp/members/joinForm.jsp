<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.security.SecureRandom"%>
<%@ page import="java.math.BigInteger"%>
<%
String contextPath = request.getContextPath();
%>

joinForm.jsp

<div id="joinForm">
	<div class="inner">
		<form action="<%=contextPath%>/member/JoinPro.me" method="post">

			<div>
				<label>아이디</label> <input type="text" id="id" name="id"
					placeholder="아이디를 입력해주세요" required maxlength="20">
				<p id="idInput"></p>
			</div>
			<div>
				<label>비밀번호</label> <input type="password" id="pass" name="pass"
					placeholder="비밀번호를 입력해주세요" required maxlength="20">
				<p id="passInput"></p>
			</div>
			<div>
				<label>이름</label> <input type="text" id="name" name="name"
					placeholder="이름을 입력해주세요" required maxlength="20">
				<p id="nameInput"></p>
			</div>
			<div>
				<label>주소</label> <input type="text" id="name" name="name"
					placeholder="이름을 입력해주세요">
				<p id="nameInput"></p>
			</div>
			<div>
				<label>성별</label> 남성<input type="radio" class="gender" name="gender"
					value="남"> 여성 <input type="radio" class="gender"
					name="gender" value="여">
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
				<a href="#" onclick="check(); return false;" type="submit">가입하기</a>
			</div>
		</form>
	</div>
</div>

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script src="<%=contextPath%>/js/join.js"></script>