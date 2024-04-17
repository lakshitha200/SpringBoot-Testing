package test.example.springboot.test.demo.Controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import test.example.springboot.test.demo.Model.Employee;
import test.example.springboot.test.demo.Service.EmployeeService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.BDDMockito.*;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    Employee employee;

    @BeforeEach
    public void setup(){

         employee = Employee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Cena")
                .email("john@gmail.com")
                .build();

    }

    //Post Controller
    @Test
    @Order(1)
    public void saveEmployeeTest() throws Exception{
        // precondition
        given(employeeService.saveEmployee(any(Employee.class))).willReturn(employee);

        // action
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // verify
        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName",
                        is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(employee.getLastName())))
                .andExpect(jsonPath("$.email",
                        is(employee.getEmail())));
    }

    //Get Controller
    @Test
    @Order(2)
    public void getEmployeeTest() throws Exception{
        // precondition
        List<Employee> employeesList = new ArrayList<>();
        employeesList.add(employee);
        employeesList.add(Employee.builder().id(2L).firstName("Sam").lastName("Curran").email("sam@gmail.com").build());
        given(employeeService.getAllEmployees()).willReturn(employeesList);

        // action
        ResultActions response = mockMvc.perform(get("/api/employees"));

        // verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(employeesList.size())));

    }

    //get by Id controller
    @Test
    @Order(3)
    public void getByIdEmployeeTest() throws Exception{
        // precondition
        given(employeeService.getEmployeeById(employee.getId())).willReturn(Optional.of(employee));

        // action
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId()));

        // verify
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }


    //Update employee
    @Test
    @Order(4)
    public void updateEmployeeTest() throws Exception{
        // precondition
        given(employeeService.getEmployeeById(employee.getId())).willReturn(Optional.of(employee));
        employee.setFirstName("Max");
        employee.setEmail("max@gmail.com");
        given(employeeService.updateEmployee(employee,employee.getId())).willReturn(employee);

        // action
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // verify
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }


    // delete employee
    @Test
    public void deleteEmployeeTest() throws Exception{
        // precondition
        willDoNothing().given(employeeService).deleteEmployee(employee.getId());

        // action
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employee.getId()));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }
}












