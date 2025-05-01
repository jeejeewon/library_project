<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.security.SecureRandom"%>
<%@ page import="java.math.BigInteger"%>

<%
request.setCharacterEncoding("UTF-8"); 
String contextPath = request.getContextPath();
String serverName = request.getServerName();
int serverPort = request.getServerPort();
String scheme = request.getScheme();

String portString = "";
if ((scheme.equals("http") && serverPort != 80) || (scheme.equals("https") && serverPort != 443)) {
	portString = ":" + serverPort;
}

// 카카오 로그인 후 리다이렉트
String redirectUriPath = contextPath + "/member/kakaoCallback.me"; 

String redirectUri = scheme + "://" + serverName + portString + redirectUriPath;
String encodedRedirectUri = URLEncoder.encode(redirectUri, "UTF-8");

String state = new BigInteger(130, new SecureRandom()).toString();
session.setAttribute("kakao_state", state);

%>

로그인 페이지

<div id="loginForm">
	<form action="<%=contextPath%>/member/loginPro.me" method="post">
		<input type="text" id="id" name="id" placeholder="아이디" required autofocus>
		<input type="password" id="pass" name="pass" placeholder="패스워드" required>
		<input type="submit" value="로그인">
	</form>
	<a href="${kakaoAuthUrl}" class="block mt-4 text-center">카카오 로그인</a>
</div>
