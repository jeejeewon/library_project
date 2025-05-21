<%@page import="Vo.boardVO"%>
<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%-- 이 페이지를 관리자만 접근가능하게하는 코드
<%
request.setCharacterEncoding("UTF-8");
String contextPath = request.getContextPath();
String id = (String) session.getAttribute("id"); 
System.out.println(id);
if (id == null || !id.equals("admin")) {
%>
<script>
   alert("접근 권한이 없습니다."); 
   history.back(); 
</script>
<%
}
%>
--%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<%
request.setCharacterEncoding("UTF-8");
%>

<html>
<head>
	<title>공지사항 글쓰기 - noticeWrite.jsp</title>
	<script src="https://code.jquery.com/jquery-latest.min.js"></script>
	<style>
		body {
			font-family: 'Arial', sans-serif;
			background-color: #f9f9f9;
			margin: 0;
			padding: 20px;
		}

		.container {
			max-width: 700px;
			margin: 0 auto;
			background-color: #fff;
			border-radius: 8px;
			box-shadow: 0 4px 8px rgba(0,0,0,0.1);
			padding: 30px;
		}

		.form-title {
			display: flex;
			justify-content: space-between;
			align-items: center;
			margin-bottom: 20px;
		}

		.form-title h2 {
			margin: 0;
			font-size: 24px;
		}

		input[type="text"],
		textarea {
			width: 100%;
			padding: 10px;
			border: 1px solid #ccc;
			border-radius: 4px;
			margin-bottom: 15px;
			font-size: 14px;
			box-sizing: border-box;
		}

		textarea {
			resize: vertical;
			min-height: 150px;
		}

		input[type="submit"],
		input[type="button"] {
			padding: 10px 20px;
			border: none;
			border-radius: 4px;
			background-color: #007BFF;
			color: white;
			cursor: pointer;
			font-size: 14px;
			margin-left: 10px;
		}

		input[type="button"] {
			background-color: #6c757d;
		}

		input[type="submit"]:hover,
		input[type="button"]:hover {
			opacity: 0.9;
		}

		.file-group {
			margin-bottom: 15px;
		}

		.file-upload-label {
			display: inline-block;
			font-weight: bold;
			background-color: #e9ecef;
			padding: 8px 12px;
			border-radius: 4px;
			cursor: pointer;
		}

		.file-name {
			margin-left: 10px;
			color: #555;
		}

		.file-input {
			display: none;
		}

		#bannerPreview img {
			width: 200px;
			height: auto;
			border: 1px solid #ccc;
			margin-top: 10px;
			border-radius: 4px;
		}
	</style>
</head>
<body>
	<div class="container">
		<form name="noticeWriteForm" method="post" action="${contextPath}/bbs/AddNotice.do" enctype="multipart/form-data">
			<div class="form-title">
				<h2>공지사항 글쓰기</h2>
				<div>
					<input type="button" value="취소" onclick="history.back();">
					<input type="submit" value="등록">
				</div>
			</div>

			<input type="text" name="title" placeholder="제목을 입력하세요">

			<div class="file-group">
				<label for="file" class="file-upload-label">첨부파일 업로드</label>
				<span class="file-name" id="fileName">선택된 파일 없음</span>
				<input type="file" name="file" id="file" class="file-input">
			</div>

			<textarea name="content" placeholder="내용을 입력하세요"></textarea>

			<div class="file-group">
				<p>배너 이미지를 등록하면 행사안내 게시판과 메인슬라이드에 노출됩니다.</p>
				<p>배너 이미지 권장 사이즈 1200px * 900px (4:3)</p>
				<label for="bannerImage" class="file-upload-label">배너이미지 업로드</label>
				<span class="file-name" id="bannerFileName">선택된 파일 없음</span>
				<input type="file" onchange="readURL(this)" name="bannerImage" id="bannerImage" class="file-input">
				<div id="bannerPreview"></div>
			</div>
		</form>
	</div>

	<script>
		function setupFileNameDisplay(inputId, spanId) {
			const input = document.getElementById(inputId);
			const span = document.getElementById(spanId);

			input.addEventListener('change', function () {
				if (input.files.length > 0) {
					span.textContent = input.files[0].name;
				} else {
					span.textContent = '선택된 파일 없음';
				}
			});
		}

		setupFileNameDisplay('file', 'fileName');
		setupFileNameDisplay('bannerImage', 'bannerFileName');

		function readURL(input) {
			if (input.files && input.files[0]) {
				const reader = new FileReader();
				reader.onload = function (e) {
					const previewContainer = document.getElementById('bannerPreview');
					previewContainer.innerHTML = ''; // clear previous

					const imgPreview = document.createElement('img');
					imgPreview.src = e.target.result;
					previewContainer.appendChild(imgPreview);
				};
				reader.readAsDataURL(input.files[0]);
			}
		}
	</script>
</body>
</html>
