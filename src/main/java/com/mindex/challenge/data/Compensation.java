package com.mindex.challenge.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Compensation {
    private Integer salary;
    private String effectiveDate;

    public Compensation() {
    }

    public Integer getSalary() {
        return this.salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public String getEffectiveDate(){ return this.effectiveDate;}

    public void setEffectiveDate(String effectiveDate){
        //DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        this.effectiveDate = effectiveDate;//dateFormat.format(effectiveDate);
    }
}
