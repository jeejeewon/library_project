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

@WebServlet("/member/*")
public class MemberController extends HttpServlet {

	MemberService memberservice;

	@Override
	public void init() throws ServletException {
		memberservice = new MemberService();
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

			// 회원 가입 화면 요청
			case "/join":
				String center = memberservice.serviceJoin(request);
				request.setAttribute("center", center);
				nextPage = "/main.jsp";
				break;

			case "/join.me":
				String joinForm = memberservice.serviceJoinForm(request);
				request.setAttribute("center", joinForm);
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
					out.println("<script>window.alert('비밀번호 틀림'); history.go(-1);</script>");
					return;
				} else if (check == -1) {
					out.println("<script>window.alert('아이디 틀림'); history.go(-1);</script>");
					return;
				}
				// 로그인 성공 (check == 1)
				HttpSession session = request.getSession();
				session.setAttribute("id", request.getParameter("id"));
				nextPage = "/main.jsp";
				break;

			// 로그아웃 요청
			case "/logout.me":
				HttpSession session_ = request.getSession(false);
				if (session_ != null) {
					// 세션 무효화 (로그아웃 처리)
					session_.invalidate();
				}
				// 메인 화면으로 이동하도록 설정
				nextPage = "/main.jsp";
				break;

			default:
				System.out.println("알 수 없는 요청: " + action);
				nextPage = "/main.jsp";
				break;
			}

			if (nextPage != null) {
				RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
				dispatch.forward(request, response); // 지정된 페이지로 포워딩
			}

		} catch (Exception e) {
			System.out.println("doHandle 메소드 실행 중 예외 발생: " + e.getMessage());
			e.printStackTrace();
		}

	} // doHandle 끝

}