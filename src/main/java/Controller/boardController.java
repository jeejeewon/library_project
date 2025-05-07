package Controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

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

import DAO.boardDAO;
import Service.boardService;
import VO.boardVO;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			doHandle(request, response);
		} catch (ServletException | IOException | FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 파일 업로드 함수
	// 글에 첨부할 파일을 저장할 위치를 상수로 선언
	public static final String BOARD_FILE_REPO = "C:\\workspace_libraryProject\\library_project\\src\\main\\webapp\\board\\board_file_repo";

	// 파일 업로드 처리를 위한 메소드
	public Map<String, String> uploadFile(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, FileUploadException {

		Map<String, String> uploadMap = new HashMap<String, String>();
		String encoding = "utf-8";

		// 1.
		// 글쓰기를 할때 첨부한 파일을 저장할 폴더 경로에대해 파일 객체를 생성합니다
		File currentDirPath = new File(BOARD_FILE_REPO); // 일반 첨부파일

		// 임시 파일 저장 경로 (`BOARD_FILE_REPO/temp`) 객체 생성
		File tempDir = new File(currentDirPath, "temp");

		// 임시 디렉토리가 존재하지 않으면 생성
		if (!tempDir.exists()) {
			boolean created = tempDir.mkdirs();
			System.out.println("임시 업로드 폴더 생성 (" + tempDir.getPath() + "): " + created); // 폴더 생성 로그
		}

		// 2.파일업로드 처리 환경 설정
		DiskFileItemFactory factory = new DiskFileItemFactory(); // 업로드 할 파일 데이터를 임시로 저장할 객체메모리 생성 // ▪
																	// DiskFileItemFactory 클래스는 업로드할 파일을 메모리에 저장할 객체를
																	// 생성하는 클래스입니다
		factory.setSizeThreshold(1024 * 1024 * 3); // 3MB 파일업로드시 사용할 임시메모리 최대크기 3메가 바이트
		factory.setRepository(tempDir); // 임시 메모리에 파일 업로드시, 지정한 3MB를 초과하는 파일은 지정한 경로에 저장 //참고 : DiskFileItemFactory 클래스는
										// 업로드 파일의 크기가 지정한크리를 넘기 전까지는 파일데이터를 메모리에 저장하고, 지정한 크기를 넘기면 지정한 경로에 저장합니다.

		// 3. 실제 파일 업로드를 수행할 객체 생성
		// 파일을 업로드할 메모리를 생성자쪽으로 전달받아 저장한 파일업로드를 처리할 객체를 생성합니다
		// -ServletFileUpload 클래스는 업로드된 파일을 처리하는 메소드가 정의되어 있습니다
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding(encoding); // 한글 인코딩 설정. 폼 데이터와 일치시키는것이 좋다.

		// 4.요청(request) 파싱 및 개별 항목(FileItem) 처리
		try {
			// request에 담긴 파일업로드 요청을 처리하여 List형태로 변환합니다
			// ▪ request에는 업로드할 파일에 대한 요청정보를 가지고있다.
			// ▪ parseRequest() 메소드는 request에 담긴 파일업로드 요청을 처리하여 List형태로 변환하여 리턴합니다
			List items = upload.parseRequest(request);
			// 위에 items는 List형태로 변환된 파일업로드 요청을 담고 있습니다
			// 파일업로드요청에는 일반적인 폼데이터와 파일업로드 요청이 섞여있습니다

			// List형태로 변환된 파일업로드 요청을 반복문을 통해서 하나씩 꺼내서 처리합니다
			// ArrayList 크기만큼 반복
			for (int i = 0; i < items.size(); i++) {

				// 하나씩 꺼낸 파일업로드 요청을 FileItem 객체에 저장합니다
				// ▪ FileItem 객체는 업로드된 파일에 대한 정보를 가지고 있습니다
				FileItem fileItem = (FileItem) items.get(i);

				// 파일업로드 요청이 아닌 일반적인 폼데이터인 경우
				// 즉 DiskFileItemFactory 객체(업로드할 아이템 하나의 정보)가 파일 아이템이 아닐 경우.
				// ▪ isFormField() 메소드는 업로드된 파일이 아닌 일반적인 폼데이터인 경우 true를 리턴합니다
				// ▪ 일반적인 폼데이터란 게시판 제목, 내용, 작성자, 비밀번호 등과 같은 데이터입니다
				if (fileItem.isFormField()) {

					System.out.println(fileItem.getFieldName() + " : " + fileItem.getString(encoding));
					// 위 encoding 매개변수는 한글이 깨지지 않도록 인코딩을 utf-8로 설정함

					// noticeWrite.jsp에서 입력한 제목, 내용만 따로 HashMap에 저장합니다.
					// ▪ HasMap은 key와 value로 이루어진 자료구조
					// ▪ HashMap에 저장된 데이터의 예 : {title=제목, content=내용}
					uploadMap.put(fileItem.getFieldName(), fileItem.getString(encoding));

					// DiskFileItemFactory 객체(업로드할 아이템 하나의 정보)가 파일 아이템인 경우 업로드를 진행한다.
				} else { // 파일업로드 요청 // 파일 필드(input type="file")인 경우

					System.out.println("파라미터명:" + fileItem.getFieldName());
					System.out.println("파일이름:" + fileItem.getName());
					System.out.println("파일크기:" + fileItem.getSize() + "byte");

					// 업로드할 파일의 파일이름을 저장소에 업로드하기
					if (fileItem.getSize() > 0) {// 파일 크기가 0보다 크다면? 즉, 업로드할 파일이 존재한다면,

						// 파일 이름에서 경로 정보 제거
						String fileNameOnly = new File(fileItem.getName()).getName();

						// 결과 Map에 파일 필드 이름과 (경로제외된)파일이름을 저장
						// 나중에 이 파일 이름으로 임시 폴더의 파일을 찾아 최종위치로 이동시킨다
						uploadMap.put(fileItem.getFieldName(), fileNameOnly);

						// 업로드된 파일을 임시 디렉토리에 저장
						File uploadFile = new File(tempDir, fileNameOnly);

						fileItem.write(uploadFile);// 업로드할 파일을 임시 저장소에 업로드하기
					} else { // 파일 크기가 0인 경우
						uploadMap.put(fileItem.getFieldName(), ""); // 업로드된 파일이 없으므로 빈 문자열 저장
					}
				}
			} // end for
		} catch (FileUploadException e) {
			System.err.println("오류: upload() 메소드 내 FileUploadException - " + e.getMessage());
			e.printStackTrace();

		} catch (Exception e) {
			System.err.println("오류: upload() 메소드 내 일반 Exception - " + e.getMessage());
			e.printStackTrace();

			throw new ServletException("파일 업로드 중 오류 발생", e);
		}

		return uploadMap;
		// uploadMap에는 업로드한 파일의 정보가 담겨있다.

	}// end uploadFile()

	
	// 파일을 지정된 경로로 이동시키는 함수
	private void moveFileToDestination(String fileName, int boardId, String fileType) {

		// fileName이 null이 아니고 빈 문자열이 아닌지 확인
		// 즉, 파일이 첨부되었는지 확인하는 조건
		if (fileName != null && !fileName.isEmpty()) {

			// 임시 파일 경로 객체 생성
			// BOARD_FILE_REPO는 파일이 저장되는 기본 경로입니다.
			// "temp" 폴더는 사용자가 업로드한 파일이 임시로 저장되는 폴더입니다.
			// fileName은 업로드된 파일의 이름입니다.
			File srcFile = new File(BOARD_FILE_REPO + File.separator + "temp" + File.separator + fileName);
			//File.separator는 파일 경로 구분자를 운영 체제에 맞게 자동으로 제공하는 상수입니다. 즉 윈도우에서는 "\"로, 리눅스에서는 "/"로 자동으로 변환됩니다.

			// 최종 저장 폴더 경로 객체 생성 (글 번호 폴더)
			// boardId는 새로 등록된 글의 번호입니다.
			// BOARD_FILE_REPO + File.separator + boardId는 새로운 폴더 경로로, 새 글 번호를 가진 폴더로 파일을
			// 이동시킵니다.
			File destDir = new File(BOARD_FILE_REPO + File.separator + boardId);

			// 만약 최종 저장 폴더가 존재하지 않으면 새로 생성
			// 폴더가 없다면, 새로운 폴더를 생성하여 파일을 저장할 공간을 마련합니다.
			if (!destDir.exists()) {
				destDir.mkdirs(); // 폴더가 없으면 새로 생성합니다.
				System.out.println(fileType + " 저장 폴더가 생성되었습니다: " + destDir.getPath());
			}

			// 임시 파일이 실제로 존재하는지 확인 후 이동
			// srcFile이 실제로 존재하는 파일인지 확인합니다.
			if (srcFile.exists()) {
				try {
					// 파일을 임시 폴더에서 새 폴더로 이동
					// FileUtils.moveFileToDirectory(srcFile, destDir, true) 는 파일을 destDir 폴더로 이동시키는
					// 메소드입니다.
					// 세 번째 파라미터 true는 파일이 이미 존재하면 덮어쓰도록 하는 옵션입니다.
					FileUtils.moveFileToDirectory(srcFile, destDir, true);
					System.out.println(fileType + " 파일 이동 완료: " + srcFile.getPath() + " -> " + destDir.getPath());
				} catch (Exception e) {
					// 만약 파일 이동 중에 에러가 발생하면 예외가 발생하고, 그 메시지를 출력합니다.
					System.err.println("파일 이동 중 오류 발생: " + e.getMessage());
				}
			} else {
				// 임시 파일이 존재하지 않는 경우 오류 로그 출력
				// 만약 srcFile이 존재하지 않으면, 파일을 찾을 수 없다는 오류 메시지를 출력합니다.
				System.err.println("오류: 새 글 등록 중 임시 파일 찾기 실패: " + srcFile.getPath());
			}
		}
	}

	private void doHandle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, FileUploadException {
		String nextPage = null;
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");

		String action = request.getPathInfo();
		System.out.println("action :" + action);

		/*-------------------------------------공지사항게시판---------------------------------------*/
		// 요청주소 "/bbs/noticeList.do"
		if (action.equals("/noticeList.do")) {

			// 조회된 게시판을
			boardList = boardService.getNoticeList();

			// 조회된 목록을 request에 "boardList"라는 이름으로 저장하기
			request.setAttribute("boardList", boardList);

			// 메인화면 중앙에 보여줄 noticeList.jsp를 request에 "center"라는 이름으로 저장하기
			request.setAttribute("center", "board/noticeList.jsp");

			// 최종적으로 보여줄 메인페이지 경로를 nextPage에 저장하기
			nextPage = "/main.jsp";

		}

		// 요청주소 "/bbs/noticeWrite.do"
		if (action.equals("/noticeWrite.do")) {// 요청명이 noticeWrite.do이면 글쓰기 화면이 나타남

			// 메인화면 중앙에 보여줄 noticeList.jsp를 request에 "center"라는 이름으로 저장하기
			request.setAttribute("center", "board/noticeWrite.jsp");

			// 최종적으로 보여줄 메인페이지 경로를 nextPage에 저장하기
			nextPage = "/main.jsp";
		}
		
		// 공지사항 상세페이지
		// 요청주소 "/bbs/noticeInfo.do"
		if (action.equals("/noticeInfo.do")) {
			
			System.out.println("jsp에서 요청된 글 번호 : " + request.getParameter("boardId"));
			
			// 조회할 글번호 파라미터 수신
			// URL 쿼리 파라미터(?boardId=...)로 전달된 조회할 글의 번호를 읽어옵니다.
			String boardIdParam = request.getParameter("boardId");
			System.out.println("요청된 글 번호 파라미터 : " + boardIdParam);
			
			// 파라미터 유효성 검사 : null이거나 빈 문자열인 경우 오류 처리
			if(boardIdParam == null || boardIdParam.isEmpty()) {
				System.out.println("요류 : 글 상세보기 요청에 글번호 파라미터 누락.");
				throw new ServletException("글 상세보기 요청 시 글번호(boardId) 파라미터가 필요합니다.");
			}
			
			//문자열로 된 글 번호를 int형으로 변환
			int boardId = Integer.parseInt(boardIdParam);
			
			// 글번호에 해당하는 게시글을 DB에서 조회
			//boardSevice에게 글번호(boardId)를 전달하여 해당 글을 모든 정보를 BoardVO객체에 담아 반환받도록 요청
			boardVO viewedBoard = boardService.viewBoard(boardId);
			System.out.println("Service에서 조회된 글 정보 : " + (viewedBoard != null ? "BoardId=" + viewedBoard.getBoardId() : "null"));//조회 결과 로그
			
			//조회된 글이 없는 경우(삭제되었거나 잘못된 번호 요청시) 예외처리
			if(viewedBoard == null) {
				System.out.println("오류 : 글 번호 " + boardId + "에 해당하는 글이 존재하지 않습니다.");
				throw new ServletException("요청하신 글 번호 " + boardId + "에 해당하는 게시글이 존재하지 않습니다.");
			}
			
			
			// 조회된 글의 이전 글 번호
			int getPreBoardId = boardService.getPreBoardId(boardId);
			request.setAttribute("getPreBoardId", getPreBoardId);
			
			// 조회된 글의 다음 글 번호
			int getNextBoardId = boardService.getNextBoardId(boardId);
			request.setAttribute("getNextBoardId", getNextBoardId);
			
			
			
			//조회된 게시글 정보를 request 객체에 속성으로 저장
			//JSP페이지에서 ${board.title}과 같이 사용하기 위해, 조회된 BoardVO객체를 "board"라는 이름으로 request에 저장
			request.setAttribute("board", viewedBoard);
			
			
			// 이동할 JSP페이지 경로 설정하기
			// 메인화면 중앙에 보여줄 noticeInfo.jsp를 request에 "center"라는 이름으로 저장하기
			request.setAttribute("center", "board/noticeInfo.jsp");
			// 최종적으로 보여줄 메인페이지 경로를 nextPage에 저장하기
			nextPage = "/main.jsp";
		}
		

//새글추가-------------------------------------------------------------------		
		// 요청주소 "/bbs/AddNotice.do"
		// noticeWrite.jsp에서 글쓰기 버튼을 클릭하면 action="/bbs/AddNotice.do"로 요청이 들어옴
		// DB에 새글 추가 작업을 수행
		if (action.equals("/AddNotice.do")) {// 요청명이 AddNotice.do이면 글쓰기 처리

			// 추가한 새글 번호를 반환받아 저장할 변수
			// 반환 받는 이유는? 글번호 폴더를 생성하기 위함입니다.
//			int boardNO = 0;

			// uploadFile()메소드를 호출해 글쓰기 화면에서 첨부하여 전송된 글관련정보를
			// HashMap에 key/value 쌍으로 저장합니다.
			// 그런후....
			// 글입력시 추가적으로 업로드할 파일을 선택하여 글쓰기 요청을 헀다면
			// 업로드할 파일명, 입력한 글제목, 입력한 글내용을 key/value형태의 값들로 저장되어 있는 HashMap을 리턴받는다.
			// 그렇지 않을 경우에는??
			// 업로드할 파일명을 제외한 입력한 글제목, 입력한 글내용을 key/value형태의 값들로 저장되어 있는 HashMap을 리턴받는다
			Map<String, String> boardMap = uploadFile(request, response);

			// HashMap에 저장된 글정보들을 다시 꺼내옵니다.
			String title = boardMap.get("title");
			String content = boardMap.get("content");
			String file = boardMap.get("file");
			String bannerImage = boardMap.get("bannerImage");

			/*
			 * 
			 * private Boolean secret; //게시글 공개 여부 private String reply; //게시글 답변
			 */

			// DB에 추가하기 위해 사용자가 입력한 글정보+업로드할 파일명을 ArticleVO객체의 각변수에 저장
//			boardVO.setBoardId(boardNO);//추가할 새글의 글번호 -->  DAO에서 getMaxBoardId() 메소드를 호출하여 DB에 저장된 가장 최신 글번호를 검색해서 제공받아 저장할것임
			boardVO.setCategory(0);// 추가할 새글의 카테고리번호를 0으로 지정해서 공지사항으로 지정
			boardVO.setTitle(title);// 추가하기위해 입력한 글제목 저장
			boardVO.setContent(content);// 추가하기 위해 입력한 글내용 저장
			boardVO.setUserId("admin");// 추가할 새글 작성자 ID를 admin으로 저장 (참고. t_member테이블에 ID가 admin이 저장되어 있어야함)
			boardVO.setBookNo(1);// 추가할 새글의 도서번호 - 임의로 0으로 지정. 추후 book BD랑 연결해야함
			boardVO.setFile(file);// 새글 입력시 첨부해서 업로드한 파일명 저장
			boardVO.setBannerImg(bannerImage);// 새글 입력시 첨부해서 업로드한 배너파일명 저장
			boardVO.setDate(new Date(System.currentTimeMillis())); // 게시글 작성일을 현재시간으로 지정
			// currentTimeMillis() 메소드는 현재시간을 밀리세컨드로 리턴합니다 //나타나는형식 : 2023-10-12 17:00:00.0
			boardVO.setViews(0);// 조회수는 0으로 지정. 나중에 방문시 조회수 증가시켜야함
			boardVO.setSecret(false);// 비밀글 여부는 false로 지정 (false :공개 , true:비공개)

			// boardService객체를 통해 DB에 새글을 추가하는 메소드를 호출합니다
			// boardService의 addNotice 메소드는 DB에 성공적으로 등록된 후 할당된 새 글의 번호(articleNO)를 반환합니다.
			int boardId = boardService.addNotice(boardVO);

			// 첨부파일이 있는 경우 파일 이동
			moveFileToDestination(file, boardId, "첨부파일");

			// 배너 이미지가 있는 경우 파일 이동
			moveFileToDestination(bannerImage, boardId, "배너 이미지");

			// 글 등록 후, 새로 등록된 글 번호를 사용하여 해당 글을 조회하는 페이지로 리다이렉트합니다.
			// 전체글을 다시 DB에서 겅색하여 보여주기 위해 다음과 같은 주소를 저장
			nextPage = "/bbs/noticeList.do";

		}

		/*-------------------------------------문의게시판---------------------------------------*/
		// 문의게시판 조회하기
		// 요청주소 "/bbs/noticeList.do"
		if (action.equals("/questionList.do")) {

			// 조회된 게시판을
			boardList = boardService.getquestionList();

			// 조회된 목록을 request에 "boardList"라는 이름으로 저장하기
			request.setAttribute("boardList", boardList);

			// 메인화면 중앙에 보여줄 noticeList.jsp를 request에 "center"라는 이름으로 저장하기
			request.setAttribute("center", "board/questionList.jsp");

			// 최종적으로 보여줄 메인페이지 경로를 nextPage에 저장하기
			nextPage = "/main.jsp";

		}

		// nextPage변수에 값이 할당된 경우(즉, 직접 응답하지 않고 JSP(VIEW)로 포워딩 해야하는 경우)
		if (nextPage != null) {
			// 1. RequestDispatcher객체를 얻어옵니다
			// - request.getRequestDispatcher(경로): 지정된 경로의 JSP 페이지로 요청을 전달할 수 있는 객체를 생성합니다.
			RequestDispatcher dispatche = request.getRequestDispatcher(nextPage);

			// 2. forward()메소드를 호출하여 현재 요청(request)과 응답(response)객체를
			// nextPage변수에 저장한 JSP페이지로 전달합니다.
			// - 제어권이 해당 JSP페이지로 완전히 넘어 갑니다.
			dispatche.forward(request, response);

		}
	}// end of doHandle()

}// end class
