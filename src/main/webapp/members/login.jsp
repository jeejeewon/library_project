<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.security.SecureRandom"%>
<%@ page import="java.math.BigInteger"%>

<%
request.setCharacterEncoding("UTF-8");
String contextPath = request.getContextPath();

// 카카오 api 
String clientId = "5a278aa0ad74ca4b39461b7e9208e622";

// 카카오 로그인 후 리다이렉트
String redirectUriPath = contextPath + "/member/kakaoCallback.me";

// 현재 요청의 서버 정보 동적 확인
String serverName = request.getServerName();
int serverPort = request.getServerPort();
String scheme = request.getScheme();

// 기본 포트(http:80, https:443)가 아닐 경우 포트번호 포함시키기
String portString = "";
if ((scheme.equals("http") && serverPort != 80) || (scheme.equals("https") && serverPort != 443)) {
	portString = ":" + serverPort;
}

//최종 리다이렉트 uri
String redirectUri = scheme + "://" + serverName + portString + redirectUriPath;
System.out.println("생성된 Redirect URI: " + redirectUri); // 개발 확인용 로그

// 리다이렉트 uri url 인코딩
String encodedRedirectUri = URLEncoder.encode(redirectUri, "UTF-8");

// CSRF 방지용 상태 토큰(state) 생성 및 세션 저장
String state = new BigInteger(130, new SecureRandom()).toString();
session.setAttribute("kakao_state", state);

// 최종 카카오 인증 요청 URL 생성
String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize?" + "response_type=code" + "&client_id=" + clientId
		+ "&redirect_uri=" + encodedRedirectUri + "&state=" + state;
//이 페이지에서 사용할 수 있도록 pageContext에 저장 (EL ${kakaoAuthUrl} 사용 가능하도록)
pageContext.setAttribute("kakaoAuthUrl", kakaoAuthUrl);
//컨텍스트 경로도 pageContext에 저장 (EL ${contextPath} 사용 가능하도록)
pageContext.setAttribute("contextPath", contextPath);
%>

<div class="container">
	<div class="login-form-wrap">
		<div class="inner">
			<div class="login-top">
				<h3>로그인</h3>
				<p>로그인을 하시면 더 많은 도서관 서비스를 이용하실 수 있습니다.</p>
			</div>
			<div id="loginForm">
				<form action="<%=contextPath%>/member/loginPro.me" method="post">
					<div class="input-wrap">
						<input type="text" id="id" name="id" placeholder="아이디" required
							autofocus> <input type="password" id="pass" name="pass"
							placeholder="패스워드" required> <input type="submit"
							value="로그인">
						<a href="${kakaoAuthUrl}" class="kakao-btn"><img
						src="https://developers.kakao.com/tool/resource/static/img/button/login/full/ko/kakao_login_medium_wide.png"></a>
					</div>
				</form>
				<div class="login-btn-wrap">
					<a href="<%=contextPath%>/member/forgotIdForm">아이디 찾기</a>
					<div class="fig"></div>
					<a href="<%=contextPath%>/member/forgotPwForm">비밀번호 찾기</a>
					<div class="fig"></div>
					<a href="<%=contextPath%>/member/join">회원가입</a>
				</div>			
			</div>
		</div>
	</div>
</div>