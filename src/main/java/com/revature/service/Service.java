package com.revature.service;

import com.revature.model.Employee;
import com.revature.repository.Repository;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class Service {
    private final Repository empRepo = new Repository();
    private final ObjectMapper map = new ObjectMapper();

    public String getAllEmployees(){
        List<Employee> employeeList=empRepo.getAllEmployees();
        String jsonString = "";

        try {
            jsonString = map.writeValueAsString(employeeList);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
    public void saveEmployee(String empJson){
        try{
            Employee newEmployee=map.readValue(empJson, Employee.class);
            empRepo.save(newEmployee);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
