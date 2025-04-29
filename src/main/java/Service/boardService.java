package Service;


import java.util.List;
import java.util.Map;
import java.util.Vector;

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
	
	

}
