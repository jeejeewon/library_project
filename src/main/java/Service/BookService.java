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
    
    // 도서의 총 개수를 반환
    public int bookCount() {
        return bookDao.bookCount();
    }

    public Vector<BookVo> booksByPage(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return bookDao.booksByPage(offset, pageSize);
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

    // 등록
    public boolean addBook(BookVo book) {
        return bookDao.addBook(book);
    }

    // 수정
    public boolean updateBook(BookVo book) {
        return bookDao.updateBook(book);
    }
    
    // 삭제
    public boolean deleteBook(int bookNo) {
        return bookDao.deleteBook(bookNo);
    }
    
	// 반납 처리
    public boolean returnBook(int rentNo) {
        return bookDao.returnBook(rentNo);
    }
    
    // 반납 대기
    public Vector<RentalVo> pendingRentals() {
        return bookDao.pendingRentals();
    }

    // 모든 대여 목록 (관리자용)
    public Vector<RentalVo> allRentals() {
        return bookDao.allRentals();
    }

    
}
