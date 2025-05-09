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
	public static final String BOARD_FILE_REPO = "C:\\workspace_libraryProject\\library_project\\src\\main\\webapp\\board\\board_file_repo";

	// 파일 업로드 처리 메소드
	public Map<String, String> uploadFile(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException, FileUploadException {

	    Map<String, String> uploadMap = new HashMap<>();
	    String encoding = "utf-8";

	    // 업로드할 파일을 임시로 저장할 폴더 생성
	    File currentDirPath = new File(BOARD_FILE_REPO);
	    File tempDir = new File(currentDirPath, "temp");

	    if (!tempDir.exists()) {
	        tempDir.mkdirs();  // temp 폴더가 없으면 생성
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
	        List items = upload.parseRequest(request);

	        for (int i = 0; i < items.size(); i++) {
	            FileItem fileItem = (FileItem) items.get(i);

	            // 일반 입력 값 처리 (ex. 제목, 내용 등)
	            if (fileItem.isFormField()) {
	                uploadMap.put(fileItem.getFieldName(), fileItem.getString(encoding));
	            } else { // 파일 업로드 처리
	                String fieldName = fileItem.getFieldName(); // input 태그 name 속성
	                String originalFileName = fileItem.getName(); // 사용자가 올린 원본 파일 이름
	                long fileSize = fileItem.getSize(); // 파일 크기

	                if (fileSize > 0) { // 파일이 존재하는 경우
	                    String fileNameOnly = new File(originalFileName).getName(); // 경로 제거 후 파일명만 추출

	                    // 업로드된 파일을 임시 폴더에 저장
	                    File uploadFile = new File(tempDir, fileNameOnly);
	                    fileItem.write(uploadFile);

	                    // 결과 맵에 파일 이름 저장 (이후 이동을 위해)
	                    uploadMap.put(fieldName, fileNameOnly);
	                } else { // 파일이 비어 있는 경우
	                    uploadMap.put(fieldName, "");
	                }
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
	private void moveFileToDestination(String fileName, int boardId, String fileType) {
	    if (fileName != null && !fileName.isEmpty()) {

	        // 임시 저장된 파일 위치
	        File srcFile = new File(BOARD_FILE_REPO + File.separator + "temp" + File.separator + fileName);

	        // 최종 저장 폴더 경로 (글 번호를 폴더 이름으로 사용)
	        File destDir = new File(BOARD_FILE_REPO + File.separator + boardId);

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

			// 조회된 게시판을
			boardList = boardService.getNoticeList();

			// 조회된 목록을 request에 "boardList"라는 이름으로 저장하기
			request.setAttribute("boardList", boardList);

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

		}// end of AddNotice.do

		
		
		
		
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
			
			
			// 컨트롤러에서 카테고리 값 받기 (기본값은 0으로 설정)
			int category = 0; // 기본값
			String categoryStr = request.getParameter("category");
			if (categoryStr != null) {
			    category = Integer.parseInt(categoryStr); // 카테고리 값이 있으면 파싱하여 사용
			}
			
			// 이전 글 번호 조회
			int getPreBoardId = boardService.getPreBoardId(boardId, category);
			request.setAttribute("getPreBoardId", getPreBoardId);

			// 다음 글 번호 조회
			int getNextBoardId = boardService.getNextBoardId(boardId, category);
			request.setAttribute("getNextBoardId", getNextBoardId);
			
			
			
			//조회된 게시글 정보를 request 객체에 속성으로 저장
			//JSP페이지에서 ${board.title}과 같이 사용하기 위해, 조회된 BoardVO객체를 "board"라는 이름으로 request에 저장
			request.setAttribute("board", viewedBoard);
			
			
			// 이동할 JSP페이지 경로 설정하기
			// 메인화면 중앙에 보여줄 noticeInfo.jsp를 request에 "center"라는 이름으로 저장하기
			request.setAttribute("center", "board/noticeInfo.jsp");
			// 최종적으로 보여줄 메인페이지 경로를 nextPage에 저장하기
			nextPage = "/main.jsp";
			
		}// end of noticeInfo.do
		
		
		
		
		
		// 공지사항 글 수정하기
		// 상세페이지에서 수정 버튼을 눌렀을때 수정페이지 요청
		// 요청주소 "/bbs/noticeModifyForm.do"
		if (action.equals("/noticeModifyForm.do")) {
			System.out.println("공지사항 글 수정 페이지 요청 시작...");
			
			// 글번호 파라미터 수신
			String boardIdParam = request.getParameter("boardId");
			System.out.println("요청된 글 번호 파라미터 : " + boardIdParam);
			
			// 파라미터 유효성 검사 : null이거나 빈 문자열인 경우 오류 처리
			if(boardIdParam == null || boardIdParam.isEmpty()) {
				System.out.println("오류 : 글 수정 요청에 글번호(boardId) 파라미터 누락.");
				throw new ServletException("글 수정 요청 시 글번호(boardId) 파라미터가 필요합니다.");
			}
			
			//문자열로 된 글 번호를 int형으로 변환
			int boardId = Integer.parseInt(boardIdParam);
			
			// 글번호에 해당하는 게시글을 DB에서 조회
			boardVO modBoard = boardService.viewBoard(boardId);
			System.out.println("Service에서 조회된 글 정보 : " + (modBoard != null ? "BoardId=" + modBoard.getBoardId() : "null"));//조회 결과 로그
			
			//조회된 글이 없는 경우(삭제되었거나 잘못된 번호 요청시) 예외처리
			if(modBoard == null) {
				System.out.println("오류 : 글 번호 " + boardId + "에 해당하는 글이 존재하지 않습니다.");
				throw new ServletException("요청하신 글 번호 " + boardId + "에 해당하는 게시글이 존재하지 않습니다.");
			}
			
			//조회된 게시글 정보를 request 객체에 속성으로 저장
			//JSP페이지에서 ${board.title}과 같이 사용하기 위해, 조회된 BoardVO객체를 "board"라는 이름으로 request에 저장
			request.setAttribute("board", modBoard);
			
			// 이동할 JSP페이지 경로 설정하기
			// 메인화면 중앙에 보여줄 noticeModifyForm.jsp를 request에 "center"라는 이름으로 저장하기
			request.setAttribute("center", "board/noticeModifyForm.jsp");
			
			// 최종적으로 보여줄 메인페이지 경로를 nextPage에 저장하기
			nextPage = "/main.jsp";
			
		}// end of noticeModifyForm.do
		
		
		// 수정페이지에서 (수정을 다 하고,) 수정버튼 눌렀을때 수정처리 요청
		// 요청주소 "/bbs/noticeModify.do"
		if (action.equals("/noticeModify.do")){
			System.out.println("공지사항 글 수정페이지로 이동 요청 시작...");
			
			//파일 업로드를 포함한 수정된 폼 데이터 처리
			// 수정 폼에서도 파일첨부가 가능하므로 uploadFile() 메소드를 호출
			// 반환된 Map에는 수정된 글 제목, 내용, 첨부파일 등등의 정보가 담겨있습니다.
			Map<String, String> boardMap = uploadFile(request, response);
			System.out.println("uploadFile()메소드 Map (수정): " + boardMap);
			
			//Map에서 수정 정보 추출
			String boardIdParam = boardMap.get("boardId"); // 수정할 글 번호
			// 글 번호 유효성 검사
			if(boardIdParam == null || boardIdParam.isEmpty()) {
				System.out.println("오류 : 글 수정 요청에 글번호(boardId) 파라미터 누락.");
				throw new ServletException("글 수정 요청 시 글번호(boardId) 파라미터가 필요합니다.");
			}
			int boardId = Integer.parseInt(boardIdParam); // 글 번호를 int형으로 변환
			String title = boardMap.get("title"); // 수정된 제목 추출
			String content = boardMap.get("content"); // 수정된 내용 추출
			String file = boardMap.get("file"); // 수정된 첨부파일 추출
			String originalFileName = boardMap.get("originalFileName"); // 폼에 hidden 필드로 전달된 기존 첨부 파일 이름 (파일 변경 시 기존 파일 삭제용)
			String bannerImage = boardMap.get("bannerImage"); // 수정된 배너 이미지 추출
			String originalBannerName = boardMap.get("originalBannerName"); // 폼에 hidden 필드로 전달된 기존 첨부 파일 이름 (파일 변경 시 기존 파일 삭제용)
			System.out.println("추출된 수정 정보 : " + "boardId=" + boardId + ", title=" + title + ", content=" + content + ", file=" + file + ", bannerImage=" + bannerImage);
			
			// boardVO 객체에 수정된 정보 저장
			// 데이터베이스에 업데이트를 하기위해 수정된 정보와 글 번호를 boardVO 객체에 저장합니다.
			// 주의 : 멤버 변수 boardVO 재사용시, 스레드 안전 문제 가능성이 있으므로 새 객체를 생성하는것이 안전합니다.
			boardVO modVO = new boardVO();//수정 정보를 담을 새 VO객체
			modVO.setBoardId(boardId); // 수정할 글 번호  (WHERE절에서 사용됩니다.)
			modVO.setTitle(title); // 수정된 제목
			modVO.setContent(content); // 수정된 내용
			modVO.setFile(file); // 수정된 첨부파일
			modVO.setBannerImg(bannerImage); // 수정된 배너 이미지
			//그 외 작성자, 작성일 등은 수정하지 않으므로 그대로 둡니다.
			
			// boardService를 통해 글 수정 처리 요청
			// boardService에게 movVO 객체를 전달하여 DB에서 해당 글의 내용을 업데이트 하도록 요청합니다.
			boardService.modifyNotice(modVO);
			System.out.println("공지사항 글 수정 완료"); //수정 완료 로그
			
			
			
			// 첨부파일 처리
			// 수정된 첨부파일이 있는 경우 파일 이동
			if (file != null && !file.isEmpty()) {
				// 새 파일을 임시 폴더(temp)에서 최종 폴더(글번호 폴더)로 이동
				File srcFile = new File(BOARD_FILE_REPO + File.separator + "temp" + File.separator + file);//임시 폴더에 저장된 파일
				File destDir = new File(BOARD_FILE_REPO + File.separator + boardId); // 최종 저장 폴더 (글번호 폴더)
				
				// 최종 폴더가 없다면 생성
				if (!destDir.exists()) { destDir.mkdirs();}
				// 임시 파일 존재 확인 후 이동
				if (srcFile.exists()) {
					FileUtils.moveFileToDirectory(srcFile, destDir, true); // 파일 이동
					System.out.println("첨부파일 이동 완료: " + srcFile.getPath() + " -> " + destDir.getPath()); // 이동 완료 로그
				}else {
					System.out.println("오류 : 임시 파일을 찾을 수 없습니다: " + srcFile.getPath());
				}
				
			    // 기존 첨부파일 처리 : 
				// 기존 파일이 있었고, 새로 첨부된 파일과 이름이 다른 경우에만 기존 파일 삭제
			    if (originalFileName != null && !originalFileName.isEmpty() && !originalFileName.equals(file)) {
			        File oldFile = new File(destDir, originalFileName);
			        // 기존 파일이 존재하는 경우 삭제
			        if (oldFile.exists()) {
			            boolean deleted = oldFile.delete();
			            System.out.println("기존 첨부 파일 삭제 ( " + oldFile.getPath() + " ) : " + deleted);//삭제 결과 로그
			        }
			    }

			}
			
			// 수정된 배너 이미지가 있는 경우 파일 이동
			if (bannerImage != null && !bannerImage.isEmpty()) {
			    if (originalBannerName != null && !originalBannerName.isEmpty()) {
			        File oldBanner = new File(BOARD_FILE_REPO + File.separator + boardId + File.separator + originalBannerName);
			        if (oldBanner.exists()) {
			            oldBanner.delete();
			            System.out.println("기존 배너 이미지 삭제됨: " + oldBanner.getPath());
			        }
			    }

			    moveFileToDestination(bannerImage, boardId, "배너 이미지");
			}
			
			
			// 수정 후 상세페이지로 리디렉션
			response.sendRedirect(request.getContextPath() + "/bbs/noticeInfo.do?boardId=" + boardId);

	
			
		}// end of noticeModify.do
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		


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
