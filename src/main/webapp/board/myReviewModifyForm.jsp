<%@page import="VO.boardVO"%>
<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<%
request.setCharacterEncoding("UTF-8");
%>

<html>
<head>

<title>내서평 글수정 - myReviewModifyForm.jsp</title>

<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<style>
.file-upload-label {
	display: inline-block;
	margin-right: 10px;
	font-weight: bold;
	background-color: #f0f0f0;
	padding: 5px 10px;
}

.file-name {
	font-style: italic;
	color: #333;
}

.file-input {
	display: none; /* 커스텀 라벨만 보이게 하려면 숨김 */
}

.form-title {
	display: flex;
	justify-content: space-between;
}
</style>

</head>
<body>
	<center>
		<form name="myReviewWriteForm" method="post" action="${contextPath}/bbs/myReviewModify.do" enctype="multipart/form-data">
			<!-- 수정 대상 게시글 ID 전달 -->
			<input type="hidden" name="boardId" value="${board.boardId}">
		
			<div class="form-title">
				<h2>공지사항 글 수정</h2>
				<div>
					<button type="button" onclick="location.href='${contextPath}/bbs/myReviewInfo.do?boardId=${board.boardId}'">취소</button>
					<input type="submit" value="수정">
				</div>
			</div>
		
			<table align="center" border="1">
				<tr>
					<td>
						<input type="text" name="title" placeholder="제목을 입력하세요" style="width: 100%;" value="${board.title}" onfocus="this.select()">
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<textarea name="content" rows="10" cols="50" placeholder="내용을 입력하세요" style="width: 100%;">${board.content}</textarea>
					</td>
				</tr>
			</table>
		</form>
	</center>

</body>
</html>
