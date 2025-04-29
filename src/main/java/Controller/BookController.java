package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Service.BookService;
import Vo.BookVo;
import Vo.RentalVo;

@WebServlet("/book/*")
public class BookController extends HttpServlet {

    private BookService bookService;

    @Override
    public void init() throws ServletException {
        // BookService 객체 초기화
        bookService = new BookService();
        System.out.println("BookController 초기화 : BookService 객체 생성 완료");
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

        // 요청 처리 기본 설정
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String action = request.getPathInfo(); // 요청 URL 분석
        System.out.println("action : " + action);

        String nextPage = null; // 이동할 JSP 경로

        // 세션에서 로그인 아이디 확인
        HttpSession session = request.getSession();
        String id = (String) session.getAttribute("user_id");
        boolean isMember = (id != null);

        // 메인 페이지 이동
        if (action == null || action.equals("/main")) {
            nextPage = "/main.jsp";

        // center 영역 변경 요청
        } else if (action.equals("/book")) {
            String center = request.getParameter("center");
            request.setAttribute("center", center);
            nextPage = "/main.jsp";

        // 전체 도서 목록 조회
        } else if (action.equals("/bookList.do")) {
            Vector<BookVo> vector = bookService.getAllBooks();
            request.setAttribute("v", vector);
            request.setAttribute("center", "/book/list.jsp");
            nextPage = "/main.jsp";

        // 카테고리별 도서 목록 조회
        } else if (action.equals("/categoryList.do")) {
            String category = request.getParameter("category");
            Vector<BookVo> vector = bookService.getBooksByCategory(category);
            request.setAttribute("category", vector);
            request.setAttribute("center", "/book/categorylist.jsp");
            nextPage = "/main.jsp";

        // 도서 검색
        } else if (action.equals("/bookSearch.do")) {
            String keyword = request.getParameter("keyword");
            Vector<BookVo> result = bookService.searchBooks(keyword);
            request.setAttribute("v", result);
            request.setAttribute("center", "/book/bookSearch.jsp");
            nextPage = "/main.jsp";

        // 신착 도서 조회
        } else if (action.equals("/newBook.do")) {
            Vector<BookVo> newBooks = bookService.getNewBooks();
            request.setAttribute("v", newBooks);
            request.setAttribute("center", "/book/newBooks.jsp");
            nextPage = "/main.jsp";

        // 인기 도서 조회
        } else if (action.equals("/bestBook.do")) {
            Vector<BookVo> bestBooks = bookService.getBestBooks();
            request.setAttribute("v", bestBooks);
            request.setAttribute("center", "/book/bestBooks.jsp");
            nextPage = "/main.jsp";

        // 도서 상세 정보 조회
        } else if (action.equals("/bookInfo.do")) {
            int bookNo = Integer.parseInt(request.getParameter("bookNo"));
            BookVo book = bookService.getBook(bookNo);
            Vector<BookVo> relatedBooks = bookService.getBooksByCategory(book.getCategory());
            request.setAttribute("book", book);
            request.setAttribute("relatedBooks", relatedBooks);
            request.setAttribute("center", "/book/info.jsp");
            nextPage = "/main.jsp";

        // 도서 대여 화면
        } else if (action.equals("/rentalBook.do")) {
            if (!isMember) {
                nextPage = "/member/login.jsp"; // 비회원이면 로그인
            } else {
                int bookNo = Integer.parseInt(request.getParameter("bookNo"));
                BookVo book = bookService.getBook(bookNo);
                request.setAttribute("book", book);
                request.setAttribute("center", "/book/rentalForm.jsp");
                nextPage = "/main.jsp";
            }

        // 도서 대여 확정
        } else if (action.equals("/bookOrder.do")) {
            if (!isMember) {
                nextPage = "/member/login.jsp";
            } else {
                int bookNo = Integer.parseInt(request.getParameter("bookNo"));
                bookService.rentBook(id, bookNo);
                response.sendRedirect(request.getContextPath() + "/book/rentalResult.do");
                return;
            }

        // 도서 대여 완료 결과
        } else if (action.equals("/rentalResult.do")) {
            Vector<RentalVo> rentedBooks = bookService.getRentalListByUser(id);
            request.setAttribute("rentalList", rentedBooks);
            request.setAttribute("center", "/book/rentalResult.jsp");
            nextPage = "/main.jsp";

        // 내 대여 상세 내역 조회
        } else if (action.equals("/bookRentalConfirm.do")) {
            Vector<RentalVo> rentalDetails = bookService.getRentalDetailByUser(id);
            request.setAttribute("rentalDetails", rentalDetails);
            request.setAttribute("center", "/book/rentalConfirm.jsp");
            nextPage = "/main.jsp";

        // 도서 등록 (관리자만 가능)
        } else if (action.equals("/addBook.do")) {
            if (!"admin".equals(id)) {
                nextPage = "/member/login.jsp";
            } else {
                bookService.addBookFromRequest(request);
                response.sendRedirect(request.getContextPath() + "/book/bookList.do");
                return;
            }

        // 도서 반납 처리 (관리자만 가능)
        } else if (action.equals("/returnBook.do")) {
            if (!"admin".equals(id)) {
                nextPage = "/member/login.jsp";
            } else {
                int rentNo = Integer.parseInt(request.getParameter("rentNo"));
                int bookNo = Integer.parseInt(request.getParameter("bookNo"));
                bookService.returnBook(rentNo, bookNo);
                response.sendRedirect(request.getContextPath() + "/book/rentalResult.do");
                return;
            }
        }

        // 최종 페이지 이동 (forward)
        if (nextPage != null) {
            System.out.println("페이지 이동(forward) 처리: " + nextPage);
            RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
            dispatch.forward(request, response);
        } else {
            System.out.println("nextPage가 null입니다.");
        }
    }
}
