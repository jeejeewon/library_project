package Service;

import java.util.List;
import Dao.libraryReserveDAO;
import Vo.libraryReserveVO;


//시설 예약 관련 비즈니스 로직 처리 Service
public class roomReserveService {

	//DB 작업할 DAO 객체 생성
	libraryReserveDAO libraryReserveDAO = new libraryReserveDAO();

	public List MeetingRoomList(String date, int start, int end) {
		
		//DB에서 예약 가능한 미팅룸 조회 명령
		List roomList = libraryReserveDAO.selectRoomList(date, start, end);

		return roomList; //조회된 List를 Controller로 반환
		
	}

	public void insertReserveRoom(libraryReserveVO vo) {
		
		//DB에 예약정보 저장 명령
		libraryReserveDAO.insertReserveRoom(vo);
	}
	
	

}
