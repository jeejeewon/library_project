package Vo;

import java.sql.Date;
import java.sql.Timestamp;

//시설 예약 정보를 저장할 VO
public class libraryReserveVO {

	private String reserveNum;		//예약번호
	private String reserveRoom;	//예약시설코드
	private String roomName;		//예약시설명 
	private int reserveSeat;		//예약한 스터디룸 좌석번호
	private String reserveId;		//예약자 ID
	private String reserveName;	//예약자명
	private Date reserveDate;		//예약일자
	private int reserveStart;		//예약시작시간
	private int reserveEnd;		//예약종료시간
	private Timestamp reserveTime;	//예약시간
	private boolean isFuture;	    //예약날짜가 미래인지 여부 (true:미래, false:현재)
	private boolean isNow; //예약된 시간에 현재 시간이 겹친다면? (현재 이용중이라면?)
	
	
	//기본생성자
	public libraryReserveVO() {}

	//미팅룸 예약시 컨트롤러에서 DB에 값 넘길 때 사용할 생성자 (예약번호, 예약자명 제외 변수 초기화)
	public libraryReserveVO(String reserveRoom, String reserveId, Date reserveDate, int reserveStart, int reserveEnd) {
		this.reserveRoom = reserveRoom;
		this.reserveId = reserveId;
		this.reserveDate = reserveDate;
		this.reserveStart = reserveStart;
		this.reserveEnd = reserveEnd;
	}

	//미팅룸 예약시 사용할 생성자 (좌석번호 제외 변수 초기화)
	public libraryReserveVO(String reserveNum, String reserveRoom, String reserveId, String reserveName,
			Date reserveDate, int reserveStart, int reserveEnd) {
		this.reserveNum = reserveNum;
		this.reserveRoom = reserveRoom;
		this.reserveId = reserveId;
		this.reserveName = reserveName;
		this.reserveDate = reserveDate;
		this.reserveStart = reserveStart;
		this.reserveEnd = reserveEnd;
	}

	//미팅룸 조회시 사용할 생성자
	public libraryReserveVO(String reserveRoom, Date reserveDate, int reserveStart, int reserveEnd,
			Timestamp reserveTime) {
		this.reserveRoom = reserveRoom;
		this.reserveDate = reserveDate;
		this.reserveStart = reserveStart;
		this.reserveEnd = reserveEnd;
		this.reserveTime = reserveTime;
	}

	//시설 예약 조회시 사용할 생성자
	public libraryReserveVO(String reserveNum, String reserveRoom, String reserveId, String reserveName,
			Date reserveDate, int reserveStart, int reserveEnd, Timestamp reserveTime, int reserveSeat) {
		super();
		this.reserveNum = reserveNum;
		this.reserveRoom = reserveRoom;
		this.reserveId = reserveId;
		this.reserveName = reserveName;
		this.reserveDate = reserveDate;
		this.reserveStart = reserveStart;
		this.reserveEnd = reserveEnd;
		this.reserveTime = reserveTime;
		this.reserveSeat = reserveSeat;
	}

	
	//getter, setter 메소드
	public String getReserveNum() {
		return reserveNum;
	}

	public void setReserveNum(String reserveNum) {
		this.reserveNum = reserveNum;
	}

	public String getReserveRoom() {
		return reserveRoom;
	}

	public void setReserveRoom(String reserveRoom) {
		this.reserveRoom = reserveRoom;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public int getReserveSeat() {
		return reserveSeat;
	}

	public void setReserveSeat(int reserveSeat) {
		this.reserveSeat = reserveSeat;
	}

	public String getReserveId() {
		return reserveId;
	}

	public void setReserveId(String reserveId) {
		this.reserveId = reserveId;
	}

	public String getReserveName() {
		return reserveName;
	}

	public void setReserveName(String reserveName) {
		this.reserveName = reserveName;
	}

	public Date getReserveDate() {
		return reserveDate;
	}

	public void setReserveDate(Date reserveDate) {
		this.reserveDate = reserveDate;
	}

	public int getReserveStart() {
		return reserveStart;
	}

	public void setReserveStart(int reserveStart) {
		this.reserveStart = reserveStart;
	}

	public int getReserveEnd() {
		return reserveEnd;
	}

	public void setReserveEnd(int reserveEnd) {
		this.reserveEnd = reserveEnd;
	}

	public Timestamp getReserveTime() {
		return reserveTime;
	}

	public void setReserveTime(Timestamp reserveTime) {
		this.reserveTime = reserveTime;
	}

	public boolean getIsFuture() {
		return isFuture;
	}

	public void setIsFuture(boolean isFuture) {
		this.isFuture = isFuture;
	}

	public boolean getIsNow() {
		return isNow;
	}

	public void setIsNow(boolean isNow) {
		this.isNow = isNow;
	}

	public void setFuture(boolean isFuture) {
		this.isFuture = isFuture;
	}
	
	
	
}
