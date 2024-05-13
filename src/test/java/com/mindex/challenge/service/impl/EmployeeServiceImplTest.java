package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;
    private String reportingStructureUrl;
    private String addCompensation;
    private String getCompensation;

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
        addCompensation = employeeIdUrl + "/addCompensation";
        getCompensation = employeeIdUrl + "/compensation";
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
        // readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        // assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        // assertEquals("Development Manager", readEmployee.getPosition());
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }

    /**
     * Test the new service and API endpoing to retrieve all reports for an employee
     */
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

    /**
     * Test adding compensation and getting compensation for employees
     */
    @Test
    public void testAddGetCompensation() {
        // Arrange
        Employee employeeAddCompensation = new Employee();
        employeeAddCompensation.setFirstName("Tiana");
        employeeAddCompensation.setLastName("Smith");
        employeeAddCompensation.setDepartment("Software");
        employeeAddCompensation.setPosition("Business Analyst");
        employeeAddCompensation = restTemplate.postForEntity(employeeUrl, employeeAddCompensation, Employee.class).getBody();

        Employee employeeGetCompensation = new Employee();
        employeeGetCompensation.setFirstName("Evan");
        employeeGetCompensation.setLastName("Merritt");
        employeeGetCompensation.setDepartment("Product Management");
        employeeGetCompensation.setPosition("Product Owner");

        // Convert string to ISO 8601
        OffsetDateTime odt = OffsetDateTime.parse("2024-06-11T00:00:00.000Z");
        Instant instant = odt.toInstant();
        Date date = Date.from(instant);
        Compensation compensation = new Compensation();

        compensation.setEffectiveDate(date);
        compensation.setSalary(65000);
        employeeGetCompensation.setCompensation(compensation);
        employeeGetCompensation = restTemplate.postForEntity(employeeUrl, employeeGetCompensation, Employee.class).getBody();

        odt = OffsetDateTime.parse("2024-06-22T00:00:00.000Z");
        instant = odt.toInstant();
        date = Date.from(instant);
        Compensation compensationToAdd = new Compensation();
        compensationToAdd.setEffectiveDate(date);
        compensationToAdd.setSalary(90000);

        // Act
        Employee testEmployee1 = restTemplate.getForEntity(employeeIdUrl, Employee.class, employeeAddCompensation.getEmployeeId()).getBody();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Employee employeeWithAddedCompensation =
                restTemplate.exchange(addCompensation,
                        HttpMethod.PUT,
                        new HttpEntity<Compensation>(compensationToAdd, headers),
                        Employee.class,
                        testEmployee1.getEmployeeId()).getBody();

        Employee testEmployee2 = restTemplate.getForEntity(employeeIdUrl, Employee.class, employeeGetCompensation.getEmployeeId()).getBody();
        Compensation employeeCompensation = restTemplate.getForEntity(getCompensation, Compensation.class, testEmployee2.getEmployeeId()).getBody();

        // Assert
        assertCompensationEquivalence(compensationToAdd, employeeWithAddedCompensation.getCompensation()); // added compensation correctly
        assertCompensationEquivalence(employeeGetCompensation.getCompensation(), employeeCompensation); // Retrieve compensation correctly
    }

    /**
     * Custom checker asserting one Compensation type equals another
     * @param expected
     * @param actual
     */
    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
        assertEquals(expected.getSalary(), actual.getSalary());
    }
}
