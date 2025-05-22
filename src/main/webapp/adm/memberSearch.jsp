<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="Vo.MemberVo"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%
String contextPath = request.getContextPath();
// Controller에서 request.setAttribute("memberList", memberList); 로 담은 목록
List<MemberVo> memberList = (List<MemberVo>) request.getAttribute("memberList");

// Controller에서 검색 조건을 Map 형태로 담아서 가져옴 (Controller에 이 로직 추가 필요)
// Map<String, String> searchCriteria = (Map<String, String>) request.getAttribute("searchCriteria");
// String currentSearchId = (searchCriteria != null) ? searchCriteria.get("searchId") : "";
// String currentSearchName = (searchCriteria != null) ? searchCriteria.get("searchName") : "";
// String currentSearchEmail = (searchCriteria != null) ? searchCriteria.get("searchEmail") : "";

String currentSearchId = request.getParameter("searchId");
String currentSearchName = request.getParameter("searchName");
String currentSearchEmail = request.getParameter("searchEmail");
if (currentSearchId == null) currentSearchId = ""; // null이면 빈 문자열로 초기화
if (currentSearchName == null) currentSearchName = "";
if (currentSearchEmail == null) currentSearchEmail = "";

// Controller에서 담아준 에러 메시지가 있다면 가져와서 표시
String errorMessage = (String) request.getAttribute("errorMessage");
%>
   

<% if (errorMessage != null) { %>
   <p class="error-message"><%= errorMessage %></p>
<% } %>


<div class="member-search-wrap">

	<h3>회원 검색</h3>
	<form action="<%=contextPath%>/admin/memberSearch" method="get">
		<input type="text" id="searchId" name="searchId"
			value="<%=currentSearchId%>" placeholder="아이디로 검색"> <input type="text"
			id="searchName" name="searchName" value="<%=currentSearchName%>"
			placeholder="이름으로 검색"> <input type="text" id="searchEmail"
			name="searchEmail" value="<%=currentSearchEmail%>"
			placeholder="이메일로 검색">
		<button type="submit">검색</button>
	</form>
	
	<h3>회원 목록</h3>
	<table>
	    <thead>
	        <tr>
	            <th>아이디</th>
	            <th>이름</th>
	            <th>이메일</th>
	            <th>주소</th>
	            <th>가입일</th>
	            <th>수정/삭제</th>
	        </tr>
	    </thead>
	    <tbody>
	        <% if (memberList != null && !memberList.isEmpty()) { %>
	           <% for (MemberVo member : memberList) { %>
	               <tr>
	                   <td><%= member.getId() %></td>
	                   <td><%= member.getName() %></td>
	                   <td><%= member.getEmail() %></td>
	                   <td><%= member.getAddress() %></td>
	                   <td><%= member.getJoinDate() %></td>
	                   <td>
	                      <a href="<%=contextPath%>/admin/memberUpdateForm?id=<%= member.getId() %>">수정</a>
	                      <a href="<%=contextPath%>/admin/del?id=<%= member.getId() %>" style="color:red;">삭제</a>
	                   </td>
	               </tr>
	           <% } %>
	       <% } else { %>
	           <tr>
	               <%
	               // 검색 조건이 있는지 확인해서 결과 없음 메시지 다르게 표시
	               boolean hasSearchCriteria = !currentSearchId.isEmpty() || !currentSearchName.isEmpty() || !currentSearchEmail.isEmpty();
	               %>
	               <td colspan="6" style="text-align: center;">
	                   <% if (hasSearchCriteria) { %>
	                       검색 조건에 맞는 회원 정보가 없습니다.
	                   <% } else { %>
	                       등록된 회원 정보가 없습니다. 또는 검색 조건을 입력하세요.
	                   <% } %>
	               </td>
	           </tr>
	       <% } %>
	    </tbody>
	</table>
</div>
