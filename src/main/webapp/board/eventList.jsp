<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
    <!-- 스타일 추가 -->
<style>
    /* 기본 body 스타일 */
    body {
        font-family: Arial, sans-serif;
        background-color: #f4f4f9;
        padding: 20px;
        margin: 0;
    }
	
	
	.board-topbar{
			display: flex;
			justify-content: space-between;
			align-items: center;
			width: 80%;
			margin-bottom: 25px;
		}
		
	    .board-head{
        	width:80%;
        	display: flex;
        	align-items: center;
        }
        .board-head h2{
        	font-size: 20px;
        }
        .board-head p{
        	margin-left: 10px;
        }
        
    /* 행사 카드 스타일 */
    .event-card {
        width: 80%;
        max-width: 1440px;
        display: flex;
        flex-wrap: wrap;
/*		justify-content: space-between; */
		justify-content: flex-start;
		gap: 3rem;
        margin: 0 auto;
    }

    .non-event {
        width: 100%;
        text-align: center;
    }

    /* 카드 기본 스타일 */
    .card {
     	flex-basis: calc((100% - 2 * 3rem) / 3);
        position: relative;
        background-color: #fff;
        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        border-radius: 8px;
        overflow: hidden;
        transition: transform 0.3s ease, box-shadow 0.3s ease;
    }

    /* 이미지 비율을 4:3으로 유지하기 위한 스타일 */
    .card img {
        width: 100%;
        height: auto;
        object-fit: cover;
        aspect-ratio: 4 / 3; /* 4:3 비율 유지 */
    }

    .card p {
        padding: 10px;
        font-size: 16px;
        font-weight: bold;
        color: #333;
    }

    .card p.date {
        font-size: 14px;
        color: #888;
    }

    .card:hover {
        transform: translateY(-5px);
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
    }

    /* 반응형 스타일 */
    /* PC에서 최대 4개 */
    @media (min-width: 1024px) {
        .card {
            width: calc(25% - 20px); /* 4개 열 */
        }
    }

    /* 태블릿에서는 3개로 설정 */
    @media (max-width: 1023px) and (min-width: 768px) {
        .card {
            width: calc(33.33% - 20px); /* 3개 열 */
        }
    }

    /* 모바일에서는 2개로 설정 */
    @media (max-width: 767px) and (min-width: 480px) {
        .card {
            width: calc(50% - 20px); /* 2개 열 */
        }
    }

    /* 초소형 모바일에서는 1개씩 */
    @media (max-width: 479px) {
        .card {
            width: 100%; /* 1개 열 */
        }
    }
    
    
           .pagination-wrapper {
            text-align: center;
            margin-top: 20px;
        }

        .pagination {
            display: inline-block;
            padding-left: 0;
            list-style: none;
            background-color: transparent;
            border-radius: 0.25rem;
        }

        .page-item {
            display: inline;
        }

        .page-link {
            color: #007bff;
            text-decoration: none;
            padding: 10px 15px;
            border: 1px solid #ddd;
            border-radius: 3px;
            margin: 0 4px;
        }

        .page-link:hover {
            background-color: #f0f0f0;
        }

        .active .page-link {
            background-color: #007bff;
            color: white;
        }
		

		
		.write-btn {
            margin: 20px 0;
            padding: 10px 20px;
            background-color: #28a745;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }

        .write-btn:hover {
            background-color: #218838;
        }
		
</style>

</head>
<body>
    <center>
        <div class="board-topbar">
			<div class="board-head">
				<h2>행사안내</h2>
				<p>도서관소식 > 행사안내</p>
			</div>
			<!-- 총 게시글 수 표시 -->
			<p class="totalCount">총 ${totalBoardCount}건, ${pageNum}/${totalPage}페이지</p>
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
                    <a href="${contextPath}/bbs/eventInfo.do?boardId=${board.boardId}" class="card">
                        <div>
                            <img src="${contextPath}/download.do?boardId=${board.boardId}&bannerImg=${board.bannerImg}&type=banner" alt="배너 이미지">
                            <p>${board.title}</p>
                            <p class="date"><fmt:formatDate value="${board.createdAt}" pattern="yyyy-MM-dd" /></p>
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
                        <li class="page-item">
                            <a class="page-link" href="${contextPath}/bbs/eventList.do?section=${section-1}&pageNum=1" aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>
                    </c:if>

                    <!-- 페이지 번호 버튼들 -->
                    <c:forEach var="i" begin="${(section-1)*5 + 1}" end="${section*5}">
                        <!-- 각 페이지 번호 계산 -->
                        <c:set var="page" value="${i}" />

                        <!-- 페이지 번호가 전체 페이지 수를 넘지 않도록 설정 -->
                        <c:if test="${page <= totalPage}">
                            <li class="page-item ${pageNum == page ? 'active' : ''}">
                                <a class="page-link" href="${contextPath}/bbs/eventList.do?section=${section}&pageNum=${page}">
                                    ${page}
                                </a>
                            </li>
                        </c:if>
                    </c:forEach>

                    <!-- 다음 버튼 -->
                    <c:if test="${section < totalSection}">
                        <li class="page-item">
                            <a class="page-link" href="${contextPath}/bbs/eventList.do?section=${section+1}&pageNum=1" aria-label="Next">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>
                    </c:if>
                </ul>
            </nav>
        </div>
    </center>
</body>
</html>
