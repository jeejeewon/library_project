package Vo;

import java.sql.Date;

public class MemberVo {

	private String id; // 회원 아이디
	private String pass; // 비밀번호
	private String name; // 이름
	private String gender; // 성별
	private String address; // 주소
	private String email; // 이메일
	private String tel; // 전화번호
	private Date joinDate; // 등록일
	private String kakaoId; // 카카오 고유 ID 추가

	// 기본 생성자
	public MemberVo() {
	}

	// 모든 정보 조회(관리자용)
	public MemberVo(String id, String pass, String name, String gender, String address, String email, String tel,
			Date joinDate, String kakaoId) {
		super();
		this.id = id;
		this.pass = pass;
		this.name = name;
		this.gender = gender;
		this.address = address;
		this.email = email;
		this.tel = tel;
		this.joinDate = joinDate;
		this.kakaoId = kakaoId;
	}

	// 회원가입
	public MemberVo(String id, String pass, String name, String gender, String address, String email, String tel) {
		super();
		this.id = id;
		this.pass = pass;
		this.name = name;
		this.gender = gender;
		this.address = address;
		this.email = email;
		this.tel = tel;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	public String getKakaoId() {
		return kakaoId;
	}

	public void setKakaoId(String kakaoId) {
		this.kakaoId = kakaoId;
	}

}
