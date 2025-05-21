<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

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
							<c:choose>
								<c:when test="${sessionScope.id == 'admin'}">
								<a href="${contextPath}/adm/home.jsp" class="item">관리자</a>
								</c:when>
							</c:choose>
							<a href="${contextPath}/member/logout.me" class="item">로그아웃</a>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</div>
	<div class="menu-wrap">
		<ul class="container menu">
			<li><span>도서이용</span>
				<ul class="sub-menu">
					<div class="inner">
						<div class="sub-menu-tit">
							<h3>도서이용</h3>
							<p>도서관 소장자료, 전자자료, 도서관 구축 원문 콘텐츠에 대한 검색과 미소장 자료 이용 방법을 제공합니다.</p>
						</div>
						<div class="sub-menu-wrap">
							<ul>
								<li><a href="${contextPath}/books/bookList.do">전체도서</a></li>
								<li><a href="${contextPath}/books/searchForm.do">도서검색</a></li>
								<li><a href="${contextPath}/books/newBooks.do">신착도서</a></li>
								<li><a href="${contextPath}/books/bestBooks.do">인기도서</a></li>
							</ul>
						</div>
					</div>
				</ul></li>
			<li><span>도서관안내</span>
				<ul class="sub-menu">
					<div class="inner">
						<div class="sub-menu-tit">
							<h3>도서관안내</h3>
							<p>도서관 분관과 시설에 대한 상세한 안내와 시설/좌석 예약/배정 메뉴를 이용할 수 있습니다.</p>
						</div>
						<div class="sub-menu-wrap">
							<ul>
								<li><a href="${contextPath}/info/mapinfo">도서관소개</a></li>
								<li><a href="${contextPath}/info/useinfo">이용안내</a></li>
								<li><a href="${contextPath}/reserve/room">시설예약</a></li>
								<li><a href="${contextPath}/info/map">오시는 길</a></li>
							</ul>
						</div>
					</div>
				</ul></li>
			<li><span>도서관소식</span>
				<ul class="sub-menu">
					<div class="inner">
						<div class="sub-menu-tit">
							<h3>도서관소식</h3>
							<p>도서관 공지사항 및 각종 통계, 도서관 조직과 규정, 개관시간 등을 안내합니다.</p>
						</div>
						<div class="sub-menu-wrap">
							<ul>
								<li><a href="${contextPath}/bbs/noticeList.do">공지사항</a></li>
								<li><a href="${contextPath}/bbs/eventList.do">행사안내</a></li>								
								<li><a href="${contextPath}/bbs/questionList.do">문의게시판</a></li>
							</ul>
						</div>
					</div>
				</ul></li>
			<li><span>내 서재</span>
				<ul class="sub-menu">
					<div class="inner">
						<div class="sub-menu-tit">
							<h3>내 서재</h3>
							<p>개인화 서비스 메뉴로 로그인한 개인별 도서관 이용 현황을 통합해서 제공합니다.</p>
						</div>
						<div class="sub-menu-wrap">
							<ul>
								<li><a href="${contextPath}/books/myRentalList.do">내 대여 내역</a></li>								
								<li><a href="${contextPath}/bbs/myReviewList.do">내 서평 관리</a></li>
								<li><a href="${contextPath}/reserve/reserveCheck">시설 예약 관리</a></li>
							</ul>
						</div>
					</div>
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
			$('.sub-menu').removeClass('on');
			$('.sub-menu').eq(idx).addClass('on');
			$('.sub-menu').hide();
			$('.sub-menu').eq(idx).slideDown();
		});
	});

	$(document).on(
			'click',
			function(event) {
				var $targetArea = $('.sub-menu.on');
				var $header = $('#header');
				if (!$targetArea.is(event.target)
						&& $targetArea.has(event.target).length === 0
						&& !$header.is(event.target)
						&& $header.has(event.target).length === 0) {
					$targetArea.hide();
				}
			});
</script>