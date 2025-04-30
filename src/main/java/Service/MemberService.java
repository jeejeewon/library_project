package Service;

import java.net.http.HttpClient;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

import Dao.MemberDao;
import Vo.MemberVo;

public class MemberService {
	private MemberDao memberDao;
	private ObjectMapper objectMapper;
	private HttpClient httpClient;

	// --- 카카오 API 관련 상수 ---
	// private final String KAKAO_CLIENT_ID = "1253f72aad6171c7bb41aaa27655dc1f"; //
	// ** 실제 카카오 앱 REST API 키로 교체 필수 **
	// private final String KAKAO_CLIENT_SECRET = "YOUR_KAKAO_CLIENT_SECRET"; // **
	// 카카오 앱에서 Client Secret을 발급받아 사용하는 경우, 실제 키로 교체 필수. 사용 안 하면 null 또는 빈 문자열 **
	// private final String KAKAO_TOKEN_URI = "https://kauth.kakao.com/oauth/token";
	// // 카카오 토큰 발급 요청 주소
	// private final String KAKAO_USER_INFO_URI =
	// "https://kapi.kakao.com/v2/user/me"; // 카카오 사용자 정보 요청 주소

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

}
