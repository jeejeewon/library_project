<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="Vo.BookVo, java.util.*" %>

<%
    request.setCharacterEncoding("UTF-8");
    String contextPath = request.getContextPath();
    Vector<BookVo> bookList = (Vector<BookVo>) request.getAttribute("v");
    String keyword = (String) request.getAttribute("keyword");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>도서 검색 결과</title>
<link rel="stylesheet" href="<%= contextPath %>/css/common.css">
<style>
    .book-section {
        min-height: 80vh;
        display: flex;
        flex-direction: column;
        justify-content: center;
        padding: 40px 20px;
    }

    .book-title {
        font-size: 22px;
        font-weight: bold;
        margin-bottom: 30px;
        text-align: center;
    }

    .book-container {
        display: grid;
        grid-template-columns: repeat(4, 1fr);
        gap: 30px;
        justify-content: center;
    }

    .book-card {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        border: 1px solid #ddd;
        border-radius: 8px;
        padding: 16px;
        background-color: #fafafa;
        transition: transform 0.2s ease;
        height: 280px;
        overflow: hidden;
        text-align: center;
    }

    .book-card:hover {
        transform: translateY(-5px);
        box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    }

    .book-card img {
        width: 100px;
        height: 140px;
        object-fit: cover;
        margin-bottom: 12px;
        border: 1px solid #ccc;
        background-color: #fff;
    }

    .book-card .title {
        font-size: 14px;
        font-weight: bold;
        margin-bottom: 6px;
        color: #333;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }

    .book-card .author {
        font-size: 12px;
        color: #666;
    }

    .empty-message {
        text-align: center;
        padding: 40px;
        font-size: 16px;
        color: #999;
    }
</style>
</head>
<body>

<div class="container book-section">
    <div class="book-title">🔍 "<%= keyword %>" 검색 결과</div>

    <%
    if (bookList != null && bookList.size() > 0) {
    %>
    <div class="book-container">
        <% for (BookVo book : bookList) { %>
        <div class="book-card">
            <a href="<%= contextPath %>/books/bookInfo.do?bookNo=<%= book.getBookNo() %>">
                <img src="<%= contextPath + "/" + book.getThumbnail() %>"
                     onerror="this.src='<%= contextPath %>/book/img/noimage.jpg';" />
            </a>
            <div class="title"><%= book.getTitle() %></div>
            <div class="author"><%= book.getAuthor() %></div>
        </div>
        <% } %>
    </div>
    <%
    } else {
    %>
    <div class="empty-message">"<%= keyword %>"에 대한 검색 결과가 없습니다.</div>
    <%
    }
    %>
</div>

</body>
</html>
