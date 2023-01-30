package com.revature;

import com.revature.controller.Controller;
import com.revature.model.Employee;
import com.revature.repository.Repository;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Starting backend server");
        HttpServer server = HttpServer.create(new InetSocketAddress(8000),0);
        server.createContext("/register",new Controller());
        server.setExecutor(null);
        server.start();

//        Repository empRepo = new Repository();
//        Employee employee = new Employee();
//
//        employee.setName("Richard Nixon");
//        employee.setEmail("RNixon@email.com");
//        employee.setPassword("q93h-qEHVAW89P");
//        empRepo.save(employee);
    }
}
