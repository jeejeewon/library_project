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
    <!-- 스타일 추가 -->
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
            padding: 20px;
        }
        
        .board-head{
        	width:80%;
        	display: flex;
        }
        .board-head p{
        	margin-left: 10px;
        }
        
        .search-form {
	        display: flex;
	        justify-content: center;
	        align-items: center;
	        margin-bottom: 20px;
	    }
	
	    .search-select {
	        padding: 8px;
	        margin-right: 10px;
	        font-size: 16px;
	        border-radius: 5px;
	        border: 1px solid #ddd;
	    }
	
	    .search-input {
	        padding: 8px;
	        font-size: 16px;
	        margin-right: 10px;
	        border-radius: 5px;
	        border: 1px solid #ddd;
	        width: 250px;
	    }
	
	    .search-btn {
	        padding: 8px 16px;
	        background-color: #007bff;
	        color: white;
	        font-size: 16px;
	        border-radius: 5px;
	        border: none;
	        cursor: pointer;
	    }
	
	    .search-btn:hover {
	        background-color: #0056b3;
	    }
	
	    .search-btn:focus {
	        outline: none;
	    }

        table {
            width: 80%;
            margin: 20px auto;
            border-collapse: collapse;
            background-color: #ffffff; /* 테이블 배경을 흰색으로 변경 */
        }

        table th, table td {
            padding: 10px;
            text-align: center;
            border: 1px solid #ddd;
        }

        table th {
            background-color: #f2f2f2;
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
		
		.board-topbar{
			display: flex;
			justify-content: space-between;
			align-items: center;
			width: 80%
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
    	<div class="board-head">
    		<h2>공지사항</h2>
    		<p>도서관소식 > 공지사항</p>
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
    	</form>
    	
    	<div class="board-topbar">
    		<!-- 총 게시글 수 표시 -->
    		<p class="totalCount">총 ${totalBoardCount}건, ${pageNum}/${totalPage}페이지</p>
        	<!-- 글쓰기 버튼 -->
        	<a href="${contextPath}/bbs/noticeWrite.do" class="write-btn">글쓰기</a>
	        <!--  아래는 운영자에게만 글쓰기 버튼이 보이게하는 코드.. 추후 이걸로 사용할 예정
			<c:if test="${sessionScope.userId == 'admin'}">
	    		<a href="${contextPath}/bbs/noticeWrite.do" class="write-btn">글쓰기</a>
			</c:if>
			-->
    	</div>

        <!-- 공지사항 리스트 테이블 -->
        <table>
            <tr height="20" align="center" bgcolor="lightgray">
                <td>No</td>
                <td>제목</td>
                <td>작성자</td>
                <td>작성일</td>
                <td>조회수</td>
                <td>첨부파일</td>
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
                    <td>${boardVo.file}</td>
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
    </center>
</body>
</html>
