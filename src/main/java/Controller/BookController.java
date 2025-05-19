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
import Vo.BookVo;
import Vo.RentalVo;

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
        
        //2단계요청주소를 추출
		String action = request.getPathInfo();
		//콘솔창에 주소 출력(디버깅용)
		System.out.println("요청한 2단계 주소:" + action); 
        String nextPage = null;

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
            int bookNo = Integer.parseInt(request.getParameter("bookNo"));
            BookVo book = bookService.bookDetail(bookNo);
            request.setAttribute("book", book);
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
            // GET 방식 접근 방지
            if (!request.getMethod().equalsIgnoreCase("POST")) {
                response.sendRedirect(request.getContextPath() + "/books/bookList.do");
                return;
            }

            HttpSession session = request.getSession();
            String userId = (String) session.getAttribute("id");

            // 로그인 안된 경우 로그인 페이지로
            if (userId == null) {
                response.sendRedirect(request.getContextPath() + "/members/login.jsp");
                return;
            }

            // 도서 번호 파라미터 확인
            int bookNo = parseIntOrDefault(request.getParameter("bookNo"), 0);
            boolean isRented = bookService.rentBook(userId, bookNo); // 트랜잭션 처리

            // 메시지 설정 및 결과 페이지 이동
            request.setAttribute("message", isRented ? "대여가 완료되었습니다." : "대여 처리에 실패했습니다.");
            request.setAttribute("center", "/book/rentalResult.jsp");
            nextPage = "/main.jsp";  
            
        // 내 대여 목록 화면(페이징 처리)    
        } else if (action.equals("/myRentalList.do")) {
            HttpSession session = request.getSession();
            String userId = (String) session.getAttribute("id");

            if (userId == null) {
                response.sendRedirect(request.getContextPath() + "/members/login.jsp");
                return;
            }

            int page = parseIntOrDefault(request.getParameter("page"), 1);
            int pageSize = 8;
            int totalCount = bookService.myRentalCount(userId);
            int totalPage = (int) Math.ceil((double) totalCount / pageSize);

            Vector<RentalVo> myList = bookService.rentalsByUserByPage(userId, page, pageSize);

            request.setAttribute("rentalList", myList);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("totalPage", totalPage);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("center", "/book/myRentalList.jsp");
            nextPage = "/main.jsp";
     
        // 관리자 화면(admin only)    
        } else if (action.equals("/adminBook.do")) {
            if (!isAdmin(request)) return;
            request.setAttribute("center", "/book/adminBook.jsp");
            nextPage = "/main.jsp";

        // 신간 도서 등록 폼 화면(admin only)   
        } else if (action.equals("/addBookForm.do")) {
            if (!isAdmin(request)) return;
            request.setAttribute("center", "/book/addBook.jsp");
            nextPage = "/main.jsp";

        // 신간 도서 등록 완료(admin only)
        } else if (action.equals("/addBook.do")) {
            if (!isAdmin(request)) return;
            boolean isAdded = handleMultipartBookUpload(request, true);
            request.setAttribute("message", isAdded ? "도서 등록 성공" : "도서 등록 실패");
            request.setAttribute("center", "/book/addBook.jsp");
            nextPage = "/main.jsp";

        // 기존 도서 수정/삭제 리스트 화면(admin only, 페이징 처리)   
        } else if (action.equals("/updateBook.do")) {
            if (!isAdmin(request)) return;
            boolean isUpdated = handleMultipartBookUpload(request, false);
            request.setAttribute("fromUpdate", isUpdated); 
            request.setAttribute("message", isUpdated ? "도서 정보가 수정되었습니다." : "도서 정보 수정에 실패했습니다.");
            paginateAndSetList(request, "list");
            request.setAttribute("center", "/book/updateBook.jsp");
            nextPage = "/main.jsp";
 
        // 도서 수정(admin only)
        } else if (action.equals("/editBook.do")) {
            if (!isAdmin(request)) return;
            int bookNo = parseIntOrDefault(request.getParameter("bookNo"), 0);
            BookVo book = bookService.bookDetail(bookNo);
            request.setAttribute("book", book);
            request.setAttribute("center", "/book/editBook.jsp");
            nextPage = "/main.jsp";

        // 도서 삭제(admin only)    
        } else if (action.equals("/deleteBook.do")) {
            if (!isAdmin(request)) return;
            int bookNo = parseIntOrDefault(request.getParameter("bookNo"), 0);
            boolean result = bookNo > 0 && bookService.deleteBook(bookNo);
            request.setAttribute("message", result ? "도서가 삭제되었습니다." : "삭제 실패 또는 존재하지 않는 도서입니다.");
            paginateAndSetList(request, "list");
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
            request.setAttribute("totalPage", totalPage);
            request.setAttribute("pageSize", pageSize);
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

    // admin 로그인 확인
    private boolean isAdmin(HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("id");
        if (userId == null || !"admin".equals(userId)) {
            ((HttpServletResponse) request.getAttribute("response")).sendRedirect(request.getContextPath() + "/members/login.jsp");
            return false;
        }
        return true;
    }

    private void paginateAndSetList(HttpServletRequest request, String type) {
        paginateAndSetList(request, type, null);
    }

    private void paginateAndSetList(HttpServletRequest request, String type, String keyword) {
        int page = parseIntOrDefault(request.getParameter("page"), 1);
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

    private int parseIntOrDefault(String param, int defaultValue) {
        try {
            return Integer.parseInt(param);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // multipart 요청 처리: 도서 등록 또는 수정
    private boolean handleMultipartBookUpload(HttpServletRequest request, boolean isAdd) {
        try {
            if (!ServletFileUpload.isMultipartContent(request)) return false;

            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setRepository(new File(request.getServletContext().getRealPath("/book/img")));
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = upload.parseRequest(request);

            BookVo book = new BookVo();

            for (FileItem item : items) {
                if (item.isFormField()) {
                    String name = item.getFieldName();
                    String value = item.getString("UTF-8");
                    switch (name) {
                        case "bookNo": book.setBookNo(parseIntOrDefault(value, 0)); break;
                        case "title": book.setTitle(value); break;
                        case "author": book.setAuthor(value); break;
                        case "publisher": book.setPublisher(value); break;
                        case "publishYear": book.setPublishYear(parseIntOrDefault(value, 0)); break;
                        case "isbn": book.setIsbn(value); break;
                        case "category": book.setCategory(value); break;
                        case "bookInfo": book.setBookInfo(value); break;
                        case "rentalState": book.setRentalState(parseIntOrDefault(value, 0)); break;
                        case "originThumbnail": book.setThumbnail(value); break; 
                    }
                } else {
                    String fileName = new File(item.getName()).getName();
                    if (!fileName.isEmpty()) {
                        // 새 파일 업로드된 경우에만 썸네일 교체
                        String uploadPath = request.getServletContext().getRealPath("/book/img");
                        File uploadedFile = new File(uploadPath + File.separator + fileName);
                        item.write(uploadedFile);
                        book.setThumbnail("book/img/" + fileName);
                    }
                }
            }

            return isAdd ? bookService.addBook(book) : bookService.updateBook(book);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
