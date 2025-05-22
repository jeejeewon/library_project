package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import Dao.DbcpBean;
import Vo.boardVO;

public class boardDAO {
    Connection con = null; // 데이터베이스 연결 통로
    PreparedStatement pstmt = null; // SQL 명령 실행 도구
    ResultSet rs = null; // 데이터 조회 결과 상자

    // 기본 생성자
    public boardDAO() {}

    // 카테고리 번호에 해당하는 게시글 목록을 가져오는 메소드
    // 매개변수 int category = 게시글의 카테고리번호 0=공지사항, 1=문의게시판, 2=내서평
    // 매개변수 int startRow = 페이징의 시작위치
    // 매개변수 int endRow = 조회할 게시글의 마지막 핸 번호
    public List<boardVO> getBoardList(int category, int startRow, int endRow, String searchKeyword, String searchType, String currentUserId) {
    	
        List<boardVO> boardList = new ArrayList<boardVO>(); // 게시글을 저장할 List 객체
       

        try {
            // DB 연결
            con = DbcpBean.getConnection();
            
            // 검색어를 포함, 페이징 처리된 게시글 리스트를 조회하는 쿼리문
//          String sql = "SELECT * FROM board WHERE category = ? AND " + searchType + " LIKE ? ORDER BY created_at DESC LIMIT ? OFFSET ?";
            // SQL 쿼리 빌드를 위한 StringBuilder 사용!
            StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM board WHERE category = ? ");
            
            // '내 서평'(카테고리 2)이고 currentUserId가 넘어왔으면 user_id로 필터링 조건 추가
            boolean useUserIdFilter = (category == 2 && currentUserId != null && !currentUserId.isEmpty());
            if (useUserIdFilter) {
                sqlBuilder.append(" AND user_id = ? "); // user_id 컬럼으로 필터링 조건 추가!
            }
            
            // 검색어 조건 추가
             if (searchType != null && !searchType.isEmpty()) { // searchType이 null이거나 비어있을 경우 대비 추가
                 sqlBuilder.append(" AND ").append(searchType).append(" LIKE ? ");
            }
          
            // 정렬, 페이징 조건 추가
            sqlBuilder.append(" ORDER BY created_at DESC LIMIT ? OFFSET ?");
            
            // 최종 SQL 쿼리 확인
            System.out.println("Generated SQL: " + sqlBuilder.toString());
            
            
            String sql = sqlBuilder.toString();
            // PreparedStatement 객체 생성 (필드 변수인 pstmt에 할당!)
            pstmt = con.prepareStatement(sql);

            
            int paramIndex = 1; // ? 는 1부터 시작!

            // 1. category 값 설정
            pstmt.setInt(paramIndex++, category);

            // 2. '내 서평' 필터링을 사용할 경우에만 currentUserId 값 설정!
            if (useUserIdFilter) {
                pstmt.setString(paramIndex++, currentUserId);
            }

            // 3. 검색어 값 설정
            pstmt.setString(paramIndex++, "%" + searchKeyword + "%"); // 검색어를 LIKE 쿼리로 포함

            // 4. 페이징을 위한 LIMIT, OFFSET 값 설정
            pstmt.setInt(paramIndex++, endRow - startRow); // 조회할 게시글 수 (LIMIT)
            pstmt.setInt(paramIndex++, startRow); // 조회 시작 행 번호 (OFFSET)

            // 쿼리 실행 (필드 변수인 rs에 할당!)
            rs = pstmt.executeQuery();

            // 결과셋에서 각 게시글 정보를 boardVO 객체로 만들어 vector에 추가
            while (rs.next()) {
                boardVO vo = new boardVO(
                    rs.getInt("board_id"),  // 게시글 고유 번호
                    rs.getInt("category"),   // 카테고리 번호
                    rs.getString("title"),   // 게시글 제목
                    rs.getString("content"), // 게시글 내용
                    rs.getString("user_id"), // 작성자 ID
                    rs.getInt("book_no"),    // 도서 번호
                    rs.getString("file"),    // 첨부파일명
                    rs.getString("banner_img"), // 배너 이미지 파일명
                    rs.getTimestamp("created_at"), // 작성일
                    rs.getInt("views"),      // 조회수
                    rs.getBoolean("secret"), // 비공개 여부
                    rs.getString("reply")    // 답변 내용
                );
                boardList.add(vo); // boardList에 게시글 객체 추가
            }

        } catch (SQLException e) {
            e.printStackTrace(); // 예외 처리
        } finally {
            DbcpBean.close(con, pstmt, rs); // 연결 자원 닫기
        }

        return boardList; // 게시글 리스트 반환
    }

    
    
    // 게시판의 총 게시글 수를 구하는 메소드
    public int getTotalBoardCount(int category, String searchKeyword, String searchType) {
    	
        int totalCount = 0; // 총 게시글 수를 저장할 변수
        
        try {
            // 데이터베이스 연결
            con = DbcpBean.getConnection();
            
            // 검색 조건을 포함, 카테고리 번호에 해당하는 게시글의 개수를 구하는 SQL 쿼리
            // searchType에는 select로 선택한게 온다 즉 title(제목), content(내용), userId(작성자) 중 하나!
            String sql = "SELECT COUNT(*) FROM board WHERE category = ? AND " + searchType + " LIKE ?";
            
            // 쿼리 실행 준비
            pstmt = con.prepareStatement(sql);
            
            
            // 첫 번째 파라미터로 category 값 설정
            // SQL 쿼리에서 ? 자리(첫 번째 파라미터)에 category 값을 설정하는 부분
            // ? 자리에 매개변수로 받은 카테고리번호를 넣게된다.
            pstmt.setInt(1, category);
            
            
            // 두번째 ? 설정
            // 검색하는 단어가 포함된 모든 결과를 찾는구문을 넣음
            // 만약 searchKeyword가 "java"라면, "%java%"는 SQL 쿼리에서 "java"라는 단어를 포함한 모든 값을 검색하도록 설정
            pstmt.setString(2, "%" + searchKeyword + "%");
            
            
            // 쿼리 실행
            rs = pstmt.executeQuery();
            
            // 쿼리 결과에서 게시글 수를 추출
            if (rs.next()) {
                totalCount = rs.getInt(1); // COUNT(*)는 결과가 하나의 컬럼이므로 첫 번째 컬럼을 가져옴
            }
        } catch (SQLException e) {
            // 예외 처리
            e.printStackTrace();
        } finally {
            // 자원 반납
            DbcpBean.close(con, pstmt, rs);
        }
        
        return totalCount; // 총 게시글 수 반환
    }



    
    // DB에 새글을 추가하는 메소드
    public int insertBoard(boardVO vo) {

    	try {
    	        con = DbcpBean.getConnection();

    	        // boardVO 객체에서 필요한 정보 꺼내기
    	        int category = vo.getCategory();
    	        String title = vo.getTitle();
    	        String content = vo.getContent();
    	        String userId = vo.getUserId();
    	        int bookNo = vo.getBookNo();
    	        String file = vo.getFile();
    	        String bannerImg = vo.getBannerImg();
    	        Timestamp createdAt = vo.getCreatedAt();
    	        boolean secret = vo.getSecret();

    	        // DB에 새글을 추가하기 위한 SQL문 (board_id는 자동 증가하므로 생략)
    	        String sql = "insert into board (category, title, content, user_id, book_no, file, banner_img, created_at, secret) "
    	                   + "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    	        pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); // 자동 생성된 키 반환

    	        pstmt.setInt(1, category);
    	        pstmt.setString(2, title);
    	        pstmt.setString(3, content);
    	        pstmt.setString(4, userId);
    	        
    	        // bookNo가 0이면 NULL로 처리, 아니면 해당 값으로 처리
    	        // (주의: boardVO의 bookNo가 int 타입이고 0이 '없음'을 의미한다고 가정)
    	        if (bookNo == 0) {
    	            pstmt.setNull(5, java.sql.Types.INTEGER); // book_no 컬럼 타입에 맞게 Types.INTEGER 사용
    	        } else {
    	            pstmt.setInt(5, bookNo);
    	        }
    	        
    	        pstmt.setString(6, file);
    	        pstmt.setString(7, bannerImg);
    	        pstmt.setTimestamp(8, createdAt);
    	        pstmt.setBoolean(9, secret);

    	        int affectedRows = pstmt.executeUpdate();

    	        if (affectedRows > 0) {
    	            // 삽입 후 자동으로 생성된 board_id 가져오기
    	            try (ResultSet rs = pstmt.getGeneratedKeys()) {
    	                if (rs.next()) {
    	                    return rs.getInt(1); // 자동 생성된 board_id 반환
    	                }
    	            }
    	        }
    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	    } finally {
    	        DbcpBean.close(con, pstmt);
    	    }
    	    return 0; // 삽입 실패 시 0 반환
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
            String sql = "SELECT b.*, bo.thumbnail " + // board의 모든 컬럼 (b.*) 와 book의 thumbnail (bo.thumbnail)
                    "FROM board b LEFT JOIN book bo ON b.book_no = bo.book_no " + // book_no로 두 테이블 연결
                    "WHERE b.board_id = ?";
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
                    rs.getTimestamp("created_at"),
                    rs.getInt("views"),
                    rs.getBoolean("secret"),
                    rs.getString("reply")
                );
                
                String thumbnailValueFromDB = rs.getString("thumbnail"); // DB에서 thumbnail 값을 읽어옴
                boardVO.setThumbnail(thumbnailValueFromDB);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs);
        }

        return boardVO;
    }
    
    // 배너이미지가 등록된 공지사항만 조회
    public boardVO selectBannerBoard(int boardId) {
        boardVO boardVO = null;

        try {
            con = DbcpBean.getConnection();
            String sql = "SELECT * FROM board WHERE board_id = ? AND category = 0 AND banner_img IS NOT NULL";
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
    
    
    
    
    // (행사게시판)현재 상세페이지의 바로 앞 글 번호를 조회하여 리턴하는 메소드
    public int getPreBannerBoardId(int currentBoardId) {
    	int boardId = 0;
    	
    	try {
    		con = DbcpBean.getConnection();
    		String sql = "SELECT board_id FROM board WHERE board_id < ? AND category = 0 AND banner_img IS NOT NULL ORDER BY board_id DESC LIMIT 1";
    		pstmt = con.prepareStatement(sql);
    		pstmt.setInt(1, currentBoardId);
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
    public int getNextBannerBoardId(int currentBoardId) {
    	int boardId = 0;
    	
    	try {
    		con = DbcpBean.getConnection();
    		String sql = "SELECT board_id FROM board WHERE board_id > ? AND category = 0 AND banner_img IS NOT NULL ORDER BY board_id ASC LIMIT 1";
    		pstmt = con.prepareStatement(sql);
    		pstmt.setInt(1, currentBoardId);
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
        boolean secret = modVO.getSecret(); // 수정된 비밀글 여부

        try {
            con = DbcpBean.getConnection();

            String sql = "UPDATE board SET title = ?, content = ?, secret = ?, file = ?, banner_img = ? WHERE board_id = ?";

            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setBoolean(3, secret);
            pstmt.setString(4, file); // <-- modVO에서 받은 파일 이름을 그대로 바인딩!
            pstmt.setString(5, bannerImg); // <-- modVO에서 받은 배너 이미지 이름을 그대로 바인딩!
            pstmt.setInt(6, boardId); // <-- WHERE 조건 boardId 바인딩

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
            System.out.println("실행될 SQL (글 삭제) : DELETE FROM board WHERE board_id = ?");
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

	
	//답변 달기 메소드
	public boolean updateReply(int boardId, String reply) {
	    String sql = "UPDATE board SET reply = ? WHERE board_id = ?";

	    try {
	        con = DbcpBean.getConnection(); // DBCP에서 커넥션 가져오기
	        pstmt = con.prepareStatement(sql);
	        pstmt.setString(1, reply);
	        pstmt.setInt(2, boardId);

	        return pstmt.executeUpdate() == 1; // 업데이트 성공 여부 반환
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (pstmt != null) pstmt.close();
	            if (con != null) con.close(); // 커넥션 반환
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return false;
	}



	public boolean deleteReply(int boardId) {
		   String sql = "UPDATE board SET reply = NULL WHERE board_id = ?";

		    try {
		        con = DbcpBean.getConnection(); // DBCP에서 커넥션 가져오기
		        pstmt = con.prepareStatement(sql);
		        pstmt.setInt(1, boardId);

		        return pstmt.executeUpdate() == 1; // 업데이트 성공 여부 반환
		    } catch (Exception e) {
		        e.printStackTrace();
		    } finally {
		        try {
		            if (pstmt != null) pstmt.close();
		            if (con != null) con.close(); // 커넥션 반환
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		    return false;
	}

	// 배너가 있는 게시글 목록을 가져오는 메소드
	public List<boardVO> getBannerList(int startRow, int endRow, String searchKeyword, String searchType) {
	    List<boardVO> boardList = new ArrayList<>();
	    String sql = "SELECT * FROM board WHERE category = 0 AND banner_img IS NOT NULL ORDER BY board_id DESC LIMIT ?, ?";  // 카테고리 0, 배너가 있는 글만

	    // 검색 조건에 따른 추가 쿼리 처리
	    if (searchKeyword != null && !searchKeyword.isEmpty()) {
	        if (searchType.equals("title")) {
	            sql += " AND title LIKE ?";
	        } else if (searchType.equals("content")) {
	            sql += " AND content LIKE ?";
	        } else if (searchType.equals("userId")) {
	            sql += " AND user_id LIKE ?";
	        }
	    }

	    try {
	        con = DbcpBean.getConnection();
	        pstmt = con.prepareStatement(sql);
	        pstmt.setInt(1, startRow);  // 페이징 시작 row
	        pstmt.setInt(2, endRow);    // 페이징 끝 row

	        // 검색 조건이 있을 경우, 파라미터 설정
	        if (searchKeyword != null && !searchKeyword.isEmpty()) {
	            pstmt.setString(3, "%" + searchKeyword + "%");  // LIKE 쿼리 파라미터
	        }

	        rs = pstmt.executeQuery();

	        while (rs.next()) {
	            boardVO board = new boardVO();
	            board.setBoardId(rs.getInt("board_id"));
	            board.setTitle(rs.getString("title"));
	            board.setContent(rs.getString("content"));
	            board.setUserId(rs.getString("user_id"));
	            board.setBannerImg(rs.getString("banner_img"));
	            // 추가적인 VO 속성 설정 필요 시 추가

	            boardList.add(board);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        DbcpBean.close(con, pstmt, rs);
	    }

	    return boardList;
	}

	// 배너가 있는 게시글의 전체 개수를 구하는 메소드
	public int getTotalBannerCount(String searchKeyword, String searchType) {
	    int totalCount = 0;
	    String sql = "SELECT COUNT(*) FROM board WHERE category = 0 AND banner_img IS NOT NULL"; // 카테고리 0, 배너가 있는 글만

	    // 검색 조건에 따른 추가 쿼리 처리
	    if (searchKeyword != null && !searchKeyword.isEmpty()) {
	        if (searchType.equals("title")) {
	            sql += " AND title LIKE ?";
	        } else if (searchType.equals("content")) {
	            sql += " AND content LIKE ?";
	        } else if (searchType.equals("userId")) {
	            sql += " AND user_id LIKE ?";
	        }
	    }

	    try {
	        con = DbcpBean.getConnection();
	        pstmt = con.prepareStatement(sql);

	        // 검색 조건이 있을 경우, 파라미터 설정
	        if (searchKeyword != null && !searchKeyword.isEmpty()) {
	            pstmt.setString(1, "%" + searchKeyword + "%");  // LIKE 쿼리 파라미터
	        }

	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            totalCount = rs.getInt(1);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        DbcpBean.close(con, pstmt, rs);
	    }

	    return totalCount;
	}

	
	//북 넘버에 맞는 서평 목록 조회
	public List<boardVO> getReviewsByBookNo(int bookNo) {
		List<boardVO> reviewList = new ArrayList<boardVO>();

	        try {
	            con = DbcpBean.getConnection();
	            String sql = "SELECT * FROM board WHERE category = 2 AND book_no = ? ORDER BY created_at DESC";
	            pstmt = con.prepareStatement(sql);
	            pstmt.setInt(1, bookNo);
	            rs = pstmt.executeQuery();
	            while (rs.next()) {
	                boardVO vo = new boardVO(
	                    rs.getInt("board_id"),
	                    rs.getInt("category"),
	                    rs.getString("title"),
	                    rs.getString("content"),
	                    rs.getString("user_id"),
	                    rs.getInt("book_no"),
	                    rs.getString("file"),
	                    rs.getString("banner_img"),
	                    rs.getTimestamp("created_at"),
	                    rs.getInt("views"),
	                    rs.getBoolean("secret"),
	                    rs.getString("reply")
	                );
	                reviewList.add(vo);
	            }// end while
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            DbcpBean.close(con, pstmt, rs);
	        }

	        return reviewList;
	}

	//메인화면에 배너에 데이터 정보 전달 (매개변수는 배너갯수)
	public List<boardVO> getLatestEventBanners(int limit) {
		 List<boardVO> eventBannerList = new ArrayList<>(); 

		    try {
		        con = DbcpBean.getConnection(); 
		        String query = "SELECT board_id, category, title, content, user_id, book_no, file, banner_img, created_at, views, secret, reply " +
		                       "FROM board " +
		                       "WHERE category = 0 AND banner_img IS NOT NULL " +
		                       "ORDER BY created_at DESC " + 
		                       "LIMIT ?;"; 


		        pstmt = con.prepareStatement(query);

		        pstmt.setInt(1, limit);


		        rs = pstmt.executeQuery();


		        while (rs.next()) {
		            boardVO board = new boardVO(
		                rs.getInt("board_id"),
		                rs.getInt("category"),
		                rs.getString("title"),
		                rs.getString("content"),
		                rs.getString("user_id"),
		                rs.getInt("book_no"),
		                rs.getString("file"),
		                rs.getString("banner_img"),
		                rs.getTimestamp("created_at"),
		                rs.getInt("views"),
		                rs.getBoolean("secret"),
		                rs.getString("reply")
		            );
		            eventBannerList.add(board);
		        }

		    } catch (SQLException e) {
		        e.printStackTrace();
		        System.err.println("BoardDAO getLatestEventBanners 오류: " + e.getMessage());
		    } finally {
		        DbcpBean.close(con, pstmt, rs); 
		    }

		    return eventBannerList;
	}

	// ✨✨ 공지사항 목록 i 개를 최신 순서대로 가져오는 메소드 (DAO)! ✨✨
	// BoardService 에서 int i 값을 받아서 최신 공지사항 i 개를 가져온다!
	public List<boardVO> getLatestNotices(int i) {
		
        List<boardVO> noticeList = new ArrayList<boardVO>(); // 공지사항 게시글을 저장할 List 객체
        // 네 기존 getBoardList 메소드처럼 Connection, PreparedStatement, ResultSet 선언이 필요할 거야!
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DbcpBean.getConnection();
            String sql = "SELECT * FROM board WHERE category = 0 ORDER BY created_at DESC LIMIT ?"; 

            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, i); 
            rs = pstmt.executeQuery();
            while (rs.next()) {
                boardVO vo = new boardVO(
                    rs.getInt("board_id"),
                    rs.getInt("category"),
                    rs.getString("title"),
                    rs.getString("content"),
                    rs.getString("user_id"),
                    rs.getInt("book_no"),
                    rs.getString("file"),
                    rs.getString("banner_img"),
                    rs.getTimestamp("created_at"),
                    rs.getInt("views"),
                    rs.getBoolean("secret"), 
                    rs.getString("reply")
                );
                noticeList.add(vo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
             System.err.println("BoardDAO - getLatestNotices DB 오류 발생: " + e.getMessage());
        } finally {
            DbcpBean.close(con, pstmt, rs); 
        }

        return noticeList;
	}






}
