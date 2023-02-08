package com.revature.controller;

import com.revature.service.TicketService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class SubmitTicketController extends Controller{
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        TicketService tServ = new TicketService();
        String httpVerb = exchange.getRequestMethod();
        if(httpVerb.equals("POST")){
            String body = getRequestBodyString(exchange);
            sendResponse(exchange,200, tServ.submitTicket(body));
        }else{
            sendResponse(exchange,403,"Prohibited Action");
        }
    }
}
