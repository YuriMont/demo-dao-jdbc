
package model.dao.impl;

import db.DB;
import db.DbException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{

    private Connection conn;
    
    public SellerDaoJDBC(Connection conn){
        this.conn = conn;
    }
    
    @Override
    public void insert(Seller obj) {

    }

    @Override
    public void update(Seller obj) {
        
    }

    @Override
    public void deleteById(Integer id) {
        
    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("SELECT * , d.nome AS DepNome FROM seller s JOIN departamento d ON s.departamentoId = d.id WHERE s.id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            
            if(rs.next()){
                Department dep = instantiateDepartment(rs);
                Seller obj = instantiateSeller(rs, dep);
                return obj;
                
            }
            return null;
            
        }catch(SQLException e){
            throw new DbException(e.getMessage());
        }finally{
            DB.closeStatement(st);
            DB.closeResultSet(rs);
            
        }
    }

    @Override
    public List<Seller> findAll() {
        return null;
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt(6));
        dep.setName(rs.getString(7));
        return dep;
    }

    private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
        Seller obj = new Seller();
        obj.setId(rs.getInt(1));
        obj.setName(rs.getString(2));
        obj.setEmail(rs.getString(3));
        obj.setBithDate(rs.getDate(4));
        obj.setBaseSalary(rs.getDouble(5));
        obj.setDepartment(dep);
        return obj;
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("SELECT s.*, d.nome AS DepName FROM seller s JOIN  departamento d "+
                                       "ON s.departamentoId = d.id "+
                                       "WHERE d.id = ? ORDER BY nome");
            st.setInt(1, department.getId());
            rs = st.executeQuery();
            
            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();
            
            while(rs.next()){
                Department dep = map.get(rs.getInt(6));
                
                if(dep==null){
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt(6), dep);
                }                
                Seller obj = instantiateSeller(rs, dep);
                list.add(obj);         
            }
            return list;
        }catch(SQLException e){
            throw new DbException("Erro: "+e.getMessage());
        }finally{
        DB.closeResultSet(rs);
        DB.closeStatement(st);
    }
    }
    
}
