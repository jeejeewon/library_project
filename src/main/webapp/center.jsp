<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%--  만약 Controller에서 "center" 값을 넘겨주지 않아 'center' 변수가 비어있다면(null 또는 빈 문자열),
    - 기본적으로 보여줄 중앙 화면을 "center.jsp"로 설정. (웹 애플리케이션의 초기 화면 등에 해당) --%>


<div class="center_wrap">
	<div class="main-search-wrap">
		<div class="main-search-form container">
			<div class="inner">
				<form action="">
					<input type="text" placeholder="검색어를 입력하세요.">
					<button type="submit">
						<svg xmlns="http://www.w3.org/2000/svg" x="0px" y="0px" width="35"height="35" viewBox="0,0,256,256">
						<g fill="#ffffff" fill-rule="nonzero" stroke="none" stroke-width="1"
								stroke-linecap="butt" stroke-linejoin="miter"
								stroke-miterlimit="10" stroke-dasharray="" stroke-dashoffset="0"
								font-family="none" font-weight="none" font-size="none"
								text-anchor="none" style="mix-blend-mode: normal">
							<g transform="scale(5.12,5.12)">
							<path d="M21,3c-9.39844,0 -17,7.60156 -17,17c0,9.39844 7.60156,17 17,17c3.35547,0 6.46094,-0.98437 9.09375,-2.65625l12.28125,12.28125l4.25,-4.25l-12.125,-12.09375c2.17969,-2.85937 3.5,-6.40234 3.5,-10.28125c0,-9.39844 -7.60156,-17 -17,-17zM21,7c7.19922,0 13,5.80078 13,13c0,7.19922 -5.80078,13 -13,13c-7.19922,0 -13,-5.80078 -13,-13c0,-7.19922 5.80078,-13 13,-13z"></path></g></g>
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

	<div class="container">center.jsp</div>
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
</script>