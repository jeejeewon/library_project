<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="Vo.BookVo, java.util.*" %>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="Vo.BookVo" %>
<%
    request.setCharacterEncoding("UTF-8");
    String contextPath = request.getContextPath();

    BookVo book = (BookVo) request.getAttribute("book");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>도서 등록/수정</title>
<link rel="stylesheet" href="<%= contextPath %>/css/common.css">
<style>
    .form-container {
        max-width: 600px;
        margin: 0 auto;
        padding: 20px;
        border: 1px solid #ccc;
        border-radius: 8px;
        background-color: #f9f9f9;
    }

    .form-container h2 {
        text-align: center;
    }

    .form-container input, .form-container textarea {
        width: 100%;
        padding: 8px;
        margin-bottom: 10px;
        border: 1px solid #ccc;
        border-radius: 4px;
    }

    .form-container button {
        width: 100%;
        padding: 10px;
        background-color: #5cb85c;
        color: white;
        border: none;
        border-radius: 4px;
    }

    .form-container .meta {
        margin-bottom: 10px;
    }
</style>
</head>
<body>

<div class="form-container">
    <h2>도서 등록/수정</h2>

    <form action="<%= contextPath %>/books/addBook.do" method="post">
        <%-- 수정할 경우 book_no 숨김 필드 --%>
        <% if (book != null) { %>
            <input type="hidden" name="bookNo" value="<%= book.getBookNo() %>">
        <% } %>

        <input type="text" name="title" placeholder="도서명" value="<%= (book != null) ? book.getTitle() : "" %>" required>
        <input type="text" name="author" placeholder="저자" value="<%= (book != null) ? book.getAuthor() : "" %>" required>
        <input type="text" name="publisher" placeholder="출판사" value="<%= (book != null) ? book.getPublisher() : "" %>" required>
        <input type="number" name="publishYear" placeholder="출판년도" value="<%= (book != null) ? book.getPublishYear() : "" %>" required>
        <input type="text" name="isbn" placeholder="ISBN" value="<%= (book != null) ? book.getIsbn() : "" %>" required>
        <input type="text" name="category" placeholder="카테고리" value="<%= (book != null) ? book.getCategory() : "" %>" required>
        <input type="text" name="thumbnail" placeholder="썸네일 경로 (예: book/img/sample.jpg)" value="<%= (book != null) ? book.getThumbnail() : "" %>">

        <textarea name="bookInfo" rows="5" placeholder="도서 설명"><%= (book != null) ? book.getBookInfo() : "" %></textarea>

        <button type="submit">저장</button>
    </form>

    <% if (request.getAttribute("message") != null) { %>
        <div style="color: green; text-align: center;"><%= request.getAttribute("message") %></div>
    <% } %>
</div>

</body>
</html>
