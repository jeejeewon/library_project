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
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

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
		doHandle(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doHandle(request, response);
	}

	private void doHandle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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
		if(action.equals("/noticeWrite.do")) {//요청명이 noticeWrite.do이면 글쓰기 화면이 나타남
			
			// 메인화면 중앙에 보여줄 noticeList.jsp를 request에 "center"라는 이름으로 저장하기
			request.setAttribute("center", "board/noticeWrite.jsp");

			// 최종적으로 보여줄 메인페이지 경로를 nextPage에 저장하기
			nextPage = "/main.jsp";
		}
		
		
//새글추가-------------------------------------------------------------------		
		// 요청주소 "/bbs/AddNotice.do"
		//noticeWrite.jsp에서 글쓰기 버튼을 클릭하면 action="/bbs/AddNotice.do"로 요청이 들어옴
		// DB에 새글 추가 작업을 수행
		if(action.equals("/AddNotice.do")) {//요청명이 AddNotice.do이면 글쓰기 처리
		
			// 추가한 새글 번호를 반환받아 저장할 변수 
			// 반환 받는 이유는? 글번호 폴더를 생성하기 위함입니다.
//			int boardNO = 0;
			
			//uploadFile()메소드를 호출해 글쓰기 화면에서 첨부하여 전송된 글관련정보를 
			//HashMap에  key/value 쌍으로 저장합니다.
			//그런후....
			//글입력시 추가적으로 업로드할 파일을 선택하여 글쓰기 요청을 헀다면
			//업로드할 파일명, 입력한 글제목, 입력한 글내용을 key/value형태의 값들로 저장되어 있는  HashMap을 리턴받는다.
			//그렇지 않을 경우에는??
			//업로드할 파일명을 제외한 입력한 글제목, 입력한 글내용을 key/value형태의 값들로 저장되어 있는 HashMap을 리턴받는다
			Map<String, String> boardMap = uploadFile(request,response);
			
			//HashMap에 저장된 글정보들을 다시 꺼내옵니다.
			String title = boardMap.get("title");
			String content = boardMap.get("content");
			String uploadFile = boardMap.get("uploadFile");
			String bannerImage = boardMap.get("bannerImage");
			
	/*		
			
			private Boolean secret;  //게시글 공개 여부
			private String reply;  //게시글 답변
		*/	
			
			
			//DB에 추가하기 위해  사용자가 입력한 글정보+업로드할 파일명을 ArticleVO객체의 각변수에 저장
//			boardVO.setBoardId(boardNO);//추가할 새글의 글번호 -->  DAO에서 getMaxBoardId() 메소드를 호출하여 DB에 저장된 가장 최신 글번호를 검색해서 제공받아 저장할것임
			boardVO.setCategory(0);//추가할 새글의 카테고리번호를 0으로 지정해서 공지사항으로 지정
			boardVO.setTitle(title);//추가하기위해 입력한 글제목 저장
			boardVO.setContent(content);//추가하기 위해 입력한 글내용 저장
			boardVO.setUserId("admin");//추가할 새글 작성자 ID를  admin으로 저장 (참고. t_member테이블에 ID가 admin이 저장되어 있어야함)
//			boardVO.setBookNo(0);//추가할 새글의 도서번호 - 임의로 0으로 지정. 추후 book BD랑 연결해야함
			boardVO.setFile(uploadFile);//새글 입력시 첨부해서 업로드한 파일명 저장
			boardVO.setBannerImg(bannerImage);//새글 입력시 첨부해서 업로드한 배너파일명 저장
			boardVO.setDate(new Date(System.currentTimeMillis())); //게시글 작성일을 현재시간으로 지정
			//currentTimeMillis() 메소드는 현재시간을 밀리세컨드로 리턴합니다 //나타나는형식 : 2023-10-12 17:00:00.0
			boardVO.setViews(0);//조회수는 0으로 지정. 나중에 방문시 조회수 증가시켜야함
			boardVO.setSecret(false);//비밀글 여부는 false로 지정 (false :공개 , true:비공개)
			
			
			
			//boardService객체를 통해 DB에 새글을 추가하는 메소드를 호출합니다
			int boardId = boardService.addNotice(boardVO);
			
			
			
			
			//uploadFile() 메소드를 호출하여 업로드된 파일을 저장하고, 일반적인 폼데이터를 HashMap에 저장합니다
			Map<String, String> writeMap = boardService.uploadFile(request, response);
			
			writeMap.get("title");
			
			// 전체글을 다시 DB에서 겅색하여 보여주기 위해 다음과 같은 주소를 저장
			nextPage = "/bbs/noticeList.do";
		}
		
		
/*-------------------------------------문의게시판---------------------------------------*/		
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
	
	//글에 첨부할 파일을 저장할 위치를 상수로 선언
		public static final String BOARD_FILE_REPO = "C:\\workspace_libraryProject\\library_project\\src\\main\\webapp\\board\\board_file_repo";
		//파일 업로드 처리를 위한 메소드
		public Map<String, String> uploadFile(HttpServletRequest request, HttpServletResponse response) {
			
			Map<String, String> uploadMap = new HashMap<String, String>();
			String encoding = "utf-8";
			
			//글쓰기를 할때 첨부한 파일을 저장할 폴더 경로에대해 파일 객체를 생성합니다
			File currentDirPath = new File(BOARD_FILE_REPO);
			
			//업로드 할 파일 데이터를 임시로 저장할 객체메모리 생성
			// ▪ DiskFileItemFactory 클래스는 업로드할 파일을 메모리에 저장할 객체를 생성하는 클래스입니다
			DiskFileItemFactory factory = new DiskFileItemFactory();
			
			factory.setSizeThreshold(1024 * 1024 * 3); //3MB 파일업로드시 사용할 임시메모리 최대크기 3메가 바이트
			factory.setRepository(currentDirPath); //임시 메모리에 파일 업로드시, 지정한 3MB를 초과하는 파일은 지정한 경로에 저장
			//참고 : DiskFileItemFactory 클래스는 업로드 파일의 크기가 지정한크리를 넘기 전까지는 파일데이터를 메모리에 저장하고, 지정한 크기를 넘기면 지정한 경로에 저장합니다.
			
			//파일을 업로드할 메모리를 생성자쪽으로 전달받아 저장한 파일업로드를 처리할 객체를 생성합니다
			// -ServletFileUpload 클래스는 업로드된 파일을 처리하는 메소드가 정의되어 있습니다
			ServletFileUpload upload = new ServletFileUpload(factory);
			try {
				//request에 담긴 파일업로드 요청을 처리하여 List형태로 변환합니다
				// ▪ request에는 업로드할 파일에 대한 요청정보를 가지고있다.
				// ▪ parseRequest() 메소드는 request에 담긴 파일업로드 요청을 처리하여 List형태로 변환하여 리턴합니다
				List items = upload.parseRequest(request);
				// 위에 items는 List형태로 변환된 파일업로드 요청을 담고 있습니다
				// 파일업로드요청에는 일반적인 폼데이터와 파일업로드 요청이 섞여있습니다
				
				//List형태로 변환된 파일업로드 요청을 반복문을 통해서 하나씩 꺼내서 처리합니다
				// ArrayList 크기만큼 반복
				for(int i=0; i<items.size(); i++) {
					
					//하나씩 꺼낸 파일업로드 요청을 FileItem 객체에 저장합니다
					// ▪ FileItem 객체는 업로드된 파일에 대한 정보를 가지고 있습니다
					FileItem fileItem = (FileItem)items.get(i);
					
					
					//파일업로드 요청이 아닌 일반적인 폼데이터인 경우
					//즉 DiskFileItemFactory 객체(업로드할 아이템 하나의 정보)가 파일 아이템이 아닐 경우.
					// ▪ isFormField() 메소드는 업로드된 파일이 아닌 일반적인 폼데이터인 경우 true를 리턴합니다
					// ▪ 일반적인 폼데이터란 게시판 제목, 내용, 작성자, 비밀번호 등과 같은 데이터입니다
					if(fileItem.isFormField()) {
						
						System.out.println(fileItem.getFieldName() + " : " + fileItem.getString(encoding));
						//위 encoding 매개변수는 한글이 깨지지 않도록 인코딩을 utf-8로 설정함
						
						//noticeWrite.jsp에서 입력한 제목, 내용만 따로 HashMap에 저장합니다.
						// ▪ HasMap은 key와 value로 이루어진 자료구조
						// ▪ HashMap에 저장된 데이터의 예 : {title=제목, content=내용}
						uploadMap.put(fileItem.getFieldName(), fileItem.getString(encoding));
						
						
					//DiskFileItemFactory 객체(업로드할 아이템 하나의 정보)가 파일 아이템인 경우 업로드를 진행한다.
					} else { //파일업로드 요청
						
						System.out.println("파라미터명:"+fileItem.getFieldName());
						System.out.println("파일이름:"+fileItem.getName());
						System.out.println("파일크기:"+fileItem.getSize()+"byte");
						
						//noticeWrite.jsp에서 입력한
						uploadMap.put(fileItem.getFieldName(), fileItem.getName());
						
						
						//업로드할 파일의 파일이름을 저장소에 업로드하기
						if(fileItem.getSize() > 0) {//파일 크기가 0보다 크다면? 즉, 업로드할 파일이 존재한다면,
							
							
							//업로드할 파일명을 얻어 파일명의 뒤에서부터 \\ 문자열이 들어있는지 인덱스 위치를 알려줌. 없으면 -1을 리턴
							//▪lastIndexOf() 메소드는 문자열의 마지막에서부터 지정한 문자가 있는지 확인하는 메소드입니다.
							int idx = fileItem.getName().lastIndexOf("\\");//뒤에서부터 문자가 들어있는 인덱스를 알려준다
							if(idx == -1) {//\\문자가 없다면
								idx = fileItem.getName().lastIndexOf("/");//-1얻기
							}
							
							String fileName = fileItem.getName().substring(idx + 1);//업로드한 파일명 얻어서 저장
							// ▪ substring(idx + 1) 메소드는 idx+1부터 끝까지 잘라내는 메소드입니다
							// ▪ idx에는 \\문자가 들어있는 인덱스가 저장되어있다.
							
							File uploadFile = new File(currentDirPath, "\\" + fileName);//업로드할 파일 경로 + 파일명에대한 객체 생성
							
							fileItem.write(uploadFile);//업로드할 파일을 저장소에 업로드하기!!!!!
						} //end if
					} //end if
				} //end for	
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return uploadMap;
			//uploadMap에는 업로드한 파일의 정보가 담겨있다.
	
	
		}//end uploadFile()	
	
}//end class
