package Service;

import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

import Dao.MemberDao;
import Vo.MemberVo;

public class AdminService {
	private MemberDao memberDao;
	private ObjectMapper objectMapper;
	private HttpClient httpClient;

	public AdminService() {
		memberDao = new MemberDao(); // MemberDAO 인스턴스 생성
		objectMapper = new ObjectMapper(); // Jackson ObjectMapper 인스턴스 생성
		httpClient = HttpClient.newBuilder().build(); // 기본 설정으로 HttpClient 인스턴스 생성
	}

	
}
