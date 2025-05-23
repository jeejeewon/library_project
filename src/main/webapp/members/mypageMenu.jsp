<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
request.setCharacterEncoding("UTF-8");
String contextPath = request.getContextPath();
%>
<div class="left_menu">
	<div class="inner">
		<div class="menu-section">
			<a href="<%=contextPath%>/member/mypage" class="menu-tit"> <img
				width="18" height="18"
				src="https://img.icons8.com/material-sharp/24/home.png" alt="home" /><span>마이페이지
					홈</span>
			</a>
		</div>
		<div class="menu-section">
			<p class="menu-tit">
				<img width="16" height="16"
					src="https://img.icons8.com/metro/26/literature.png"
					alt="literature" /><span>내 서재</span>
			</p>
			<ul>
				<li><a href="<%=contextPath%>/books/myRentalList.do">내 대여 내역</a></li>				
			</ul>
		</div>
		<div class="menu-section">
			<p class="menu-tit">
				<img width="18" height="18"
					src="https://img.icons8.com/ios-glyphs/30/user--v1.png"
					alt="user--v1" /><span>내 정보</span>
			</p>
			<ul>
				<li><a href="<%=contextPath%>/member/modify">내 정보 관리</a></li>
				<li><a href="<%=contextPath%>/bbs/myReviewList.do">내 서평 관리</a></li>
				<li><a href="<%=contextPath%>/reserve/reserveCheck">시설 예약 관리</a></li>
				<li><a href="<%=contextPath%>//bbs/questionList.do">문의하기</a></li>
			</ul>
		</div>
	</div>
</div>