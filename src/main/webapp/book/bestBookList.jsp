<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Vo.BookVo, java.util.*" %>
<%
    request.setCharacterEncoding("UTF-8");
    String contextPath = request.getContextPath();
    Vector<BookVo> bookList = (Vector<BookVo>) request.getAttribute("v");
    int currentPage = (int) request.getAttribute("currentPage");
    int totalCount = (int) request.getAttribute("totalCount");
    int pageSize = (int) request.getAttribute("pageSize");
    int totalPage = (int) Math.ceil((double) totalCount / pageSize);
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>인기 도서</title>
    <link rel="stylesheet" href="<%= contextPath %>/css/common.css">
    <style>
        .content-box {
            max-width: 1200px;
            margin: 0 auto;
            padding: 40px 20px;
        }

        .toolbar {
            display: flex;
            justify-content: flex-end;
            align-items: center;
            gap: 12px;
            margin-bottom: 20px;
            flex-wrap: wrap;
        }

        .btn {
            padding: 8px 14px;
            font-size: 14px;
            border-radius: 4px;
            color: white;
            text-decoration: none;
            border: none;
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

        .title {
            text-align: center;
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 30px;
            color: #003c83;
        }

        .grid-container {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 30px;
        }

        .card {
            background-color: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.05);
            text-align: center;
            transition: transform 0.2s;
        }

        .card:hover {
            transform: translateY(-4px);
        }

        .card img.thumb {
            width: 100%;
            max-width: 140px;
            height: auto;
            aspect-ratio: 3 / 4;
            object-fit: cover;
            border: 1px solid #ccc;
            border-radius: 4px;
            margin-bottom: 10px;
        }

        .card .title {
            font-size: 16px;
            font-weight: bold;
            color: #333;
            margin-bottom: 6px;
        }

        .card .author {
            font-size: 14px;
            color: #666;
        }

        .pagination {
            margin-top: 40px;
            text-align: center;
        }

        .pagination a {
            display: inline-block;
            margin: 0 6px;
            padding: 6px 12px;
            border-radius: 4px;
            font-size: 14px;
            background-color: #eee;
            color: #333;
            text-decoration: none;
            border: 1px solid #ccc;
        }

        .pagination a:hover {
            background-color: #ddd;
        }

        .pagination a.active {
            background-color: #003c83;
            color: white;
            border-color: #003c83;
        }

        .empty-message {
            text-align: center;
            padding: 40px;
            font-size: 16px;
            color: #888;
        }

        @media (max-width: 768px) {
            .grid-container {
                grid-template-columns: repeat(2, 1fr);
            }
        }

        @media (max-width: 480px) {
            .grid-container {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
<div class="content-box">
    <div class="toolbar">
<%--         <form action="<%= contextPath %>/books/bookSearch.do" method="get"> --%>
<!--             <input type="text" name="keyword" placeholder="도서명, 저자, 출판사, 분야 검색"> -->
<!--             <button type="submit" class="btn btn-green">검색</button> -->
<!--         </form> -->
        <a href="<%= contextPath %>/view/main" class="btn btn-blue">메인으로</a>
    </div>

    <div class="title">인기 도서</div>

    <% if (bookList != null && !bookList.isEmpty()) { %>
        <div class="grid-container">
            <% for (BookVo book : bookList) { %>
                <div class="card">
                    <a href="<%= contextPath %>/books/bookDetail.do?bookNo=<%= book.getBookNo() %>">
                        <img class="thumb" src="<%= contextPath %>/<%= book.getThumbnail() %>"
                             onerror="this.src='<%= contextPath %>/book/img/noimage.jpg';" alt="도서 썸네일">
                    </a>
                    <div class="title"><%= book.getTitle() %></div>
                    <div class="author"><%= book.getAuthor() %></div>
                </div>
            <% } %>
        </div>

        <div class="pagination">
            <% if (currentPage > 1) { %>
                <a href="<%= contextPath %>/books/bestBooks.do?page=<%= currentPage - 1 %>">◀</a>
            <% } %>
            <% for (int i = 1; i <= totalPage; i++) { %>
                <% if (i == currentPage) { %>
                    <a class="active"><%= i %></a>
                <% } else { %>
                    <a href="<%= contextPath %>/books/bestBooks.do?page=<%= i %>"><%= i %></a>
                <% } %>
            <% } %>
            <% if (currentPage < totalPage) { %>
                <a href="<%= contextPath %>/books/bestBooks.do?page=<%= currentPage + 1 %>">▶</a>
            <% } %>
        </div>

    <% } else { %>
        <div class="empty-message">인기 도서가 없습니다.</div>
    <% } %>
</div>
</body>
</html>
