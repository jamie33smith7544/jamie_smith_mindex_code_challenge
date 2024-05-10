package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;

import javax.naming.CompositeName;

public interface EmployeeService {
    Employee create(Employee employee);
    Employee read(String id);
    Employee update(Employee employee);
    Employee addCompensation(String id, Compensation compensation);
    Compensation getEmployeeCompensation(String id);
}
