package com.revature;

import com.revature.model.Employee;
import com.revature.service.Service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

/*Mocking is when you replace a real class into something else,
* usually a fake, and will hardcode a value to isolate the testing class*/
public class ServiceTest {
    @Test
    public void Get_All_Employees_Should_Give_All_Employees(){
        //Arrange
        Service serv = new Service();

        List<Employee> expectedListofEmployees=new ArrayList<>();
        //Mockito.when(mockEmpRepo.getAllEmployee()).thenReturn(expectedListofEmployees)

        //Act
        String jsonListEmployee = serv.getAllEmployees();

        //Assert
        Assertions.assertNotEquals("null",jsonListEmployee);
    }
}
