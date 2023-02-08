package com.revature.controller;

import com.revature.service.TicketService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class FinalizeTicketController extends Controller{
    private final TicketService ticketService = new TicketService();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String httpVerb= exchange.getRequestMethod();
        if(httpVerb.equals("PUT")){
            String body=getRequestBodyString(exchange);
            sendResponse(exchange,200, ticketService.finalizeTicket(body));
        }
    }
}
