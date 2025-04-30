<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="Vo.BookVo, java.util.*" %>

<%
    request.setCharacterEncoding("UTF-8");
    String contextPath = request.getContextPath();
    BookVo book = (BookVo) request.getAttribute("book");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%= book.getTitle() %> - 상세 정보</title>
<link rel="stylesheet" href="<%= contextPath %>/css/common.css">
<style>
    .book-info-section {
        min-height: 80vh;
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 60px 20px;
    }

    .book-detail {
        display: flex;
        flex-direction: row;
        gap: 40px;
        max-width: 1000px;
        width: 100%;
        background-color: #fdfdfd;
        padding: 30px;
        border: 1px solid #ddd;
        border-radius: 8px;
        box-shadow: 0 4px 10px rgba(0,0,0,0.05);
    }

    .book-cover img {
        width: 200px;
        height: 270px;
        object-fit: cover;
        border: 1px solid #ccc;
        background-color: #fff;
    }

    .book-info {
        flex: 1;
        display: flex;
        flex-direction: column;
        justify-content: center;
    }

    .book-info h2 {
        font-size: 24px;
        font-weight: bold;
        margin-bottom: 16px;
        color: #222;
    }

    .book-info .meta {
        font-size: 14px;
        color: #555;
        margin-bottom: 6px;
    }

    .book-info .desc {
        margin-top: 20px;
        line-height: 1.6;
        font-size: 15px;
        color: #333;
    }

    .rental-state {
        margin-top: 16px;
        font-weight: bold;
        font-size: 15px;
        color: green;
    }

    .rental-state.unavailable {
        color: red;
    }

    .button-group {
        margin-top: 30px;
        display: flex;
        gap: 12px;
    }

    .btn {
        display: inline-block;
        padding: 8px 16px;
        border-radius: 6px;
        font-size: 14px;
        text-decoration: none;
        color: white;
        background-color: #555;
        transition: background-color 0.2s;
    }
    .btn:hover {
        background-color: #333;
    }

    .btn-loan {
        background-color: #4caf50;
    }
    .btn-loan:hover {
        background-color: #388e3c;
    }

    .btn-admin {
        background-color: #f44336;
    }
    .btn-admin:hover {
        background-color: #c62828;
    }
</style>
</head>
<body>

<div class="container book-info-section">
    <div class="book-detail">
        <!-- 왼쪽: 표지 -->
        <div class="book-cover">
            <img src="<%= contextPath + "/" + book.getThumbnail() %>" 
                 onerror="this.src='<%= contextPath %>/book/img/noimage.jpg';" alt="표지 이미지" />
        </div>

        <!-- 오른쪽: 도서 정보 -->
        <div class="book-info">
            <h2><%= book.getTitle() %></h2>
            <div class="meta">저자: <%= book.getAuthor() %></div>
            <div class="meta">출판사: <%= book.getPublisher() %> (<%= book.getPublishYear() %>)</div>
            <div class="meta">ISBN: <%= book.getIsbn() %></div>
            <div class="meta">분류: <%= book.getCategory() %></div>

            <div class="rental-state <%= book.getRentalState() != 0 ? "unavailable" : "" %>">
                <%
                    switch(book.getRentalState()) {
                        case 0: out.print("📗 대출 가능"); break;
                        case 1: out.print("📕 대출 중"); break;
                        case -1: out.print("❌ 분실 또는 이용 불가"); break;
                        default: out.print("알 수 없음");
                    }
                %>
            </div>

            <div class="desc"><%= book.getBookInfo() %></div>

            <!-- 버튼 영역 -->
            <div class="button-group">
                <a href="javascript:history.back();" class="btn">뒤로가기</a>
                <a href="<%= contextPath %>/books/rentalBook.do?bookNo=<%= book.getBookNo() %>" class="btn btn-loan">대출하기</a>
                <a href="<%= contextPath %>/books/editBook.do?bookNo=<%= book.getBookNo() %>" class="btn btn-admin">관리자 기능</a>
            </div>
        </div>
    </div>
</div>

</body>
</html>
