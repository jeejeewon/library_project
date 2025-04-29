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
							<a href="" class="item">마이페이지</a>
							<a href="${contextPath}/member/logout.me" class="item">로그아웃</a>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</div>
	<div class="menu">
		<ul class="container">
			<li><span>도서이용</span></li>
			<li><a href="${contextPath}/info/mapinfo"><span>도서관안내</span></a></li>
			<li><span>도서관소식</span></li>
			<li><span>내 서재</span></li>
		</ul>
	</div>
</div>