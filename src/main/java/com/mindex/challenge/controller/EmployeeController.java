package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import java.net.http.HttpResponse;

@RestController
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/employee")
    public Employee create(@RequestBody Employee employee) {
        LOG.debug("Received employee create request for [{}]", employee);

        return employeeService.create(employee);
    }

    @GetMapping("/employee/{id}")
    public Employee read(@PathVariable String id) {
        LOG.debug("Received employee create request for id [{}]", id);

        return employeeService.read(id);
    }

    @PutMapping("/employee/{id}")
    public Employee update(@PathVariable String id, @RequestBody Employee employee) {
        LOG.debug("Received employee create request for id [{}] and employee [{}]", id, employee);

        employee.setEmployeeId(id);
        return employeeService.update(employee);
    }

    /**
     * New GET endpoint that takes in an employee id, and calls logic from ReportingStructureService
     * @param id- The employee id
     * @return Reporting Structure of the Employee and number of reports under that employee
     */
    @GetMapping(value = "/employee/{id}/reportingStructure", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReportingStructure readStructure(@PathVariable String id){
        return this.employeeService.getReportingStructure(id);
    }

    /**
     * PUT endpoint to update employee with a new compensation
     * @param compensation
     * @param id
     * @return Employee with compensation added
     */
    @PutMapping(value="/employee/{id}/addCompensation", produces = MediaType.APPLICATION_JSON_VALUE)
    public Employee addCompensation(@RequestBody Compensation compensation, @PathVariable String id) {
        LOG.debug("Received employee and compensation add request with id: [{}]", id);
        return employeeService.addCompensation(id, compensation);
    }

    /**
     * GET endpoint to fetch employee compensation data
     * @param id
     * @return an employee's compensation data
     */
    @GetMapping(value="/employee/{id}/compensation", produces = MediaType.APPLICATION_JSON_VALUE)
    public Compensation addCompensation(@PathVariable String id) {
        LOG.debug("Received request to get compensation for employee: [{}]", id);

        return employeeService.getEmployeeCompensation(id);
    }
}
