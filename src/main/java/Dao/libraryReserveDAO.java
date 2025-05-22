package Dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import Vo.libraryReserveVO;

//시설 예약 관련 DB 작업할 DAO
public class libraryReserveDAO {
	
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
		
	//DB에서 조회한 시설을 저장할 VO 객체 생성
	libraryReserveVO libraryReserveVO = null;
	
	//실시간 예약가능한 미팅룸 조회 메소드
	public List MeetingRoomList(String date, int start, int end) {
		
		//DB에서 조회한 값을 저장할 List 객체 생성
		List roomList = new ArrayList();
		
		//DB에서 예약 가능한 미팅룸 조회 쿼리문
		String sql = "select distinct rr.reserve_room as room_code, "
				   + "case when count(reserve_num) = 0 then 1 else 0 end as status "
				   + "from(select 'meetingA' as reserve_room union "
				   + "select 'meetingB' union select 'meetingC') as rr "
				   + "left join room_reserve r "
				   + "on rr.reserve_room = r.reserve_room "
				   + "and r.reserve_date = ? "
				   + "and r.reserve_start < ? "
				   + "and r.reserve_end > ? "
				   + "group by rr.reserve_room";
		try {
			con = DbcpBean.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, date); //예약날짜
			pstmt.setInt(2, end); 	  //예약종료시간
			pstmt.setInt(3, start);   //예약시작시간
			
			//조회된 결과를 ResultSet 객체에 저장
			rs = pstmt.executeQuery();
			
			while(rs.next()) { //조회된 결과가 있을 경우 				
				int status = rs.getInt("status");				
				if(status == 1) { //예약상태가 '1'인 경우 (예약 가능한 경우)		
					
					String roomCode = rs.getString("room_code");
					String roomName = "";
					
					switch (roomCode) {
					    case "meetingA":
					    	roomName = "미팅룸A";
					        break;
					    case "meetingB":
					    	roomName = "미팅룸B";
					        break;
					    case "meetingC":
					    	roomName = "미팅룸C";
					        break;
					    default:
					    	roomName = "알수없음";
					        break;
					}
								
					libraryReserveVO = new libraryReserveVO();
					libraryReserveVO.setReserveRoom(rs.getString("room_code"));	
					libraryReserveVO.setRoomName(roomName);	
					
					roomList.add(libraryReserveVO);
					
				}
			}				
		} catch (Exception e) {
			System.err.println("실시간 예약가능한 미팅룸 조회 실패" + e);
			e.printStackTrace();
		}finally {
			DbcpBean.close(con, pstmt, rs);			
		}		
		return roomList; //조회된 List를 Service로 반환
		
	} //selectRoomList 메소드 끝
	
	
	//미팅룸 예약 메소드
	public int reserveMeetingRoom(libraryReserveVO vo) {
		
		System.out.println("reserveMeetingRoom DAO 호출됨===================");
		
		//예약번호 생성 (MA-0-0520-1314 형태 : 미팅룸코드-(좌석번호)-예약날짜-예약시작시간종료시간)
		String numRoom = vo.getReserveRoom(); //예약한 미팅룸 코드 (예: meetingA)
		String numDate = vo.getReserveDate().toString(); //예약날짜 (예: 2025-05-20)
		
		//미팅룸 코드에서 'MA' 추출
		char first = numRoom.charAt(0); //첫번째 문자
		char last = numRoom.charAt(numRoom.length()-1); //마지막 문자
		numRoom = (first + "" + last).toUpperCase(); //첫번째 문자와 마지막 문자를 합쳐서 'MA'로 변경
		
		//예약날짜에서 '0520'만 추출
		String[] datePart = numDate.split("-");
		numDate = datePart[1] + datePart[2];
		
		//최종 예약번호
		String reserveNum = numRoom + "-0-" + numDate + "-" + vo.getReserveStart() + vo.getReserveEnd();
		
		//DB에 예약정보 저장 쿼리문
		String sql = "insert into room_reserve(reserve_num, reserve_room, reserve_id, reserve_name, reserve_date, reserve_start, reserve_end, reserve_time) " +
					 "values(?, ?, ?, (select name from member where id = ? ), ?, ?, ?, sysdate())";
		int result = 0;
		
		try {
			con = DbcpBean.getConnection();
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, reserveNum); //예약번호
			pstmt.setString(2, vo.getReserveRoom()); //예약한 미팅룸 코드
			pstmt.setString(3, vo.getReserveId()); //예약자 아이디
			pstmt.setString(4, vo.getReserveId()); //예약자 아이디
			pstmt.setDate(5, vo.getReserveDate()); //예약날짜
			pstmt.setInt(6, vo.getReserveStart()); //예약시작시간
			pstmt.setInt(7, vo.getReserveEnd()); //예약종료시간
			
			//쿼리문 실행
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.err.println("미팅룸 예약 실패" + e);
			e.printStackTrace();		
			result = 0;
		}finally {
			DbcpBean.close(con, pstmt); //DB 연결 해제
		}
		
		return result;
		
	} //reserveMeetingRoom 메소드 끝
	
	
	//예약정보 조회 메소드
	public List<libraryReserveVO> selectReserveList(String userId) {
		
		//예약정보를 저장할 List 객체 생성
		List<libraryReserveVO> reserveList = new ArrayList<libraryReserveVO>();
		
		String sql = "select *, "
		           + "case when now() between date_add(reserve_date, interval reserve_start hour) "
		           + "and date_add(reserve_date, interval reserve_end hour) then 0 "
		           + "when now() < date_add(reserve_date, interval reserve_start hour) then 1 "
		           + "else 2 end as status "
		           + "from room_reserve "
		           + "where reserve_id = ? "
		           + "order by status asc, "
		           + "reserve_date asc, reserve_start asc limit 10";
		
		
		try {
			con = DbcpBean.getConnection();
			
			//첫번째 쿼리문 실행
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, userId); //예약자 아이디
			
			//조회된 결과를 ResultSet 객체에 저장
			rs = pstmt.executeQuery();
			
			while(rs.next()) { //조회된 결과가 있을 경우 
				
				String roomCode = rs.getString("reserve_room");
				String roomName = "";
				
				switch (roomCode) {
				    case "meetingA": roomName = "미팅룸A"; break;
				    case "meetingB": roomName = "미팅룸B"; break;
				    case "meetingC": roomName = "미팅룸C"; break;
				    case "studyA":   roomName = "스터디룸A"; break;
				    case "studyB":   roomName = "스터디룸B"; break;
				    case "studyC":   roomName = "스터디룸C"; break;
				    default:         roomName = "알수없음"; break;
				}
				
				//DB에서 조회한 값을 VO 객체에 저장
				libraryReserveVO reserveVO = new libraryReserveVO(rs.getString("reserve_num"), roomCode,  
																  rs.getString("reserve_id"), rs.getString("reserve_name"), rs.getDate("reserve_date"), 
																  rs.getInt("reserve_start"), rs.getInt("reserve_end"), rs.getTimestamp("reserve_time"), rs.getInt("reserve_seat"));
				reserveVO.setRoomName(roomName);
						
				//예약정보를 List에 추가
				reserveList.add(reserveVO);
			}							
		} catch (Exception e) {
			System.err.println("예약정보 조회 실패" + e);
			e.printStackTrace();
		}finally {
			DbcpBean.close(con, pstmt, rs);			
		}				
		return reserveList; //조회된 List를 Service로 반환
		
	}//selectReserveList 메소드 끝


	//예약 정보를 DB에서 삭제하는 메소드
	public void deleteReserve(String reserve_id, String reserve_num) {
		
		String sql = "delete from room_reserve where reserve_id = ? and reserve_num = ?";
		
		try {
			con = DbcpBean.getConnection();
			
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, reserve_id);
			pstmt.setString(2, reserve_num);
			
			pstmt.executeUpdate();
	
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DbcpBean.close(con, pstmt);
		}		
		
	}//deleteReserve 메소드 끝


	public List studySeatList(String date, int start, int end, String studyRoom) {
		
		//DB에서 조회한 값을 저장할 List 객체 생성
		List seatList = new ArrayList();
		
		//DB에서 실시간 좌석 현황 조회 쿼리문
		String sql = "select reserve_seat "
				   + "from room_reserve "
				   + "where reserve_date = ? "
				   + "and reserve_start < ? "
				   + "and reserve_end > ? "
				   + "and reserve_room like ?";
		
		try {
			con = DbcpBean.getConnection();			
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, date); 	   //예약날짜
			pstmt.setInt(2, end); 		   //예약종료시간
			pstmt.setInt(3, start); 	   //예약시작시간
			pstmt.setString(4, studyRoom); //선택한 스터디룸
			
			//조회된 결과를 ResultSet 객체에 저장
			rs = pstmt.executeQuery();
			
			while(rs.next()) { //조회된 결과가 있을 경우 				
				
				//DB에서 조회한 값을 VO 객체에 저장
				libraryReserveVO = new libraryReserveVO();
				libraryReserveVO.setReserveSeat(rs.getInt("reserve_seat"));
						
				//vo 객체에 저장된 값을 List에 추가
				seatList.add(libraryReserveVO);				
			}					
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DbcpBean.close(con, pstmt, rs);
		}
		return seatList;
	}//studySeatList 메소드 끝


	//스터디룸 예약 메소드
	public int reserveStudyRoom(libraryReserveVO vo) {
		
		System.out.println("reserveStudyRoom DAO 호출됨===================");
		
		//예약번호 생성 (SA-13-0520-1314 형태 : 스터디룸코드-좌석번호-예약날짜-예약시작시간종료시간)
		String numRoom = vo.getReserveRoom(); //예약한 미팅룸 코드 (예: meetingA)
		String numDate = vo.getReserveDate().toString(); //예약날짜 (예: 2025-05-20)
		
		//스터디룸 코드에서 'SA' 추출
		char first = numRoom.charAt(0); //첫번째 문자
		char last = numRoom.charAt(numRoom.length()-1); //마지막 문자
		//첫번째 문자와 마지막 문자를 합쳐서 'SA'로 변경 후 뒤에 좌석번호 붙임 'SA13'
		numRoom = (first + "" + last).toUpperCase(); 
		
		//예약날짜에서 '0520'만 추출
		String[] datePart = numDate.split("-");
		numDate = datePart[1] + datePart[2]; 
		
		//최종 예약번호
		String reserveNum = numRoom + "-" + vo.getReserveSeat() + "-" + numDate + "-" + vo.getReserveStart() + vo.getReserveEnd();
		
		//DB에 예약정보 저장 쿼리문
		String sql = "insert into room_reserve(reserve_num, reserve_room, reserve_id, reserve_name, reserve_date, reserve_start, reserve_end, reserve_time, reserve_seat) " +
					 "values(?, ?, ?, (select name from member where id = ? ), ?, ?, ?, sysdate(), ?)";
		int result = 0;
		
		try {
			con = DbcpBean.getConnection();
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, reserveNum); //예약번호
			pstmt.setString(2, vo.getReserveRoom()); //예약한 스터디룸 코드
			pstmt.setString(3, vo.getReserveId()); //예약자 아이디
			pstmt.setString(4, vo.getReserveId()); //예약자 아이디
			pstmt.setDate(5, vo.getReserveDate()); //예약날짜
			pstmt.setInt(6, vo.getReserveStart()); //예약시작시간
			pstmt.setInt(7, vo.getReserveEnd()); //예약종료시간
			pstmt.setInt(8, vo.getReserveSeat()); //예약좌석
			
			//쿼리문 실행
			result = pstmt.executeUpdate();		
			
		} catch (Exception e) {
			System.err.println("스터디룸 예약 실패" + e);
			e.printStackTrace();			
			result = 0;
		}finally {
			DbcpBean.close(con, pstmt); //DB 연결 해제
		}				
		return result;		
	}//reserveStudyRoom 메소드 끝


	
	//스터디룸 예약 정보 수정 메소드
	public void updateStudyRoom(libraryReserveVO vo) {
		
		System.out.println("updateStudyRoom DAO 호출됨===================");

		//예약번호 생성 (SA-13-0520-1314 형태 : 스터디룸코드-좌석번호-예약날짜-예약시작시간종료시간)
		String numRoom = vo.getReserveRoom(); //예약한 미팅룸 코드 (예: meetingA)
		String numDate = vo.getReserveDate().toString(); //예약날짜 (예: 2025-05-20)
		
		//스터디룸 코드에서 'SA' 추출
		char first = numRoom.charAt(0); //첫번째 문자
		char last = numRoom.charAt(numRoom.length()-1); //마지막 문자
		//첫번째 문자와 마지막 문자를 합쳐서 'SA'로 변경 후 뒤에 좌석번호 붙임 'SA13'
		numRoom = (first + "" + last).toUpperCase(); 
		
		//예약날짜에서 '0520'만 추출
		String[] datePart = numDate.split("-");
		numDate = datePart[1] + datePart[2]; 
		
		//최종 예약번호
		String reserveNum = numRoom + "-" + vo.getReserveSeat() + "-" + numDate + "-" + vo.getReserveStart() + vo.getReserveEnd();
				
		
		String sql = "update room_reserve "
				   + "set reserve_num = ?, reserve_room = ?, reserve_seat = ?, reserve_date = ?, reserve_start = ?, reserve_end = ?, reserve_time = sysdate(), reserve_notice = ? "
				   + "where reserve_id = ? and reserve_num = ?";
		
		try {
			con = DbcpBean.getConnection();
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, reserveNum); 		   //예약 번호
			pstmt.setString(2, vo.getReserveRoom());   //예약 스터디룸
			pstmt.setInt(3, vo.getReserveSeat());      //예약 좌석번호
			pstmt.setDate(4, vo.getReserveDate());     //예약 날짜
			pstmt.setInt(5, vo.getReserveStart());     //예약 시작시간
			pstmt.setInt(6, vo.getReserveEnd());       //예약 종료시간
			pstmt.setString(7, vo.getReserveNotice()); //관리자메모
			pstmt.setString(8, vo.getReserveId());	   //예약 수정 요청한 아이디
			pstmt.setString(9, vo.getReserveNum());    //예약 수정 요청한 예약번호
			
			//쿼리문 실행
			pstmt.executeUpdate();			
		} catch (Exception e) {
			System.err.println("스터디룸 예약 수정 실패" + e);
			e.printStackTrace();	
		}finally {
			DbcpBean.close(con, pstmt);
		}			
	}//updateStudyRoom 메소드 끝


	public void updateMeetingRoom(libraryReserveVO vo) {		
		System.out.println("updateMeetingRoom DAO 호출됨===================");

		//예약번호 생성 (SA-13-0520-1314 형태 : 미팅룸코드-좌석번호-예약날짜-예약시작시간종료시간)
		String numRoom = vo.getReserveRoom(); //예약한 미팅룸 코드 (예: meetingA)
		String numDate = vo.getReserveDate().toString(); //예약날짜 (예: 2025-05-20)
		
		//스터디룸 코드에서 'SA' 추출
		char first = numRoom.charAt(0); //첫번째 문자
		char last = numRoom.charAt(numRoom.length()-1); //마지막 문자
		//첫번째 문자와 마지막 문자를 합쳐서 'SA'로 변경 후 뒤에 좌석번호 붙임 'SA13'
		numRoom = (first + "" + last).toUpperCase(); 
		
		//예약날짜에서 '0520'만 추출
		String[] datePart = numDate.split("-");
		numDate = datePart[1] + datePart[2]; 
		
		//최종 예약번호
		String reserveNum = numRoom + "-" + vo.getReserveSeat() + "-" + numDate + "-" + vo.getReserveStart() + vo.getReserveEnd();
				
		
		String sql = "update room_reserve "
				   + "set reserve_num = ?, reserve_room = ?, reserve_date = ?, reserve_start = ?, reserve_end = ?, reserve_time = sysdate(), reserve_notice = ? "
				   + "where reserve_id = ? and reserve_num = ?";
		
		try {
			con = DbcpBean.getConnection();
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, reserveNum); 		    //예약 번호
			pstmt.setString(2, vo.getReserveRoom());    //예약 스터디룸
			pstmt.setDate(3, vo.getReserveDate());      //예약 날짜
			pstmt.setInt(4, vo.getReserveStart());      //예약 시작시간
			pstmt.setInt(5, vo.getReserveEnd());        //예약 종료시간
			pstmt.setString(6, vo.getReserveNotice());  //관리자메모
			pstmt.setString(7, vo.getReserveId());	 	//예약 수정 요청한 아이디
			pstmt.setString(8, vo.getReserveNum());     //예약 수정 요청한 예약번호
			
			//쿼리문 실행
			pstmt.executeUpdate();			
		} catch (Exception e) {
			System.err.println("미팅룸 예약 수정 실패" + e);
			e.printStackTrace();	
		}finally {
			DbcpBean.close(con, pstmt);
		}					
	} //updateMeetingRoom 메소드 끝


	//DB에서 전체 시설 예약 내역 조회하는 메소드
	public List allReservedList() {
		
		System.out.println("allReservedList DAO 호출됨===================");
		
		// 0:이용중, 1:이용전, 2:이용완료
		String sql = "select r.*, m.tel as tel, "
		           + "case when now() between date_add(r.reserve_date, interval r.reserve_start hour) "
		           + "and date_add(r.reserve_date, interval r.reserve_end hour) then 0 "
		           + "when now() < date_add(r.reserve_date, interval r.reserve_start hour) then 1 "
		           + "else 2 end as status "
		           + "from room_reserve r join member m "
		           + "on r.reserve_id = m.id "
		           + "order by status asc, "
		           + "reserve_date asc, reserve_start asc";
		
		List reservedList = new ArrayList();
		
		try {
			con = DbcpBean.getConnection();
			pstmt = con.prepareStatement(sql);
			
			//쿼리문 실행
			pstmt.executeQuery(sql);
			
			//조회된 결과를 ResultSet 객체에 저장
			rs = pstmt.executeQuery();
			
			while(rs.next()) { //조회된 결과가 있을 경우 				
				
				//DB에서 조회한 값을 VO 객체에 저장
				libraryReserveVO = new libraryReserveVO(rs.getString("reserve_num"), rs.getString("reserve_room"), 
						           rs.getString("reserve_id"), rs.getString("reserve_name"), rs.getDate("reserve_date"), 
						           rs.getInt("reserve_start"), rs.getInt("reserve_end"), rs.getTimestamp("reserve_time"), rs.getInt("reserve_seat"));		
			
				libraryReserveVO.setTel(rs.getString("tel"));
				libraryReserveVO.setReserveNotice(rs.getString("reserve_notice"));
				
				reservedList.add(libraryReserveVO);
				
			}						
		} catch (Exception e) {
			System.err.println("전체 예약 내역 조회 실패" + e);
			e.printStackTrace();	
		}finally {
			DbcpBean.close(con, pstmt, rs);
		}					
		
		return reservedList;
	}//allReservedList


	//사용자가 선택한 날짜와 시간대에 예약 건이 있는지 DB에 조회 (예약중복방지)
	public boolean checkReserve(Map<String, Object> reserveMap) {
		
		System.out.println("checkReserve DAO 호출됨===================");
		
		String reserveNum = (String) reserveMap.get("reserveNum");
		boolean isUpdate = reserveNum != null && !reserveNum.trim().equals("");
		
		boolean result = false;
		
		String sql = "";		
		
		System.out.println("isUpdate : " + isUpdate);
		System.out.println("endTime : " + (int) reserveMap.get("EndTime"));
		System.out.println("startTime : " + (int) reserveMap.get("StartTime"));
		System.out.println("userID : " + (String) reserveMap.get("userID"));
		System.out.println("reserveDate : " + (Date) reserveMap.get("reserveDate"));
		
		if(isUpdate) { //예약 수정일 경우?
			System.out.println("첫번째 sql 실행");
			sql = "select * from room_reserve "
				+ "where reserve_id = ? "
				+ "and reserve_date = ? "
				+ "and (reserve_start < ? and reserve_end > ?) "
				+ "and reserve_num != ?";
			
		}else { //신규 예약일 경우?
			System.out.println("두번째 sql 실행");
			sql = "select * from room_reserve "
			    + "where reserve_id = ? "
			    + "and reserve_date = ? "
			    + "and (reserve_start < ?"
			    + " and reserve_end > ?)";				
		}
		
		try {
			con = DbcpBean.getConnection();
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, (String) reserveMap.get("userID"));
			pstmt.setDate(2, (Date) reserveMap.get("reserveDate"));
			pstmt.setInt(3, (int) reserveMap.get("EndTime"));
			pstmt.setInt(4, (int) reserveMap.get("StartTime"));
			
			if(isUpdate) {
				pstmt.setString(5, (String) reserveMap.get("reserveNum"));
			}
					
			rs = pstmt.executeQuery();
			result = rs.next();
						 
			return 	result;		
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DbcpBean.close(con, pstmt, rs);
		}
		
		return result;
		
	} //checkReserve

	


}
