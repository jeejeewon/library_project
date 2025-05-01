package Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Service.MemberService;
import Vo.MemberVo;

@WebServlet("/member/*")
public class MemberController extends HttpServlet {

	private MemberService memberservice;
	private MemberVo memberVo;

	@Override
	public void init() throws ServletException {
		memberservice = new MemberService();
		memberVo = new MemberVo();
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
				boolean result = memberservice.serviceOverLappedId(request);
				// AJAX 응답을 위해 PrintWriter 얻기
				out = response.getWriter();
				// 결과를 클라이언트(JavaScript)로 전송
				if (result) {
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
					out.println("<script>window.alert('비밀번호가 틀렸습니다.'); history.go(-1);</script>");
					return;
				} else if (check == -1) {
					out.println("<script>window.alert('존재하지 않는 계정입니다.'); history.go(-1);</script>");
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
				String mypage = memberservice.serviceMypage(request);
				request.setAttribute("center", mypage);
				nextPage = "/main.jsp";
				break;

			// 본인 확인 요청
			case "/modify":
				String passPage = memberservice.servicepassForm(request);
				request.setAttribute("center", passPage);
				nextPage = "/main.jsp";
				break;

			// 본인 확인 처리 요청
			case "/modify.me":
				// 비밀번호 체크
				int passCheck = memberservice.serviceUserCheck(request);
				if (passCheck == 0) { // 비밀번호 틀림
					out.println("<script>window.alert('비밀번호가 틀렸습니다.'); history.go(-1);</script>");
					return;
				} else if (passCheck == -1) {
					out.println("<script>window.alert('로그인을 하셔야 접근 가능 합니다.'); history.go(-1)</script>");
					return;
				}

				// 회원 정보 vo
				memberVo = memberservice.getMember(request.getParameter("id"));
				request.setAttribute("memberVo", memberVo);

				String modify = memberservice.serviceuserModify(request);
				request.setAttribute("center", modify);

				nextPage = "/main.jsp";
				break;

			// 본인 확인 처리 요청
			case "/modifyPro.me":
				break;

			default:
				System.out.println("알 수 없는 요청: " + action);
				nextPage = "/main.jsp";
				break;
			}

			if (nextPage != null) {
				RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
				dispatch.forward(request, response); // 지정된 페이지로 포워딩
			} else {
				response.sendRedirect("view/main");
			}

		} catch (Exception e) {
			System.out.println("doHandle 메소드 실행 중 예외 발생: " + e.getMessage());
			e.printStackTrace();
		}

	} // doHandle 끝

}