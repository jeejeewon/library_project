package Controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//시설 예약 컨트롤러
@WebServlet("/reserve/*")
public class roomReserveController extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		String contextPath = request.getContextPath();	
		String action = request.getPathInfo();		
//		
		System.out.println("doHandle() 호출됨! action = " + action);
		
		String nextPage = null;
			
		//[시설예약] 클릭시 보여줄 뷰
		if(action.equals("/room")) {
			
			request.setAttribute("center", "/libReserve/reserveRoom.jsp");
			
			nextPage = "/main.jsp";			
				
		//[ 도서관안내 ]-[ 시설예약 ]-[ 스터디룸 예약 ] 뷰	
		}else if(action.equals("/reserveStudy")) {
			
			request.setAttribute("center", "/libReserve/reserveStudy.jsp");
			
			nextPage = "/main.jsp";		
			
		//[ 도서관안내 ]-[ 시설예약 ]-[ 미팅룸 예약 ] 뷰	
		}else if(action.equals("/reserveMeeting")) {
			
			request.setAttribute("center", "/libReserve/reserveMeeting.jsp");
			
			nextPage = "/main.jsp";	
			
		//[ 내서재 ]-[ 시설예약내역 ] 뷰	
		}else if(action.equals("/reserveCheck")) {
			
			request.setAttribute("center", "/libReserve/reserveCheck.jsp");
			
			nextPage = "/main.jsp";	
			
		//[ 관리자메뉴 ]-[ 시설예약관리 ] 뷰	
		}else if(action.equals("/reserveAdmin")) {
			
			request.setAttribute("center", "/libReserve/reserveAdmin.jsp");
			
			nextPage = "/main.jsp";	
		}
		
		
		if(nextPage != null) {			
			System.out.println("포워딩 실행 : " + nextPage);
			request.getRequestDispatcher(nextPage).forward(request, response);			
		}else {
			System.out.println("nextPage가 null입니다.");
		}
		
	}//doHandle
	
}
