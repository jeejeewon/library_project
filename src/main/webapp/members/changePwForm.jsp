<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
String contextPath = request.getContextPath();
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<div class="container">
	<div>
		<div>
			<div>
				<div>${id}
					<form action="<%=contextPath%>/member/changePw.do">
						<input type="hidden" name="id" value="${id}">

						<div>
							<input class="form-control" id=newAuthenCode name="newAuthenCode"
								type="text" placeholder="인증번호를 입력하세요" /> <label for="inputId">인증번호를
								입력하세요.</label>
						</div>
						<div>
							<c:set var="abc" value="${requestScope.id}"></c:set>
						</div>
						<div>
							<input cid="newPw" name="newPw" type="password"
								placeholder="새 비밀번호를 입력하세요" /> <label for="inputPassword">새
								비밀번호를 입력하세요.</label>
						</div>
						<div>
							<input id="checkPw" name="checkPw" type="password"
								placeholder="새 비밀번호를 한번 더 입력하세요" /> <label for="inputPassword">새
								비밀번호를 한번 더 입력하세요.</label>
						</div>
						<div>
							<input type="submit" id="btn" value="change"
								onclick="return check();">
						</div>
					</form>
				</div>
			</div>
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

