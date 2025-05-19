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
			<!-- 원래 첨부파일 이름 전달 -->
			<input type="hidden" name="originalFileName" value="${board.file}">
			<!-- 원래 배너 이미지 이름 전달 -->
			<input type="hidden" name="originalBannerName" value="${board.bannerImg}">
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
						<!-- 첨부파일 삭제 버튼 (초기에는 숨김) -->
						<button type="button" id="deleteFileBtn" style="<c:if test='${empty board.file}'>display:none;</c:if>">첨부파일 삭제</button>
    					<input type="file" name="file" id="file" class="file-input">	
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
    					<!-- 배너이미지 삭제 버튼 (초기에는 숨김) -->
		                <button type="button" id="deleteBannerBtn" style="<c:if test='${empty board.bannerImg}'>display:none;</c:if>">배너이미지 삭제</button>
    					<input type="file" name="bannerImage" id="bannerImage" class="file-input">
    					
    					<div id="bannerPreview" style="display: inline-block; margin-left: 20px;">
        					<c:if test="${not empty board.bannerImg}">
            					<img src="${contextPath}/download.do?boardId=${board.boardId}&bannerImg=${board.bannerImg}&type=banner" style="width:200px; height:auto; border:1px solid #ccc; margin-top:5px;">
        					</c:if>
    					</div>
					</td>
				</tr>
			</table>
			
			<!-- 히든 input 추가 (파일 삭제 여부 확인용) -->
			<input type="hidden" id="deleteFile" name="deleteFile" value="false">
			<input type="hidden" id="deleteBanner" name="deleteBanner" value="false">
	
		</form>
	</center>



	<script>
		// 파일 삭제 함수
		function deleteFile(fileInputId, fileNameSpanId, deleteBtnId, previewId) {
			const fileInput = document.getElementById(fileInputId);
			const fileNameSpan = document.getElementById(fileNameSpanId);
			const deleteBtn = document.getElementById(deleteBtnId);
	
			// 파일 초기화
			fileInput.value = '';
			fileNameSpan.textContent = '선택된 파일 없음';
			deleteBtn.style.display = 'none';
	
			// 이미지 미리보기 지우기 (배너일 경우)
			if (previewId) {
				const previewContainer = document.getElementById(previewId);
				if (previewContainer) {
					previewContainer.innerHTML = '';
				}
			}
	
			// 삭제 여부 히든 input 값 설정
			if (fileInputId === 'file') {
				document.getElementById('deleteFile').value = 'true'; // 파일 삭제 여부를 'true'로 설정
			}
			if (fileInputId === 'bannerImage') {
				document.getElementById('deleteBanner').value = 'true'; // 배너 이미지 삭제 여부를 'true'로 설정
			}
		}
	
		// 파일 선택시 이름 출력 및 삭제 버튼 표시
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
				
			    // 새 파일을 올렸을 때는 해당 파일의 delete 플래그를 false로 초기화! (이거 중요!)
			    if (inputId === 'file') {
			        document.getElementById('deleteFile').value = 'false';
			    } else if (inputId === 'bannerImage') {
			        document.getElementById('deleteBanner').value = 'false';
			    }
			});
		}
	
		// 일반 파일, 배너 이미지 둘 다 적용
		setupFileNameDisplay('file', 'fileName', 'deleteFileBtn');
		setupFileNameDisplay('bannerImage', 'bannerFileName', 'deleteBannerBtn');
	
		// 배너 이미지 미리보기
		function readURL(input) {
			if (input.files && input.files[0]) {
				const reader = new FileReader();
				reader.readAsDataURL(input.files[0]);
	
				reader.onload = function (e) {
					const previewContainer = document.getElementById('bannerPreview');
					previewContainer.innerHTML = ''; // 기존 이미지 제거
	
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
		
		
		// DOMContentLoaded 이벤트 발생 시 실행 (페이지의 모든 HTML 요소가 로드되면)
		document.addEventListener('DOMContentLoaded', function() {

			// 파일 선택 시 이름 표시 및 삭제 버튼/히든값 처리 로직 연결
            // setupFileNameDisplay 함수 호출은 여기에! (기존에 바로 호출했다면 이 안으로 옮기세요)
            setupFileNameDisplay('file', 'fileName', 'deleteFileBtn');
            setupFileNameDisplay('bannerImage', 'bannerFileName', 'deleteBannerBtn');

			// --- 파일 삭제 버튼에 클릭 이벤트 리스너 추가 ---

			const deleteFileBtn = document.getElementById('deleteFileBtn');
			if (deleteFileBtn) { // 요소가 존재하는지 확인
				deleteFileBtn.addEventListener('click', function() {
					// onclick에 있던 내용을 그대로 여기에!
					deleteFile('file', 'fileName', 'deleteFileBtn');
				});

                // 페이지 로드 시 원래 파일이 있으면 삭제 버튼 보이게 (기존 로직 옮겨옴)
                const originalFileNameSpan = document.getElementById('fileName');
                if (originalFileNameSpan && originalFileNameSpan.textContent.trim() !== '선택된 파일 없음' && originalFileNameSpan.textContent.trim() !== '') {
                     deleteFileBtn.style.display = 'inline-block';
                }
			}


			const deleteBannerBtn = document.getElementById('deleteBannerBtn');
			if (deleteBannerBtn) { // 요소가 존재하는지 확인
				deleteBannerBtn.addEventListener('click', function() {
					// onclick에 있던 내용을 그대로 여기에!
					deleteFile('bannerImage', 'bannerFileName', 'deleteBannerBtn', 'bannerPreview');
				});

                 // 페이지 로드 시 원래 파일이 있으면 삭제 버튼 보이게 (기존 로직 옮겨옴)
                 const originalBannerNameSpan = document.getElementById('bannerFileName');
                 if (originalBannerNameSpan && originalBannerNameSpan.textContent.trim() !== '선택된 파일 없음' && originalBannerNameSpan.textContent.trim() !== '') {
                      deleteBannerBtn.style.display = 'inline-block';
                 }
			}

		}); // DOMContentLoaded 끝
	</script>
</body>
</html>
