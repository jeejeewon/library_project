package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import Vo.MemberVo;

public class MemberDao {
	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	DataSource ds;

	private Connection getConnection() throws Exception {
		Connection con = null;
		Context init = new InitialContext();
		DataSource ds = (DataSource) init.lookup("java:comp/env/jdbc/jspbeginner");
		con = ds.getConnection();
		return con;
	}

	// 모든 회원 정보 조회 (관리자용)
	public List<MemberVo> selectAllMembers() {
		List<MemberVo> memberList = new ArrayList<>();
		String sql = "slect * from member order by joinDate desc";
		try {

			con = getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery(); // 쿼리 결과 얻기

			while (rs.next()) {
				MemberVo vo = new MemberVo();
				vo.setId(rs.getString("id"));
				vo.setPass("********"); // 마스킹처리
				vo.setName(rs.getString("name"));
				vo.setGender(rs.getString("gender"));
				vo.setAddress(rs.getString("adress"));
				vo.setEmail(rs.getString("email"));
				vo.setTel(rs.getString("tel"));
				vo.setKakaoId(rs.getString("kakao_id")); // 카카오 ID 설정
				memberList.add(vo);
			}

		} catch (Exception e) {
			System.out.println("selectAllMembers 메소드 내부에서 오류!");
			e.printStackTrace();
		} finally {
			ResourceClose();
		}

		return memberList;
	}

	// 회원가입 시 아이디 중복 확인
	public Boolean overlappedId(String id) {

		boolean result = false;
		String sql = "";

		try {

			con = this.getConnection();
			sql = "select * from member where id = ?";

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				String value = rs.getString("result");
				result = Boolean.parseBoolean(value);
			}

		} catch (Exception e) {
			System.out.println("MemberDAO.overlappedId() 메소드 오류: "+e);
            e.printStackTrace();
		} finally {
			ResourceClose();
		}

		return result;
	}

	// 회원 추가 (회원가입)
	public void insertMember(MemberVo vo) {
		try {

			con = getConnection();
			String sql = "insert int member(id, pass, name, age, gender, address, email, tel, joinDate, kakaoId)"
					+ "values(?,?,?,?,?,?,?,?,sysdate,?)";

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, vo.getId());
			pstmt.setString(2, vo.getPass());

		} catch (Exception e) {
			System.out.println("insertMember 메소드 내부에서 오류!");
			e.printStackTrace();
		} finally {
			ResourceClose();
		}

	}

	// 로그인 시 아이디, 비밀번호 일치 여부
	public int userCheck(String login_id, String login_pass) {
		int check = -1;

		try {
			con = getConnection();
			String sql = "select pass from member where id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, login_id);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				if (login_pass.equals(rs.getString("pass"))) {
					check = 1;
				} else {
					check = 0;
				}
			} else {
				check = -1;
			}

		} catch (Exception e) {

			System.out.println("MemberDAO.userCheck() 메소드 오류: " + e);
			e.printStackTrace();
		} finally {
			ResourceClose();
		}
		return check;
	}

	// 자원 해제
	public void ResourceClose() {
		try {
			if (pstmt != null) {
				pstmt.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
