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
	<title>내 서평 상세페이지 - myReviewInfo.jsp</title>
	<style>
		body {
				background-color: #fafafa;
			}
			
		.content-box {
		    max-width: 1000px;
		    margin: 0 auto;
		    padding: 40px 20px;
		}
		
		.thumb {
            width: 100%;
            max-width: 180px;
            height: auto;
            aspect-ratio: 3 / 4;
            object-fit: cover;
            border: 1px solid #ccc;
            border-radius: 6px;
        }

		/* 중앙 정렬을 위한 스타일 */
		.center-wrapper {
			background-color: #fff;
			border-radius: 10px;
            padding: 32px;
			box-shadow: 0 2px 10px rgba(0,0,0,0.06);
		}
		
		.back-btn, .top-btn{
			padding: 6px 14px;
            font-size: 14px;
            border-radius: 4px;
            background-color: #003c83;
            color: white;
            text-decoration: none;
            cursor: pointer;
            
            border: none;
  			outline: none;
		}

        .back-btn:hover, .top-btn:hover {
            background-color: #002c66;
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
	<section class="content-box">
		<a href="javascript:history.back()" class="back-btn">이전으로</a>
		<div class="center-wrapper">
			<div class="title-area">
				<div class="left">
					<p>${board.title}</p>
					<p>${board.userId}</p>
				</div>
				<div class="right">
	                <!-- 작성일 출력 시 포맷팅 처리 -->
	                <p>작성일: <fmt:formatDate value="${board.createdAt}" pattern="yyyy-MM-dd HH:mm" /></p>
	                <p>조회수: ${board.views}</p>
				</div>
			</div>
	
			<div class="content-area">
			
				<!-- 책 이미지 영역 -->
				<div class="book-image-container">
					<%-- 책 이미지 가져오기 --%>
					<c:if test="${not empty requestScope.board.thumbnail}">
						<img class="thumb" src="${contextPath}/${requestScope.board.thumbnail}" alt="${requestScope.board.title} 도서 이미지">
					</c:if>
				</div>
				<p>게시글 ${board.content}</p>
			</div>
	
			<div class="board-info-bottom">
				<div class="board-info-bottom-left">
					<c:if test="${sessionScope.id == 'admin' or board.userId == sessionScope.id}">
						<a href="${contextPath}/bbs/myReviewModifyForm.do?boardId=${board.boardId}">수정</a>
						<a href="#" onclick="fn_remove_board('${contextPath}/bbs/removeReviewList.do', ${board.boardId})">삭제</a>
					</c:if>
				</div>
			</div>
		</div>
		<button onclick="scrollToTop()" class="top-btn">TOP</button>
	</section>
	
	
	
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
	                // 삭제 후 리다이렉트할 URL로 이동 (기본값은 내서평 목록 페이지)
	                window.location.href = data.redirect || "${contextPath}/bbs/myReviewList.do"; 
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


