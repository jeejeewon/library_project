package Controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//시설 안내 컨트롤러
@WebServlet("/info/*")
public class libraryInfoController extends HttpServlet {

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
		
		//[도서관안내] 및 [도서관소개] 클릭시 보여줄 뷰
		if(action.equals("/mapinfo")) {
			
			request.setAttribute("center", "/information/mapInfo.jsp");
			
			nextPage = "/main.jsp";		
			
		//[이용안내] 클릭시 보여줄 뷰			
		}else if(action.equals("/useinfo")) {
			
			request.setAttribute("center", "/information/useInfo.jsp");
			
			nextPage = "/main.jsp";	
		
		//[오시는길] 클릭시 보여줄 뷰	
		}else if(action.equals("/map")) {
			
			request.setAttribute("center", "/information/map.jsp");
			
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
