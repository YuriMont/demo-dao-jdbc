package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String user = "root";
    private static final String password = "1234567";
    private static final String url = "jdbc:mysql://localhost:3306/coursejdbc";

    public static Connection getConnection() {    
            try {
            Class.forName(driver);
            return DriverManager.getConnection(url, user, password);
            
        } catch (ClassNotFoundException | SQLException e){
            throw new DbException(e.getMessage());
        } 
    }

    public static void closeConnection(Connection conn) {
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
