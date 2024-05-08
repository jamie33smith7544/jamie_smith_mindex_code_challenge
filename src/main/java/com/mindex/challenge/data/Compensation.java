package com.mindex.challenge.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Compensation {
    private Employee employee;
    private Integer salary;
    private String effectiveDate;

    public Compensation() {
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Integer getSalary() {
        return this.salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public String getEffectiveDate(){ return this.effectiveDate;}

    public void setEffectiveDate(Date effectiveDate){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        this.effectiveDate = dateFormat.format(effectiveDate);
    }
}
