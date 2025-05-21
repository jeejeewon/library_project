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
		/*화면 백그라운드색상*/
		body {
				background-color: #fafafa;
			}
		
		.content-box {
			display: flex;
		    flex-direction: column;
		    align-items: flex-start;
    
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
        
        /*이전, top 버튼*/
        .back-btn, .top-btn{
			margin: 20px 0;
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
        
        .top-btn{
        	align-self: flex-end;
        }
        
        
        /*카드영역*/
		.card {
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
			display: grid;
			/* 2개의 행 (top, bottom), 2개의 열 (left, right) */
		    grid-template-rows: auto 1fr; /* 첫째 행 auto, 둘째 행 남은 공간 */
		    grid-template-columns: 1fr auto; /* 첫째 열 남은 공간, 둘째 열 auto */
		    gap: 10px;
		}
		
		.title-area .title{
			grid-column: 1 / 3;
    		grid-row: 1 / 2;
    		background-color:pink;
    		
    		font-weight: bold;
    		font-size: 24px;
		}
		.title-area .board-info{
			grid-column: 1 / 2;
		    grid-row: 2 / 3;
		    justify-self: start;
		}
		.title-area .board-info p{
			font-size: 13px;
			line-height: 15px;
			font-weight: bold;
		}
		.title-area .board-info span{
			font-size: 12px;
			line-height:14px;
			color: #999;
		}
		.title-area .delet-modify{
			grid-column: 2 / 3;
		    grid-row: 2 / 3;
		    justify-self: end;
		}


		/* 반응형 디자인 */
		@media screen and (max-width: 768px) {
			

			
			
		
		}
	</style>
</head>

<body>
	<section class="content-box">
		<a href="javascript:history.back()" class="back-btn">이전으로</a>
		<div class="card">
			<div class="title-area">
				<p class="title">${board.title}</p>
				<div class="board-info">
					<p>${board.userId}</p>
					<span><fmt:formatDate value="${board.createdAt}" pattern="yyyy-MM-dd HH:mm" /></span>
			        <span>조회 ${board.views}</span>
				</div>
		        <div class="delet-modify">
					<c:if test="${sessionScope.id == 'admin' or board.userId == sessionScope.id}">
						<a href="${contextPath}/bbs/myReviewModifyForm.do?boardId=${board.boardId}">수정</a>
						<a href="#" onclick="fn_remove_board('${contextPath}/bbs/removeReviewList.do', ${board.boardId})">삭제</a>
					</c:if>
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


