<%-- bookReviewList.jsp 파일 내용 (수정 제안! - 행 클릭 가능하게) --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<html>
<head>
    <title>서평 리스트 - bookReviewList.jsp</title>
    <script src="https://code.jquery.com/jquery-latest.min.js"></script>

    <style>
        table {
            width: 80%;
            margin: 20px auto;
            border-collapse: collapse;
            background-color: #ffffff;
            table-layout: fixed;
        }
        table th, table td {
            padding: 10px;
            text-align: center;
            border: 1px solid #ddd;
            word-wrap: break-word;
        }
        table th { background-color: #f2f2f2; }

        /* 각 셀 너비 */
        table th:nth-child(1), table td:nth-child(1) { width: 10%; } /* 작성자 */
        table th:nth-child(2), table td:nth-child(2) { width: 30%; } /* 제목 */
        table th:nth-child(3), table td:nth-child(3) { width: 50%; } /* 내용 */
        table th:nth-child(4), table td:nth-child(4) { width: 10%; } /* 작성일 */


        .clickable-row {
            cursor: pointer;
        }
        .clickable-row:hover {
            background-color: #f9f9f9;
        }

        /* 제목 한 줄, 내용 두 줄 제한 */
        .title-cell {
            white-space: nowrap; overflow: hidden; text-overflow: ellipsis;
        }
        .content-clamp {
             overflow: hidden; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical;
        }
    </style>
</head>
<body>
    <center>
		<table>
		    <thead>
			    <tr height="20" align="center" bgcolor="lightgray">
			        <td style="width: 10%;">작성자</td>
			        <td style="width: 30%;">제목</td>
			        <td style="width: 50%;">내용</td>
			        <td style="width: 10%;">작성일</td>
			    </tr>
			</thead>
            <tbody>
    		
    		    <!-- 등록된 서평이 없을 때 -->
    		    <c:if test="${empty requestScope.reviewList}">
    		        <tr>
    		            <td colspan="4" align="center">📭 등록된 서평이 없습니다.</td>
    		        </tr>
    		    </c:if>
    		    
    		    <!-- 서평 리스트 출력 -->
    		    <c:forEach var="review" items="${requestScope.reviewList}">
    		        <tr class="clickable-row" data-board-id="${review.boardId}">
                        <td>${review.userId}</td>
                        <td class="title-cell">${review.title}</td>
                        <td>
                           <div class="content-clamp">${review.content}</div>
                        </td>
    		            <td><fmt:formatDate value="${review.createdAt}" pattern="yyyy-MM-dd" /></td>
    		        </tr>
    		    </c:forEach>
            </tbody>
		</table>
    </center>

    <script>
        $(document).ready(function() {
            // .clickable-row 클래스를 가진 모든 tr 요소에 클릭 이벤트를 걸어줘!
            $(".clickable-row").click(function() {
                // 클릭된 tr 요소에서 data-board-id 속성 값을 가져와!
                var boardId = $(this).data("board-id"); // data() 메소드가 data- 접두사 빼고 가져옴!

                // 서평 상세 페이지 URL을 만들어! (컨트롤러 경로에 맞게 수정!)
                var detailUrl = "${contextPath}/board/reviewDetail.do?boardId=" + boardId;

                // 해당 URL로 페이지 이동!
                window.location.href = detailUrl;
            });
        });
    </script>

</body>
</html>
