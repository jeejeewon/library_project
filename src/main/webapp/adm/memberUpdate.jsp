<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="Vo.MemberVo"%>
<%
String contextPath = request.getContextPath();
MemberVo member = (MemberVo) request.getAttribute("member");

// 회원 정보가 없으면 (예: 잘못된 아이디로 접근) 에러 메시지 표시나 다른 처리 필요
if (member == null) {
    // 간단하게 에러 메시지 출력 후 종료하거나, 에러 페이지로 리다이렉트
    out.println("<script>alert('회원 정보를 불러오는데 실패했습니다.'); history.back();</script>");
    return; // JSP 실행 중단
}
%>

<div class="member-update">
	<h3>회원 정보 수정</h3>

	<form action="<%=contextPath%>/admin/memberUpdatePro" method="post">
		<div class="form-group">
			<label for="id">아이디</label>
			<%= member.getId() %>
			<input type="hidden" id="id" name="id" value="<%= member.getId() %>">
		</div>

		<div class="form-group">
			<label for="pass">비밀번호</label> <input type="password" id="pass"
				name="pass" value="<%= member.getPass() %>">
		</div>

		<div class="form-group">
			<label for="name">이름</label> <input type="text" id="name"
				name="name" value="<%= member.getName() %>">
		</div>

		<div class="form-group">
			<label for="gender">성별</label> <input type="text" id="gender"
				name="gender" value="<%= member.getGender() %>">
		</div>

		<div class="form-group">
			<label for="address">주소</label> <input type="text" id="address"
				name="address" value="<%= member.getAddress() %>">
		</div>

		<div class="form-group">
			<label for="email">이메일</label> <input type="email" id="email"
				name="email" value="<%= member.getEmail() %>">
		</div>

		<div class="form-group">
			<label for="tel">전화번호</label> <input type="tel" id="tel" name="tel"
				value="<%= member.getTel() %>">
		</div>

		<div class="form-group">
			<label>가입일</label>
			<%= member.getJoinDate() %>
		</div>
		<div class="form-group">
			<label>카카오ID</label>
			<%= member.getKakaoId() != null ? member.getKakaoId() : "없음" %>
		</div>


		<br>
		<div>
			<button type="submit">정보 수정</button>
			<button type="button"
				onclick="location.href='<%=contextPath%>/admin/memberSearch'">취소</button>
		</div>
	</form>
</div>
