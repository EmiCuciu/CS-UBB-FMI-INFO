package com.example.lab4.repository;

import com.example.lab4.model.ComputerRepairedForm;

import java.util.List;

public interface ComputerRepairedFormRepository extends Repository<Integer, ComputerRepairedForm> {
    List<ComputerRepairedForm> filterByEmployee(String employee);
    List<ComputerRepairedForm> filterByEmployeeAndDate(String employee, String date);
}
