package Vo;

import java.sql.Date;

public class MemberVo {

	private String id;
	private String pass;
	private String name;
	private int age;
	private String gender;
	private String email;
	private String tel;
	private String address;
	private Date joindate;
	private String kakaoId;
	
	// 기본 생성자
	public MemberVo() {}

	// 전체 회원 정보 조회(관리자용)
	public MemberVo(String id, String pass, String name, int age, String gender, String email, String tel,
			String address, String kakaoId) {
		super();
		this.id = id;
		this.pass = pass;
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.email = email;
		this.tel = tel;
		this.address = address;
		this.kakaoId = kakaoId;
	}

	// 회원가입용
	public MemberVo(String id, String pass, String name, int age, String gender, String email, String tel,
			String address) {
		super();
		this.id = id;
		this.pass = pass;
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.email = email;
		this.tel = tel;
		this.address = address;
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

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getJoindate() {
		return joindate;
	}

	public void setJoindate(Date joindate) {
		this.joindate = joindate;
	}

	public String getKakaoId() {
		return kakaoId;
	}

	public void setKakaoId(String kakaoId) {
		this.kakaoId = kakaoId;
	}
	
	
}
