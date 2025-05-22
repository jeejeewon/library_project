package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
			pstmt.setString(2, vo.getPass()); // 카카오 사용자의 경우, Service 단에서 비밀번호 필드(NOT NULL 제약조건) 처리가 필요함 (예: 임의의 값 설정)
			pstmt.setString(3, vo.getName());
			pstmt.setString(4, vo.getGender());
			pstmt.setString(5, vo.getAddress());
			pstmt.setString(6, vo.getEmail());
			pstmt.setString(7, vo.getTel());
			pstmt.setString(8, vo.getKakaoId()); // kakaoId 값 설정 추가 (null일 수도 있음)

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
				if (id != null && id.equals("admin")) {
					result = "관리자 계정 삭제 성공";
				} else {
					result = "회원 탈퇴 성공";
				}
			} else {
				result = "탈퇴실패";
			}

		} catch (Exception e) {
			System.out.println("MemberDao.memDelete() 메소드 오류 : " + e);
			 result = "회원 삭제 처리 중 시스템 오류 발생";
			e.printStackTrace();
		} finally {
			ResourceClose();
		}

		return result;

	}

	// 아이디와 이메일 조회
	public MemberVo selectMember(String id, String email) {

		MemberVo memberVo = null;

		try {
			con = this.getConnection();

			String sql = "select id, email from member where id=? and email=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, email);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				memberVo = new MemberVo();
				memberVo.setId(id);
				memberVo.setEmail(email);
			}

		} catch (Exception e) {
			System.out.println("MemberDao / selectMember 메소드 오류 : " + e);
		} finally {
			ResourceClose();
		}
		return memberVo;
	}

	// 이메일로 아이디 조회
	public MemberVo selectEmail(String email) {

		MemberVo memberVo = null;

		try {
			con = this.getConnection();

			String sql = "select id, email from member where email=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				memberVo = new MemberVo();
				memberVo.setId(rs.getString("id"));
				memberVo.setEmail(email);
			}

		} catch (Exception e) {
			System.out.println("MemberDao / selectMember 메소드 오류 : " + e);
		} finally {
			ResourceClose();
		}
		return memberVo;
	}

	// 카카오 ID를 사용하여 회원 정보를 조회
	public MemberVo findMemberByKakaoId(String kakaoId) {
		MemberVo memberVo = null;
		String sql = "SELECT * FROM member WHERE kakao_id = ?";

		try {
			con = this.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, kakaoId);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				memberVo = new MemberVo();
				memberVo.setId(rs.getString("id"));
				memberVo.setPass(rs.getString("pass"));
				memberVo.setName(rs.getString("name"));
				memberVo.setGender(rs.getString("gender"));
				memberVo.setAddress(rs.getString("address"));
				memberVo.setEmail(rs.getString("email"));
				memberVo.setTel(rs.getString("tel"));
				memberVo.setKakaoId(rs.getString("kakao_id"));
			}
		} catch (Exception e) {
			System.out.println("MemberDAO.findMemberByKakaoId() 메소드 오류: " + e);
			e.printStackTrace();
		} finally {
			ResourceClose();
		}
		return memberVo;
	}

	// 비밀번호 수정
	public int updatePw(String id, String newPw) {

		int updateResult = 0;

		try {
			con = this.getConnection();
			String sql = "update member " + "set pass=? " + "where id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, newPw);
			pstmt.setString(2, id);

			updateResult = pstmt.executeUpdate();

		} catch (Exception e) {
			System.out.println("MemberDAO.updatePw() 메소드 오류 : " + e);
		} finally {
			ResourceClose();
		}

		return updateResult;
	}

	// ---- 관리자 페이지
	public List<MemberVo> selectMemberList(Map<String, String> searchCriteria) {
		List<MemberVo> memberList = new ArrayList<>(); // 결과를 담을 리스트
		Connection con = null; // 메소드 안에서 Connection 관리 예시
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = this.getConnection(); // DB 연결 가져오기

			StringBuilder sql = new StringBuilder(
					"SELECT id, pass, name, gender, address, email, tel, joindate, kakao_id FROM member");
			List<Object> params = new ArrayList<>(); // PreparedStatement에 들어갈 파라미터 목록

			boolean whereAdded = false; // WHERE 절이 추가되었는지 확인하는 플래그

			// 아이디 검색 조건
			if (searchCriteria.containsKey("searchId") && !searchCriteria.get("searchId").isEmpty()) {
				sql.append(" WHERE id LIKE ?"); // LIKE 검색
				params.add("%" + searchCriteria.get("searchId") + "%");
				whereAdded = true;
			}

			// 이름 검색 조건
			if (searchCriteria.containsKey("searchName") && !searchCriteria.get("searchName").isEmpty()) {
				if (!whereAdded) {
					sql.append(" WHERE");
					whereAdded = true;
				} else {
					sql.append(" AND"); // 이미 WHERE가 있으면 AND로 연결
				}
				sql.append(" name LIKE ?"); // LIKE 검색
				params.add("%" + searchCriteria.get("searchName") + "%");
			}

			// 이메일 검색 조건
			if (searchCriteria.containsKey("searchEmail") && !searchCriteria.get("searchEmail").isEmpty()) {
				if (!whereAdded) {
					sql.append(" WHERE");
					whereAdded = true;
				} else {
					sql.append(" AND");
				}
				sql.append(" email LIKE ?"); // LIKE 검색
				params.add("%" + searchCriteria.get("searchEmail") + "%");
			}

			// 최신 가입일 순으로 정렬 (필요하다면)
			sql.append(" ORDER BY joindate DESC"); // 최신순 정렬 예시

			System.out.println("DAO Search SQL: " + sql.toString()); // 디버깅용 SQL 출력

			pstmt = con.prepareStatement(sql.toString());

			// PreparedStatement에 파라미터 바인딩
			for (int i = 0; i < params.size(); i++) {
				pstmt.setObject(i + 1, params.get(i));
			}

			rs = pstmt.executeQuery(); // 쿼리 실행

			// 결과 가져오기
			while (rs.next()) {
				MemberVo member = new MemberVo();
				member.setId(rs.getString("id"));
				member.setPass(rs.getString("pass"));
				member.setName(rs.getString("name"));
				member.setGender(rs.getString("gender"));
				member.setAddress(rs.getString("address"));
				member.setEmail(rs.getString("email"));
				member.setTel(rs.getString("tel"));
				member.setJoinDate(rs.getDate("joindate")); // DATE 타입 가져오기
				member.setKakaoId(rs.getString("kakao_id"));
				memberList.add(member); // 리스트에 추가!
			}

		} catch (Exception e) {
			System.out.println("MemberDao / selectMemberList 메소드 오류: " + e.getMessage());
			e.printStackTrace(); // 자세한 오류 추적
		} finally {
			// ResourceClose(); // 예시 메소드 사용
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
			}
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}

		return memberList; // 검색 결과 리스트 반환!
	}

	// 회원 한 명 정보 가져오는 메소드 (아이디로 조회)
	public MemberVo selectMember(String memberId) {
		MemberVo memberVo = null; // 결과를 담을 객체
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = this.getConnection(); // DB 연결

			String sql = "SELECT id, pass, name, gender, address, email, tel, joindate, kakao_id FROM member WHERE id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, memberId); // 아이디 조건 설정

			rs = pstmt.executeQuery(); // 쿼리 실행

			// 결과 가져오기
			if (rs.next()) {
				memberVo = new MemberVo(); // 찾았으면 객체 생성
				memberVo.setId(rs.getString("id"));
				memberVo.setPass(rs.getString("pass"));
				memberVo.setName(rs.getString("name"));
				memberVo.setGender(rs.getString("gender"));
				memberVo.setAddress(rs.getString("address"));
				memberVo.setEmail(rs.getString("email"));
				memberVo.setTel(rs.getString("tel"));
				memberVo.setJoinDate(rs.getDate("joindate"));
				memberVo.setKakaoId(rs.getString("kakao_id"));
			}

		} catch (Exception e) {
			System.out.println("MemberDao / selectMember 메소드 오류: " + e.getMessage());
			e.printStackTrace();
		} finally {
			ResourceClose();
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
			}
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}

		return memberVo; // 찾은 회원 정보 객체 또는 null 반환
	}

	// 회원 정보 수정 메소드
	public int updateMember(MemberVo member) {
		int updateCount = 0; // 업데이트된 행의 개수 (성공하면 1)
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = this.getConnection(); // DB 연결

			// UPDATE SQL 쿼리 (id는 조건으로 사용하고, 다른 필드 업데이트)
			// 비밀번호도 수정 가능하게 하려면 pass=? 추가
			String sql = "UPDATE member SET pass=?, name=?, gender=?, address=?, email=?, tel=? WHERE id=?";
			pstmt = con.prepareStatement(sql);

			// PreparedStatement에 수정할 값들 설정
			pstmt.setString(1, member.getPass()); // 비밀번호 업데이트 예시
			pstmt.setString(2, member.getName());
			pstmt.setString(3, member.getGender());
			pstmt.setString(4, member.getAddress());
			pstmt.setString(5, member.getEmail());
			pstmt.setString(6, member.getTel());
			pstmt.setString(7, member.getId()); // WHERE 조건에 사용될 아이디

			updateCount = pstmt.executeUpdate(); // 쿼리 실행 및 결과 (수정된 행 수) 가져오기

		} catch (Exception e) {
			System.out.println("MemberDao / updateMember 메소드 오류: " + e.getMessage());
			e.printStackTrace();
		} finally {
			// ResourceClose();
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
			}
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}

		return updateCount; // 업데이트 성공/실패 결과 반환 (1이면 성공, 0이면 실패)
	}

	// 최근 7일 이내 가입한 회원 정보 조회 (관리자용)
	public List<MemberVo> selectRecentMembers() {

		List<MemberVo> recentMemberList = new ArrayList<>();
		String sql = "SELECT * FROM member WHERE joinDate >= CURRENT_DATE - INTERVAL '7' DAY ORDER BY joinDate DESC";
		try {
			con = this.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery(); // 쿼리 결과 얻기

			while (rs.next()) {
				MemberVo memberVo = new MemberVo();
				memberVo.setId(rs.getString("id"));
				memberVo.setPass("********");
				memberVo.setName(rs.getString("name"));
				memberVo.setGender(rs.getString("gender"));
				memberVo.setAddress(rs.getString("address"));
				memberVo.setEmail(rs.getString("email"));
				memberVo.setTel(rs.getString("tel"));
				memberVo.setJoinDate(rs.getDate("joinDate"));
				memberVo.setKakaoId(rs.getString("kakao_id"));
				recentMemberList.add(memberVo);
			}

		} catch (Exception e) {
			System.out.println("selectRecentMembers 메소드 내부에서 오류!");
			e.printStackTrace();
		} finally {
			ResourceClose();
		}
		System.out.println(">>> [DAO] selectRecentMembers 메소드 종료. 반환 리스트 크기: " + recentMemberList.size());
		return recentMemberList;
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
