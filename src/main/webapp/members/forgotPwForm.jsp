<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
String contextPath = request.getContextPath();
%>

<div class="container">
	<div class="form-wrap find-idpw">
		<div class="inner">
			<h3>비밀번호 찾기</h3>
			<p>비밀번호를 잊으셨나요? </br>회원가입시 입력한 아래의 정보를 입력해 주세요.</p>
			<form action="<%=contextPath%>/member/forgotPwPro.do" method="post"
				name="emailForm" novalidate="novalidate">
				<div class="input-wrap">
					<input type="text" id="id" name="id" placeholder="아이디를 입력하세요">
				</div>
				<div class="input-wrap">
					<input type="email" id="email" name="email"
						placeholder="이메일을 입력하세요">
				</div>
				<div class="input-wrap">
					<button type="submit" value="확인">확인</button>
				</div>
			</form>
		</div>
	</div>
</div>
<script>
document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('.form-wrap.find-idpw form');

    form.addEventListener('submit', function(event) {
        const idInput = document.getElementById('id');
        const emailInput = document.getElementById('email');

        const idValue = idInput.value.trim();
        const emailValue = emailInput.value.trim();

        if (idValue === '') {
            alert('아이디를 입력해주세요.');
            event.preventDefault();
            idInput.focus();
            return;
        }

        if (emailValue === '') {
            alert('이메일을 입력해주세요.');
            event.preventDefault();
            emailInput.focus();
            return;
        }
    });
});
</script>