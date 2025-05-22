package Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Dao.boardDAO;
import Vo.boardVO;

public class boardService {

	boardDAO boardDao;

	public boardService() {
		boardDao = new boardDAO();
	}

	// 페이징된 게시글 목록과 관련 정보를 반환하는 메소드
	public Map<String, Object> getBoardList(int category, int section, int pageNum, String searchKeyword, String searchType, String currentUserId) {
		
		// 기본게시판들일경우 - 한 페이지에 보여줄 게시글의 수는 10개
		int pageSize = 10;
		
		// 섹션(section)마다 보여줄 페이지 수 (한 섹션에 5개의 페이지번호)
		int sectionSize = 5;
		
		// 페이지 번호에 따른 시작 게시글 번호 (LIMIT의 시작 번호)
		int startRow = (pageNum - 1) * pageSize;
		int endRow = startRow + pageSize;
		
		// DAO를 통해 페이징된 게시글 목록을 가져오기
		List<boardVO> boardList = boardDao.getBoardList(category, startRow, endRow, searchKeyword, searchType, currentUserId);
		
	    // 전체 게시글 수를 계산하여 전체 페이지 수 구하기
	    int totalBoardCount = boardDao.getTotalBoardCount(category, searchKeyword, searchType); // 전체 게시글 수
	    int totalPage = (int) Math.ceil(totalBoardCount / (double) pageSize);
	    int totalSection = (int) Math.ceil(totalPage / (double) sectionSize);

	    // 결과를 Map에 담아서 반환
	    Map<String, Object> resultMap = new HashMap<>();
	    resultMap.put("boardList", boardList);
	    resultMap.put("totalPage", totalPage);
	    resultMap.put("totalSection", totalSection);
	    resultMap.put("totalBoardCount", totalBoardCount);
	    
	    return resultMap;
	}


	

	// 게시글 추가를 위한 메소드.
	public int addBoard(boardVO boardVO) {
		System.out.println("BoardService - addNotice 호출됨 (제목: " + boardVO.getTitle() + ")");
		// 단순히 DAO의 insertBoard 메소드를 호출하여 글 추가 작업을 위임합니다.
		// 필요하다면 여기서 데이터 유효성 검사 등을 추가할 수 있습니다.
		// DAO로부터 추가된 글의 번호를 받아 그대로 Controller에게 반환합니다.
		return boardDao.insertBoard(boardVO);
	}
	

	// 특정 글번호(boardId)를 받아 해당 글의 상세 정보를 조회하도록 DAO에게 요청하는 메소드.
	public boardVO viewBoard(int boardId) {
		System.out.println("BoardService - viewBoard 호출됨 (글번호: " + boardId + ")");

		// 먼저 글이 실제로 존재하는지 확인
		boardVO board = boardDao.selectBoard(boardId);
		// DAO에게 해당 글번호에 대한 상세 정보를 요청하기 전에 먼저,
		// 해당 글 번호에 뷰 수를 1 증가키는 작업을 수행합니다.
		// 존재하는 글일 경우에만 조회수 증가
		if (board != null) {
			boardDao.increaseViewCount(boardId);
		}

		return board;
	}
	
	public boardVO viewBannerBoard(int boardId) {
		System.out.println("BoardService - viewBannerBoard 호출됨 (글번호: " + boardId + ")");

		// 먼저 글이 실제로 존재하는지 확인
		boardVO board = boardDao.selectBannerBoard(boardId);
		// DAO에게 해당 글번호에 대한 상세 정보를 요청하기 전에 먼저,
		// 해당 글 번호에 뷰 수를 1 증가키는 작업을 수행합니다.
		// 존재하는 글일 경우에만 조회수 증가
		if (board != null) {
			boardDao.increaseViewCount(boardId);
		}

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
	
	
	
	// 이전 글 번호를 조회하는 메소드 (행사게시판)
	public int getPreBannerBoardId(int currentBoardId) {
		return boardDao.getPreBannerBoardId(currentBoardId);
	}
	
	// 다음 글 번호를 조회하는 메소드 (행사게시판)
	public int getNextBannerBoardId(int currentBoardId) {
		return boardDao.getNextBannerBoardId(currentBoardId);
	}

	// 게시글 수정을 위한 메소드
	public void modifyBoard(boardVO modVO) {
		System.out.println("BoardService - modifyNotice 호출됨 (글 번호: " + modVO.getBoardId() + ")");
		// 단순히 DAO의 updateBoard 메소드를 호출하여 글 수정 작업을 위임합니다.
		// 필요하다면 여기서 데이터 유효성 검사 등을 추가할 수 있습니다.
		boardDao.updateBoard(modVO);
	}

	// 글 삭제 메소드
	public int removeBoard(int boardID) {
		System.out.println("boardService - removeBoard 호출됨 (글번호 :" + boardID + ")");
		
		// 글 삭제 작업을 dao에게 요청
		boardDao.deletBoard(boardID);
		System.out.println("boardService - 글 삭제 작업 완료 (DB)");

		// 삭제된 글ID 반환
		return boardID;

	}

	// 답변달기
	public boolean updateReply(int boardId, String reply) {

		return boardDao.updateReply(boardId, reply);
	}
	
	// 답변삭제
	public boolean deleteReply(int boardId) throws Exception {
		
		if(boardId <= 0) {
			throw new IllegalArgumentException("잘못된 게시글 ID입니다.");
		}
		
		return boardDao.deleteReply(boardId);
	}

	
	
	// 이벤트리스트 게시글 목록과 관련 정보를 반환하는 메소드 (페이징 포함)
	public Map<String, Object> getEventBoardList(int section, int pageNum, String searchKeyword, String searchType) {
	    // 한 페이지에 보여줄 게시글의 수
	    int pageSize = 12;
	    
	    // 섹션(section)마다 보여줄 페이지 수 (한 섹션에 5개의 페이지번호)
	    int sectionSize = 5;
	    
	    // 페이지 번호에 따른 시작 게시글 번호 (LIMIT의 시작 번호)
	    int startRow = (pageNum - 1) * pageSize;
	    int endRow = startRow + pageSize;
	    
	    // 배너가 있는 게시글 목록을 DAO에서 가져오기
	    List<boardVO> boardList = boardDao.getBannerList(startRow, endRow, searchKeyword, searchType);
	    
	    // 전체 게시글 수를 계산하여 전체 페이지 수 구하기
	    int totalBoardCount = boardDao.getTotalBannerCount(searchKeyword, searchType); // 배너가 있는 게시글의 전체 개수
	    int totalPage = (int) Math.ceil(totalBoardCount / (double) pageSize);
	    int totalSection = (int) Math.ceil(totalPage / (double) sectionSize);
	    
	    // 결과를 Map에 담아서 반환
	    Map<String, Object> resultMap = new HashMap<>();
	    resultMap.put("boardList", boardList);
	    resultMap.put("totalPage", totalPage);
	    resultMap.put("totalSection", totalSection);
	    resultMap.put("totalBoardCount", totalBoardCount);
	    
	    return resultMap;
		
	}

	//북 넘버에 맞는 서평 목록 조회
	public List<boardVO> getReviewsByBookNo(int bookNo) {
		
		return boardDao.getReviewsByBookNo(bookNo);
	}
	
	
	//메인화면에 뿌릴 데이터들 
	public List<boardVO> getLatestEventBanners(int i) {
		
		return boardDao.getLatestEventBanners(i);
	}

	public List<boardVO> getLatestNotices(int i) {
		return boardDao.getLatestNotices(i);
	}



	
}
