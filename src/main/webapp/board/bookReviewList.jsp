<%-- bookReviewList.jsp 파일 내용 (수정 제안! - 행 클릭 가능하게) --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<html>
<head>
    <title>서평 리스트 - bookReviewList.jsp</title>
    <script src="https://code.jquery.com/jquery-latest.min.js"></script>

    <style>
    	.review-form{
		    background-color: white;
		    border-radius: 10px;
		    padding: 32px;
		    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.06);
    	}
        .review-item {
        	text-align: justify;
            padding: 10px 0;
            border-bottom: 0.5px solid #eee; 
        }
        .review-item:last-child {
            border-bottom: none;
        }
        /* 제목 한 줄, 내용 두 줄 제한 */
        .title-cell {
        	font-size: 15px;
        	font-weight: bold;
            white-space: nowrap; 
            overflow: hidden; 
            text-overflow: ellipsis;
        }
        .content-clamp {
       		 font-size: 15px;
             overflow: hidden; 
             display: -webkit-box; 
             -webkit-line-clamp: 2; 
             -webkit-box-orient: vertical;
             margin: 10px 0;
        }
        .other-area{
        	font-size: 12px;
        	color: #888;
        	display: flex;
        	justify-content: space-between;
        	align-items: center;
        }
        .other-area > div span{
        	margin-right: 10px;	
        }
        .more-link {
            margin-left: 10px;
        	height: 24px;
		    line-height: 24px;
		    text-decoration: none;
		    color: #666;
		    font-size: 12px;
		    padding: 0 10px;
		    background: #fcfcfc;
		    border: 1px solid #e5e5e5;
		    border-radius: 4px;
		    box-sizing: border-box;
            display: none;
        }
        
        
    </style>
</head>
<body>
    <center>
    	<section class="review-form">
    		<!-- 등록된 서평이 없을 때 -->
			<c:if test="${empty requestScope.reviewList}">
				<p>📭 등록된 서평이 없습니다.</p>
			</c:if>
			<!-- 등록된 서평이 있을 때 -->
	    	<c:forEach var="review" items="${requestScope.reviewList}">
	    		<div class="review-item" data-board-id="${review.boardId}"
	    			<c:if test="${not empty sessionScope.id and sessionScope.id eq review.userId}">
	    			 	<%-- 현재 로그인한 유저가 쓴 글이면 이 속성을 추가 --%>
	    			 	data-is-my-review="true"
	    			 </c:if>>
	    			<div class="title-area">
	    				<p class="title-cell">${review.title}</p>
	                    <p class="content-clamp">${review.content}</p>
	    			</div>
	    			<div class="other-area">
	    				<div>
	    					<span>${review.userId}</span>
	    		    		<span><fmt:formatDate value="${review.createdAt}" pattern="yyyy-MM-dd" /></span>
	    				</div>
		                <a href="${contextPath}/bbs/reviewDetail.do?boardId=${review.boardId}" class="more-link hidden">더보기</a>
	    			</div>
	    		</div>
	    	</c:forEach>
    	</section>
    </center>

    <script>
    $(document).ready(function() {

        function checkTruncationAndShowMore() {
        	
            $(".review-item").each(function() {
                const $reviewItem = $(this);
                
                const isMyReview = $reviewItem.data('isMyReview') === true;
                
                const $contentP = $reviewItem.find('.content-clamp');
                const $moreLink = $reviewItem.find('.more-link');

                // 내용 요소가 있는지, 그리고 내용이 잘렸는지 확인
                 const isContentTruncated = $contentP.length > 0 && $contentP[0].scrollHeight > $contentP[0].clientHeight;
                 
             	// 더보기 버튼을 보여줄 최종 조건 :
                // 내용이 잘렸거나 (isContentTruncated) 또는 내가 쓴 글이면 (isMyReview) 보여줌
                if (isContentTruncated || isMyReview) {
                    // 내용이 잘렸다면 '더보기' 링크를 보이게 함
                    $moreLink.css('display', 'inline-block');
                } else {
                    // 내용이 잘리지 않았다면 '더보기' 링크를 숨김
                     $moreLink.css('display', 'none');
                }
            });
        }
        // 1. 페이지 로드 완료 후 바로 실행해서 초기 상태 설정;
        checkTruncationAndShowMore();

        // 2. 창 크기가 변경될 때마다 다시 실행해서 반응형 처리
        $(window).resize(function() {
            checkTruncationAndShowMore();
        });
    });
    </script>

</body>
</html>
