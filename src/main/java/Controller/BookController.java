package Controller;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import Service.BookService;
import Vo.BookVo;

@WebServlet("/books/*")
public class BookController extends HttpServlet {

    private BookService bookService;

    @Override
    public void init() throws ServletException {
        // 서비스 객체 초기화
        bookService = new BookService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doHandle(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doHandle(request, response);
    }

    protected void doHandle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 요청 및 응답 한글 처리
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 요청 URI에서 /book 이후의 경로 추출
        String action = request.getPathInfo();
        System.out.println("요청한 2단계 주소: " + action); // 디버깅용

        String nextPage = null;

        // 메인화면 요청 처리 ("/Main")
        if (action.equals("/Main")) {
            nextPage = "/main.jsp";

        // 전체 도서 목록
        } else if (action.equals("/bookList.do")) {
            Vector<BookVo> vector = bookService.getAllBooks(); // 전체 도서 불러오기
            request.setAttribute("v", vector); // 도서 목록을 request에 담기
            request.setAttribute("center", "/book/bookList.jsp"); // center 영역에 들어갈 jsp
            nextPage = "/main.jsp";

        // 카테고리별 도서 목록
        } else if (action.equals("/categoryList.do")) {
            String category = request.getParameter("category");
            Vector<BookVo> vector = bookService.getBooksByCategory(category);
            request.setAttribute("category", vector);
            request.setAttribute("center", "/book/categoryList.jsp");
            nextPage = "/main.jsp";            
            
        // 도서 검색 화면    
        } else if (action.equals("/searchForm.do")) {
            request.setAttribute("center", "/book/bookSearchForm.jsp");
            nextPage = "/main.jsp";

        // 도서 검색 결과 화면
        } else if (action.equals("/bookSearch.do")) {
            String keyword = request.getParameter("keyword");
            Vector<BookVo> result = bookService.searchBooks(keyword);
            request.setAttribute("keyword", keyword); // 검색어 유지
            request.setAttribute("v", result);
            request.setAttribute("center", "/book/bookSearch.jsp");
            nextPage = "/main.jsp";        

        // 신착 도서
        } else if (action.equals("/newBook.do")) {
            Vector<BookVo> newBooks = bookService.getNewBooks();
            request.setAttribute("v", newBooks);
            request.setAttribute("center", "/book/newBooks.jsp");
            nextPage = "/main.jsp";

        // 인기 도서
        } else if (action.equals("/bestBook.do")) {
            Vector<BookVo> bestBooks = bookService.getBestBooks();
            request.setAttribute("v", bestBooks);
            request.setAttribute("center", "/book/bestBooks.jsp");
            nextPage = "/main.jsp";

        // 도서 상세 정보 조회
        } else if (action.equals("/bookInfo.do")) {
            int bookNo = Integer.parseInt(request.getParameter("bookNo"));
            BookVo book = bookService.getBook(bookNo); // 선택한 도서 정보
            Vector<BookVo> relatedBooks = bookService.getBooksByCategory(book.getCategory()); // 같은 카테고리 추천
            request.setAttribute("book", book);
            request.setAttribute("relatedBooks", relatedBooks);
            request.setAttribute("center", "/book/bookInfo.jsp");
            nextPage = "/main.jsp";

        } else if (action.equals("/rentalResult.do")) {
            // TODO: 대여 결과 처리
            request.setAttribute("center", "/book/rentalResult.jsp");
            nextPage = "/main.jsp";

        } else if (action.equals("/bookRentalConfirm.do")) {
            // TODO: 내 대여 내역 확인
            request.setAttribute("center", "/book/bookRentalConfirm.jsp");
            nextPage = "/main.jsp";

        } else if (action.equals("/addBook.do")) {
            // TODO: 도서 등록 처리
            request.setAttribute("center", "/book/addBook.jsp");
            nextPage = "/main.jsp";

        } else if (action.equals("/returnBook.do")) {
            // TODO: 도서 반납 처리
            request.setAttribute("center", "/book/returnBook.jsp");
            nextPage = "/main.jsp";

        } else {
            // 예외 상황 시 에러 페이지로
            request.setAttribute("errMsg", "존재하지 않는 요청입니다.");
            request.setAttribute("center", "/error/error.jsp");
            nextPage = "/main.jsp";
        }

        if (nextPage != null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher(nextPage);
            dispatcher.forward(request, response);
        }
    }
}
