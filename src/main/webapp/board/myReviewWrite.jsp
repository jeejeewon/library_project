<%@page import="Vo.boardVO"%>
<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<%
request.setCharacterEncoding("UTF-8");
%>

<html>
<head>
	<title>문의글 글쓰기 - questionWrite.jsp</title>
	<script src="https://code.jquery.com/jquery-latest.min.js"></script>
	<style type="text/css">
		.review-write-section{
			margin: 25px 0;
		}
		
		.Non-member-form{
			margin-top:10px;
			text-align: center;
		}
		.login-btn{
			margin: 10px;
			display: inline-block;
			padding: 6px 14px;
    		font-size: 14px;
  			border-radius: 4px;
    		background-color: #003c83;	
    		color: white;
    		text-decoration: none;
		}
		
		.login-btn:hover{
			background-color: #002c66;
		}
		
		.write-form{
			display: flex;
			gap: 2%;
			margin: 20px 0;
		}
		.write-form-left{
			flex-grow: 1;
		}
		.write-form-left input
		, .write-form-left textarea{
			display: block;
			width: 100%;
			margin: 5px 0;
			padding: 10px;
			box-sizing: border-box;
			
		    border: 1px solid #dedede;
		    background: #fff;
		    font-size: 13px;
		    line-height: 1.2em;
		    color: #424242;
		    border-radius: 3px;
		    resize: none;
		}
		.write-form-left textarea{
			resize: none;
		}
		.write-form-right{
			flex-basis: 18%;
			flex-shrink: 0; 
			padding: 10px;
			box-sizing: border-box;
			
		    font-size: 14px;
		    border-radius: 4px;
		    background-color: #003c83;
		    color: white;
		    text-decoration: none;
		    margin: 5px 0;
		    
		    border: none;
		}
		.write-form-right:hover{
			cursor:pointer;
			background-color: #002c66;
		}
		
		@media (max-width: 768px) {
			.login-btn{
				display: block;
				max-width: 200px;
				margin: 5px auto;
			}
		
			.write-form{
				gap: normal;
				flex-direction: column;
			}
			.write-form-left{
				width: 100%;
			}
			
        }
	</style>
</head>
<body>
	<section class="review-write-section">
		<c:if test="${not empty sessionScope.id}">
		<form name="WriteForm" action="${contextPath}/bbs/myReviewWrite.do" method="post" class="write-form">
			<!-- bookNo전달 -->
			<input type="hidden" name="bookNo" value="${param.bookNo}">
			<div class="write-form-left">
				<!-- 제목 입력 -->
	       		<input type="text" placeholder="제목을 입력하세요." id="title" name="title" required> <%-- required는 필수 입력 필드로 만드는 HTML5 속성 --%>
				<!-- 내용 입력 -->
				<textarea name="content" placeholder="내용을 입력하세요."  rows="5" required></textarea>
			</div>
			<%-- 서평 등록 버튼 --%>
	        <button type="submit"  class="write-form-right">서평 등록</button>
		</form>
	</c:if>
	<c:if test="${empty sessionScope.id}">
		<div class="Non-member-form">
			<span>💡 서평은 회원만 작성할 수 있습니다. 로그인 하시면 서평을 남길 수 있어요!</span>
    		<span class="login-btn"><a href="${contextPath}/member/login">로그인 하러 가기</a></span>
		</div>
	</c:if>
	</section>
</body>
</html>
