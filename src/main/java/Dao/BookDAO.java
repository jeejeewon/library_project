package Dao;

import java.sql.*;
import java.util.Vector;

import Vo.BookVo;
import Vo.RentalVo;

public class BookDAO {

    Connection con;
    PreparedStatement pstmt;
    ResultSet rs;

    /* 전체 도서 목록 조회 (목록용: 최소 필드만) */
    public Vector<BookVo> allBooks() {
        Vector<BookVo> bookList = new Vector<>();

        String sql = "select book_no, title, author, thumbnail from "
        		   + "book order by book_no desc";

        try {
            con = DbcpBean.getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                BookVo book = new BookVo();
                book.setBookNo(rs.getInt("book_no"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setThumbnail(rs.getString("thumbnail"));
                bookList.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs);
        }
        return bookList;
    }
    /* 도서 상세 조회 */
    public BookVo bookDetail(int bookNo) {
        BookVo book = null;

        String sql = "select * from book where book_no = ?";

        try {
            con = DbcpBean.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, bookNo);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                book = new BookVo();
                book.setBookNo(rs.getInt("book_no"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setPublisher(rs.getString("publisher"));
                book.setPublishYear(rs.getInt("publish_year"));
                book.setBookInfo(rs.getString("book_info"));
                book.setIsbn(rs.getString("isbn"));
                book.setCategory(rs.getString("category"));
                book.setRentalState(rs.getInt("rental_state"));
                book.setThumbnail(rs.getString("thumbnail"));
                book.setRegDate(rs.getTimestamp("reg_date"));
                book.setModDate(rs.getTimestamp("mod_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs);
        }

        return book;
    }

    /* 검색 결과 조회 */
    public Vector<BookVo> searchBooks(String keyword) {
        Vector<BookVo> bookList = new Vector<>();

        String sql = "select book_no, title, author, thumbnail " 
                   + "from book "
                   + "where title like ? or author like ? or "
                   + "publisher like ? or category like ?";

        try {
            con = DbcpBean.getConnection();
            pstmt = con.prepareStatement(sql);
            String kw = "%" + keyword + "%";
            pstmt.setString(1, kw);
            pstmt.setString(2, kw);
            pstmt.setString(3, kw);
            pstmt.setString(4, kw);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                BookVo book = new BookVo();
                book.setBookNo(rs.getInt("book_no"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setThumbnail(rs.getString("thumbnail"));
                bookList.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs);
        }
        return bookList;
    }

    /* 신착 도서 조회 */
    public Vector<BookVo> newBooks() {
        Vector<BookVo> bookList = new Vector<>();

        String sql = "select book_no, title, author, thumbnail "
                   + "from book "
                   + "where publish_year = year(curdate()) "
                   + "order by reg_date desc";

        try {
            con = DbcpBean.getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                BookVo book = new BookVo();
                book.setBookNo(rs.getInt("book_no"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setThumbnail(rs.getString("thumbnail"));
                bookList.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs);
        }

        return bookList;
    }

    /* 인기 도서 조회 */
    public Vector<BookVo> bestBooks() {
        Vector<BookVo> bestList = new Vector<>();

        String sql = "select book_no, thumbnail, title, author, publisher, publish_year, rent_count "
                   + "from book where rent_count > 0 order by rent_count desc limit 8"; 
        		   // 대여가 0보다 커야 불러오고, 8개 정도만 보여주기

        try {
            con = DbcpBean.getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                BookVo vo = new BookVo();
                vo.setBookNo(rs.getInt("book_no"));
                vo.setThumbnail(rs.getString("thumbnail"));
                vo.setTitle(rs.getString("title"));
                vo.setAuthor(rs.getString("author"));
                bestList.add(vo);
            }
        } catch (Exception e) {
            System.out.println("selectBestBooks 오류 : " + e);
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs);
        }

        return bestList;
    }

    /* 도서 대출 */
    public boolean rentBook(String userId, int bookNo) {
    	
        boolean rental = false;

        try {
            con = DbcpBean.getConnection();

            // 1. 대출 상태 확인
            String bookState = "select count(*) from rental_book where book_no = ? "
            				 + "and return_state = 0";
            
            pstmt = con.prepareStatement(bookState);
            pstmt.setInt(1, bookNo);
            rs = pstmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                return false; // 이미 대출 중
            }

            DbcpBean.close(null, pstmt, rs); // 이전 pstmt, rs 닫기

            // 2. 대출 정보 삽입
            String insertBook = "insert into rental_book (user_id, book_no, return_due) "
            		          + "values (?, ?, date_add(now(), interval 14 day))";
            				  // 대출기간 14일 지정
            
            pstmt = con.prepareStatement(insertBook);
            pstmt.setString(1, userId);
            pstmt.setInt(2, bookNo);

            int row = pstmt.executeUpdate();

            if (row > 0) {
                DbcpBean.close(null, pstmt); // pstmt 닫고 다시 준비

                // 3. 대출 횟수(rent_count) 증가
                String updateBook = "update book set rent_count = rent_count + 1 "
                		          + "where book_no = ?";
                pstmt = con.prepareStatement(updateBook);
                pstmt.setInt(1, bookNo);
                pstmt.executeUpdate();

                rental = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbcpBean.close(con, pstmt, rs);
        }

        return rental;
    }
    
	public Vector<RentalVo> myRentals(String userId) {
	    Vector<RentalVo> rentalList = new Vector<>();

	    String sql = "select r.rent_no, r.book_no, b.title, b.thumbnail, "
	               + "r.start_date, r.return_due, r.return_date, r.return_state "
	               + "from rental_book r " 
	               + "join book b on r.book_no = b.book_no "
	               + "where r.user_id = ? " 
	               + "order by r.start_date desc";

	    try {
	        con = DbcpBean.getConnection();
	        pstmt = con.prepareStatement(sql);
	        pstmt.setString(1, userId);
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

	            // BookVo 객체 생성해서 join 데이터 넣기
	            BookVo book = new BookVo();
	            book.setBookNo(rs.getInt("book_no")); 
	            book.setTitle(rs.getString("title"));
	            book.setAuthor(rs.getString("author"));
	            book.setThumbnail(rs.getString("thumbnail"));

	            rental.setBook(book); // RentalVo에 BookVo 세팅

	            rentalList.add(rental);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DbcpBean.close(con, pstmt, rs);
	    }

	    return rentalList;
	}
    
	/* 도서 등록 */
	public boolean insertBook(BookVo book) {
	    String sql = "insert into book (title, author, publisher, publish_year, category, "
	    		   + "book_info, isbn, thumbnail) "
	               + "values (?, ?, ?, ?, ?, ?, ?, ?)";
	    try {
	        con = DbcpBean.getConnection();
	        pstmt = con.prepareStatement(sql);
	        pstmt.setString(1, book.getTitle());
	        pstmt.setString(2, book.getAuthor());
	        pstmt.setString(3, book.getPublisher());
	        pstmt.setInt(4, book.getPublishYear());
	        pstmt.setString(5, book.getCategory());
	        pstmt.setString(6, book.getBookInfo());
	        pstmt.setString(7, book.getIsbn());
	        pstmt.setString(8, book.getThumbnail());
	        return pstmt.executeUpdate() > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DbcpBean.close(con, pstmt);
	    }
	    return false;
	}

	/* 도서 수정 */
	public boolean updateBook(BookVo book) {	    
		String sql = "update book set title=?, author=?, publisher=?, publish_year=?, "
	    		   + "category=?, book_info=?, isbn=?, thumbnail=? "
	               + "where book_no=?";
	    try {
	        con = DbcpBean.getConnection();
	        pstmt = con.prepareStatement(sql);
	        pstmt.setString(1, book.getTitle());
	        pstmt.setString(2, book.getAuthor());
	        pstmt.setString(3, book.getPublisher());
	        pstmt.setInt(4, book.getPublishYear());
	        pstmt.setString(5, book.getCategory());
	        pstmt.setString(6, book.getBookInfo());
	        pstmt.setString(7, book.getIsbn());
	        pstmt.setString(8, book.getThumbnail());
	        pstmt.setInt(9, book.getBookNo());
	        return pstmt.executeUpdate() > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DbcpBean.close(con, pstmt);
	    }
	    return false;
	}

	/* 반납 처리 */
	public boolean returnBook(int rentNo) {
	    boolean result = false;
	    String sql = "update rental_book set return_state = 1, return_date = now() "
	    		   + "where rent_no = ?";

	    try {
	        con = DbcpBean.getConnection();
	        pstmt = con.prepareStatement(sql);
	        pstmt.setInt(1, rentNo);

	        int row = pstmt.executeUpdate();
	        if (row > 0) {
	            result = true;
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        DbcpBean.close(con, pstmt, rs);
	    }

	    return result;
	}
	
	/* 모든 대여 목록 조회 (관리자용) */
	public Vector<RentalVo> allRentals() {
	    Vector<RentalVo> rentalList = new Vector<>();

	    String sql = "select r.rent_no, r.user_id, r.book_no, b.title, b.thumbnail, "
	               + "r.start_date, r.return_due, r.return_date, r.return_state "
	               + "from rental_book r "
	               + "join book b on r.book_no = b.book_no "
	               + "order by r.start_date desc";

	    try {
	        con = DbcpBean.getConnection();
	        pstmt = con.prepareStatement(sql);
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
	            book.setBookNo(rs.getInt("book_no"));
	            book.setTitle(rs.getString("title"));
	            book.setThumbnail(rs.getString("thumbnail"));
	            rental.setBook(book);

	            rentalList.add(rental);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        DbcpBean.close(con, pstmt, rs);
	    }

	    return rentalList;
	}

	

}
