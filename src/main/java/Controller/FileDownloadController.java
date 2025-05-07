package Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/download.do")
public class FileDownloadController extends HttpServlet{
	
	// 다운로드 시킬 이미지 파일이 저장된 글번호 폴더 위치의 상위 폴더
	public static String BOARD_FILE_REPO = "C:\\workspace_libraryProject\\library_project\\src\\main\\webapp\\board\\board_file_repo";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doHandle(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doHandle(req, resp);
	}
	
	protected void doHandle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=utf-8");
		resp.setCharacterEncoding("utf-8");
		
		//요청한 값 얻기
		String bannerImg = req.getParameter("bannerImg"); //보여질 배너 이미지
		String file = req.getParameter("file"); //다운로드할 첨부파일
		String boardId = req.getParameter("boardId"); //게시판 글번호
		
		//웹브라우저로 데이터를 바이트 단위로 내보낼 출력스트림 통로 생성
		OutputStream out = resp.getOutputStream();
		
		//글번호 폴더 내부에 다운로드할 파일에 대한 파일 경로 설정
		File fileDownload = new File(BOARD_FILE_REPO + "\\" + boardId + "\\" + file);
		
		// 캐시 사용 방지
		// Cache-Control: no-cache는 브라우저가 이 응답을 저장하거나 재사용하지 않도록 합니다.
		// 즉, 항상 서버에서 새로운 응답을 받도록 합니다.
		resp.setHeader("Cache-Control", "no-cache");
	
		// 응답 헤더에 Content-disposition을 추가하여
		// 브라우저가 이미지를 직접 표시하는 대신 다운로드하도록 설정합니다.
		// attachment는 다운로드를 의미하고,
		// fileName=...은 다운로드할 파일의 이름을 지정합니다.
		resp.addHeader("Content-Disposition", "attachment; filename=" + file);

		//이미지 파일을 자바에서 읽어오기위해 FileInputStream통로 생성
		//이 스트림은 파일을 바이트 형태로 읽어오는 역할을 합니다.
		FileInputStream in = new FileInputStream(fileDownload);
		
		//파일을 조금씩 나누어 읽기 위해 버퍼를 준비. 여기서는 8KB씩 읽어옴
		byte[] buffer = new byte[1024 * 8];
		
		// 무한반복문을 사용해서 파일을 끝까지 읽을때까지 반복한다
		while(true) {
			//읽어온 파일을 저장할 변수
			int count = in.read(buffer);
			//여기서 .read()메소드는 읽어온 바이트 수를 반환한다.
			//읽어온 바이트 수가 0이면 더이상 읽을 파일이 없다는 의미, 파일의 끝에 도달하면 -1을 반환한다.
			
			//파일을 다 읽어오면 -1이 반환되므로 반복문 종료
			if(count == -1) break;
			
			//읽어온 파일을 웹브라우저로 내보내기
			//buffer배열에서 0부터 count만큼의 바이트를 실제로 보냅니다.
			out.write(buffer, 0, count);
		}
		
		//자원해제
		in.close(); //파일 읽기 통로 닫기
		out.close(); //웹브라우저로 내보내기 통로 닫기
		
		
	}

	
	

}