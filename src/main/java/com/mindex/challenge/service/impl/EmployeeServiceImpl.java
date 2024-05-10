package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLSession;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Creating employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }

    // Task 1 Code
    /**
     * Service function that initializes ReportingStructure and sets all the variables
     * @param employeeId
     * @return reportingStructure of main employee and total of employees who report to them
     */
    @Override
    public ReportingStructure getReportingStructure(String employeeId) {
        LOG.debug("Getting all reports for employee with id [{}]", employeeId);
        //Initialize types
        ReportingStructure reportingStructure = new ReportingStructure();
        ResponseEntity response;

        // get employee and set number of reports
        Employee mainEmployee = this.read(employeeId);
        reportingStructure.setEmployee(mainEmployee);
        reportingStructure.setNumberOfReports(countReports(mainEmployee, mainEmployee.getEmployeeId()));

        return reportingStructure;
    }

    /**
     * Recursive function to loop through all sub-levels of employees and count the total reports
     * @param employee- current employee to search directReports from
     * @param mainId- passed in main employee id to prevent being added to report total
     * @return the number of reports for a certain employee
     */
    public int countReports(Employee employee, String mainId){
        Employee currentEmployee;
        List<Employee> directReports = employee.getDirectReports();
        int reports = 0;

        // base case to prevent main emplyee from being added to total
        if(employee.getEmployeeId() != mainId){
            reports = 1;
        }

        // base case, if no more directReports to loop through
        if (directReports == null){
            return reports;
        }

        // recursive calls
        for (int i=0; i<directReports.size();i++) {
            // get current directReport and re-call employeeService.read to verify non null data
            String employeeId = directReports.get(i).getEmployeeId();
            currentEmployee = this.read(employeeId);
            reports += countReports(currentEmployee, mainId);
        }
        return reports;
    }

    // Task 2 Code
    @Override
    public Employee addCompensation(String id, Compensation compensation) {
        Employee employee = employeeRepository.findByEmployeeId(id);
        employee.setCompensation(compensation);

        return this.update(employee);
    }

    @Override
    public Compensation getEmployeeCompensation(String id) {
        Employee employee = employeeRepository.findByEmployeeId(id);
        return employee.getCompensation();
    }
}
