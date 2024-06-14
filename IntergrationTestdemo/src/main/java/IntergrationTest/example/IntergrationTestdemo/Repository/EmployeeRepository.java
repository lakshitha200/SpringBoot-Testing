package IntergrationTest.example.IntergrationTestdemo.Repository;

import IntergrationTest.example.IntergrationTestdemo.Model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee,Long>{

}
