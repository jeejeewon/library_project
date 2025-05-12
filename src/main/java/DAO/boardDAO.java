package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import java.util.Vector;

import DAO.DbcpBean;
import VO.boardVO;

public class boardDAO {
    Connection con = null; // 데이터베이스 연결 통로
    PreparedStatement pstmt = null; // SQL 명령 실행 도구
    ResultSet rs = null; // 데이터 조회 결과 상자

    // 기본 생성자
    public boardDAO() {}

    // 데이터베이스의 board테이블에 저장된 정보를 조회해서 읽어오는 메소드
    public Vector<boardVO> getAllBoardList() {
        Vector<boardVO> vector = new Vector<>();
        boardVO vo = null;

        try {
            con = DbcpBean.getConnection();
            String sql = "select * from board";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                vo = new boardVO(
                    rs.getInt("board_id"),
                    rs.getInt("category"),
                    rs.getString("title"),
                    rs.getString("content"),
                    rs.getString("user_id"),
                    rs.getInt("book_no"),
                    rs.getString("file"),
                    rs.getString("banner_img"),
                    rs.getTimestamp("created_at"), // 수정된 부분
                    rs.getInt("views"),
                    rs.getBoolean("secret"),
                    rs.getString("reply")
                );
                vector.add(vo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs);
        }

        return vector;
    }

    // 데이터베이스의 board테이블을 조회해서 읽어오는 메소드 (매개변수는 카테고리번호)
    public Vector<boardVO> getBoardList(int category) {
        Vector<boardVO> vector = new Vector<>();
        boardVO vo = null;

        try {
            con = DbcpBean.getConnection();
            String sql = "SELECT * FROM board WHERE category = ? ORDER BY created_at DESC";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, category);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                vo = new boardVO(
                    rs.getInt("board_id"),
                    rs.getInt("category"),
                    rs.getString("title"),
                    rs.getString("content"),
                    rs.getString("user_id"),
                    rs.getInt("book_no"),
                    rs.getString("file"),
                    rs.getString("banner_img"),
                    rs.getTimestamp("created_at"), // 수정된 부분
                    rs.getInt("views"),
                    rs.getBoolean("secret"),
                    rs.getString("reply")
                );
                vector.add(vo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs);
        }

        return vector;
    }

    // DB에 새 글을 추가하기 전에 DB에 저장된 가장 최신 글번호를 검색해서 제공하는 메소드
    public int getMaxBoardId() {
        try {
            con = DbcpBean.getConnection();
            String sql = "select max(board_id) from board";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return (rs.getInt(1) + 1); // 최신글번호에 1을 더해서 리턴
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs);
        }
        return 0;
    }

    // DB에 새글을 추가하는 메소드
    public int insertBoard(boardVO vo) {
        int boardId = getMaxBoardId(); // DB에 새글을 추가하기 전에 DB에 저장된 가장 최신 글번호를 검색해서 제공하는 메소드 호출

        try {
            con = DbcpBean.getConnection();

            // boardVO객체에서 필요한 정보 꺼내기
            int category = vo.getCategory();
            String title = vo.getTitle();
            String content = vo.getContent();
            String userId = vo.getUserId();
            int bookNo = vo.getBookNo();
            String file = vo.getFile();
            String bannerImg = vo.getBannerImg();
            Timestamp createdAt = vo.getCreatedAt(); // 수정된 부분
            boolean secret = vo.getSecret();

            // DB에 새글을 추가하기 위한 SQL문
            String sql = "insert into board (board_id, category, title, content, user_id, book_no, file, banner_img, created_at, secret) "
                       + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            pstmt = con.prepareStatement(sql);

            pstmt.setInt(1, boardId);
            pstmt.setInt(2, category);
            pstmt.setString(3, title);
            pstmt.setString(4, content);
            pstmt.setString(5, userId);
            pstmt.setInt(6, bookNo);
            pstmt.setString(7, file);
            pstmt.setString(8, bannerImg);
            pstmt.setTimestamp(9, createdAt); // 수정된 부분
            pstmt.setBoolean(10, secret);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt);
        }
        return boardId;
    }

    // 뷰 수를 +1 증가시키는 메소드
    public void increaseViewCount(int boardId) {
        try {
            con = DbcpBean.getConnection();
            String sql = "UPDATE board SET views = views + 1 WHERE board_id = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, boardId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt);
        }
    }

    // 주어진 글번호(boardId)에 해당하는 글을 DB에서 조회하여 boardVO객체에 담아서 리턴하는 메소드
    public boardVO selectBoard(int boardId) {
        boardVO boardVO = null;

        try {
            con = DbcpBean.getConnection();
            String sql = "SELECT * FROM board WHERE board_id = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, boardId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                boardVO = new boardVO(
                    rs.getInt("board_id"),
                    rs.getInt("category"),
                    rs.getString("title"),
                    rs.getString("content"),
                    rs.getString("user_id"),
                    rs.getInt("book_no"),
                    rs.getString("file"),
                    rs.getString("banner_img"),
                    rs.getTimestamp("created_at"), // 수정된 부분
                    rs.getInt("views"),
                    rs.getBoolean("secret"),
                    rs.getString("reply")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs);
        }

        return boardVO;
    }

    // 현재 상세페이지의 바로 앞 글 번호를 조회하여 리턴하는 메소드
    public int getPreBoardId(int currentBoardId, int category) {
        int boardId = 0;

        try {
            con = DbcpBean.getConnection();
            String sql = "SELECT board_id FROM board WHERE board_id < ? AND category = ? ORDER BY board_id DESC LIMIT 1";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, currentBoardId);
            pstmt.setInt(2, category);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("board_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs);
        }

        return boardId;
    }

    // 현재 상세페이지의 바로 뒤 글 번호를 조회하여 리턴하는 메소드
    public int getNextBoardId(int currentBoardId, int category) {
        int boardId = 0;

        try {
            con = DbcpBean.getConnection();
            String sql = "SELECT board_id FROM board WHERE board_id > ? AND category = ? ORDER BY board_id ASC LIMIT 1";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, currentBoardId);
            pstmt.setInt(2, category);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("board_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs);
        }

        return boardId;
    }

    // 게시글 수정
    public void updateBoard(boardVO modVO) {
        int boardId = modVO.getBoardId(); // 수정할 글 번호
        String title = modVO.getTitle(); // 수정된 제목
        String content = modVO.getContent(); // 수정된 내용
        String file = modVO.getFile(); // 수정된 첨부파일
        String bannerImg = modVO.getBannerImg(); // 수정된 배너 이미지

        try {
            con = DbcpBean.getConnection();

            String sql = "UPDATE board SET title = ?, content = ?";

            if (file != null && !file.isEmpty()) {
                sql += ", file = ?";
            }
            if (bannerImg != null && !bannerImg.isEmpty()) {
                sql += ", banner_img = ?";
            }
            sql += " WHERE board_id = ?";

            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, title);
            pstmt.setString(2, content);

            if (file != null && !file.isEmpty()) {
                pstmt.setString(3, file);
                if (bannerImg != null && !bannerImg.isEmpty()) {
                    pstmt.setString(4, bannerImg);
                    pstmt.setInt(5, boardId);
                } else {
                    pstmt.setInt(4, boardId);
                }
            } else {
                if (bannerImg != null && !bannerImg.isEmpty()) {
                    pstmt.setString(3, bannerImg);
                    pstmt.setInt(4, boardId);
                } else {
                    pstmt.setInt(3, boardId);
                }
            }

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt);
        }
    }

    
    //게시글을 DB테이블에서 삭제하는 메소드
	public void deletBoard(int boardID) {
		System.out.println("글 삭제 작업 시작 (글 번호 : "+boardID+")");
		try {
			//1.DB연결 빌려오기
			con = DbcpBean.getConnection();
			//2.SQL작성
            String sql = "DELETE FROM board WHERE board_id = ?";
            System.out.println("실행될 SQL (글 삭제) : "+sql.replace("?", String.valueOf(boardID)));
            //3.PreparedStatement 객체 생성 및 파라미터 설정
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, boardID);
            //4.SQL실행
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("DELETE 실행 결과 : 삭제 완료");
			
		} catch (SQLException e) {
			System.err.println("오류 : deletBoard()메소드 실행 중 SQL 오류 : " + e.getMessage());
			e.printStackTrace();
		}finally {
			//5.자원해제
			DbcpBean.close(con, pstmt);
		}
		
	}
}
