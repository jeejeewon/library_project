<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setCharacterEncoding("UTF-8");
    String contextPath = request.getContextPath();
    String userId = (String) session.getAttribute("id");
    if (userId == null || !"admin".equals(userId)) {
        response.sendRedirect(contextPath + "/members/login.jsp");
        return;
    }
%>
<div class="content-box">
	<div class="title">
		<h2>관리자 도서 관리</h2>
	</div>

	<div class="toolbar">
		<a href="<%= contextPath %>/main.jsp" class="btn-main">메인으로</a>
	</div>

	<br>
	<br>

	<div class="grid">
		<div class="card">
			<h3>📕 도서 등록</h3>
			<p>신규 입고 도서 추가</p>
			<a href="<%= contextPath %>/books/addBookForm.do" class="btn">등록하기</a>
		</div>

		<div class="card">
			<h3>📘 도서 수정/삭제</h3>
			<p>기존 도서 정보 수정 삭제</p>
			<a href="<%= contextPath %>/books/updateBook.do" class="btn">관리하기</a>
		</div>

		<div class="card">
			<h3>📙 반납 처리</h3>
			<p>대여 중인 도서의 반납 관리</p>
			<a href="<%= contextPath %>/books/returnBook.do" class="btn">반납
				관리</a>
		</div>

		<div class="card">
			<h3>📗 전체 대여 내역</h3>
			<p>모든 사용자의 대여 기록 확인</p>
			<a href="<%= contextPath %>/books/allRental.do" class="btn">내역 보기</a>
		</div>
	</div>
</div>