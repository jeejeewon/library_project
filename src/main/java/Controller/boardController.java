package Controller;

import java.io.File;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.servlet.ServletContext;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.http.impl.client.FutureRequestExecutionService;
import org.json.simple.JSONObject;

import Dao.boardDAO;
import Service.boardService;
import Vo.boardVO;

@WebServlet("/bbs/*")
public class boardController extends HttpServlet {

	private boardService boardService;
	private boardVO boardVO;
	private Vector<boardVO> boardList;

	@Override
	public void init(ServletConfig config) throws ServletException {
		boardService = new boardService();
		boardVO = new boardVO();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			doHandle(request, response);
		} catch (ServletException | IOException | FileUploadException e) {

			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			doHandle(request, response);
		} catch (ServletException | IOException | FileUploadException e) {

			e.printStackTrace();
		}
	}

	// 업로드 파일이 저장될 기본 경로 상수
//	public static final String BOARD_FILE_REPO = "C:\\workspace_libraryProject\\library_project\\src\\main\\webapp\\board\\board_file_repo";

	// 업로드 파일을 저장하는 메소드
	public Map<String, String> uploadFile(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException, FileUploadException {

	    Map<String, String> uploadMap = new HashMap<>();
	    String encoding = "utf-8";

	    // 1. 웹 애플리케이션의 실제 루트 경로를 가져옴 (예: C:\...\webapp)
	    String webappRootPath = request.getServletContext().getRealPath("/");
	    // 2. 업로드 파일이 저장될 실제 최종 경로를 만듦
        // webappRootPath + "board" + "board_file_repo" 이런 식으로 연결해야 함
        // 윈도우(\)와 리눅스(/) 폴더 구분자를 자동으로 맞춰주는 File.separator 사용
	    String finalUploadPath = webappRootPath + "board" + File.separator + "board_file_repo";
	    // 3. 임시 파일이 저장될 폴더 경로를 만듦 (최종 저장 폴더 아래에 temp 폴더)
        String tempUploadPath = finalUploadPath + File.separator + "temp";
	    // 업로드할 파일을 임시로 저장할 폴더 생성 (이제 tempUploadPath 사용)
	    File tempDir = new File(tempUploadPath);
	    
	    // 업로드할 파일을 임시로 저장할 폴더 생성
//	    File currentDirPath = new File(BOARD_FILE_REPO);
//	    File tempDir = new File(currentDirPath, "temp");

	    if (!tempDir.exists()) {
	        tempDir.mkdirs(); // temp 폴더와 그 상위 폴더들까지 없으면 생성
	    }

	    // 파일 업로드 환경 설정
	    DiskFileItemFactory factory = new DiskFileItemFactory();
	    factory.setSizeThreshold(1024 * 1024 * 3); // 메모리에 저장할 파일 최대 크기: 3MB
	    factory.setRepository(tempDir); // 3MB 초과 파일은 임시 폴더에 저장

	    // 업로드 처리를 위한 객체 생성
	    ServletFileUpload upload = new ServletFileUpload(factory);
	    upload.setHeaderEncoding(encoding); // 한글 깨짐 방지를 위한 인코딩 설정

	    try {
	        // 업로드 요청 파싱 (폼 입력값과 파일이 모두 포함되어 있음)
	        List<FileItem> items = upload.parseRequest(request);

	        for (FileItem fileItem : items) {
	            String fieldName = fileItem.getFieldName();

	            if (fileItem.isFormField()) {
	                uploadMap.put(fieldName, fileItem.getString(encoding));
	            } else { // 파일 업로드 처리
	                String originalFileName = fileItem.getName();
	                long fileSize = fileItem.getSize(); // 파일 크기

	                if (fileSize > 0) { // 파일이 존재하는 경우
	                    String fileNameOnly = new File(originalFileName).getName(); // 경로 제거 후 파일명만 추출

	                    // 타임스탬프 추가하여 파일명 변경
	                    String newFileName = System.currentTimeMillis() + "_" + fileNameOnly;

	                    // 업로드된 파일을 임시 폴더에 저장
	                    File uploadFile = new File(tempDir, newFileName);
	                    fileItem.write(uploadFile);

	                    // 첨부파일 처리
	                    if ("file".equals(fieldName)) {
	                        uploadMap.put("file", newFileName); // 글 작성용 첨부파일
	                        uploadMap.put("newFileName", newFileName); // 글 수정 시 새로 업로드한 첨부파일
	                    } else if ("bannerImage".equals(fieldName)) {
	                        uploadMap.put("bannerImage", newFileName); // 글 작성 시 배너 이미지
	                        uploadMap.put("newBannerName", newFileName); // 글 수정 시 새로 업로드한 배너 이미지
	                    }
	                } else { // 파일이 비어 있는 경우
	                    // 파일 없으면 공백 처리
	                    if ("file".equals(fieldName)) {
	                        uploadMap.put("newFileName", ""); // 수정 시 새 첨부파일 없음
	                    } else if ("bannerImage".equals(fieldName)) {
	                        uploadMap.put("newBannerName", ""); // 수정 시 새 배너 없음
	                    }
	                }
	            }
	        }

	        // 기존 파일 이름들도 함께 수신 (수정 폼에서 hidden input으로 전달됨)
	        uploadMap.putIfAbsent("originalFileName", "");
	        uploadMap.putIfAbsent("originalBannerName", "");

	        // 삭제 요청 파라미터 수신 (hidden input으로 전달된 값 반영)
	        uploadMap.putIfAbsent("deleteFile", "false");
	        uploadMap.putIfAbsent("deleteBanner", "false");

	        // 기존 파일 이름들도 함께 수신 (수정 폼에서 hidden input으로 전달된 값)
	        if (!uploadMap.containsKey("originalFileName")) {
	            uploadMap.put("originalFileName", ""); // 없으면 공백 기본값
	        }
	        if (!uploadMap.containsKey("originalBannerName")) {
	            uploadMap.put("originalBannerName", "");
	        }

	        // 첨부파일 삭제 처리
	        if ("true".equals(uploadMap.get("deleteFile"))) {
	            // 첨부파일만 삭제
	            uploadMap.put("newFileName", ""); // 첨부파일 삭제
	        } else {
	            // 삭제되지 않으면 기존 파일 유지
	            if ("".equals(uploadMap.get("newFileName"))) {
	                uploadMap.put("newFileName", uploadMap.get("originalFileName"));
	            }
	        }

	        // 배너 이미지 삭제 처리
	        if ("true".equals(uploadMap.get("deleteBanner"))) {
	            // 배너이미지만 삭제
	            uploadMap.put("newBannerName", ""); // 배너 이미지 삭제
	        } else {
	            // 삭제되지 않으면 기존 배너 유지
	            if ("".equals(uploadMap.get("newBannerName"))) {
	                uploadMap.put("newBannerName", uploadMap.get("originalBannerName"));
	            }
	        }

	    } catch (FileUploadException e) {
	        System.err.println("파일 업로드 실패: " + e.getMessage());
	        e.printStackTrace();
	    } catch (Exception e) {
	        System.err.println("파일 처리 중 오류 발생: " + e.getMessage());
	        e.printStackTrace();
	        throw new ServletException("파일 업로드 중 오류 발생", e);
	    }

	    return uploadMap; // 업로드된 데이터(제목, 내용, 파일명 등) 반환
	}








	// 임시 폴더의 파일을 실제 저장 폴더로 이동시키는 메소드
	private void moveFileToDestination(ServletContext context, String fileName, int boardId, String fileType) {
		if (fileName != null && !fileName.isEmpty()) {
			// ServletContext 객체에서 getRealPath 사용
	        String webappRootPath = context.getRealPath("/");
	        
            // 웹 애플리케이션 루트 경로 + 업로드 폴더 상대 경로 = 최종 업로드 기본 경로
            String finalUploadBasePath = webappRootPath + "board" + File.separator + "board_file_repo";
			// 임시 저장된 파일 위치 (이제 BOARD_FILE_REPO 대신 finalUploadBasePath 사용)
			File srcFile = new File(finalUploadBasePath + File.separator + "temp" + File.separator + fileName);
			// 최종 저장 폴더 경로 (글 번호를 폴더 이름으로 사용) (여기도 finalUploadBasePath 사용)
			File destDir = new File(finalUploadBasePath + File.separator + boardId);
			
			
			// 임시 저장된 파일 위치
//			File srcFile = new File(BOARD_FILE_REPO + File.separator + "temp" + File.separator + fileName);
			// 최종 저장 폴더 경로 (글 번호를 폴더 이름으로 사용)
//			File destDir = new File(BOARD_FILE_REPO + File.separator + boardId);

			// 최종 폴더가 없다면 생성
			if (!destDir.exists()) {
				destDir.mkdirs();
			}
			// 파일이 실제로 존재한다면 이동 처리
			if (srcFile.exists()) {
				try {
					FileUtils.moveFileToDirectory(srcFile, destDir, true); // 파일 이동
				} catch (Exception e) {
					System.err.println("파일 이동 오류: " + e.getMessage());
				}
			} else {
				System.err.println("임시 파일을 찾을 수 없습니다: " + srcFile.getPath());
			}
		}
	}

	// doHandle() 메소드 : 요청을 처리하는 메소드
	private void doHandle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, FileUploadException {
		String nextPage = null;
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");

		String action = request.getPathInfo();
		System.out.println("action :" + action);

		/*-------------------------------------공지사항게시판---------------------------------------*/
		// 공지사항 게시판 조회하기
		// 요청주소 "/bbs/noticeList.do"
		if (action.equals("/noticeList.do")) {
			
			// 검색어와 검색타입 받기
			String searchType = request.getParameter("searchType");
			String searchKeyword = request.getParameter("searchKeyword");
			
			// 기본값 설정 (검색어가 없으면 빈 문자열, 검색 타입이 없으면 제목 검색)
			if (searchType == null) searchType = "title";
			if (searchKeyword == null) searchKeyword = "";
			
			// URL 쿼리 파라미터(?section=...&pageNum=...)로 전달된 섹션(페이지 그룹) 번호와 페이지 번호를 읽어옵니다.
			String sectionParam = request.getParameter("section");
			String pageNumParam = request.getParameter("pageNum");
			
			//파라미터 값이 없거나 비어있는경우에 기본값을 1로 설정
			int section = Integer.parseInt(sectionParam == null || sectionParam.isEmpty() ? "1" : sectionParam);
			int pageNum = Integer.parseInt(pageNumParam == null || pageNumParam.isEmpty() ? "1" : pageNumParam);
			
			
			// 카테고리 설정 (현재 공지사항이므로 0번으로 설정함)
			int category = 0; // 카테고리 0번 (공지사항)
			
			//서비스 호출하여 페이징된 게시글 목록과 검색된 게시글 목록 가져오기
			Map<String, Object> resultMap = boardService.getBoardList(category, section, pageNum, searchKeyword, searchType, null);
			
			//페이징된 게시글 목록과 페이징정보 추출하기
			List<boardVO> boardList = (List<boardVO>)resultMap.get("boardList"); //게시글목록
			int totalPage = (int) resultMap.get("totalPage"); // 총 페이지 수
			int totalSection = (int) resultMap.get("totalSection"); //총 섹션 수
			int totalBoardCount = (int) resultMap.get("totalBoardCount"); //총 게시글 수
			
			//정보들을 request에 저장하기
		    request.setAttribute("searchKeyword", searchKeyword);
		    request.setAttribute("searchType", searchType);
		    request.setAttribute("boardList", boardList);
		    request.setAttribute("totalPage", totalPage);
		    request.setAttribute("totalSection", totalSection);
		    request.setAttribute("totalBoardCount", totalBoardCount);
		    request.setAttribute("section", section);
		    request.setAttribute("pageNum", pageNum);

			// 메인화면 중앙에 보여줄 noticeList.jsp를 request에 "center"라는 이름으로 저장하기
			request.setAttribute("center", "board/noticeList.jsp");

			// 최종적으로 보여줄 메인페이지 경로를 nextPage에 저장하기
			nextPage = "/main.jsp";

		}

		// 공지사항 글쓰기 화면
		// 요청주소 "/bbs/noticeWrite.do"
		if (action.equals("/noticeWrite.do")) {// 요청명이 noticeWrite.do이면 글쓰기 화면이 나타남

			// 메인화면 중앙에 보여줄 noticeList.jsp를 request에 "center"라는 이름으로 저장하기
			request.setAttribute("center", "board/noticeWrite.jsp");

			// 최종적으로 보여줄 메인페이지 경로를 nextPage에 저장하기
			nextPage = "/main.jsp";
		}

		// 공지사항 새 글 추가
		// 요청주소 "/bbs/AddNotice.do"
		// noticeWrite.jsp에서 글쓰기 버튼을 클릭하면 action="/bbs/AddNotice.do"로 요청이 들어옴
		// DB에 새글 추가 작업을 수행
		if (action.equals("/AddNotice.do")) {// 요청명이 AddNotice.do이면 글쓰기 처리

			Map<String, String> boardMap = uploadFile(request, response);

			// HashMap에 저장된 글정보들을 다시 꺼내옵니다.
			String title = boardMap.get("title");
			String content = boardMap.get("content");
			String file = boardMap.get("file");
			String bannerImage = boardMap.get("bannerImage");

			// DB에 추가하기 위해 사용자가 입력한 글정보+업로드할 파일명을 ArticleVO객체의 각변수에 저장
			boardVO.setCategory(0);// 추가할 새글의 카테고리번호를 0으로 지정해서 공지사항으로 지정
			boardVO.setTitle(title);// 추가하기위해 입력한 글제목 저장
			boardVO.setContent(content);// 추가하기 위해 입력한 글내용 저장
			boardVO.setUserId((String) request.getSession().getAttribute("id"));
			boardVO.setFile(file);// 새글 입력시 첨부해서 업로드한 파일명 저장
			boardVO.setBannerImg(bannerImage);// 새글 입력시 첨부해서 업로드한 배너파일명 저장
			boardVO.setCreatedAt(new Timestamp(System.currentTimeMillis())); // 게시글 작성일을 현재시간으로 지정
			boardVO.setViews(0);// 조회수는 0으로 지정. 나중에 방문시 조회수 증가시켜야함
			boardVO.setSecret(false);// 비밀글 여부는 false로 지정 (false :공개 , true:비공개)  (공지사항에서는 무조건 공개글)

			// boardService객체를 통해 DB에 새글을 추가하는 메소드를 호출합니다
			// boardService의 addNotice 메소드는 DB에 성공적으로 등록된 후 할당된 새 글의 번호(articleNO)를 반환합니다.
			int boardId = boardService.addBoard(boardVO);

			// 첨부파일이 있는 경우 파일 이동
//			moveFileToDestination(file, boardId, "첨부파일");
			// 배너 이미지가 있는 경우 파일 이동
//			moveFileToDestination(bannerImage, boardId, "배너 이미지");
			
			// ServletContext 객체를 가져오기
			ServletContext servletContext = request.getServletContext();
			// 첨부파일이 있는 경우 파일 이동
			if (file != null && !file.isEmpty()) { // 파일이 있는 경우에만 호출하도록
			    moveFileToDestination(servletContext, file, boardId, "첨부파일"); // <-- 첫 번째 인자로 servletContext 넘겨줌
			}
			// 배너 이미지가 있는 경우 파일 이동
			if (bannerImage != null && !bannerImage.isEmpty()) {
			    moveFileToDestination(servletContext, bannerImage, boardId, "배너 이미지");
			}
			
			
			// 글 등록 후, 새로 등록된 글 번호를 사용하여 해당 글을 조회하는 페이지로 리다이렉트합니다.
			// 전체글을 다시 DB에서 겅색하여 보여주기 위해 다음과 같은 주소를 저장
			nextPage = "/bbs/noticeList.do";

		} // end of AddNotice.do

		// 공지사항 상세페이지
		// 요청주소 "/bbs/noticeInfo.do"
		if (action.equals("/noticeInfo.do")) {

			System.out.println("jsp에서 요청된 글 번호 : " + request.getParameter("boardId"));

			// 조회할 글번호 파라미터 수신
			// URL 쿼리 파라미터(?boardId=...)로 전달된 조회할 글의 번호를 읽어옵니다.
			String boardIdParam = request.getParameter("boardId");
			System.out.println("요청된 글 번호 파라미터 : " + boardIdParam);

			// 파라미터 유효성 검사 : null이거나 빈 문자열인 경우 오류 처리
			if (boardIdParam == null || boardIdParam.isEmpty()) {
				System.out.println("오류 : 글 상세보기 요청에 글번호 파라미터 누락.");
				throw new ServletException("글 상세보기 요청 시 글번호(boardId) 파라미터가 필요합니다.");
			}

			// 문자열로 된 글 번호를 int형으로 변환
			int boardId = Integer.parseInt(boardIdParam);

			// 글번호에 해당하는 게시글을 DB에서 조회
			// boardSevice에게 글번호(boardId)를 전달하여 해당 글을 모든 정보를 BoardVO객체에 담아 반환받도록 요청
			boardVO viewedBoard = boardService.viewBoard(boardId);

			System.out.println(
					"Service에서 조회된 글 정보 : " + (viewedBoard != null ? "BoardId=" + viewedBoard.getBoardId() : "null"));// 조회
																														// 결과
																														// 로그

			// 조회된 글이 없는 경우(삭제되었거나 잘못된 번호 요청시) 예외처리
			if (viewedBoard == null) {
				System.out.println("오류 : 글 번호 " + boardId + "에 해당하는 글이 존재하지 않습니다.");
				throw new ServletException("요청하신 글 번호 " + boardId + "에 해당하는 게시글이 존재하지 않습니다.");
			}

			int category = viewedBoard.getCategory();  // 조회한 게시글의 카테고리 값을 사용

			// 이전 글 번호 조회
			int getPreBoardId = boardService.getPreBoardId(boardId, category);
			request.setAttribute("getPreBoardId", getPreBoardId);

			// 다음 글 번호 조회
			int getNextBoardId = boardService.getNextBoardId(boardId, category);
			request.setAttribute("getNextBoardId", getNextBoardId);

			// 조회된 게시글 정보를 request 객체에 속성으로 저장
			// JSP페이지에서 ${board.title}과 같이 사용하기 위해, 조회된 BoardVO객체를 "board"라는 이름으로 request에
			// 저장
			request.setAttribute("board", viewedBoard);

			// 이동할 JSP페이지 경로 설정하기
			// 메인화면 중앙에 보여줄 noticeInfo.jsp를 request에 "center"라는 이름으로 저장하기
			request.setAttribute("center", "board/noticeInfo.jsp");
			// 최종적으로 보여줄 메인페이지 경로를 nextPage에 저장하기
			nextPage = "/main.jsp";

		} // end of noticeInfo.do

		// 공지사항 글 수정하기
		// 상세페이지에서 수정 버튼을 눌렀을때 수정페이지 요청
		// 요청주소 "/bbs/noticeModifyForm.do"
		if (action.equals("/noticeModifyForm.do")) {
			System.out.println("공지사항 글 수정 페이지 요청 시작...");

			// 글번호 파라미터 수신
			String boardIdParam = request.getParameter("boardId");
			System.out.println("요청된 글 번호 파라미터 : " + boardIdParam);

			// 파라미터 유효성 검사 : null이거나 빈 문자열인 경우 오류 처리
			if (boardIdParam == null || boardIdParam.isEmpty()) {
				System.out.println("오류 : 글 수정 요청에 글번호(boardId) 파라미터 누락.");
				throw new ServletException("글 수정 요청 시 글번호(boardId) 파라미터가 필요합니다.");
			}

			// 문자열로 된 글 번호를 int형으로 변환
			int boardId = Integer.parseInt(boardIdParam);

			// 글번호에 해당하는 게시글을 DB에서 조회
			boardVO modBoard = boardService.viewBoard(boardId);
			System.out.println(
					"Service에서 조회된 글 정보 : " + (modBoard != null ? "BoardId=" + modBoard.getBoardId() : "null"));// 조회 결과
																												// 로그

			// 조회된 글이 없는 경우(삭제되었거나 잘못된 번호 요청시) 예외처리
			if (modBoard == null) {
				System.out.println("오류 : 글 번호 " + boardId + "에 해당하는 글이 존재하지 않습니다.");
				throw new ServletException("요청하신 글 번호 " + boardId + "에 해당하는 게시글이 존재하지 않습니다.");
			}

			// 조회된 게시글 정보를 request 객체에 속성으로 저장
			// JSP페이지에서 ${board.title}과 같이 사용하기 위해, 조회된 BoardVO객체를 "board"라는 이름으로 request에
			// 저장
			request.setAttribute("board", modBoard);

			// 이동할 JSP페이지 경로 설정하기
			// 메인화면 중앙에 보여줄 noticeModifyForm.jsp를 request에 "center"라는 이름으로 저장하기
			request.setAttribute("center", "board/noticeModifyForm.jsp");

			// 최종적으로 보여줄 메인페이지 경로를 nextPage에 저장하기
			nextPage = "/main.jsp";

		} // end of noticeModifyForm.do

		// 수정페이지에서 (수정을 다 하고,) 수정버튼 눌렀을때 수정처리 요청
		// 요청주소 "/bbs/noticeModify.do"
		if (action.equals("/noticeModify.do")) {
		    System.out.println("공지사항 글 수정페이지로 이동 요청 시작...");

		    // 파일 업로드를 포함한 수정된 폼 데이터 처리
		    Map<String, String> boardMap = uploadFile(request, response);
		    System.out.println("uploadFile()메소드 Map (수정): " + boardMap);

		    // Map에서 수정 정보 추출
		    String boardIdParam = boardMap.get("boardId");
		    if (boardIdParam == null || boardIdParam.isEmpty()) {
		        System.out.println("오류 : 글 수정 요청에 글번호(boardId) 파라미터 누락.");
		        throw new ServletException("글 수정 요청 시 글번호(boardId) 파라미터가 필요합니다.");
		    }
		    int boardId = Integer.parseInt(boardIdParam);
		    String title = boardMap.get("title");
		    String content = boardMap.get("content");

		    String finalFileName = boardMap.get("newFileName");
		    String originalFileName = boardMap.get("originalFileName");
		    String deleteFile = boardMap.get("deleteFile");

		    String finalBannerName = boardMap.get("newBannerName");
		    String originalBannerName = boardMap.get("originalBannerName");
		    String deleteBanner = boardMap.get("deleteBanner");

		    // boardVO 객체에 수정된 정보 저장
		    boardVO modVO = new boardVO();
		    modVO.setBoardId(boardId);
		    modVO.setTitle(title);
		    modVO.setContent(content);
		    modVO.setFile(finalFileName);
		    modVO.setBannerImg(finalBannerName);
		    modVO.setSecret(false);
		    
		    
		    // ServletContext 객체 가져오기
		    ServletContext servletContext = request.getServletContext();
		    // 새로운 첨부 파일이 있다면 이동
		    if (!"".equals(finalFileName) && !finalFileName.equals(originalFileName)) { // 새 파일이 있고 원래 이름과 다를 경우에만 (실제로 업로드된 경우)
		        moveFileToDestination(servletContext, finalFileName, boardId, "file");
		        System.out.println("새 첨부파일 이동 완료: " + finalFileName);
		    } else if ("".equals(finalFileName) && !"".equals(originalFileName) && "true".equals(deleteFile)){
		        // 첨부파일 삭제 요청으로 finalFileName이 ""이 됐고 원래 파일이 있었을 때, moveFileToDestination 호출 안 함 (삭제만 함)
		         System.out.println("첨부파일 삭제 요청됨. 이동 없음.");
		    } else {
		        // 파일 수정/삭제 없음 (finalFileName == originalFileName 또는 둘 다 빈값)
		        System.out.println("첨부파일 변경 없음.");
		    }


		    // 새로운 배너 이미지가 있다면 이동
		    if (!"".equals(finalBannerName) && !finalBannerName.equals(originalBannerName)) { // 새 파일이 있고 원래 이름과 다를 경우에만
		        moveFileToDestination(servletContext, finalBannerName, boardId, "banner");
		        System.out.println("새 배너 이미지 이동 완료: " + finalBannerName);
		    } else if ("".equals(finalBannerName) && !"".equals(originalBannerName) && "true".equals(deleteBanner)){
		        // 배너 이미지 삭제 요청으로 finalBannerName이 ""이 됐고 원래 배너가 있었을 때
		        System.out.println("배너 이미지 삭제 요청됨. 이동 없음.");
		    } else {
		         // 배너 이미지 수정/삭제 없음
		        System.out.println("배너 이미지 변경 없음.");
		    }


		    // 수정된 정보를 DB에 반영 (finalFileName과 finalBannerName이 들어간 modVO 사용)
		    boardService.modifyBoard(modVO);  // DB 수정 요청
		    System.out.println("공지사항 글 수정 완료");

		    // 수정 후 상세페이지로 리디렉션
		    response.sendRedirect(request.getContextPath() + "/bbs/noticeInfo.do?boardId=" + boardId);
		}

		// end of noticeModify.do

		

		
		
		
		/*-------------------------------------문의게시판---------------------------------------*/
		// 문의게시판 조회하기
		// 요청주소 "/bbs/questionList.do"
		if (action.equals("/questionList.do")) {
			
			
			
			// 검색어와 검색타입 받기
			String searchType = request.getParameter("searchType");
			String searchKeyword = request.getParameter("searchKeyword");
			
			// 기본값 설정 (검색어가 없으면 빈 문자열, 검색 타입이 없으면 제목 검색)
			if (searchType == null) searchType = "title";
			if (searchKeyword == null) searchKeyword = "";

				
			// URL 쿼리 파라미터(?section=...&pageNum=...)로 전달된 섹션(페이지 그룹) 번호와 페이지 번호를 읽어옵니다.
			String sectionParam = request.getParameter("section");
			String pageNumParam = request.getParameter("pageNum");
			
			//파라미터 값이 없거나 비어있는경우에 기본값을 1로 설정
			int section = Integer.parseInt(sectionParam == null || sectionParam.isEmpty() ? "1" : sectionParam);
			int pageNum = Integer.parseInt(pageNumParam == null || pageNumParam.isEmpty() ? "1" : pageNumParam);
			
			
			// 카테고리 설정 (현재 문의게시판이므로 1번으로 설정함)
			int category = 1; // 카테고리 1번 (문의게시판)
			
			//서비스 호출하여 페이징된 게시글 목록과 검색된 게시글 목록 가져오기
			Map<String, Object> resultMap = boardService.getBoardList(category, section, pageNum, searchKeyword, searchType, null);
			
			//페이징된 게시글 목록과 페이징정보 추출하기
			List<boardVO> boardList = (List<boardVO>)resultMap.get("boardList"); //게시글목록
			int totalPage = (int) resultMap.get("totalPage"); // 총 페이지 수
			int totalSection = (int) resultMap.get("totalSection"); //총 섹션 수
			int totalBoardCount = (int) resultMap.get("totalBoardCount"); //총 게시글 수
			
			//정보들을 request에 저장하기
		    request.setAttribute("searchKeyword", searchKeyword);
		    request.setAttribute("searchType", searchType);
		    request.setAttribute("boardList", boardList);
		    request.setAttribute("totalPage", totalPage);
		    request.setAttribute("totalSection", totalSection);
		    request.setAttribute("totalBoardCount", totalBoardCount);
		    request.setAttribute("section", section);
		    request.setAttribute("pageNum", pageNum);

			// 메인화면 중앙에 보여줄 questionList.jsp를 request에 "center"라는 이름으로 저장하기
			request.setAttribute("center", "board/questionList.jsp");

			// 최종적으로 보여줄 메인페이지 경로를 nextPage에 저장하기
			nextPage = "/main.jsp";

		}
		
		
		// 문의글 글쓰기 화면
		// 요청주소 "/bbs/questionWrite.do"
		if (action.equals("/questionWrite.do")) {// 요청명이 questionWrite.do이면 글쓰기 화면이 나타남

			// 메인화면 중앙에 보여줄 noticeList.jsp를 request에 "center"라는 이름으로 저장하기
			request.setAttribute("center", "board/questionWrite.jsp");

			// 최종적으로 보여줄 메인페이지 경로를 nextPage에 저장하기
			nextPage = "/main.jsp";
		}

		// 문의글 새 글 추가
		// DB에 새글 추가 작업을 수행
		if (action.equals("/AddQuestion.do")) {// 요청명이 AddQuestion.do이면 글쓰기 처리

			Map<String, String> boardMap = uploadFile(request, response);

			// HashMap에 저장된 글정보들을 다시 꺼내옵니다.
			String title = boardMap.get("title");
			String content = boardMap.get("content");
			String file = boardMap.get("file");
			String bannerImage = boardMap.get("bannerImage");
			
			// 비밀글 처리 여부
			// secrt 파라미터 값 받아오기   <---jsp에서 체크한 부분!
			String secret = boardMap.get("secret");
			// secret 값이 null이면 false로 처리, "on"이면 true로 처리
			boolean isSecret = (secret != null && secret.equals("on"));

			// DB에 추가하기 위해 사용자가 입력한 글정보+업로드할 파일명을 ArticleVO객체의 각변수에 저장
			boardVO.setCategory(1);// 추가할 새글의 카테고리번호를 1으로 지정해서 문의글로 지정
			boardVO.setTitle(title);// 추가하기위해 입력한 글제목 저장
			boardVO.setContent(content);// 추가하기 위해 입력한 글내용 저장
			boardVO.setUserId((String) request.getSession().getAttribute("id"));
			boardVO.setFile(file);// 새글 입력시 첨부해서 업로드한 파일명 저장
			boardVO.setBannerImg(bannerImage);// 새글 입력시 첨부해서 업로드한 배너파일명 저장
			boardVO.setCreatedAt(new Timestamp(System.currentTimeMillis())); // 게시글 작성일을 현재시간으로 지정
			boardVO.setViews(0);// 조회수는 0으로 지정. 나중에 방문시 조회수 증가시켜야함
			boardVO.setSecret(isSecret);// 비밀글 여부

			// boardService객체를 통해 DB에 새글을 추가하는 메소드를 호출합니다
			// boardService의 addNotice 메소드는 DB에 성공적으로 등록된 후 할당된 새 글의 번호(articleNO)를 반환합니다.
			int boardId = boardService.addBoard(boardVO);

			// 첨부파일이 있는 경우 파일 이동
//			moveFileToDestination(file, boardId, "첨부파일");
			// 배너 이미지가 있는 경우 파일 이동
//			moveFileToDestination(bannerImage, boardId, "배너 이미지");
			// ServletContext 객체를 가져오기
			ServletContext servletContext = request.getServletContext();
			// 첨부파일이 있는 경우 파일 이동
			if (file != null && !file.isEmpty()) { // 파일이 있는 경우에만 호출하도록
			    moveFileToDestination(servletContext, file, boardId, "첨부파일"); // <-- 첫 번째 인자로 servletContext 넘겨줌
			}
			// 배너 이미지가 있는 경우 파일 이동
			if (bannerImage != null && !bannerImage.isEmpty()) {
			    moveFileToDestination(servletContext, bannerImage, boardId, "배너 이미지");
			}

			// 글 등록 후, 새로 등록된 글 번호를 사용하여 해당 글을 조회하는 페이지로 리다이렉트합니다.
			// 전체글을 다시 DB에서 겅색하여 보여주기 위해 다음과 같은 주소를 저장
			nextPage = "/bbs/questionList.do";

		} // end of AddNotice.do

		
		
		// 문의사항 상세페이지
		// 요청주소 "/bbs/questionInfo.do"
		if (action.equals("/questionInfo.do")) {
			// 현재 접속 유저 확인 (로그인 상태 및 유저 ID 가져오기)
			String currentUserId = (String) request.getSession().getAttribute("id");
			// 현재 접속 유저의 관리자 여부 체크
            boolean isAdmin = "admin".equals(currentUserId);

			System.out.println("jsp에서 요청된 글 번호 : " + request.getParameter("boardId"));

			// 조회할 글번호 파라미터 수신
			// URL 쿼리 파라미터(?boardId=...)로 전달된 조회할 글의 번호를 읽어옵니다.
			String boardIdParam = request.getParameter("boardId");
			System.out.println("요청된 글 번호 파라미터 : " + boardIdParam);

			// 파라미터 유효성 검사 : null이거나 빈 문자열인 경우 오류 처리
			if (boardIdParam == null || boardIdParam.isEmpty()) {
				System.out.println("오류 : 글 상세보기 요청에 글번호 파라미터 누락.");
				throw new ServletException("글 상세보기 요청 시 글번호(boardId) 파라미터가 필요합니다.");
			}

			// 문자열로 된 글 번호를 int형으로 변환
			int boardId = Integer.parseInt(boardIdParam);

			// 글번호에 해당하는 게시글을 DB에서 조회
			// boardSevice에게 글번호(boardId)를 전달하여 해당 글을 모든 정보를 BoardVO객체에 담아 반환받도록 요청
			boardVO viewedBoard = boardService.viewBoard(boardId);

			System.out.println("Service에서 조회된 글 정보 : " + (viewedBoard != null ? "BoardId=" + viewedBoard.getBoardId() : "null"));

			// 조회된 글이 없는 경우(삭제되었거나 잘못된 번호 요청시) 예외처리
			if (viewedBoard == null) {
				System.out.println("오류 : 글 번호 " + boardId + "에 해당하는 글이 존재하지 않습니다.");
				throw new ServletException("요청하신 글 번호 " + boardId + "에 해당하는 게시글이 존재하지 않습니다.");
			}
			
			// 비밀글 여부 확인
			String authorId = viewedBoard.getUserId(); // 조회된 글의 작성자 id 가져옴
			boolean isSecret = viewedBoard.getSecret(); // 비밀글 체크 여부 가져오기
			
			// 관리자도 아니고 작성자 본인도 아닌데 비밀글을 보려 한다면 권한 없음
			if (isSecret && (!isAdmin && (currentUserId == null || !currentUserId.equals(authorId)))) {
				System.out.println("비밀글 접근 권한 없음: 유저 " + currentUserId + "는 이 비밀글(" + boardId + ")의 작성자(" + authorId + ")도 아니고 관리자도 아닙니다.");
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("alert('해당 게시글을 볼 권한이 없습니다!');"); // 경고 메시지
				// 리다이렉트할 문의 리스트 페이지 경로로 수정해야 함!
				out.println("location.href='" + request.getContextPath() + "/bbs/questionList.do';");
				out.println("</script>");
				out.flush();
				return; 
					}
			
			int category = viewedBoard.getCategory();  // 조회한 게시글의 카테고리 값을 사용

			// 이전 글 번호 조회
			int getPreBoardId = boardService.getPreBoardId(boardId, category);
			request.setAttribute("getPreBoardId", getPreBoardId);

			// 다음 글 번호 조회
			int getNextBoardId = boardService.getNextBoardId(boardId, category);
			request.setAttribute("getNextBoardId", getNextBoardId);

			// 조회된 게시글 정보를 request 객체에 속성으로 저장
			// JSP페이지에서 ${board.title}과 같이 사용하기 위해, 조회된 BoardVO객체를 "board"라는 이름으로 request에
			// 저장
			request.setAttribute("board", viewedBoard);

			// 이동할 JSP페이지 경로 설정하기
			// 메인화면 중앙에 보여줄 questionInfo.jsp를 request에 "center"라는 이름으로 저장하기
			request.setAttribute("center", "board/questionInfo.jsp");
			// 최종적으로 보여줄 메인페이지 경로를 nextPage에 저장하기
			nextPage = "/main.jsp";

		} // end of questionInfo.do
		
		
		
		
		
		// 상세페이지에서 문의사항 글 답변달기/답변수정하기
		// 요청주소 "/bbs/reply.do"
		if(action.equals("/reply.do")) {
		    String boardIdStr = request.getParameter("boardId");
		    String reply = request.getParameter("reply");

		    int boardId = 0;
		    try {
		        boardId = Integer.parseInt(boardIdStr);
		    } catch (NumberFormatException e) {
		        response.setContentType("application/json;charset=UTF-8");
		        response.getWriter().write("{\"result\":\"fail\", \"message\":\"잘못된 요청입니다.\"}");
		        return;
		    }

		    boardService service = new boardService();
		    boolean updateSuccess = false;

		    try {
		        // 이미 등록된 답변이 있어도 덮어쓰기 가능하므로 updateReply만 호출
		        updateSuccess = service.updateReply(boardId, reply);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    response.setContentType("application/json;charset=UTF-8");
		    if(updateSuccess) {
		        response.getWriter().write("{\"result\":\"success\", \"reply\":\"" + reply.replace("\"", "\\\"") + "\"}");
		    } else {
		        response.getWriter().write("{\"result\":\"fail\", \"message\":\"답변 저장 실패\"}");
		    }
		}



		
		
		
		
		
		// 상세페이지에서 문의사항 글 삭제하기
		// 요청주소 "/bbs/replyDelete.do"
		if(action.equals("/replyDelete.do")) {
		    // 요청 파라미터에서 boardId 받아오기
		    String boardIdStr = request.getParameter("boardId");
		    
		    int boardId = 0;
		    try {
		        boardId = Integer.parseInt(boardIdStr); // 문자열을 정수로 변환
		    } catch (NumberFormatException e) {
		        // 잘못된 요청 처리 (숫자가 아닐 경우)
		        response.setContentType("application/json;charset=UTF-8");
		        response.getWriter().write("{\"result\":\"fail\", \"message\":\"잘못된 요청입니다.\"}");
		        return;
		    }

		    boardService service = new boardService(); // 서비스 객체 생성
		    boolean deleteSuccess = false;
		    try {
		    	deleteSuccess = service.deleteReply(boardId); // 서비스 통해 삭제 처리
		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    response.setContentType("application/json;charset=UTF-8");
		    if(deleteSuccess) {
		        response.getWriter().write("{\"result\":\"success\"}"); // 성공 응답
		    } else {
		        response.getWriter().write("{\"result\":\"fail\", \"message\":\"삭제 실패\"}"); // 실패 응답
		    }
		}



		
		
		
		
		// 문의게시판 글 수정하기
		// 상세페이지에서 수정 버튼을 눌렀을때 수정페이지 요청
		// 요청주소 "/bbs/questionModifyForm.do"
		if (action.equals("/questionModifyForm.do")) {
			System.out.println("문의사항 글 수정 페이지 요청 시작...");

			// 글번호 파라미터 수신
			String boardIdParam = request.getParameter("boardId");
			System.out.println("요청된 글 번호 파라미터 : " + boardIdParam);

			// 파라미터 유효성 검사 : null이거나 빈 문자열인 경우 오류 처리
			if (boardIdParam == null || boardIdParam.isEmpty()) {
				System.out.println("오류 : 글 수정 요청에 글번호(boardId) 파라미터 누락.");
				throw new ServletException("글 수정 요청 시 글번호(boardId) 파라미터가 필요합니다.");
			}

			// 문자열로 된 글 번호를 int형으로 변환
			int boardId = Integer.parseInt(boardIdParam);

			// 글번호에 해당하는 게시글을 DB에서 조회
			boardVO modBoard = boardService.viewBoard(boardId);
			System.out.println(
					"Service에서 조회된 글 정보 : " + (modBoard != null ? "BoardId=" + modBoard.getBoardId() : "null"));// 조회 결과
																												// 로그

			// 조회된 글이 없는 경우(삭제되었거나 잘못된 번호 요청시) 예외처리
			if (modBoard == null) {
				System.out.println("오류 : 글 번호 " + boardId + "에 해당하는 글이 존재하지 않습니다.");
				throw new ServletException("요청하신 글 번호 " + boardId + "에 해당하는 게시글이 존재하지 않습니다.");
			}

			// 조회된 게시글 정보를 request 객체에 속성으로 저장
			// JSP페이지에서 ${board.title}과 같이 사용하기 위해, 조회된 BoardVO객체를 "board"라는 이름으로 request에
			// 저장
			request.setAttribute("board", modBoard);

			// 이동할 JSP페이지 경로 설정하기
			// 메인화면 중앙에 보여줄 noticeModifyForm.jsp를 request에 "center"라는 이름으로 저장하기
			request.setAttribute("center", "board/questionModifyForm.jsp");

			// 최종적으로 보여줄 메인페이지 경로를 nextPage에 저장하기
			nextPage = "/main.jsp";

		} // end of questionModifyForm.do

		// 수정페이지에서 (수정을 다 하고,) 수정버튼 눌렀을때 수정처리 요청
		// 요청주소 "/bbs/questionModify.do"
		if (action.equals("/questionModify.do")) {
			System.out.println("문의사항 글 수정페이지로 이동 요청 시작...");

			// 파일 업로드를 포함한 수정된 폼 데이터 처리
			// 수정 폼에서도 파일첨부가 가능하므로 uploadFile() 메소드를 호출
			// 반환된 Map에는 수정된 글 제목, 내용, 첨부파일 등등의 정보가 담겨있습니다.
			Map<String, String> boardMap = uploadFile(request, response);
			System.out.println("uploadFile()메소드 Map (수정): " + boardMap);
			


			// Map에서 수정 정보 추출
			String boardIdParam = boardMap.get("boardId"); // 수정할 글 번호
			// 글 번호 유효성 검사
			if (boardIdParam == null || boardIdParam.isEmpty()) {
				System.out.println("오류 : 글 수정 요청에 글번호(boardId) 파라미터 누락.");
				throw new ServletException("글 수정 요청 시 글번호(boardId) 파라미터가 필요합니다.");
			}
			int boardId = Integer.parseInt(boardIdParam); // 글 번호를 int형으로 변환
			String title = boardMap.get("title"); // 수정된 제목 추출
			String content = boardMap.get("content"); // 수정된 내용 추출
			String file = boardMap.get("file"); // 수정된 첨부파일 추출
			String originalFileName = boardMap.get("originalFileName"); // 폼에 hidden 필드로 전달된 기존 첨부 파일 이름 (파일 변경 시 기존 파일
																		// 삭제용)
			String bannerImage = boardMap.get("bannerImage"); // 수정된 배너 이미지 추출
			String originalBannerName = boardMap.get("originalBannerName"); // 폼에 hidden 필드로 전달된 기존 첨부 파일 이름 (파일 변경 시 기존
																			// 파일 삭제용)
			// 비밀글 처리 여부
			// secret 파라미터 값 받아오기   <---jsp에서 체크한 부분!
			String secret = boardMap.get("secret");
			// secret 값이 null이면 false로 처리, "on"이면 true로 처리
			boolean isSecret = (secret != null && secret.equals("on"));
			
			
			System.out.println("추출된 수정 정보 : " + "boardId=" + boardId + ", title=" + title + ", content=" + content
					+ ", file=" + file + ", bannerImage=" + bannerImage + ", secret" + isSecret);

			// boardVO 객체에 수정된 정보 저장
			// 데이터베이스에 업데이트를 하기위해 수정된 정보와 글 번호를 boardVO 객체에 저장합니다.
			// 주의 : 멤버 변수 boardVO 재사용시, 스레드 안전 문제 가능성이 있으므로 새 객체를 생성하는것이 안전합니다.
			boardVO modVO = new boardVO();// 수정 정보를 담을 새 VO객체
			modVO.setBoardId(boardId); // 수정할 글 번호 (WHERE절에서 사용됩니다.)
			modVO.setTitle(title); // 수정된 제목
			modVO.setContent(content); // 수정된 내용
			modVO.setFile(file); // 수정된 첨부파일
			modVO.setBannerImg(bannerImage); // 수정된 배너 이미지
			modVO.setSecret(isSecret);// 비밀글 여부
			// 그 외 작성자, 작성일 등은 수정하지 않으므로 그대로 둡니다.

			// boardService를 통해 글 수정 처리 요청
			// boardService에게 movVO 객체를 전달하여 DB에서 해당 글의 내용을 업데이트 하도록 요청합니다.
			boardService.modifyBoard(modVO);
			System.out.println("문의사항 글 수정 완료"); // 수정 완료 로그

			
			
			
			// ServletContext 객체를 가져옴!
			ServletContext servletContext = request.getServletContext(); // <-- 여기서 가져옴!
			// 웹 애플리케이션 루트 경로 가져오기
			String webappRootPath = servletContext.getRealPath("/"); // <-- 실제 루트 경로!
			// 최종 업로드 기본 경로 계산 (BOARD_FILE_REPO 대신 사용)
			String finalUploadBasePath = webappRootPath + "board" + File.separator + "board_file_repo";
			
			// 첨부파일 처리
			// 수정된 첨부파일이 있는 경우 파일 이동
			if (file != null && !file.isEmpty()) {
				// 새 파일을 임시 폴더(temp)에서 최종 폴더(글번호 폴더)로 이동
				File srcFile = new File(finalUploadBasePath + File.separator + "temp" + File.separator + file); // 임시 폴더에 저장된 파일
				File destDir = new File(finalUploadBasePath + File.separator + boardId); // 최종 저장 폴더 (글번호 폴더)
				
				// 최종 폴더가 없다면 생성
				if (!destDir.exists()) {
					destDir.mkdirs();
				}
				// 임시 파일 존재 확인 후 이동
				if (srcFile.exists()) {
					FileUtils.moveFileToDirectory(srcFile, destDir, true); // 파일 이동
					System.out.println("첨부파일 이동 완료: " + srcFile.getPath() + " -> " + destDir.getPath()); // 이동 완료 로그
				} else {
					System.out.println("오류 : 임시 파일을 찾을 수 없습니다: " + srcFile.getPath());
				}

				// 기존 첨부파일 처리 :
				// 기존 파일이 있었고, 새로 첨부된 파일과 이름이 다른 경우에만 기존 파일 삭제
				if (originalFileName != null && !originalFileName.isEmpty() && !originalFileName.equals(file)) {
					File oldFile = new File(destDir, originalFileName);
					// 기존 파일이 존재하는 경우 삭제
					if (oldFile.exists()) {
						boolean deleted = oldFile.delete();
						System.out.println("기존 첨부 파일 삭제 ( " + oldFile.getPath() + " ) : " + deleted);// 삭제 결과 로그
					}
				}
			}

			
			
			// 수정된 배너 이미지가 있는 경우 파일 이동
			if (bannerImage != null && !bannerImage.isEmpty()) {

                // 1-1. 기존 배너 이미지가 있었다면 파일 시스템에서 삭제
				if (originalBannerName != null && !originalBannerName.isEmpty()) {
					File oldBanner = new File(
							finalUploadBasePath + File.separator + boardId + File.separator + originalBannerName);
					if (oldBanner.exists()) {
						boolean deleted = oldBanner.delete(); // 기존 배너 이미지 삭제
						System.out.println("기존 배너 이미지 삭제됨 ( " + oldBanner.getPath() + " ) : " + deleted);
					}
				}

                // 1-2. uploadFile 메소드에서 임시 폴더에 저장된 새 배너 이미지를 최종 폴더로 이동
				moveFileToDestination(servletContext, bannerImage, boardId, "배너 이미지"); 


            } else if (originalBannerName != null && !originalBannerName.isEmpty() && "true".equals(request.getParameter("deleteBanner"))) { // 배너 이미지 삭제만 요청한 경우
                // 2. 새 배너 이미지는 없고, 기존 배너 이미지가 있었는데 삭제 요청이 들어온 경우
                System.out.println("배너 이미지 삭제 요청됨.");
                
                 String webappRootPathForDelete = servletContext.getRealPath("/");
                 
                 String finalUploadBasePathForDelete = webappRootPathForDelete + "board" + File.separator + "board_file_repo";
                 
                 File originalBannerFileToDelete = new File(finalUploadBasePathForDelete + File.separator + boardId, originalBannerName);
                 if (originalBannerFileToDelete.exists()) {
                      if (originalBannerFileToDelete.delete()) {
                          System.out.println("요청에 따라 원래 배너 이미지 삭제 완료: " + originalBannerName);
                      } else {
                          System.err.println("요청에 따라 원래 배너 이미지 삭제 실패: " + originalBannerName);
                      }
                 } else {
                      System.err.println("삭제하려 했으나 원래 배너 이미지를 찾을 수 없습니다: " + originalBannerName);
                 }
            }
            // 3. 배너 이미지 변경/삭제 요청이 없는 경우 (그대로 유지)

			// 수정 후 상세페이지로 리디렉션
			response.sendRedirect(request.getContextPath() + "/bbs/questionInfo.do?boardId=" + boardId);

		} // end of questionModify.do

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/*-------------------------------------내서평게시판---------------------------------------*/
		// 내서평 게시판 조회하기 (마이메뉴에서 내서평을 눌렀을경우)
		// 요청주소 "/bbs/myReviewList.do"
		if (action.equals("/myReviewList.do")) {
			// 1. 로그인 상태 확인 (세션에 'id'가 없으면 로그인 페이지로 리다이렉트)
			String currentUserId = (String) request.getSession().getAttribute("id");
				if (currentUserId == null || currentUserId.isEmpty()) {
					response.sendRedirect(request.getContextPath() + "/member/login");
					return; // 중요! 리다이렉트 했으니 더 이상 코드 실행하지 않도록 막아줘야 함
					}

			
			
			// 검색어와 검색타입 받기
			String searchType = request.getParameter("searchType");
			String searchKeyword = request.getParameter("searchKeyword");
			
			// 기본값 설정 (검색어가 없으면 빈 문자열, 검색 타입이 없으면 제목 검색)
			if (searchType == null) searchType = "title";
			if (searchKeyword == null) searchKeyword = "";

			
			// URL 쿼리 파라미터(?section=...&pageNum=...)로 전달된 섹션(페이지 그룹) 번호와 페이지 번호를 읽어옵니다.
			String sectionParam = request.getParameter("section");
			String pageNumParam = request.getParameter("pageNum");
			
			//파라미터 값이 없거나 비어있는경우에 기본값을 1로 설정
			int section = Integer.parseInt(sectionParam == null || sectionParam.isEmpty() ? "1" : sectionParam);
			int pageNum = Integer.parseInt(pageNumParam == null || pageNumParam.isEmpty() ? "1" : pageNumParam);
			
			
			// 카테고리 설정 (현재 서평이므로 2번으로 설정함)
			int category = 2; // 카테고리 2번 (내서평)
			

			//서비스 호출하여 페이징된 게시글 목록과 검색된 게시글 목록 가져오기
			Map<String, Object> resultMap = boardService.getBoardList(category, section, pageNum, searchKeyword, searchType, currentUserId);
			
			//페이징된 게시글 목록과 페이징정보 추출하기
			List<boardVO> boardList = (List<boardVO>)resultMap.get("boardList"); //게시글목록
			int totalPage = (int) resultMap.get("totalPage"); // 총 페이지 수
			int totalSection = (int) resultMap.get("totalSection"); //총 섹션 수
			int totalBoardCount = (int) resultMap.get("totalBoardCount"); //총 게시글 수
			
			//정보들을 request에 저장하기
		    request.setAttribute("searchKeyword", searchKeyword);
		    request.setAttribute("searchType", searchType);
		    request.setAttribute("boardList", boardList);
		    request.setAttribute("totalPage", totalPage);
		    request.setAttribute("totalSection", totalSection);
		    request.setAttribute("totalBoardCount", totalBoardCount);
		    request.setAttribute("section", section);
		    request.setAttribute("pageNum", pageNum);

			// 메인화면 중앙에 보여줄 myReviewList.jsp를 request에 "center"라는 이름으로 저장하기
			request.setAttribute("center", "board/myReviewList.jsp");

			// 최종적으로 보여줄 메인페이지 경로를 nextPage에 저장하기
			nextPage = "/main.jsp";

		}
		
		
		
		
		
		// 내서평 상세페이지 (마이메뉴에서 내서평 -> 게시글을 클릭했을 경우)
		// 요청주소 "/bbs/myReviewInfo.do"
		if (action.equals("/myReviewInfo.do")) {
			
			// 로그인 상태 확인
				String currentUserId = (String) request.getSession().getAttribute("id");
					if (currentUserId == null || currentUserId.isEmpty()) {
					System.out.println("비회원 접근 시도 -> 로그인 페이지로 리다이렉트");
					response.sendRedirect(request.getContextPath() + "/member/login");
					return;
					}
						
			System.out.println("jsp에서 요청된 글 번호 : " + request.getParameter("boardId"));

			// 조회할 글번호 파라미터 수신
			// URL 쿼리 파라미터(?boardId=...)로 전달된 조회할 글의 번호를 읽어옵니다.
			String boardIdParam = request.getParameter("boardId");
			System.out.println("요청된 글 번호 파라미터 : " + boardIdParam);

			// 파라미터 유효성 검사 : null이거나 빈 문자열인 경우 오류 처리
			if (boardIdParam == null || boardIdParam.isEmpty()) {
				System.out.println("오류 : 글 상세보기 요청에 글번호 파라미터 누락.");
				throw new ServletException("글 상세보기 요청 시 글번호(boardId) 파라미터가 필요합니다.");
			}

			// 문자열로 된 글 번호를 int형으로 변환
			int boardId = Integer.parseInt(boardIdParam);

			// 글번호에 해당하는 게시글을 DB에서 조회
			// boardSevice에게 글번호(boardId)를 전달하여 해당 글을 모든 정보를 BoardVO객체에 담아 반환받도록 요청
			boardVO viewedBoard = boardService.viewBoard(boardId);

			System.out.println(
					"Service에서 조회된 글 정보 : " + (viewedBoard != null ? "BoardId=" + viewedBoard.getBoardId() : "null"));// 조회
																														// 결과
																														// 로그

			// 조회된 글이 없는 경우(삭제되었거나 잘못된 번호 요청시) 예외처리
			if (viewedBoard == null) {
				System.out.println("오류 : 글 번호 " + boardId + "에 해당하는 글이 존재하지 않습니다.");
				throw new ServletException("요청하신 글 번호 " + boardId + "에 해당하는 게시글이 존재하지 않습니다.");
			}

			int category = viewedBoard.getCategory();  // 조회한 게시글의 카테고리 값을 사용

			// 이전 글 번호 조회
			int getPreBoardId = boardService.getPreBoardId(boardId, category);
			request.setAttribute("getPreBoardId", getPreBoardId);

			// 다음 글 번호 조회
			int getNextBoardId = boardService.getNextBoardId(boardId, category);
			request.setAttribute("getNextBoardId", getNextBoardId);

			// 조회된 게시글 정보를 request 객체에 속성으로 저장
			// JSP페이지에서 ${board.title}과 같이 사용하기 위해, 조회된 BoardVO객체를 "board"라는 이름으로 request에
			// 저장
			request.setAttribute("board", viewedBoard);

			// 이동할 JSP페이지 경로 설정하기
			// 메인화면 중앙에 보여줄 myReviewInfo.jsp를 request에 "center"라는 이름으로 저장하기
			request.setAttribute("center", "board/myReviewInfo.jsp");
			// 최종적으로 보여줄 메인페이지 경로를 nextPage에 저장하기
			nextPage = "/main.jsp";

		} // end of myReviewInfoInfo.do
		
		
		
		//일반 서평 상세페이지 
		//bookDetail화면에서 서평[더보기]를 눌렀을 경우
		if(action.equals("/reviewDetail.do")) {

			System.out.println("jsp에서 요청된 글 번호 : " + request.getParameter("boardId"));

			// 조회할 글번호 파라미터 수신
			// URL 쿼리 파라미터(?boardId=...)로 전달된 조회할 글의 번호를 읽어옵니다.
			String boardIdParam = request.getParameter("boardId");
			System.out.println("요청된 글 번호 파라미터 : " + boardIdParam);

			// 파라미터 유효성 검사 : null이거나 빈 문자열인 경우 오류 처리
			if (boardIdParam == null || boardIdParam.isEmpty()) {
				System.out.println("오류 : 글 상세보기 요청에 글번호 파라미터 누락.");
				throw new ServletException("글 상세보기 요청 시 글번호(boardId) 파라미터가 필요합니다.");
			}

			// 문자열로 된 글 번호를 int형으로 변환
			int boardId = Integer.parseInt(boardIdParam);

			// 글번호에 해당하는 게시글을 DB에서 조회
			// boardSevice에게 글번호(boardId)를 전달하여 해당 글을 모든 정보를 BoardVO객체에 담아 반환받도록 요청
			boardVO viewedBoard = boardService.viewBoard(boardId);

			System.out.println(
					"Service에서 조회된 글 정보 : " + (viewedBoard != null ? "BoardId=" + viewedBoard.getBoardId() : "null"));// 조회
																														// 결과
																														// 로그

			// 조회된 글이 없는 경우(삭제되었거나 잘못된 번호 요청시) 예외처리
			if (viewedBoard == null) {
				System.out.println("오류 : 글 번호 " + boardId + "에 해당하는 글이 존재하지 않습니다.");
				throw new ServletException("요청하신 글 번호 " + boardId + "에 해당하는 게시글이 존재하지 않습니다.");
			}

			int category = viewedBoard.getCategory();  // 조회한 게시글의 카테고리 값을 사용

			// 이전 글 번호 조회
			int getPreBoardId = boardService.getPreBoardId(boardId, category);
			request.setAttribute("getPreBoardId", getPreBoardId);

			// 다음 글 번호 조회
			int getNextBoardId = boardService.getNextBoardId(boardId, category);
			request.setAttribute("getNextBoardId", getNextBoardId);

			// 조회된 게시글 정보를 request 객체에 속성으로 저장
			// JSP페이지에서 ${board.title}과 같이 사용하기 위해, 조회된 BoardVO객체를 "board"라는 이름으로 request에
			// 저장
			request.setAttribute("board", viewedBoard);

			// 이동할 JSP페이지 경로 설정하기
			// 메인화면 중앙에 보여줄 myReviewInfo.jsp를 request에 "center"라는 이름으로 저장하기
			request.setAttribute("center", "board/reviewInfo.jsp");
			// 최종적으로 보여줄 메인페이지 경로를 nextPage에 저장하기
			nextPage = "/main.jsp";
			
		}
		
		
		
		
		// 서평 등록하기
		if (action.equals("/myReviewWrite.do")) {
		    System.out.println("서평 글 등록 요청 시작 action: " + action);

		    // 요청 파라미터에서 글정보들을 직접 가져옵니다.
		    String title = request.getParameter("title");
		    String content = request.getParameter("content");
		    String bookNoStr = request.getParameter("bookNo");

		    System.out.println("request.getParameter() 로 가져온 값들: title=" + title + ", content=" + content + ", bookNoStr=" + bookNoStr);

		    // bookNo는 String으로 넘어오니 int로 변환
		    int bookNo = 0; // 기본값 설정
		    if (bookNoStr != null && !bookNoStr.isEmpty()) {
		        try {
		            bookNo = Integer.parseInt(bookNoStr);
		        } catch (NumberFormatException e) {
		            e.printStackTrace();
		            return; // 오류 발생 시 처리 중단
		        }
		    } else {
		         System.err.println("오류!! bookNo 파라미터 값이 null 이거나 비어있음");
		         // bookNo가 없으면 서평 등록이 불가능하니 에러 처리
		         return; // 오류 발생 시 여기서 처리 중단
		    }

		    String currentUserId = (String) request.getSession().getAttribute("id");
		    System.out.println("세션에서 가져온 유저 ID: " + currentUserId);
		    if (currentUserId == null) {
		       System.err.println("오류 : 로그인된 유저 ID가 없음. 서평 등록 취소.");
		    }

		    // DB에 추가하기 위해 boardVO 객체에 값 설정
		    boardVO vo = new boardVO();
		    vo.setCategory(2);
		    vo.setTitle(title);
		    vo.setContent(content);
		    vo.setUserId(currentUserId);
		    vo.setBookNo(bookNo);
		    vo.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		    vo.setViews(0);
		    vo.setSecret(false);

		    System.out.println("boardVO 객체에 값 설정 완료. userId=" + vo.getUserId() + ", bookNo=" + vo.getBookNo());

		    System.out.println("boardService.addBoard 호출 전...");
		    int boardId = -1;
		    try {
		        // boardService의 addBoard 메소드가 boardVO 객체를 인자로 받는다면 vo 객체를 넘겨줘!
		        boardId = boardService.addBoard(vo);
		        System.out.println("boardService.addBoard 호출 후. 등록된 boardId: " + boardId);
		    } catch (Exception e) { // DB 저장 등 예외 발생 시
		         System.err.println("오류 : boardService.addBoard 호출 중 예외 발생!");
		         e.printStackTrace();
		         return; // 에러 발생 시 여기서 처리 중단
		    }

		    String redirectUrl = request.getContextPath() + "/books/bookDetail.do?bookNo=" + bookNo;
		    System.out.println("서평 등록 처리 완료. 리다이렉트 URL: " + redirectUrl);

		    response.sendRedirect(redirectUrl); 

		    return; 
		}

		
		
		// 내서평 글 수정하기
		// 상세페이지에서 수정 버튼을 눌렀을때 수정페이지 요청
		// 요청주소 "/bbs/myReviewModifyForm.do"
		if (action.equals("/myReviewModifyForm.do")) {
			System.out.println("내서평 글 수정 페이지 요청 시작...");

			// 글번호 파라미터 수신
			String boardIdParam = request.getParameter("boardId");
			System.out.println("요청된 글 번호 파라미터 : " + boardIdParam);

			// 파라미터 유효성 검사 : null이거나 빈 문자열인 경우 오류 처리
			if (boardIdParam == null || boardIdParam.isEmpty()) {
				System.out.println("오류 : 글 수정 요청에 글번호(boardId) 파라미터 누락.");
				throw new ServletException("글 수정 요청 시 글번호(boardId) 파라미터가 필요합니다.");
			}

			// 문자열로 된 글 번호를 int형으로 변환
			int boardId = Integer.parseInt(boardIdParam);

			// 글번호에 해당하는 게시글을 DB에서 조회
			boardVO modBoard = boardService.viewBoard(boardId);
			System.out.println(
					"Service에서 조회된 글 정보 : " + (modBoard != null ? "BoardId=" + modBoard.getBoardId() : "null"));// 조회 결과
																												// 로그

			// 조회된 글이 없는 경우(삭제되었거나 잘못된 번호 요청시) 예외처리
			if (modBoard == null) {
				System.out.println("오류 : 글 번호 " + boardId + "에 해당하는 글이 존재하지 않습니다.");
				throw new ServletException("요청하신 글 번호 " + boardId + "에 해당하는 게시글이 존재하지 않습니다.");
			}

			// 조회된 게시글 정보를 request 객체에 속성으로 저장
			// JSP페이지에서 ${board.title}과 같이 사용하기 위해, 조회된 BoardVO객체를 "board"라는 이름으로 request에
			// 저장
			request.setAttribute("board", modBoard);

			// 이동할 JSP페이지 경로 설정하기
			// 메인화면 중앙에 보여줄 noticeModifyForm.jsp를 request에 "center"라는 이름으로 저장하기
			request.setAttribute("center", "board/myReviewModifyForm.jsp");

			// 최종적으로 보여줄 메인페이지 경로를 nextPage에 저장하기
			nextPage = "/main.jsp";

		} // end of myReviewModifyForm.do

		// 수정페이지에서 (수정을 다 하고,) 수정버튼 눌렀을때 수정처리 요청
		// 요청주소 "/bbs/myReviewModify.do"
		if (action.equals("/myReviewModify.do")) {
		    System.out.println("내서평 글 수정페이지로 이동 요청 시작...");

		    // 파일 업로드를 포함한 수정된 폼 데이터 처리
		    Map<String, String> boardMap = uploadFile(request, response);
		    System.out.println("uploadFile()메소드 Map (수정): " + boardMap);

		    // Map에서 수정 정보 추출
		    String boardIdParam = boardMap.get("boardId");
		    if (boardIdParam == null || boardIdParam.isEmpty()) {
		        System.out.println("오류 : 글 수정 요청에 글번호(boardId) 파라미터 누락.");
		        throw new ServletException("글 수정 요청 시 글번호(boardId) 파라미터가 필요합니다.");
		    }
		    int boardId = Integer.parseInt(boardIdParam);
		    String title = boardMap.get("title");
		    String content = boardMap.get("content");

		    // boardVO 객체에 수정된 정보 저장
		    boardVO modVO = new boardVO();
		    modVO.setBoardId(boardId);
		    modVO.setTitle(title);
		    modVO.setContent(content);
		    modVO.setSecret(false);

		    // 수정된 정보를 DB에 반영
		    boardService.modifyBoard(modVO);  // DB 수정 요청
		    System.out.println("내서평 글 수정 완료");

		    // 수정 후 상세페이지로 리디렉션
		    response.sendRedirect(request.getContextPath() + "/bbs/myReviewInfo.do?boardId=" + boardId);
		}

		// end of myReviewModify.do
		
		
		
	
	
		
		
		
		
		
		
		
		
		
		/*-------------------------------------행사안내 게시판---------------------------------------*/
		// 행사안내 리스트 조회하기
		// 행사안내 리스트는 공지사항에서 배너이미지를 등록한 게시글이 노출됩니다.
		// 요청주소 "/bbs/eventList.do"
		if (action.equals("/eventList.do")) {
			
			
			// 검색어와 검색타입 받기
			String searchType = request.getParameter("searchType");
			String searchKeyword = request.getParameter("searchKeyword");
			
			// 기본값 설정 (검색어가 없으면 빈 문자열, 검색 타입이 없으면 제목 검색)
			if (searchType == null) searchType = "title";
			if (searchKeyword == null) searchKeyword = "";

			
			// URL 쿼리 파라미터(?section=...&pageNum=...)로 전달된 섹션(페이지 그룹) 번호와 페이지 번호를 읽어옵니다.
			String sectionParam = request.getParameter("section");
			String pageNumParam = request.getParameter("pageNum");
			
			//파라미터 값이 없거나 비어있는경우에 기본값을 1로 설정
			int section = Integer.parseInt(sectionParam == null || sectionParam.isEmpty() ? "1" : sectionParam);
			int pageNum = Integer.parseInt(pageNumParam == null || pageNumParam.isEmpty() ? "1" : pageNumParam);
			
		    // 서비스 호출하여 페이징된 게시글 목록과 검색된 게시글 목록 가져오기
		    Map<String, Object> resultMap = boardService.getEventBoardList(section, pageNum, searchKeyword, searchType);

			//페이징된 게시글 목록과 페이징정보 추출하기
			List<boardVO> boardList = (List<boardVO>)resultMap.get("boardList"); //게시글목록
			int totalPage = (int) resultMap.get("totalPage"); // 총 페이지 수
			int totalSection = (int) resultMap.get("totalSection"); //총 섹션 수
			int totalBoardCount = (int) resultMap.get("totalBoardCount"); //총 게시글 수
			
            // 정보들을 request에 저장하기
            request.setAttribute("searchKeyword", searchKeyword);
            request.setAttribute("searchType", searchType);
            request.setAttribute("boardList", boardList);
            request.setAttribute("totalPage", totalPage);
            request.setAttribute("totalSection", totalSection);
            request.setAttribute("totalBoardCount", totalBoardCount);
            request.setAttribute("section", section);
            request.setAttribute("pageNum", pageNum);

			// 메인화면 중앙에 보여줄 eventList.jsp를 request에 "center"라는 이름으로 저장하기
			request.setAttribute("center", "board/eventList.jsp");

			// 최종적으로 보여줄 메인페이지 경로를 nextPage에 저장하기
			nextPage = "/main.jsp";

		}
		
		
		// 행사안내 상세페이지
		// 요청주소 "/bbs/eventInfo.do"
		if (action.equals("/eventInfo.do")) {

			System.out.println("jsp에서 요청된 글 번호 : " + request.getParameter("boardId"));

			// 조회할 글번호 파라미터 수신
			// URL 쿼리 파라미터(?boardId=...)로 전달된 조회할 글의 번호를 읽어옵니다.
			String boardIdParam = request.getParameter("boardId");
			System.out.println("요청된 글 번호 파라미터 : " + boardIdParam);

			// 파라미터 유효성 검사 : null이거나 빈 문자열인 경우 오류 처리
			if (boardIdParam == null || boardIdParam.isEmpty()) {
				System.out.println("오류 : 글 상세보기 요청에 글번호 파라미터 누락.");
				throw new ServletException("글 상세보기 요청 시 글번호(boardId) 파라미터가 필요합니다.");
			}

			// 문자열로 된 글 번호를 int형으로 변환
			int boardId = Integer.parseInt(boardIdParam);

			// 글번호에 해당하는 게시글을 DB에서 조회
			// boardSevice에게 글번호(boardId)를 전달하여 해당 글을 모든 정보를 BoardVO객체에 담아 반환받도록 요청
			boardVO viewedBoard = boardService.viewBannerBoard(boardId);

			System.out.println(
					"Service에서 조회된 글 정보 : " + (viewedBoard != null ? "BoardId=" + viewedBoard.getBoardId() : "null"));

			// 조회된 글이 없는 경우(삭제되었거나 잘못된 번호 요청시) 예외처리
			if (viewedBoard == null) {
				System.out.println("오류 : 글 번호 " + boardId + "에 해당하는 글이 존재하지 않습니다.");
				throw new ServletException("요청하신 글 번호 " + boardId + "에 해당하는 게시글이 존재하지 않습니다.");
			}

			int category = viewedBoard.getCategory();  // 조회한 게시글의 카테고리 값을 사용

			// 이전 글 번호 조회
			int getPreBoardId = boardService.getPreBannerBoardId(boardId);
			request.setAttribute("getPreBoardId", getPreBoardId);

			// 다음 글 번호 조회
			int getNextBoardId = boardService.getNextBannerBoardId(boardId);
			request.setAttribute("getNextBoardId", getNextBoardId);

			// 조회된 게시글 정보를 request 객체에 속성으로 저장
			// JSP페이지에서 ${board.title}과 같이 사용하기 위해, 조회된 BoardVO객체를 "board"라는 이름으로 request에
			// 저장
			request.setAttribute("board", viewedBoard);

			// 이동할 JSP페이지 경로 설정하기
			// 메인화면 중앙에 보여줄 noticeInfo.jsp를 request에 "center"라는 이름으로 저장하기
			request.setAttribute("center", "board/eventInfo.jsp");
			// 최종적으로 보여줄 메인페이지 경로를 nextPage에 저장하기
			nextPage = "/main.jsp";

		} // end of noticeInfo.do


		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		// 게시판글 삭제 (모든게시판 공통 적용) 
		// 게시판 action에 따른 redirect URL 매핑
		Map<String, String> redirectMap = new HashMap<>();
		redirectMap.put("/removeQuestion.do", "/bbs/questionList.do");
		redirectMap.put("/removeNotice.do", "/bbs/noticeList.do");
		redirectMap.put("/removeMyReviewList.do", "/bbs/myReviewList.do");
//		redirectMap.put("/removeEvent.do", "/bbs/eventList.do");  추후 다른걸로 추가예정

		// 해당 요청이 매핑에 있는지 확인
		if (redirectMap.containsKey(action)) {
		    System.out.println("글 삭제 처리 시작 ...");

		    String boardIdParam = request.getParameter("boardId");
		    System.out.println("삭제 요청된 게시글의 글번호(BoardId) 파라미터 : " + boardIdParam);

		    if (boardIdParam == null || boardIdParam.isEmpty()) {
		        System.err.println("오류: 글 삭제 요청에 글번호(BoardId) 누락");
		        throw new ServletException("글 삭제 요청시 글번호(BoardId)가 필요합니다.");
		    }

		    int boardID = Integer.parseInt(boardIdParam);
		    int deletedBoardId = boardService.removeBoard(boardID);
		    System.out.println("Service로 부터 반환된 삭제된 글 번호 : " + deletedBoardId);

		    
		    // ServletContext 객체를 가져옴
		    ServletContext servletContext = request.getServletContext();
            // 웹 애플리케이션 루트 경로 가져오기
		    String webappRootPath = servletContext.getRealPath("/"); // <-- 실제 루트 경로!
            // 최종 업로드 기본 경로 계산 (BOARD_FILE_REPO 대신 사용)
		    String finalUploadBasePath = webappRootPath + "board" + File.separator + "board_file_repo";
            // 삭제할 파일 폴더 경로 계산 (BOARD_FILE_REPO 대신 finalUploadBasePath 사용)
            // 삭제된 글 번호(deletedBoardId)로 폴더를 찾음!
		    
		    File fileDir = new File(finalUploadBasePath + File.separator + deletedBoardId);
		    
		    // 파일 폴더 존재 확인 및 삭제 로직
		    if (fileDir.exists()) {
		        try {
		            File[] files = fileDir.listFiles();
		            if (files != null) {
		                for (File file : files) {
		                    if (file.exists()) {
		                        boolean deleted = file.delete();
		                        if (deleted) {
		                            System.out.println("첨부파일 삭제 완료: " + file.getPath());
		                        } else {
		                            System.err.println("첨부파일 삭제 실패: " + file.getPath());
		                        }
		                    }
		                }
		            }
		            boolean dirDeleted = fileDir.delete();
		            if (dirDeleted) {
		                System.out.println("첨부파일 폴더 삭제 완료: " + fileDir.getPath());
		            } else {
		                System.err.println("첨부파일 폴더 삭제 실패: " + fileDir.getPath());
		            }
		        } catch (Exception e) {
		            System.err.println("오류: 첨부파일 폴더 삭제 실패 (" + fileDir.getPath() + "): " + e.getMessage());
		        }
		    }

		    // JSON 응답 처리
		    response.setContentType("application/json; charset=UTF-8");
		    PrintWriter pw = response.getWriter();
		    JSONObject jsonResponse = new JSONObject();
		    jsonResponse.put("result", "success");
		    jsonResponse.put("message", "게시글이 삭제되었습니다.");
		    jsonResponse.put("redirect", request.getContextPath() + redirectMap.get(action)); // 매핑된 경로 사용
		    pw.print(jsonResponse.toString());
		    pw.flush();
		    System.out.println("글 삭제 성공 JSON 응답 전송: " + jsonResponse.toString());
		    return;
		}

		
		
		
		
		
		// 서평 단독 삭제로직 (bookDetail->서평 더보기->서평 상세페이지->삭제 루트일 경우임)
		if ("/removeReviewList.do".equals(action)) {
		    try {
                String boardIdParam = request.getParameter("boardId");
                String bookNoParam = request.getParameter("bookNo");

                if (boardIdParam == null || boardIdParam.isEmpty()) {
                    throw new IllegalArgumentException("글 삭제 요청시 글번호(BoardId)가 필요합니다.");
                }
                int boardID = Integer.parseInt(boardIdParam);

                if (bookNoParam == null || bookNoParam.isEmpty()) {
                     throw new IllegalArgumentException("서평 삭제 요청시 책 번호(bookNo)가 필요합니다.");
                }
                int bookNo = Integer.parseInt(bookNoParam);


	            int deletedBoardId = boardService.removeBoard(boardID);


                ServletContext servletContext = request.getServletContext();
                String webappRootPath = servletContext.getRealPath("/");
                String finalUploadBasePath = webappRootPath + "board" + File.separator + "board_file_repo";
                File fileDir = new File(finalUploadBasePath + File.separator + deletedBoardId);

                if (fileDir.exists()) {
                    try {
                        File[] files = fileDir.listFiles();
                        if (files != null) {
                            for (File file : files) {
                                if (file.exists()) {
                                    file.delete();
                                }
                            }
                        }
                        fileDir.delete();
                    } catch (Exception fileDeleteException) {
                         // 에러 로그는 catch 블록에 남겨둬야 오류 발생 시 콘솔에서 확인 가능!
                         System.err.println("오류: 첨부파일 삭제 중 예외 발생 (" + fileDir.getPath() + "): " + fileDeleteException.getMessage());
                         fileDeleteException.printStackTrace();
                    }
                }


                response.setContentType("application/json; charset=UTF-8");
                try (PrintWriter pw = response.getWriter()) {
                    JSONObject jsonResponse = new JSONObject();
                    jsonResponse.put("result", "success");
                    jsonResponse.put("message", "게시글이 삭제되었습니다.");

                    String redirectUrl = request.getContextPath() + "/books/bookDetail.do?bookNo=" + bookNo;
                    jsonResponse.put("redirect", redirectUrl);

                    pw.print(jsonResponse.toString());
                    pw.flush();
                } catch (IOException ioException) {
                    // 에러 로그는 catch 블록에 남겨둬야 오류 발생 시 콘솔에서 확인 가능!
                    System.err.println("오류: 성공 응답 전송 중 IOException 발생: " + ioException.getMessage());
                    ioException.printStackTrace();
                }

                return;

            } catch (Exception e) {
                // 에러 로그는 catch 블록에 남겨둬야 오류 발생 시 콘솔에서 확인 가능!
                System.err.println("오류: 서평 삭제 중 예외 발생: " + e.getMessage());
                e.printStackTrace();

                response.setContentType("application/json; charset=UTF-8");
                try (PrintWriter pw = response.getWriter()) {
                     JSONObject jsonResponse = new JSONObject();
                     jsonResponse.put("result", "fail");
                     jsonResponse.put("message", "게시글 삭제 중 오류가 발생했습니다.");

                     pw.print(jsonResponse.toString());
                     pw.flush();
                } catch (IOException ioException) {
                    // 에러 로그는 catch 블록에 남겨둬야 오류 발생 시 콘솔에서 확인 가능!
                    System.err.println("오류: 실패 응답 전송 중 IOException 발생: " + ioException.getMessage());
                    ioException.printStackTrace();
                }

                return;
            }
		}

		
		
		
		
		
		
		
		
		
		
		
		
		
		if (nextPage != null) {          
            RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);            
            dispatch.forward(request, response);
        } else {           
            System.out.println("nextPage가 null입니다. (아마도 pw.print로 직접 응답 처리됨)" );
        }
		
		
		
		
	}// end of doHandle()

}// end class
