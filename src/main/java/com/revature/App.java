package com.revature;

import com.revature.controller.*;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public final class App {
    private App() {
    }
    public static void main(String[] args) throws IOException {
        System.out.println("Starting backend server");
        HttpServer server = HttpServer.create(new InetSocketAddress(8000),0);
        server.createContext("/login",new LoginController());
        server.createContext("/register", new RegisterController());
        server.createContext("/ticketsub", new SubmitTicketController());
        server.createContext("/ticketchange", new FinalizeTicketController());
        server.createContext("/ticketviewall", new ViewAllTicketsController());
        server.createContext("/ticketviewown", new ViewPersonalTicketsController());

        server.setExecutor(null);
        server.start();
    }
}
