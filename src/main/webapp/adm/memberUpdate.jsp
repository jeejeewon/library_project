<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="Vo.MemberVo"%>
<%
String contextPath = request.getContextPath();
// Controller에서 request.setAttribute("member", memberToUpdate); 로 담은 회원 정보를 가져와요
MemberVo member = (MemberVo) request.getAttribute("member");

// 회원 정보가 없으면 (예: 잘못된 아이디로 접근) 에러 메시지 표시나 다른 처리 필요
if (member == null) {
    // 간단하게 에러 메시지 출력 후 종료하거나, 에러 페이지로 리다이렉트
    out.println("<script>alert('회원 정보를 불러오는데 실패했습니다.'); history.back();</script>");
    return; // JSP 실행 중단
}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 회원 정보 수정</title>
<style>
    .form-group { margin-bottom: 15px; }
    label { display: inline-block; width: 80px; }
    input[type="text"], input[type="email"], input[type="tel"], input[type="password"] { width: 200px; padding: 5px; }
</style>
</head>
<body>

    <div class="container">
        <h3>회원 정보 수정</h3>

        <!-- ✨ 회원 정보 수정 폼 ✨ -->
        <!-- method는 POST로 보내는 것이 좋습니다. -->
        <form action="<%=contextPath%>/admin/memberUpdatePro" method="post">
            <!-- 수정할 회원의 아이디는 사용자에게 보여주되, 수정은 못하게 하고 hidden 필드로 넘겨야 해요! -->
            <div class="form-group">
                <label for="id">아이디:</label>
                <%= member.getId() %> <!-- 아이디는 수정 불가하므로 텍스트로 표시 -->
                <input type="hidden" id="id" name="id" value="<%= member.getId() %>"> <!-- ✨ hidden 필드로 아이디 값 넘기기! ✨ -->
            </div>

             <div class="form-group">
                <label for="pass">비밀번호:</label>
                <input type="password" id="pass" name="pass" value="<%= member.getPass() %>"> <%-- 보안상 비밀번호는 빈칸으로 두거나 새 비밀번호 입력 칸을 따로 만드는 것이 좋습니다 --%>
            </div>

            <div class="form-group">
                <label for="name">이름:</label>
                <input type="text" id="name" name="name" value="<%= member.getName() %>">
            </div>

             <div class="form-group">
                <label for="gender">성별:</label>
                 <%-- 성별은 라디오 버튼이나 셀렉트 박스로 선택하게 하는 것이 좋습니다 --%>
                <input type="text" id="gender" name="gender" value="<%= member.getGender() %>">
            </div>

            <div class="form-group">
                <label for="address">주소:</label>
                <input type="text" id="address" name="address" value="<%= member.getAddress() %>">
            </div>

            <div class="form-group">
                <label for="email">이메일:</label>
                <input type="email" id="email" name="email" value="<%= member.getEmail() %>">
            </div>

            <div class="form-group">
                <label for="tel">전화번호:</label>
                <input type="tel" id="tel" name="tel" value="<%= member.getTel() %>">
            </div>

            <%-- 가입일, 카카오ID는 보통 수정하지 않으므로 표시만 하거나 생략 --%>
             <div class="form-group">
                <label>가입일:</label>
                <%= member.getJoinDate() %>
            </div>
             <div class="form-group">
                <label>카카오ID:</label>
                <%= member.getKakaoId() != null ? member.getKakaoId() : "없음" %>
            </div>


            <br>
            <div>
                <button type="submit">정보 수정</button>
                <!-- 목록으로 돌아가는 버튼/링크 -->
                <button type="button" onclick="location.href='<%=contextPath%>/admin/memberSearch.admin'">취소</button>
            </div>
        </form>
    </div>

</body>
</html>