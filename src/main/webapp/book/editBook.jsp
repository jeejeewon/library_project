<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
    request.setCharacterEncoding("UTF-8");
    String contextPath = request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 메뉴</title>
<link rel="stylesheet" href="<%= contextPath %>/css/common.css">
<style>
    .admin-menu {
        max-width: 600px;
        margin: 80px auto;
        padding: 30px;
        border: 2px solid #003c83;
        border-radius: 10px;
        background-color: #f0f4ff;
        text-align: center;
    }

    .admin-menu h2 {
        color: #003c83;
        margin-bottom: 20px;
    }

    .admin-button {
        display: block;
        width: 80%;
        margin: 15px auto;
        padding: 12px;
        background-color: #003c83;
        color: white;
        text-decoration: none;
        border-radius: 5px;
        font-weight: bold;
    }

    .admin-button.secondary {
        background-color: #666;
    }
</style>
</head>
<body>

<div class="container admin-menu">
    <h2>관리자 메뉴</h2>

    <a href="<%= contextPath %>/books/addBook.do" class="admin-button">📚도서 등록</a>
    <a href="<%= contextPath %>/books/updateBook.do" class="admin-button">📚도서 수정</a>
    <a href="<%= contextPath %>/books/returnBook.do" class="admin-button">🔄반납 관리</a>
    <a href="<%= contextPath %>/books/allRental.do" class="admin-button secondary">📄전체 대여 목록</a>
</div>

</body>
</html>
