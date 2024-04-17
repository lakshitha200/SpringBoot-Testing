package test.example.springboot.test.demo.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import test.example.springboot.test.demo.Model.Employee;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);
}
