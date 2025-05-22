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
    border-bottom: 2px solid #003c83
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

/* 총 게시글 수 및 글쓰기 버튼 영역 스타일 */
.board-topbar {
    display: flex; /* 자식 요소 가로 정렬 */
    justify-content: space-between; /* 자식 요소 양쪽 끝으로 벌리기 */
    align-items: center; /* 세로 중앙 정렬 */
    margin-bottom: 15px; /* 아래 여백 */
    padding-bottom: 10px; /* 하단 여백 */
    border-bottom: 1px solid #eee; /* 하단 구분선 */
}

.totalCount {
    font-size: 14px; /* 글자 크기 */
    color: #555; /* 글자색 */
    margin: 0; /* 기본 마진 제거 */
}

/* *** 수정 사항 4, 5번 반영: 글쓰기 버튼 색상 변경 *** */
.write-btn {
    display: inline-block; /* 버튼처럼 보이도록 */
    padding: 8px 15px; /* 안쪽 여백 */
    background-color: #003c83; /* 배경색 #003c83 */
    color: white; /* 글자색 */
    text-decoration: none; /* 밑줄 제거 */
    border-radius: 4px; /* 모서리 둥글게 */
    font-size: 14px; /* 글자 크기 */
    transition: background-color 0.3s ease; /* 호버 효과 */
}

.write-btn:hover {
    background-color: #002c66;
}

/* 게시글 목록 테이블 스타일 */
.board-list-area table {
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 20px;
}

.board-list-area th, .board-list-area td {
    padding: 12px; /* 셀 안쪽 여백 */
    border-bottom: 1px solid #eee; /* 하단 경계선 */
    text-align: center; /* 가운데 정렬 */
    font-size: 14px; /* 글자 크기 */
    color: #333; /* 글자색 */
}

.first-tr{
	background-color: #f4f4f4;
	font-weight: bold;
}


/* *** 수정 사항 2번 반영: 컬럼 너비 조정 (No, 제목, 첨부파일) *** */
/* table th 또는 table td 에 적용하면 됨. nth-child(컬럼순서) 선택자 사용 */
.board-list-area th:nth-child(1), /* No 컬럼 헤더 */
.board-list-area td:nth-child(1) { /* No 컬럼 셀 */
    width: 5%; /* 너비 작게 (예시) */
}

.board-list-area th:nth-child(2), /* 제목 컬럼 헤더 */
.board-list-area td:nth-child(2) { /* 제목 컬럼 셀 */
    width: auto; /* 또는 flex-grow 같은 속성으로 남은 공간 대부분 차지 */
    text-align: left; /* 제목은 왼쪽 정렬이 일반적이지만, 가운데 정렬 유지도 가능 */
    flex-grow: 1; /* Flexbox는 아니지만, 개념적으로 남은 공간을 차지 */
    /* 테이블 레이아웃은 Flexbox와 달라서 너비% 또는 auto 사용 */
}

.board-list-area th:nth-child(3), /* 작성자 컬럼 */
.board-list-area td:nth-child(3) {
    width: 15%; /* 너비 설정 (예시) */
}
.board-list-area th:nth-child(4), /* 작성일 컬럼 */
.board-list-area td:nth-child(4) {
     width: 15%; /* 너비 설정 (예시) */
}
.board-list-area th:nth-child(5), /* 조회수 컬럼 */
.board-list-area td:nth-child(5) {
     width: 8%; /* 너비 설정 (예시) */
}

.board-list-area th:nth-child(6), /* 첨부파일 컬럼 헤더 */
.board-list-area td:nth-child(6) { /* 첨부파일 컬럼 셀 */
    width: 5%; /* *** No 컬럼과 같은 크기로 작게 (예시) *** */
    text-align: center; /* 가운데 정렬 유지 */
}


/* *** 수정 사항 3번 반영: 제목 링크 호버 시 스타일 변경 제거 *** */
.board-list-area td a {
    text-decoration: none; /* 밑줄 제거 */
    color: #333; /* 글자색 */
    display: block; /* 영역 전체 클릭 가능하도록 */
    padding: 0; /* 기본 패딩 제거 */
    margin: 0; /* 기본 마진 제거 */
    cursor: pointer; /* *** 커서 포인터 유지! *** */
    /* 호버 시 밑줄 및 색상 변경 규칙 제거 */
}

/* 첨부파일 아이콘 스타일 */
.board-list-area td svg { /* SVG 아이콘 사용하는 경우 */
    vertical-align: middle; /* 세로 중앙 정렬 */
}

/* 등록된 공지사항 없을 때 메시지 */
.board-list-area td[colspan="6"] {
    font-style: italic; /* 이탤릭체 */
    color: #777; /* 회색 글자색 */
    padding: 30px; /* 여백 크게 줘서 중앙에 오도록 */
}


/* 페이지네이션 영역 스타일 */
.pagination-wrapper {
    display: flex; /* 페이지네이션 가운데 정렬을 위해 Flexbox 사용 */
    justify-content: center; /* 가운데 정렬 */
    margin-top: 20px; /* 위 여백 */
}

.pagination {
    display: flex; /* 페이지 번호들을 가로로 나란히 */
    list-style: none; /* 기본 리스트 스타일 제거 */
    padding: 0; /* 기본 패딩 제거 */
    margin: 0; /* 기본 마진 제거 */
}

.page-item {
    margin: 0 4px; /* 페이지 번호 간 간격 */
}

/* *** 수정 사항 4, 5번 반영: 페이지 번호 링크 및 활성/호버 색상 변경 *** */
.page-link {
    display: block; /* 링크 영역 전체 클릭 가능하도록 */
    padding: 8px 12px; /* 안쪽 여백 */
    border: 1px solid #ccc; /* 테두리 */
    border-radius: 4px; /* 모서리 둥글게 */
    color: #003c83; /* 글자색 #003c83 */
    text-decoration: none; /* 밑줄 제거 */
    transition: background-color 0.3s ease; /* 호버 효과 */
}

.page-item.active .page-link {
    background-color: #003c83; /* 활성 페이지 배경색 #003c83 */
    color: white; /* 활성 페이지 글자색 */
    border-color: #003c83; /* 활성 페이지 테두리색 #003c83 */
    pointer-events: none; /* 활성 페이지 클릭 막기 */
}

/* *** 수정 사항 5번 반영: 페이지 번호 링크 호버 색상 변경 *** */
.page-link:hover:not(.active .page-link) { /* 활성 페이지가 아닐 때만 호버 효과 */
    background-color: #f2f2f2; /* 호버 시 배경색 변경 (헤더 배경색과 유사하게) */
    border-color: #b3b3b3; /* 호버 시 테두리색 변경 */
    color: #002c66; /* 호버 시 글자색 변경 */
}

/* 이전/다음 버튼 텍스트 스타일 */
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
                        <a href="${contextPath}/bbs/noticeInfo.do?boardId=${boardVo.boardId}">
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
