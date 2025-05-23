package Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import Dao.MemberDao;
import Vo.KakaoTokenResponseVo;
import Vo.KakaoUserInfoVo;
import Vo.MemberVo;
import mail.NaverMailSend;

public class MemberService {
	private MemberDao memberDao;
	private ObjectMapper objectMapper;
	private HttpClient httpClient;

	// --- 카카오 API 관련 상수 ---
	// 실제 운영 시에는 설정 파일(.properties, .yml) 또는 환경 변수에서 안전하게 로드해야 합니다.
	private final String KAKAO_CLIENT_ID = "5a278aa0ad74ca4b39461b7e9208e622";
	private final String KAKAO_CLIENT_SECRET = "";
	private final String KAKAO_TOKEN_URI = "https://kauth.kakao.com/oauth/token"; // 카카오 토큰 발급 요청 주소
	private final String KAKAO_USER_INFO_URI = "https://kapi.kakao.com/v2/user/me"; // 카카오 사용자 정보 요청 주소

	// 의존하는 객체들(MemberDAO, ObjectMapper, HttpClient)을 초기화
	public MemberService() {
		memberDao = new MemberDao(); // MemberDAO 인스턴스 생성
		objectMapper = new ObjectMapper(); // Jackson ObjectMapper 인스턴스 생성
		httpClient = HttpClient.newBuilder().build(); // 기본 설정으로 HttpClient 인스턴스 생성
	}

	public String serviceLoginMember() {
		return "members/login.jsp";
	}

	public int serviceUserCheck(HttpServletRequest request) {
		String login_id = request.getParameter("id");
		String login_pass = request.getParameter("pass");

		String m_id = request.getParameter("m_id");
		String m_pass = request.getParameter("m_pass");

		// MemberDAO의 userCheck 메소드를 호출하여 DB에서 아이디와 비밀번호 일치 여부
		return memberDao.userCheck(login_id, login_pass);
	}

	public String serviceJoin(HttpServletRequest request) {
		return "members/join.jsp";
	}

	public Boolean serviceOverLappedId(HttpServletRequest request) {
		String id = request.getParameter("id");
		return memberDao.overlappedId(id);
	}

	public void serviceInsertMember(HttpServletRequest request) {
		String user_id = request.getParameter("id");
		String user_pass = request.getParameter("pass");
		String user_name = request.getParameter("name");
		String user_gender = request.getParameter("gender");
		String user_address = request.getParameter("address");
		String user_email = request.getParameter("email");
		String user_tel = request.getParameter("tel");

		// 추출한 정보로 MemberVo 객체 생성
		MemberVo vo = new MemberVo(user_id, user_pass, user_name, user_gender, user_address, user_email, user_tel);
		memberDao.insertMember(vo);
	}

	public String serviceMypage(HttpServletRequest request) {
		return "members/mypage.jsp";
	}

	public String servicePassForm(HttpServletRequest request) {
		return "members/pass.jsp";
	}

	public String serviceUserModify(HttpServletRequest request) {
		return "members/modify.jsp";
	}

	public String serviceLeave(HttpServletRequest request) {
		return "members/leave.jsp";
	}

	public String serviceForgotIdform(HttpServletRequest request) {
		return "members/forgotIdForm.jsp";
	}

	public MemberVo serviceForgotId(HttpServletRequest request) {

		String email = request.getParameter("email");

		MemberVo member = memberDao.selectEmail(email);

		if (member != null) {
			System.out.println("찾은 회원 이메일: " + email);
			return member;
		} else {
			System.out.println("입력된 이메일의 회원 정보 없음: " + email);
			return null;
		}

	}

	public String serviceFindIdByEmail(HttpServletRequest request) {
		return "members/fondIdByEmail.jsp";
	}

	public String serviceForgotPwform(HttpServletRequest request) {
		return "members/forgotPwForm.jsp";
	}

	// 회원 정보 조회
	public MemberVo getMember(String id) {
		return memberDao.memberInfo(id);
	}

	// 회원 정보 수정
	public int serviceMemUpdate(HttpServletRequest request) {
		MemberVo memberVo = new MemberVo();

		memberVo.setId(request.getParameter("id"));
		memberVo.setPass(request.getParameter("pass"));
		memberVo.setName(request.getParameter("name"));
		memberVo.setGender(request.getParameter("gender"));
		memberVo.setAddress(request.getParameter("address"));
		memberVo.setEmail(request.getParameter("email"));
		memberVo.setTel(request.getParameter("tel"));

		return memberDao.memUpdate(memberVo);
	}

	// 회원 탈퇴
	public String serviceMemDelete(HttpServletRequest request) {
		String id = request.getParameter("id");

		// 세션 삭제
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate(); // 세션 무효화
		}

		return memberDao.memDelete(id);
	}
	
	public String serviceMemDeleteAdm(HttpServletRequest request) {
		String id = request.getParameter("id");

		return memberDao.memDelete(id);
	}

	// 카카오 로그인 관련 메소드
	public MemberVo loginOrRegisterKakaoUser(String code, String state, String sessionState, HttpServletRequest request)
			throws IOException, InterruptedException, SecurityException {

		// State 검증 (CSRF 방어): 콜백으로 받은 state와 세션의 state 비교
		if (sessionState == null || state == null || !sessionState.equals(state)) {
			System.out.println("카카오 로그인 오류: State 값이 일치하지 않습니다!");
			// 실제 운영 환경에서는 로깅 후 사용자에게 에러 페이지를 보여주는 것이 좋습니다.
			throw new SecurityException("유효하지 않은 접근입니다. (state 불일치)");
		}
		// 검증 후 세션의 state 값 제거 (재사용 방지)
		HttpSession session = request.getSession();
		session.removeAttribute("kakao_state");

		// 인증 코드(code)를 사용하여 카카오로부터 액세스 토큰(access_token) 요청
		String accessToken = getKakaoAccessToken(code, request);
		if (accessToken == null) {
			System.out.println("카카오 로그인 오류: 액세스 토큰 발급에 실패했습니다.");
			return null; // 토큰 발급 실패 시 null 반환
		}

		// 발급받은 액세스 토큰을 사용하여 카카오로부터 사용자 정보 요청
		KakaoUserInfoVo kakaoUserInfo = getKakaoUserInfo(accessToken);
		if (kakaoUserInfo == null || kakaoUserInfo.getId() == null) {
			System.out.println("카카오 로그인 오류: 사용자 정보 조회에 실패했습니다.");
			return null;
		}

		// 카카오 사용자 정보 기반으로 애플리케이션 회원 조회 또는 신규 등록
		String kakaoId = String.valueOf(kakaoUserInfo.getId()); // 카카오 고유 ID (Long -> String)
		MemberVo memberVo = memberDao.findMemberByKakaoId(kakaoId); // DB에서 카카오 ID로 회원 조회

		if (memberVo == null) {
			System.out.println("카카오 신규 회원 감지...");
			memberVo = registerNewKakaoMember(kakaoUserInfo);

			if (memberVo == null) {
				System.out.println("카카오 로그인 오류: 신규 회원 등록에 실패했습니다.");
				return null; // 회원 등록 실패 시 null 반환
			}
			System.out.println("카카오 신규 회원 등록 완료: ID=" + memberVo.getId());

		} else {
			System.out.println("기존 카카오 연동 회원 확인: ID=" + memberVo.getId());
		}

		return memberVo;
	};

	// 카카오 액세스 토큰 발급
	private String getKakaoAccessToken(String code, HttpServletRequest request)
			throws IOException, InterruptedException {
		String redirectUri = generateRedirectUri(request);

		// 카카오 토큰 API 요청 본문 데이터 구성 (Map 사용)
		Map<Object, Object> data = new HashMap<>();
		data.put("grant_type", "authorization_code");
		data.put("client_id", KAKAO_CLIENT_ID);
		data.put("redirect_uri", redirectUri);
		data.put("code", code);

		// Client Secret을 사용하는 경우, Map에 추가
		if (KAKAO_CLIENT_SECRET != null && !KAKAO_CLIENT_SECRET.isEmpty()) {
			data.put("client_secret", KAKAO_CLIENT_SECRET);
		}

		// Java 11 HttpClient를 사용하여 카카오 토큰 API에 POST 요청 생성
		HttpRequest tokenRequest = HttpRequest.newBuilder().uri(URI.create(KAKAO_TOKEN_URI)) // 요청 URI 설정
				.header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8") // 헤더 설정
				.POST(HttpRequest.BodyPublishers.ofString(buildFormData(data))) // POST 방식 및 Form 데이터 설정
				.build(); // HttpRequest 객체 생성

		// 동기 방식으로 HTTP 요청 전송 및 응답 수신
		// BodyHandlers.ofString()은 응답 본문을 UTF-8 문자열로 변환
		HttpResponse<String> response = httpClient.send(tokenRequest, HttpResponse.BodyHandlers.ofString());

		// HTTP 응답 상태 코드 확인 및 처리
		if (response.statusCode() == 200) { // 성공 (HTTP 200 OK)
			try {
				// 응답 본문(JSON)을 KakaoTokenResponseVo 객체로 변환 (Jackson 사용)
				KakaoTokenResponseVo tokenResponse = objectMapper.readValue(response.body(),
						KakaoTokenResponseVo.class);
				System.out.println("카카오 액세스 토큰 수신 성공."); // 로그 출력
				return tokenResponse.getAccessToken(); // 액세스 토큰 값 반환
			} catch (Exception e) {
				System.out.println("카카오 토큰 응답 JSON 파싱 오류: " + e.getMessage());
				e.printStackTrace();
				return null;
			}
		} else { // 실패 (HTTP 200 외 상태 코드)
			System.out.println("카카오 토큰 발급 실패. 상태 코드: " + response.statusCode() + ", 응답 본문: " + response.body());
			return null;
		}
	}

	// 카카오 액세스 토큰을 사용하여 카카오 API 서버로부터 사용자 정보 조회
	private KakaoUserInfoVo getKakaoUserInfo(String accessToken) throws IOException, InterruptedException {
		// Java 11 HttpClient를 사용하여 카카오 사용자 정보 API에 GET 요청 생성
		HttpRequest userInfoRequest = HttpRequest.newBuilder().uri(URI.create(KAKAO_USER_INFO_URI)) // 요청 URI 설정
				.header("Authorization", "Bearer " + accessToken) // 인증 헤더 설정 (Bearer 타입)
				.header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8") // 헤더 설정
				.GET() // GET 방식 요청 (POST도 가능)
				.build(); // HttpRequest 객체 생성

		// 동기 방식으로 HTTP 요청 전송 및 응답 수신
		HttpResponse<String> response = httpClient.send(userInfoRequest, HttpResponse.BodyHandlers.ofString());

		// HTTP 응답 상태 코드 확인 및 처리
		if (response.statusCode() == 200) { // 성공 (HTTP 200 OK)
			try {
				// 응답 본문(JSON)을 KakaoUserInfoVo 객체로 변환 (Jackson 사용)
				KakaoUserInfoVo userInfo = objectMapper.readValue(response.body(), KakaoUserInfoVo.class);
				// 로그 출력 (사용자 ID 및 이메일 - null 체크 포함)
				System.out.println("카카오 사용자 정보 수신 성공: ID=" + userInfo.getId() + ", Email="
						+ (userInfo.getKakaoAccount() != null ? userInfo.getKakaoAccount().getEmail() : "N/A"));
				return userInfo; // 사용자 정보 DTO 반환
			} catch (Exception e) {
				System.out.println("카카오 사용자 정보 응답 JSON 파싱 오류: " + e.getMessage());
				e.printStackTrace();
				return null;
			}
		} else { // 실패 (HTTP 200 외 상태 코드)
			System.out.println("카카오 사용자 정보 조회 실패. 상태 코드: " + response.statusCode() + ", 응답 본문: " + response.body());
			return null;
		}
	}

	// 카카오 사용자 정보를 바탕으로 신규 회원 등록
	private MemberVo registerNewKakaoMember(KakaoUserInfoVo kakaoUserInfo) {
		MemberVo newMember = new MemberVo();
		String kakaoId = String.valueOf(kakaoUserInfo.getId());
		// 이메일, 닉네임 정보 추출 (null 가능성 확인)
		String email = null;
		String nickname = null;

		if (kakaoUserInfo.getKakaoAccount() != null) {
			email = kakaoUserInfo.getKakaoAccount().getEmail(); // 사용자가 동의 안 했거나 없으면 null
			if (kakaoUserInfo.getKakaoAccount().getProfile() != null) {
				nickname = kakaoUserInfo.getKakaoAccount().getProfile().getNickname();
			}
		}

		String generatedId = null;
		boolean isEmailId = false; // 이메일을 ID로 사용하는지 여부 플래그

		if (email != null && email.length() <= 12 && !memberDao.overlappedId(email)) {
			generatedId = email;
			isEmailId = true;
			System.out.println("카카오 회원 ID 생성: 이메일 사용 -> " + generatedId);
		} else {
			// 카카오 ID 기반으로 ID 생성 (접두사 "k_" + 카카오 ID)
			generatedId = "k_" + kakaoId;
			// DB ID 컬럼 길이(12자 가정)에 맞게 자르기 (고유성 문제 발생 가능성 있음)
			if (generatedId.length() > 12) {
				generatedId = generatedId.substring(0, 12);
			}
			System.out.println("카카오 회원 ID 생성 시도: 카카오ID 기반 -> " + generatedId);
			// 생성된 ID가 혹시 중복될 경우 (다른 카카오 사용자와 앞 10자리가 겹치는 등)
			// 뒤에 숫자를 붙여서 중복 회피 시도 (최대 5번)
			int tryCount = 0;
			while (memberDao.overlappedId(generatedId) && tryCount < 5) {
				String baseId = generatedId.substring(0, Math.min(11, generatedId.length())); // 숫자 붙일 공간 확보
				generatedId = baseId + (tryCount + 1); // 최대 12자
				System.out.println("ID 중복 발생, 재시도 -> " + generatedId);
				tryCount++;
			}
			// 5번 시도 후에도 중복이면 ID 생성 실패 처리
			if (memberDao.overlappedId(generatedId)) {
				System.out.println("카카오 회원 고유 ID 생성 실패: " + kakaoId);
				return null;
			}
		}

		// MemberVo 객체 필드 설정
		newMember.setId(generatedId);
		// 비민번호 설정
		// UUID 사용
		newMember.setPass(UUID.randomUUID().toString());

		newMember.setName(nickname != null ? nickname : "카카오회원"); // 이름은 카카오 닉네임 사용, 없으면 기본값
		newMember.setEmail(email); // 이메일 정보가 없거나 동의 안 했으면 null
		newMember.setKakaoId(kakaoId);
		newMember.setGender(null); // 예시: null (DB에서 허용 시)
		newMember.setAddress(null);
		newMember.setTel(null);

		// MemberDAO를 통해 DB에 회원 정보 삽입
		try {
			memberDao.insertMember(newMember);
			// 성공적으로 삽입 후, DB에서 다시 조회하여 reg_date 등 완전한 정보를 포함한 객체 반환 (선택 사항)
			// 여기서는 kakaoId로 다시 조회하여 반환
			return memberDao.findMemberByKakaoId(kakaoId);
		} catch (Exception e) {
			System.out.println("신규 카카오 회원 DB 삽입 오류: " + e.getMessage());
			e.printStackTrace();
			// 이메일을 ID로 사용하려다 실패한 경우, ID 생성 재시도 로직 추가 고려 가능
			return null;
		}
	}

	// --- Helper Methods (유틸리티 메소드) ---

	private String generateRedirectUri(HttpServletRequest request) {
		String contextPath = request.getContextPath();
		String redirectUriPath = contextPath + "/member/kakaoCallback.me"; // 콜백 서블릿 경로
		String serverName = request.getServerName(); // 서버 도메인 또는 IP
		int serverPort = request.getServerPort(); // 서버 포트
		String scheme = request.getScheme(); // 프로토콜 (http 또는 https)
		String portString = ""; // 포트 번호 문자열 (기본 포트 아닐 때만 포함)

		// HTTP(80) 또는 HTTPS(443) 기본 포트가 아닌 경우에만 포트 번호 추가
		if ((scheme.equals("http") && serverPort != 80) || (scheme.equals("https") && serverPort != 443)) {
			portString = ":" + serverPort;
		}
		// 최종 Redirect URI 조합: "http(s)://서버이름:포트번호/컨텍스트경로/콜백경로"
		return scheme + "://" + serverName + portString + redirectUriPath;
	}

	private String buildFormData(Map<Object, Object> data) {
		StringBuilder builder = new StringBuilder();
		// Map의 각 엔트리(Key-Value 쌍)에 대해 반복
		for (Map.Entry<Object, Object> entry : data.entrySet()) {
			// 첫 번째 파라미터가 아니면 구분자 '&' 추가
			if (builder.length() > 0) {
				builder.append("&");
			}
			// Key를 UTF-8 URL 인코딩하여 추가
			builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
			builder.append("=");
			// Value를 UTF-8 URL 인코딩하여 추가
			builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
		}
		// 최종적으로 빌드된 문자열 반환
		return builder.toString();
	}

	// 메일 인증번호 받기
	public String serviceForgotPw(HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession(false);

		if ((String) session.getAttribute("id") != null) {// 로그인된 상태

			session.invalidate();

			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('접근 권한이 없습니다.');");
			out.println("location.href='" + request.getContextPath() + "/main.jsp';");
			out.println("</script>");
		}

		String email = request.getParameter("email");
		String id = request.getParameter("id");

		MemberService service = new MemberService();
		MemberVo member = service.getEmail(id, email);
		if (member == null || !member.getEmail().equals(email)) {
			// 회원 정보가 일치하지 않은 경우
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('회원 정보가 존재하지 않습니다.');");
			out.println("history.back(-1);");
			out.println("</script>");
		} else {
			// 메일 전송
			NaverMailSend sendMail = new NaverMailSend();
			String authenticationCode = sendMail.sendEmail(email);

			// 포워딩
			session.setAttribute("authenticationCode", authenticationCode);
			request.setAttribute("id", id);
			request.setAttribute("email", email);
		}
		return "/member/pwdChange.do";
	}

	// 아이디와 이메일 조회
	public MemberVo getEmail(String id, String email) {
		return memberDao.selectMember(id, email);
	}

	// 인증코드 이용, 새 비밀번호 변경 페이지 요청
	public void serviceAuthenCode(HttpServletRequest request, HttpServletResponse response) throws Exception {

		PrintWriter out = response.getWriter();

		HttpSession session = request.getSession(false);
		// 비로그인 상태 authenticationCode
		String authenCode1 = (String) session.getAttribute("authenticationCode"); // 받은 인증번호
		String newAuthenCode = request.getParameter("newAuthenCode"); // 입력된 인증번호 값

		System.out.println(authenCode1);
		if (authenCode1.equals(newAuthenCode)) {// 인증번호가 맞을떄

			// 인증코드가 저장된 속성이 있는 경우
			String id = request.getParameter("id");
			String newPw = request.getParameter("newPw");

			int updateResult = 0;

			updateResult = memberDao.updatePw(id, newPw);
			if (updateResult == 1) {
				out.println("<script>");
				out.println("alert('정상적으로 수정되었습니다. 로그인 페이지로 돌아갑니다.');");
				out.println("location.href='" + request.getContextPath() + "/member/login';");
				out.println("</script>");
			} else {
				out.println("<script>");
				out.println("alert('비밀번호 수정에 실패했습니다.');");
				out.println("history.back(-1);");
				out.println("</script>");
			}

		} else {// 인증번호가 틀릴떄
			response.setContentType("text/html;charset=utf-8");

			out.println("<script>");
			out.println("alert('잘못된 접근 권한입니다.');");
			out.println("location.href='" + request.getContextPath() + "/index.jsp';");
			out.println("</script>");
		}

	}

	// ------ 관리자용 ------
	// 회원 비즈니스 로직 처리 객체 (Service)

	// 회원 목록 검색 비즈니스 로직
	// Controller에서 받은 request 객체에서 검색 조건을 추출하여 DAO에 넘기고 결과를 반환
	public List<MemberVo> serviceMemberSearch(HttpServletRequest request) {

		// request에서 검색 조건들 가져오기
		String searchId = request.getParameter("searchId");
		String searchName = request.getParameter("searchName");
		String searchEmail = request.getParameter("searchEmail");

		// 검색 조건들을 Map에 담기
		Map<String, String> searchCriteria = new HashMap<>();
		if (searchId != null && !searchId.trim().isEmpty()) {
			searchCriteria.put("searchId", searchId.trim());
		}
		if (searchName != null && !searchName.trim().isEmpty()) {
			searchCriteria.put("searchName", searchName.trim());
		}
		if (searchEmail != null && !searchEmail.trim().isEmpty()) {
			searchCriteria.put("searchEmail", searchEmail.trim());
		}

		// DAO에게 검색 요청하고 결과 받기
		List<MemberVo> memberList = memberDao.selectMemberList(searchCriteria);

		return memberList; // 검색 결과 리스트 반환
	}

	// 특정 회원 정보 가져오는 비즈니스 로직
	// Controller에서 받은 request에서 회원 아이디를 추출하여 DAO에 넘기고 결과를 반환
	public MemberVo serviceGetMember(HttpServletRequest request) {
		String memberId = request.getParameter("id"); // request 파라미터 이름 'id'와 맞춰야 함

		if (memberId == null || memberId.trim().isEmpty()) {
			System.out.println("Service / serviceGetMember: 아이디 파라미터가 누락되었습니다.");
			return null; // 아이디가 없으면 null 반환
		}

		// DAO에게 특정 회원 정보 요청하고 결과 받기
		MemberVo member = memberDao.selectMember(memberId.trim());

		return member; // 찾은 회원 정보 또는 null 반환
	}

	// 회원 정보 수정 비즈니스 로직
	// Controller에서 받은 request에서 수정할 회원 정보를 추출하여 DAO에 넘기고 결과를 반환
	public boolean serviceUpdateMember(HttpServletRequest request) {

		// request에서 수정할 회원 정보 가져오기
		String id = request.getParameter("id"); // hidden 필드 등으로 받아와야 할 아이디
		String pass = request.getParameter("pass");
		String name = request.getParameter("name");
		String gender = request.getParameter("gender");
		String address = request.getParameter("address");
		String email = request.getParameter("email");
		String tel = request.getParameter("tel");
		// joindate, kakao_id는 보통 수정하지 않으므로 제외

		// 필수 값이 누락되었는지 간단히 체크 (더 복잡한 유효성 검사는 Service에서 하는 것이 좋음)
		if (id == null || id.trim().isEmpty() || pass == null || pass.trim().isEmpty() || name == null
				|| name.trim().isEmpty()) {
			System.out.println("Service / serviceUpdateMember: 필수 수정 정보 누락");
			return false; // 필수 정보가 없으면 실패
		}

		// MemberVo 객체에 담기
		MemberVo member = new MemberVo();
		member.setId(id.trim());
		member.setPass(pass.trim());
		member.setName(name.trim());
		member.setGender(gender != null ? gender.trim() : null); // null 체크
		member.setAddress(address != null ? address.trim() : null);
		member.setEmail(email != null ? email.trim() : null);
		member.setTel(tel != null ? tel.trim() : null);

		// DAO에게 업데이트 요청하고 결과 받기
		int updateCount = memberDao.updateMember(member);

		// 업데이트된 행의 수가 1개 이상이면 성공으로 판단
		return updateCount > 0;
	}

	public List<MemberVo> getRecentMembers() {

		List<MemberVo> recentMemberList = this.memberDao.selectRecentMembers();
		return recentMemberList;
	}

}
