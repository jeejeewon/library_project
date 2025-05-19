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
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>전체 대여 목록</title>
    <link rel="stylesheet" href="<%= contextPath %>/css/common.css">
    <style>
        .content-box {
            max-width: 1200px;
            margin: 0 auto;
            padding: 40px 20px;
        }

        .title {
            text-align: center;
            font-size: 26px;
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

        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 30px;
            font-size: 14px;
        }

        th, td {
            text-align: center;
            padding: 6px 6px;
            border: 1px solid #ccc;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        th {
            background-color: #f8f8f8;
        }

        .thumbnail {
            width: 40px;
            height: 60px;
            object-fit: cover;
            border-radius: 4px;
        }

        .pagination {
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
    <div class="title">전체 대여 목록</div>

    <div class="toolbar">
        <a href="<%= contextPath %>/books/adminBook.do">관리자화면으로</a>
    </div>

    <% if (rentalList != null && !rentalList.isEmpty()) { %>
        <table>
            <thead>
                <tr>
                    <th>대여번호</th>
                    <th>썸네일</th>
                    <th>도서명</th>
                    <th>사용자 ID</th>
                    <th>대여일</th>
                    <th>반납 예정일</th>
                    <th>반납일</th>
                    <th>상태</th>
                </tr>
            </thead>
            <tbody>
                <% for (RentalVo rental : rentalList) {
                    BookVo book = rental.getBook();
                %>
                    <tr>
                        <td><%= rental.getRentNo() %></td>
                        <td>
                            <img src="<%= contextPath %>/<%= book.getThumbnail() %>"
                                 class="thumbnail"
                                 onerror="this.src='<%= contextPath %>/book/img/noimage.jpg';" alt="썸네일" />
                        </td>
                        <td><%= book.getTitle() %></td>
                        <td><%= rental.getUserId() %></td>
                        <td><%= sdf.format(rental.getStartDate()) %></td>
                        <td><%= sdf.format(rental.getReturnDue()) %></td>
                        <td><%= rental.getReturnDate() != null ? sdf.format(rental.getReturnDate()) : "-" %></td>
                        <td>
                            <% if (rental.getReturnState() == 1) { %>
                                ✔ 반납
                            <% } else { %>
                                ❌ 미반납
                            <% } %>
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>

        <div class="pagination">
            <% if (currentPage > 1) { %>
                <a href="<%= contextPath %>/books/allRental.do?page=<%= currentPage - 1 %>">◀</a>
            <% } %>

            <% for (int i = 1; i <= totalPage; i++) { %>
                <% if (i == currentPage) { %>
                    <a class="active"><%= i %></a>
                <% } else { %>
                    <a href="<%= contextPath %>/books/allRental.do?page=<%= i %>"><%= i %></a>
                <% } %>
            <% } %>

            <% if (currentPage < totalPage) { %>
                <a href="<%= contextPath %>/books/allRental.do?page=<%= currentPage + 1 %>">▶</a>
            <% } %>
        </div>

    <% } else { %>
        <div class="empty-message">대여 기록이 없습니다.</div>
    <% } %>
</div>
</body>
</html>
