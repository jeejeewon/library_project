<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Vo.BookVo" %>
<%
    request.setCharacterEncoding("UTF-8");
    String contextPath = request.getContextPath();
    BookVo book = (BookVo) request.getAttribute("book");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>도서 수정</title>
    <link rel="stylesheet" href="<%= contextPath %>/css/common.css">
    <style>
        body {
            background-color: #fafafa;
        }

        .content-box {
            max-width: 700px;
            margin: 40px auto;
            padding: 30px;
            background-color: #fff;
            border: 1px solid #ddd;
            border-radius: 10px;
        }

        .content-box h2 {
            font-size: 24px;
            color: #003c83;
            text-align: center;
            margin-bottom: 30px;
        }

        form label {
            display: block;
            font-size: 14px;
            margin: 12px 0 4px;
            color: #333;
        }

        form input, form textarea, form select {
            width: 100%;
            padding: 10px;
            font-size: 14px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        form .btn-group {
            margin-top: 30px;
            text-align: center;
            display: flex;
            justify-content: center;
            gap: 12px;
        }

        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            font-size: 14px;
            color: white;
            cursor: pointer;
            text-decoration: none;
        }

        .btn-blue {
            background-color: #003c83;
        }

        .btn-blue:hover {
            background-color: #002c66;
        }

        .btn-red {
            background-color: #f44336;
        }

        .btn-red:hover {
            background-color: #c62828;
        }

        .btn-gray {
            background-color: #888;
        }

        .btn-gray:hover {
            background-color: #555;
        }

    </style>
</head>
<body>
<div class="content-box">
    <h2>도서 수정</h2>
 
    <form action="<%= contextPath %>/books/updateBook.do" method="post" enctype="multipart/form-data">
        <input type="hidden" name="bookNo" value="<%= book.getBookNo() %>">

        <label for="thumbnail">현재 썸네일</label>
			<img src="<%= contextPath %>/<%= book.getThumbnail() %>" width="100" height="140"
			     onerror="this.src='<%= contextPath %>/book/img/noimage.jpg';" alt="현재 썸네일" />
		
			<!-- 기존 썸네일 경로를 히든 필드로 보냄 -->
			<input type="hidden" name="originThumbnail" value="<%= book.getThumbnail() %>">
			
		<label for="thumbnail">새 썸네일 이미지 (선택)</label>
			<input type="file" name="thumbnail" accept="image/*"> 

        <label for="title">도서명</label>
        <input type="text" name="title" value="<%= book.getTitle() %>" required>

        <label for="author">저자</label>
        <input type="text" name="author" value="<%= book.getAuthor() %>" required>

        <label for="publisher">출판사</label>
        <input type="text" name="publisher" value="<%= book.getPublisher() %>" required>

        <label for="publishYear">출판년도</label>
        <input type="number" name="publishYear" value="<%= book.getPublishYear() %>" required>

        <label for="isbn">ISBN</label>
        <input type="text" name="isbn" value="<%= book.getIsbn() %>" required>

        <label for="category">카테고리</label>
        <input type="text" name="category" value="<%= book.getCategory() %>" required>

        <label for="bookInfo">도서 설명</label>
        <textarea name="bookInfo" rows="5"><%= book.getBookInfo() %></textarea>

        <label for="rentalState">도서 상태</label>
        <select name="rentalState" required>
            <option value="0" <%= book.getRentalState() == 0 ? "selected" : "" %>>📗 대출 가능</option>
            <option value="1" <%= book.getRentalState() == 1 ? "selected" : "" %>>📕 대출 중</option>
            <option value="-1" <%= book.getRentalState() == -1 ? "selected" : "" %>>❌ 분실 또는 이용 불가</option>
        </select>

        <div class="btn-group">
        	<a href="javascript:history.back();" class="btn btn-gray">이전으로</a>
            <button type="submit" class="btn btn-blue">수정하기</button>
            <a href="<%= contextPath %>/books/deleteBook.do?bookNo=<%= book.getBookNo() %>" class="btn btn-red" onclick="return confirm('정말 삭제하시겠습니까?');">삭제하기</a>
        </div>
    </form>

</div>
</body>
</html>
