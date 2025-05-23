<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%--  만약 Controller에서 "center" 값을 넘겨주지 않아 'center' 변수가 비어있다면(null 또는 빈 문자열),
    - 기본적으로 보여줄 중앙 화면을 "center.jsp"로 설정. (웹 애플리케이션의 초기 화면 등에 해당) --%>
<%
request.setCharacterEncoding("UTF-8");
String contextPath = request.getContextPath();
%>

<style>
</style>

<div class="center_wrap">
	<div class="main-search-wrap">
		<div class="main-search-form container">
			<div class="inner">
				<form action="<%=contextPath%>/books/bookSearch.do" method="get">
					<input type="text" name="keyword" placeholder="도서명, 저자, 출판사, 분야 검색"
						required />
					<button type="submit">
						<svg xmlns="http://www.w3.org/2000/svg" x="0px" y="0px" width="30"
							height="30" viewBox="0,0,256,256">
						<g fill="#ffffff" fill-rule="nonzero" stroke="none"
								stroke-width="1" stroke-linecap="butt" stroke-linejoin="miter"
								stroke-miterlimit="10" stroke-dasharray="" stroke-dashoffset="0"
								font-family="none" font-weight="none" font-size="none"
								text-anchor="none" style="mix-blend-mode: normal">
							<g transform="scale(5.12,5.12)">
							<path
								d="M21,3c-9.39844,0 -17,7.60156 -17,17c0,9.39844 7.60156,17 17,17c3.35547,0 6.46094,-0.98437 9.09375,-2.65625l12.28125,12.28125l4.25,-4.25l-12.125,-12.09375c2.17969,-2.85937 3.5,-6.40234 3.5,-10.28125c0,-9.39844 -7.60156,-17 -17,-17zM21,7c7.19922,0 13,5.80078 13,13c0,7.19922 -5.80078,13 -13,13c-7.19922,0 -13,-5.80078 -13,-13c0,-7.19922 5.80078,-13 13,-13z"></path></g></g>
						</svg>
					</button>
				</form>
			</div>
		</div>
		<div class="main-swiper-wrap">
			<div class="main-swiper">
				<div class="swiper-wrapper">
					<div class="swiper-slide">
						<img
							src="https://library.korea.ac.kr/wp-content/uploads/2021/04/main_lib_20210422.jpg">
					</div>
					<div class="swiper-slide">
						<img
							src="https://library.korea.ac.kr/wp-content/uploads/2021/04/%EB%A6%AC%EC%94%BD%ED%81%AC%EC%8A%A4%ED%8E%98%EC%9D%B4%EC%8A%A41_%ED%81%AC%EA%B8%B0%EB%B3%80%EA%B2%BD-2.jpg">
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="main-notice-and-Video">
		<div class="main-banner-slider swiper">
			<div class="swiper-wrapper">
				<c:if test="${not empty eventBannerList}">
					<c:forEach var="banner" items="${eventBannerList}">
						<div class="swiper-slide">
							<img
								src="${pageContext.request.contextPath}/board/board_file_repo/${banner.boardId}/${banner.bannerImg}"
								alt="${banner.title} 배너 이미지">
						</div>
					</c:forEach>
				</c:if>
			</div>
			<div class="swiper-pagination"></div>
		</div>
		<div class="main-notice">
			<ul>
				<c:if test="${not empty noticeList}">
					<c:forEach var="notice" items="${noticeList}">
						<li><span> <c:choose>
									<c:when test="${fn:length(notice.title) > 20}">
                         			${fn:substring(notice.title, 0, 20)}...
                            	</c:when>
									<c:otherwise>
                                	${notice.title}
                            	</c:otherwise>
								</c:choose>
						</span> <span><fmt:formatDate value="${notice.createdAt}"
									pattern="MM-dd" /></span></li>
					</c:forEach>
				</c:if>
				<c:if test="${empty noticeList}">
					<li>공지사항이 없습니다.</li>
				</c:if>
			</ul>
		</div>

		<div class="main-Video">
			<iframe width="100%" height="270px"
				src="https://www.youtube.com/embed/VHxusqT1dXo?si=8aN0XRzafHIAIuKP&amp;controls=0"
				title="YouTube video player" frameborder="0"
				allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
				referrerpolicy="strict-origin-when-cross-origin" allowfullscreen></iframe>
		</div>
	</div>
</div>


<script>
	$(document).ready(function() {
		new Swiper('.main-swiper', {
			loop : true,
			autoplay : {
				delay : 3000,
				disableOnInteraction : false,
			},
			speed : 3000,
			effect : "fade",
			fadeEffect : {
				crossFade : true,
			},
			slidesPerView : 1,
			paginationClickable : false,
		});
	});

	$(document).ready(function() {
		var mainBannerSwiper = new Swiper('.main-banner-slider', {
			slidesPerView : 1,
			spaceBetween : 0,
			loop : true,
			autoplay : {
				delay : 3000,
				disableOnInteraction : false,
			},
			speed : 800,

			pagination : {
				el : '.main-banner-slider .swiper-pagination',
				clickable : true,
			},
			navigation : {
				nextEl : '.main-banner-slider .swiper-button-next',
				prevEl : '.main-banner-slider .swiper-button-prev',
			},
		});
	});
</script>