<%@page import="Vo.MemberVo"%>
<%@ page
	import="Vo.RentalVo, Vo.BookVo, java.util.*, java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%
request.setCharacterEncoding("UTF-8");
String contextPath = request.getContextPath();
String id = (String) session.getAttribute("id"); // 세션에서 사용자 ID를 가져옴
MemberVo memberVo = (MemberVo) request.getAttribute("memberVo");
Vector<RentalVo> rentalList = (Vector<RentalVo>) request.getAttribute("rentalList");

if (id == null) { // 로그인하지 않은 경우
%>
<script>
	alert("로그인을 하셔야 접근 가능 합니다."); // 알림 창 표시
	history.back(); // 이전 페이지로 이동
</script>
<%
}
%>

<div class="container">
	<div id="myPageMain" class="mypage">
		<jsp:include page="mypageMenu.jsp" />
		<div class="page">
			<div class="inner">
				<div class="my-info">
					<p>
						<span class="user-name"><%=memberVo.getName()%></span>님, 안녕하세요!
					</p>
					<p><%=memberVo.getEmail()%></p>
				</div>
				<div class="my-book">
					<div class="inner">
						<div class="tit">
							<h3>최근 대여 목록</h3>
							<a href="<%=contextPath%>/books/myRentalList.do">전체 보기</a>
						</div>
						<%
						if (rentalList != null && !rentalList.isEmpty()) {
						%>
						<div class="book-list">
							<%
							for (RentalVo rental : rentalList) {
								BookVo book = rental.getBook();
								boolean isReturned = rental.getReturnState() == 1;
								if (book != null) {
							%>
							<a href="<%= contextPath %>/books/bookDetail.do?bookNo=<%=rental.getBookNo()%>" class="card">
			                	<img src="<%=contextPath%>/<%=book.getThumbnail()%>"
									onerror="this.src='<%=contextPath%>/book/img/noimage.jpg';"
									alt="썸네일" />
								<div class="book-tit"><%=book.getTitle()%></div>
								<%-- <div>
								 <% if (isReturned) { %>
					                <span class="returned">✔ 반납</span>
					            <% } else { %>
					                <span class="not-returned">❌ 미반납</span>
					            <% } %>
					            </div> --%>
							</a>
							<%}	}%>
						</div>
						<%
						} else {
						%>
						<div class="empty-message">현재 대여 중인 도서가 없습니다.</div>
						<%}%>
					</div>
				</div>
				<div class="my-review">
					<div class="inner">
						<div class="tit">
							<h3>최근 내 서평</h3>
							<a href="<%=contextPath%>/bbs/myReviewList.do">전체 보기</a>
						</div>
						<div class="review-list">
							<!-- 내서평게시판 리스트 테이블 -->
							<table>

								<!-- 등록된 서평이 없을 때 -->
								<c:if test="${empty boardList}">
									<div class="empty">
										<p colspan="5" align="center">📭 등록된 서평이 없습니다.</p>
									</div>
								</c:if>
								<!-- 내서평 리스트 출력 -->
								<c:forEach var="boardVo" items="${boardList}" varStatus="status">
									<div class="item">
										<a href="<%=contextPath%>/bbs/myReviewInfo.do?boardId=${boardVo.boardId}">
											<p class="review-tit">${boardVo.title} </p>
											<p class="review-date">
												<fmt:formatDate value="${boardVo.createdAt}" pattern="yyyy-MM-dd" />
											</p>											
										</a>
									</div>
								</c:forEach>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>