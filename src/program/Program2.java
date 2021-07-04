
package program;

import java.util.ArrayList;
import java.util.List;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

    public static void main(String[] args) {
        DepartmentDao dd = DaoFactory.createDepartamentDao();
        System.out.println("==== Test 1 DepartmentDaoJDBC findById ====");
        Department dep = dd.findById(1);
        System.out.println(dep);
        System.out.println("==== Test 2 DepartmentDaoJDBC insert ====");
        Department newDep = new Department(null, "Imoveis");
        dd.insert(newDep);
        System.out.println("id = "+newDep.getName());
        System.out.println("==== Test 3 DepartmentDaoJDBC update ====");
        dep = dd.findById(1);
        dep.setName("Informatica");
        dd.update(dep);
         System.out.println("==== Test 4 DepartmentDaoJDBC deleteById ====");
         dd.deleteById(8);
          System.out.println("==== Test 5 DepartmentDaoJDBC findAll ====");
        List<Department>list = new ArrayList<>();
        list = dd.findAll();
        list.forEach(System.out::println);
        
    }
    
}
