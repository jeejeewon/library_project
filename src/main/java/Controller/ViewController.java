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

@WebServlet("/view/*")
public class ViewController extends HttpServlet {

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
			nextPage = "/main.jsp";
		}

		if (nextPage != null) {          
            RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);            
            dispatch.forward(request, response);
        } else {           
            System.out.println("nextPage가 null입니다. (아마도 pw.print로 직접 응답 처리됨)" );
        }

	} // doHandle 끝

}
