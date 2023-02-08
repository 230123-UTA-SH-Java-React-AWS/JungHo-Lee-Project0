package com.revature.service;

import com.revature.model.Employee;
import com.revature.repository.AuthRepository;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class AuthService extends Service{
    //login method checks user input with database for match
    //register method collects user input and sends to authrepo to add to db
    private final AuthRepository empRepo = new AuthRepository();
    private final ObjectMapper map = new ObjectMapper();

    public String login(String empJson){
        String jsonString="";
        try {
            Employee employee=map.readValue(empJson, Employee.class);
            String mail_in = employee.getEmail(), pass_in=employee.getPassword();
            if(passCheck(mail_in,pass_in)){
                jsonString=map.writeValueAsString("Login Successful");
                System.out.println("Login success");
            }else{
                jsonString=map.writeValueAsString("Login Failed");
                System.out.println("Login failed");
            }
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return jsonString;
    }
    public boolean passCheck(String email, String password){
        List<Employee> mailPassList = empRepo.credGet();
        boolean match = false;
        for(Employee e: mailPassList){
            if(email.equals(e.getEmail()) && password.equals(e.getPassword())){
                match=true;
                break;
            }
        }
        return match;
    }

    public boolean emailCheck(String email){
        List<Employee> mailList = empRepo.credGet();
        boolean unique = true;
        for(Employee e: mailList){
            if(email.equals(e.getEmail())){
                unique = false;
                break;
            }
        }
        return unique;
    }
    public String register(String empJson){
        String jsonString = "";
        try {
            Employee newEmployee=map.readValue(empJson, Employee.class);
            String email_in = newEmployee.getEmail();
            if(!emailCheck(email_in)){
                jsonString=map.writeValueAsString("Email in use");
                System.out.println("Email in use");
            }else{
                empRepo.save(newEmployee);
                jsonString=map.writeValueAsString("User registered");
                System.out.println("Registration successful");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
