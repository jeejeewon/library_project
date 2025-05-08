package DAO;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	
	
	
	
	
	//뷰 수를 +1 증가시키는 메소드
	public void increaseViewCount(int boardId) {
		try {
			con = DbcpBean.getConnection();
			String sql = "UPDATE board SET views = views + 1 WHERE board_id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, boardId); // ?에 글 번호 설정
			pstmt.executeUpdate(); //SQL 실행
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			
		}
			DbcpBean.close(con, pstmt);
		}
	
	//주어진 글번호(boardId)에 해당하는 글을 DB에서 조회하여 boardVO객체에 담아서 리턴하는 메소드
	public boardVO selectBoard(int boardId) {
		
		//조회된 글 정보를 담을 boardVO객체 생성
		boardVO boardVO = null;
		
		try {
			//1. DB 연결
			con = DbcpBean.getConnection();
			//2. SQL문 작성
			String sql = "SELECT * FROM board WHERE board_id = ?";
			System.out.println("실행될 SQL (글 상세 조회) : " + sql.replace("?", String.valueOf(boardId)));
			//3. PreparedStatement객체 생성 및 파라미터 설정
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, boardId); //첫번째 ?에 글번호 설정
			//4. SQL 실행 및 결과 받기
			rs = pstmt.executeQuery();
			//5. ResultSet처리 : 조회된 결과가 있다면...
			if(rs.next()) {
				//현재 조회된 줄의 게시판 정보를 담을 새 상자 만들기
				boardVO = new boardVO(rs.getInt("board_id"), 
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
				System.out.println("글 번호 :" + boardId + " 상세 정보 조회 성공.");
			}else {
				//조회된 결과가 없다면?
				System.out.println("글 번호 :" + boardId + " 상세 정보 조회 실패.");
			}
		} catch (SQLException e) {
			System.err.println("오류 : selectBoard() 메소드 실행중 SQL 오류 : " + e.getMessage());
			e.printStackTrace();
		}
		
		return boardVO;
	}
	
	
	
	// 현재 상세페이지의 바로 앞 글 번호를 조회하여 리턴하는 메소드
	public int getPreBoardId(int currentBoardId, int category) {
	    int boardId = 0;
	    
	    try {
	        // DB 연결
	        con = DbcpBean.getConnection();
	        // SQL문 작성: 현재 글보다 작은 번호 중 가장 큰 것 1개
	        String sql = "SELECT board_id FROM board WHERE board_id < ? AND category = ? ORDER BY board_id DESC LIMIT 1";
	        // PreparedStatement 생성 및 파라미터 설정
	        pstmt = con.prepareStatement(sql);
	        pstmt.setInt(1, currentBoardId); // 현재 글 번호 전달
	        pstmt.setInt(2, category); // 카테고리 번호 전달
	        // SQL 실행 및 결과 받기
	        rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            boardId = rs.getInt("board_id"); // 결과 있으면 board_id 꺼내기
	        }
	        
	    } catch (SQLException e) {
	        System.err.println("오류 : getPreBoardId() 메소드 실행 중 오류 : " + e.getMessage());
	        e.printStackTrace();
	    } finally {
	        // 자원 반납
	        DbcpBean.close(con, pstmt, rs);
	    }

	    return boardId; // 결과 없으면 0 반환
	}

	// 현재 상세페이지의 바로 뒤 글 번호를 조회하여 리턴하는 메소드
	public int getNextBoardId(int currentBoardId, int category) {
	    int boardId = 0;
	    
	    try {
	        // DB 연결
	        con = DbcpBean.getConnection();
	        // SQL문 작성: 현재 글보다 작은 번호 중 가장 큰 것 1개
	        String sql = "SELECT board_id FROM board WHERE board_id > ? AND category = ? ORDER BY board_id ASC LIMIT 1";
	        // PreparedStatement 생성 및 파라미터 설정
	        pstmt = con.prepareStatement(sql);
	        pstmt.setInt(1, currentBoardId); // 현재 글 번호 전달
	        pstmt.setInt(2, category); // 카테고리 번호 전달
	        // SQL 실행 및 결과 받기
	        rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            boardId = rs.getInt("board_id"); // 결과 있으면 board_id 꺼내기
	        }
	        
	    } catch (SQLException e) {
	        System.err.println("오류 : getNextBoardId() 메소드 실행 중 오류 : " + e.getMessage());
	        e.printStackTrace();
	    } finally {
	        // 자원 반납
	        DbcpBean.close(con, pstmt, rs);
	    }

	    return boardId; // 결과 없으면 0 반환
	}
	
	//게시글 수정
	public void updateBoard(boardVO modVO) {
		
		
		
		// boardVO객체에서 수정할 정보 꺼내기
		int boardId = modVO.getBoardId(); // 수정할 글 번호
		String title = modVO.getTitle(); // 수정된 제목
		String content = modVO.getContent(); // 수정된 내용
		String file = modVO.getFile(); // 수정된 첨부파일
		String bannerImg = modVO.getBannerImg(); // 수정된 배너 이미지
		System.out.println("boardDAO에서 updateBoard 호출됨. 글 수정 요청정보 - boardId: " + boardId + ", title: " + title + ", content: " + content + ", file: " + file + ", bannerImg: " + bannerImg);

		try {
			// 1. DB 연결
			con = DbcpBean.getConnection();
			
			
			
			// 2. SQL문 작성
			String sql = "UPDATE board SET title = ?, content = ?";
			
			// 만약 새로운 파일 이름이 있다면 (null도 아니고 빈 문자열도 아닌경우)
			if(file != null && !file.isEmpty()) {
				sql += ", file = ?";
			}
			
			// 만약 새로운 배너 이름이 있다면 (null도 아니고 빈 문자열도 아닌경우)
			if(bannerImg != null && !bannerImg.isEmpty()) {
				sql += ", banner_img = ?";
			}
			
			//마지막으로 WHERE 조건절 추가하기
			sql += " WHERE board_id = ?";
			
			System.out.println("실행될 SQL 글 수정 : " + sql);
			
			
			
			// 3. PreparedStatement 객체 생성
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, title); // 첫번째 ?에 수정된 제목
			pstmt.setString(2, content); // 두번째 ?에 수정된 내용
			
			// 만약 새로운 파일 이름이 있다면
			if(file != null && !file.isEmpty()) {
				pstmt.setString(3, file); // 세번째 ?에 수정된 첨부파일
				
				// 만약 새로운 배너 이름이 있다면
				if(bannerImg != null && !bannerImg.isEmpty()) {
					pstmt.setString(4, bannerImg); // 네번째 ?에 수정된 배너 이미지
					pstmt.setInt(5, boardId); // 다섯번째 ?에 수정할 글 번호
				}else {
					// 배너를 수정하지 않는 경우, 네번째 ?는 글 번호가 됨
					pstmt.setInt(4, boardId); // 네번째 ?에 수정할 글 번호
				}
				
			}else {// 새로운 파일 이름이 없다면 (파일 수정을 안한 경우)
				
				// 파일은 수정 하지 않고 배너만 수정하는 경우
				if(bannerImg != null && !bannerImg.isEmpty()) {
					pstmt.setString(3, bannerImg); // 세번째 ?에 수정된 배너 이미지
					pstmt.setInt(4, boardId); // 네번째 ?에 수정할 글 번호
					
				}else { // 파일과 배너 모두 수정하지 않는 경우
					pstmt.setInt(3, boardId); // 세번째 ?에 수정할 글 번호
				}
			}
			
			
			// 4. SQL 실행 (업데이트 실행)
			int result = pstmt.executeUpdate();
			System.out.println("UPDATE 실행 결과, 영향 받은 행의 수 : " + result);
			
			
		} catch (SQLException e) {
			System.err.println("오류 : updateBoard() 메소드 실행 중 오류 : " + e.getMessage());
			e.printStackTrace();
		} finally {
			// 5. 자원 반납
			DbcpBean.close(con, pstmt);
		}
		
		
		
		
	}

	
	
	
	

	
	

	

}
