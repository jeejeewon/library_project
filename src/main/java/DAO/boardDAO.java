package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.catalina.Context;

import DAO.DbcpBean;
import VO.boardVO;

public class boardDAO {
	Connection con = null; // 데이터베이스 연결 통로
	PreparedStatement pstmt = null; // SQL 명령 실행 도구
	ResultSet rs = null; // 데이터 조회 결과 상자
	
	//기본생성자
	public boardDAO() {}
	

	// 데이터베이스의 board테이블에 저장된 정보를 조회해서 읽어오는 메소드
	public Vector<boardVO> getAllBoardList() {

		// 조회된 정보들을 담을 배열vector 생성
		Vector<boardVO> vector = new Vector();
		// 조회된 정보를 임시로 담을 상자vo 준비
		boardVO vo = null;

		try {
			con = DbcpBean.getConnection();
			String sql = "select * from board";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {

				// 현재 조회된 줄의 게시판 정보를 담을 새 상자 만들기
				vo = new boardVO(rs.getInt("board_id"), 
								rs.getInt("category"), 
								rs.getString("title"),
								rs.getString("content"), 
								rs.getString("user_id"), 
								rs.getInt("book_no"), 
								rs.getString("file"),
								rs.getString("banner_img"), 
								rs.getDate("date"), 
								rs.getInt("views"), 
								rs.getBoolean("secret"),
								rs.getString("reply"));
				vector.add(vo);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbcpBean.close(con, pstmt, rs);
		}

		return vector;
	}
	
	
	
	public Vector<boardVO> getNoticeList() {

		// 조회된 정보들을 담을 배열vector 생성
		Vector<boardVO> vector = new Vector();
		// 조회된 정보를 임시로 담을 상자vo 준비
		boardVO vo = null;

		try {
			con = DbcpBean.getConnection();
			String sql = "select * from board where category = 0 "
					   + "ORDER BY date desc";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {

				// 현재 조회된 줄의 게시판 정보를 담을 새 상자 만들기
				vo = new boardVO(rs.getInt("board_id"), 
								rs.getInt("category"), 
								rs.getString("title"),
								rs.getString("content"), 
								rs.getString("user_id"), 
								rs.getInt("book_no"), 
								rs.getString("file"),
								rs.getString("banner_img"), 
								rs.getDate("date"), 
								rs.getInt("views"), 
								rs.getBoolean("secret"),
								rs.getString("reply"));
				vector.add(vo);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbcpBean.close(con, pstmt, rs);
		}

		return vector;
	}

	

}
