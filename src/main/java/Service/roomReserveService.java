package Service;

import java.util.List;
import Dao.libraryReserveDAO;


//시설 예약 관련 비즈니스 로직 처리 Service
public class roomReserveService {

	//DB 작업할 DAO 객체 생성
	libraryReserveDAO libraryReserveDAO = new libraryReserveDAO();

	public List MeetingRoomList(String date, String start, String end) {
		
		//조회한 미팅룸을 저장할 변수
		List meetingRoomList = null;
		
		//DB에서 예약 가능한 미팅룸 조회 명령
		meetingRoomList = libraryReserveDAO.selectRoomList(date, start, end);


		
		
		return null;
	}
	
	

}
