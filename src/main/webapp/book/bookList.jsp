<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*, Vo.BookVo" %>

<%
    request.setCharacterEncoding("UTF-8");
    String contextPath = request.getContextPath();
    Vector<BookVo> bookList = (Vector<BookVo>) request.getAttribute("v");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>도서 목록</title>
<link rel="stylesheet" href="<%= contextPath %>/css/common.css">
<style>
    .book-section {
        padding: 40px 20px;
    }

    .book-title {
        font-size: 24px;
        font-weight: bold;
        text-align: center;
        margin-bottom: 30px;
    }

    .book-container {
        display: grid;
        grid-template-columns: repeat(4, 1fr); /* 가로 4칸 */
        gap: 30px;
    }

    .book-card {
        display: flex;
        flex-direction: column;
        justify-content: center;         /* ★ 수직 가운데 정렬 */
        align-items: center;            /* 수평 가운데 정렬 */
        border: 1px solid #ddd;
        border-radius: 8px;
        padding: 16px;
        background-color: #fafafa;
        transition: transform 0.2s ease;
        height: 280px;
        overflow: hidden;
        text-align: center;
    }

    .book-card:hover {
        transform: translateY(-5px);
        box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    }

    .book-card img {
        width: 100px;
        height: 140px;
        object-fit: cover;
        margin-bottom: 12px;
        border: 1px solid #ccc;
        background-color: #fff;
    }

	.book-card .title {
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

    .book-card .author {
        font-size: 12px;
        color: #666;
    }

    .empty-message {
        text-align: center;
        padding: 40px;
        font-size: 16px;
        color: #999;
    }

    .pagination {
        margin-top: 40px;
        text-align: center;
    }
    .pagination a {
        display: inline-block;
        margin: 0 4px;
        padding: 6px 12px;
        border: 1px solid #ccc;
        color: #333;
        text-decoration: none;
        border-radius: 4px;
        font-size: 14px;
    }
    .pagination a:hover {
        background-color: #f2f2f2;
    }
    .pagination .active {
        background-color: #333;
        color: white;
        border-color: #333;
    }
</style>
</head>
<body>

<div class="container book-section">

    <!-- 검색창 -->
	<div class="search-box" style="text-align: right; margin-bottom: 30px; padding-right: 20px;">
	    <form action="<%= contextPath %>/books/bookSearch.do" method="get" style="display: inline-block;">
	        <input type="text" name="keyword" placeholder="도서명, 저자, 출판사, 분야 검색"
	               style="width: 250px; padding: 6px; border: 1px solid #ccc; border-radius: 4px;" />
	        <button type="submit"
	                style="padding: 6px 12px; background-color: #4caf50; color: white; border: none; border-radius: 4px; cursor: pointer;">
	            검색
	        </button>
	    </form>
	</div>
    
    
    <div class="book-title">전체 도서 목록</div>

    <%
    if (bookList != null && bookList.size() > 0) {
    %>
    
    <div class="book-container">
        <%
        for (BookVo book : bookList) {
        %>
        <div class="book-card">
            <a href="<%= contextPath %>/books/bookInfo.do?bookNo=<%= book.getBookNo() %>">
                <img src="<%= contextPath + "/" + book.getThumbnail() %>"
                     onerror="this.src='<%= contextPath %>/book/img/noimage.jpg';" alt="썸네일" />
            </a>
            <div class="title"><%= book.getTitle() %></div>
            <div class="author"><%= book.getAuthor() %></div>
        </div>
        <%
        }
        %>
    </div>

    <!-- 페이징 버튼 (정적) -->
    <div class="pagination">
        <a href="#">◀</a>
        <a href="#" class="active">1</a>
        <a href="#">2</a>
        <a href="#">3</a>
        <a href="#">▶</a>
    </div>

    <%
    } else {
    %>
    <div class="empty-message">등록된 도서가 없습니다.</div>
    <%
    }
    %>
</div>

</body>
</html>
