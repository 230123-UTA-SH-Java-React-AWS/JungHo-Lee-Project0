package com.revature.service;

import com.revature.model.Employee;
import com.revature.repository.Repository;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class Service {
    public void saveEmployee(String empJson){
        Repository repo = new Repository();
        ObjectMapper mapper = new ObjectMapper();
        try{
            Employee newEmployee=mapper.readValue(empJson, Employee.class);
            repo.save(newEmployee);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonParseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
