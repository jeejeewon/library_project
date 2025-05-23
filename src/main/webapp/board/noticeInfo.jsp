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

<%
    // 세션에서 'fromPage' 값을 가져오기
    String fromPage = (String) session.getAttribute("fromPage");
%>

<html>
<head>
	<title>공지사항 상세페이지 - noticeInfo.jsp</title>
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
		    padding: 40px 20px;
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
		/*수정, 삭제 버튼*/
		.delet-modify{
		flex-shrink: 0;
		display: flex;
		align-items: flex-start;
		}
		
		.delet-modify a{
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
		.delet-modify a:hover {
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
		.download-link:hover{
			background-color: #f4f4f4;
		}
					    
		/*배너이미지설정*/
		.banner-img{
			margin: 0 auto;
			width: 100%;
		}
		.banner-img img{
			width: 100%;
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
        	display: flex;
        		
        }
        .content-box-bottom-right{
        	display: flex;
        }
        
        /*목록, top 버튼 디자인*/
        .back-list-btn, .top-btn{
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
		.top-btn{
			background-color: #003c83;
		}
		.back-list-btn:hover, .top-btn:hover {
			background-color: #002c66;
		}
        

		/* 반응형 디자인 */
		@media screen and (max-width: 768px) {
			
		}
	</style>
</head>

<body>

	<section class="content-box">
		<div class="content-box-top">
			<div class="paging">
			    <!-- 이전글 버튼 (이전글이 있을 경우만 표시) -->
			    <c:if test="${getPreBoardId > 0}">
			        <form action="${preBoardUrl}" method="get">
			            <input type="hidden" name="boardId" value="${getPreBoardId}">
			            <input type="hidden" name="fromPage" value="${fromPage}">
			            <button type="submit">이전글</button>
			        </form>
			    </c:if>
				
			    <!-- 다음글 버튼 (다음글이 있을 경우만 표시) -->
			    <c:if test="${getNextBoardId > 0}">
			        <form action="${nextBoardUrl}" method="get">
			            <input type="hidden" name="boardId" value="${getNextBoardId}">
			            <input type="hidden" name="fromPage" value="${fromPage}">
			            <button type="submit">다음글</button>
			        </form>
			    </c:if>
		 	</div>
		</div>
		<div class="content-box-middle">
			<div class="title-area">
				<div class="title-area-left">
					<p class="title">${board.title}</p>
					<div class="board-info">
						<span class="user-id">${board.userId}</span>
						<span class="board-info-gray"><fmt:formatDate value="${board.createdAt}" pattern="yyyy-MM-dd HH:mm" /></span>
				        <span class="board-info-gray">조회 ${board.views}</span>
					</div>
				</div>
				<div class="delet-modify">
					<c:if test="${sessionScope.id == 'admin'}">
		    			<a href="${contextPath}/bbs/noticeModifyForm.do?boardId=${board.boardId}">수정</a>
						<a href="#" onclick="fn_remove_board('${contextPath}/bbs/removeNotice.do', ${board.boardId})">삭제</a>
					</c:if>
				</div>
			</div>
			<div class="content-area">
				<div class="file-area">
					<c:if test="${not empty board.file}"><!-- 첨부파일이 있을 경우에만 나타나도록 설정 -->
						<a href="${contextPath}/download.do?boardId=${board.boardId}&file=${board.file}&type=file" download class="download-link"><span>💾</span><span>${board.file}</span><span>⭳</span></a>
					</c:if>
				</div>
				<c:if test="${not empty board.bannerImg}"><!-- 배너이지미파일이 있을 경우에만 나타나도록 설정 -->
					<div class="banner-img">
						<img src="${contextPath}/download.do?boardId=${board.boardId}&bannerImg=${board.bannerImg}&type=banner" id="preview2" alt="배너 이미지">
					</div>
				</c:if>
				<p>${board.content}</p>
			</div>
		</div>
			
		<div class="content-box-bottom">
			<div class="content-box-bottom-left">
			
			</div>
			<div class="content-box-bottom-right">
				<c:choose>
		        <c:when test="${fromPage == 'noticeList'}">
		            <form action="${contextPath}/bbs/noticeList.do" method="get">
		                <button type="submit" class="back-list-btn">목록</button>
		            </form>
		        </c:when>
		        <c:when test="${fromPage == 'eventList'}">
		            <form action="${contextPath}/bbs/eventList.do" method="get">
		                <button type="submit" class="back-list-btn">목록</button>
		            </form>
		        </c:when>
		        <c:otherwise>
		            <form action="${contextPath}/bbs/noticeList.do" method="get">
		                <button type="submit" class="back-list-btn">목록</button>
		            </form>
		        </c:otherwise>
		    </c:choose>
			<button onclick="scrollToTop()" class="top-btn">TOP</button>
			</div>
		</div>
	</section>
	
	<script type="text/javascript">
	function scrollToTop() {
        window.scrollTo({ top: 0, behavior: 'smooth' });
    };

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
	                // 삭제 후 리다이렉트할 URL로 이동 (기본값은 공지사항 목록 페이지)
	                window.location.href = data.redirect || "${contextPath}/bbs/noticeList.do"; 
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


