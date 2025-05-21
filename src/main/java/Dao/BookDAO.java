package Dao;

import java.sql.*;
import java.util.Vector;

import Vo.BookVo;
import Vo.RentalVo;

public class BookDAO{

    private Connection con;
    private PreparedStatement pstmt;
    private ResultSet rs;

    // 전체 도서 수
    public int bookCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM book";
        try {
            con = DbcpBean.getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) count = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs);
        }
        return count;
    }
    
    // 일반 전체 도서 페이징
    public Vector<BookVo> booksByPage(int offset, int limit) {
        Vector<BookVo> list = new Vector<>();
        String sql = "SELECT * FROM book ORDER BY book_no DESC LIMIT ?, ?";

        try {
            con = DbcpBean.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, offset);
            pstmt.setInt(2, limit);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                BookVo book = new BookVo();
                book.setBookNo(rs.getInt("book_no"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setPublisher(rs.getString("publisher"));      
                book.setPublishYear(rs.getInt("publish_year"));
                book.setIsbn(rs.getString("isbn"));                
                book.setCategory(rs.getString("category"));          
                book.setBookInfo(rs.getString("book_info"));  
                book.setThumbnail(rs.getString("thumbnail"));
                book.setRentalState(rs.getInt("rental_state"));  
                list.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs);
        }

        return list;
    }
   
    
    // 도서 상세정보
    public BookVo getBook(int bookNo) {
        BookVo book = null;
        String sql = "SELECT * FROM book WHERE book_no = ?";

        try {
            con = DbcpBean.getConnection(); 
            pstmt = con.prepareStatement(sql); 
            pstmt.setInt(1, bookNo); 
            rs = pstmt.executeQuery();

            if (rs.next()) {
                book = extractBook(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs); 
        }

        return book;
    }

    // 검색 도서 전체 수
    public int bookCount(String keyword) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM book "
        		   + "WHERE title LIKE ? OR author LIKE ? OR category LIKE ?";
        try {
            con = DbcpBean.getConnection();
            pstmt = con.prepareStatement(sql);
            String like = "%" + keyword + "%";
            pstmt.setString(1, like);
            pstmt.setString(2, like);
            pstmt.setString(3, like);
            rs = pstmt.executeQuery();
            if (rs.next()) count = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs);
        }
        return count;
    }

    // 검색 도서 페이징 (제목, 저자, 출판사, 카테고리 등 검색)
    public Vector<BookVo> booksByPage(String keyword, int offset, int limit) {
        Vector<BookVo> list = new Vector<>();
        String sql = "SELECT * FROM book " +
                     "WHERE title LIKE ? OR author LIKE ? OR category LIKE ? OR publisher LIKE ? " +
                     "ORDER BY book_no DESC LIMIT ?, ?";

        try {
            con = DbcpBean.getConnection();
            pstmt = con.prepareStatement(sql);

            String likeKeyword = "%" + keyword + "%";
            pstmt.setString(1, likeKeyword);
            pstmt.setString(2, likeKeyword);
            pstmt.setString(3, likeKeyword);
            pstmt.setString(4, likeKeyword);
            pstmt.setInt(5, offset);
            pstmt.setInt(6, limit);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                BookVo book = new BookVo();
                book.setBookNo(rs.getInt("book_no"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setThumbnail(rs.getString("thumbnail"));
                list.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs);
        }

        return list;
    }

    // 신착 도서 전체 수
    public int newBooksCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM book "
        		   + "WHERE publish_year = YEAR(CURDATE())";
        try {
            con = DbcpBean.getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs);
        }
        return count;
    }   
    
    // 신착 도서 페이징
    public Vector<BookVo> newBooksByPage(int offset, int limit) {
        Vector<BookVo> list = new Vector<>();
        String sql = "SELECT * FROM book WHERE publish_year = YEAR(CURDATE()) "
                   + "ORDER BY book_no DESC LIMIT ?, ?";

        try {
            con = DbcpBean.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, offset);
            pstmt.setInt(2, limit);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                BookVo book = new BookVo();
                book.setBookNo(rs.getInt("book_no"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setThumbnail(rs.getString("thumbnail"));
                list.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs);
        }

        return list;
    }
    
	// 인기 도서 전체 수
    public int bestBooksCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM book WHERE rent_count > 0"; 

        try {
            con = DbcpBean.getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs);
        }
        return count;
    }

    // 인기 도서 페이징 조회
    public Vector<BookVo> bestBooksByPage(int offset, int limit) {
        Vector<BookVo> list = new Vector<>();
        String sql = "SELECT * FROM book WHERE rent_count > 5 "
        		   + "ORDER BY rent_count DESC LIMIT ?, ?";

        try {
            con = DbcpBean.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, offset);
            pstmt.setInt(2, limit);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                BookVo book = new BookVo();
                book.setBookNo(rs.getInt("book_no"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setThumbnail(rs.getString("thumbnail"));
                list.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs);
        }

        return list;
    }
    
    // 도서 대여
    // rental_book 테이블에 return 날짜와 return 상태 반영
    // book 테이블에 rental 상태 반영과 대여횟수 추가
    public boolean rentBook(String userId, int bookNo) {
        boolean result = false;
        String insertSql = "INSERT INTO rental_book (user_id, book_no, "
        			     + "start_date, return_due, return_state) "
                         + "VALUES (?, ?, NOW(), DATE_ADD(NOW(), INTERVAL 14 DAY), 0)";
        String updateSql = "UPDATE book SET rental_state = 1, "
        			     + "rent_count = rent_count + 1 WHERE book_no = ?";

        try {
            con = DbcpBean.getConnection();
            con.setAutoCommit(false);

            // 1. rental_book insert
            pstmt = con.prepareStatement(insertSql);
            pstmt.setString(1, userId);
            pstmt.setInt(2, bookNo);
            int insertResult = pstmt.executeUpdate();
            pstmt.close();

            // 2. book 테이블 update
            pstmt = con.prepareStatement(updateSql);
            pstmt.setInt(1, bookNo);
            int updateResult = pstmt.executeUpdate();

            if (insertResult == 1 && updateResult == 1) {
                con.commit();
                result = true;
            } else {
                con.rollback();
            }

        } catch (Exception e) {
            try { 
            	if (con != null) con.rollback(); 
            } catch (Exception ex) { 
            	ex.printStackTrace(); 
            	}
            e.printStackTrace();
	        } finally {
	            try { 
	            	if (con != null) con.setAutoCommit(true); 
	            } catch (Exception ex) { 
	            	ex.printStackTrace(); 
	            }
	            DbcpBean.close(con, pstmt, rs);
	        }
        return result;
    }
    
    // 대여 중인 도서 개수 조회 (반납 안 된 상태만)
    public int countCurrentRentals(String userId) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM rental_book WHERE user_id = ? AND return_state = 0";

        try {
            con = DbcpBean.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs);
        }

        return count;
    }
        
    // 대여 중인 도서 개수 조회
    public int countRentalsByUser(String userId) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM rental_book WHERE user_id = ?";
        try {
            con = DbcpBean.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();
            if (rs.next()) count = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs);
        }
        return count;
    }

    // 대여 목록 페이징
    public Vector<RentalVo> getRentalsByUserByPage(String userId, int offset, int limit) {
        Vector<RentalVo> list = new Vector<>();
        String sql = "SELECT r.*, b.title, b.thumbnail FROM rental_book r " +
                     "JOIN book b ON r.book_no = b.book_no " +
                     "WHERE r.user_id = ? " +
                     "ORDER BY r.start_date DESC LIMIT ?, ?";
        try {
            con = DbcpBean.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);
            pstmt.setInt(2, offset);
            pstmt.setInt(3, limit);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                RentalVo rental = new RentalVo();
                rental.setRentNo(rs.getInt("rent_no"));
                rental.setUserId(rs.getString("user_id"));
                rental.setBookNo(rs.getInt("book_no"));
                rental.setStartDate(rs.getTimestamp("start_date"));
                rental.setReturnDue(rs.getTimestamp("return_due"));
                rental.setReturnDate(rs.getTimestamp("return_date"));
                rental.setReturnState(rs.getInt("return_state"));

                BookVo book = new BookVo();
                book.setTitle(rs.getString("title"));
                book.setThumbnail(rs.getString("thumbnail"));
                rental.setBook(book);

                list.add(rental);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs);
        }
        return list;
    }
    
    // 도서 등록
    public boolean addBook(BookVo book) {
        boolean result = false;
        String sql = "INSERT INTO book (title, author, publisher, publish_year, "
        		   + "isbn, category, book_info, thumbnail) " 
        		   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            con = DbcpBean.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getPublisher());
            pstmt.setInt(4, book.getPublishYear()); // 출간년도만 입력
            pstmt.setString(5, book.getIsbn());
            pstmt.setString(6, book.getCategory());
            pstmt.setString(7, book.getBookInfo());
            pstmt.setString(8, book.getThumbnail());
            result = pstmt.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs);
        }
        return result;
    }
    
    // isbn 중복 확인
    public boolean isIsbnExists(String isbn) {
        boolean exists = false;
        String sql = "SELECT COUNT(*) FROM book WHERE isbn = ?";
        try {
            con = DbcpBean.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, isbn);
            rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                exists = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs);
        }
        return exists;
    }
 
    // 도서 수정
    public boolean updateBook(BookVo book) {
        boolean result = false;
        String sql = "UPDATE book SET title=?, author=?, publisher=?, publish_year=?, "
                   + "isbn=?, category=?, book_info=?, thumbnail=?, rental_state=? "
                   + "WHERE book_no=?";

        try {
            con = DbcpBean.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getPublisher());
            pstmt.setInt(4, book.getPublishYear());  // 출간년도만 입력
            pstmt.setString(5, book.getIsbn());
            pstmt.setString(6, book.getCategory());
            pstmt.setString(7, book.getBookInfo());
            pstmt.setString(8, book.getThumbnail());
            pstmt.setInt(9, book.getRentalState());
            pstmt.setInt(10, book.getBookNo());
            result = pstmt.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs);
        }

        return result;
    }

    // 도서 삭제
    public boolean deleteBook(int bookNo) {
        boolean result = false;
        String sql = "DELETE FROM book WHERE book_no=?";
        try {
            con = DbcpBean.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, bookNo);
            result = pstmt.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs);
        }
        return result;
    }

    // 반납 처리: return_date 업데이트, return_state 변경, 도서 상태 변경
    public boolean processReturn(int rentNo) {
        boolean result = false;
        String sql1 = "UPDATE rental_book SET return_state = 1, "
        		    + "return_date = NOW() WHERE rent_no = ?";
        String sql2 = "UPDATE book SET rental_state = 0 "
        		    + "WHERE book_no = (SELECT book_no FROM rental_book WHERE rent_no = ?)";

        try {
            con = DbcpBean.getConnection();
            con.setAutoCommit(false);

            pstmt = con.prepareStatement(sql1);
            pstmt.setInt(1, rentNo);
            int updated1 = pstmt.executeUpdate();
            pstmt.close();

            pstmt = con.prepareStatement(sql2);
            pstmt.setInt(1, rentNo);
            int updated2 = pstmt.executeUpdate();

            if (updated1 > 0 && updated2 > 0) {
                con.commit();
                result = true;
            } else {
                con.rollback();
            }

        } catch (Exception e) {
            e.printStackTrace();
            try { con.rollback(); } catch (Exception ignored) {}
        } finally {
            try { con.setAutoCommit(true); } catch (Exception ignored) {}
            DbcpBean.close(con, pstmt, rs);
        }
        return result;
    }

    public int countPendingRentals() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM rental_book WHERE return_state = 0";

        try {
            con = DbcpBean.getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs);
        }
        return count;
    }

    public Vector<RentalVo> selectPendingRentalsByPage(int offset, int limit) {
        Vector<RentalVo> list = new Vector<>();
        String sql = "SELECT r.*, b.title, b.thumbnail FROM rental_book r "
        		   + "JOIN book b ON r.book_no = b.book_no "
        		   + "WHERE r.return_state = 0 ORDER BY r.start_date DESC LIMIT ?, ?";

        try {
            con = DbcpBean.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, offset);
            pstmt.setInt(2, limit);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                RentalVo vo = new RentalVo();
                vo.setRentNo(rs.getInt("rent_no"));
                vo.setUserId(rs.getString("user_id"));
                vo.setBookNo(rs.getInt("book_no"));
                vo.setStartDate(rs.getTimestamp("start_date"));
                vo.setReturnDue(rs.getTimestamp("return_due"));
                vo.setReturnDate(rs.getTimestamp("return_date"));
                vo.setReturnState(rs.getInt("return_state"));

                BookVo book = new BookVo();
                book.setTitle(rs.getString("title"));
                book.setThumbnail(rs.getString("thumbnail"));
                vo.setBook(book);

                list.add(vo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs);
        }

        return list;
    }

    // 반납 대기중 목록 가져오기
    public Vector<RentalVo> getPendingRentals() {
        Vector<RentalVo> list = new Vector<>();
        String sql = "SELECT r.*, b.title, b.thumbnail FROM rental_book r "
        		   + "JOIN book b ON r.book_no = b.book_no "
                   + "WHERE r.return_state = 0 ORDER BY r.start_date ASC";

        try {
            con = DbcpBean.getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                RentalVo vo = new RentalVo();
                vo.setRentNo(rs.getInt("rent_no"));
                vo.setBookNo(rs.getInt("book_no"));
                vo.setUserId(rs.getString("user_id"));
                vo.setStartDate(rs.getTimestamp("start_date"));
                vo.setReturnDue(rs.getTimestamp("return_due"));

                BookVo book = new BookVo();
                book.setTitle(rs.getString("title"));
                book.setThumbnail(rs.getString("thumbnail"));
                vo.setBook(book);

                list.add(vo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs);
        }
        return list;
    }

    // 전체 도서 대여 확인 (book 테이블과 JOIN)    
    public int allRentalCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM rental_book";
        try {
            con = DbcpBean.getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) count = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs);
        }
        return count;
    }
    
    public Vector<RentalVo> allRentalsByPage(int offset, int limit) {
        Vector<RentalVo> list = new Vector<>();
        String sql = "SELECT r.*, b.title, b.thumbnail FROM rental_book r " +
                     "JOIN book b ON r.book_no = b.book_no " +
                     "ORDER BY r.start_date DESC LIMIT ?, ?";
        try {
            con = DbcpBean.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, offset);
            pstmt.setInt(2, limit);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                RentalVo rental = new RentalVo();
                rental.setRentNo(rs.getInt("rent_no"));
                rental.setUserId(rs.getString("user_id"));
                rental.setBookNo(rs.getInt("book_no"));
                rental.setStartDate(rs.getTimestamp("start_date"));
                rental.setReturnDue(rs.getTimestamp("return_due"));
                rental.setReturnDate(rs.getTimestamp("return_date"));
                rental.setReturnState(rs.getInt("return_state"));

                BookVo book = new BookVo();
                book.setTitle(rs.getString("title"));
                book.setThumbnail(rs.getString("thumbnail"));
                rental.setBook(book);

                list.add(rental);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs);
        }
        return list;
    }
   
    private BookVo extractBook(ResultSet rs) throws SQLException {
        BookVo book = new BookVo();
        book.setBookNo(rs.getInt("book_no"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setPublisher(rs.getString("publisher"));
        book.setPublishYear(rs.getInt("publish_year"));
        book.setBookInfo(rs.getString("book_info"));
        book.setCategory(rs.getString("category"));
        book.setThumbnail(rs.getString("thumbnail"));
        book.setIsbn(rs.getString("isbn"));
        book.setRentCount(rs.getInt("rent_count"));        
        book.setRentalState(rs.getInt("rental_state")); 
        return book;
    }

}
