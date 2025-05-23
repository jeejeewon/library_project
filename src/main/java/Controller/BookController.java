package Controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import Service.BookService;
import Service.boardService;
import Vo.BookVo;
import Vo.RentalVo;
import Vo.boardVO;

@WebServlet("/books/*")
public class BookController extends HttpServlet {
    
    private BookService bookService;

    @Override
    public void init() throws ServletException {
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

    // 모든 요청을 처리하는 중앙 메소드
    protected void doHandle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        
        // 2단계요청주소를 추출
        String action = request.getPathInfo();
        // 콘솔창에 주소 출력(디버깅용)
        System.out.println("요청한 2단계 주소:" + action);
        
        String nextPage = null;
        HttpSession session = request.getSession();

        
        // 메인화면 요청 처리 ("/Main")
        if (action.equals("/Main")) {
            nextPage = "/main.jsp";

        // 전체 도서 리스트(페이징 처리)
        } else if (action.equals("/bookList.do")) {
            paginateAndSetList(request, "list");
            request.setAttribute("center", "/book/bookList.jsp");
            nextPage = "/main.jsp";

        // 도서 상세 정보  
        } else if (action.equals("/bookDetail.do")) {
            int bookNo = parseIntOrDefault(request.getParameter("bookNo"), 0);
            BookVo book = bookService.bookDetail(bookNo);
            
            boardService reviewService = new boardService(); //boardService 임포트 해주세요
            List<boardVO> reviewList = reviewService.getReviewsByBookNo(bookNo);
            request.setAttribute("book", book);
            request.setAttribute("reviewList", reviewList);
                        
            request.setAttribute("center", "/book/bookDetail.jsp");
            nextPage = "/main.jsp";
            
        // 도서 검색 화면 
        } else if (action.equals("/searchForm.do")) {
            request.setAttribute("center", "/book/bookSearchForm.jsp");
            nextPage = "/main.jsp";

        // 도서 검색 완료 화면(페이징 처리)
        } else if (action.equals("/bookSearch.do")) {
            String keyword = request.getParameter("keyword");
            paginateAndSetList(request, "search", keyword);
            request.setAttribute("keyword", keyword);
            request.setAttribute("center", "/book/bookSearch.jsp");
            nextPage = "/main.jsp";

        // 신착 도서 화면(페이징 처리)
        } else if (action.equals("/newBooks.do")) {
            paginateAndSetList(request, "new");
            request.setAttribute("center", "/book/newBookList.jsp");
            nextPage = "/main.jsp";

        // 인기 도서 화면(페이징 처리)  
        } else if (action.equals("/bestBooks.do")) {
            paginateAndSetList(request, "best", null);
            request.setAttribute("center", "/book/bestBookList.jsp");
            nextPage = "/main.jsp";

        // 대여 확인 화면
        } else if (action.equals("/confirmRental.do")) {
            int bookNo = parseIntOrDefault(request.getParameter("bookNo"), 0);
            BookVo book = bookService.bookDetail(bookNo);

            if (book == null) {
                response.sendRedirect(request.getContextPath() + "/books/bookList.do");
                return;
            }

            request.setAttribute("book", book);
            request.setAttribute("center", "/book/confirmRental.jsp");
            nextPage = "/main.jsp";

        // 도서 대여 처리
        } else if (action.equals("/rentalBook.do")) {
            if (!request.getMethod().equalsIgnoreCase("POST")) {
                response.sendRedirect(request.getContextPath() + "/books/bookList.do");
                return;
            }

            String userId = (String) session.getAttribute("id");
            int bookNo = parseIntOrDefault(request.getParameter("bookNo"), 0);

            if (userId == null) {
                session.setAttribute("redirectAfterLogin", "/books/confirmRental.do?bookNo=" + bookNo);
                request.setAttribute("center", "/members/login.jsp");
                nextPage = "/main.jsp";
            } else {
                boolean isRented = bookService.rentBook(userId, bookNo);
                request.setAttribute("message", isRented ? "대여가 완료되었습니다." : "대여는 5권까지 가능합니다.");
                request.setAttribute("center", "/book/rentalResult.jsp");
                nextPage = "/main.jsp";
            }

        // 내 대여 목록 화면(페이징 처리) 
        } else if (action.equals("/myRentalList.do")) {
            String userId = (String) session.getAttribute("id");

            if (userId == null) {
                session.setAttribute("redirectAfterLogin", "/books/myRentalList.do");
                request.setAttribute("center", "/members/login.jsp");
                nextPage = "/main.jsp";
            } else {
                int page = parseIntOrDefault(request.getParameter("page"), 1);
                int pageSize = 8;
                int totalCount = bookService.myRentalCount(userId);
                int totalPage = (int) Math.ceil((double) totalCount / pageSize);
                Vector<RentalVo> list = bookService.rentalsByUserByPage(userId, page, pageSize);

                request.setAttribute("rentalList", list);
                request.setAttribute("currentPage", page);
                request.setAttribute("totalCount", totalCount);
                request.setAttribute("totalPage", totalPage);
                request.setAttribute("pageSize", pageSize);
                request.setAttribute("center", "/book/myRentalList.jsp");
                nextPage = "/main.jsp";
            }

	    // 관리자 화면(admin only)    
	    } else if (action.equals("/adminBook.do")) {
	        if (!isAdmin(request)) return;
	        request.setAttribute("center", "/book/adminBook.jsp");
	        nextPage = "/main.jsp";
	
	    // 신규 도서 등록 폼 화면(admin only)   
	    } else if (action.equals("/addBookForm.do")) {
	        if (!isAdmin(request)) return;
	        request.setAttribute("center", "/book/addBook.jsp");
	        nextPage = "/main.jsp";
	
	    // 신규 도서 등록 완료(admin only)
	    } else if (action.equals("/addBook.do")) {
	        if (!isAdmin(request)) return;
	
	        try {
	            BookVo book = bookFromMultipart(request);
	
	            if (bookService.isIsbnExists(book.getIsbn())) {
	                // 중복 ISBN 알림 후 이전 페이지로 이동
	                request.setAttribute("script", "<script>alert('이미 등록된 ISBN입니다.'); history.back();</script>");
	                request.setAttribute("center", "/book/addBook.jsp");
	                nextPage = "/main.jsp";
	                return;
	            }
	
	            boolean isAdded = bookService.addBook(book);
	            request.setAttribute("message", isAdded ? "도서 등록 성공" : "도서 등록 실패");
	            paginateAndSetList(request, "list");
	            request.setAttribute("center", "/book/bookList.jsp");
	            nextPage = "/main.jsp";
	
	        } catch (Exception e) {
	            e.printStackTrace();
	            request.setAttribute("message", "도서 등록 중 오류 발생");
	            request.setAttribute("center", "/book/addBook.jsp");
	            nextPage = "/main.jsp";
	        }
	        
	    // 기존 도서 수정/삭제 리스트 화면(admin only, 페이징 처리)   
	    } else if (action.equals("/updateBook.do")) {
	        if (!isAdmin(request)) return;
	        boolean isUpdated = multipartBookUpload(request, false);
	        int page = parseIntOrDefault(request.getParameter("page"), 1);
	        request.setAttribute("fromUpdate", isUpdated); 
	        request.setAttribute("message", isUpdated ? "도서 정보가 수정되었습니다." : "도서 정보 수정에 실패했습니다.");
	        paginateAndSetList(request, "list", null, page);
	        request.setAttribute("center", "/book/updateBook.jsp");
	        nextPage = "/main.jsp";
	         
	    // 도서 수정(admin only)
	    } else if (action.equals("/editBook.do")) {
	        if (!isAdmin(request)) return;
	        int bookNo = parseIntOrDefault(request.getParameter("bookNo"), 0);
	        int currentPage = parseIntOrDefault(request.getParameter("page"), 1);
	        BookVo book = bookService.bookDetail(bookNo);
	        request.setAttribute("book", book);
	        request.setAttribute("currentPage", currentPage);
	        request.setAttribute("center", "/book/editBook.jsp");
	        nextPage = "/main.jsp";
	
	    // 도서 삭제(admin only)    
	    } else if (action.equals("/deleteBook.do")) {
	        if (!isAdmin(request)) return;
	        int bookNo = parseIntOrDefault(request.getParameter("bookNo"), 0);
	        int page = parseIntOrDefault(request.getParameter("page"), 1);
	        boolean result = bookNo > 0 && bookService.deleteBook(bookNo);
	        request.setAttribute("message", result ? "도서가 삭제되었습니다." : "삭제 실패 또는 존재하지 않는 도서입니다.");
	        paginateAndSetList(request, "list", null, page);
	        request.setAttribute("center", "/book/updateBook.jsp");
	        nextPage = "/main.jsp";
	
	    // 도서 반납 화면(페이징 처리)    
	    } else if (action.equals("/returnBook.do")) {
	        if (!isAdmin(request)) return;
	
	        int rentNo = parseIntOrDefault(request.getParameter("rentNo"), 0);
	        int page = parseIntOrDefault(request.getParameter("page"), 1);
	        int pageSize = 8;
	
	        if (rentNo > 0) {
	            boolean success = bookService.returnBook(rentNo);
	            request.setAttribute("message", success ? "반납이 완료되었습니다." : "반납 처리에 실패했습니다.");
	        }
	
	        int totalCount = bookService.pendingRentalCount();
	        int totalPage = (int) Math.ceil((double) totalCount / pageSize);
	        Vector<RentalVo> rentalList = bookService.pendingRentalsByPage(page, pageSize);
	
	        request.setAttribute("rentalList", rentalList);
	        request.setAttribute("currentPage", page);
	        request.setAttribute("totalCount", totalCount);
	        request.setAttribute("pageSize", pageSize);
	        request.setAttribute("totalPage", totalPage);
	        request.setAttribute("center", "/book/returnBook.jsp");
	        nextPage = "/main.jsp";
	
	    // 전체 대여 도서 화면(페이징 처리)    
	    } else if (action.equals("/allRental.do")) {
	        if (!isAdmin(request)) return;
	
	        int page = parseIntOrDefault(request.getParameter("page"), 1);
	        int pageSize = 5;
	        int totalCount = bookService.allRentalCount();
	        int totalPage = (int) Math.ceil((double) totalCount / pageSize);
	        
	        Vector<RentalVo> rentalList = bookService.allRentalsByPage(page, pageSize);
	
	        request.setAttribute("rentalList", rentalList);
	        request.setAttribute("currentPage", page);
	        request.setAttribute("totalCount", totalCount);
	        request.setAttribute("pageSize", pageSize);
	        request.setAttribute("totalPage", totalPage);
	        request.setAttribute("center", "/book/allRentalList.jsp");
	        nextPage = "/main.jsp";
	        
	    // 위의 *.do 에 해당하지 않는 경우 error 메세지       
	    } else {
            request.setAttribute("errMsg", "존재하지 않는 요청입니다.");
            nextPage = "/main.jsp";
        }

        // nextPage변수에 값이 할당된 경우
        // 직접 응답하지 않고 JSP(VIEW)로 포워딩 해야하는 경우
        if (nextPage != null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher(nextPage);
            dispatcher.forward(request, response);
        }
    }

    // 관리자 여부 확인 메서드
    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("id");
        if (userId == null || !"admin".equals(userId)) {
            request.setAttribute("center", "/members/login.jsp");
            request.setAttribute("message", "관리자만 접근 가능합니다.");
            return false;
        }
        return true;
    }

    // 페이징 처리 - 기본 리스트용
    private void paginateAndSetList(HttpServletRequest request, String type) {
        int page = parseIntOrDefault(request.getParameter("page"), 1);
        paginateAndSetList(request, type, null, page);
    }

    // 페이징 처리 - 검색 키워드 포함
    private void paginateAndSetList(HttpServletRequest request, String type, String keyword) {
        int page = parseIntOrDefault(request.getParameter("page"), 1);
        paginateAndSetList(request, type, keyword, page);
    }

    // 페이징 처리 - 실제 로직 수행 메서드
    private void paginateAndSetList(HttpServletRequest request, String type, String keyword, int page) {
        int pageSize = 8;
        int totalCount;
        Vector<BookVo> bookList;

        switch (type) {
            case "search":
                totalCount = bookService.bookCount(keyword);
                bookList = bookService.booksByPage(keyword, page, pageSize);
                break;
            case "new":
                totalCount = bookService.newBooksCount();
                bookList = bookService.newBooksByPage(page, pageSize);
                break;
            case "best":
                totalCount = bookService.bestBooksCount();
                bookList = bookService.bestBooksByPage(page, pageSize);
                break;
            default:
                totalCount = bookService.bookCount();
                bookList = bookService.booksByPage(page, pageSize);
                break;
        }

        int totalPage = (int) Math.ceil((double) totalCount / pageSize);
        request.setAttribute("v", bookList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("pageSize", pageSize);
    }

    // 문자열을 정수로 안전하게 변환하는 메서드
    private int parseIntOrDefault(String param, int defaultValue) {
        try {
            return Integer.parseInt(param);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // multipart/form-data를 통한 도서 정보 추출 메서드
    private BookVo bookFromMultipart(HttpServletRequest request) throws Exception {
        BookVo book = new BookVo();

        if (!ServletFileUpload.isMultipartContent(request)) return book;

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setRepository(new File(request.getServletContext().getRealPath("/book/img")));
        ServletFileUpload upload = new ServletFileUpload(factory);
        List<FileItem> items = upload.parseRequest(request);

        for (FileItem item : items) {
            if (item.isFormField()) {
                String name = item.getFieldName();
                String value = item.getString("UTF-8");
                switch (name) {
                    case "bookNo": book.setBookNo(parseIntOrDefault(value, 0)); break;
                    case "title": book.setTitle(value); break;
                    case "author": book.setAuthor(value); break;
                    case "publisher": book.setPublisher(value); break;
                    case "publishYear": book.setPublishYear(Integer.parseInt(value)); break;
                    case "isbn": book.setIsbn(value); break;
                    case "category": book.setCategory(value); break;
                    case "bookInfo": book.setBookInfo(value); break;
                    case "rentalState": book.setRentalState(parseIntOrDefault(value, 0)); break;
                    case "existingThumbnail":
                        if (book.getThumbnail() == null || book.getThumbnail().isEmpty()) {
                            book.setThumbnail(value);
                        }
                        break;
                }
            } else {
                String fileName = new File(item.getName()).getName();
                if (!fileName.isEmpty()) {
                    String uploadPath = request.getServletContext().getRealPath("/book/img");
                    File uploadedFile = new File(uploadPath + File.separator + fileName);
                    item.write(uploadedFile);
                    book.setThumbnail("book/img/" + fileName);
                }
            }
        }

        return book;
    }

    // 도서 등록 또는 수정 처리 메서드
    private boolean multipartBookUpload(HttpServletRequest request, boolean isAdd) {
        try {
            BookVo book = bookFromMultipart(request);
            if (book == null) return false;
            return isAdd ? bookService.addBook(book) : bookService.updateBook(book);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
