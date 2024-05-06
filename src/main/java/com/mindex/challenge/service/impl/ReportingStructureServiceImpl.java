package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ReportingStructureServiceImpl implements ReportingStructureService{

    // Initialize logger for class
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureService.class);

    // Automatically wire the in memory database
    @Autowired
    private EmployeeRepository employeeRepository;

    // Initializer class that sets up structure, employee data, and calls function for recursive classes
    @Override
    public ReportingStructure getReportingStructure(String employeeId) {
        ReportingStructure test = new ReportingStructure();
        return test;
    }

    // recursive class to return number of reports
}
