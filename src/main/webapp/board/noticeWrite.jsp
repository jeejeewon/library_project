<%@page import="VO.boardVO"%>
<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<%
request.setCharacterEncoding("UTF-8");
%>

<html>
<head>

<title>공지사항 글쓰기 - noticeWrite.jsp</title>


<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<style>
.file-upload-label {
	display: inline-block;
	margin-right: 10px;
	font-weight: bold;
	background-color: #f0f0f0;
	padding: 5px 10px;
}

.file-name {
	font-style: italic;
	color: #333;
}

.file-input {
	display: none; /* 커스텀 라벨만 보이게 하려면 숨김 */
}

.form-title {
	display: flex;
	justify-content: space-between;
}
</style>

</head>
<body>
	<center>

		<form name="noticeWriteForm" method="post" action="${contextPath}/bbs/AddNotice.do" enctype="multipart/form-data">
		
			<div class="form-title">
				<h2>공지사항 글쓰기</h2>
				<div>
					<input type="reset" value="취소"> 
					<input type="submit" value="등록">
				</div>
			</div>
		
			<table align="center" border="1">
				<tr>
					<td>
						<input type="text" name="title" placeholder="제목을 입력하세요" style="width: 100%;">
					</td>
					<td>
						<label for="file" class="file-upload-label">첨부파일 업로드</label>
						<span class="file-name" id="fileName">선택된 파일 없음</span> 
						<input type="file" name="file" id="file" class="file-input">
					</td>
				</tr>
				<tr>
					<td colspan="2"><textarea name="content" rows="10" cols="50" placeholder="내용을 입력하세요" style="width: 100%;"></textarea></td>
				</tr>
				<tr>
					<td colspan="2">
						<label for="bannerImage" class="file-upload-label">배너이미지 업로드</label>
						<span class="file-name" id="bannerFileName">선택된 파일 없음</span> 
						<input type="file" onchange="readURL(this)" name="bannerImage" id="bannerImage" class="file-input">
						<div id="bannerPreview" style="display: inline-block; margin-left: 20px;"></div>
					</td>
				</tr>
			</table>

		</form>

	</center>
	<script>
		function setupFileNameDisplay(inputId, spanId) {
			const input = document.getElementById(inputId);
			const span = document.getElementById(spanId);

			input.addEventListener('change', function() {
				if (input.files.length > 0) {
					span.textContent = input.files[0].name;
				} else {
					span.textContent = '선택된 파일 없음';
				}
			});
		}

		// 두 파일 입력 요소 각각에 대해 적용
		setupFileNameDisplay('file', 'fileName');
		setupFileNameDisplay('bannerImage', 'bannerFileName');

		// 배너이미지 업로드시, 미리보기 기능을 구현하는 함수
		function readURL(input) {
			if (input.files && input.files[0]) {
				const reader = new FileReader();
				reader.readAsDataURL(input.files[0]);

				reader.onload = function(e) {
					const previewContainer = document
							.getElementById('bannerPreview');

					// 이전에 표시된 이미지 제거 (있을 경우)
					previewContainer.innerHTML = '';

					// 새 이미지 생성
					const imgPreview = document.createElement('img');
					imgPreview.src = e.target.result;
					imgPreview.style.width = '200px';
					imgPreview.style.height = 'auto';
					imgPreview.style.border = '1px solid #ccc';
					imgPreview.style.marginTop = '5px';

					// 이미지 삽입
					previewContainer.appendChild(imgPreview);
				};
			}
		}
	</script>
</body>
</html>