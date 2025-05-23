<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Vo.RentalVo, Vo.BookVo, java.util.*" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%
    request.setCharacterEncoding("UTF-8");
    String contextPath = request.getContextPath();
    Vector<RentalVo> rentalList = (Vector<RentalVo>) request.getAttribute("rentalList");
    int currentPage = (int) request.getAttribute("currentPage");
    int totalCount = (int) request.getAttribute("totalCount");
    int pageSize = (int) request.getAttribute("pageSize");
    int totalPage = (int) Math.ceil((double) totalCount / pageSize);
    String message = (String) request.getAttribute("message");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>반납 대기 목록</title>
    <link rel="stylesheet" href="<%= contextPath %>/css/common.css">
    <style>
        .content-box {
            max-width: 1200px;
            margin: 0 auto;
            padding: 40px 20px;
        }
        
        .toolbar {
            text-align: right;
            margin-bottom: 20px;
        }
        
        .toolbar a {
            padding: 6px 16px;
            font-size: 14px;
            border-radius: 4px;
            background-color: #003c83;
            color: white;
            text-decoration: none;
        }

        .toolbar a:hover {
            background-color: #002c66;
        } 

        .title {
            text-align: center;
            font-size: 26px;
            font-weight: bold;
            margin-bottom: 30px;
            color: #003c83;
        }

        .message {
            text-align: center;
            color: green;
            margin-bottom: 20px;
            font-size: 15px;
        }

        .card-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
            gap: 20px;
        }

        .card {
            background-color: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.05);
            text-align: center;
        }

        .card img {
            width: 100px;
            height: 140px;
            object-fit: cover;
            border-radius: 4px;
            margin-bottom: 10px;
        }

        .card .title {
            font-size: 16px;
            font-weight: bold;
            margin-bottom: 6px;
        }

        .card .meta {
            font-size: 13px;
            color: #666;
            margin-bottom: 6px;
        }

        .card .btn-return {
            display: inline-block;
            padding: 6px 12px;
            background-color: #003c83;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            margin-top: 8px;
        }

        .card .btn-return:hover {
            background-color: #002c66;
        }

        .pagination {
            margin-top: 30px;
            text-align: center;
        }

        .pagination a {
            display: inline-block;
            margin: 0 4px;
            padding: 6px 10px;
            border-radius: 4px;
            font-size: 14px;
            background-color: #eee;
            color: #333;
            text-decoration: none;
            border: 1px solid #ccc;
        }

        .pagination a.active {
            background-color: #003c83;
            color: white;
            border-color: #003c83;
        }
    </style>
</head>
<body>
<div class="content-box">
    <div class="title">반납 대기 중인 도서</div>
    
    <div class="toolbar">
        <a href="<%= contextPath %>/books/adminBook.do">관리자화면으로</a>
    </div>
    <br> 

    <% if (message != null) { %>
        <div class="message"><%= message %></div>
    <% } %>

    <% if (rentalList != null && !rentalList.isEmpty()) { %>
        <div class="card-grid">
            <% for (RentalVo rental : rentalList) {
                BookVo book = rental.getBook();
            %>
                <div class="card">
                    <img src="<%= contextPath %>/<%= book.getThumbnail() %>"
                         onerror="this.src='<%= contextPath %>/book/img/noimage.jpg';" alt="썸네일" />
                    <div class="title"><%= book.getTitle() %></div>
                    <div class="meta">사용자: <%= rental.getUserId() %></div>
                    <div class="meta">대여일: <%= sdf.format(rental.getStartDate()) %></div>
                    <div class="meta">반납 예정일: <%= sdf.format(rental.getReturnDue()) %></div>
                    <a href="<%= contextPath %>/books/returnBook.do?rentNo=<%= rental.getRentNo() %>" 
                       class="btn-return" onclick="return confirm('반납 처리하시겠습니까?');">
                        반납 처리
                    </a>
                </div>
            <% } %>
        </div>

        <div class="pagination">
            <% if (currentPage > 1) { %>
                <a href="<%= contextPath %>/books/returnBook.do?page=<%= currentPage - 1 %>">◀</a>
            <% } %>
            <% for (int i = 1; i <= totalPage; i++) { %>
                <% if (i == currentPage) { %>
                    <a class="active"><%= i %></a>
                <% } else { %>
                    <a href="<%= contextPath %>/books/returnBook.do?page=<%= i %>"><%= i %></a>
                <% } %>
            <% } %>
            <% if (currentPage < totalPage) { %>
                <a href="<%= contextPath %>/books/returnBook.do?page=<%= currentPage + 1 %>">▶</a>
            <% } %>
        </div>
    <% } else { %>
        <div class="message">반납 대기 중인 도서가 없습니다.</div>
    <% } %>
</div>
</body>
</html>
