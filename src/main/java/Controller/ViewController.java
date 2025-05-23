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

import Vo.boardVO;

@WebServlet("/view/*")
public class ViewController extends HttpServlet {

	private Service.boardService boardService;

	@Override
	public void init() throws ServletException {
		super.init();
		boardService = new Service.boardService();
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

		HttpSession session = request.getSession();
		String id = (String) session.getAttribute("id");

		if (action.equals("/main")) {

			System.out.println("/view/main 요청 처리 시작");

			try {
				System.out.println("이벤트 배너 데이터 가져오는 중...");
				List<boardVO> eventBannerList = boardService.getLatestEventBanners(6);
				request.setAttribute("eventBannerList", eventBannerList);
				System.out.println("가져온 이벤트 배너 개수: " + (eventBannerList != null ? eventBannerList.size() : 0));
                if (eventBannerList != null) {
                    System.out.println("각 배너 이미지 파일 이름 확인");
                    for (int i = 0; i < eventBannerList.size(); i++) {
                        boardVO banner = eventBannerList.get(i);
                        System.out.println("배너 " + (i+1) + " 이미지 파일 이름: " + banner.getBannerImg());
                    }
                }
				

				System.out.println("공지사항 데이터 가져오는 중...");
				List<boardVO> noticeList = boardService.getLatestNotices(5); 
				request.setAttribute("noticeList", noticeList);
				System.out.println("가져온 공지사항 개수: " + (noticeList != null ? noticeList.size() : 0)); 

				nextPage = "/main.jsp";
				System.out.println("/main.jsp 로 포워딩 예정");

			} catch (Exception e) {

				e.printStackTrace();
				System.err.println("메인 페이지 데이터 로딩 중 오류 발생: " + e.getMessage());

				nextPage = "/main.jsp";
			}

			if (nextPage != null) {
				RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
				dispatch.forward(request, response);
			} else {
				System.out.println("nextPage가 null입니다. (아마도 pw.print로 직접 응답 처리됨)");
			}

		}

	}
}
