package Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Service.BookService;
import Service.MemberService;
import Service.boardService;
import Vo.MemberVo;
import Vo.RentalVo;
import Vo.boardVO;

@WebServlet("/member/*")
public class MemberController extends HttpServlet {

	private MemberService memberservice;
	private MemberVo memberVo;
	private BookService bookService;
	private boardService boardService;

	@Override
	public void init() throws ServletException {
		memberservice = new MemberService();
		memberVo = new MemberVo();
		bookService = new BookService();
		boardService = new boardService();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doHandle(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doHandle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		String action = request.getPathInfo();

		System.out.println(action);

		String nextPage = null;

		String result = null;
		boolean resultBloolean = false;
		int resultInt = 0;

		try {
			switch (action) {

			// 회원 가입 페이지 요청
			case "/join":
				String center = memberservice.serviceJoin(request);
				request.setAttribute("center", center);
				nextPage = "/main.jsp";
				break;

			// 아이디 중복 체크
			case "/joinIdCheck.me":
				resultBloolean = memberservice.serviceOverLappedId(request);
				// AJAX 응답을 위해 PrintWriter 얻기
				out = response.getWriter();
				// 결과를 클라이언트(JavaScript)로 전송
				if (resultBloolean) {
					out.write("not_usable");
				} else {
					out.write("usable");
				}
				// AJAX 응답이므로 포워딩하지 않고 종료
				return; // 아래의 포워딩 로직을 실행하지 않음

			// 회원 가입 처리 요청
			case "/joinPro.me":
				memberservice.serviceInsertMember(request);
				nextPage = "login";
				break;

			// 로그인 화면 요청
			case "/login":
				String loginPage = memberservice.serviceLoginMember();
				request.setAttribute("center", loginPage);
				nextPage = "/main.jsp";
				break;

			// 로그인 요청
			case "/loginPro.me":
				// 아이디 체크
				int check = memberservice.serviceUserCheck(request);
				if (check == 0) { // 비밀번호 틀림
					out.println("<script>");
					out.println("window.alert('비밀번호가 틀렸습니다.');");
					out.println("history.go(-1);");
					out.println("</script>");
					return;
				} else if (check == -1) {
					out.println("<script>");
					out.println("window.alert('존재하지 않는 계정입니다.');");
					out.println("history.go(-1);");
					out.println("</script>");
					return;
				}
				// 로그인 성공 (check == 1)
				HttpSession session = request.getSession();
				session.setAttribute("id", request.getParameter("id"));

				// 메인 화면으로 이동하도록 설정
				nextPage = null;
				break;

			// 로그아웃 요청
			case "/logout.me":
				HttpSession session_ = request.getSession(false);
				if (session_ != null) {
					// 세션 무효화 (로그아웃 처리)
					session_.invalidate();
				}
				nextPage = null;
				break;

			// 마이페이지 요청
			case "/mypage":
				HttpSession sessionId = request.getSession(false);
				// 회원 정보 vo
				String userId = (String) sessionId.getAttribute("id");
				if (userId != null) {
					memberVo = memberservice.getMember(userId);
					request.setAttribute("memberVo", memberVo);
					
					// 대여 목록
					Vector<RentalVo> myList = bookService.rentalsByUserByPage(userId, 1, 6);
					request.setAttribute("rentalList", myList);
					
					// 내 서평 목록				
					String searchType = request.getParameter("searchType");
					String searchKeyword = request.getParameter("searchKeyword");		
					
					if (searchType == null) searchType = "title";
					if (searchKeyword == null) searchKeyword = "";		
					
					String sectionParam = request.getParameter("section");
					String pageNumParam = request.getParameter("pageNum");		
				
					int section = Integer.parseInt(sectionParam == null || sectionParam.isEmpty() ? "1" : sectionParam);
					int pageNum = Integer.parseInt(pageNumParam == null || pageNumParam.isEmpty() ? "1" : pageNumParam);					
					
					int category = 2; // 카테고리 2번 (내서평)					
				
					String currentUserId = (String) request.getSession().getAttribute("id");					
					Map<String, Object> resultMap = boardService.getBoardList(category, section, pageNum, searchKeyword, searchType, currentUserId);
			
					List<boardVO> boardList = (List<boardVO>)resultMap.get("boardList"); //게시글목록
					int totalPage = (int) resultMap.get("totalPage"); // 총 페이지 수
					int totalSection = (int) resultMap.get("totalSection"); //총 섹션 수
					int totalBoardCount = (int) resultMap.get("totalBoardCount"); //총 게시글 수
					
					//정보들을 request에 저장하기
				    request.setAttribute("searchKeyword", searchKeyword);
				    request.setAttribute("searchType", searchType);
				    request.setAttribute("boardList", boardList);
				    request.setAttribute("totalPage", totalPage);
				    request.setAttribute("totalSection", totalSection);
				    request.setAttribute("totalBoardCount", totalBoardCount);
				    request.setAttribute("section", section);
				    request.setAttribute("pageNum", pageNum);					

				} else {
					System.out.println("로그인 정보가 없습니다.");
				}

				String mypage = memberservice.serviceMypage(request);
				request.setAttribute("center", mypage);
				nextPage = "/main.jsp";
				break;

			// 본인 확인 요청
			case "/modify":
				String passPage = memberservice.servicePassForm(request);
				request.setAttribute("center", passPage);
				nextPage = "/main.jsp";
				break;

			// 본인 확인 처리 요청
			case "/modify.me":
				// 비밀번호 체크
				int passCheck = memberservice.serviceUserCheck(request);
				if (passCheck == 0) { // 비밀번호 틀림
					out.println("<script>");
					out.println("window.alert('비밀번호가 틀렸습니다.');");
					out.println("history.go(-1);");
					out.println("</script>");
					return;
				} else if (passCheck == -1) {
					out.println("<script>");
					out.println("window.alert('로그인을 하셔야 접근 가능 합니다.');");
					out.println("history.go(-1);");
					out.println("</script>");
					return;
				}

				// 회원 정보 vo
				memberVo = memberservice.getMember(request.getParameter("id"));
				request.setAttribute("memberVo", memberVo);

				String modify = memberservice.serviceUserModify(request);
				request.setAttribute("center", modify);

				nextPage = "/main.jsp";
				break;

			// 회원 정보 수정 요청
			case "/modifyPro.me":
				resultInt = memberservice.serviceMemUpdate(request);

				if (resultInt == 1) {
					String mypageMain = memberservice.serviceMypage(request);
					request.setAttribute("center", mypageMain);
				} else {
					out.println("<script>");
					out.println("alert('회원 수정 불가합니다.');");
					out.println("history.go(-1);");
					out.println("</script>");
					return;
				}
				nextPage = "/main.jsp";
				break;

			// 회원 탈퇴(삭제) 페이지 요청
			case "/leave":
				String leave = memberservice.serviceLeave(request);
				request.setAttribute("center", leave);
				nextPage = "/main.jsp";
				break;

			// 회원 탈퇴(삭제) 요청
			case "/leave.me":
				result = memberservice.serviceMemDelete(request);
				out.println("<script>");
				out.println("alert('" + result + "');");
				out.println("location.href='" + request.getContextPath() + "/index.jsp'");
				out.println("</script>");
				return;

			// 아이디 찾기 페이지 요청
			case "/forgotIdForm":
				String forgotId = memberservice.serviceForgotIdform(request);
				request.setAttribute("center", forgotId);
				nextPage = "/main.jsp";
				break;

			// 아이디 결과 페이지 요청
			case "/forgotIdPro.do":
				MemberVo foundMember = memberservice.serviceForgotId(request);
				if (foundMember != null) {
					System.out.println("회원 찾음!");
					request.setAttribute("id", foundMember.getId());
					request.setAttribute("message", "아이디를 찾았습니다!");
					request.setAttribute("center", "members/findIdByEmail.jsp");

				} else {
					System.out.println("회원 못 찾음!");
					request.setAttribute("id", null);
					request.setAttribute("message", "일치하는 회원 정보가 없습니다.");
					out.println("<script>");
					out.println("alert('회원 정보를 찾을 수 없습니다.');");
					out.println("history.go(-1);");
					out.println("</script>");
					nextPage = "/main.jsp";
					return;
				}
				nextPage = "/main.jsp";
				break;

			// 비번 찾기 페이지 요청
			case "/forgotPwForm":
				String forgotPw = memberservice.serviceForgotPwform(request);
				request.setAttribute("center", forgotPw);
				nextPage = "/main.jsp";
				break;

			case "/changePwForm": // 메일 인증번호 페이지 요청(이동)

				String changePwForm = memberservice.serviceForgotPwform(request);
				request.setAttribute("center", changePwForm);
				nextPage = "/main.jsp";
				break;

			// 메일 인증
			case "/forgotPwPro.do":

				String requestURI = request.getRequestURI();
				String contextPath = request.getContextPath();
				String command = requestURI.substring(contextPath.length());

				String redirect = memberservice.serviceForgotPw(request, response);
				request.setAttribute("center", redirect);

				System.out.println(redirect);
				nextPage = "/member/pwdChange.do";
				break;

			case "/changePw.do": // 인증코드 이용, 새 비밀번호 변경 페이지 요청
				try {
					memberservice.serviceAuthenCode(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;

			case "/pwdChange.do": // 비밀번호 변경 페이지 요청
				request.setAttribute("center", "members/changePwForm.jsp");
				nextPage = "/main.jsp";
				break;

			// 카카오 로그인 요청
			case "/kakaoCallback.me":
				// 요청 파라미터에서 인증 코드(code)와 상태 토큰(state) 가져오기
				String code = request.getParameter("code");
				String state = request.getParameter("state");
				// 세션에서 로그인 요청 시 저장했던 상태 토큰(kakao_state) 가져오기
				HttpSession kakaoSession = request.getSession();
				String sessionState = (String) kakaoSession.getAttribute("kakao_state");

				// MemberService의 카카오 로그인 처리 메소드 호출
				MemberVo memberVo = null;

				try {
					memberVo = memberservice.loginOrRegisterKakaoUser(code, state, sessionState, request);
					if (memberVo != null) {
						// 세션 생성 및 사용자 정보 저장 (기존 로그인과 동일하게 'id' 속성 사용)
						kakaoSession.setAttribute("id", memberVo.getId());
						kakaoSession.setAttribute("name", memberVo.getName());
						response.sendRedirect("member/loginPro.me");
						// System.out.println("카카오 로그인 성공: User ID=" + memberVo.getId());
						return;
					} else {
						System.out.println("카카오 로그인/회원가입 처리 실패 (MemberService 반환값 null)");
						request.setAttribute("error", "카카오 로그인 처리 중 오류가 발생했습니다.");
						nextPage = "/login";
					}

				} catch (SecurityException se) { // State 불일치 등 보안 예외
					System.out.println("카카오 로그인 보안 오류: " + se.getMessage());
					request.setAttribute("error", "카카오 로그인 처리 중 오류가 발생했습니다.");
					nextPage = "login";

				} catch (Exception e) {
					System.out.println("카카오 로그인 처리 중 예외 발생: " + e.getMessage());
					e.printStackTrace();
					request.setAttribute("error", "카카오 로그인 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
					nextPage = "login";
				}
				return;

			default:
				System.out.println("알 수 없는 요청: " + action);
				nextPage = "/main.jsp";
				break;
			}

			if (nextPage != null) {
				RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
				dispatch.forward(request, response); // 지정된 페이지로 포워딩
			} else {
				response.sendRedirect(request.getContextPath() + "/view/main");
			}

		} catch (Exception e) {
			System.out.println("doHandle 메소드 실행 중 예외 발생: " + e.getMessage());
			e.printStackTrace();
		}

	} // doHandle 끝

}