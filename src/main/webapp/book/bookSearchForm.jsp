<%@ page contentType="text/html; charset=UTF-8" %>
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
    .book-search-form-container {
        min-height: 80vh;
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .search-form-box {
        text-align: center;
        border: 1px solid #ddd;
        padding: 40px 30px;
        border-radius: 10px;
        background-color: #fafafa;
        box-shadow: 0 4px 10px rgba(0,0,0,0.05);
    }

    .search-form-box h2 {
        font-size: 24px;
        margin-bottom: 30px;
    }

    .search-form-box input[type="text"] {
        width: 300px;
        padding: 10px;
        border-radius: 4px;
        border: 1px solid #ccc;
        font-size: 14px;
    }

    .search-form-box button {
        padding: 10px 16px;
        margin-left: 8px;
        background-color: #4caf50;
        border: none;
        color: white;
        border-radius: 4px;
        font-size: 14px;
        cursor: pointer;
    }

    .search-form-box button:hover {
        background-color: #388e3c;
    }
</style>
</head>
<body>

<div class="container book-search-form-container">
    <div class="search-form-box">
        <h2>도서 검색</h2>
        <form action="<%= contextPath %>/books/bookSearch.do" method="get">
            <input type="text" name="keyword" placeholder="도서명, 저자, 출판사 검색" required />
            <button type="submit">검색</button>
        </form>
    </div>
</div>

</body>
</html>
