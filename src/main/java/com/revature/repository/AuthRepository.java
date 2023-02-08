package com.revature.repository;

import com.revature.model.Employee;
import com.revature.utils.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthRepository extends Repository{
    public List<Employee> credGet(){
        String sql = "select empemail, emppasswd from employee";
        List<Employee> listOfEmployees = new ArrayList<>();
        try(Connection con = ConnectionUtil.getConnection()){
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()){
                Employee newEmployee = new Employee();
                newEmployee.setEmail(rs.getString(1));
                newEmployee.setPassword(rs.getString(2));

                listOfEmployees.add(newEmployee);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return listOfEmployees;
    }
    public Employee getEmployeebyEmail(String email){
        //empty objects for storage
        List<Employee>employeeList = new ArrayList<>();
        Employee employee=null;
        String sql = "select * from employee where empemail = ?";
        try(Connection con = ConnectionUtil.getConnection()){
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1,email);
            ResultSet rs = statement.executeQuery();

            while (rs.next()){
                //grabbing table data
                Employee newEmployee = new Employee();
                newEmployee.setEmail(rs.getString(3));
                newEmployee.setPassword(rs.getString(4));
                newEmployee.setEmpID(rs.getInt(1));
                newEmployee.setName(rs.getString(2));
                newEmployee.setManager(rs.getBoolean(5));

                employeeList.add(newEmployee);
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        if(employeeList.size()>0) employee=employeeList.get(0);
        return employee;
    }
    public int getEmployeeId(String email){
        if(email == null) return -1;
        List<Integer> employeeIdList = new ArrayList<>();
        int employeeId = -1;

        String sql = "select empid from employee where empemail = ?";
        try (Connection con = ConnectionUtil.getConnection()) {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                employeeIdList.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

        if(employeeIdList.size() > 0) employeeId = employeeIdList.get(0);
        return employeeId;
    }
}
