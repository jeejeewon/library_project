<%@ page contentType="text/html; charset=UTF-8" %>
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
<title>도서 수정</title>
<link rel="stylesheet" href="<%= contextPath %>/css/common.css">
<style>
    .table-container {
        max-width: 1000px;
        margin: 0 auto;
        padding: 30px;
    }

    table {
        width: 100%;
        border-collapse: collapse;
        margin-bottom: 20px;
        font-size: 14px;
    }

    th, td {
        border: 1px solid #ccc;
        padding: 10px;
        text-align: center;
    }

    th {
        background-color: #f2f2f2;
    }

    .btn {
        padding: 5px 10px;
        font-size: 12px;
        text-decoration: none;
        border-radius: 4px;
        display: inline-block;
    }

    .btn-edit {
        background-color: #007bff;
        color: white;
    }

    .btn-delete {
        background-color: #dc3545;
        color: white;
    }

    .pagination {
        text-align: center;
        margin-top: 20px;
    }

    .pagination a {
        display: inline-block;
        margin: 0 4px;
        padding: 6px 10px;
        border: 1px solid #ccc;
        border-radius: 4px;
        color: #333;
        text-decoration: none;
    }

    .pagination .active {
        background-color: #333;
        color: white;
    }
</style>
</head>
<body>

<div class="table-container">
    <h2 style="text-align:center;">도서 수정 / 삭제</h2>

    <table>
        <tr>
            <th>번호</th>
            <th>제목</th>
            <th>저자</th>
            <th>출판사</th>
            <th>출간년도</th>
            <th>카테고리</th>
            <th>수정</th>
            <th>삭제</th>
        </tr>
        <%
        if (bookList != null && bookList.size() > 0) {
            for (BookVo book : bookList) {
        %>
        <tr>
            <td><%= book.getBookNo() %></td>
            <td><%= book.getTitle() %></td>
            <td><%= book.getAuthor() %></td>
            <td><%= book.getPublisher() %></td>
            <td><%= book.getPublishYear() %></td>
            <td><%= book.getCategory() %></td>
            <td>
                <a href="<%= contextPath %>/books/bookDetail.do?bookNo=<%= book.getBookNo() %>" class="btn btn-edit">수정</a>
            </td>
            <td>
                <a href="javascript:confirmDelete(<%= book.getBookNo() %>)" class="btn btn-delete">삭제</a>
            </td>
        </tr>
        <% 
            }
        } else { 
        %>
        <tr>
            <td colspan="8">등록된 도서가 없습니다.</td>
        </tr>
        <% } %>
    </table>

    <!-- 페이징 -->
    <div class="pagination">
        <% for (int i = 1; i <= totalPage; i++) { %>
            <a href="<%= contextPath %>/books/updateBook.do?page=<%= i %>" class="<%= (i == currentPage) ? "active" : "" %>">
                <%= i %>
            </a>
        <% } %>
    </div>
</div>

<script>
    function confirmDelete(bookNo) {
        if (confirm("정말 이 도서를 삭제하시겠습니까?")) {
            location.href = "<%= contextPath %>/books/deleteBook.do?bookNo=" + bookNo;
        }
    }
</script>

</body>
</html>
