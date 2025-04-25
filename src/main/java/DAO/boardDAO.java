package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import VO.boardVO;

public class boardDAO {

	Connection con = null; //데이터베이스 연결 통로
	PreparedStatement pstmt = null; // SQL 명령 실행 도구 
	ResultSet rs = null; //데이터 조회 결과 상자 
	
	public boardDAO() {}
	
	
	//데이터베이스의 board테이블에 저장된 정보를 조회해서 읽어오는 메소드
	public Vector<boardVO> getAllBoardData(){
		
		//조회된 정보들을 담을 배열vector 생성
		Vector<boardVO> vector = new Vector<boardVO>();
		//조회된 정보를 임시로 담을 상자vo 준비
		boardVO vo = null;
		
		
		
		
		
		return null;
		
	}
}
