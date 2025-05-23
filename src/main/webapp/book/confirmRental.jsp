<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="Vo.BookVo" %>
<%
    String contextPath = request.getContextPath();
    BookVo book = (BookVo) request.getAttribute("book");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>대여 확인</title>
    <link rel="stylesheet" href="<%= contextPath %>/css/common.css">
    <style>
        body {
            background-color: #fafafa;
        }
        .confirm-box {
            max-width: 600px;
            margin: 80px auto;
            text-align: center;
            background: #fff;
            border: 1px solid #ddd;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
        }
        .confirm-box h2 {
            font-size: 20px;
            margin-bottom: 30px;
            color: #333;
        }
        .confirm-box .btn {
            padding: 10px 20px;
            margin: 10px;
            font-size: 14px;
            border: none;
            border-radius: 4px;
            color: #fff;
            cursor: pointer;
        }
        .btn-green {
            background-color: #4caf50;
        }
        .btn-green:hover {
            background-color: #388e3c;
        }
        .btn-gray {
            background-color: #888;
        }
        .btn-gray:hover {
            background-color: #555;
        }
    </style>
</head>
<body>
<div class="confirm-box">
    <h2>
        "<%= book.getTitle() %>"<br>
        이 책을 대여하시겠습니까?
    </h2>
    <form action="<%= contextPath %>/books/rentalBook.do" method="post" style="display:inline;">
        <input type="hidden" name="bookNo" value="<%= book.getBookNo() %>">
        <button type="submit" class="btn btn-green">대여하기</button>
    </form>
    <a href="javascript:history.back();" class="btn btn-gray">취소</a>
</div>
</body>
</html>
