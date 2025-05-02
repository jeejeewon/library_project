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
<title>공지사항 상세페이지 - noticeInfo.jsp</title>
</head>
<body>
	<center>
		<div class="board-info-top">
			<div class="paging">
				<form action="/bbs/viewNotice.do" method="get">
					<input type="hidden" name="boardId" value="${boardId-1}">
					<button type="submit">이전글</button>
				</form>
				<form action="/bbs/viewNotice.do" method="get">
					<input type="hidden" name="boardId" value="${boardId+1}">
					<button type="submit">다음글</button>
				</form>
				<form action="/bbs/noticeList.do" method="get">
					<button type="submit">목록</button>
				</form>
			</div>
		</div>
		<div class="title-area">
			<div class="title-area-left">
				<p>게시글 제목 영역</p>
				<p>유저아이디 영역</p>
			</div>
			<div class="title-area-right">
				<p>게시글 작성일 영역</p>
				<p>게시글 조회수 영역</p>
			</div>
			<div class="content-area">
				<p>게시글 내용 영역</p>
				<div class="file">
					<a>첨부파일</a>첨부파일이름
				</div>
			</div>
		</div>
		<div class="board-info-bottom">
			<div class="board-info-bottom-left">
				<a>수정</a>
				<a>삭제</a>
			</div>
			<div class="board-info-bottom-right">
				<form action="/bbs/noticeList.do" method="get">
					<button type="submit">목록</button>
				</form>
				<button onclick="scrollToTop()">TOP</button>
			</div>
		</div>


	</center>
</body>
</html>