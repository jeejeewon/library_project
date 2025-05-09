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

			con = this.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery(); // 쿼리 결과 얻기

			while (rs.next()) {
				MemberVo memberVo = new MemberVo();
				memberVo.setId(rs.getString("id"));
				memberVo.setPass("********"); // 마스킹처리
				memberVo.setName(rs.getString("name"));
				memberVo.setGender(rs.getString("gender"));
				memberVo.setAddress(rs.getString("adress"));
				memberVo.setEmail(rs.getString("email"));
				memberVo.setTel(rs.getString("tel"));
				memberVo.setJoinDate(rs.getDate("joinDate"));
				memberVo.setKakaoId(rs.getString("kakao_id")); // 카카오 ID 설정
				memberList.add(memberVo);
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

				result = true;
			}

		} catch (Exception e) {
			System.out.println("MemberDAO.overlappedId() 메소드 오류: " + e);
			e.printStackTrace();
		} finally {
			ResourceClose();
		}

		return result;
	}

	// 회원 추가 (회원가입)
	public void insertMember(MemberVo vo) {
		try {

			con = this.getConnection();
			String sql = "insert into member(id, pass, name, gender, address, email, tel, joinDate, kakao_Id)"
					+ "values(?,?,?,?,?,?,?,sysdate(),?)";

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, vo.getId());
			pstmt.setString(2, vo.getPass());
			pstmt.setString(3, vo.getName());
			pstmt.setString(4, vo.getGender());
			pstmt.setString(5, vo.getAddress());
			pstmt.setString(6, vo.getEmail());
			pstmt.setString(7, vo.getTel());
			pstmt.setString(8, vo.getKakaoId());

			// insert
			pstmt.executeUpdate();

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
			con = this.getConnection();
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

	// 회원정보 수정에 불러올 vo
	public MemberVo memberInfo(String id) {
		MemberVo memberVo = null;
		String sql = null;
		try {
			con = this.getConnection();

			sql = "select * from member where id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				memberVo = new MemberVo();
				memberVo.setId(rs.getString("id"));
				memberVo.setPass("********");
				memberVo.setName(rs.getString("name"));
				memberVo.setGender(rs.getString("gender"));
				memberVo.setAddress(rs.getString("address"));
				memberVo.setEmail(rs.getString("email"));
				memberVo.setTel(rs.getString("tel"));
				memberVo.setJoinDate(rs.getDate("joinDate"));
				memberVo.setKakaoId(rs.getString("kakao_id"));
			}
		} catch (Exception e) {
			System.out.println("MemberDao.memberInfo() 메소드 오류 : " + e);
		} finally {
			ResourceClose();
		}
		return memberVo;
	}

	// 회원정보 수정 요청
	public int memUpdate(MemberVo memberVo) {
		String sql = null;
		int result = 0;
		try {
			con = this.getConnection();
			sql = "update member set pass=?, gender=?, address=?, email=?, tel=? where id=?";

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, memberVo.getPass());
			pstmt.setString(2, memberVo.getGender());
			pstmt.setString(3, memberVo.getAddress());
			pstmt.setString(4, memberVo.getEmail());
			pstmt.setString(5, memberVo.getTel());
			pstmt.setString(6, memberVo.getId());

			System.out.println(memberVo.getPass());
			System.out.println(memberVo.getGender());
			System.out.println(memberVo.getAddress());
			System.out.println(memberVo.getEmail());
			System.out.println(memberVo.getTel());
			System.out.println(memberVo.getId());

			result = pstmt.executeUpdate(); // 성공할 경우 1

		} catch (Exception e) {
			System.out.println("MemberDao.memUpdate() 메소드 오류 : " + e);
		} finally {
			ResourceClose();
		}
		return result;
	}

	// 회원 탈퇴(삭제)
	public String memDelete(String id) {

		String sql = null;
		String result = null;

		try {
			con = this.getConnection();

			sql = "delete from member where id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);

			int val = pstmt.executeUpdate();

			if (val == 1) {
				result = "탈퇴성공";
			} else {
				result = "탈퇴실패";
			}

		} catch (Exception e) {
			System.out.println("MemberDao.memDelete() 메소드 오류 : " + e);
			e.printStackTrace();
		} finally {
			ResourceClose();
		}

		return result;

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
