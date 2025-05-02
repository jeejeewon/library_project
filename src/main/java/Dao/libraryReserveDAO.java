package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import Vo.libraryRoomVO;

//시설 예약 관련 DB 작업할 DAO
public class libraryReserveDAO {
	
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
		
	//DB에서 조회한 시설을 저장할 VO 객체 생성
	libraryRoomVO libraryRoomVO = new libraryRoomVO();

	public List selectRoomList(String date, int start, int end) {
		
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

}
