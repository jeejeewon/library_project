package Dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/*
 JSP/Servlet 프로젝트에서 공통으로 사용하는 DB 연결 & 해제 유틸 클래스
 (DataSource(Connection Pool) 활용)
*/
public class DbcpBean {

    // DataSource 객체 (커넥션 풀)
    private static DataSource ds;

    // static 초기화 블록 → 클래스가 로딩될 때 1번만 실행됨
    static {
        try {
            // 1. JNDI 초기화
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:comp/env");

            // 2. context.xml 에 등록된 DataSource 이름 (jdbc/jspbeginner 사용!)
            ds = (DataSource) envContext.lookup("jdbc/jspbeginner");
            System.out.println("DataSource lookup 성공!");

        } catch (NamingException e) {
            System.err.println("DataSource lookup 실패: " + e.getMessage());
            e.printStackTrace();
            // 치명적인 에러 → 앱 실행 중지
            throw new RuntimeException("DataSource lookup 실패", e);
        }
    }

    /* 커넥션 풀에서 Connection 하나 빌려오기 */
    public static Connection getConnection() throws SQLException {
        if (ds == null) {
            throw new SQLException("DataSource가 초기화되지 않았습니다.");
        }
        return ds.getConnection();
    }

    /* Connection 반납 */
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close(); // 커넥션 풀에 반납
            } catch (SQLException e) {
                System.err.println("Connection 닫기 실패: " + e.getMessage());
            }
        }
    }

    /* Statement 닫기 (PreparedStatement 포함) */
    public static void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("Statement 닫기 실패: " + e.getMessage());
            }
        }
    }

    /* ResultSet 닫기 */
    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("ResultSet 닫기 실패: " + e.getMessage());
            }
        }
    }

    /* Connection, Statement, ResultSet 한 번에 닫기 */
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        close(rs);
        close(stmt);
        close(conn);
    }

    /* Connection, Statement 한 번에 닫기 */
    public static void close(Connection conn, Statement stmt) {
        close(stmt);
        close(conn);
    }

}
