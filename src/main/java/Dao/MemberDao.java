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

	// 모든 회원 정보 조회 (관리자용)
	public List<MemberVo> selectAllMembers() {
		List<MemberVo> memberList = new ArrayList<>();
		String sql = "slect * from member order by joinDate desc";
		try {

			con = DbcpBean.getConnection();
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
			DbcpBean.close(con, pstmt, rs);
		}

		return memberList;
	}

	// 회원가입 시 아이디 중복 확인
	public boolean idCheck(String id) {
		boolean result = false;
		try {

			con = DbcpBean.getConnection();
			String sql = "select id from member where id = ?";

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id); // 첫 번째 '?' 자리에 id 값 바인딩

			rs = pstmt.executeQuery();

			if (rs.next()) { // 결과 행이 존재하면
				String value = rs.getString("result");
				// 문자열을 boolean 타입으로 변환하여 result 변수에 저장
				result = Boolean.parseBoolean(value);
			}

		} catch (Exception e) {
			System.out.println("idCheck 메소드 내부에서 오류!");
			e.printStackTrace();
		} finally {
			DbcpBean.close(con, pstmt, rs);
		}
		return result;
	}

	// 회원 추가 (회원가입)
	public void insertMember(MemberVo vo) {
		try {

			con = DbcpBean.getConnection();
			String sql = "insert int member(id, pass, name, age, gender, address, email, tel, joinDate, kakaoId)" 
			+ "values(?,?,?,?,?,?,?,?,sysdate,?)";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, vo.getId());
			pstmt.setString(2, vo.getPass());
			
			

		} catch (Exception e) {
			System.out.println("insertMember 메소드 내부에서 오류!");
			e.printStackTrace();
		} finally {
			DbcpBean.close(con, pstmt, rs);
		}

	}

	// 로그인 시 아이디, 비밀번호 일치 여부
	public int userCheck(String login_id, String pass) {
		int check = -1;
		return check;
	}

}
