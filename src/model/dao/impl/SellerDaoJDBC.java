
package model.dao.impl;

import db.DB;
import db.DbException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        PreparedStatement st = null;
        
        try{
            st = conn.prepareStatement("INSERT INTO seller VALUES (DEFAULT, ?, ?, ?, ?, ?)", Statement
            .RETURN_GENERATED_KEYS);
            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, new java.sql.Date(obj.getBithDate().getTime()));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());
            
            int linhas = st.executeUpdate();
            
            if(linhas>0){
                ResultSet rs = st.getGeneratedKeys();
                System.out.print("Inserido com sucesso! ");
                if(rs.next()){
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            }
            else{
                throw new DbException("Erro inesperado, nenhuma linha foi afetada");
            }
        }catch(SQLException e){
            throw new DbException("Erro: "+e.getMessage());
        }finally{
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Seller obj) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("UPDATE seller " + 
            "SET nome = ?, email = ?, data_nascimento = ?, baseSalary = ?, departamentoId = ? " +
            "WHERE id = ?", Statement.RETURN_GENERATED_KEYS);
            
            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, new java.sql.Date(obj.getBithDate().getTime()));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());
            st.setInt(6, obj.getId());
            
            int linhas = st.executeUpdate();
            if(linhas>0){
                System.out.println("Linhas modificada com sucesso! ");
            }
            else{
                throw new DbException("Erro inesperado nenhuma linha foi afetada");
            }
            
        }catch(SQLException e){
            throw new DbException("Erro: "+e.getMessage());
        }finally{
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("DELETE FROM seller WHERE id = ? ");
            st.setInt(1, id);
            int linhas = st.executeUpdate();
            if(linhas> 0){
                System.out.println("Linhas excluida com sucesso! ");
            }
            else{
                throw new DbException("Erro inesperado nenhuma linha foi afetada");
            }
            
        }catch(SQLException e){
            throw new DbException("Erro: "+e.getMessage());
        }finally{
            DB.closeStatement(st);
        }
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
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("SELECT s.*, d.nome DepName FROM seller s JOIN departamento d "
                                      +"ON s.departamentoId = d.id "
                                      +"ORDER BY nome ");
            rs = st.executeQuery();
            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();
            
            while(rs.next()){
                Department dep = map.get(rs.getInt(6));
                if(dep == null){
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
