package Service;

import java.util.List;
import java.util.Map;

import Dao.libraryReserveDAO;
import Vo.libraryReserveVO;


//시설 예약 관련 비즈니스 로직 처리 Service
public class roomReserveService {

	//DB 작업할 DAO 객체 생성
	libraryReserveDAO libraryReserveDAO = new libraryReserveDAO();

	
	//실시간 예약가능한 미팅룸 리스트 조회하는 메소드
	public List MeetingRoomList(String date, int start, int end) {		
		//DB에서 예약 가능한 미팅룸 조회 명령
		List roomList = libraryReserveDAO.MeetingRoomList(date, start, end);
		return roomList; //조회된 List를 Controller로 반환		
	}
	

	//미팅룸 예약을 진행하는 메소드
	public int reserveMeetingRoom(libraryReserveVO vo) {		
		//DB에 예약정보 저장 명령
		return libraryReserveDAO.reserveMeetingRoom(vo);
	}

	
	//예약 정보를 조회하는 메소드
	public List<libraryReserveVO> selectReserveList(String userId) {
		//DB에서 예약정보 조회 명령
		List<libraryReserveVO> reserveList = libraryReserveDAO.selectReserveList(userId);				
		return reserveList;
	}
	
	//예약을 삭제하는 메소드
	public void deleteReserve(String reserveId, String reserveNum) {
		
		//DB에 예약정보 삭제 명령
		libraryReserveDAO.deleteReserve(reserveId, reserveNum);
		
	}

	
	//스터디룸 실시간 좌석 현황을 조회하는 메소드
	public List studySeatList(String date, int start, int end, String studyRoom) {
		
		//DB에서 실시간 좌석 현황 조회 명령
		List seatList = libraryReserveDAO.studySeatList(date, start, end, studyRoom);
		
		return seatList;		
	}

	
	//스터디룸 예약하는 메소드
	public int reserveStudyRoom(libraryReserveVO vo) {	
		//DB에 예약정보 저장 명령
		return libraryReserveDAO.reserveStudyRoom(vo);	
		
	}
	
	
	//스터디룸 예약 수정하는 메소드
	public void updateStudyRoom(libraryReserveVO vo) {
		//DB에 예약정보 수정 명령
		libraryReserveDAO.updateStudyRoom(vo);			
	}

	
	//미팅룸 예약 수정하는 메소드
	public void updateMeetingRoom(libraryReserveVO vo) {
		//DB에 예약정보 수정 명령
		libraryReserveDAO.updateMeetingRoom(vo);				
	}

	
	//전체 시설 예약 내역을 조회하는 메소드
	public List allReservedList() {
		return libraryReserveDAO.allReservedList();
	}

	
	//중복 예약 방지를 위해 사용자가 선택한 날짜와 시간대에 예약 건이 있는지 확인하는 메소드
	public boolean checkReserve(Map<String, Object> reserveMap) {
		return libraryReserveDAO.checkReserve(reserveMap);
	}


	


	
	

}
