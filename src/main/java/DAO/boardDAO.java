package DAO;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.catalina.Context;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

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
	
	
	// 데이터베이스의 board테이블의 카테고리0인 공지사항게시판의 글들만 조회해서 읽어오는 메소드
	public Vector<boardVO> getNoticeList() {
		Vector<boardVO> vector = new Vector();
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
	
	
	// 데이터베이스의 board테이블의 카테고리1인 문의게시판의 글들만 조회해서 읽어오는 메소드
	public Vector<boardVO> getquestionList() {
		Vector<boardVO> vector = new Vector();
		boardVO vo = null;

		try {
			con = DbcpBean.getConnection();
			String sql = "select * from board where category = 1 "
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

	
	//DB에 새 글을 추가하기 전에 DB에 저장된 가장 최신 글번호를 검색해서 제공하는 메소드
	public int getMaxBoardId() {
		try {
			con = DbcpBean.getConnection();
			//DB의 board테이블에서 가장 큰 board_id를 검색하는 SQL문
			String sql = "select max(board_id) from board";
			//SQL문을 실행하기 위한 PreparedStatement객체 생성
			pstmt = con.prepareStatement(sql);
			//SQL문을 실행하여 결과를 ResultSet객체에 저장
			rs = pstmt.executeQuery();
			//ResultSet객체에서 검색된 결과를 꺼내서 maxBoardId변수에 저장
			if(rs.next()) {//조회된 최신글번호가 검색되었다면?
				return (rs.getInt(1) + 1); //최신글번호에 1을 더해서 리턴 : 새로운 글 추가시 가장 큰 번호 +1을 한 번호를 사용하기 위해서
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbcpBean.close(con, pstmt, rs);//자원해제
		}
		return 0;
	}
	
	
	//DB에 새글을 추가 시키는 메소드
	public int insertBoard(boardVO vo) {
		
		int boardId = getMaxBoardId(); //DB에 새글을 추가하기 전에 DB에 저장된 가장 최신 글번호를 검색해서 제공하는 메소드 호출
		System.out.println("새 글을 등록할 시 사용할 글 번호 : " + boardId);
		
		try {
			//DB에 연결 하기 위한 Connection객체 생성
			con = DbcpBean.getConnection();
			
			
			// 3. boardVO객체에서 필요한 정보 꺼내기
			int category = vo.getCategory(); //게시글 카테고리번호 (0:공지사항, 1:문의게시판, 2:내 서평)
			String title = vo.getTitle(); //게시글 제목
			String content = vo.getContent(); //게시글 내용
			String userId = vo.getUserId(); //유저 ID
			int bookNo = vo.getBookNo(); //도서번호
			String file = vo.getFile(); //첨부파일명
			String bannerImg = vo.getBannerImg(); //배너파일명
			Date date = vo.getDate(); //게시글 작성일
//			int views = vo.getViews(); //게시글 조회수
			boolean secret = vo.getSecret(); //게시글 공개 여부 (false :공개 , true:비공개)

			
			//DB에 새글을 추가하기 위한 SQL문
			String sql = "insert into board (board_id, category, title, content, user_id, book_no, file, banner_img, date, secret) "
			           + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			//views는 테이블생성시 DEFAULT 0 으로 설정되어 있으므로 제외하였음.
			
			//SQL문을 실행하기 위한 PreparedStatement객체 생성
			pstmt = con.prepareStatement(sql);
		
			pstmt.setInt(1, boardId); //첫번째 ?에 새 글 번호
			pstmt.setInt(2, category); //두번째 ?에 새 글 번호
			pstmt.setString(3, title); //세번째 ?에 새 글 제목
			pstmt.setString(4, content); //네번째 ?에 새 글 내용
			pstmt.setString(5, userId); //다섯번째 ?에 새 글 작성자 ID
			pstmt.setInt(6, bookNo); //여섯번째 ?에 새 글 도서번호
			pstmt.setString(7, file); //일곱번째 ?에 새 글 첨부파일명
			pstmt.setString(8, bannerImg); //여덟번째 ?에 새 글 배너파일명
			pstmt.setDate(9, date); //아홉번째 ?에 현재 날짜
			pstmt.setBoolean(10, secret); //열번째 ?에 비공개 여부
			
			// 6. SQL 실행: INSERT, UPDATE, DELETE 문장은 executeUpdate() 메소드 사용
			//   - executeUpdate()는 실행 결과로 영향을 받은 행(row)의 수를 반환 (여기서는 1이 반환되어야 정상)
			pstmt.executeUpdate(); 

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbcpBean.close(con, pstmt); //자원해제
		}
		return boardId; //추가된 글의 번호를 리턴
	}
	
	
	

	
	

	

}
