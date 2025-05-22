<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!-- Context Path 설정 -->
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!-- 문자 인코딩 설정 -->
<%
request.setCharacterEncoding("UTF-8");
%>

<html>
<head>
<title>행사안내 리스트 - eventList.jsp</title>
<style>
body {
    margin: 0;
    font-family: 'Arial', sans-serif;
    line-height: 1.6;
    color: #333;
    background-color: white;
}

.board-list-area {
	margin: 0 auto;
	padding: 40px 0;
}

.board-topbar {
	display: flex;
	justify-content: center;
	align-items: center;
	width: 90%;
	max-width: 1440px;
	margin: 0 auto 30px auto;
}

.board-head {
	width: 100%;
	text-align: center;
}

.board-topbar h2 {
	font-size: 28px;
	color: #003c83;
	font-weight: bold;
	margin-bottom: 0;
}

.non-event {
	width: 100%;
	text-align: center;
	padding: 50px 0;
	font-size: 18px;
	color: #666;
}

.event-card {
	width: 90%;
	max-width: 1440px;
	margin: 0 auto;
	display: grid;
	grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
	gap: 30px;
}

.card {
	display: block;
	text-decoration: none;
	background-color: #fff;
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
	border-radius: 8px;
	overflow: hidden;
	transition: transform 0.3s ease, box-shadow 0.3s ease;
    color: inherit;
    max-width: 350px;
}

.card:hover {
	transform: translateY(-8px);
	box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

.card > div {
    display: flex;
    flex-direction: column;
}

.card img {
	width: 100%;
	height: 200px;
	object-fit: cover;
	display: block;
	margin-bottom: 15px;
}

.title-limit {
	padding: 0 15px;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    text-overflow: ellipsis;
    word-break: break-word;

    width: 100%;
    box-sizing: border-box;
    margin: 0;
    font-size: 18px;
    font-weight: bold;
    color: #333;
    line-height: 1.4;

    max-height: 50.4px;
}

.card p.date {
    font-size: 14px;
    color: #666;
    margin: 10px 0 0 0;
    padding: 0;
}

.pagination-wrapper {
	display: flex;
	justify-content: center;
	margin-top: 40px;
}

.pagination {
	display: flex;
	list-style: none;
	padding: 0;
	margin: 0;
}

.page-item {
	margin: 0 5px;
}

.page-link {
	display: block;
	padding: 10px 15px;
	border: 1px solid #ddd;
	border-radius: 4px;
	color: #003c83;
	text-decoration: none;
	transition: background-color 0.2s ease, border-color 0.2s ease;
}

.page-item.active .page-link {
	background-color: #003c83;
	color: white;
	border-color: #003c83;
	pointer-events: none;
}

.page-link:hover:not(.active .page-link) {
	background-color: #e9e9e9;
	border-color: #bbb;
	color: #002c66;
}

.page-link span[aria-hidden="true"] {
	font-weight: bold;
}

@media (max-width: 1200px) {
    .event-card {
        grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
        gap: 25px;
    }
}

@media (max-width: 768px) {
    .event-card {
        grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
        gap: 20px;
        width: 95%;
    }
    .board-topbar {
        width: 95%;
    }
    .card img {
        height: 160px;
    }
     .title-limit {
        font-size: 17px;
        line-height: 1.4;
        max-height: calc(17px * 1.4 * 2);
    }
}

@media (max-width: 480px) {
     .event-card {
        grid-template-columns: 1fr;
        gap: 20px;
        width: 95%;
    }
     .card img {
        height: 180px;
    }
    .board-topbar h2 {
        font-size: 24px;
    }
     .title-limit {
        font-size: 18px;
         max-height: calc(18px * 1.4 * 2);
    }
}

	
</style>

</head>
<body>
	<section class="board-list-area">
		<div class="board-topbar">
			<div class="board-head">
				<h2>행사안내</h2>
			</div>
		</div>

		<!-- 행사안내 리스트 카드 -->
		<div class="event-card">
			<!-- 글이 없을경우 -->
			<c:if test="${empty boardList}">
				<p class="non-event">📭 등록된 행사가 없습니다.</p>
			</c:if>

			<!-- 게시글 반복문 -->
			<c:forEach var="board" items="${boardList}">
				<c:if test="${not empty board.bannerImg}">
					<!-- 배너 이미지가 있는 경우에만 -->
					<a href="${contextPath}/bbs/eventInfo.do?boardId=${board.boardId}"
						class="card">
						<div>
							<img
								src="${contextPath}/download.do?boardId=${board.boardId}&bannerImg=${board.bannerImg}&type=banner"
								alt="배너 이미지">
							<p class="title-limit">${board.title}</p>
							<p class="date">
								<fmt:formatDate value="${board.createdAt}" pattern="yyyy-MM-dd" />
							</p>
						</div>
					</a>
				</c:if>
			</c:forEach>
		</div>

		<!-- 페이지네이션 -->
		<div class="pagination-wrapper">
			<nav aria-label="Page navigation">
				<ul class="pagination">
					<!-- 이전 버튼 -->
					<c:if test="${section > 1}">
						<li class="page-item"><a class="page-link"
							href="${contextPath}/bbs/eventList.do?section=${section-1}&pageNum=1"
							aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
						</a></li>
					</c:if>

					<!-- 페이지 번호 버튼들 -->
					<c:forEach var="i" begin="${(section-1)*5 + 1}" end="${section*5}">
						<!-- 각 페이지 번호 계산 -->
						<c:set var="page" value="${i}" />

						<!-- 페이지 번호가 전체 페이지 수를 넘지 않도록 설정 -->
						<c:if test="${page <= totalPage}">
							<li class="page-item ${pageNum == page ? 'active' : ''}"><a
								class="page-link"
								href="${contextPath}/bbs/eventList.do?section=${section}&pageNum=${page}">
									${page} </a></li>
						</c:if>
					</c:forEach>

					<!-- 다음 버튼 -->
					<c:if test="${section < totalSection}">
						<li class="page-item"><a class="page-link"
							href="${contextPath}/bbs/eventList.do?section=${section+1}&pageNum=1"
							aria-label="Next"> <span aria-hidden="true">&raquo;</span>
						</a></li>
					</c:if>
				</ul>
			</nav>
		</div>
	</section>
</body>
</html>
