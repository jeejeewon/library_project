package Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/download.do")
public class FileDownloadController extends HttpServlet {

//	public static String BOARD_FILE_REPO = "C:\\workspace_libraryProject\\library_project\\src\\main\\webapp\\board\\board_file_repo";

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

		String boardId = req.getParameter("boardId");
		String type = req.getParameter("type"); // "file" or "banner"
		String fileName = ""; // 요청 파라미터에서 파일 이름 가져옴

		if ("file".equals(type)) {
			fileName = req.getParameter("file");
		} else if ("banner".equals(type)) {
			fileName = req.getParameter("bannerImg");
		}

		if (fileName == null || fileName.trim().isEmpty()) {
			resp.getWriter().write("<script>alert('파일명이 존재하지 않습니다.'); history.back();</script>");
			return;
		}

		 // 1. ServletContext 객체를 가져옴!
        ServletContext servletContext = req.getServletContext(); // HttpServletRequest 객체에서 가져옴!
        // 2. 웹 애플리케이션 루트 경로 가져오기
        String webappRootPath = servletContext.getRealPath("/"); // 실제 루트 경로!
        // 3. 최종 업로드 기본 경로 계산 (BOARD_FILE_REPO 대신 사용)
        String finalUploadBasePath = webappRootPath + "board" + File.separator + "board_file_repo";
        // 4. 다운로드할 파일의 실제 전체 경로 계산 (BOARD_FILE_REPO 대신 finalUploadBasePath 사용)
        String filePath = finalUploadBasePath + File.separator + boardId + File.separator + fileName;
        // ==> BOARD_FILE_REPO + "\\" + boardId + "\\" + fileName; 이거 대신 계산된 filePath 사용
        File downloadFile = new File(filePath); // <-- 계산된 filePath 사용
        
        
//		File downloadFile = new File(BOARD_FILE_REPO + "\\" + boardId + "\\" + fileName);

        // 5. 파일 존재 확인
		if (!downloadFile.exists()) {
			System.err.println("요청된 파일을 찾을 수 없습니다: " + filePath); // 서버 로그에 파일 못 찾은 경로 남기기
			resp.getWriter().write("<script>alert('요청하신 파일이 존재하지 않습니다.'); history.back();</script>");
			return;
		}
		
		// 6. HTTP 응답 헤더 설정 (다운로드 파일명, 캐시 방지 등)
		// 한글 파일명 대응
		String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
		resp.setHeader("Cache-Control", "no-cache");
		
		// 파일 타입에 맞는 Content-Type 설정이 이미지 표시
        // Mime Type 설정 추가
		String mimeType = servletContext.getMimeType(downloadFile.getAbsolutePath());
        if (mimeType == null) {
            // 기본값 설정 (예: 알 수 없는 파일 타입)
            mimeType = "application/octet-stream";
        }
        resp.setContentType(mimeType); // ==> 파일 타입에 맞게 Content-Type 설정
		
        // Content-Disposition 설정 (attachment: 다운로드, inline: 브라우저 표시)
        // 이미지 파일은 inline으로 해서 브라우저에 바로 보이도록 하는 게 좋아!
        // 첨부파일은 attachment로 해서 다운로드 되도록!
        if ("file".equals(type)) {
            resp.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\""); // 첨부파일 -> 다운로드
        } else if ("banner".equals(type)) {
             resp.setHeader("Content-Disposition", "inline; filename=\"" + encodedFileName + "\""); // 배너 이미지 -> 브라우저에 바로 표시
        } else {
            // type이 이상하면 기본값으로 attachment
             resp.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
        }
//		resp.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");

        
        // 7. 파일 읽어서 브라우저에 스트림으로 보내주기
        try (OutputStream out = resp.getOutputStream(); FileInputStream in = new FileInputStream(downloadFile)) {
			byte[] buffer = new byte[1024 * 8];
			int count;
			while ((count = in.read(buffer)) != -1) {
				out.write(buffer, 0, count);
			}
            System.out.println("파일 다운로드/표시 완료: " + filePath); // 성공 로그
		} catch (IOException e) {
             System.err.println("파일 스트림 처리 중 오류 발생: " + filePath + " - " + e.getMessage()); // 스트림 오류 로그
             // TODO: 스트림 오류 발생 시 사용자에게 오류 메시지를 보여주거나 다른 처리 필요
        }
		
	}// doHandle end
}// FileDownloadController end
