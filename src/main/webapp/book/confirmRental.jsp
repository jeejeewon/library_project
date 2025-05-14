<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="Vo.BookVo" %>
<%
    String contextPath = request.getContextPath();
    BookVo book = (BookVo) request.getAttribute("book");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>도서 대출 확인</title>
<link rel="stylesheet" href="<%= contextPath %>/css/common.css">
<style>
    .confirm-section {
        min-height: 60vh;
        padding: 60px 20px;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        text-align: center;
    }

    .confirm-title {
        font-size: 24px;
        font-weight: bold;
        margin-bottom: 20px;
    }

    .confirm-info {
        font-size: 16px;
        color: #424242;
        margin-bottom: 30px;
    }

    .button-box {
        display: flex;
        gap: 20px;
    }

    .btn {
        padding: 10px 20px;
        border-radius: 4px;
        font-size: 14px;
        text-decoration: none;
        cursor: pointer;
    }

    .btn-confirm {
        background-color: #003c83;
        color: #fff;
        border: none;
    }

    .btn-cancel {
        background-color: #ccc;
        color: #333;
        border: none;
    }
</style>
</head>
<body>
<div class="container confirm-section">
    <div class="confirm-title">도서 대출 확인</div>
    <div class="confirm-info">
        도서명: <strong><%= book.getTitle() %></strong><br>
        저자: <%= book.getAuthor() %><br><br>
        이 도서를 대출하시겠습니까?
    </div>

    <form action="<%= contextPath %>/books/rentalBook.do" method="post" class="button-box">
        <input type="hidden" name="bookNo" value="<%= book.getBookNo() %>">
        <button type="submit" class="btn btn-confirm">대출하기</button>
        <a href="javascript:history.back();" class="btn btn-cancel">취소</a>
    </form>
</div>
</body>
</html>
