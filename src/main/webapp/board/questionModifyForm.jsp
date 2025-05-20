<%@page import="VO.boardVO"%>
<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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

<title>문의사항 글수정 - questionModifyForm.jsp</title>


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


		<form name="questionWriteForm" method="post" action="${contextPath}/bbs/questionModify.do" enctype="multipart/form-data">
		
			<!-- 수정 대상 게시글 ID 전달 -->
			<input type="hidden" name="boardId" value="${board.boardId}">
		
			<div class="form-title">
				<h2>문의사항 글 수정</h2>
				<div>
					<button type="button" onclick="location.href='${contextPath}/bbs/questionInfo.do?boardId=${board.boardId}'">취소</button>
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
						<button type="button" id="deleteFileBtn" onclick="deleteFile('file', 'fileName', 'deleteFileBtn')" style="<c:if test='${empty board.file}'>display:none;</c:if>">첨부파일 삭제</button>
    					<input type="file" name="file" id="file" class="file-input">	
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<textarea name="content" rows="10" cols="50" placeholder="내용을 입력하세요" style="width: 100%;">${board.content}</textarea>
					</td>
				</tr>
			</table>
			
			<div class="checkbox-group">
				<label for="secret" class="checkbox-label">
					<input type="checkbox" name="secret" id="secret" value="on" <c:if test="${board.secret}">checked</c:if> />
					비밀글로 설정
				</label>
			</div>
			
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
			});
		}
	
		
		setupFileNameDisplay('file', 'fileName', 'deleteFileBtn');
	


	</script>
	
</body>
</html>