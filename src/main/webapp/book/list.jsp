<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
request.setCharacterEncoding("UTF-8");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>BOOKLIST</title>

</head>
<body>
	<table border="1" width="100%">
		<tr>
			<th>도서번호</th>
			<th>도서제목</th>
			<th>도서저자</th>
			<th>출판사</th>
			<th>출판년도</th>
			<th>상세보기</th>
		</tr>
		<c:forEach var="book" items="${v}">
			<tr>
				<td>${book.bookNo}</td>
				<td>${book.title}</td>
				<td>${book.author}</td>
				<td>${book.publisher}</td>
				<td>${book.publishYear}</td>
				<td><a
					href="${pageContext.request.contextPath}/book/bookInfo.do?bookNo=${book.bookNo}">보기</a></td>
			</tr>
		</c:forEach>
	</table>

</body>
</html>