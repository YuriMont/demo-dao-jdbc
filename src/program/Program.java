
package program;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

    public static void main(String[] args) throws ParseException {
        SellerDao sellerDao = DaoFactory.createSellerDao();
        
        Seller seller = sellerDao.findById(3);
        System.out.println("==== TEST 1: seller findById ==== ");
        System.out.println(seller);
         System.out.println("==== TEST 2: seller findByDepartment ==== ");
         Department department = new Department(2, null);
         List<Seller> list = sellerDao.findByDepartment(department);
         list.forEach(System.out::println);
         list.clear();
         System.out.println("==== TEST 3: seller findAll ==== ");
         list = sellerDao.findAll();
         list.forEach(System.out::println);
         System.out.println("==== TEST 4: seller insert ==== ");
         Seller newSeller = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, department);
         sellerDao.insert(newSeller);
         System.out.println(" id = "+newSeller.getId());
         System.out.println("==== TEST 5: seller update ==== ");
         seller = sellerDao.findById(1);
         seller.setName("Martha Waine");
         Date data = new SimpleDateFormat("dd/MM/yyyy").parse("23/08/1988");
         seller.setBithDate(data);
         sellerDao.update(seller);
         
         
    }
    
}
