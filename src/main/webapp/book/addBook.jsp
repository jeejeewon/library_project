<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="Vo.BookVo" %>
<%
    request.setCharacterEncoding("UTF-8");
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>도서 등록</title>
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
        margin-bottom: 20px;
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
        background-color: #007bff;
        color: white;
        border: none;
        border-radius: 4px;
    }
</style>
</head>
<body>
<div class="form-container">
    <h2>도서 등록</h2>
    <form action="<%= contextPath %>/books/addBookProcess.do" method="post" enctype="multipart/form-data">
        <input type="text" name="title" placeholder="도서명" required>
        <input type="text" name="author" placeholder="저자" required>
        <input type="text" name="publisher" placeholder="출판사" required>
        <input type="number" name="publishYear" placeholder="출판년도" required>
        <input type="text" name="isbn" placeholder="ISBN" required>
        <input type="text" name="category" placeholder="카테고리" required>
        <input type="file" name="thumbnail" accept="image/*" required>
        <textarea name="bookInfo" rows="5" placeholder="도서 설명"></textarea>
        <button type="submit">등록</button>
    </form>
</div>
</body>
</html>