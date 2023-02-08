package com.revature.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public abstract class Controller implements HttpHandler {
    protected void sendResponse(HttpExchange exchange, int statCode, String toSend) throws IOException {
        exchange.sendResponseHeaders(statCode, toSend.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(toSend.getBytes());
        os.close();
    }

    protected String getRequestBodyString(HttpExchange exchange) {
        InputStream is = exchange.getRequestBody();
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader(is, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return textBuilder.toString();
    }

    /*private final Service empService = new Service();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String verb = exchange.getRequestMethod();
        switch (verb){
            case"POST":
                postRequest(exchange);
                break;
            case"GET":
                getRequest(exchange);
                break;
            default:
                break;
        }
    }

    private void postRequest(HttpExchange exchange)throws IOException{
        InputStream is = exchange.getRequestBody();
        StringBuilder textBuilder = new StringBuilder();
        try(Reader reader = new BufferedReader(new InputStreamReader(is, Charset.forName(StandardCharsets.UTF_8.name())))){
            int c = 0;
            while((c=reader.read())!= -1){
                textBuilder.append((char)c);
            }
        }
        System.out.println(textBuilder);
        exchange.sendResponseHeaders(200,textBuilder.toString().getBytes().length);

        //call service layer
        empService.saveEmployee(textBuilder.toString());

        OutputStream os = exchange.getResponseBody();
        os.write(textBuilder.toString().getBytes());
        os.close();
    }

    private void getRequest(HttpExchange exchange)throws IOException{
        String jsonCurrentList = empService.getAllEmployees();

        exchange.sendResponseHeaders(200, jsonCurrentList.getBytes().length);

        OutputStream os = exchange.getResponseBody();
        os.write(jsonCurrentList.getBytes());
        os.close();

    }*/
}
