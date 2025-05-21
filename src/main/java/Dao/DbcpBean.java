package Dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * JNDI를 통해 DataSource(Connection Pool)를 얻어오고,
 * 필요할 때 Connection 객체를 제공하고,
 * 사용 후 자원을 반납하는 유틸리티 클래스.
 */
public class DbcpBean {

    // DataSource (Connection Pool)를 담을 static 필드
    private static DataSource ds;

    // static 초기화 블록: 클래스 로딩 시 한 번만 실행됨
    static {
        try {
            // 1. JNDI 서버 객체 생성 (InitialContext)
            Context initContext = new InitialContext();
            
            // 2. Context 객체를 얻어옴 (java:comp/env)
            Context envContext = (Context) initContext.lookup("java:comp/env");
            
            // 3. DataSource 객체 찾기 (JNDI 이름: "jdbc/oracle")
            //    context.xml 에 설정된 name 값 ("jdbc/oracle")을 사용합니다.
            ds = (DataSource) envContext.lookup("jdbc/jspbeginner");
            System.out.println("DataSource lookup 성공!");

        } catch (NamingException e) {
            System.err.println("DataSource lookup 실패: " + e.getMessage());
            e.printStackTrace();
            // 앱 실행에 필수적이므로 RuntimeException 발생시켜서 문제를 알림
            throw new RuntimeException("DataSource lookup 실패", e);
        }
    }

    /**
     * Connection Pool 로부터 Connection 객체 하나를 빌려옵니다.
     *
     * @return Connection 객체
     * @throws SQLException DB 접근 오류 발생 시
     */
    public static Connection getConnection() throws SQLException {
        if (ds == null) {
            // static 초기화 블록에서 문제가 발생했을 경우
            throw new SQLException("DataSource가 초기화되지 않았습니다.");
        }
        // DataSource 로부터 Connection 객체를 얻어냄 (빌려옴)
        return ds.getConnection();
    }

    /**
     * 사용 완료된 Connection 객체를 Connection Pool에 반납합니다.
     * (try-with-resources 구문을 사용하는 것을 더 권장합니다)
     *
     * @param conn 반납할 Connection 객체
     */
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close(); // Pool에게 Connection 반납
            } catch (SQLException e) {
                System.err.println("Connection 반납 실패: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * 사용 완료된 Statement 객체를 닫습니다.
     *
     * @param stmt 닫을 Statement 객체
     */
    public static void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("Statement 닫기 실패: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * 사용 완료된 ResultSet 객체를 닫습니다.
     *
     * @param rs 닫을 ResultSet 객체
     */
    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("ResultSet 닫기 실패: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Connection, Statement, ResultSet 객체를 한 번에 닫거나 반납합니다.
     * (try-with-resources 구문을 사용하는 것을 더 권장합니다)
     *
     * @param conn 반납할 Connection 객체
     * @param stmt 닫을 Statement 객체
     * @param rs   닫을 ResultSet 객체
     */
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        close(rs);
        close(stmt);
        close(conn); // Connection 은 가장 마지막에 반납(닫음)
    }

    /**
     * Connection, Statement 객체를 한 번에 닫거나 반납합니다.
     * (try-with-resources 구문을 사용하는 것을 더 권장합니다)
     *
     * @param conn 반납할 Connection 객체
     * @param stmt 닫을 Statement 객체
     */
    public static void close(Connection conn, Statement stmt) {
        close(stmt);
        close(conn);
    }

    /**
     * (선택적) 초기화된 DataSource 객체를 직접 얻을 필요가 있을 경우 사용합니다.
     * @return 초기화된 DataSource 객체
     */
    public static DataSource getDataSource() {
        return ds;
    }
}







