package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

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
		   int start = Integer.parseInt(request.getParameter("StartTime").split(":")[0]); //문자열로 받아온 시작시간을 int형으로 변환
		   int end = Integer.parseInt(request.getParameter("EndTime").split(":")[0]); //문자열로 받아온 종료시간을 int형으로 변환
				
		   System.out.println("선택날짜 : " + date); System.out.println("시작시간 : " + start);
		   System.out.println("종료시간 : " + end);
		   
		   //예약날짜를 선택하지 않았을 경우
		   if(date == null || date.equals("")) {			     
			   response.setContentType("application/json; charset=utf-8");			   
			    out.write("{\"error\": \"이용날짜를 선택해 주세요.\"}"); //ajax 에러메세지 리턴
			    out.flush();
			    out.close();			    
			    return;
		   }
		  			
		   //1. DB에서 예약가능한 미팅룸 조회 (나중에 예약이 되어 있으면 상태값이 0으로 바뀔거임)
		   //2. 예약가능한 미팅룸 리스트를 List로 받아오기
		   //Service에서 비즈니스 로직 처리
		   List roomList = roomReserveService.MeetingRoomList(date, start, end);
		   
		   System.out.println("예약가능한 미팅룸 리스트 : " + roomList);
		   
		   
		   //3. 받은 리스트를 JSON형식으로 변환하기 (jackson 라이브러리 사용 - ObjectMapper)
		   ObjectMapper objectMapper = new ObjectMapper(); 
		   String json = objectMapper.writeValueAsString(roomList);
		    
		   System.out.println("JSON형식으로 변환된 데이터 : " + json);
		   		   
		   //4. JSON형식으로 변환된 데이터를 ajax로 리턴하기
		   response.setContentType("application/json; charset=utf-8");

		   //ajax로 반환
		   out.write(json); //ajax로 JSON형식으로 변환된 데이터 리턴
		   out.flush();
		   out.close();    
		   return; //ajax로 리턴했으므로 다음 페이지로 포워딩할 필요 없음
		   
		}
		
		
		if(nextPage != null) {			
			System.out.println("포워딩 실행 : " + nextPage);
			request.getRequestDispatcher(nextPage).forward(request, response);			
		}else {
			System.out.println("nextPage가 null입니다.");
		}
		
	}//doHandle
	
}
