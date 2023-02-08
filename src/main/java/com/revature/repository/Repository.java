package com.revature.repository;

import com.revature.model.Employee;
import com.revature.utils.ConnectionUtil;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Repository {
    //AKA register() have authservice call this when registering new user
    public void save(Employee employee){
        //old way of saving to JSON
        /*ObjectMapper mapper = new ObjectMapper();
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

        }*/
        //new way saves to database
        //make sure sql is on a single line & all coded values are replaced with ?
        String sql = "insert into employee(empname,empemail,emppasswd,empmanager) "+
            "values(?, ?, ?, ?) on conflict do nothing;";

        try(Connection con = ConnectionUtil.getConnection()){
            PreparedStatement prstmt = con.prepareStatement(sql);

            //replaces ? with real values we receive
            //uses one-based index rather than java's zero-base
            //doing it this way protects the input from sql injection attacks
            prstmt.setString(1, employee.getName());
            prstmt.setString(2, employee.getEmail());
            prstmt.setString(3, employee.getPassword());
            prstmt.setBoolean(4, employee.isManager());

            //execute() does not expect to return anything from statement
            //executeQuery does not expect something to return after statement execution
            prstmt.execute();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<Employee> getAllEmployees(){
        String sql = "select * from employee";
        List<Employee> listOfEmployees = new ArrayList<Employee>();
        try(Connection con = ConnectionUtil.getConnection()){
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()){
                Employee newEmployee = new Employee();
                newEmployee.setName(rs.getString(1));
                newEmployee.setEmail(rs.getString(2));
                newEmployee.setPassword(rs.getString(3));
                newEmployee.setManager(rs.getBoolean(4));

                listOfEmployees.add(newEmployee);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return listOfEmployees;
    }
}
