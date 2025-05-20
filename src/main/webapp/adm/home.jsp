<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
request.setCharacterEncoding("UTF-8");
String contextPath = request.getContextPath();
String id = (String) session.getAttribute("id"); 
System.out.println(id);
if (id == null || !id.equals("admin")) {
%>
<script>
	alert("접근 권한이 없습니다."); 
	history.back(); 
</script>
<%
}
%>

<div class="container">
	<div class="adm-page">
		<h3>관리자 메인</h3>
		
		<ul>
		<li><a href="<%=contextPath%>/admin/memberSearch">회원 목록</a></li>
		<li></li>
		<li></li>
		<li></li>
		</ul>
		
	</div>
</div>
