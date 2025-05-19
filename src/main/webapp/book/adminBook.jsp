<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setCharacterEncoding("UTF-8");
    String contextPath = request.getContextPath();
    String userId = (String) session.getAttribute("id");
    if (userId == null || !"admin".equals(userId)) {
        response.sendRedirect(contextPath + "/members/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>관리자 도서 관리</title>
    <link rel="stylesheet" href="<%= contextPath %>/css/common.css">
    <style>
        body {
            background-color: #f8f9fa;
        }

        .admin-wrapper {
            max-width: 1100px;
            margin: 60px auto;
            padding: 30px 20px;
        }

        .admin-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 40px;
        }

        .admin-header h2 {
            font-size: 26px;
            font-weight: bold;
            color: #003c83;
        }

        .btn-main {
            background-color: #003c83;
            color: white;
            padding: 8px 16px;
            border-radius: 4px;
            font-size: 14px;
            text-decoration: none;
        }

        .btn-main:hover {
            background-color: #002c66;
        }

        .grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
            gap: 24px;
        }

        .card {
            background-color: #ffffff;
            border-radius: 10px;
            padding: 24px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
            text-align: center;
            transition: transform 0.2s ease;
        }

        .card:hover {
            transform: translateY(-4px);
        }

        .card h3 {
            font-size: 18px;
            color: #333;
            margin-bottom: 12px;
        }

        .card p {
            font-size: 14px;
            color: #666;
            margin-bottom: 20px;
            line-height: 1.5;
        }

        .card .btn {
            background-color: #003c83;
            color: white;
            padding: 8px 14px;
            border-radius: 4px;
            font-size: 13px;
            text-decoration: none;
        }

        .card .btn:hover {
            background-color: #002c66;
        }

        @media (max-width: 600px) {
            .admin-header {
                flex-direction: column;
                gap: 12px;
            }
        }
    </style>
</head>
<body>
<div class="admin-wrapper">
    <div class="admin-header">
        <h2>관리자 도서 관리</h2>
        <a href="<%= contextPath %>/main.jsp" class="btn-main">메인으로</a>
    </div>

    <div class="grid">
        <div class="card">
            <h3>📕 도서 등록</h3>
            <p>신규 도서 추가</p>
            <a href="<%= contextPath %>/books/addBookForm.do" class="btn">등록하기</a>
        </div>

        <div class="card">
            <h3>📘 도서 수정/삭제</h3>
            <p>기존 도서 정보 수정 삭제</p>
            <a href="<%= contextPath %>/books/updateBook.do" class="btn">관리하기</a>
        </div>

        <div class="card">
            <h3>📙 반납 처리</h3>
            <p>대여 중인 도서의 반납 관리</p>
            <a href="<%= contextPath %>/books/returnBook.do" class="btn">반납 관리</a>
        </div>
        
        <div class="card">
            <h3>📗 전체 대여 내역</h3>
            <p>모든 사용자의 대여 기록 확인</p>
            <a href="<%= contextPath %>/books/allRental.do" class="btn">내역 보기</a>
        </div>
    </div>
</div>
</body>
</html>
