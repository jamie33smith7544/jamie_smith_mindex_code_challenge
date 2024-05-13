package com.mindex.challenge.data;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Model that specifies componenets of an employee's compensation
 * Salary: Integer type
 * effectiveDate: Date type
 */
public class Compensation {
    private Integer salary;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date effectiveDate;

    public Compensation() {
    }

    public Integer getSalary() {
        return this.salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public Date getEffectiveDate(){ return this.effectiveDate;}

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
