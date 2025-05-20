<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="Vo.MemberVo"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%
String contextPath = request.getContextPath();
// Controller에서 request.setAttribute("memberList", memberList); 로 담은 목록을 가져와요
List<MemberVo> memberList = (List<MemberVo>) request.getAttribute("memberList");

// Controller에서 검색 조건을 Map 형태로 담아줬다면 가져올 수 있어요 (Controller에 이 로직 추가 필요)
// Map<String, String> searchCriteria = (Map<String, String>) request.getAttribute("searchCriteria");
// String currentSearchId = (searchCriteria != null) ? searchCriteria.get("searchId") : "";
// String currentSearchName = (searchCriteria != null) ? searchCriteria.get("searchName") : "";
// String currentSearchEmail = (searchCriteria != null) ? searchCriteria.get("searchEmail") : "";

// 간단하게는 request 파라미터에서 직접 가져와서 input에 미리 채워넣을 수도 있어요
String currentSearchId = request.getParameter("searchId");
String currentSearchName = request.getParameter("searchName");
String currentSearchEmail = request.getParameter("searchEmail");
if (currentSearchId == null) currentSearchId = ""; // null이면 빈 문자열로 초기화
if (currentSearchName == null) currentSearchName = "";
if (currentSearchEmail == null) currentSearchEmail = "";

// Controller에서 담아준 에러 메시지가 있다면 가져와서 표시
String errorMessage = (String) request.getAttribute("errorMessage");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 회원 검색</title>
<style>
    table { width: 100%; border-collapse: collapse; }
    th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
    th { background-color: #f2f2f2; }
    .error-message { color: red; margin-bottom: 10px; }
</style>
</head>
<body>

    <div class="container">
        <h3>관리자 회원 검색</h3>

        <% if (errorMessage != null) { %>
            <p class="error-message"><%= errorMessage %></p>
        <% } %>

        <!-- ✨ 회원 검색 폼 ✨ -->
        <form action="<%=contextPath%>/admin/memberSearch" method="get">
            <label for="searchId">아이디:</label>
            <input type="text" id="searchId" name="searchId" value="<%= currentSearchId %>">
            <label for="searchName">이름:</label>
            <input type="text" id="searchName" name="searchName" value="<%= currentSearchName %>">
            <label for="searchEmail">이메일:</label>
            <input type="text" id="searchEmail" name="searchEmail" value="<%= currentSearchEmail %>">
            <button type="submit">검색</button>
        </form>

        <br>

        <!-- ✨ 검색 결과 목록 표시 ✨ -->
        <h4>회원 목록</h4>
        <table>
            <thead>
                <tr>
                    <th>아이디</th>
                    <th>이름</th>
                    <th>이메일</th>
                    <th>주소</th>
                    <th>가입일</th>
                    <th>수정</th>
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
                                <!-- ✨ 수정 버튼/링크: 클릭 시 해당 회원의 아이디를 파라미터로 넘겨서 수정 폼 요청 ✨ -->
                                <a href="<%=contextPath%>/admin/memberUpdateForm?id=<%= member.getId() %>">수정</a>
                                <%-- 삭제 기능도 추가한다면 여기에 추가 --%>
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

</body>
</html>