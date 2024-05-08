package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService{

    // Initialize logger for class
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureService.class);

    // Wire employee service
    @Autowired
    private EmployeeService employeeService;

    /**
     * Service function that initializes ReportingStructure and sets all the variables
     * @param employeeId
     * @return reportingStructure of main employee and total of employees who report to them
     */
    @Override
    public ReportingStructure getReportingStructure(String employeeId) {
        LOG.debug("Getting all reports for employee with id [{}]", employeeId);
        //Initialize type and get employee data
        ReportingStructure reportingStructure = new ReportingStructure();
        Employee mainEmployee = employeeService.read(employeeId);

        // set employee and number of reports
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
            currentEmployee = employeeService.read(employeeId);
            reports += countReports(currentEmployee, mainId);
        }
        return reports;
    }
}
