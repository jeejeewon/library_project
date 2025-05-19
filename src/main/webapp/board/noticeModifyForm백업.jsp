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

<title>공지사항 글수정 - noticeModifyForm.jsp</title>


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


		<form name="noticeWriteForm" method="post" action="${contextPath}/bbs/noticeModify.do" enctype="multipart/form-data">
		
			<!-- 수정 대상 게시글 ID 전달 -->
			<input type="hidden" name="boardId" value="${board.boardId}">
		
			<div class="form-title">
				<h2>공지사항 글 수정</h2>
				<div>
					<button type="button" onclick="location.href='${contextPath}/bbs/noticeInfo.do?boardId=${board.boardId}'">취소</button>
					<input type="submit" value="수정">
				</div>
			</div>
		
			
			<table align="center" border="1">
				<tr>
					<td>
						<input type="text" name="title" placeholder="제목을 입력하세요" style="width: 100%;" value="${board.title}" onfocus="this.select()">
					</td>
					<td>
					    <label for="file" class="file-upload-label">첨부파일 업로드</label>
					    <span class="file-name" id="fileName">
					        <c:choose>
					            <c:when test="${not empty board.file}">
					                ${board.file}
					            </c:when>
					            <c:otherwise>
					                선택된 파일 없음
					            </c:otherwise>
					        </c:choose>
					    </span>
					
					    <!-- 첨부파일 삭제 버튼 -->
					    <button type="button" id="deleteFileBtn" 
					        onclick="deleteFile('file', 'fileName', 'deleteFileBtn')" 
					        style="<c:if test='${empty board.file}'>display:none;</c:if>">첨부파일 삭제</button>
					
					    <!-- 실제 파일 input -->
					    <input type="file" name="file" id="file" class="file-input">
					
					    <!-- 기존 파일 이름 전송 -->
					    <input type="hidden" name="originalFileName" value="${board.file}">
					    <!-- 삭제 여부 전송 -->
					    <input type="hidden" name="deleteFile" id="deleteFile" value="false">
					</td>

				</tr>
				<tr>
					<td colspan="2">
						<textarea name="content" rows="10" cols="50" placeholder="내용을 입력하세요" style="width: 100%;">${board.content}</textarea>
					</td>
				</tr>
				<tr>
					<td colspan="2">
					    <label for="bannerImage" class="file-upload-label">배너이미지 업로드</label>
					    <span class="file-name" id="bannerFileName">
					        <c:choose>
					            <c:when test="${not empty board.bannerImg}">
					                ${board.bannerImg}
					            </c:when>
					            <c:otherwise>
					                선택된 파일 없음
					            </c:otherwise>
					        </c:choose>
					    </span>
					
					    <!-- 배너이미지 삭제 버튼 -->
					    <button type="button" id="deleteBannerBtn" 
					        onclick="deleteFile('bannerImage', 'bannerFileName', 'deleteBannerBtn', 'bannerPreview')" 
					        style="<c:if test='${empty board.bannerImg}'>display:none;</c:if>">배너이미지 삭제</button>
					
					    <!-- 실제 배너 이미지 input -->
					    <input type="file" name="bannerImage" id="bannerImage" class="file-input">
					
					    <!-- 기존 배너 이미지 이름 전송 -->
					    <input type="hidden" name="originalBannerName" value="${board.bannerImg}">
					    <!-- 삭제 여부 전송 -->
					    <input type="hidden" name="deleteBanner" id="deleteBanner" value="false">
					
					    <!-- 미리보기 -->
					    <div id="bannerPreview" style="display: inline-block; margin-left: 20px;">
					        <c:if test="${not empty board.bannerImg}">
					            <img src="${contextPath}/download.do?boardId=${board.boardId}&bannerImg=${board.bannerImg}&type=banner" 
					                 style="width:200px; height:auto; border:1px solid #ccc; margin-top:5px;">
					        </c:if>
					    </div>
					</td>
				</tr>
			</table>
		</form>

	</center>
	<script>
		// 파일 삭제 함수
		function deleteFile(fileInputId, fileNameSpanId, deleteBtnId, previewId) {
			const fileInput = document.getElementById(fileInputId);
			const fileNameSpan = document.getElementById(fileNameSpanId);
			const deleteBtn = document.getElementById(deleteBtnId);
	
			// 파일 input 초기화
			fileInput.value = '';
			fileNameSpan.textContent = '선택된 파일 없음';
			deleteBtn.style.display = 'none';
	
			// 이미지 미리보기 제거 (배너일 경우)
			if (previewId) {
				const previewContainer = document.getElementById(previewId);
				if (previewContainer) {
					previewContainer.innerHTML = '';
				}
			}
	
			// 🔥 삭제 여부를 나타내는 hidden input 값 true로 설정
			if (fileInputId === 'file') {
				document.getElementById('deleteFile').value = 'true';
			}
			if (fileInputId === 'bannerImage') {
				document.getElementById('deleteBanner').value = 'true';
			}
		}
	
		// 파일 선택 시 파일명 표시 및 삭제 버튼 노출
		function setupFileNameDisplay(inputId, spanId, buttonId, deleteHiddenId) {
			const input = document.getElementById(inputId);
			const span = document.getElementById(spanId);
			const button = document.getElementById(buttonId);
			const deleteHidden = document.getElementById(deleteHiddenId);
	
			input.addEventListener('change', function () {
				if (input.files.length > 0) {
					span.textContent = input.files[0].name;
					button.style.display = 'inline-block';
	
					// 🔥 새 파일을 선택한 경우 삭제 플래그는 false로 리셋
					if (deleteHidden) {
						deleteHidden.value = 'false';
					}
				} else {
					span.textContent = '선택된 파일 없음';
					button.style.display = 'none';
				}
			});
		}
	
		// 일반 파일과 배너 이미지 각각 적용
		setupFileNameDisplay('file', 'fileName', 'deleteFileBtn', 'deleteFile');
		setupFileNameDisplay('bannerImage', 'bannerFileName', 'deleteBannerBtn', 'deleteBanner');
	
		// 배너 이미지 미리보기
		function readURL(input) {
			if (input.files && input.files[0]) {
				const reader = new FileReader();
				reader.readAsDataURL(input.files[0]);
	
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
			}
		}
	
		document.getElementById('bannerImage').addEventListener('change', function () {
			readURL(this);
		});
	</script>

	
</body>
</html>