<%@ page language="java" contentType="text/html; charset=UTF-8" 
	pageEncoding="UTF-8"%>
<%@ page import="Vo.RentalVo, Vo.BookVo, java.util.*" %>

<%
    request.setCharacterEncoding("UTF-8");
    String contextPath = request.getContextPath();    
    Vector<RentalVo> pendingList = (Vector<RentalVo>) request.getAttribute("pendingList");
    String message = (String) request.getAttribute("message");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>반납 처리</title>
<link rel="stylesheet" href="<%= contextPath %>/css/common.css">
<style>
    .admin-section {
        min-height: 80vh;
        padding: 40px 20px;
    }

    .admin-title {
        font-size: 22px;
        font-weight: bold;
        margin-bottom: 20px;
        text-align: center;
    }

    .message {
        color: green;
        text-align: center;
        margin-bottom: 20px;
    }

    .rental-container {
        display: grid;
        grid-template-columns: repeat(4, 1fr);
        gap: 20px;
    }

    .rental-card {
        border: 1px solid #ccc;
        border-radius: 8px;
        padding: 12px;
        background-color: #fafafa;
        text-align: center;
    }

    .rental-card img {
        width: 100px;
        height: 140px;
        object-fit: cover;
        margin-bottom: 10px;
    }

    .rental-card .title {
        font-weight: bold;
        margin-bottom: 5px;
    }

    .rental-card .dates {
        font-size: 12px;
        color: #555;
        margin-bottom: 10px;
    }

    .rental-card .btn-return {
        padding: 6px 12px;
        background-color: #003c83;
        color: white;
        border: none;
        border-radius: 4px;
        text-decoration: none;
        font-size: 12px;
    }
</style>
</head>
<body>

<div class="container admin-section">
    <div class="admin-title">미반납 도서 목록</div>

    <% if (message != null) { %>
        <div class="message"><%= message %></div>
    <% } %>

    <% if (pendingList != null && pendingList.size() > 0) { %>
        <div class="rental-container">
            <% for (RentalVo rental : pendingList) {
                   BookVo book = rental.getBook(); %>
            <div class="rental-card">
                <img src="<%= contextPath %>/<%= book.getThumbnail() %>" 
                     onerror="this.src='<%= contextPath %>/book/img/noimage.png';">
                <div class="title"><%= book.getTitle() %></div>
                <div class="dates">
                    대출일: <%= rental.getStartDate().toLocalDateTime().toLocalDate() %><br>
                    반납 예정일: <%= rental.getReturnDue().toLocalDateTime().toLocalDate() %>
                </div>
                <a href="<%= contextPath %>/books/returnBook.do?rentNo=<%= rental.getRentNo() %>" class="btn-return">반납 처리</a>
            </div>
            <% } %>
        </div>
    <% } else { %>
        <div style="text-align: center;">현재 미반납 도서가 없습니다.</div>
    <% } %>

</div>

</body>
</html>
