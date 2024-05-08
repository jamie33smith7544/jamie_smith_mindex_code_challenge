package com.mindex.challenge.data;

/**
 * Model that contains an employee, and total number of reports
 * Also includes getters and setters for varaibles associated for this type
 */
public class ReportingStructure {
    private Employee employee;
    private Integer numberOfReports;

    public ReportingStructure() {
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Integer getNumberOfReports() {
        return numberOfReports;
    }

    public void setNumberOfReports(Integer numberOfReports) {
        this.numberOfReports = numberOfReports;
    }
}