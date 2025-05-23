<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
    request.setCharacterEncoding("UTF-8");
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>도서 검색</title>
    <link rel="stylesheet" href="<%= contextPath %>/css/common.css">
    <style>
        body {
            background-color: #fafafa;
        }

        .content-box {
            max-width: 800px;
            margin: 0 auto;
            padding: 100px 20px 40px;
            text-align: center;
        }

        .title {
            font-size: 28px;
            font-weight: bold;
            color: #003c83;
            margin-bottom: 40px;
        }

        .search-form {
            display: flex;
            justify-content: center;
            gap: 12px;
            flex-wrap: wrap;
        }

        .search-form input[type="text"] {
            width: 100%;
            max-width: 480px;
            padding: 14px;
            font-size: 16px;
            border-radius: 6px;
            border: 1px solid #ccc;
        }

        .btn {
            padding: 14px 22px;
            font-size: 15px;
            border-radius: 6px;
            border: none;
            cursor: pointer;
            text-decoration: none;
            color: white;
            display: inline-block;
        }

        .btn-green {
            background-color: #4caf50;
        }

        .btn-green:hover {
            background-color: #388e3c;
        }

        .btn-blue {
            background-color: #003c83;
        }

        .btn-blue:hover {
            background-color: #002c66;
        }
    </style>
</head>
<body>
<div class="content-box">
    <div class="title">도서 검색</div>
    <form action="<%= contextPath %>/books/bookSearch.do" method="get" class="search-form">
        <input type="text" name="keyword" placeholder="도서명, 저자, 출판사, 분야 검색" required />
        <button type="submit" class="btn btn-green">검색</button>
        <a href="<%= contextPath %>/view/main" class="btn btn-blue">메인으로</a>
    </form>
</div>
</body>
</html>
