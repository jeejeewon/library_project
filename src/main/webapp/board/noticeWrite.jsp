<%@page import="Vo.boardVO"%>
<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<%
request.setCharacterEncoding("UTF-8");
%>

<html>
<head>
	<title>공지사항 글쓰기 - noticeWrite.jsp</title>
	<script src="https://code.jquery.com/jquery-latest.min.js"></script>
	<style>

	.wri-mod-form {
		width:100%;
		max-width: 800px;
		margin: 20px auto;
		background-color: #fff;
		border-radius: 8px;
	}

	.form-title {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 20px;
		padding-bottom: 15px;
		border-bottom: 2px solid #003c83;
	}

	.form-title h2 {
		margin: 0;
		color: #003c83;
		font-size: 24px;
		font-weight: bold;
	}

	input[type="text"],
	textarea {
		width: 100%;
		padding: 10px;
		margin-bottom: 15px;
		border: 1px solid #ccc;
		border-radius: 4px;
		font-size: 1rem;
		box-sizing: border-box;
		resize: vertical;
	}

    textarea {
        min-height: 500px;
    }

	input[type="button"],
	input[type="submit"],
	button[type="button"] {
    	padding: 8px 18px;
    	color: #fff;
    	cursor: pointer;
	}

	input[type="submit"] {
		background-color: #003c83;
		color: white;
	}

	input[type="submit"]:hover {
		 background-color: #002c66; 
	}

	input[type="button"] {
		background-color: #f4f4f4;
		color: #424242; 
	}
	input[type="button"]:hover {
		background-color: #f2f2f2; 
	}

	button[type="button"] { 
		background-color: #f4f4f4;
		color: #424242;
		font-size: 0.9rem;
		padding: 8px 18px;
		margin-left: 10px;
		vertical-align: middle;
		border: 1px solid #dedede;
		border-radius: 4px;
	}
    button[type="button"]:hover {
        background-color: #f2f2f2;
    }

	.file-group {
		margin-bottom: 15px;
		border: 1px dashed #ccc;
		padding: 15px;
		border-radius: 4px;
		background-color: #f9f9f9;
	}
    .file-group p {
        margin-top: 0;
        margin-bottom: 5px;
        font-size: 0.9rem;
        color: #555;
    }

	.file-upload-label {
		display: inline-block;
		padding: 8px 15px;
		background-color: #003c83;
		color: white;
		border-radius: 4px;
		cursor: pointer;
		transition: background-color 0.3s ease;
		margin-right: 10px;
        vertical-align: middle;
	}

	.file-upload-label:hover {
		background-color: #002c66;
	}

	.file-input {
		display: none;
	}

	.file-name {
		font-weight: bold;
		color: #555;
        vertical-align: middle;
	}

	.checkbox-group {
		margin-top: 15px;
		margin-bottom: 20px;
        padding: 10px;
        background-color: #e9ecef;
        border-radius: 4px;
        display: inline-block;
	}

	.checkbox-label {
		cursor: pointer;
        font-size: 0.95rem;
        color: #495057;
	}

	.checkbox-label input[type="checkbox"] {
		margin-right: 5px;
        vertical-align: middle;
	}

    #bannerPreview img {
        max-width: 100%;
        height: auto;
        display: block;
        margin-top: 10px;
        border: 1px solid #ddd;
        padding: 5px;
        background-color: #fff;
    }

</style>
	
</head>
<body>
	<section class="wri-mod-form">
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
				<button type="button" id="deleteFileBtn" onclick="deleteFile('file', 'fileName', 'deleteFileBtn')" style="display:none;">첨부파일 삭제</button>
			</div>

			<textarea name="content" placeholder="내용을 입력하세요"></textarea>

			<div class="file-group">
				<p>배너 이미지를 등록하면 행사안내 게시판과 메인슬라이드에 노출됩니다.</p>
				<p>배너 이미지 권장 사이즈 1200px * 900px (4:3)</p>
				<label for="bannerImage" class="file-upload-label">배너이미지 업로드</label>
				<span class="file-name" id="bannerFileName">선택된 파일 없음</span>
				<input type="file" name="bannerImage" id="bannerImage" class="file-input">
				<button type="button" id="deleteBannerBtn" onclick="deleteFile('bannerImage', 'bannerFileName', 'deleteBannerBtn', 'bannerPreview')" style="display:none;">배너이미지 삭제</button>
				<div id="bannerPreview"></div>
			</div>
		</form>
	</section>

	<script>
		function deleteFile(fileInputId, fileNameSpanId, deleteBtnId, previewId) {
			const fileInput = document.getElementById(fileInputId);
			const fileNameSpan = document.getElementById(fileNameSpanId);
			const deleteBtn = document.getElementById(deleteBtnId);

			fileInput.value = '';
			fileNameSpan.textContent = '선택된 파일 없음';
			deleteBtn.style.display = 'none';

			if (previewId) {
				const previewContainer = document.getElementById(previewId);
				if (previewContainer) {
					previewContainer.innerHTML = '';
				}
			}
		}

		function setupFileNameDisplay(inputId, spanId, buttonId) {
			const input = document.getElementById(inputId);
			const span = document.getElementById(spanId);
			const button = document.getElementById(buttonId);

			input.addEventListener('change', function () {
				if (input.files.length > 0) {
					span.textContent = input.files[0].name;
					button.style.display = 'inline-block';
				} else {
					span.textContent = '선택된 파일 없음';
					button.style.display = 'none';
				}

				if (inputId === 'bannerImage') {
					readURL(this);
				}
			});
		}

		function readURL(input) {
			if (input.files && input.files[0]) {
				const reader = new FileReader();
				reader.onload = function (e) {
					const previewContainer = document.getElementById('bannerPreview');
					previewContainer.innerHTML = '';

					const imgPreview = document.createElement('img');
					imgPreview.src = e.target.result;
					imgPreview.style.width = '200px';
					imgPreview.style.height = 'auto';
					imgPreview.style.border = '1px solid #ccc';
					imgPreview.style.marginTop = '5px';

					previewContainer.appendChild(imgPreview);
				};
				reader.readAsDataURL(input.files[0]);
			} else {
                const previewContainer = document.getElementById('bannerPreview');
                if (previewContainer) {
                    previewContainer.innerHTML = '';
                }
            }
		}

		document.addEventListener('DOMContentLoaded', function() {
            setupFileNameDisplay('file', 'fileName', 'deleteFileBtn');
            setupFileNameDisplay('bannerImage', 'bannerFileName', 'deleteBannerBtn');

			const deleteFileBtn = document.getElementById('deleteFileBtn');
			if (deleteFileBtn) {
				deleteFileBtn.addEventListener('click', function() {
					deleteFile('file', 'fileName', 'deleteFileBtn');
				});
			}

			const deleteBannerBtn = document.getElementById('deleteBannerBtn');
			if (deleteBannerBtn) {
				deleteBannerBtn.addEventListener('click', function() {
					deleteFile('bannerImage', 'bannerFileName', 'deleteBannerBtn', 'bannerPreview');
				});
			}
		});
	</script>
</body>
</html>
