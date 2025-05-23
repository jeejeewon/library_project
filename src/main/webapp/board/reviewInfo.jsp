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
		
		/*컨텐츠-탑 영역*/
        .content-box-top{
        	margin: 20px 0;
        }
        
        /*이전, top 버튼 디자인*/
        .back-btn, .top-btn{
            font-size: 14px;
            border-radius: 4px;
            background-color: #003c83;
            color: white;
            text-decoration: none;
            cursor: pointer;
            box-sizing:border-box;
            border: none;
  			outline: none;
		}
		/*버튼 사이즈는 약간 다름*/
		.back-btn{padding: 6px 14px;}
		.top-btn{padding: 8px 16px;}
		
        .back-btn:hover, .top-btn:hover {
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
		.title-area-left .board-info{
		}
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
		
		
		/*내용영역(이미지,내용)*/
		.content-area{
			padding-top:20px;
			display: flex;
			gap:20px;
			align-items: start;
		}
		/*이미지설정*/
		.book-image-container{
			flex-shrink: 0; 
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
        /*내용*/
		.content-area p{
			flex-grow: 1;
			line-height: 1.6;
			font-size:15px;
			color: #555;
			word-break: break-word;
		}
		
		
		/*컨텐츠-바텀 영역*/
        .content-box-bottom{
        	margin: 20px 0;
        	align-self: flex-end;
        }
        
		/* 반응형 디자인 */
		@media screen and (max-width: 768px) {
			

			
			
		
		}
	</style>
</head>

<body>
	<section class="content-box">
		<div class="content-box-top">
			<a href="javascript:history.back()" class="back-btn">이전으로</a>
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
					<c:if test="${sessionScope.id == 'admin' or board.userId == sessionScope.id}">
						<a href="${contextPath}/bbs/myReviewModifyForm.do?boardId=${board.boardId}">수정</a>
						<a href="#" onclick="fn_remove_board('${contextPath}/bbs/removeReviewList.do', ${board.boardId}, ${board.bookNo})">삭제</a>
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
				<p>${board.content}</p>
			</div>
		</div>
		<div class="content-box-bottom">
			<button onclick="scrollToTop()" class="top-btn">TOP</button>
		</div>
	</section>
	
	
	
<script type="text/javascript">
	    function fn_remove_board(url, boardId, bookNo) { 
	        console.log("fn_remove_board called with URL:", url, "boardId:", boardId, "bookNo:", bookNo); 

	        if (!confirm("정말 삭제하시겠어요?")) return; 
	        fetch(url, {
	            method: "POST", 
	            headers: {
	                "Content-Type": "application/x-www-form-urlencoded"
	            },
	            body: new URLSearchParams({ boardId: boardId, bookNo: bookNo }) 
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
	                window.location.href = data.redirect || "${contextPath}/bbs/myReviewList.do";
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
	</script>


</body>
</html>


