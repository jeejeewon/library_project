<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="Vo.RentalVo, Vo.BookVo, java.util.*" %>

<%
    request.setCharacterEncoding("UTF-8");
    String contextPath = request.getContextPath();    
    Vector<RentalVo> rentalList = (Vector<RentalVo>) request.getAttribute("rentals");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>내 대여 내역</title>
<link rel="stylesheet" href="<%= contextPath %>/css/common.css">
<style>
    .rental-section {
        min-height: 80vh;
        padding: 40px 20px;
        display: flex;
        flex-direction: column;
        justify-content: center;
    }

    .rental-title {
        font-size: 22px;
        font-weight: bold;
        margin-bottom: 30px;
        text-align: center;
    }

    .rental-container {
        display: grid;
        grid-template-columns: repeat(5, 1fr); /* 한 줄에 5개 */
        gap: 30px;
        justify-content: center;
    }

    .rental-card {
        display: flex;
        flex-direction: column;
        justify-content: center; 
        align-items: center;
        border: 1px solid #ddd;
        border-radius: 8px;
        padding: 16px;
        background-color: #fafafa;
        text-align: center;
        transition: transform 0.2s ease;
        height: 350px;
        overflow: hidden;
    }

    .rental-card:hover {
        transform: translateY(-5px);
        box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    }

    .rental-card img {
        width: 100px;
        height: 140px;
        object-fit: cover;
        margin-bottom: 12px;
        border: 1px solid #ccc;
    }

    .title {
        font-size: 14px;
        font-weight: bold;
        margin-bottom: 6px;
        color: #333;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        overflow: hidden;
        text-overflow: ellipsis;
        height: 38px;
    }

    .author {
        font-size: 12px;
        color: #666;
        margin-bottom: 8px;
    }
    
	.dates {
	    font-size: 13px;
	    color: #40474d;      /* 좀 더 진한 회색 */
	    font-weight: 500;
	    line-height: 1.5;
	}
	
	.dates .returned {
	    color: green;
	    font-weight: bold;
	}
	
	.dates .not-returned {
	    color: #d93025;  /* 구글 에러색 (붉은색) */
	    font-weight: bold;
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
    <div class="rental-title">내 대여 내역</div>

    <%
    if (rentalList != null && rentalList.size() > 0) {
    %>
    <div class="rental-container">
        <% for (RentalVo rental : rentalList) {
               BookVo book = rental.getBook(); %>
        <div class="rental-card">
            <a href="<%= contextPath %>/books/bookList.do?bookNo=<%= book.getBookNo() %>">
                <img src="<%= contextPath %>/<%= book.getThumbnail() %>"
                     onerror="this.src='<%= contextPath %>/book/img/noimage.png';" />
            </a>
            <div class="title"><%= book.getTitle() %></div>
            <div class="author"><%= book.getAuthor() %></div>
            <div class="dates">
			    대출일: <%= rental.getStartDate() != null ? rental.getStartDate().toLocalDateTime().toLocalDate() : "-" %><br>
			    반납 예정일: <%= rental.getReturnDue() != null ? rental.getReturnDue().toLocalDateTime().toLocalDate() : "-" %><br>
			    반납 여부:
			    <% if (rental.getReturnState() == 1) { %>
			        <span class="returned">반납 완료</span>
			    <% } else { %>
			        <span class="not-returned">미반납</span>
			    <% } %>
			</div>
        </div>
        <% } %>
    </div>
    <%
    } else {
    %>
    <div class="empty-message">현재 대여 중인 도서가 없습니다.</div>
    <%
    }
    %>
</div>

</body>
</html>