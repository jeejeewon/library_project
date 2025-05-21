<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
String contextPath = request.getContextPath();
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<div class="container">	
	<div class="form-wrap change-pw">
		<div class="inner">
			<form action="<%=contextPath%>/member/changePw.do">
				<input type="hidden" name="id" value="${id}">
				<c:set var="abc" value="${requestScope.id}"></c:set>	
				<div>
					<h3 class="description">
						이메일 <span>${requestScope.email}</span> 으로 발송된</br> 인증 번호를 입력하세요.
					</h3>
				</div>	
				<div class="input-wrap">
					<input class="form-control" id=newAuthenCode name="newAuthenCode"
						type="text" placeholder="인증번호를 입력하세요" /> 
				</div>				
				<div class="input-wrap">
					<input cid="newPw" name="newPw" type="password"
						placeholder="새 비밀번호를 입력하세요" />
				</div>
				<div class="input-wrap">
					<input id="checkPw" name="checkPw" type="password"
						placeholder="새 비밀번호를 한번 더 입력하세요" />
				</div>
				<div class="input-wrap">
					<input type="submit" id="btn" value="비밀번호 변경"
						onclick="return check();">
				</div>
			</form>
		</div>
	</div>
</div>

<script src="<%=contextPath%>/js/scripts.js"></script>

<!-- 아이디, 비밀번호 유효성 검사 -->
<script type="text/javascript">
	function check() {
		var code = document.getElementById('newAuthenCode').value;
		var newPw = document.getElementById('newPw').value;
		var checkPw = document.getElementById('checkPw').value;

		if (newPw.trim() == '' || checkPw.trim() == '' || code.trim() == '') {
			alert("모든 입력란을 작성하세요.");
			return false;
		}
		if (newPw !== checkPw) {
			alert("비밀번호가 일치하지 않습니다.");
			return false;
		}

	}
</script>

