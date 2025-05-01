<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<c:out value="${contextPath}" />

<div class="header_wrap">
	<div class="top">
		<div class="container">
			<div id="logo">
				<a href="${contextPath}/index.jsp"><img
					src="https://home.pen.go.kr/images/web/siminlib/main/logo.png"></a>
			</div>
			<div id="login">
				<div class="inner">
					<c:choose>
						<c:when test="${empty sessionScope.id}">
							<a href="${contextPath}/member/login" class="item">로그인</a>
							<a href="${contextPath}/member/join" class="item">회원가입</a>
						</c:when>
						<c:otherwise>
							<p class="item">환영합니다 ${sessionScope.id}님</p>
							<a href="${contextPath}/member/mypage" class="item">마이페이지</a>
							<a href="${contextPath}/member/logout.me" class="item">로그아웃</a>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</div>
	<div class="menu-wrap">
		<ul class="container menu">
			<li>
			<span>도서이용</span>
			<ul class="sub-menu">
					<li>아직 없음~</li>			
				</ul>
			</li>			
			<li>
				<span>도서관안내</span>
				<ul class="sub-menu">
					<li><a href="${contextPath}/info/mapinfo">도서관소개</a></li>
					<li><a href="${contextPath}/info/useinfo">이용안내</a></li>
					<li><a href="${contextPath}/reserve/room">시설 예약</a></li>
					<li><a href="${contextPath}/reserve/reserveStudy">독서실 예약</a></li>
					<li><a href="${contextPath}/reserve/reserveMeeting">회의실 예약</a></li>
					<li><a href="${contextPath}/info/map">오시는 길</a></li>
				</ul>
			</li>
			<li>
				<span>도서관소식</span>
				<ul class="sub-menu">
					<li>아직 없음~</li>			
				</ul>
			</li>
			<li>
			<span>내 서재</span>
				<ul class="sub-menu">
					<li>희망 도서</li>
					<li>최근 조회한 도서</li>
					<li>내 서평 관리</li>
					<li><a href="${contextPath}/reserve/reserveCheck">시설 예약 관리</a></li>
				</ul>
			</li>
		</ul>
	</div>
</div>

<script>
	$(".menu>li").each(function() {
		$(this).click(function() {
			const idx = $(this).index();
			$('.menu>li').removeClass('on');
			$('.menu>li').eq(idx).addClass('on');
			$('.sub-menu').hide();
			$('.sub-menu').eq(idx).slideDown();
		});
	});
</script>