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
	   String sql = "SELECT * FROM member ORDER BY joinDate DESC";
	   try {
		   
		   con = DbcpBean.getConnection();           
           pstmt = con.prepareStatement(sql);          
           rs = pstmt.executeQuery(); // 쿼리 결과 얻기
           
           while(rs.next()) {
        	   MemberVo vo = new MemberVo();
        	   
        	   memberList.add(vo);
           }        
           		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbcpBean.close(con, pstmt, rs);
		}
	   
		return memberList;
	}	

	// 아이디 중복 확인
	public boolean idCheck(String id) {
		boolean result = false;
		try {

		} catch (Exception e) {

		} finally {
			DbcpBean.close(con, pstmt, rs);
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
