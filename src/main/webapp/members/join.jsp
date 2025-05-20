<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.security.SecureRandom"%>
<%@ page import="java.math.BigInteger"%>
<%
request.setCharacterEncoding("UTF-8");
String contextPath = request.getContextPath();
%>
<link href="<%=contextPath%>/css/members.css" rel="stylesheet">
<div class="container">
	<div id="joinForm">
		<h3>회원가입</h3>
		<p>로그인을 하시면 더 많은 도서관 서비스를 이용하실 수 있습니다.</p>
		<div class="inner">
			<form action="<%=contextPath%>/member/joinPro.me" method="post">

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
					<label>주소</label> <input type="text" id="address" name="address"
						placeholder="주소를 입력해주세요">
					<p id="nameInput"></p>
				</div>
				<div>
					<label>성별</label> 남성 <input type="radio" class="gender"
						name="gender" value="남성" checked> 여성 <input type="radio"
						class="gender" name="gender" value="여성">
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

				<div id="agree">
					<div class="policy">
						<div class="inner">
							<h3>회원가입이용약관</h3>
							<div class="content">
								<p>이용약관1</p>
								<p>내용내용내용</p>
								<input type="checkbox" class="agree-checkbox"> 이용약관1에
								동의합니다.
							</div>
							<div class="content">
								<p>이용약관2</p>
								<p>내용내용내용</p>
								<input type="checkbox" class="agree-checkbox"> 이용약관2에
								동의합니다.
							</div>
							<input type="checkbox" name="agree" id="checkAll"> 모든
							이용약관에 동의합니다.
						</div>
					</div>
					<p id="agreeInput"></p>
				</div>
				<div class="btn_wrap">
					<a href="#" onclick="check(); return false;" type="button">가입하기</a>
				</div>
			</form>
		</div>
	</div>
</div>

<script src="<%=contextPath%>/js/join.js"></script>