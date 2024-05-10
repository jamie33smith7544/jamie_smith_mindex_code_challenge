package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/employee/reportingStructure/{id}")
    public ResponseEntity<ReportingStructure> readStructure(@PathVariable String id){
        ResponseEntity response;

        try{
            return new ResponseEntity<>(this.employeeService.getReportingStructure(id), HttpStatus.OK);
        }
        catch (Exception e){
            LOG.debug("There was an issue getting reporting structure");
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/employee/{id}/addCompensation")
    public ResponseEntity<Employee> addCompensation(@RequestBody Compensation compensation, @PathVariable String id) {
        LOG.debug("Received employee and compensation add request with id: [{}]", id);

        try {
            return new ResponseEntity<>(employeeService.addCompensation(id, compensation), HttpStatus.OK);
        }
        catch (Exception e){
            LOG.debug("There was an issue adding compensation");
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/employee/{id}/compensation")
    public ResponseEntity<Compensation> addCompensation(@PathVariable String id) {
        LOG.debug("Received request to get compensation for employee: [{}]", id);

        try{
            return new ResponseEntity<>(employeeService.getEmployeeCompensation(id), HttpStatus.OK);
        }
        catch (Exception e){
            LOG.debug("There was an issue retrieving compensation");
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
