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
	<title>문의사항 상세페이지 - questionInfo.jsp</title>
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
				<p>게시글 제목: ${board.title}</p>
				<p>작성자: ${board.userId}</p>
			</div>
			<div class="right">
                <!-- 작성일 출력 시 포맷팅 처리 -->
                <p>작성일: <fmt:formatDate value="${board.createdAt}" pattern="yyyy-MM-dd HH:mm" /></p>
                <p>조회수: ${board.views}</p>
			</div>
		</div>

		<div class="content-area">
			<p>게시글 내용: ${board.content}</p>
			<c:if test="${not empty board.file}"><!-- 첨부파일이 있을 경우에만 나타나도록 설정 -->
				<div class="file">
					<img src="${contextPath}/download.do?boardId=${board.boardId}&file=${board.file}&type=file" id="preview1" alt="첨부 이미지">
					<a href="${contextPath}/download.do?boardId=${board.boardId}&file=${board.file}&type=file" download class="download-link">첨부파일</a>
					<span>${board.file}</span>
				</div>
			</c:if>
			<c:if test="${not empty board.bannerImg}"><!-- 배너이지미파일이 있을 경우에만 나타나도록 설정 -->
				<div class="file">
					<img src="${contextPath}/download.do?boardId=${board.boardId}&bannerImg=${board.bannerImg}&type=banner" id="preview2" alt="배너 이미지">
					<a href="${contextPath}/download.do?boardId=${board.boardId}&bannerImg=${board.bannerImg}&type=banner" download class="download-link">배너이미지</a>
					<span>${board.bannerImg}</span>
				</div>
			</c:if>
		</div>

		<div class="board-info-bottom">
			<div class="board-info-bottom-left">
				<a href="${contextPath}/bbs/questionModifyForm.do?boardId=${board.boardId}">수정</a>
				<a href="#" onclick="fn_remove_board('${contextPath}/bbs/removeBoard.do', ${board.boardId})">삭제</a>
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
			<p>답변내용 : ${board.reply}</p>
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
	                window.location.href = data.redirect || "${contextPath}/bbs/questionList.do"; 
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
	</script>

</body>
</html>


