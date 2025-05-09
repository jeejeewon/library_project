package Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/download.do")
public class FileDownloadController extends HttpServlet {

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

		String boardId = req.getParameter("boardId");
		String type = req.getParameter("type"); // "file" or "banner"
		String fileName = "";

		if ("file".equals(type)) {
			fileName = req.getParameter("file");
		} else if ("banner".equals(type)) {
			fileName = req.getParameter("bannerImg");
		}

		if (fileName == null || fileName.trim().isEmpty()) {
			resp.getWriter().write("<script>alert('파일명이 존재하지 않습니다.'); history.back();</script>");
			return;
		}

		File downloadFile = new File(BOARD_FILE_REPO + "\\" + boardId + "\\" + fileName);

		if (!downloadFile.exists()) {
			resp.getWriter().write("<script>alert('요청하신 파일이 존재하지 않습니다.'); history.back();</script>");
			return;
		}

		// 한글 파일명 대응
		String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
		resp.setHeader("Cache-Control", "no-cache");
		resp.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");

		try (OutputStream out = resp.getOutputStream(); FileInputStream in = new FileInputStream(downloadFile)) {
			byte[] buffer = new byte[1024 * 8];
			int count;
			while ((count = in.read(buffer)) != -1) {
				out.write(buffer, 0, count);
			}
		}
		
		
		
	}// doHandle end
}// FileDownloadController end
