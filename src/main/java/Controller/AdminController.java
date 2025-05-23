package Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Service.AdminService;
import Service.MemberService;
import Vo.MemberVo;

@WebServlet("/admin/*")
public class AdminController extends HttpServlet {

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

		System.out.println(">>> 수신된 Action 값: " + action);

		String nextPage = null;

		String result = null;
		boolean resultBloolean = false;
		int resultInt = 0;

		try {
			switch (action) {
			case "/main":
				List<MemberVo> recentMemberList = memberservice.getRecentMembers();
				request.setAttribute("recentMemberList", recentMemberList);
				request.setAttribute("center", "/adm/main.jsp");
				nextPage = "/adm/home.jsp";
				break;

			case "/book":
				request.setAttribute("center", "/adm/adminBook.jsp");
				nextPage = "/adm/home.jsp";
				break;

			// 회원 목록 검색 페이지 보여주기 (초기 진입 또는 검색 후 결과 표시)
			case "/memberSearch":
				// 초기 진입 시에는 검색 조건이 없으므로 전체 회원 목록 또는 빈 목록을 가져와서 표시
				List<MemberVo> memberSearch = memberservice.serviceMemberSearch(request); 
				request.setAttribute("memberList", memberSearch); 

				// 검색 페이지 JSP로 이동
				request.setAttribute("center", "/adm/memberSearch.jsp");
				nextPage = "/adm/home.jsp";
				break;

			// 회원 정보 수정 폼 보여주기
			case "/memberUpdateForm":				
				MemberVo memberToUpdate = memberservice.serviceGetMember(request);
				if (memberToUpdate != null) {
					request.setAttribute("member", memberToUpdate); // 회원 정보를 request에 담기
					request.setAttribute("center", "/adm/memberUpdate.jsp");
					nextPage = "/adm/home.jsp";
				} else {
					// 해당 아이디의 회원을 찾지 못했을 경우 처리 (예: 에러 메시지 표시 후 목록으로 돌아가기)
					System.out.println("AdminMemberController: 수정할 회원을 찾을 수 없습니다.");
					request.setAttribute("errorMessage", "해당 회원을 찾을 수 없습니다.");
					nextPage = "/adm/memberSearch.jsp";
				}
				break;

			// 회원 정보 수정 처리
			case "/memberUpdatePro":				
				boolean updateSuccess = memberservice.serviceUpdateMember(request);

				if (updateSuccess) {
					// 업데이트 성공 시 목록 페이지로 리다이렉트 (POST-Redirect-GET 패턴)
					System.out.println("AdminMemberController: 회원 정보 수정 성공!");
					 response.sendRedirect(request.getContextPath() + "/admin/memberSearch");
					 return;
				} else {

					System.out.println("AdminMemberController: 회원 정보 수정 실패!");
					request.setAttribute("errorMessage", "회원 정보 수정에 실패했습니다.");

					List<MemberVo> memberListAfterError = memberservice.serviceMemberSearch(request);

					request.setAttribute("memberList", memberListAfterError);
					nextPage = "/adm/memberSearch.jsp";
				}
				break;		

			case "/del":
				result = memberservice.serviceMemDeleteAdm(request);
				out.println("<script>");
				out.println("alert('" + result + "');");
				out.println("location.href='" + request.getContextPath() + "/admin/memberSearch'");
				out.println("</script>");
				return;

			default:
				System.out.println("알 수 없는 요청: " + action);
				nextPage = "/adm/home.jsp";
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
	}

}