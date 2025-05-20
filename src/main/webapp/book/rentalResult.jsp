<%@ page contentType="text/html; charset=UTF-8" %>
<%
    String contextPath = request.getContextPath();
    String message = (String) request.getAttribute("message");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>대여 결과</title>
    <link rel="stylesheet" href="<%= contextPath %>/css/common.css">
    <style>
        body {
            background-color: #fafafa;
        }

        .result-box {
            max-width: 600px;
            margin: 100px auto;
            text-align: center;
            background: #fff;
            border: 1px solid #ddd;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
        }

        .result-box h2 {
            font-size: 20px;
            color: #333;
            margin-bottom: 24px;
        }

        .btn-group {
            display: flex;
            justify-content: center;
            gap: 14px;
            margin-top: 20px;
        }

        .btn {
            padding: 10px 20px;
            font-size: 14px;
            border-radius: 4px;
            color: white;
            text-decoration: none;
            border: none;
        }

        .btn-blue {
            background-color: #003c83;
        }

        .btn-blue:hover {
            background-color: #002c66;
        }

        .btn-green {
            background-color: #4caf50;
        }

        .btn-green:hover {
            background-color: #388e3c;
        }
    </style>
</head>
<body>
<div class="result-box">
    <h2><%= message %></h2>

    <div class="btn-group">
        <a href="<%= contextPath %>/books/bookList.do" class="btn btn-blue">도서 목록으로</a>
        <a href="<%= contextPath %>/books/myRentalList.do" class="btn btn-green">나의 대여 목록</a>
    </div>
</div>
</body>
</html>
