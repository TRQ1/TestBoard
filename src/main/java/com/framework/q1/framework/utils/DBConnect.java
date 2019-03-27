package com.framework.q1.framework.utils;

import java.sql.*;

public class DBConnect {

    public Connection connDb() {
        Connection conn = null;
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            String url = "jdbc:mariadb://localhost:3306/boards_test";
            String userid = "root";
            String passwd = "qwer0987";
            conn = DriverManager.getConnection(url, userid, passwd);
        } catch (SQLException e) {
            System.out.println(e);
        } catch (ClassNotFoundException c) {
            System.out.println(c);
        }
        return conn;
    }

    /**
     *
     * @param pstmt
     * @param conn
     * PrepareStatement, ResultSet, Connection 종료를 위한 메소드
     */
    public void close(PreparedStatement pstmt, Connection conn) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        } else if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }

    }

    public void resultClose(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }
}
