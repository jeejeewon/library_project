package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import Vo.MemberVo;

public class MemberDao {
	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	DataSource ds;

	// 자원해제
	private void closeResource() {
		if (con != null)
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		if (pstmt != null)
			try {
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		if (rs != null)
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	// member 테이블의 모든 회원 정보 조회 (관리자용)
	public List<MemberVo> selectAllMembers() {
		List<MemberVo> memberList = new ArrayList<>();

		return memberList;
	}	

	// 아이디 중복 확인
	public boolean idCheck(String id) {
		boolean result = false;
		try {

		} catch (Exception e) {

		} finally {
			closeResource();
		}
		return result;
	}
	// 회원가입


	// 회원 추가
	public void insertMember(MemberVo vo) {

	}

	// 로그인 시 아이디, 비밀번호 일치 여부
	public int userCheck(String login_id, String pass) {
		int check = -1;
		return check;
	}

}
