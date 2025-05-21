<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
request.setCharacterEncoding("UTF-8");
String contextPath = request.getContextPath();
%>

<div class="nav-wrap container">
	<ul>
		<li><a href="<%=contextPath %>/books/bookList.do">전체도서</a></li>
		<li><a href="<%=contextPath %>/books/searchForm.do">도서검색</a></li>
		<li><a href="<%=contextPath %>/books/newBooks.do">신착도서</a></li>
		<li><a href="<%=contextPath %>/books/bestBooks.do">인기도서</a></li>
	</ul>
	<ul>
		<li><a href="<%=contextPath %>/info/mapinfo">도서관소개</a></li>
		<li><a href="<%=contextPath %>/info/useinfo">이용안내</a></li>
		<li><a href="<%=contextPath %>/reserve/room">시설예약</a></li>
		<li><a href="<%=contextPath %>/info/map">오시는 길</a></li>
	</ul>
	<ul>
		<li><a href="<%=contextPath %>/bbs/noticeList.do">공지사항</a></li>
		<li><a href="<%=contextPath %>/bbs/eventList.do">행사안내</a></li>
		<li><a href="<%=contextPath %>/bbs/questionList.do">문의게시판</a></li>
	</ul>
	<ul>
		<li><a href="<%=contextPath %>/books/myRentalList.do">내 대여 내역</a></li>
		<li><a href="<%=contextPath%>/bbs/myReviewList.do">내 서평</a></li>
		<li><a href="<%=contextPath %>/reserve/reserveCheck">내 시설 예약</a></li>
	</ul>
</div>