<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Vo.BookVo" %>
<%
    request.setCharacterEncoding("UTF-8");
    String contextPath = request.getContextPath();
    BookVo book = (BookVo) request.getAttribute("book");
    String userId = (String) session.getAttribute("id");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= book.getTitle() %> - 상세 정보</title>
    <link rel="stylesheet" href="<%= contextPath %>/css/common.css">
    <style>
        body {
            background-color: #fafafa;
        }

        .content-box {
            max-width: 1000px;
            margin: 0 auto;
            padding: 40px 20px;
        }

        .back-btn {
            margin-bottom: 20px;
            text-align: left;
        }

        .back-btn a {
            padding: 6px 14px;
            font-size: 14px;
            border-radius: 4px;
            background-color: #003c83;
            color: white;
            text-decoration: none;
        }

        .back-btn a:hover {
            background-color: #002c66;
        }

        .detail-card {
            display: grid;
            grid-template-columns: minmax(180px, 1fr) 2fr;
            gap: 32px;
            background-color: white;
            border-radius: 10px;
            padding: 32px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.06);
            align-items: start;
        }

        .thumb {
            width: 100%;
            max-width: 180px;
            height: auto;
            aspect-ratio: 3 / 4;
            object-fit: cover;
            border: 1px solid #ccc;
            border-radius: 6px;
        }

        .book-info-box {
            display: flex;
            flex-direction: column;
        }

        .book-title {
            font-size: 24px;
            font-weight: bold;
            color: #333;
            margin-bottom: 16px;
        }

        .book-meta {
            font-size: 15px;
            color: #555;
            margin-bottom: 8px;
        }

        .book-info {
            margin-top: 20px;
            font-size: 15px;
            color: #444;
            line-height: 1.7;
        }

        .rental-state {
            margin-top: 18px;
            font-weight: bold;
            font-size: 15px;
            color: green;
        }

        .rental-state.unavailable {
            color: red;
        }

        .btn-group {
            margin-top: auto;
            display: flex;
            justify-content: flex-end;
            gap: 10px;
        }

        .btn {
            padding: 10px 18px;
            border-radius: 4px;
            font-size: 14px;
            text-decoration: none;
            color: white;
            background-color: #003c83;
            border: none;
            cursor: pointer;
        }

        .btn:hover {
            background-color: #002c66;
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

        .btn[disabled] {
            background-color: #ccc !important;
            cursor: not-allowed;
        }

        @media (max-width: 768px) {
            .detail-card {
                grid-template-columns: 1fr;
                text-align: center;
            }

            .btn-group {
                justify-content: center;
                margin-top: 20px;
            }
        }
    </style>
</head>
<body>
<div class="content-box">
    <div class="back-btn">
        <a href="javascript:history.back();">이전으로</a>
    </div>
	<br>
	
    <div class="detail-card">
        <img class="thumb" src="<%= contextPath %>/<%= book.getThumbnail() %>"
             onerror="this.src='<%= contextPath %>/book/img/noimage.jpg';" alt="표지 이미지" />

        <div class="book-info-box">
            <div class="book-title"><%= book.getTitle() %></div>
            <div class="book-meta">저자: <%= book.getAuthor() %></div>
            <div class="book-meta">출판사: <%= book.getPublisher() %> (<%= book.getPublishYear() %>)</div>
            <div class="book-meta">ISBN: <%= book.getIsbn() %></div>
            <div class="book-meta">분류: <%= book.getCategory() %></div>
            <div class="book-meta">대여횟수: <%= book.getRentCount() %></div>

            <div class="rental-state <%= book.getRentalState() != 0 ? "unavailable" : "" %>">
                <% switch(book.getRentalState()) {
                    case 0: out.print("📗 대출 가능"); break;
                    case 1: out.print("📕 대출 중"); break;
                    case -1: out.print("❌ 분실 또는 이용 불가"); break;
                    default: out.print("알 수 없음");
                } %>
            </div>

			<!-- 책소개 -->
			<div class="book-info"><%= book.getBookInfo().replaceAll("\n", "<br>") %></div>
				
            <div class="btn-group">
                <% if (userId != null) { %>
                    <% if (book.getRentalState() == 0) { %>
                        <form action="<%= contextPath %>/books/confirmRental.do" method="get" style="display:inline;">
                            <input type="hidden" name="bookNo" value="<%= book.getBookNo() %>">
                            <button type="submit" class="btn btn-loan">대여하기</button>
                        </form>
                    <% } else if (book.getRentalState() == 1) { %>
                        <button class="btn btn-loan" disabled>대출 중입니다</button>
                    <% } else if (book.getRentalState() == -1) { %>
                        <button class="btn btn-loan" disabled>이용 불가</button>
                    <% } %>

                    <% if ("admin".equals(userId)) { %>
                        <a href="<%= contextPath %>/books/adminBook.do?bookNo=<%= book.getBookNo() %>" class="btn btn-admin">관리자기능</a>
                    <% } %>
                <% } else { %>
                    <a href="<%= contextPath %>/member/login" class="btn btn-loan">대여하기</a>
                <% } %>
            </div>
        </div>
    </div>

	<!-- 한줄서평 -->
	<div>	    
		<%-- 서평 작성 폼 --%>
		<jsp:include page="../board/myReviewWrite.jsp" flush="true"/>
		<%-- 서평 리스트 출력 --%>
		<jsp:include page="../board/bookReviewList.jsp" flush="true"/>
	</div>
</div>
</body>
</html>
