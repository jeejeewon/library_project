<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.security.SecureRandom"%>
<%@ page import="java.math.BigInteger"%>

<%

String contextPath = request.getContextPath();

// 카카오 REST API 키 설정
String clientId = "1253f72aad6171c7bb41aaa27655dc1f";

// 카카오 로그인 후 리다이렉트 될 uri
String redirectUriPath = contextPath + "/member/kakaoCallback.me";

String serverName = request.getServerName();
int serverPort = request.getServerPort();
String scheme = request.getScheme();

String portString = "";
if ((scheme.equals("http") && serverPort != 80) || (scheme.equals("https") && serverPort != 443)) {
	portString = ":" + serverPort;
}

// 최종 리다이렉트 uri
String redirectUri = scheme + "://" + serverName + portString + redirectUriPath;
String encodedRedirectUri = URLEncoder.encode(redirectUri, "UTF-8");
String state = new BigInteger(130, new SecureRandom()).toString();
session.setAttribute("kakao_state", state);

// 카카오 인증 요청 URL 생성
String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize?" + "response_type=code" + "&client_id=" + clientId
		+ "&redirect_uri=" + encodedRedirectUri + "&state=" + state;
//이 페이지에서 사용할 수 있도록 pageContext에 저장 (EL ${kakaoAuthUrl} 사용 가능하도록)
pageContext.setAttribute("kakaoAuthUrl", kakaoAuthUrl);
//컨텍스트 경로도 pageContext에 저장 (EL ${contextPath} 사용 가능하도록)
pageContext.setAttribute("contextPath", contextPath);

%>

<div id="loginForm">

	<form action="${contextPath}/member/loginPro.me" method="post">
		<input type="text" id="id" name="id" placeholder="아이디" required
			autofocus> <input type="password" id="pass" name="pass"
			placeholder="패스워드" required> <input type="submit" value="로그인">
	</form>
	<a href="${kakaoAuthUrl}" class="block mt-4 text-center"> <img
		src="https://k.kakaocdn.net/14/dn/btroDszwNrM/RVcydeogHRr3oIFJ2UkuK0/o.jpg"
		alt="카카오 로그인 버튼">
	</a>
</div>