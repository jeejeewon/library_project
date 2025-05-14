<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.*" %>
<%
	request.setCharacterEncoding("UTF-8");
	String contextPath = request.getContextPath();
    String message = (String) request.getAttribute("message");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>대출 결과</title>
<link rel="stylesheet" href="<%= contextPath %>/css/common.css">
<style>
    .result-box {
        padding: 100px;
        text-align: center;
        font-size: 18px;
    }
    .btn {
        margin-top: 30px;
        padding: 10px 20px;
        background-color: #333;
        color: #fff;
        text-decoration: none;
        border-radius: 5px;
    }
    
</style>
</head>
<body>

<div class="container">
    <div class="result-box">
        <p><%= message %></p>
        <a href="<%= contextPath %>/books/bookList.do" class="btn">도서 목록으로 돌아가기</a>
    </div>
</div>

</body>
</html>
