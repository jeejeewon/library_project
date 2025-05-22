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
									<a href="${contextPath}/admin/main" class="item">관리자</a>
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
	$(".menu>li").each(function(index) {
		$(this).click(function() {
			const $clickedLi = $(this); 
			const $clickedSubMenu = $clickedLi.find('.sub-menu'); 
			
			if ($clickedLi.hasClass('on')) {
				$clickedSubMenu.slideUp(200, function() {
                    $clickedLi.removeClass('on');
                    $clickedSubMenu.removeClass('on');
                });
			} else {
				$('.sub-menu').slideUp(200, function() { 
                     $('.menu>li').removeClass('on');
                     $('.sub-menu').removeClass('on');
                });

				$clickedLi.addClass('on');
				$clickedSubMenu.addClass('on');
				$clickedSubMenu.slideDown(200);
			}
		});
	});

	$(document).on(
			'click',
			function(event) {
				var $target = $(event.target);
				var $headerWrap = $('.header_wrap');
				var $menuWrap = $('.menu-wrap');
				var $subMenuOn = $('.sub-menu.on');

				if ($target.closest($headerWrap).length && !$target.closest($menuWrap).length && !$target.closest($subMenuOn).length) {
					$subMenuOn.slideUp(200, function() { 
                         $('.menu>li').removeClass('on');
                         $('.sub-menu').removeClass('on');
                    });
				}
				
                else if (!$target.closest($headerWrap).length) {
                    $subMenuOn.slideUp(200, function() {
                         $('.menu>li').removeClass('on');
                         $('.sub-menu').removeClass('on');
                    });
                }
			});
</script>