package Service;

import java.util.Vector;

import Dao.BookDAO;
import Vo.BookVo;

public class BookService {

    // DAO 객체 생성
    private BookDAO bookDao;

    public BookService() {
        // DAO 초기화
        bookDao = new BookDAO();
    }

    // 전체 도서 목록 조회
    public Vector<BookVo> getAllBooks() {
        return bookDao.selectAllBooks();
    }

    // 카테고리별 도서 목록 조회
    public Vector<BookVo> getBooksByCategory(String category) {
        return bookDao.selectBooksByCategory(category);
    }

    // 키워드로 도서 검색
    public Vector<BookVo> searchBooks(String keyword) {
        return bookDao.searchBooks(keyword);
    }

    // 신착 도서 목록 조회
    public Vector<BookVo> getNewBooks() {
        return bookDao.selectNewBooks();
    }

    // 인기 도서 목록 조회
    public Vector<BookVo> getBestBooks() {
        return bookDao.selectBestBooks();
    }

    // 도서 번호로 상세 정보 조회
    public BookVo getBook(int bookNo) {
        return bookDao.selectBookByNo(bookNo);
    }

    // 메소드도 추가 중
}
