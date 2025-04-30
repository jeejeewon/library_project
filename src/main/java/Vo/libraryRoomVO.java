package Vo;

//시설 정보 저장할 VO
public class libraryRoomVO {

	private String room_code;	//시설코드	
	private String room_name;	//시설명
	private int seat_num;		//스터디룸 좌석번호
	private int reserve_state;	//예약상태 (0: 예약가능, 1: 예약불가)
	
	
	//기본생성자
	public libraryRoomVO() {}
	
	
	//시설정보 저장할 생성자 (미팅룸)
	public libraryRoomVO(String room_code, String room_name, int reserve_state) {
		this.room_code = room_code;
		this.room_name = room_name;
		this.reserve_state = reserve_state;
	}
	

	//시설정보 저장할 생성자 (스터디룸)
	public libraryRoomVO(String room_code, String room_name, int seat_num, int reserve_state) {
		this.room_code = room_code;
		this.room_name = room_name;
		this.seat_num = seat_num;
		this.reserve_state = reserve_state;
	}

	
	//getter, setter 메소드
	public String getRoom_code() {
		return room_code;
	}


	public void setRoom_code(String room_code) {
		this.room_code = room_code;
	}


	public String getRoom_name() {
		return room_name;
	}


	public void setRoom_name(String room_name) {
		this.room_name = room_name;
	}


	public int getSeat_num() {
		return seat_num;
	}


	public void setSeat_num(int seat_num) {
		this.seat_num = seat_num;
	}


	public int getReserve_state() {
		return reserve_state;
	}


	public void setReserve_state(int reserve_state) {
		this.reserve_state = reserve_state;
	}
	
}
