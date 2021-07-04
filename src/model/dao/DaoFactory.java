
package model.dao;


import db.DB;
import java.sql.Connection;
import model.dao.impl.DepartmentDaoJDBC;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {
    public static SellerDao createSellerDao(){
        Connection conn = DB.getConnection();
        return new SellerDaoJDBC(conn);
    }
    public static DepartmentDao createDepartamentDao(){
        Connection conn = DB.getConnection();
        return new DepartmentDaoJDBC(conn);
    }
}
