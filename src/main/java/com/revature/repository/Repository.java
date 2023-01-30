package com.revature.repository;

import com.revature.model.Employee;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Repository {
    public void save(Employee employee){
        ObjectMapper mapper = new ObjectMapper();
        String jsonObject="";
        try{
            jsonObject=mapper.writeValueAsString(employee);

            File empFile=new File("./src/main/java/com/revature/repository/employee.json");
            empFile.createNewFile();

            FileWriter writer=new FileWriter("./src/main/java/com/revature/repository/employee.json");
            writer.write(jsonObject);
            writer.close();

        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
