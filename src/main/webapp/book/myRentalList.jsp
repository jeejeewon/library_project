<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Vo.RentalVo, Vo.BookVo, java.util.*, java.text.SimpleDateFormat" %>
<%
    request.setCharacterEncoding("UTF-8");
    String contextPath = request.getContextPath();
    Vector<RentalVo> rentalList = (Vector<RentalVo>) request.getAttribute("rentalList");
    int currentPage = (int) request.getAttribute("currentPage");
    int totalCount = (int) request.getAttribute("totalCount");
    int pageSize = (int) request.getAttribute("pageSize");
    int totalPage = (int) Math.ceil((double) totalCount / pageSize);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>나의 대여 목록</title>
    <link rel="stylesheet" href="<%= contextPath %>/css/common.css">
    <style>
        .content-box {
            max-width: 1200px;
            margin: 0 auto;
            padding: 40px 20px;
        }

        .title {
            text-align: center;
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 30px;
            color: #003c83;
        }

        .toolbar {
            text-align: right;
            margin-bottom: 20px;
        }

        .toolbar a {
            padding: 8px 16px;
            font-size: 14px;
            border-radius: 4px;
            background-color: #003c83;
            color: white;
            text-decoration: none;
        }

        .toolbar a:hover {
            background-color: #002c66;
        }

        .grid-container {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
            gap: 24px;
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
            border: 1px solid #ccc;
            border-radius: 4px;
            margin-bottom: 10px;
        }

        .card .title {
            font-size: 16px;
            font-weight: bold;
            color: #333;
            margin-bottom: 4px;
        }

        .card .meta {
            font-size: 13px;
            color: #666;
            margin-bottom: 4px;
        }

        .status {
            font-size: 13px;
            font-weight: bold;
            margin-top: 10px;
        }

        .status.returned {
            color: green;
        }

        .status.pending {
            color: red;
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
    </style>
</head>
<body>
<div class="content-box">
    <div class="title">나의 대여 목록</div>

    <div class="toolbar">
        <a href="<%= contextPath %>/member/mypage">마이페이지</a>
    </div>

    <% if (rentalList != null && !rentalList.isEmpty()) { %>
        <div class="grid-container">
            <% for (RentalVo rental : rentalList) {
                BookVo book = rental.getBook();
                if (book != null) {
            %>
            <div class="card">
                <img src="<%= contextPath %>/<%= book.getThumbnail() %>"
                     onerror="this.src='<%= contextPath %>/book/img/noimage.jpg';" alt="썸네일" />
                <div class="title"><%= book.getTitle() %></div>
                <div class="meta">대여일: <%= sdf.format(rental.getStartDate()) %></div>
                <div class="meta">반납 예정일: <%= sdf.format(rental.getReturnDue()) %></div>
                <% if (rental.getReturnState() == 1) { %>
                    <div class="status returned">✅ 반납 완료</div>
                <% } else { %>
                    <div class="status pending">📕 미반납</div>
                <% } %>
            </div>
            <% 
                }
              } %>
        </div>

        <div class="pagination">
            <% if (currentPage > 1) { %>
                <a href="<%= contextPath %>/books/myRentalList.do?page=<%= currentPage - 1 %>">◀</a>
            <% } %>

            <% for (int i = 1; i <= totalPage; i++) { %>
                <% if (i == currentPage) { %>
                    <a class="active"><%= i %></a>
                <% } else { %>
                    <a href="<%= contextPath %>/books/myRentalList.do?page=<%= i %>"><%= i %></a>
                <% } %>
            <% } %>

            <% if (currentPage < totalPage) { %>
                <a href="<%= contextPath %>/books/myRentalList.do?page=<%= currentPage + 1 %>">▶</a>
            <% } %>
        </div>
    <% } else { %>
        <div class="empty-message">현재 대여 중인 도서가 없습니다.</div>
    <% } %>
</div>
</body>
</html>
