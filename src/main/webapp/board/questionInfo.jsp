<%@page import="Vo.boardVO"%>
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
	<title>문의사항 상세페이지 - questionInfo.jsp</title>
	<!-- jquery -->
	<script src="https://code.jquery.com/jquery-latest.min.js"></script>
	<style>
		/* 기본 리셋 */
		* {
			margin: 0;
			padding: 0;
			box-sizing: border-box;
		}

		body {
			font-family: 'Arial', sans-serif;
			background-color: #f4f7fc;
			color: #333;
			line-height: 1.6;
		}

		/* 중앙 정렬을 위한 스타일 */
		.center-wrapper, .bottom-wrapper {
			max-width: 960px;
			margin: 30px auto;
			padding: 20px;
			background-color: #fff;
			border-radius: 8px;
			box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
		}

		/* 제목 영역 스타일 */
		.title-area {
			display: flex;
			justify-content: space-between;
			border-bottom: 2px solid #eee;
			padding-bottom: 20px;
			margin-bottom: 20px;
		}

		.title-area .left, .title-area .right {
			width: 48%;
		}

		.title-area .left p, .title-area .right p {
			font-size: 18px;
			font-weight: bold;
		}

		/* 게시글 내용 영역 스타일 */
		.content-area {
			margin-bottom: 30px;
		}

		.content-area p {
			font-size: 16px;
			line-height: 1.8;
			color: #555;
		}


	    /* 첨부파일 영역 스타일 */
		.file {
		    margin-top: 20px;
		    padding: 15px;
		    background-color: #f9f9f9;
		    border-radius: 8px;
		    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
		    display: flex;
		    align-items: center;
		    gap: 15px;
		}
		
		.file img {
		    max-width: 150px;
		    border-radius: 8px;
		    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
		}
		
		.file a.download-link {
		    font-size: 14px;
		    color: white;
		    background-color: #007bff;
		    text-decoration: none;
		    font-weight: bold;
		    padding: 8px 16px;
		    border-radius: 4px;
		    display: inline-block;
		    margin-left: 10px;
		    transition: background-color 0.3s ease, color 0.3s ease;
		    text-align: center;
		}
		
		.file a.download-link:hover {
		    background-color: #0056b3;
		    color: #fff;
		}
		
		.file span {
		    font-size: 14px;
		    color: #555;
		    margin-left: 10px;
		}

		/* 페이징 버튼 */
		.paging {
			display: flex;
			justify-content: flex-end;
			gap: 5px;
			margin-bottom: 20px;
		}

		.paging form button {
			padding: 10px 20px;
			border: none;
			background-color: #f4f7fc;
			color: #007bff;
			cursor: pointer;
			font-size: 14px;
			border-radius: 4px;
		}

		.paging form button:hover {
			background-color: #e0e0e0;
		}
	
		.board-info-bottom{
			display: flex;
            justify-content: space-between;
		}
		
		/* '수정', '삭제', '목록', 'TOP' 버튼 스타일 */
		.board-info-bottom a,
		.board-info-bottom button,
		.board-info-bottom form button {
			padding: 8px 16px;
			font-size: 14px;
			border-radius: 4px;
			background-color: #007bff;
			color: white;
			border: none;
			cursor: pointer;
			text-decoration: none;
			margin: 0 5px;
		}
        .board-info-bottom a:hover,
		.board-info-bottom button:hover,
		.board-info-bottom form button:hover {
			background-color: #0056b3;
		}

		/* 버튼을 가로로 정렬 */
		.board-info-bottom-left,
		.board-info-bottom-right {
			display: flex;
			justify-content: flex-start;
			gap: 10px;
		}

		.board-info-bottom-right {
			justify-content: flex-end;
		}

		/* 반응형 디자인 */
		@media screen and (max-width: 768px) {
			.title-area {
				flex-direction: column;
				align-items: center;
			}

			.title-area .left,
			.title-area .right {
				width: 100%;
				text-align: center;
				margin-bottom: 10px;
			}

			.board-info-bottom {
				flex-direction: column;
				align-items: center;
			}

			.board-info-bottom-left,
			.board-info-bottom-right {
				justify-content: center;
			}

			.paging {
				flex-direction: column;
				align-items: center;
				gap: 10px;
				
			}
		}
	</style>
</head>

<body>

	<div class="center-wrapper">
	
		<div class="paging">
		    <!-- 이전글 버튼 (이전글이 있을 경우만 표시) -->
			<c:if test="${getPreBoardId > 0}">
			    <form action="${pageContext.request.contextPath}/bbs/questionInfo.do" method="get">
			        <input type="hidden" name="boardId" value="${getPreBoardId}">
			        <button type="submit">이전글</button>
			    </form>
			</c:if>
			
			<!-- 다음글 버튼 (다음글이 있을 경우만 표시) -->
			<c:if test="${getNextBoardId > 0}">
			    <form action="${pageContext.request.contextPath}/bbs/questionInfo.do" method="get">
			        <input type="hidden" name="boardId" value="${getNextBoardId}">
			        <button type="submit">다음글</button>
			    </form>
			</c:if>
			<form action="${pageContext.request.contextPath}/bbs/questionList.do" method="get">
				<button type="submit">목록</button>
			</form>
		</div>

		<div class="title-area">
			<div class="left">
					<c:if test="${board.secret}">
						<span style="color: gray;">🔒 비밀글</span>
					</c:if>
				<span>${board.title}</span><!-- 제목 -->
				<p>작성자: ${board.userId}</p>
			</div>
			<div class="right">
                <!-- 작성일 출력 시 포맷팅 처리 -->
                <p>작성일: <fmt:formatDate value="${board.createdAt}" pattern="yyyy-MM-dd HH:mm" /></p>
                <p>조회수: ${board.views}</p>
			</div>
		</div>

		<div class="content-area">
			<p><c:out value="${board.content}" /></p><!-- 게시글 내용 -->
			<c:if test="${not empty board.file}"><!-- 첨부파일이 있을 경우에만 나타나도록 설정 -->
				<div class="file">
					<img src="${contextPath}/download.do?boardId=${board.boardId}&file=${board.file}&type=file" id="preview1" alt="첨부 이미지">
					<a href="${contextPath}/download.do?boardId=${board.boardId}&file=${board.file}&type=file" download class="download-link">첨부파일</a>
					<span>${board.file}</span>
				</div>
			</c:if>
		</div>

		<div class="board-info-bottom">
			<div class="board-info-bottom-left">
				<a href="${contextPath}/bbs/questionModifyForm.do?boardId=${board.boardId}">수정</a>
				<a href="#" onclick="fn_remove_board('${contextPath}/bbs/removeQuestion.do', ${board.boardId})">삭제</a>
			</div>
			<div class="board-info-bottom-right">
				<form action="${pageContext.request.contextPath}/bbs/questionList.do" method="get">
					<button type="submit">목록</button>
				</form>
				<button onclick="scrollToTop()">TOP</button>
			</div>
		</div>
	</div>
	
	<div class="bottom-wrapper">
		<div class="content-area">

			<%-- 여기에 답변 내용이 표시됨 --%>
			<div class="reply-display-area">
				<div id="replyContent"><c:out value="${board.reply}" /></div>
			</div>

			<%-- 답변이 있을 때만 보이게 할 수정/삭제 버튼 영역 --%>
			<div id="replyButtons" style="display: none;"> <%-- 기본은 숨겨놓기 --%>
				<button id="editReplyBtn" type="button">답변수정</button> <%-- 수정 버튼 추가! --%>
				<button id="deleteReplyBtn" type="button">답변삭제</button>
			</div>

			<%-- 답변이 없을 때만 보이게 할 "답변달기" 버튼 --%>
			<button type="button" id="replyBtn">답변달기</button>

			<!-- 여기에 답변 폼이 동적으로 삽입됨 (Ajax) -->
			<div id="replyFormArea"></div>

		</div>
	</div>

	

	
	
	<script type="text/javascript">
	
	    // fn_remove_board 함수 정의
	    function fn_remove_board(url, boardId) {
	        // 삭제하려는 글의 URL과 boardId 값을 콘솔에 출력하여 확인
	        console.log("fn_remove_board called with URL:", url, "and boardId:", boardId);
	
	        // 삭제 확인을 위한 팝업
	        if (!confirm("정말 삭제하시겠어요?")) return; // 사용자가 취소를 클릭하면 함수 종료
	
	        // fetch를 사용해 서버에 삭제 요청을 보냄
	        fetch(url, {
	            method: "POST", // POST 방식으로 요청
	            headers: {
	                "Content-Type": "application/x-www-form-urlencoded" // 폼 데이터 형식으로 전송
	            },
	            body: new URLSearchParams({ boardId: boardId }) // boardId를 URL 파라미터로 보냄
	        })
	        .then(res => {
	            // 응답 상태가 정상인지 확인
	            console.log("Remove Response Status:", res.status); // 응답 상태 코드 로그
	            if (!res.ok) { // 응답이 정상적이지 않으면 에러 처리
	                throw new Error('HTTP error! status: ' + res.status); // 에러 발생
	            }
	            return res.json(); // 정상 응답일 경우, JSON 형식으로 변환
	        })
	        .then(data => {
	            // 서버로부터 받은 JSON 데이터 출력
	            console.log("Remove Response Data:", data); // 응답 데이터 로그
	
	            // 삭제 성공 여부 확인
	            if (data.result === 'success') {
	                // 삭제 성공 메시지
	                alert(data.message || "글을 삭제했습니다.");
	                // 삭제 후 리다이렉트할 URL로 이동 (기본값은 문의사항 목록 페이지)
	                window.location.href = "${contextPath}/bbs/questionList.do"; 
	            } else {
	                // 삭제 실패 시 오류 로그 출력
	                console.error("삭제 응답 오류 또는 실패:", data);
	                alert("삭제 중 문제가 발생했습니다."); // 사용자에게 오류 메시지 알림
	            }
	        })
	        .catch(err => {
	            // fetch 요청 중 오류가 발생하면 로그에 출력
	            console.error("fn_remove_board fetch 오류:", err);
	            alert("삭제 요청 중 오류가 발생했습니다: " + err.message); // 오류 메시지 사용자에게 알림
	        });
	    }

	    
	    //top 클릭시 
	    function scrollToTop() {
	        window.scrollTo({ top: 0, behavior: 'smooth' });
	    }
	    
	    
	    
	    
	    
	    //textarea 안에 HTML이 들어가면 깨질 수 있으므로 아래 함수로 escape 처리
	    function escapeHtml(text) {
	        return text
	            .replace(/&/g, "&amp;")
	            .replace(/</g, "&lt;")
	            .replace(/>/g, "&gt;")
	            .replace(/"/g, "&quot;");
	    }
	    
	    
	    
	 	//답변 달기 기능
	 	$(document).ready(function() {

		// --- 버튼 가시성을 업데이트하는 함수 ---
		// hasReply가 true면 수정/삭제 버튼 보이고, 답변달기 버튼 숨김
		// hasReply가 false면 답변달기 버튼 보이고, 수정/삭제 버튼 숨김
		function updateReplyButtons(hasReply) {
			if (hasReply) {
				$("#replyBtn").hide(); // 답변달기 버튼 숨김
				$("#replyButtons").show(); // 수정/삭제 버튼 영역 보임
			} else {
				$("#replyBtn").show(); // 답변달기 버튼 보임
				$("#replyButtons").hide(); // 수정/삭제 버튼 영역 숨김
			}
		}

		// --- 페이지 로딩 시 초기 버튼 상태 설정 ---
		// ${board.reply} 값이 비어있거나 null이 아니면 답변이 있는 걸로 판단
        // jsp에서 null이면 빈 문자열로 찍힐 수도 있어서 trim()으로 공백 제거 후 체크!
        // 만약 null일 때 "null"이라는 문자열로 찍힌다면 그 경우도 같이 체크해줘야 함
		const initialReplyContent = $("#replyContent").text().trim();
		updateReplyButtons(initialReplyContent !== "" && initialReplyContent !== "null"); // 실제 찍히는 값 확인 필요!

		// --- 답변 삭제 기능 ---
		$("#deleteReplyBtn").on("click", function() {
			if (!confirm("답변을 정말 삭제하시겠습니까?")) return;

			$.ajax({
				url: "${pageContext.request.contextPath}/bbs/replyDelete.do",
				type: "POST",
				data: { boardId: "${board.boardId}" },
				dataType: "json", // 서버에서 JSON 응답을 줄 거라고 예상 (success/fail 결과)
				success: function(response) {
					if(response.result === "success") {
						$("#replyContent").text(""); // 답변 내용 지우기
						updateReplyButtons(false); // <-- 중요! 삭제했으니 답변 없는 상태로 버튼 변경
						alert("답변이 삭제되었습니다.");
					} else {
						alert("답변 삭제에 실패했습니다: " + (response.message || "알 수 없는 오류")); // 서버에서 실패 이유를 알려주면 좋음
					}
				},
				error: function(xhr, status, error) { // 에러 발생 시 좀 더 자세히 알 수 있도록 수정
					alert("서버 오류가 발생했습니다. 상태: " + status + ", 오류: " + error);
				}
			});
		});

		// --- 답변 달기 폼 보여주기 ---
		$("#replyBtn").on("click", function(){
            // 혹시 모르니 현재 답변 내용 다시 체크 (UI상 보이지 않더라도)
            const currentReplyContent = $("#replyContent").text().trim();
            if (currentReplyContent !== "" && currentReplyContent !== "null") {
                 alert("이미 답변이 존재합니다."); // 이미 답변 있으면 폼 안 보여줌
                 return;
            }

			if($("#replyForm").length === 0) { // 폼이 없을 때만 생성
				const replyForm = `
					<form id="replyForm">
						<textarea name="reply" rows="4" cols="50" required></textarea>
						<input type="hidden" name="boardId" value="${board.boardId}" />
						<button type="submit">답변 저장</button>
						<button type="button" id="cancelReplyFormBtn">취소</button> <%-- 취소 버튼 추가 --%>
					</form>
				`;
				$("#replyFormArea").html(replyForm);
				// $("#replyBtn").hide(); // 폼 보일 때 답변달기 버튼 숨김 (updateReplyButtons 함수에서 처리할 수도 있음)
			}
		});

        // --- 답변 폼 취소 버튼 기능 ---
        $(document).on("click", "#cancelReplyFormBtn", function() {
            $("#replyFormArea").empty(); // 폼 삭제
            // updateReplyButtons(false); // 취소 시 답변이 없는 상태면 답변달기 버튼 다시 보이게 (필요시 추가)
            // 취소 버튼은 답변이 없을 때 폼을 닫는 용도라, 그냥 폼만 없애고 버튼 상태는 그대로 두는 게 자연스러울 듯?
            // 아니면 폼 띄울 때 숨겼던 #replyBtn을 다시 보이게 해주기
            $("#replyBtn").show(); // 폼 닫으면 답변달기 버튼 다시 보이게
        });


		// --- 답변 저장 기능 (폼 제출 시 Ajax) ---
		$(document).on("submit", "#replyForm", function(e) {
			e.preventDefault(); // 기본 폼 전송 방지

			$.ajax({
				url: "${pageContext.request.contextPath}/bbs/reply.do",
				type: "POST",
				data: $(this).serialize(),
				dataType: "json", // 서버에서 JSON 응답을 기대 (success/fail, reply 내용 등)
				success: function(response) {
					if(response.result === "success") {
						if(response.reply) { // 서버에서 저장된 답변 내용을 응답으로 보내주면
							$("#replyContent").html(response.reply); // 바로 내용 갱신!
						}
						$("#replyFormArea").empty(); // 폼 숨기기
						updateReplyButtons(true); // <-- 중요! 저장 성공했으니 답변 있는 상태로 버튼 변경
						alert("답변이 저장되었습니다.");
					} else {
						alert("답변 저장에 실패했습니다: " + (response.message || "알 수 없는 오류")); // 서버에서 실패 이유를 알려주면 좋음
					}
				},
				error: function(xhr, status, error) { // 에러 발생 시 좀 더 자세히 알 수 있도록 수정
					alert("서버 오류 발생했습니다. 상태: " + status + ", 오류: " + error);
				}
			});
		});
		
		// --- 답변 수정 기능 ---
		// #editReplyBtn 클릭 시 동작 정의
		$("#editReplyBtn").on("click", function() {
			const currentReply = escapeHtml($("#replyContent").html().trim()); // 기존 답변 가져오기
		    // 이미 수정 폼이 존재하면 중복 방지
		    if ($("#editReplyForm").length > 0) return;
			
		    const editForm = `
		        <form id="editReplyForm">
		            <textarea name="reply" rows="4" cols="50" required>${currentReply}</textarea>
		            <input type="hidden" name="boardId" value="${board.boardId}" />
		            <button type="submit">수정 저장</button>
		            <button type="button" id="cancelEditReplyBtn">취소</button>
		        </form>
		    `;
		    $("#replyContent").hide();               // 기존 답변 숨기기
		    $("#editReplyBtn").hide();              // 수정 버튼 숨기기
		    $("#replyFormArea").html(editForm);     // 수정 폼 삽입
		});
		
		
		
		// --- 수정 폼 취소 버튼 ---
		$(document).on("click", "#cancelEditReplyBtn", function() {
			$("#replyFormArea").empty();     // 수정 폼 삭제
		    $("#replyContent").show();      // 기존 답변 다시 표시
		    $("#editReplyBtn").show();      // 수정 버튼 다시 보이기
		});
		// --- 수정 저장 (폼 제출 시) ---
		$(document).on("submit", "#editReplyForm", function(e) {
		    e.preventDefault();

		    $.ajax({
		        url: "${pageContext.request.contextPath}/bbs/reply.do",
		        type: "POST",
		        data: $(this).serialize(),
		        dataType: "json",
		        success: function(response) {
		            if (response.result === "success") {
		                if (response.reply) {
		                    $("#replyContent").html(response.reply).show(); // 수정된 내용 갱신
		                }
		                $("#replyFormArea").empty(); // 폼 삭제
		                
		                $("#editReplyBtn").show(); // 수정 버튼 다시 보이기
		                
		                alert("답변이 수정되었습니다.");
		            } else {
		                alert("수정에 실패했습니다: " + (response.message || "알 수 없는 오류"));
		            }
		        },
		        error: function(xhr, status, error) {
		            alert("서버 오류 발생. 상태: " + status + ", 오류: " + error);
		        }
		    });
		});



	});

	    
	
	</script>

</body>
</html>


