package Service;

import java.util.Vector;

import Dao.BookDAO;
import Vo.BookVo;
import Vo.RentalVo;

public class BookService {

    private BookDAO bookDao;

    public BookService() {
        bookDao = new BookDAO();
    }

    // 전체 도서 개수
    public int bookCount() {
        return bookDao.bookCount();
    }    

    // 도서 상세
    public BookVo bookDetail(int bookNo) {
        return bookDao.getBook(bookNo);
    }    

    // 검색어 포함 도서 수
    public int bookCount(String keyword) {
        return bookDao.bookCount(keyword);
    }

    // 전체 도서 목록 페이징
    public Vector<BookVo> booksByPage(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return bookDao.booksByPage(offset, pageSize);
    }

    // 검색 도서 목록 페이징
    public Vector<BookVo> booksByPage(String keyword, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return bookDao.booksByPage(keyword, offset, pageSize);
    }

    // 신착 도서 전체 수
    public int newBooksCount() {
        return bookDao.newBooksCount();
    }

    // 신착 도서 페이징 목록
    public Vector<BookVo> newBooksByPage(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return bookDao.newBooksByPage(offset, pageSize);
    }

    // 인기 도서 전체 수
    public int bestBooksCount() {
        return bookDao.bestBooksCount();
    }
    
    // 인기 도서 페이징 목록
    public Vector<BookVo> bestBooksByPage(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return bookDao.bestBooksByPage(offset, pageSize);
    }

    // 도서 대여
    public boolean rentBook(String userId, int bookNo) {
        // 현재 대여 중인 도서 수 조회
        int currentRentCount = bookDao.countCurrentRentals(userId);

        // 5권 이상 대여 불가
        if (currentRentCount >= 5) {
            return false;
        }
        return bookDao.rentBook(userId, bookNo);
    }
    
    // 사용자별 대여 전체 수
    public int myRentalCount(String userId) {
        return bookDao.countRentalsByUser(userId);
    }

    // 사용자별 대여 페이징 목록
    public Vector<RentalVo> rentalsByUserByPage(String userId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return bookDao.getRentalsByUserByPage(userId, offset, pageSize);
    }

    // 도서 등록
    public boolean addBook(BookVo book) {
        return bookDao.addBook(book);
    }

    // isbn 중복 확인
    public boolean isIsbnExists(String isbn) {
        return bookDao.isIsbnExists(isbn);
    }

    // 도서 수정
    public boolean updateBook(BookVo book) {
        return bookDao.updateBook(book);
    }

    // 도서 삭제
    public boolean deleteBook(int bookNo) {
        return bookDao.deleteBook(bookNo);
    }

    // 반납 처리
    public boolean returnBook(int rentNo) {
        return bookDao.processReturn(rentNo);
    }

    // 반납 처리 목록
    public Vector<RentalVo> getPendingRentals() {
        return bookDao.getPendingRentals();
    }
   
    // 반납 처리 수
    public int pendingRentalCount() {
        return bookDao.countPendingRentals();
    }

    // 반납 처리 페이징 목록
    public Vector<RentalVo> pendingRentalsByPage(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return bookDao.selectPendingRentalsByPage(offset, pageSize);
    }

    // 전체 대여 수
    public int allRentalCount() {
        return bookDao.allRentalCount();
    }
    
    // 전체 대여 페이징 목록
    public Vector<RentalVo> allRentalsByPage(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return bookDao.allRentalsByPage(offset, pageSize);
    }


}