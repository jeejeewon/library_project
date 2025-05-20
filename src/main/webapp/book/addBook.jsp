<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setCharacterEncoding("UTF-8");
    String contextPath = request.getContextPath();
    String message = (String) request.getAttribute("message");
    String script = (String) request.getAttribute("script");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>도서 등록</title>
    <link rel="stylesheet" href="<%= contextPath %>/css/common.css">
    <style>
        body {
            background-color: #fafafa;
        }

        .content-box {
            max-width: 700px;
            margin: 60px auto;
            padding: 30px;
            background-color: #fff;
            border: 1px solid #ddd;
            border-radius: 10px;
        }

        .content-box h2 {
            font-size: 24px;
            margin-bottom: 30px;
            color: #003c83;
            text-align: center;
        }

        form label {
            display: block;
            font-size: 14px;
            margin: 12px 0 4px;
            color: #333;
        }
        
        form select {
		    width: 100%;
		    padding: 10px;
		    font-size: 14px;
		    border: 1px solid #ccc;
		    border-radius: 4px;
		}

        form input, form textarea {
            width: 100%;
            padding: 10px;
            font-size: 14px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        .btn-group {
            margin-top: 30px;
            display: flex;
            justify-content: center;
            gap: 12px;
        }

        .btn {
            padding: 10px 20px;
            font-size: 14px;
            border: none;
            border-radius: 4px;
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

        .btn-gray {
            background-color: #888;
        }

        .btn-gray:hover {
            background-color: #555;
        }

        .message {
            text-align: center;
            font-size: 15px;
            margin-bottom: 20px;
            color: green;
        }
    </style>
</head>
<body>

	<%-- 등록 실패 시 alert 출력 --%>
	<% if (script != null) { %>
	    <%= script %>
	<% } %>
	
	<div class="content-box">
	    <h2>신규 입고 도서 등록</h2>
	
	    <% if (message != null) { %>
	        <div class="message"><%= message %></div>
	    <% } %>
	
	    <form action="<%= contextPath %>/books/addBook.do" method="post" enctype="multipart/form-data">
	    
	        <label for="thumbnail">썸네일 이미지</label>
	        <input type="file" name="thumbnail" accept="image/*">
	    
	        <label for="title">도서명</label>
	        <input type="text" name="title" required>
	
	        <label for="author">저자</label>
	        <input type="text" name="author" required>
	
	        <label for="publisher">출판사</label>
	        <input type="text" name="publisher" required>
	
	        <label for="publishYear">출판년도</label>
	        <input type="number" name="publishYear" required>
	
	        <label for="isbn">ISBN</label>
	        <input type="text" name="isbn" required>
	
			<label for="category">분야</label>
			<select name="category" required>
	            <option value="">분야</option>
	            <option value="문학">문학</option>
	            <option value="과학">과학</option>
	            <option value="IT">IT</option>
	            <option value="자격증">자격증</option>
	            <option value="어학">어학</option>
	            <option value="어린이">어린이</option>
	            <option value="기타">기타</option>
			</select>
	
	        <label for="bookInfo">도서 설명</label>
	        <textarea name="bookInfo" rows="5"></textarea>
	        
	        <div class="btn-group">
	            <a href="<%= contextPath %>/books/adminBook.do" class="btn btn-gray">관리자화면</a>
	            <button type="submit" class="btn btn-blue">등록하기</button>
	        </div>
	    </form>
	</div>
</body>
</html>
