package com.revature.controller;

import com.revature.service.AuthService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class LoginController extends Controller{
    private final AuthService aServ = new AuthService();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String httpVerb = exchange.getRequestMethod();
        if(httpVerb.equals("POST")){
            String body = getRequestBodyString(exchange);
            sendResponse(exchange,200, aServ.login(body));
        }else{
            sendResponse(exchange,403,"Prohibited Action");
        }
    }
}
