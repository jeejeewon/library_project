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
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
            padding: 20px;
            margin: 0;
        }

        .board-head {
            width: 80%;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .board-head p {
            margin-left: 10px;
            font-size: 18px;
            color: #666;
        }

        .search-form {
            display: flex;
            justify-content: center;
            align-items: center;
            margin-bottom: 20px;
        }

        .search-select, .search-input, .search-btn {
            padding: 8px;
            font-size: 16px;
            margin-right: 10px;
            border-radius: 5px;
            border: 1px solid #ddd;
        }

        .search-btn {
            background-color: #007bff;
            color: white;
            border: none;
            cursor: pointer;
        }

        .search-btn:hover {
            background-color: #0056b3;
        }

        .search-btn:focus {
            outline: none;
        }

        .board-topbar {
            display: flex;
            justify-content: space-between;
            width: 80%;
            margin-bottom: 20px;
        }

        .write-btn {
            padding: 10px 20px;
            background-color: #28a745;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }

        .write-btn:hover {
            background-color: #218838;
        }

        /* 행사 카드 스타일 */
        .event-card {
            display: flex;
            flex-wrap: wrap;
            justify-content: space-between;
            gap: 20px;
        }

        .card {
            width: 30%;
            background-color: #fff;
            border: 1px solid #ddd;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            text-align: center;
            transition: transform 0.3s;
        }

        .card img {
            width: 100%;
            height: 200px;
            object-fit: cover;
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

        /* 페이지네이션 스타일 */
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
    </style>
</head>
<body>
    <center>
        <div class="board-head">
            <h2>행사안내</h2>
            <p>도서관소식 > 행사안내</p>
        </div>

        <!-- 검색 기능의 폼 태그 -->
        <form action="${contextPath}/bbs/eventList.do" method="get" class="search-form">
            <select name="searchType" class="search-select">
                <option value="title" ${ searchType == 'title' ? 'selected' : '' }>제목</option>
                <option value="content" ${ searchType == 'content' ? 'selected' : '' }>내용</option>
                <option value="userId" ${ searchType == 'userId' ? 'selected' : '' }>작성자</option>
            </select>
            <input type="text" name="searchKeyword" class="search-input" placeholder="검색어를 입력하세요." value="${searchKeyword}">
            <button type="submit" class="search-btn">검색</button>
        </form>

        <div class="board-topbar">
            <!-- 총 게시글 수 표시 -->
            <p class="totalCount">총 ${totalBoardCount}건, ${pageNum}/${totalPage}페이지</p>
            <!-- 글쓰기 버튼 -->
            <a href="${contextPath}/bbs/eventWrite.do" class="write-btn">글쓰기</a>
        </div>

        <!-- 행사안내 리스트 카드 -->
        <div class="event-card">
            <!-- 글이 없을경우 -->
            <c:if test="${empty boardList}">
                <p align="center">📭 등록된 행사가 없습니다.</p>
            </c:if>

            <!-- 게시글 반복문 -->
            <c:forEach var="board" items="${boardList}">
                <c:if test="${not empty board.bannerImg}">
                    <!-- 배너 이미지가 있는 경우에만 -->
                    <a href="${contextPath}/bbs/noticeInfo.do?boardId=${board.boardId}">
                        <div class="card">
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
