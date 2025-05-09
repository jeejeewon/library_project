package Vo;

import java.sql.Date;
import java.sql.Timestamp;

//시설 예약 정보를 저장할 VO
public class libraryReserveVO {

	private String reserve_num;		//예약번호
	private String reserve_room;	//예약시설명
	private int reserve_seat;		//예약한 스터디룸 좌석번호
	private String reserve_id;		//예약자 ID
	private String reserve_name;	//예약자명
	private Date reserve_date;		//예약일자
	private int reserve_start;		//예약시작시간
	private int reserve_end;		//예약종료시간
	private Timestamp reserve_time;		//예약시간
	private boolean isFuture;	    //예약날짜가 미래인지 여부 (true:미래, false:현재)
	
	
	//기본생성자
	public libraryReserveVO() {}

	//미팅룸 예약시 컨트롤러에서 DB에 값 넘길 때 사용할 생성자 (예약번호, 예약자명 제외 변수 초기화)
	public libraryReserveVO(String reserve_room, String reserve_id, Date reserve_date, int reserve_start,
			int reserve_end) {
		this.reserve_room = reserve_room;
		this.reserve_id = reserve_id;
		this.reserve_date = reserve_date;
		this.reserve_start = reserve_start;
		this.reserve_end = reserve_end;
	}


	//미팅룸 예약시 사용할 생성자 (좌석번호 제외 변수 초기화)
	public libraryReserveVO(String reserve_num, String reserve_room, String reserve_id, String reserve_name,
			Date reserve_date, int reserve_start, int reserve_end) {
		this.reserve_num = reserve_num;
		this.reserve_room = reserve_room;
		this.reserve_id = reserve_id;
		this.reserve_name = reserve_name;
		this.reserve_date = reserve_date;
		this.reserve_start = reserve_start;
		this.reserve_end = reserve_end;
	}
	
	//미팅룸 조회시 사용할 생성자
	public libraryReserveVO(String reserve_room, Date reserve_date, int reserve_start, int reserve_end,
			Timestamp reserve_time) {
		this.reserve_room = reserve_room;
		this.reserve_date = reserve_date;
		this.reserve_start = reserve_start;
		this.reserve_end = reserve_end;
		this.reserve_time = reserve_time;
	}
	
	
	

	//getter, setter 메소드
	public String getReserve_num() {
		return reserve_num;
	}

	public void setReserve_num(String reserve_num) {
		this.reserve_num = reserve_num;
	}

	public String getReserve_room() {
		return reserve_room;
	}

	public void setReserve_room(String reserve_room) {
		this.reserve_room = reserve_room;
	}

	public int getReserve_seat() {
		return reserve_seat;
	}

	public void setReserve_seat(int reserve_seat) {
		this.reserve_seat = reserve_seat;
	}

	public String getReserve_id() {
		return reserve_id;
	}

	public void setReserve_id(String reserve_id) {
		this.reserve_id = reserve_id;
	}

	public String getReserve_name() {
		return reserve_name;
	}

	public void setReserve_name(String reserve_name) {
		this.reserve_name = reserve_name;
	}

	public Date getReserve_date() {
		return reserve_date;
	}

	public void setReserve_date(Date reserve_date) {
		this.reserve_date = reserve_date;
	}

	public int getReserve_start() {
		return reserve_start;
	}

	public void setReserve_start(int reserve_start) {
		this.reserve_start = reserve_start;
	}

	public int getReserve_end() {
		return reserve_end;
	}

	public void setReserve_end(int reserve_end) {
		this.reserve_end = reserve_end;
	}

	public Timestamp getReserve_time() {
		return reserve_time;
	}

	public void setReserve_time(Timestamp reserve_time) {
		this.reserve_time = reserve_time;
	}

	public boolean getIsFuture() {
		return isFuture;
	}

	public void setIsFuture(boolean isFuture) {
		this.isFuture = isFuture;
	}
	

		
}
