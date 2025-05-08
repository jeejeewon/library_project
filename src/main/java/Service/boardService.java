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

	// 공지사항 게시글 추가를 위한 메소드.
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
		
		// DAO에게 해당 글번호에 대한 상세 정보를 요청하기 전에 먼저,
		// 해당 글 번호에 뷰 수를 1 증가키는 작업을 수행합니다.
		boardDao.increaseViewCount(boardId);
		
		boardVO board = boardDao.selectBoard(boardId);
		return board;
	}

	// 이전 글 번호를 조회하는 메소드
	public int getPreBoardId(int currentBoardId, int category) {
	    return boardDao.getPreBoardId(currentBoardId, category); // DAO에 category 전달
	}

	// 다음 글 번호를 조회하는 메소드
	public int getNextBoardId(int currentBoardId, int category) {
	    return boardDao.getNextBoardId(currentBoardId, category); // DAO에 category 전달
	}

	// 공지사항 게시글 수정을 위한 메소드
	public void modifyNotice(boardVO modVO) {
		System.out.println("BoardService - modifyNotice 호출됨 (글 번호: " + modVO.getBoardId() + ")");
		// 단순히 DAO의 updateBoard 메소드를 호출하여 글 수정 작업을 위임합니다.
		// 필요하다면 여기서 데이터 유효성 검사 등을 추가할 수 있습니다.
		boardDao.updateBoard(modVO);
	}
	

}
