package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import Vo.libraryRoomVO;

//시설 예약 관련 DB 작업할 DAO
public class libraryReserveDAO {
	
	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	DataSource ds;
	
	private Connection getConnection() throws Exception {
		Connection con = null;
		Context init = new InitialContext();
		DataSource ds = (DataSource) init.lookup("java:comp/env/jdbc/jspbeginner");
		con = ds.getConnection();
		return con;
	}
	
	//DB에서 조회한 시설을 저장할 VO 객체 생성
	libraryRoomVO libraryRoomVO = new libraryRoomVO();

	public List selectRoomList(String date, String start, String end) {
		
		String sql = "select * from library_room where ";
		
		
		
		
		return null;
	}

}
