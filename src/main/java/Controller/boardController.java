package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAO.boardDAO;
import Service.boardService;
import VO.boardVO;

@WebServlet("/bbs/*")
public class boardController extends HttpServlet {

	private boardService boardService;

	@Override
	public void init(ServletConfig config) throws ServletException {
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

	private void doHandle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String nextPage = null;
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		
		String action = request.getPathInfo();
		System.out.println("action :" + action);

/*-------------------------------------공지사항게시판---------------------------------------*/
		// 요청주소 "/bbs/noticeList.do"
		if (action.equals("/noticeList.do")) {

			// 조회된 게시판을
			Vector<boardVO> boardList = boardService.getNoticeList();

			// 조회된 목록을 request에 "boardList"라는 이름으로 저장하기
			request.setAttribute("boardList", boardList);

			// 메인화면 중앙에 보여줄 noticeList.jsp를 request에 "center"라는 이름으로 저장하기
			request.setAttribute("center", "board/noticeList.jsp");

			// 최종적으로 보여줄 메인페이지 경로를 nextPage에 저장하기
			nextPage = "/main.jsp";
			
		}
		
		// 요청주소 "/bbs/noticeWrite.do"
		if(action.equals("/noticeWrite.do")) {//요청명이 noticeWrite.do이면 글쓰기 화면이 나타남
			
			// 메인화면 중앙에 보여줄 noticeList.jsp를 request에 "center"라는 이름으로 저장하기
			request.setAttribute("center", "board/noticeWrite.jsp");

			// 최종적으로 보여줄 메인페이지 경로를 nextPage에 저장하기
			nextPage = "/main.jsp";
		}
		
		if(action.equals("/AddNotice.do")) {//요청명이 AddNotice.do이면 글쓰기 처리
			
			Map<String, String> writeMap = boardService.uploadFile(request, response);
			
			
			
			
			// 전체글을 다시 DB에서 겅색하여 보여주기 위해 다음과 같은 주소를 저장
			nextPage = "/bbs/noticeList.do";
		}
		
		
/*-------------------------------------문의게시판---------------------------------------*/		
		// 요청주소 "/bbs/noticeList.do"
		if (action.equals("/questionList.do")) {
		
			// 조회된 게시판을
			Vector<boardVO> boardList = boardService.getquestionList();
			
			// 조회된 목록을 request에 "boardList"라는 이름으로 저장하기
			request.setAttribute("boardList", boardList);
			
			// 메인화면 중앙에 보여줄 noticeList.jsp를 request에 "center"라는 이름으로 저장하기
			request.setAttribute("center", "board/questionList.jsp");
			
			// 최종적으로 보여줄 메인페이지 경로를 nextPage에 저장하기
			nextPage = "/main.jsp";
			
		}

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

		// nextPage변수에 값이 할당된 경우(즉, 직접 응답하지 않고 JSP(VIEW)로 포워딩 해야하는 경우)
		if (nextPage != null) {
			// 1. RequestDispatcher객체를 얻어옵니다
			// - request.getRequestDispatcher(경로): 지정된 경로의 JSP 페이지로 요청을 전달할 수 있는 객체를 생성합니다.
			RequestDispatcher dispatche = request.getRequestDispatcher(nextPage);

			// 2. forward()메소드를 호출하여 현재 요청(request)과 응답(response)객체를
			// nextPage변수에 저장한 JSP페이지로 전달합니다.
			// - 제어권이 해당 JSP페이지로 완전히 넘어 갑니다.
			dispatche.forward(request, response);

		}
	}
}
