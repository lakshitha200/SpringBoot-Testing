package test.example.springboot.test.demo.Repository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import test.example.springboot.test.demo.Model.Employee;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeRepositoryUnitTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @DisplayName("Test 1:Save Employee Test")
    @Order(1)
    @Rollback(value = false)
    public void saveEmployeeTest(){

        //Action
        Employee employee = Employee.builder()
                .firstName("Sam")
                .lastName("Curran")
                .email("sam@gmail.com")
                .build();

        employeeRepository.save(employee);

        //Verify
        System.out.println(employee);
        Assertions.assertThat(employee.getId()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    public void getEmployeeTest(){

        //Action
        Employee employee = employeeRepository.findById(1L).get();
        //Verify
        System.out.println(employee);
        Assertions.assertThat(employee.getId()).isEqualTo(1L);
    }

    @Test
    @Order(3)
    public void getListOfEmployeesTest(){
        //Action
        List<Employee> employees = employeeRepository.findAll();
        //Verify
        System.out.println(employees);
        Assertions.assertThat(employees.size()).isGreaterThan(0);

    }

    @Test
    @Order(4)
    @Rollback(value = false)
    public void updateEmployeeTest(){

        //Action
        Employee employee = employeeRepository.findById(1L).get();
        employee.setEmail("samcurran@gmail.com");
        Employee employeeUpdated =  employeeRepository.save(employee);

        //Verify
        System.out.println(employeeUpdated);
        Assertions.assertThat(employeeUpdated.getEmail()).isEqualTo("samcurran@gmail.com");

    }

    @Test
    @Order(5)
    @Rollback(value = false)
    public void deleteEmployeeTest(){
        //Action
        employeeRepository.deleteById(1L);
        Optional<Employee> employeeOptional = employeeRepository.findById(1L);

        //Verify
        Assertions.assertThat(employeeOptional).isEmpty();
    }

}
