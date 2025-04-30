<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="myCenter" value="${requestScope.center}" />

mypage.jsp
<div id="mypage">
	<div class="left_menu">
		<div class="inner">
			<p>마이페이지 홈</p>
			<div class="menu_section">
				<p>내 서재</p>
				<ul>
					<li>희망 도서</li>
					<li>최근 조회한 도서</li>
				</ul>
			</div>
			<div class="menu_section">
				<p>내 정보</p>
				<ul>
					<li><a href="${contextPath}/member/modify">내 정보 관리</a></li>					
					<li>내 서평 관리</li>
					<li><a href="${contextPath}/reserve/reserveCheck">시설 예약 관리</a></li>
					<li>1:1 문의</li>
				</ul>
			</div>
		</div>
	</div>
	<div class="page">
		<div class="inner">
			${sessionScope.id}님의 마이페이지
			<%-- <c:if test="${empty myCenter}">				
					
			</c:if>
			<c:otherwise>
				
			</c:otherwise> --%>
		</div>
	</div>
</div>