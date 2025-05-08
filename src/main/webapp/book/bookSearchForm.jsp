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
    body {
        background-color: #f5f5f5;
    }

    .search-form-wrapper {
        min-height: 90vh;
        display: flex;
        justify-content: center;
        align-items: flex-start;  /* 상단 정렬 */
        padding-top: 100px;       /* 위쪽 여백 추가 */
    }

    .search-form-box {
        text-align: center;
    }

    .search-form-box h1 {
        font-size: 32px;
        margin-bottom: 40px;
        color: #333;
    }

    .search-form-box form {
        display: flex;
        justify-content: center;
        gap: 12px;
    }

    .search-form-box input[type="text"] {
        width: 500px;
        padding: 16px;
        font-size: 18px;
        border-radius: 6px;
        border: 1px solid #ccc;
    }

    .search-form-box button {
        padding: 16px 24px;
        font-size: 16px;
        background-color: #4caf50;
        border: none;
        color: white;
        border-radius: 6px;
        cursor: pointer;
    }

    .search-form-box button:hover {
        background-color: #388e3c;
    }
</style>
</head>
<body>

<div class="container search-form-wrapper">
    <div class="search-form-box">
        <h1>도서 검색</h1>
        <form action="<%= contextPath %>/books/bookSearch.do" method="get">
            <input type="text" name="keyword" placeholder="도서명, 저자, 출판사, 분야 검색" required />
            <button type="submit">검색</button>
        </form>
    </div>
</div>

</body>
</html>
