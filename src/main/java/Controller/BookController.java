package Controller;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.http.HttpSession;

import Service.BookService;
import Vo.BookVo;
import Vo.RentalVo;

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
            Vector<BookVo> vector = bookService.allBooks(); // 전체 도서 불러오기
            request.setAttribute("v", vector); // 도서 목록을 request에 담기
            request.setAttribute("center", "/book/bookList.jsp"); // center 영역에 들어갈 jsp
            nextPage = "/main.jsp";

        // 도서 상세 정보 조회
        } else if (action.equals("/bookDetail.do")) {
            int bookNo = Integer.parseInt(request.getParameter("bookNo"));
            BookVo book = bookService.bookDetail(bookNo); // 선택한 도서 정보
            request.setAttribute("book", book);
            request.setAttribute("center", "/book/bookDetail.jsp");
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
        } else if (action.equals("/newBooks.do")) {
            Vector<BookVo> newBooks = bookService.newBooks();
            request.setAttribute("v", newBooks);
            request.setAttribute("center", "/book/newBookList.jsp");
            nextPage = "/main.jsp";

        // 인기 도서
        } else if (action.equals("/bestBooks.do")) {
            Vector<BookVo> bestBooks = bookService.bestBooks();
            request.setAttribute("v", bestBooks);
            request.setAttribute("center", "/book/bestBookList.jsp");
            nextPage = "/main.jsp";
   
        // 도서 대여 하기    
        } else if (action.equals("/rentalBook.do")) {
        	
        	// 세션 가져오기
        	HttpSession session = request.getSession();
        	        	
        	// 로그인 아이디 확인 하기 위해 세션에서 로그인한 아이디 꺼내오기
            String userId = (String) session.getAttribute("id");
            
            //미로그인 상태에서 대여 시도 했다면
            if (userId == null) {
                // 로그인 안했으면 로그인 페이지로 리다이렉트
                response.sendRedirect(request.getContextPath() + "/member/login.jsp");
                return; // 더 이상 진행하지 않음
            }

            // bookNo 파라미터 가져오기
            int bookNo = Integer.parseInt(request.getParameter("bookNo"));

            // 대출 처리 (BookService 호출)
            boolean isRented = bookService.rentBook(userId, bookNo);

            if (isRented) {
                request.setAttribute("message", "대출이 완료되었습니다.");
            } else {
                request.setAttribute("message", "대출 처리에 실패했습니다. 이미 대출된 도서일 수 있습니다.");
            }

            // 대출 결과 페이지
            request.setAttribute("center", "/book/rentalResult.jsp");
            nextPage = "/main.jsp"; 
            
        // 내 대여 내역 확인    
        } else if (action.equals("/rentalConfirm.do")) {

            HttpSession session = request.getSession();
            String userId = (String) session.getAttribute("id");

            if (userId == null) {
                // 로그인 안 했으면 로그인 페이지로
                response.sendRedirect(request.getContextPath() + "/member/login.jsp");
                return;
            }

            // 로그인한 사용자의 대여 목록 가져오기
            Vector<RentalVo> myRentals = bookService.myRentals(userId);

            request.setAttribute("rentals", myRentals);
            request.setAttribute("center", "/book/rentalConfirm.jsp");
            nextPage = "/main.jsp";

        // 도서 등록 및 수정 관리자만
        }  else if (action.equals("/addBook.do")) {

            HttpSession session = request.getSession();
            String userId = (String) session.getAttribute("id");

            if (userId == null || !userId.equals("admin")) {
                response.sendRedirect(request.getContextPath() + "/member/login.jsp");
                return;
            }

            // POST 방식이면 등록/수정 처리
            if (request.getMethod().equalsIgnoreCase("POST")) {
                boolean result = bookService.updateBook(request);
                request.setAttribute("message", result ? "등록/수정 성공" : "실패했습니다.");
            }

            request.setAttribute("center", "/book/addBook.jsp");
            nextPage = "/main.jsp";

        // 도서 반납 처리 관리자만
        } else if (action.equals("/returnBook.do")) {

            HttpSession session = request.getSession();
            String userId = (String) session.getAttribute("id");

            if (userId == null || !userId.equals("admin")) {
                response.sendRedirect(request.getContextPath() + "/member/login.jsp");
                return;
            }

            if (request.getMethod().equalsIgnoreCase("POST")) {
                int rentNo = Integer.parseInt(request.getParameter("rentNo"));
                boolean result = bookService.returnBook(rentNo);
                request.setAttribute("message", result ? "반납 완료!" : "반납 실패!");
            }

            request.setAttribute("rentalList", bookService.allRentals());
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
