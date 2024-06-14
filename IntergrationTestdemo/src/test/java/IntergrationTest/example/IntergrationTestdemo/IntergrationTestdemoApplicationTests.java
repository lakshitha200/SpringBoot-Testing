package IntergrationTest.example.IntergrationTestdemo;

import IntergrationTest.example.IntergrationTestdemo.Model.Employee;
import IntergrationTest.example.IntergrationTestdemo.Repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
class IntergrationTestdemoApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private ObjectMapper objectMapper;

	Employee employee;



	@Container
	static MySQLContainer mySQLContainer = new MySQLContainer("mysql:latest");

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
		registry.add("spring.datasource.username", mySQLContainer::getUsername);
		registry.add("spring.datasource.password", mySQLContainer::getPassword);
	}

	@BeforeAll
	static void beforeAll() {
		mySQLContainer.start();

		System.out.println(mySQLContainer.getDatabaseName());
		System.out.println(mySQLContainer.getJdbcUrl());
		System.out.println(mySQLContainer.getUsername());
		System.out.println(mySQLContainer.getPassword());
	}

	@AfterAll
	static void afterAll() {
		mySQLContainer.stop();
	}


	@BeforeEach
	public void setup(){
		employee = Employee.builder()
				.id(1L)
				.firstName("Lakshitha")
				.lastName("Fernando")
				.email("lakshitha@gmail.com")
				.build();

	}


	//Save Employee Test
	@Test
	@Order(1)
	public void saveEmployeeTest() throws Exception {
		// precondition

		/* ** Precondition is in the above setup() method.
		 Other test methods can access this employee object instead
		 create new employee for each test methods */

		// Action and Verify
		mockMvc.perform(post("/api/employees")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employee)))

				.andExpect(status().isCreated())
				.andDo(print())
				.andExpect(jsonPath("$.firstName").value("Lakshitha"))
				.andExpect(jsonPath("$.lastName").value("Fernando"))
				.andExpect(jsonPath("$.email").value("lakshitha@gmail.com"));
	}


	//Get All employees Test
	@Test
	@Order(2)
	public void getAllEmployee() throws Exception {
		// precondition
		List<Employee> employeesList = new ArrayList<>();
		employeesList.add(employee);
		employeesList.add(Employee.builder().id(2L).firstName("Sam").lastName("Curran").email("sam@gmail.com").build());
		employeeRepository.saveAll(employeesList);


		// Action and Verify
		mockMvc.perform(get("/api/employees"))

				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.size()",is(employeesList.size())));

	}

	//Get employeeById Test
	@Test
	@Order(3)
	public void getEmployeeById() throws Exception{

		// Action and Verify
		mockMvc.perform(get("/api/employees/{id}", employee.getId()))

				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(employee.getLastName())))
				.andExpect(jsonPath("$.email", is(employee.getEmail())));

	}

	//Update employees Test
	@Test
	@Order(4)
	public void updateEmployee() throws Exception {

		//Action
		employee.setEmail("lakshitha12345@gmail.com");
		employee.setEmail("Fdo");
		mockMvc.perform(put("/api/employees/{id}", employee.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employee)))
				//verify
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.lastName", is(employee.getLastName())))
				.andExpect(jsonPath("$.email", is(employee.getEmail())));
	}

	//Delete employees Test
	@Test
	@Order(5)
	public void deleteEmployee() throws Exception{

		// Action and Verify
		mockMvc.perform(delete("/api/employees/{id}", employee.getId()))
				.andExpect(status().isOk())
				.andDo(print());
	}
}
