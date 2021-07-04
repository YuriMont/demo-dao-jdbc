
package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import model.dao.DepartmentDao;
import model.entities.Department;
import db.DbException;
import db.DB;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DepartmentDaoJDBC implements DepartmentDao{
    
    private Connection conn;

    public DepartmentDaoJDBC(Connection conn) {
        this.conn = conn;
    }
    private Department instantiateDepartment(ResultSet rs) throws SQLException{
        Department dep = new Department();
               dep.setId(rs.getInt(1));
               dep.setName(rs.getString(2));
        return dep;       
    }

    @Override
    public void insert(Department obj) {
       PreparedStatement st = null;
       try{
           st = conn.prepareStatement("INSERT INTO departamento VALUES (DEFAULT, ?)", Statement.RETURN_GENERATED_KEYS);
           st.setString(1, obj.getName());
           int linhas = st.executeUpdate();
           if(linhas>0){
               ResultSet rs = st.getGeneratedKeys();
               System.out.println("Inserido com sucesso!");
               if(rs.next()){
                   int id = rs.getInt(1);
                   obj.setId(id);
               }
               DB.closeResultSet(rs);
           }
           else{
               throw new DbException("Erro inesperado nenhuma linha foi alterada");
           }
       }catch(SQLException e){
           throw new DbException("Erro: "+e.getMessage());
       }finally{
           DB.closeStatement(st);
       }
    }

    @Override
    public void update(Department obj) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("UPDATE departamento SET nome = ? WHERE id = ?", 
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1, obj.getName());
            st.setInt(2, obj.getId());
            
            int linhas = st.executeUpdate();
            if(linhas>0){
                System.out.println("Modificado com sucesso!");
            }
            else{
                throw new DbException("Erro inesperado nenhuma linha foi alterada");
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
            st = conn.prepareStatement("DELETE FROM departamento WHERE id = ?");
            st.setInt(1, id);
            int linhas = st.executeUpdate();
            if(linhas>0){
                System.out.println("Linha excluida com sucesso!");
            }else{
                throw new DbException("Erro inesperado nenhuma linha foi alterada");
            }
        }catch(SQLException e){
            throw new DbException("Erro: "+e.getMessage());
        }finally{
            DB.closeStatement(st);
        }
    }

    @Override
    public Department findById(Integer id) {
       PreparedStatement st = null;
       ResultSet rs = null;
       try{
           st = conn.prepareStatement("SELECT * FROM departamento WHERE id = ? ORDER BY nome");
           st.setInt(1, id);
           
           rs = st.executeQuery();
           if(rs.next()){
               Department dep = instantiateDepartment(rs);
               return dep;
           }
           return null;
       }catch(SQLException e){
           throw new  DbException("Erro: "+e.getMessage());
       }finally{
           DB.closeResultSet(rs);
           DB.closeStatement(st);
       }
           
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("SELECT * FROM departamento ORDER BY id");
            rs = st.executeQuery();
            
            List<Department> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();
            while(rs.next()){
                Department dep = map.get(rs.getInt(1));
                if(dep == null){
                dep = instantiateDepartment(rs);
                map.put(rs.getInt(1), dep); 
                }
                
                list.add(dep);
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
