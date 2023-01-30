package com.revature.controller;

import com.revature.service.Service;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Controller implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String verb = exchange.getRequestMethod();
        switch (verb){
            case"POST":
                postRequest(exchange);
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
        System.out.println(textBuilder.toString());
        exchange.sendResponseHeaders(200,textBuilder.toString().getBytes().length);

        //call service layer
        Service empService = new Service();
        empService.saveEmployee(textBuilder.toString());

        OutputStream os = exchange.getResponseBody();
        os.write(textBuilder.toString().getBytes());
        os.close();
    }
}
