package test.example.springboot.test.demo.Service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import test.example.springboot.test.demo.Model.Employee;
import test.example.springboot.test.demo.Repository.EmployeeRepository;
import test.example.springboot.test.demo.Service.Impl.EmployeeServiceImpl;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeServiceUnitTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;


    @BeforeEach
    public void setup(){

        employee = Employee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Cena")
                .email("john@gmail.com")
                .build();

    }

    @Test
    @Order(1)
    public void saveEmployeeTest(){
        // precondition
        given(employeeRepository.save(employee)).willReturn(employee);

        //action
        Employee savedEmployee = employeeService.saveEmployee(employee);

        // verify the output
        System.out.println(savedEmployee);
        assertThat(savedEmployee).isNotNull();
    }

    @Test
    @Order(2)
    public void getEmployeeById(){
        // precondition
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        // action
        Employee existingEmployee = employeeService.getEmployeeById(employee.getId()).get();

        // verify
        System.out.println(existingEmployee);
        assertThat(existingEmployee).isNotNull();

    }


    @Test
    @Order(3)
    public void getAllEmployee(){
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Sam")
                .lastName("Curran")
                .email("sam@gmail.com")
                .build();

        // precondition
        given(employeeRepository.findAll()).willReturn(List.of(employee,employee1));

        // action
        List<Employee> employeeList = employeeService.getAllEmployees();

        // verify
        System.out.println(employeeList);
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isGreaterThan(1);
    }

    @Test
    @Order(4)
    public void updateEmployee(){

        // precondition
        given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));
        employee.setEmail("max@gmail.com");
        employee.setFirstName("Max");
        given(employeeRepository.save(employee)).willReturn(employee);

        // action
        Employee updatedEmployee = employeeService.updateEmployee(employee,employee.getId());

        // verify
        System.out.println(updatedEmployee);
        assertThat(updatedEmployee.getEmail()).isEqualTo("max@gmail.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Max");
    }

    @Test
    @Order(5)
    public void deleteEmployee(){

        // precondition
        willDoNothing().given(employeeRepository).deleteById(employee.getId());

        // action
        employeeService.deleteEmployee(employee.getId());

        // verify
        verify(employeeRepository, times(1)).deleteById(employee.getId());
    }


}
