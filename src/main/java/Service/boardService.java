package Service;


import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAO.boardDAO;
import VO.boardVO;

public class boardService {
	
	boardDAO boardDao;
	
	
	public boardService() {
		boardDao = new boardDAO();
	}


	public Vector<boardVO> getAllBoardList() {

		return boardDao.getAllBoardList();
		
		
	}
	
	public Vector<boardVO> getNoticeList() {

		return boardDao.getNoticeList();
		
	}
	
	public Vector<boardVO> getquestionList() {
		
		return boardDao.getquestionList();
		
	}

	
	public Map<String, String> uploadFile(HttpServletRequest request, HttpServletResponse response) {
		
		return boardDao.uploadFile(request, response);
	}
	
	

}
