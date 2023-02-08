package com.revature.controller;

import com.revature.service.TicketService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class ViewPersonalTicketsController extends Controller{
    private final TicketService ticketService = new TicketService();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String httpVerb= exchange.getRequestMethod();
        if(httpVerb.equals("GET")){
            String body = getRequestBodyString(exchange);
            sendResponse(exchange,200, ticketService.getPersonalTickets(body));
        }
    }
}
