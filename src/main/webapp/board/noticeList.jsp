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

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항 리스트 - noticeLi</title>
</head>
<body>
	<center>
		<table align="center" border="1" width="80%">
			<tr height="20" align="center" bgcolor="lightgray">
				<td>No</td>
				<td>제목</td>
				<td>작성자</td>
				<td>작성일</td>
				<td>조회수</td>
				<td>첨부파일</td>
			</tr>
		    <c:forEach var="boardVo" items="${boardList}" varStatus="status">
                <tr height="20" align="center">
                    <td>${status.count}</td>
                    <td>${boardVo.title}</td>
                    <td>${boardVo.userId}</td>
                    <td>${boardVo.date}</td>
                    <td>${boardVo.views}</td>
                    <td>${boardVo.file}</td>
                </tr>
             </c:forEach>
		</table>
	</center>
</body>
</html>