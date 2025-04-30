package Dao;

import java.sql.*;
import java.util.Vector;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import Vo.BookVo;

public class BookDAO {

    Connection con;
    PreparedStatement pstmt;
    ResultSet rs;

    // DB 연결 메소드
    public void getCon() {
        try {
            Context init = new InitialContext();
            DataSource ds = (DataSource) init.lookup("java:comp/env/jdbc/jspbeginner");
            con = ds.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 전체 도서 목록 조회
    public Vector<BookVo> selectAllBooks() {
        Vector<BookVo> bookList = new Vector<>();
        getCon(); // 커넥션 연결

        String sql = "select * from book order by reg_date desc";

        try {
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                BookVo book = new BookVo();

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

                bookList.add(book);
                
            }
            con.close(); // 리소스 정리
        } catch (SQLException e) {
            e.printStackTrace();
        } 

        return bookList;
    }

    public Vector<BookVo> selectBooksByCategory(String category) {
        return null;
    }

    public Vector<BookVo> searchBooks(String keyword) {
        Vector<BookVo> result = new Vector<>();
        getCon();

        String sql = "select * from book where title like ? or author like ? or publisher like ?";

        try {
            pstmt = con.prepareStatement(sql);
            String kw = "%" + keyword + "%";
            pstmt.setString(1, kw);
            pstmt.setString(2, kw);
            pstmt.setString(3, kw);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                BookVo book = new BookVo();
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
                
                result.add(book);
                
            }
            con.close(); // 리소스 정리
        } catch (Exception e) {
            e.printStackTrace();
        } 
        
        return result;
    }


    public Vector<BookVo> selectNewBooks() {
        return null;
    }

    public Vector<BookVo> selectBestBooks() {
        return null;
    }
    
    public BookVo selectBookByNo(int bookNo) {
        BookVo book = null;
        getCon(); // 커넥션 풀에서 연결 받기

        String sql = "SELECT * FROM book WHERE book_no = ?";

        try {
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
            
            con.close(); // 리소스 정리
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        return book;
    }

}
