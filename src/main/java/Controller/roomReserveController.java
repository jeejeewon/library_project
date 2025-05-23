package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import Service.roomReserveService;
import Vo.libraryReserveVO;

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
			
			//로그인한 사용자만 스터디룸 예약 가능
			//로그인한 사용자 정보 가져오기
			HttpSession session = request.getSession();
			String userId = (String)session.getAttribute("id");
//			System.out.println("userId : " + userId);

			//로그인을 하지 않은 경우
			if(userId == null || userId.equals("")) {
				//로그인 페이지로 이동
				response.setContentType("text/html;charset=utf-8");
				out.println("<script>alert('로그인 후 이용 가능합니다.'); location.href='" + contextPath + "/member/login';</script>");
				out.flush();
				out.close();
				return;
			}			
					
			request.setAttribute("center", "/libReserve/reserveStudy.jsp");			
			nextPage = "/main.jsp";		
			
			
		//[ 도서관안내 ]-[ 시설예약 ]-[ 미팅룸 예약 ] 뷰	
		}else if(action.equals("/reserveMeeting")) {	
			
			//로그인한 사용자만 미팅룸 예약 가능
			//로그인한 사용자 정보 가져오기
			HttpSession session = request.getSession();
			String userId = (String)session.getAttribute("id");
//			System.out.println("userId : " + userId);

			//로그인을 하지 않은 경우
			if(userId == null || userId.equals("")) {
				//로그인 페이지로 이동
				response.setContentType("text/html;charset=utf-8");
				out.println("<script>alert('로그인 후 이용 가능합니다.'); location.href='" + contextPath + "/member/login';</script>");
				out.flush();
				out.close();
				return;
			}
			
			request.setAttribute("center", "/libReserve/reserveMeeting.jsp");			
			nextPage = "/main.jsp";	
			
			
		//[ 내서재 ]-[ 시설예약내역 ] 뷰	
		}else if(action.equals("/reserveCheck")) {
			
			//로그인한 사용자 정보 가져오기
			HttpSession session = request.getSession();
			String userId = (String)session.getAttribute("id");
			
			if(userId == null || userId.equals("")) {
				//로그인 페이지로 이동
				response.setContentType("text/html;charset=utf-8");
				out.println("<script>alert('로그인 후 이용 가능합니다.'); location.href='" + contextPath + "/member/login';</script>");
				out.flush();
				out.close();
				return;
			}
			
			//로그인한 사용자의 예약내역을 List로 받아오기
			List<libraryReserveVO> reserveList = roomReserveService.selectReserveList(userId);
							
			//날짜를 비교하여 이용전/이용중/이용완료 상태값 저장
			for(libraryReserveVO vo : reserveList) {
				
				//날짜 비교를 위해 날짜와 시간 합침
				LocalDate reserveDate  = vo.getReserveDate().toLocalDate();
				int reserveStart = vo.getReserveStart();
				int reserveEnd = vo.getReserveEnd();
				
				LocalDateTime startDateTime = reserveDate.atTime(reserveStart, 0);
				LocalDateTime endDateTime = reserveDate.atTime(reserveEnd, 0);
				LocalDateTime now = LocalDateTime.now();
				
				//상태값 계산
				String status = "";
				
				if(now.isBefore(startDateTime)) {
					status = "이용전";
				}else if(!now.isAfter(endDateTime)) {
					status = "이용중";
				}else {
					status = "이용완료";
				}			
				//vo에 저장
				vo.setStatus(status);
			}
			
			//List를 request에 바인딩
			request.setAttribute("reserveList", reserveList);
			
			System.out.println("reserveList : " + reserveList);	
			
			request.setAttribute("center", "/libReserve/reserveCheck.jsp");				
			
			nextPage = "/main.jsp";	
			
			
		//[ 관리자메뉴 ]-[ 시설예약관리 ] 뷰	
		}else if(action.equals("/reserveAdmin")) {		
			
			//로그인한 사용자 정보 가져오기
			HttpSession session = request.getSession();
			String userId = (String)session.getAttribute("id");
			
			System.out.println("id : " + userId);
			
			//관리자가 아닐 경우 
			if(userId == null || !userId.equals("admin")) {
				response.setContentType("text/html;charset=utf-8");
				out.println("<script>alert('접근 권한이 없습니다. 관리자에게 문의하세요.');  location.href='" + contextPath + "/view/main';</script>");
				out.flush();
				out.close();
				return;
			}
			
			request.setAttribute("center", "/libReserve/reserveAdmin.jsp");			
			nextPage = "/main.jsp";	
		
			
		//미팅룸 예약시 사용자가 선택한 날짜와 시간에 맞는 미팅룸 리스트를 ajax로 리턴
		}else if(action.equals("/meetingRoomList")) {
				
		   String date = request.getParameter("Date");
		   int start = Integer.parseInt(request.getParameter("StartTime").split(":")[0]); //문자열로 받아온 시작시간을 int형으로 변환
		   int end = Integer.parseInt(request.getParameter("EndTime").split(":")[0]); //문자열로 받아온 종료시간을 int형으로 변환
				
		   System.out.println("선택날짜 : " + date); 
		   System.out.println("시작시간 : " + start);
		   System.out.println("종료시간 : " + end);
		   
		   //예약날짜를 선택하지 않았을 경우
		   if(date == null || date.equals("")) {			     
			   response.setContentType("application/json; charset=utf-8");			   
			    out.write("{\"error\": \"이용날짜를 선택해 주세요.\"}"); //ajax 에러메세지 리턴
			    out.flush();
			    out.close();			    
			    return;
		   }
		  			
		   //DB에서 예약가능한 미팅룸 조회 
		   //예약가능한 미팅룸 리스트를 List로 받아오기
		   List roomList = roomReserveService.MeetingRoomList(date, start, end);
		   
		   System.out.println("예약가능한 미팅룸 리스트 : " + roomList);		   
		   
		   //받은 리스트를 JSON형식으로 변환하기 (jackson 라이브러리 사용 - ObjectMapper)
		   ObjectMapper objectMapper = new ObjectMapper(); 
		   String json = objectMapper.writeValueAsString(roomList);
		    
		   System.out.println("JSON형식으로 변환된 데이터 : " + json);
		   		   
		   //JSON형식으로 변환된 데이터를 ajax로 리턴하기
		   response.setContentType("application/json; charset=utf-8");

		   //ajax로 반환
		   out.write(json); //ajax로 JSON형식으로 변환된 데이터 리턴
		   out.flush();
		   out.close();    
		   return; //ajax로 리턴했으므로 다음 페이지로 포워딩할 필요 없음
		  
		   
		//미팅룸 예약하기 버튼 클릭시 입력한 정보대로 예약 진행
		}else if(action.equals("/meetingRoomReserve")) {
			
			System.out.println("meetingRoomReserve호출됨===============");
			
			//예약정보를 받아오기
			String userId = request.getParameter("userID"); //예약자 아이디
			
			String selectedDate = request.getParameter("reserveDate"); //예약날짜
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			java.util.Date utilDate = null; //utilDate는 java.util.Date형
			Date reserveDate = null;		//sqlDate는 java.sql.Date형
			
			try {
				utilDate = dateFormat.parse(selectedDate);
				reserveDate = new Date(utilDate.getTime()); //java.util.Date형을 java.sql.Date형으로 변환
			} catch (Exception e) {
				e.printStackTrace();
			}
								
			int StartTime = Integer.parseInt(request.getParameter("StartTime").split(":")[0]); //예약 시작시간
			int EndTime = Integer.parseInt(request.getParameter("EndTime").split(":")[0]); //예약 종료시간
			String roomCode = request.getParameter("roomCode"); //예약한 미팅룸 코드
			String roomName = request.getParameter("roomName");
			
			System.out.println("예약자 아이디 : " + userId);
			System.out.println("예약날짜 : " + reserveDate);
			System.out.println("예약 시작시간 : " + StartTime);
			System.out.println("예약 종료시간 : " + EndTime);
			System.out.println("예약한 미팅룸 코드 : " + roomCode);
			System.out.println("예약한 미팅룸 : " + roomName);
			
			//받아온 정보를 VO객체에 저장
			libraryReserveVO vo = new libraryReserveVO(roomCode, userId, reserveDate, StartTime, EndTime);
			vo.setRoomName(roomName);
			
			//vo를 service로 넘겨서 비즈니스 로직 처리
			int result = roomReserveService.reserveMeetingRoom(vo);
			System.out.println(result);
			
			if(result == 1) {
				response.setStatus(response.SC_OK);
				out.write("OK");
			}else {
				response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
				out.write("FAIL");
			}
			   out.flush();
			   out.close();  
			
			return;
			
			
		//시설 예약 삭제 	
		}else if(action.equals("/deleteReserve")) {
			
			System.out.println("deleteReserve호출됨===============");
			
			//삭제할 예약 내역의 예약아이디와 예약번호 값 가져오기
			String reserveId = request.getParameter("reserve_id");
			String reserveNum = request.getParameter("reserve_num");
			
			System.out.println("삭제 예약 아이디 : " + reserveId);
			System.out.println("삭제 예약 번호 : " + reserveNum);
			
			//아이디와 예약번호를 service로 넘겨서 비즈니스 로직 처리
			roomReserveService.deleteReserve(reserveId, reserveNum);

			return;
		
			
		//시설 예약 수정
		}else if(action.equals("/updateReserve")) {
			
			System.out.println("updateReserve호출됨===============");
			
			//수정할 예약 내역의 예약아이디와 예약번호 값 가져오기
			String reserveId = request.getParameter("reserve_id");
			String reserveNum = request.getParameter("reserve_num");
			
			System.out.println("수정 예약 아이디 : " + reserveId);
			System.out.println("수정 예약 번호 : " + reserveNum);
				
			return;	
			
			
		//스터디룸 실시간 좌석 현황	
		}else if(action.equals("/studyRoomList")) {
			
			System.out.println("studyRoomList호출됨===============");
			
			String date = request.getParameter("Date");
			String startTime = request.getParameter("StartTime");
			String endTime = request.getParameter("EndTime");
			
			//예약 시간을 불러오지 못했을 경우 (당일 19시 이후 예약할 경우)
			if (startTime == null || endTime == null) {
			    System.out.println("예약 시간을 불러오지 못했습니다.");
			    response.setContentType("application/json; charset=utf-8");
			    response.getWriter().write("\"NO_AVAILABLE_TIME\"");
			    return;
			}
			
			int start = Integer.parseInt(startTime.split(":")[0]); //문자열로 받아온 시작시간을 int형으로 변환
			int end = Integer.parseInt(endTime.split(":")[0]); //문자열로 받아온 종료시간을 int형으로 변환
			String studyRoom = request.getParameter("studyRoom");
			
			System.out.println("선택날짜 : " + date); 
			System.out.println("시작시간 : " + start);
			System.out.println("종료시간 : " + end);						
			System.out.println("선택한 스터디룸 : " + studyRoom);	
			
		   //DB에서 실시간 좌석 현황 조회
		   //실시간 좌석 현황을 리스트를 List로 받아오기
		   List seatList = roomReserveService.studySeatList(date, start, end, studyRoom);
		   
		   System.out.println("예약가능한 스터디룸 리스트 : " + seatList);		   		 
		   		   
		   //받은 리스트를 JSON형식으로 변환하기 (jackson 라이브러리 사용 - ObjectMapper)
		   ObjectMapper objectMapper = new ObjectMapper(); 
		   String json = objectMapper.writeValueAsString(seatList);
		    
		   System.out.println("JSON형식으로 변환된 데이터 : " + json);
		   		   
		   //JSON형식으로 변환된 데이터를 ajax로 리턴하기
		   response.setContentType("application/json; charset=utf-8");

		   //ajax로 반환
		   out.write(json); //ajax로 JSON형식으로 변환된 데이터 리턴
		   out.flush();
		   out.close();    		
			
			return; //ajax로 리턴
			
			
		//스터디룸 예약 진행
		}else if(action.equals("/studyRoomReserve")) {
			
			System.out.println("studyRoomReserve호출됨===============");
			
			//예약정보를 받아오기
			String userId = request.getParameter("userID"); 
			
			String selectedDate = request.getParameter("reserveDate"); //예약날짜
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			java.util.Date utilDate = null; //utilDate는 java.util.Date형
			Date reserveDate = null;		//sqlDate는 java.sql.Date형
			
			try {
				utilDate = dateFormat.parse(selectedDate);
				reserveDate = new Date(utilDate.getTime()); //java.util.Date형을 java.sql.Date형으로 변환
			} catch (Exception e) {
				e.printStackTrace();
			}
								
			int StartTime = Integer.parseInt(request.getParameter("StartTime").split(":")[0]); //예약 시작시간
			int EndTime = Integer.parseInt(request.getParameter("EndTime").split(":")[0]); //예약 종료시간
			String roomCode = request.getParameter("roomCode"); //선택한 스터디룸
			int seat = Integer.parseInt(request.getParameter("seat")); //선택한 좌석
			
			System.out.println("예약자 아이디 : " + userId);
			System.out.println("예약날짜 : " + reserveDate);
			System.out.println("예약 시작시간 : " + StartTime);
			System.out.println("예약 종료시간 : " + EndTime);
			System.out.println("예약한 스터디룸 : " + roomCode + "-" + seat);
			
			//받아온 정보를 VO객체에 저장
			libraryReserveVO vo = new libraryReserveVO(roomCode, userId, reserveDate, StartTime, EndTime);
			vo.setReserveSeat(seat);
			
			//vo를 service로 넘겨서 비즈니스 로직 처리
			int result = roomReserveService.reserveStudyRoom(vo);		
			System.out.println(result);
			
			if(result == 1) {
				response.setStatus(response.SC_OK);
				out.write("OK");
			}else {
				response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
				out.write("FAIL");
			}
			   out.flush();
			   out.close();  
			
			return;
			
			
		//스터디룸 예약 수정 진행
		}else if(action.equals("/studyRoomUpdate")) {
			
			System.out.println("studyRoomUpdate호출됨===============");
			
			//예약정보를 받아오기
			String userId = request.getParameter("userID"); 
			String reserveNum = request.getParameter("reserveNum");
			
			String selectedDate = request.getParameter("reserveDate"); //예약날짜
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			java.util.Date utilDate = null; //utilDate는 java.util.Date형
			Date reserveDate = null;		//sqlDate는 java.sql.Date형
			String reserveNotice = request.getParameter("reserveNotice");
			
			try {
				utilDate = dateFormat.parse(selectedDate);
				reserveDate = new Date(utilDate.getTime()); //java.util.Date형을 java.sql.Date형으로 변환
			} catch (Exception e) {
				e.printStackTrace();
			}
								
			int StartTime = Integer.parseInt(request.getParameter("StartTime").split(":")[0]); //예약 시작시간
			int EndTime = Integer.parseInt(request.getParameter("EndTime").split(":")[0]); //예약 종료시간
			String roomCode = request.getParameter("roomCode"); //선택한 스터디룸
			int seat = Integer.parseInt(request.getParameter("seat")); //선택한 좌석
			
			System.out.println("예약자 아이디 : " + userId);
			System.out.println("예약 수정날짜 : " + reserveDate);
			System.out.println("예약 수정시간 : " + StartTime + " ~ " + EndTime);
			System.out.println("예약 수정 스터디룸 : " + roomCode + "-" + seat);
			System.out.println("예약번호 : " + reserveNum);
			System.out.println("관리자 메모 : " + reserveNotice);
								
			//받아온 정보를 VO객체에 저장
			libraryReserveVO vo = new libraryReserveVO(roomCode, userId, reserveDate, StartTime, EndTime);
			vo.setReserveSeat(seat);
			vo.setReserveNum(reserveNum);
			vo.setReserveNotice(reserveNotice);
			
			//vo를 service로 넘겨서 비즈니스 로직 처리
			roomReserveService.updateStudyRoom(vo);
			
			//예약완료 후 예약내역 페이지로 이동
			nextPage = "/reserve/reserveCheck";
		
			
		//미팅룸 예약 수정 진행	
		}else if(action.equals("/meetingRoomUpdate")) {
			
			System.out.println("meetingRoomUpdate호출됨===============");
			
			//예약정보를 받아오기
			String userId = request.getParameter("userID"); 
			String reserveNum = request.getParameter("reserveNum");
			
			String selectedDate = request.getParameter("reserveDate"); //예약날짜
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			java.util.Date utilDate = null; //utilDate는 java.util.Date형
			Date reserveDate = null;		//sqlDate는 java.sql.Date형
			String reserveNotice = request.getParameter("reserveNotice");
			
			try {
				utilDate = dateFormat.parse(selectedDate);
				reserveDate = new Date(utilDate.getTime()); //java.util.Date형을 java.sql.Date형으로 변환
			} catch (Exception e) {
				e.printStackTrace();
			}
								
			int StartTime = Integer.parseInt(request.getParameter("StartTime").split(":")[0]); //예약 시작시간
			int EndTime = Integer.parseInt(request.getParameter("EndTime").split(":")[0]); //예약 종료시간
			String roomCode = request.getParameter("roomCode"); //선택한 미팅룸
			
			System.out.println("예약자 아이디 : " + userId);
			System.out.println("예약 수정날짜 : " + reserveDate);
			System.out.println("예약 수정시간 : " + StartTime + " ~ " + EndTime);
			System.out.println("예약 수정 미팅룸 : " + roomCode);
			System.out.println("예약번호 : " + reserveNum);
			System.out.println("관리자 메모 : " + reserveNotice);
					
			//받아온 정보를 VO객체에 저장
			libraryReserveVO vo = new libraryReserveVO(roomCode, userId, reserveDate, StartTime, EndTime);
			vo.setReserveNum(reserveNum);
			vo.setReserveNotice(reserveNotice);
			
			//vo를 service로 넘겨서 비즈니스 로직 처리
			roomReserveService.updateMeetingRoom(vo);
			
			//예약완료 후 예약내역 페이지로 이동
			nextPage = "/reserve/reserveCheck";
			
		//전체 시설 예약 내역 조회
		}else if(action.equals("/allReservedList")){
			
			System.out.println("allReservedList호출됨===============");
			
			List reservedList = roomReserveService.allReservedList();
			
			   System.out.println("조회한 예약 내역 : " + reservedList);		   
			   
			   //받은 리스트를 JSON형식으로 변환하기 (jackson 라이브러리 사용 - ObjectMapper)
			   ObjectMapper objectMapper = new ObjectMapper(); 
			   //날짜 포맷 지정
			   objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")); 
			   String json = objectMapper.writeValueAsString(reservedList);
			    
			   System.out.println("JSON형식으로 변환된 데이터 : " + json);
			   		   
			   //JSON형식으로 변환된 데이터를 ajax로 리턴하기
			   response.setContentType("application/json; charset=utf-8");

			   //ajax로 반환
			   out.write(json); //ajax로 JSON형식으로 변환된 데이터 리턴
			   out.flush();
			   out.close();    		
			
			   return;
						
		}else if(action.equals("/checkReserve")) {
			
			System.out.println("checkReserve호출됨===============");
			
			Map<String, Object> reserveMap = new HashMap();
			
			//값 얻기
			String userID = request.getParameter("userID");
				
			//예약번호가 있을 경우 map에 저장
			String reserveNumParam = request.getParameter("reserveNum");
			if(reserveNumParam != null && !reserveNumParam.trim().isEmpty()) {
			    reserveMap.put("reserveNum", reserveNumParam);
			}
			
			String reserveDateStr = request.getParameter("reserveDate");					
		    Date reserveDate = null;		    
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date parsedDate = sdf.parse(reserveDateStr);
				reserveDate = new Date(parsedDate.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}		
			
			int StartTime = Integer.parseInt(request.getParameter("StartTime").split(":")[0]); 
			int EndTime = Integer.parseInt(request.getParameter("EndTime").split(":")[0]);
						
			reserveMap.put("userID", userID);
			reserveMap.put("reserveDate", reserveDate);
			reserveMap.put("StartTime", StartTime);
			reserveMap.put("EndTime", EndTime);			
			
			//동일 날짜와 시간대에 예약 건이 있는지 체크
			boolean result = roomReserveService.checkReserve(reserveMap);
			
			System.out.println("result :" + result);
			
			response.setContentType("application/json;charset=utf-8");
			out.print("{\"isReserved\": " + result + "}");
			out.flush();
			out.close();  		
		}
		
	
		if(nextPage != null) {			
			System.out.println("포워딩 실행 : " + nextPage);
			request.getRequestDispatcher(nextPage).forward(request, response);			
		}else {
			System.out.println("nextPage가 null입니다.");
		}
		
	}//doHandle
	
}
