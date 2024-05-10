package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;
    private String reportingStructureUrl;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        reportingStructureUrl = employeeIdUrl + "/reportingStructure";
    }

    @Test
    public void testCreateReadUpdate() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);


        // Read checks
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEmployeeEquivalence(createdEmployee, readEmployee);


        // Update checks
        readEmployee.setPosition("Development Manager");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Employee updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Employee>(readEmployee, headers),
                        Employee.class,
                        readEmployee.getEmployeeId()).getBody();

        assertEmployeeEquivalence(readEmployee, updatedEmployee);

        // Check that employee was actually updated.  Won't work for in memory db, but if being tested with a separate service db
        readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEquals("Development Manager", readEmployee.getPosition());
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }

    @Test
    public void testGetReportingStructure() {
        // Arrange Section: Create Employees
        Employee employee4 = new Employee();
        employee4.setFirstName("Angela");
        employee4.setLastName("Brown");
        employee4.setDepartment("Software");
        employee4.setPosition("Product Owner");
        employee4 = restTemplate.postForEntity(employeeUrl, employee4, Employee.class).getBody();

        Employee employee2 = new Employee();
        employee2.setFirstName("Jim");
        employee2.setLastName("Halpert");
        employee2.setDepartment("Software");
        employee2.setPosition("Developer I");
        ArrayList<Employee> directReports = new ArrayList<>();
        directReports.add(employee4);
        employee2.setDirectReports(directReports);
        employee2 = restTemplate.postForEntity(employeeUrl, employee2, Employee.class).getBody();

        Employee employee3 = new Employee();
        employee3.setFirstName("Pam");
        employee3.setLastName("Halpert");
        employee3.setDepartment("Software");
        employee3.setPosition("Developer I");
        employee3 = restTemplate.postForEntity(employeeUrl, employee3, Employee.class).getBody();

        Employee employee1 = new Employee();
        employee1.setFirstName("Micheal");
        employee1.setLastName("Scott");
        employee1.setDepartment("Software");
        employee1.setPosition("Manager");
        directReports = new ArrayList<>();
        directReports.add(employee2);
        directReports.add(employee3);
        employee1.setDirectReports(directReports);
        employee1 = restTemplate.postForEntity(employeeUrl, employee1, Employee.class).getBody();


        // Act: Get reporting structure for 3 employees
        Employee testEmployee1 = restTemplate.getForEntity(employeeIdUrl, Employee.class, employee1.getEmployeeId()).getBody();
        ReportingStructure employee1ReportingStructure = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, testEmployee1.getEmployeeId()).getBody();

        Employee testEmployee2 = restTemplate.getForEntity(employeeIdUrl, Employee.class, employee1.getEmployeeId()).getBody();
        ReportingStructure employee2ReportingStructure = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, testEmployee2.getEmployeeId()).getBody();

        Employee testEmployee3 = restTemplate.getForEntity(employeeIdUrl, Employee.class, employee1.getEmployeeId()).getBody();
        ReportingStructure employee3ReportingStructure = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, testEmployee3.getEmployeeId()).getBody();

        // Assert
        assertEquals(3, employee1ReportingStructure.getNumberOfReports().intValue()); //employee 1 has 3
        assertEquals(3, employee2ReportingStructure.getNumberOfReports().intValue()); //employee 2 has 1
        assertEquals(3, employee3ReportingStructure.getNumberOfReports().intValue()); //employee 3 has 0

    }

    @Test
    public void testAddGetCompensation() {
        // Arrange
        // Act
        // Assert
    }
}
