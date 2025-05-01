package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Service.roomReserveService;

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
		
		//비즈니스 로직 처리할 Service 객체 생성
		roomReserveService roomReserveService = new roomReserveService();		
		
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
			
		}else if(action.equals("/meetingRoomList")) {
				
		   String date = request.getParameter("Date");
		   String start = request.getParameter("StartTime");
		   String end = request.getParameter("EndTime");
				
		   System.out.println("선택날짜 : " + date); System.out.println("시작시간 : " + start);
		   System.out.println("종료시간 : " + end);
		   
		   //예약날짜를 선택하지 않았을 경우
		   if(date == null || date.equals("")) {			     
			   response.setContentType("application/json; charset=utf-8");			   
			    out.write("{\"error\": \"예약날짜를 선택하세요.\"}"); //ajax 에러메세지 리턴
			    out.flush();
			    out.close();			    
			    return;
		   }
		  
			
		   //1. DB에서 예약가능한 미팅룸 조회 (나중에 예약이 되어 있으면 상태값이 0으로 바뀔거임)
		   /*
		   		쿼리문
		   		 select * 											-- 조건에 만족한 시설의 모든 값을 조회
				 from library_room				
				 where reserve_state = 1							-- 조건1. 예약상태가 1(예약가능한 상태)
					   and room_code not in (						-- 조건2. 서브쿼리문(조회된 예약건)을 제외
							select reserve_room						-- 시설예약테이블에서 사용자가 입력한 날짜, 시간에 해당하는 예약 건 조회
				            from room_reserve		
				            where reserve_date = "사용자가 입력한 날짜"
								and reserve_start >= "시작시간" 		-- 13을 시작시간으로 선택할 경우 13시부터~
				                and reserve_end <= "종료시간"			-- 15를 종료시간으로 선택할 경우 ~15시까지     
		   */
		   
		   //2. 예약가능한 미팅룸리스트를 JSONList로 받아서 ajax로 리턴
		   //3. ajax에서 리턴받은 JSONList를 이용해서 예약가능한 미팅룸 리스트를 화면에 출력 (버튼으로 만들어서)
		   
		   //예약가능한 미팅룸 리스트 가져오기
		   
		   
			return;
		}
		
		
		if(nextPage != null) {			
			System.out.println("포워딩 실행 : " + nextPage);
			request.getRequestDispatcher(nextPage).forward(request, response);			
		}else {
			System.out.println("nextPage가 null입니다.");
		}
		
	}//doHandle
	
}
