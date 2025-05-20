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

<%
    // 세션에서 'fromPage' 값을 가져오기
    String fromPage = (String) session.getAttribute("fromPage");
%>

<html>
<head>
	<title>행사안내 상세페이지 - eventInfo.jsp</title>
	<style>
		/* 기본 리셋 */
		* {
			margin: 0;
			padding: 0;
			box-sizing: border-box;
		}

		body {
			font-family: 'Arial', sans-serif;
			background-color: #f4f7fc;
			color: #333;
			line-height: 1.6;
		}

		/* 중앙 정렬을 위한 스타일 */
		.center-wrapper {
			max-width: 960px;
			margin: 30px auto;
			padding: 20px;
			background-color: #fff;
			border-radius: 8px;
			box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
		}

		/* 제목 영역 스타일 */
		.title-area {
			display: flex;
			justify-content: space-between;
			border-bottom: 2px solid #eee;
			padding-bottom: 20px;
			margin-bottom: 20px;
		}

		.title-area .left, .title-area .right {
			width: 48%;
		}

		.title-area .left p, .title-area .right p {
			font-size: 18px;
			font-weight: bold;
		}

		/* 게시글 내용 영역 스타일 */
		.content-area {
			margin-bottom: 30px;
		}

		.content-area p {
			font-size: 16px;
			line-height: 1.8;
			color: #555;
		}
		
		
		

	    /* 첨부파일 영역 스타일 */
	    .banner-img{
			text-align:center;
			width: 100%;
		}
	    .banner-img img{
			width: 100%;
			max-width: 250px
		}
	    
		.file {
		    margin-top: 20px;
		    padding: 15px;
		    background-color: #f9f9f9;
		    border-radius: 8px;
		    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
		    display: flex;
		    align-items: center;
		    gap: 15px;
		}
		
		.file img {
		    max-width: 150px;
		    border-radius: 8px;
		    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
		}
		
		.file a.download-link {
		    font-size: 14px;
		    color: white;
		    background-color: #007bff;
		    text-decoration: none;
		    font-weight: bold;
		    padding: 8px 16px;
		    border-radius: 4px;
		    display: inline-block;
		    margin-left: 10px;
		    transition: background-color 0.3s ease, color 0.3s ease;
		    text-align: center;
		}
		
		.file a.download-link:hover {
		    background-color: #0056b3;
		    color: #fff;
		}
		
		.file span {
		    font-size: 14px;
		    color: #555;
		    margin-left: 10px;
		}


		/* 페이징 버튼 */
		.paging {
			display: flex;
			justify-content: flex-end;
			gap: 5px;
			margin-bottom: 20px;
		}

		.paging form button {
			padding: 10px 20px;
			border: none;
			background-color: #f4f7fc;
			color: #007bff;
			cursor: pointer;
			font-size: 14px;
			border-radius: 4px;
		}

		.paging form button:hover {
			background-color: #e0e0e0;
		}
	
		.board-info-bottom{
			display: flex;
            justify-content: space-between;
		}
		
		/* '수정', '삭제', '목록', 'TOP' 버튼 스타일 */
		.board-info-bottom a,
		.board-info-bottom button,
		.board-info-bottom form button {
			padding: 8px 16px;
			font-size: 14px;
			border-radius: 4px;
			background-color: #007bff;
			color: white;
			border: none;
			cursor: pointer;
			text-decoration: none;
			margin: 0 5px;
		}
        .board-info-bottom a:hover,
		.board-info-bottom button:hover,
		.board-info-bottom form button:hover {
			background-color: #0056b3;
		}

		/* 버튼을 가로로 정렬 */
		.board-info-bottom-left,
		.board-info-bottom-right {
			display: flex;
			justify-content: flex-start;
			gap: 10px;
		}

		.board-info-bottom-right {
			justify-content: flex-end;
		}

		/* 반응형 디자인 */
		@media screen and (max-width: 768px) {
			.title-area {
				flex-direction: column;
				align-items: center;
			}

			.title-area .left,
			.title-area .right {
				width: 100%;
				text-align: center;
				margin-bottom: 10px;
			}

			.board-info-bottom {
				flex-direction: column;
				align-items: center;
			}

			.board-info-bottom-left,
			.board-info-bottom-right {
				justify-content: center;
			}

			.paging {
				flex-direction: column;
				align-items: center;
				gap: 10px;
			}
		}
	</style>
</head>

<body>

	<div class="center-wrapper">
	
		<div class="paging">
		    <!-- 이전글 버튼 (이전글이 있을 경우만 표시) -->
		    <c:if test="${getPreBoardId > 0}">
		        <form action="${preBoardUrl}" method="get">
		            <input type="hidden" name="boardId" value="${getPreBoardId}">
		            <input type="hidden" name="fromPage" value="${fromPage}">
		            <button type="submit">이전글</button>
		        </form>
		    </c:if>
			
		    <!-- 다음글 버튼 (다음글이 있을 경우만 표시) -->
		    <c:if test="${getNextBoardId > 0}">
		        <form action="${nextBoardUrl}" method="get">
		            <input type="hidden" name="boardId" value="${getNextBoardId}">
		            <input type="hidden" name="fromPage" value="${fromPage}">
		            <button type="submit">다음글</button>
		        </form>
		    </c:if>
			
			<!-- 목록 버튼 -->
		    <c:choose>
		        <c:when test="${fromPage == 'eventList'}">
		            <form action="${contextPath}/bbs/eventList.do" method="get">
		                <button type="submit">목록</button>
		            </form>
		        </c:when>
		        <c:when test="${fromPage == 'eventList'}">
		            <form action="${contextPath}/bbs/eventList.do" method="get">
		                <button type="submit">목록</button>
		            </form>
		        </c:when>
		        <c:otherwise>
		            <form action="${contextPath}/bbs/eventList.do" method="get">
		                <button type="submit">목록</button>
		            </form>
		        </c:otherwise>
		    </c:choose>

		</div>

		<div class="title-area">
			<div class="left">
				<p>게시글 제목: ${board.title}</p>
				<p>작성자: ${board.userId}</p>
			</div>
			<div class="right">
                <!-- 작성일 출력 시 포맷팅 처리 -->
                <p>작성일: <fmt:formatDate value="${board.createdAt}" pattern="yyyy-MM-dd HH:mm" /></p>
                <p>조회수: ${board.views}</p>
			</div>
		</div>

		<div class="content-area">
			<c:if test="${not empty board.bannerImg}"><!-- 배너이지미파일이 있을 경우에만 나타나도록 설정 -->
				<div class="banner-img">
					<img src="${contextPath}/download.do?boardId=${board.boardId}&bannerImg=${board.bannerImg}&type=banner" id="preview2" alt="배너 이미지">
				</div>
			</c:if>
			<p>${board.content}</p>
			<c:if test="${not empty board.file}"><!-- 첨부파일이 있을 경우에만 나타나도록 설정 -->
				<div class="file">
					<img src="${contextPath}/download.do?boardId=${board.boardId}&file=${board.file}&type=file" id="preview1" alt="첨부 이미지">
					<a href="${contextPath}/download.do?boardId=${board.boardId}&file=${board.file}&type=file" download class="download-link">첨부파일</a>
					<span>${board.file}</span>
				</div>
			</c:if>
		</div>

		<div class="board-info-bottom">
			<div class="board-info-bottom-left">
			</div>
			<div class="board-info-bottom-right">
				<form action="${pageContext.request.contextPath}/bbs/eventList.do" method="get">
					<button type="submit">목록</button>
				</form>
				<button onclick="scrollToTop()">TOP</button>
			</div>
		</div>
	</div>
</body>
</html>


