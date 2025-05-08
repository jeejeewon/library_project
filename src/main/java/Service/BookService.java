package Service;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import Dao.BookDAO;
import Vo.BookVo;
import Vo.RentalVo;

public class BookService {

    // DAO 객체 생성
    private BookDAO bookDao;

    public BookService() {
        // DAO 초기화
        bookDao = new BookDAO();
    }

    // 전체 도서 목록 조회
    public Vector<BookVo> allBooks() {
        return bookDao.allBooks();
    }

    // 도서 상세 정보 조회
    public BookVo bookDetail(int bookNo) {
        return bookDao.bookDetail(bookNo);
    }

    // 도서 검색
    public Vector<BookVo> searchBooks(String keyword) {
        return bookDao.searchBooks(keyword);
    }

    // 신착 도서 목록 조회
    public Vector<BookVo> newBooks() {
        return bookDao.newBooks();
    }

    // 인기 도서 목록 조회
    public Vector<BookVo> bestBooks() {
        return bookDao.bestBooks();
    }

    // 대출 처리 메서드 추가
    public boolean rentBook(String userId, int bookNo) {
        return bookDao.rentBook(userId, bookNo);
    }

    // 내 대여 내역 확인
    public Vector<RentalVo> myRentals(String userId) {
		return bookDao.myRentals(userId);
	}

    // 등록/수정 (관리자)
    public boolean updateBook(HttpServletRequest request) {

        String bookNoStr = request.getParameter("bookNo");

        BookVo book = new BookVo();
        book.setTitle(request.getParameter("title"));
        book.setAuthor(request.getParameter("author"));
        book.setPublisher(request.getParameter("publisher"));
        book.setPublishYear(request.getParameter("publishYear"));
        book.setCategory(request.getParameter("category"));
        book.setBookInfo(request.getParameter("bookInfo"));
        book.setIsbn(request.getParameter("isbn"));
        book.setThumbnail(request.getParameter("thumbnail"));

        if (bookNoStr != null && !bookNoStr.isEmpty()) {
            book.setBookNo(Integer.parseInt(bookNoStr));
            return bookDao.updateBook(book);
        } else {
            return bookDao.insertBook(book);
        }
    }

	// 반납 처리
    public boolean returnBook(int rentNo) {
        return bookDao.returnBook(rentNo);
    }

    // 모든 대여 목록 (관리자용)
    public Vector<RentalVo> allRentals() {
        return bookDao.allRentals();
    }
    
}
