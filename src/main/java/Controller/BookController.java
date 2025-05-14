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

    protected void doHandle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String action = request.getPathInfo();
        System.out.println("요청한 2단계 주소: " + action);

        String nextPage = null;

        if (action.equals("/Main")) {
            nextPage = "/main.jsp";

        } else if (action.equals("/bookList.do")) {
            int page = 1;
            int pageSize = 12;
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                try {
                    page = Integer.parseInt(pageParam);
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }

            int totalCount = bookService.bookCount();
            Vector<BookVo> vector = bookService.booksByPage(page, pageSize);

            request.setAttribute("v", vector);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("center", "/book/bookList.jsp");
            nextPage = "/main.jsp";

        } else if (action.equals("/bookDetail.do")) {
            int bookNo = Integer.parseInt(request.getParameter("bookNo"));
            BookVo book = bookService.bookDetail(bookNo);
            request.setAttribute("book", book);
            request.setAttribute("center", "/book/bookDetail.jsp");
            nextPage = "/main.jsp";

        } else if (action.equals("/searchForm.do")) {
            request.setAttribute("center", "/book/bookSearchForm.jsp");
            nextPage = "/main.jsp";

        } else if (action.equals("/bookSearch.do")) {
            String keyword = request.getParameter("keyword");
            Vector<BookVo> result = bookService.searchBooks(keyword);
            request.setAttribute("keyword", keyword);
            request.setAttribute("v", result);
            request.setAttribute("center", "/book/bookSearch.jsp");
            nextPage = "/main.jsp";

        } else if (action.equals("/newBooks.do")) {
            Vector<BookVo> newBooks = bookService.newBooks();
            request.setAttribute("v", newBooks);
            request.setAttribute("center", "/book/newBookList.jsp");
            nextPage = "/main.jsp";

        } else if (action.equals("/bestBooks.do")) {
            Vector<BookVo> bestBooks = bookService.bestBooks();
            request.setAttribute("v", bestBooks);
            request.setAttribute("center", "/book/bestBookList.jsp");
            nextPage = "/main.jsp";

       
        } else if (action.equals("/confirmRental.do")) {
            int bookNo = Integer.parseInt(request.getParameter("bookNo"));
            BookVo book = bookService.bookDetail(bookNo);
            request.setAttribute("book", book);
            request.setAttribute("center", "/book/confirmRental.jsp");
            nextPage = "/main.jsp";
        
        } else if (action.equals("/rentalBook.do")) {
            if (!request.getMethod().equalsIgnoreCase("POST")) {
                response.sendRedirect(request.getContextPath() + "/books/bookList.do");
                return;
            }

            HttpSession session = request.getSession();
            String userId = (String) session.getAttribute("id");
            if (userId == null) {
                response.sendRedirect(request.getContextPath() + "/members/login.jsp");
                return;
            }

            int bookNo = Integer.parseInt(request.getParameter("bookNo"));
            boolean isRented = bookService.rentBook(userId, bookNo);
            request.setAttribute("message", isRented ? "대출이 완료되었습니다." : "대출 처리에 실패했습니다.");
            request.setAttribute("center", "/book/rentalResult.jsp");
            nextPage = "/main.jsp";

        } else if (action.equals("/editBook.do")) {
            HttpSession session = request.getSession();
            String userId = (String) session.getAttribute("id");
            if (userId == null || !"admin".equals(userId)) {
                response.sendRedirect(request.getContextPath() + "/members/login.jsp");
                return;
            }

            request.setAttribute("center", "/book/editBook.jsp");
            nextPage = "/main.jsp";

         // 등록 폼으로 이동
        } else if (action.equals("/addBookForm.do")) {
            HttpSession session = request.getSession();
            String userId = (String) session.getAttribute("id");

            if (userId == null || !"admin".equals(userId)) {
                response.sendRedirect(request.getContextPath() + "/members/login.jsp");
                return;
            }

            request.setAttribute("center", "/book/addBook.jsp");
            nextPage = "/main.jsp";

        // 등록 처리
        } else if (action.equals("/addBook.do")) {
            HttpSession session = request.getSession();
            String userId = (String) session.getAttribute("id");

            if (userId == null || !"admin".equals(userId)) {
                response.sendRedirect(request.getContextPath() + "/members/login.jsp");
                return;
            }

			// File upload form 처리
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if (isMultipart) {
				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setRepository(new File(request.getServletContext().getRealPath("/book/img")));
				ServletFileUpload upload = new ServletFileUpload(factory);

				try {
					List<FileItem> items = upload.parseRequest(request);
					BookVo book = new BookVo();
					String fileName = null;

					for (FileItem item : items) {
						if (item.isFormField()) {
							String fieldName = item.getFieldName();
							String value = item.getString("UTF-8");

							switch (fieldName) {
								case "title":
									book.setTitle(value);
									break;
								case "author":
									book.setAuthor(value);
									break;
								case "publisher":
									book.setPublisher(value);
									break;
								case "publishYear":
									book.setPublishYear(Integer.parseInt(value));
									break;
								case "isbn":
									book.setIsbn(value);
									break;
								case "category":
									book.setCategory(value);
									break;
								case "bookInfo":
									book.setBookInfo(value);
									break;
							}
						} else {
							fileName = new File(item.getName()).getName();
							if (!fileName.isEmpty()) {
								String uploadPath = request.getServletContext().getRealPath("/book/img");
								File uploadedFile = new File(uploadPath + File.separator + fileName);
								item.write(uploadedFile);
								book.setThumbnail("book/img/" + fileName);
							}
						}
					}

					boolean result = bookService.addBook(book);
					request.setAttribute("message", result ? "도서 등록 성공" : "도서 등록 실패");

				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("message", "에러 발생: " + e.getMessage());
				}
			}

			request.setAttribute("center", "/book/addBook.jsp");
			nextPage = "/main.jsp";
            
       
        } else if (action.equals("/updateBook.do")) {
            HttpSession session = request.getSession();
            String userId = (String) session.getAttribute("id");
            if (userId == null || !"admin".equals(userId)) {
                response.sendRedirect(request.getContextPath() + "/members/login.jsp");
                return;
            }

            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if (isMultipart) {
                DiskFileItemFactory factory = new DiskFileItemFactory();
                factory.setRepository(new File(request.getServletContext().getRealPath("/book/img")));
                ServletFileUpload upload = new ServletFileUpload(factory);

                try {
                    List<FileItem> items = upload.parseRequest(request);
                    BookVo book = new BookVo();

                    for (FileItem item : items) {
                        if (item.isFormField()) {
                            String name = item.getFieldName();
                            String value = item.getString("UTF-8");
                            switch (name) {
                                case "bookNo": book.setBookNo(Integer.parseInt(value)); break;
                                case "title": book.setTitle(value); break;
                                case "author": book.setAuthor(value); break;
                                case "publisher": book.setPublisher(value); break;
                                case "publishYear": book.setPublishYear(Integer.parseInt(value)); break;
                                case "isbn": book.setIsbn(value); break;
                                case "category": book.setCategory(value); break;
                                case "bookInfo": book.setBookInfo(value); break;
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

                    boolean result = bookService.updateBook(book);
                    request.setAttribute("message", result ? "수정 성공" : "수정 실패");

                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("message", "에러 발생: " + e.getMessage());
                }
            }

            int page = 1;
            int pageSize = 12;
            String pageParam = request.getParameter("page");
            if (pageParam != null) {
                try {
                    page = Integer.parseInt(pageParam);
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }

            int totalCount = bookService.bookCount();
            int totalPage = (int) Math.ceil((double) totalCount / pageSize);
            Vector<BookVo> bookList = bookService.booksByPage(page, pageSize);

            request.setAttribute("v", bookList);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("totalPage", totalPage);
            request.setAttribute("pageSize", pageSize);

            request.setAttribute("center", "/book/updateBook.jsp");
            nextPage = "/main.jsp";
            
        } else if (action.equals("/deleteBook.do")) {
            HttpSession session = request.getSession();
            String userId = (String) session.getAttribute("id");

            if (userId == null || !"admin".equals(userId)) {
                response.sendRedirect(request.getContextPath() + "/members/login.jsp");
                return;
            }

            int bookNo = 0;
            try {
                bookNo = Integer.parseInt(request.getParameter("bookNo"));
            } catch (NumberFormatException e) {
                bookNo = 0;
            }

            boolean result = false;
            if (bookNo > 0) {
                result = bookService.deleteBook(bookNo);
            }

            // 메시지 설정
            request.setAttribute("message", result ? "도서가 삭제되었습니다." : "삭제 실패 또는 존재하지 않는 도서입니다.");

            // 삭제 후 목록 갱신
            int page = 1;
            int pageSize = 10;
            int totalCount = bookService.bookCount();
            Vector<BookVo> bookList = bookService.booksByPage(page, pageSize);

            request.setAttribute("v", bookList);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("center", "/book/updateBook.jsp");
            nextPage = "/main.jsp";
        
            
        } else if (action.equals("/returnBook.do")) {
            HttpSession session = request.getSession();
            String userId = (String) session.getAttribute("id");
            if (userId == null || !"admin".equals(userId)) {
                response.sendRedirect(request.getContextPath() + "/members/login.jsp");
                return;
            }

            String rentNoStr = request.getParameter("rentNo");
            if (rentNoStr != null) {
                int rentNo = 0;
                try {
                    rentNo = Integer.parseInt(rentNoStr);
                } catch (NumberFormatException e) {
                    rentNo = 0;
                }

                if (rentNo > 0) {
                    boolean success = bookService.returnBook(rentNo);
                    request.setAttribute("message", success ? "반납 처리가 완료되었습니다." : "반납 처리에 실패했습니다.");
                }
            }

            Vector<RentalVo> pendingReturns = bookService.pendingRentals();
            request.setAttribute("pendingList", pendingReturns);
            request.setAttribute("center", "/book/returnBook.jsp");
            nextPage = "/main.jsp";

        } else if (action.equals("/allRental.do")) {
            HttpSession session = request.getSession();
            String userId = (String) session.getAttribute("id");
            if (userId == null || !"admin".equals(userId)) {
                response.sendRedirect(request.getContextPath() + "/members/login.jsp");
                return;
            }

            Vector<RentalVo> rentalList = bookService.allRentals();
            request.setAttribute("rentalList", rentalList);
            request.setAttribute("center", "/book/allRentalList.jsp");
            nextPage = "/main.jsp";

        } else {
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
