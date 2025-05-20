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

		System.out.println(action);

		String nextPage = null;

		String result = null;
		boolean resultBloolean = false;
		int resultInt = 0;

		try {
			switch (action) {

			// 회원 목록 검색 페이지 보여주기 (초기 진입 또는 검색 후 결과 표시)
			case "/memberSearch":
				// 초기 진입 시에는 검색 조건이 없으므로 전체 회원 목록 또는 빈 목록을 가져와서 표시
				List<MemberVo> memberList = memberservice.serviceMemberSearch(request); // Service에서 request 파싱해서 검색
				request.setAttribute("memberList", memberList); // 검색 결과 리스트를 request에 담기

				// 검색 페이지 JSP로 이동 (여기에 검색 폼과 결과 목록이 함께 표시될 거에요)
				nextPage = "/adm/memberSearch.jsp";
				break;

			// 회원 정보 수정 폼 보여주기
			case "/memberUpdateForm":
				// request에서 수정할 회원의 아이디를 가져와서
				MemberVo memberToUpdate = memberservice.serviceGetMember(request);
				if (memberToUpdate != null) {
					request.setAttribute("member", memberToUpdate); // 회원 정보를 request에 담기
					nextPage = "/adm/memberUpdate.jsp"; // 수정 폼 JSP로 이동
				} else {
					// 해당 아이디의 회원을 찾지 못했을 경우 처리 (예: 에러 메시지 표시 후 목록으로 돌아가기)
					System.out.println("AdminMemberController: 수정할 회원을 찾을 수 없습니다.");
					request.setAttribute("errorMessage", "해당 회원을 찾을 수 없습니다.");
					nextPage = "/adm/memberSearch.jsp";
				}
				break;

			// 회원 정보 수정 처리
			case "/memberUpdatePro":
				// request에서 수정된 정보를 받아서 Service에게 업데이트 요청
				boolean updateSuccess = memberservice.serviceUpdateMember(request);

				if (updateSuccess) {
					// 업데이트 성공 시 목록 페이지로 리다이렉트 (POST-Redirect-GET 패턴)
					System.out.println("AdminMemberController: 회원 정보 수정 성공!");
					// 리다이렉트 시에는 requestScope 데이터가 사라지므로 파라미터로 메시지 전달 고려
					// response.sendRedirect(request.getContextPath() +
					// "/admin/memberSearch.admin?message=updateSuccess");
					nextPage = request.getContextPath() + "/admin/memberSearch"; // 리다이렉트 경로
				} else {
					// 업데이트 실패 시 에러 메시지 표시 후 수정 폼이나 목록 페이지로 이동
					System.out.println("AdminMemberController: 회원 정보 수정 실패!");
					request.setAttribute("errorMessage", "회원 정보 수정에 실패했습니다.");
				
					List<MemberVo> memberListAfterError = memberservice.serviceMemberSearch(request); 
																									
					request.setAttribute("memberList", memberListAfterError);
					nextPage = "/adm/memberSearch.jsp";
				}
				break; // 리다이렉트 또는 포워드를 위한 break

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
	}

}