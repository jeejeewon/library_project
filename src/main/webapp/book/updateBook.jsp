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
    String message = (String) request.getAttribute("message");
    Boolean fromUpdate = (Boolean) request.getAttribute("fromUpdate");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>도서 수정/삭제</title>
    <link rel="stylesheet" href="<%= contextPath %>/css/common.css">
    <style>
        body {
            background-color: #fafafa;
        }

        .content-box {
            max-width: 1100px;
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
            table-layout: fixed;
            border-collapse: collapse;
            font-size: 14px;
            background-color: #fff;
        }

        th, td {
            border: 1px solid #ccc;
            text-align: center;
            padding: 10px;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        th {
            background-color: #f0f0f0;
        }

        .green { color: green; font-weight: bold; }
        .red { color: red; font-weight: bold; }
        .gray { color: gray; font-weight: bold; }

        .btn {
            padding: 6px 12px;
            font-size: 13px;
            border: none;
            border-radius: 4px;
            background-color: #003c83;
            color: white;
            text-decoration: none;
            cursor: pointer;
        }

        .btn:hover {
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
    <div class="title">도서 수정 및 삭제</div>

    <div class="toolbar">
        <a href="<%= contextPath %>/books/adminBook.do">관리자화면으로</a> 
    </div>

    <% if (Boolean.TRUE.equals(fromUpdate) && message != null) { %>
        <script>
            alert("<%= message.replaceAll("\"", "\\\\\"") %>");
        </script>
    <% } %>

    <% if (bookList != null && !bookList.isEmpty()) { %>
        <table>
            <thead>
                <tr>
                    <th style="width: 8%;">도서NO.</th>
                    <th style="width: 27%;">도서명</th>
                    <th style="width: 14%;">저자</th>
                    <th style="width: 14%;">출판사</th>
                    <th style="width: 8%;">분야</th>
                    <th style="width: 8%;">출판년도</th>                    
                    <th style="width: 15%;">도서상태</th>                    
                    <th style="width: 10%;">관리</th>
                </tr>
            </thead>
            <tbody>
                <% for (BookVo book : bookList) { %>
                <tr>
                    <td><%= book.getBookNo() %></td>
                    <td><%= book.getTitle() %></td>
                    <td><%= book.getAuthor() %></td>
                    <td><%= book.getPublisher() %></td>
                    <td><%= book.getCategory() %></td>
                    <td><%= book.getPublishYear() %></td>
                    <td class="<%= 
                        book.getRentalState() == 0 ? "green" :
                        book.getRentalState() == 1 ? "red" : "gray" %>">
                        <%= book.getRentalState() == 0 ? "대출 가능" :
                             book.getRentalState() == 1 ? "대출 중" : "분실/이용 불가" %>
                    </td>
                    <td>
                        <a href="<%= contextPath %>/books/editBook.do?bookNo=<%= book.getBookNo() %>&page=<%= currentPage %>" class="btn">수정/삭제</a>
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>

        <div class="pagination">
            <% if (currentPage > 1) { %>
                <a href="<%= contextPath %>/books/updateBook.do?page=<%= currentPage - 1 %>">◀</a>
            <% } %>
            <% for (int i = 1; i <= totalPage; i++) { %>
                <% if (i == currentPage) { %>
                    <a class="active"><%= i %></a>
                <% } else { %>
                    <a href="<%= contextPath %>/books/updateBook.do?page=<%= i %>"><%= i %></a>
                <% } %>
            <% } %>
            <% if (currentPage < totalPage) { %>
                <a href="<%= contextPath %>/books/updateBook.do?page=<%= currentPage + 1 %>">▶</a>
            <% } %>
        </div>
    <% } else { %>
        <div class="empty-message">등록된 도서가 없습니다.</div>
    <% } %>
</div>
</body>
</html>
