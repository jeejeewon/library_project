<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
request.setCharacterEncoding("UTF-8");
String contextPath = request.getContextPath();
%>

<div class="system-page">
	<h3>시스템 페이지</h3>
	<ul>
		<li>
			<p>전체 도서</p>
			<a href="<%=contextPath%>/books/bookList.do" target="_blank"><%=contextPath%>/books/bookList.do</a>
		</li>		
		<li>
			<p>도서 관리</p>
			<a href="<%=contextPath%>/books/adminBook.do" target="_blank"><%=contextPath%>/books/adminBook.do</a>
		</li>
		<li>
			<p>시설 관리</p>
			<a href="" target="_blank"><%=contextPath%></a>
		</li>		
		<li>
			<p>전체 회원</p>
			<a href="<%=contextPath%>/admin/memberSearch"><%=contextPath%>/admin/memberSearch</a>
		</li>	
	</ul>
</div>

<div class="recent-member">
	<h3>최근 7일 이내 가입한 회원</h3>
	<table border="1">
		<thead>
			<tr>
				<th>ID</th>
				<th>이름</th>
				<th>성별</th>
				<th>이메일</th>
				<th>연락처</th>
				<th>가입일</th>
				<th>카카오 ID</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="member" items="${requestScope.recentMemberList}">
				<tr>
					<td>${member.id}</td>
					<td>${member.name}</td>
					<td>${member.gender}</td>
					<td>${member.email}</td>
					<td>${member.tel}</td>
					<td>${member.joinDate}</td>
					<td>${member.kakaoId}</td>
				</tr>
			</c:forEach>
			<c:if test="${empty requestScope.recentMemberList}">
				<tr>
					<td colspan="8">최근 7일 이내 가입한 회원이 없습니다.</td>
				</tr>
			</c:if>
		</tbody>
	</table>
</div>

