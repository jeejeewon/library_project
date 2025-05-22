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
    <title>공지사항 리스트 - noticeList.jsp</title>

  <style>
.board-list-area {
    max-width: 1000px;
    margin: 20px auto;
    padding: 20px;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
}

.board-head h2 {
    font-size: 24px;
    color: #003c83;
    font-weight: bold;
    padding-bottom: 10px;
    margin-bottom: 20px;
    text-align: center;
}

.search-form {
    display: flex;
    gap: 10px;
    margin-bottom: 20px;
    align-items: center;
}

.search-select {
    padding: 8px;
    border: 1px solid #ccc;
    border-radius: 4px;
    font-size: 14px;
}

.search-input {
    flex-grow: 1;
    padding: 8px;
    border: 1px solid #ccc;
    border-radius: 4px;
    font-size: 14px;
}

.search-btn {
    padding: 8px 15px;
    background-color: #003c83;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 14px;
    transition: background-color 0.3s ease;
}

.search-btn:hover {
    background-color: #002c66;
}

.board-topbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
    padding-bottom: 10px;
    border-bottom: 1px solid #eee;
}

.totalCount {
    font-size: 14px;
    color: #555;
    margin: 0;
}

.write-btn {
    display: inline-block;
    padding: 8px 15px;
    background-color: #003c83;
    color: white;
    text-decoration: none;
    border-radius: 4px; 
    font-size: 14px; 
    transition: background-color 0.3s ease;
}

.write-btn:hover {
    background-color: #002c66;
}


.board-list-area table {
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 20px;
    able-layout: fixed;
}

.board-list-area th, .board-list-area td {
    padding: 12px;
    border-bottom: 1px solid #eee; 
    text-align: center;
    font-size: 14px;
    color: #333;
}

.first-tr{
	background-color: #f4f4f4;
	font-weight: bold;
}


.board-list-area th:nth-child(1), 
.board-list-area td:nth-child(1) {
    width: 5%;
}

.board-list-area th:nth-child(2),
.board-list-area td:nth-child(2) { 
    width: auto;
    text-align: left;
    flex-grow: 1;
}

.board-list-area th:nth-child(3),
.board-list-area td:nth-child(3) {
    width: 15%;
}
.board-list-area th:nth-child(4),
.board-list-area td:nth-child(4) {
     width: 15%;
}
.board-list-area th:nth-child(5),
.board-list-area td:nth-child(5) {
     width: 8%;
}

.board-list-area th:nth-child(6),
.board-list-area td:nth-child(6) {
    width: 5%;
    text-align: center;
}


.board-list-area td a {
    text-decoration: none;
    color: #333;
    display: block;
    padding: 0;
    margin: 0;
    cursor: pointer;
}


.board-list-area td svg { 
    vertical-align: middle; 
}


.board-list-area td[colspan="6"] {
    font-style: italic;
    color: #777;
    padding: 30px; 
}


.pagination-wrapper {
    display: flex;
    justify-content: center;
    margin-top: 20px; 
}

.pagination {
    display: flex;
    list-style: none; 
    padding: 0; 
    margin: 0; 
}

.page-item {
    margin: 0 4px;
}

.page-link {
    display: block;
    padding: 8px 12px; 
    border: 1px solid #ccc; 
    border-radius: 4px;
    color: #003c83; 
    text-decoration: none; 
    transition: background-color 0.3s ease;
}

.page-item.active .page-link {
    background-color: #003c83; 
    color: white; 
    border-color: #003c83;
    pointer-events: none;
}


.page-link:hover:not(.active .page-link) {
    background-color: #f2f2f2; 
    border-color: #b3b3b3;
    color: #002c66;
}

.page-link span[aria-hidden="true"] {
    font-weight: bold;
}

    </style>

</head>
<body>
    	<section class="board-list-area">
    		<div class="board-head">
    		<h2>공지사항</h2>
    	</div>
		<!-- 검색 기능의 폼 태그 -->
		<form action="${contextPath}/bbs/noticeList.do" method="get" class="search-form">
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
			<c:if test="${sessionScope.id == 'admin'}">
	    		<a href="${contextPath}/bbs/noticeWrite.do" class="write-btn">글쓰기</a>
			</c:if>
    	</div>

        <!-- 공지사항 리스트 테이블 -->
        <table>
            <tr height="20" align="center" class="first-tr">
                <td>No</td>
                <td>제목</td>
                <td>작성자</td>
                <td>작성일</td>
                <td>조회수</td>
                <td>첨부</td>
            </tr>
            <!-- 등록된 공지사항이 없을 때 표시 -->
            <c:if test="${empty boardList}">
                <tr>
                    <td colspan="6" align="center">📭 등록된 공지사항이 없습니다.</td>
                </tr>
            </c:if>
            <!-- 공지사항 리스트 출력 -->
            <c:forEach var="boardVo" items="${boardList}" varStatus="status">
                <tr height="20" align="center">
                    <!-- 게시글 번호 계산 (현재 페이지와 총 게시글 수를 고려) -->
                    <td>${totalBoardCount - (pageNum - 1) * 10 - status.count + 1}</td>
                    <td>
                        <a href="${contextPath}/bbs/noticeInfo.do?boardId=${boardVo.boardId}" class="">
                            ${boardVo.title}
                        </a>
                    </td>
                    <td>${boardVo.userId}</td>
                    <td><fmt:formatDate value="${boardVo.createdAt}" pattern="yyyy-MM-dd" /></td>
                    <td>${boardVo.views}</td>
                    <td>
					    <c:choose>
					        <c:when test="${not empty boardVo.file}">
					            💾
					        </c:when>
					        <c:otherwise>  </c:otherwise>
					    </c:choose>
					</td>
                </tr>
            </c:forEach>
        </table>

        <!-- 페이지네이션 -->
        <div class="pagination-wrapper">
            <nav aria-label="Page navigation">
                <ul class="pagination">
                    <!-- 이전 버튼 -->
                    <c:if test="${section > 1}">
                        <li class="page-item">
                            <a class="page-link" href="${contextPath}/bbs/noticeList.do?section=${section-1}&pageNum=1" aria-label="Previous">
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
                                <a class="page-link" href="${contextPath}/bbs/noticeList.do?section=${section}&pageNum=${page}">
                                    ${page}
                                </a>
                            </li>
                        </c:if>
                    </c:forEach>

                    <!-- 다음 버튼 -->
                    <c:if test="${section < totalSection}">
                        <li class="page-item">
                            <a class="page-link" href="${contextPath}/bbs/noticeList.do?section=${section+1}&pageNum=1" aria-label="Next">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>
                    </c:if>
                </ul>
            </nav>
        </div>
    	</section>
</body>
</html>
