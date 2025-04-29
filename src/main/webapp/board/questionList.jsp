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


<title>공지사항 리스트 - noticeLi</title>
<style type="text/css">
    .reply-row {
        display: none;
        background-color: #f9f9f9;
    }
    tr:hover {
        cursor: pointer;
        background-color: #eef;
    }
</style>
<script>
    function toggleReply(index) {
        const replyRow = document.getElementById("replyRow" + index);
        if (replyRow.style.display === "none") {
            replyRow.style.display = "table-row";
        } else {
            replyRow.style.display = "none";
        }
    }
</script>
</head>
<body>
	<center>
		<table align="center" border="1" width="80%">
			<tr height="20" align="center" bgcolor="lightgray">
				<td>상태</td>
				<td>제목</td>
				<td>작성자</td>
				<td>작성일</td>
				<td>조회수</td>
			</tr>
		    <c:forEach var="boardVo" items="${boardList}" varStatus="status">
                <tr height="20" align="center" onclick="toggleReply(${status.index})">
                    <td>
                    	<c:choose>
                    		<c:when test="${not empty boardVo.reply}">
                    			답변완료
                    		</c:when>
                    	</c:choose>
                    </td>
                    <td>${boardVo.title}</td>
                    <td>${boardVo.userId}</td>
                    <td>${boardVo.date}</td>
                    <td>${boardVo.views}</td>
                </tr>
                <tr id="replyRow${status.index}" class="reply-row">
                	<td colspan="5" align="left" style="padding: 10px;">
						<c:choose>
							<c:when test="${not empty boardVo.reply}">
							<strong>답변:</strong> ${boardVo.reply}
                            </c:when>
                            <c:otherwise>
                            	<span style="color:gray">답변이 없습니다.</span>
                            </c:otherwise>
						</c:choose>
						
                    </td>
                </tr>
             </c:forEach>
		</table>
	</center>
</body>
</html>