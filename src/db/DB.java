package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {

    public static Connection conn;
    public static final String driver = "com.mysql.jdbc.Driver";
    public static final String user = "root";
    public static final String password = "1234567";
    public static final String url = "jdbc:mysql://localhost:3306/coursejdbc";

    public DB() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    public Connection getConnection() {
        return conn;
    }

    public static void closeConnection() {
        if (conn != null) {
            try{
                conn.close();
            }catch(SQLException e){
                throw new DbException(e.getMessage());
            }
        }
    }
    public static void closeStatement(Statement st){
        if(st!=null){
           try{
               st.close();
           }catch(SQLException e){
               throw new DbException(e.getMessage());
           }
        }
    }
    public static void closeResultSet(ResultSet rs){
        if(rs!=null){
            try{
                rs.close();
            }catch(SQLException e){
                throw new DbException(e.getMessage());
            }
            }
    }

}
