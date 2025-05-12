<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="Vo.RentalVo, Vo.BookVo, java.util.*" %>

<%
    request.setCharacterEncoding("UTF-8");
    String contextPath = request.getContextPath();    
    Vector<RentalVo> rentalList = (Vector<RentalVo>) request.getAttribute("rentalList");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>전체 대여 목록</title>
<link rel="stylesheet" href="<%= contextPath %>/css/common.css">
<style>
    .rental-section {
        min-height: 80vh;
        padding: 40px 20px;
    }

    .rental-title {
        font-size: 22px;
        font-weight: bold;
        margin-bottom: 50px;
        text-align: center;
    }

    table {
        width: 100%;
        border-collapse: collapse;
        font-size: 14px;
    }

    th, td {
        padding: 10px;
        border: 1px solid #dedede;
        text-align: center;
    }

    th {
        background-color: #f0f0f0;
    }

    .btn-return {
        background-color: #003c83;
        color: white;
        padding: 5px 10px;
        border-radius: 4px;
        text-decoration: none;
        font-size: 12px;
    }

    .btn-return:hover {
        background-color: #00275a;
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

<div class="container rental-section">
    <div class="rental-title">전체 대여 목록 (관리자)</div>

    <%
    if (rentalList != null && rentalList.size() > 0) {
    %>
    <table>
        <thead>
            <tr>
                <th>대여번호</th>
                <th>사용자 ID</th>
                <th>도서 제목</th>
                <th>대출일</th>
                <th>반납 예정일</th>
                <th>반납 여부</th>
                <th>반납 처리</th>
            </tr>
        </thead>
        <tbody>
            <% for (RentalVo rental : rentalList) {
                   BookVo book = rental.getBook(); %>
            <tr>
                <td><%= rental.getRentNo() %></td>
                <td><%= rental.getUserId() %></td>
                <td>
                    <a href="<%= contextPath %>/books/bookDetail.do?bookNo=<%= book.getBookNo() %>">
                        <%= book.getTitle() %>
                    </a>
                </td>
                <td><%= rental.getStartDate() != null ? rental.getStartDate().toLocalDateTime().toLocalDate() : "-" %></td>
                <td><%= rental.getReturnDue() != null ? rental.getReturnDue().toLocalDateTime().toLocalDate() : "-" %></td>
                <td><%= rental.getReturnState() == 1 ? "반납 완료" : "미반납" %></td>
                <td>
                    <% if (rental.getReturnState() == 0) { %>
                        <a href="<%= contextPath %>/books/returnConfirm.do?rentNo=<%= rental.getRentNo() %>" class="btn-return">반납 처리</a>
                    <% } else { %>
                        완료
                    <% } %>
                </td>
            </tr>
            <% } %>
        </tbody>
    </table>
    <%
    } else {
    %>
    <div class="empty-message">현재 대여 중인 데이터가 없습니다.</div>
    <%
    }
    %>
</div>

</body>
</html>
