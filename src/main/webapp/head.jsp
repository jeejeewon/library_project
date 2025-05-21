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
			<li>
				<span>도서이용</span>
				<ul class="sub-menu">
					<div class="inner">
						<div class="sub-menu-wrap">
							<jsp:include page="nav.jsp" />
						</div>
					</div>
				</ul>
			</li>	
			<li>
				<span>도서관안내</span>
				<ul class="sub-menu">
					<div class="inner">						
						<div class="sub-menu-wrap">
							<jsp:include page="nav.jsp" />
						</div>
					</div>
				</ul>
			</li>
			<li>
				<span>도서관소식</span>
				<ul class="sub-menu">
					<div class="inner">						
						<div class="sub-menu-wrap">
							<jsp:include page="nav.jsp" />
						</div>
					</div>
				</ul>
			</li>
			<li>
				<span>내 서재</span>
				<ul class="sub-menu">
					<div class="inner">						
						<div class="sub-menu-wrap">
							<jsp:include page="nav.jsp" />
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