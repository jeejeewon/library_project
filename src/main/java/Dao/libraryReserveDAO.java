package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import Vo.libraryReserveVO;
import Vo.libraryRoomVO;

//시설 예약 관련 DB 작업할 DAO
public class libraryReserveDAO {
	
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
		
	//DB에서 조회한 시설을 저장할 VO 객체 생성
	libraryRoomVO libraryRoomVO = null;
	
	//실시간 예약가능한 미팅룸 조회 메소드
	public List MeetingRoomList(String date, int start, int end) {
		
		//DB에서 조회한 값을 저장할 List 객체 생성
		List roomList = new ArrayList();
		
		//DB에서 예약 가능한 미팅룸 조회 쿼리문
		String sql = "select r.room_code, r.room_name, " + //예약가능한 시설코드와 시설명 조회
				"CASE WHEN rr.reserve_num IS NULL THEN 1 ELSE 0 END AS status " + //가상컬럼으로 예약가능여부 조회
				"from library_room r " +
				"LEFT JOIN room_reserve rr " + //시설테이블과 예약테이블 조인
				"ON r.room_code = reserve_room " + //시설테이블의 시설코드와 예약테이블의 예약시설을 기준으로 조인
				"AND rr.reserve_date = ? " + //선택한 예약 날짜와 예약테이블의 예약날짜가 같고
				"AND rr.reserve_start < ? AND rr.reserve_end > ? " + //예약시작시간과 예약종료시간이 겹치지 않으며
				"WHERE rr.reserve_num IS NULL " + //예약테이블의 예약번호가 NULL인 경우만 조회
				"AND r.room_code LIKE 'meeting%'";	//그 중에서 미팅룸만 조회	
		try {
			con = DbcpBean.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, date); //예약날짜
			pstmt.setInt(2, end); //예약시작시간
			pstmt.setInt(3, start); //예약종료시간
			
			//조회된 결과를 ResultSet 객체에 저장
			rs = pstmt.executeQuery();
			
			while(rs.next()) { //조회된 결과가 있을 경우 
				
				//DB에서 조회한 값을 VO 객체에 저장
				libraryRoomVO = new libraryRoomVO(rs.getString("room_code"), rs.getString("room_name"));
				
				//vo 객체에 저장된 값을 List에 추가
				roomList.add(libraryRoomVO);
	
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
	public void reserveMeetingRoom(libraryReserveVO vo) {
		
		System.out.println("insertReserveRoom DAO 호출됨===================");
		
		//예약번호 생성 (MA-0520-1314 형태 : 미팅룸코드-예약날짜-예약시작시간종료시간)
		String num_room = vo.getReserve_room(); //예약한 미팅룸 코드 (예: meetingA)
		String num_date = vo.getReserve_date().toString(); //예약날짜 (예: 2025-05-20)
		
		//미팅룸 코드에서 'MA' 추출
		char first = num_room.charAt(0); //첫번째 문자
		char last = num_room.charAt(num_room.length()-1); //마지막 문자
		num_room = (first + "" + last).toUpperCase(); //첫번째 문자와 마지막 문자를 합쳐서 'MA'로 변경
		
		//예약날짜에서 '0520'만 추출
		String[] datePart = num_date.split("-");
		num_date = datePart[1] + datePart[2];
		
		//최종 예약번호
		String reserve_num = num_room + "-" + num_date + "-" + vo.getReserve_start() + vo.getReserve_end();
		
		//DB에 예약정보 저장 쿼리문
		String sql = "insert into room_reserve(reserve_num, reserve_room, reserve_id, reserve_name, reserve_date, reserve_start, reserve_end, reserve_time) " +
					 "values(?, ?, ?, (select name from member where id = ? ), ?, ?, ?, sysdate())";
		
		try {
			con = DbcpBean.getConnection();
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, reserve_num); //예약번호
			pstmt.setString(2, vo.getReserve_room()); //예약한 미팅룸 코드
			pstmt.setString(3, vo.getReserve_id()); //예약자 아이디
			pstmt.setString(4, vo.getReserve_id()); //예약자 아이디
			pstmt.setDate(5, vo.getReserve_date()); //예약날짜
			pstmt.setInt(6, vo.getReserve_start()); //예약시작시간
			pstmt.setInt(7, vo.getReserve_end()); //예약종료시간
			
			//쿼리문 실행
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.err.println("미팅룸 예약 실패" + e);
			e.printStackTrace();			
		}finally {
			DbcpBean.close(con, pstmt); //DB 연결 해제
		}		
		
	} //insertReserveRoom 메소드 끝
	
	//예약정보 조회 메소드
	public List<libraryReserveVO> selectReserveList(String userId) {
		
		//예약정보를 저장할 List 객체 생성
		List<libraryReserveVO> reserveList = new ArrayList<libraryReserveVO>();
		
		//예약정보 조회 쿼리문(미래예약정보)
		String sql1 = "select * from room_reserve where reserve_id = ? "
				   + "and reserve_date + interval reserve_start hour >= sysdate() "
				   + "order by reserve_date asc";
		
		//예약정보 조회 쿼리문(과거예약정보)
		String sql2 = "select * from room_reserve where reserve_id = ? "
					+ "and reserve_date + interval reserve_end hour < sysdate() "
					+ "order by reserve_date desc";
		
		//오늘날짜를 기준으로 미래/과거 예약정보를 구분하기 위해 오늘 날짜를 가져옴
		LocalDate today = LocalDate.now(); //오늘 날짜
		
		try {
			con = DbcpBean.getConnection();
			
			//첫번째 쿼리문 실행
			pstmt = con.prepareStatement(sql1);
			pstmt.setString(1, userId); //예약자 아이디
			
			//조회된 결과를 ResultSet 객체에 저장
			rs = pstmt.executeQuery();
			
			while(rs.next()) { //조회된 결과가 있을 경우 
				
				//DB에서 조회한 값을 VO 객체에 저장
				libraryReserveVO reserveVO = new libraryReserveVO(rs.getString("reserve_room"), rs.getDate("reserve_date"), rs.getInt("reserve_start"), rs.getInt("reserve_end"), rs.getTimestamp("reserve_time"));

				boolean isFuture = reserveVO.getReserve_date().toLocalDate().isAfter(today); //예약날짜가 오늘 날짜보다 미래인지 확인
				
				reserveVO.setIsFuture(isFuture); //예약날짜가 미래인지 여부를 VO 객체에 저장
						
				//예약정보를 List에 추가
				reserveList.add(reserveVO);
			}
			
			DbcpBean.close(rs);
			DbcpBean.close(pstmt);
			
			//두번째 쿼리문 실행
			pstmt = con.prepareStatement(sql2);
			pstmt.setString(1, userId); //예약자 아이디
			
			//조회된 결과를 ResultSet 객체에 저장
			rs = pstmt.executeQuery();
			
			while(rs.next()) { //조회된 결과가 있을 경우 
				
				//DB에서 조회한 값을 VO 객체에 저장
				libraryReserveVO reserveVO = new libraryReserveVO(rs.getString("reserve_room"), rs.getDate("reserve_date"), rs.getInt("reserve_start"), rs.getInt("reserve_end"), rs.getTimestamp("reserve_time"));
				
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

}
