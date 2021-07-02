
package model.dao;


import db.DB;
import java.sql.Connection;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {
    public static SellerDao createSellerDao(){
        Connection conn = DB.getConnection();
        return new SellerDaoJDBC(conn);
    }
}
