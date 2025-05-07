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


	public int addNotice(boardVO boardVO) {
		System.out.println("BoardService - addNotice 호출됨 (제목: " + boardVO.getTitle() + ")");
		// 단순히 DAO의 insertBoard 메소드를 호출하여 글 추가 작업을 위임합니다.
		// 필요하다면 여기서 데이터 유효성 검사 등을 추가할 수 있습니다.
		// DAO로부터 추가된 글의 번호를 받아 그대로 Controller에게 반환합니다.
		return boardDao.insertBoard(boardVO);
	}

	// 특정 글번호(boardId)를 받아 해당 글의 상세 정보를 조회하도록 DAO에게 요청하는 메소드.
	public boardVO viewBoard(int boardId) {
		System.out.println("BoardService - viewBoard 호출됨 (글번호: " + boardId + ")");
		boardVO board = boardDao.selectBoard(boardId);
		return board;
	}


	public int getPreBoardId(int boardId) {
		return boardDao.getPreBoardId(boardId);
	}


	public int getNextBoardId(int boardId) {
		return boardDao.getNextBoardId(boardId);
	}
	

}
