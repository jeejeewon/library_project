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
		/*화면 백그라운드색상*/
		body {
				background-color: white;
			}
		
		.content-box {
			display: flex;
		    flex-direction: column;
		    align-items: flex-start;
    
		    max-width: 1000px;
		    margin: 0 auto;
		    padding: 40px 20px 0 20px;
		}
		/*컨텐츠-탑 영역*/
        .content-box-top{
        	margin: 10px 0;
        }
				
		/* 페이징 버튼 */
		.paging {
			display: flex;
			justify-content: flex-end;
			gap: 5px;
				}
		
		.paging form button {
        	padding: 8px 16px;
            font-size: 14px;
            border-radius: 4px;
            background-color: #92B5DE;
            color: white;
            text-decoration: none;
            cursor: pointer;
            box-sizing:border-box;
            border: none;
  			outline: none;
  			margin-left: 5px;
			}
		
		.paging form button:hover {
			background-color: #002c66;
			}
				
		 /*컨텐츠-미들 영역*/
		.content-box-middle {
			align-self: center; 
			width:100%;
		 	margin: 0 auto;
			background-color: #fff;
			border-radius: 10px;
            padding: 32px;
			box-shadow: 0 2px 10px rgba(0,0,0,0.06);
		}
		
		/* 타이틀 영역 */
		.title-area{
			display:flex;
			justify-content: space-between;
		    border-bottom: 1px solid #eee; 
		    padding-bottom: 15px;
		}
		.title-area .title-area-left{
			flex-grow: 1;
			flex-shrink: 1; 
		}
		/*타이틀*/
		.title-area-left .title{
    		font-weight: bold;
    		font-size: 24px;
		}
		.title-secret{
			font-size: 14px;
			color: gray;
		}
		/*수정, 삭제 버튼*/
		.delet-modify{
		flex-shrink: 0;
		display: flex;
		align-items: flex-start;
		}
		
		.delet-modify a, #cancelEditReplyBtn, #deleteReplyBtn, #cancelReplyFormBtn{
			box-sizing:border-box;
			background-color: #f4f4f4;
			padding: 8px 15px;
		    border-radius: 4px;
		    font-size: 14px;
		    text-decoration: none;
		    color: #424242;	   
		    border: none;
		    cursor: pointer;
		    margin-left: 5px;
		}
		.delet-modify a:hover, #cancelEditReplyBtn:hover, #deleteReplyBtn:hover {
			background-color: #eee;
		}
		
		/*타이틀인포(유저이름,날짜,조회수)*/
		.title-area-left .board-info .user-id{
			font-size: 13px;
			line-height: 15px;
			font-weight: bold;
		}
		.title-area-left .board-info .board-info-gray{
			font-size: 12px;
			line-height:14px;
			color: #999;
		}
		
		
		/*내용영역(첨부,배너,내용)*/
		.content-area{
			padding-top:20px;
			display: flex;
			flex-direction: column;
			gap:20px;
		}
		/*첨부파일영역설정*/
		.file-area{
			text-align: right;
		}
		.file-area .download-link{
			text-align:center;
			background-color:white;
			border: 0.5px solid #eee;
			padding: 15px;
			font-size: 14px;
			font-weight: bold;
			border-radius: 4px;
			}
		.download-link span{
			padding: 0 10px;
		}
		.download-link:hover, #deleteReplyBtn:hover{
			background-color: #f4f4f4;
		}
        /*내용*/
		.content-area p{
			line-height: 1.6;
			font-size:15px;
			color: #555;
			word-break: break-word;
		}
		
		
		/*컨텐츠-바텀 영역*/
        .content-box-bottom{
        	margin: 20px 0;
        	position: relative;
        	display: flex;
        	justify-content: space-between;
        }
        
        /* 운영자가 아닐 때 (왼쪽 영역 없을 때) 오른쪽 영역을 오른쪽 끝에 붙이기 */
		.content-box-bottom.no-reply-controls {
			justify-content: flex-end;
		}
        
        .content-box-bottom-left{
        }
        .content-box-bottom-right{
			display: flex;
        }
        /*버튼 디자인*/
        .back-list-btn, .top-btn,
        #replyBtn, .reply-submit-btn,
        #editReplyBtn{
        	padding: 8px 16px;
            font-size: 14px;
            border-radius: 4px;
            color: white;
            text-decoration: none;
            cursor: pointer;
            box-sizing:border-box;
            border: none;
  			outline: none;
  			margin-left: 5px;
		}
		.back-list-btn{
			background-color: #92B5DE;
		}
		.top-btn, #editReplyBtn, .reply-submit-btn{
			background-color: #003c83;
		}
		.back-list-btn:hover, .top-btn:hover, .reply-submit-btn:hover, #editReplyBtn:hover {
			background-color: #002c66;
		}
		
		/*답변영역*/
		#replyBtn{
			background-color: red;
		}
		
		.reply-display-area-wrap{
			width:100%;
		    padding: 0 20px 40px 20px;
		
		}
		.reply-display-area{
			width:100%;
			max-width:1000px;
		 	margin: 20px auto;
			background-color: #fff;
			border-radius: 10px;
            padding: 32px;
			box-shadow: 0 2px 10px rgba(0,0,0,0.06);
		}
		.reply-btn-wrap, #replyButtons{
			width: 100%;
			display: flex;
			justify-content: flex-end;
			margin: 10px 0;
		}

		.reply-textarea {
		    width: 100%;
		    padding: 10px;
		    border: 1px solid #ccc;
		    resize: none;
		}
	</style>
</head>

<body>

	<section class="content-box">
		<div class="content-box-top">
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
			</div>
		</div>
		<div class="content-box-middle">
			<div class="title-area">
				<div class="title-area-left">
					<p class="title">
						<c:if test="${board.secret}">
							<span class="title-secret">[ 🔒 비밀글 ]</span>
						</c:if>
						${board.title}
					</p>
					<div class="board-info">
						<span class="user-id">${board.userId}</span>
						<span class="board-info-gray"><fmt:formatDate value="${board.createdAt}" pattern="yyyy-MM-dd HH:mm" /></span>
				        <span class="board-info-gray">조회 ${board.views}</span>
					</div>
				</div>
				<div class="delet-modify">
					<c:if test="${sessionScope.id == 'admin' or board.userId == sessionScope.id}">
						<a href="${contextPath}/bbs/questionModifyForm.do?boardId=${board.boardId}">수정</a>
						<a href="#" onclick="fn_remove_board('${contextPath}/bbs/removeQuestion.do', ${board.boardId})">삭제</a>
					</c:if>
				</div>
			</div>
			<div class="content-area">
				<div class="file-area">
					<c:if test="${not empty board.file}"><!-- 첨부파일이 있을 경우에만 나타나도록 설정 -->
						<a href="${contextPath}/download.do?boardId=${board.boardId}&file=${board.file}&type=file" download class="download-link"><span>💾</span><span>${board.file}</span><span>⭳</span></a>
					</c:if>
				</div>
				<p>${board.content}</p>
			</div>
		</div>
		<div class="content-box-bottom <c:if test="${sessionScope.id != 'admin'}">no-reply-controls</c:if>">
			
				<%-- content-box-bottom-left: 운영자admin일 경우에만 보이는 답변 버튼 영역 --%>
				<c:if test="${sessionScope.id == 'admin'}">
					<div class="content-box-bottom-left">
					<%-- 답변이 없을 때 보이는 버튼 --%>
					<button type="button" id="replyBtn">답변달기</button>
					</div>
				</c:if>
			
			<%-- content-box-bottom-right: 목록, TOP 버튼 영역 --%>
			<div class="content-box-bottom-right">
				<form action="${pageContext.request.contextPath}/bbs/questionList.do" method="get">
					<button type="submit" class="back-list-btn">목록</button>
				</form>
				<button onclick="scrollToTop()" class="top-btn">TOP</button>
			</div>
		</div>
	</section>
	<section class="reply-display-area-wrap">
			<div class="reply-display-area" style="display: none;"> <%-- <-- 기본적으로 숨김! --%>
			<%-- 답변이 있을때 표시됨 --%>
			<div id="replyContent">
				<c:out value="${board.reply}" />
			</div>	
			<!-- 여기에 답변 폼이 동적으로 삽입됨 (Ajax) -->
			<div id="replyFormArea"></div>
			<%-- 답변이 있을 때만 보이게 할 수정/삭제 버튼 영역 --%>
			<c:if test="${sessionScope.id == 'admin'}">
				<div id="replyButtons" style="display: none;">
					<button id="editReplyBtn" type="button">답변수정</button>
					<button id="deleteReplyBtn" type="button">답변삭제</button>
				</div>
			</c:if>
		</div>
	</section>

	
		<script type="text/javascript">

	    function fn_remove_board(url, boardId) {
	        console.log("fn_remove_board called with URL:", url, "and boardId:", boardId);
	        if (!confirm("정말 삭제하시겠어요?")) return;
	        fetch(url, {
	            method: "POST",
	            headers: {
	                "Content-Type": "application/x-www-form-urlencoded"
	            },
	            body: new URLSearchParams({ boardId: boardId })
	        })
	        .then(res => {
	            console.log("Remove Response Status:", res.status);
	            if (!res.ok) {
	                throw new Error('HTTP error! status: ' + res.status);
	            }
	            return res.json();
	        })
	        .then(data => {
	            console.log("Remove Response Data:", data);
	            if (data.result === 'success') {
	                alert(data.message || "글을 삭제했습니다.");
	                window.location.href = "${contextPath}/bbs/questionList.do";
	            } else {
	                console.error("삭제 응답 오류 또는 실패:", data);
	                alert("삭제 중 문제가 발생했습니다.");
	            }
	        })
	        .catch(err => {
	            console.error("fn_remove_board fetch 오류:", err);
	            alert("삭제 요청 중 오류가 발생했습니다: " + err.message);
	        });
	    }

	    function scrollToTop() {
	        window.scrollTo({ top: 0, behavior: 'smooth' });
	    };

	    function escapeHtml(text) {
            if (text == null) {
                return '';
            }
	        return text
	            .replace(/&/g, "&amp;")
	            .replace(/</g, "&lt;")
	            .replace(/>/g, "&gt;")
	            .replace(/"/g, "&quot;")
                .replace(/'/g, "&#039;");
	    };

	 	$(document).ready(function() {

		function updateReplyVisibility(hasReply, isFormActive) {
			if (hasReply) {
				$("#replyButtons").show();
			} else {
				$("#replyButtons").hide();
			}
			if (!hasReply && !isFormActive) { 
				$("#replyBtn").show(); 
			} else {
	            $("#replyBtn").hide();
			}

            
            if (hasReply || isFormActive) {
                 $(".reply-display-area").show();
            } else {
                 $(".reply-display-area").hide();
            }
            if (isFormActive) {
                $("#replyContent").hide();
            } else {
                $("#replyContent").show();
                $("#replyFormArea").empty();
            }
			
			
		}

	    const initialReplyContent = $("#replyContent").text().trim();
		const hasInitialReply = initialReplyContent !== "";

		updateReplyVisibility(hasInitialReply, false);

		$("#deleteReplyBtn").on("click", function() {
			if (!confirm("답변을 정말 삭제하시겠습니까?")) return;

			$.ajax({
				url: "${pageContext.request.contextPath}/bbs/replyDelete.do",
				type: "POST",
				data: { boardId: "${board.boardId}" },
				dataType: "json",
				success: function(response) {
					if(response.result === "success") {
						$("#replyContent").text("");
						alert("답변이 삭제되었습니다.");
						updateReplyVisibility(false, false);
					} else {
						alert("답변 삭제에 실패했습니다: " + (response.message || "알 수 없는 오류"));
					}
				},
				error: function(xhr, status, error) {
					alert("서버 오류 발생했습니다. 상태: " + status + ", 오류: " + error);
				}
			});
		});

		$("#replyBtn").on("click", function(){
	        const currentReplyContent = $("#replyContent").text().trim();
	        if (currentReplyContent !== "") {
	             alert("이미 답변이 존재합니다.");
	             return;
	        }

			$("#replyFormArea").empty();
			// 템플릿 리터럴 대신 일반 문자열 연결 사용
			const replyForm = '<form id="replyForm">' +
				'<input type="hidden" name="boardId" value="${board.boardId}" />' +
				'<div class="reply-btn-wrap">'+
				'<button type="submit" class="reply-submit-btn">답변 저장</button>' +
				'<button type="button" id="cancelReplyFormBtn">취소</button>' +
				'</div>'+
				'<textarea name="reply" rows="4" cols="50" required class="reply-textarea"></textarea>'
				'</form>';
			$("#replyFormArea").html(replyForm);

			updateReplyVisibility(false, true);
		});

	    $(document).on("click", "#cancelReplyFormBtn", function() {
	        $("#replyFormArea").empty();
	        updateReplyVisibility(false, false);
	    });

		$(document).on("submit", "#replyForm", function(e) {
			e.preventDefault();

			$.ajax({
				url: "${pageContext.request.contextPath}/bbs/reply.do",
				type: "POST",
				data: $(this).serialize(),
				dataType: "json",
				success: function(response) {
					if(response.result === "success") {
						if(response.reply) {
							$("#replyContent").html(escapeHtml(response.reply));
						}
						$("#replyFormArea").empty();
						alert("답변이 저장되었습니다.");
						updateReplyVisibility(true, false);
					} else {
						alert("답변 저장에 실패했습니다: " + (response.message || "알 수 없는 오류"));
					}
				},
				error: function(xhr, status, error) {
					alert("서버 오류 발생했습니다. 상태: " + status + ", 오류: " + error);
				}
			});
		});

		$("#editReplyBtn").on("click", function() {
		    if ($("#editReplyForm").length > 0) return;
			const currentReply = escapeHtml($("#replyContent").html().trim());

	        $("#replyContent").hide();
			$("#replyFormArea").empty();

		    // 템플릿 리터럴 대신 일반 문자열 연결 사용
		    const editForm = '<form id="editReplyForm">' +
		        '<input type="hidden" name="boardId" value="${board.boardId}" />' +
		        '<div class="reply-btn-wrap">'+
		        '<button type="submit" class="reply-submit-btn">수정 저장</button>' +
		        '<button type="button" id="cancelEditReplyBtn">취소</button>' +
		        '</div>'+
		        '<textarea name="reply" rows="4" cols="50" required class="reply-textarea">' + currentReply + '</textarea>' +
		        '</form>';			
		    $("#replyButtons").hide();
		    $("#replyFormArea").html(editForm);
		});

		$(document).on("click", "#cancelEditReplyBtn", function() {
			$("#replyFormArea").empty();
		    $("#replyContent").show();
		    $("#replyButtons").show();
		});

	    $(document).on("submit", "#editReplyForm", function(e) {
	        e.preventDefault();

	        $.ajax({
	            url: "${pageContext.request.contextPath}/bbs/reply.do",
	            type: "POST",
	            data: $(this).serialize(),
	            dataType: "json",
	            success: function(response) {
	                if(response.result === "success") {
	                    if(response.reply) {
	                        $("#replyContent").html(escapeHtml(response.reply));
	                    }
	                    $("#replyFormArea").empty();
	                    $("#replyContent").show();
	                    $("#replyButtons").show();
	                    alert("답변이 수정되었습니다.");
	                } else {
	                    alert("답변 수정에 실패했습니다: " + (response.message || "알 수 없는 오류"));
	                }
	            },
	            error: function(xhr, status, error) {
	                alert("서버 오류 발생했습니다. 상태: " + status + ", 오류: " + error);
	            }
	        });
	    });

		});
	</script>
	
</body>
</html>


