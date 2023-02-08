package com.revature.service;

import com.revature.model.Employee;
import com.revature.model.Ticket;
import com.revature.repository.AuthRepository;
import com.revature.repository.TicketRepository;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class TicketService {
    private final ObjectMapper map = new ObjectMapper();

    public String finalizeTicket(String json) {
        JsonNode jsonNode;
        try{
            jsonNode=map.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to get all tickets";
        }
        JsonNode emailNode = jsonNode.get("email");
        JsonNode pwdNode = jsonNode.get("password");
        JsonNode ticketIDNode = jsonNode.get("ticketID");
        JsonNode newStatusNode = jsonNode.get("newStatus");
        if(emailNode==null||pwdNode==null||ticketIDNode==null||newStatusNode==null){
            return "Unable to change ticket, make sure "+
                "email,password,ticketID and newStatus are not blank";
        }
        String email = emailNode.asText();
        String passwd= pwdNode.asText();
        int ticketID = ticketIDNode.asInt();
        Ticket.Status newStatus;
        if(newStatusNode.asText().equalsIgnoreCase("approved")){
            newStatus= Ticket.Status.APPROVED;
        }else if(newStatusNode.asText().equalsIgnoreCase("denied")){
            newStatus=Ticket.Status.DENIED;
        }else{
            return "Not a valid status for ticket update";
        }
        AuthRepository authRepo = new AuthRepository();
        Employee manager = authRepo.getEmployeebyEmail(email);
        if (manager==null)return "Email is not associated with an employee";
        if(!manager.getEmail().equals(email)||!manager.getPassword().equals(passwd)) {
            return "Credential verification failed";
        }
        if (!manager.isManager()) return "Only managers are able to do this";

        TicketRepository ticketRepo = new TicketRepository();
        return ticketRepo.finalizeTicketbyID(ticketID,newStatus);
    }

    public String submitTicket(String json){
        TicketRepository tickRepo = new TicketRepository();
        JsonNode jsonNode;
        try {
            jsonNode= map.readTree(json);
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to register ticket; request contained bad data";
        }
        JsonNode emailNode = jsonNode.get("email");
        JsonNode pwdNode = jsonNode.get("password");
        JsonNode amntNode=jsonNode.get("amount");
        JsonNode descNode=jsonNode.get("description");
        if(emailNode==null||pwdNode==null||amntNode==null||descNode==null){
            return "Unable to create ticket, make sure email, password, amount and description are not blank";
        }
        String email= emailNode.asText();
        String password=pwdNode.asText();
        BigDecimal amount;
        try {
            amount = new BigDecimal(amntNode.asText());
        }catch (NumberFormatException exception){
            return "unable to parse amount this ticket was for";
        }
        String description = descNode.asText();
        AuthRepository authRepository = new AuthRepository();
        Employee employee = authRepository.getEmployeebyEmail(email);
        if(employee==null)return "That email is not associated with an employee";
        if(!Objects.equals(employee.getPassword(), password)) return "Credentials of "+email+" Failed";
        return tickRepo.createNewTicket(amount,description, employee.getEmpID());
    }

    public String getTicketsFiltered(String body){
        JsonNode jsonNode;
        try {
            jsonNode=map.readTree(body);
        }catch (IOException e){
            e.printStackTrace();
            return "Failed to get all tickets";
        }
        JsonNode emailNode=jsonNode.get("email");
        JsonNode passNode=jsonNode.get("password");
        JsonNode fromEmployeeNode=jsonNode.get("fromEmployee");
        JsonNode statusNode=jsonNode.get("status");
        if(emailNode==null||passNode==null){
            return "Formatting error, make sure email and password are not blank. fromEmployee can be left blank"+
                " or be the email of an employee. status can be blank, 'approved', 'denied', or 'pending'";
        }
        //manager credentials
        String email = emailNode.asText();
        String pwd = passNode.asText();

        //simplified way to check for null values
        String fromEmployee = fromEmployeeNode==null? null:fromEmployeeNode.asText();
        AuthRepository aRepo = new AuthRepository();
        int empID = aRepo.getEmployeeId(fromEmployee);

        Ticket.Status status;
        if(statusNode.asText().equalsIgnoreCase("approved")){
            status= Ticket.Status.APPROVED;
        }else if(statusNode.asText().equalsIgnoreCase("denied")){
            status= Ticket.Status.DENIED;
        }else if(statusNode.asText().equalsIgnoreCase("pending")){
            status= Ticket.Status.PENDING;
        }else{
            status=null;
        }

        //validate credentials are right and are from a manager
        Employee manager = aRepo.getEmployeebyEmail(email);
        if(manager==null)return "email address not associated with employee";
        if(!Objects.equals(manager.getEmail(),email)||!Objects.equals(manager.getPassword(),pwd)){
            return "Credential verification failed";
        }
        if(!manager.isManager()) return "Only managers can perform this action";

        //makes list of tickets
        TicketRepository ticketRepo = new TicketRepository();
        List<Ticket> tickets = ticketRepo.getFilterTickets(empID,status);
        String jsonString;
        try {
             jsonString = map.writeValueAsString(tickets);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return jsonString;
    }

    public String getPersonalTickets(String body) {
        JsonNode jsonNode;
        try {
            //maps http body into json nodes
            jsonNode=map.readTree(body);
        }catch (IOException e){
            e.printStackTrace();
            return "Failed to get all tickets";
        }
        //extract useful bits from nodes
        JsonNode emailNode=jsonNode.get("email");
        JsonNode passNode=jsonNode.get("password");
        JsonNode statusNode=jsonNode.get("status");
        if(emailNode==null||passNode==null){
            return "Formatting error, make sure email and password are not blank."+
                " status can be blank, 'approved', 'denied', or 'pending'";
        }
        //convert node data into usable text
        String email = emailNode.asText();
        String pwd = passNode.asText();
        Ticket.Status status;
        if(statusNode.asText().equalsIgnoreCase("approved")){
            status= Ticket.Status.APPROVED;
        }else if(statusNode.asText().equalsIgnoreCase("denied")){
            status= Ticket.Status.DENIED;
        }else if(statusNode.asText().equalsIgnoreCase("pending")){
            status= Ticket.Status.PENDING;
        }else{
            status=null;
        }
        AuthRepository authRepo = new AuthRepository();
        Employee employee = authRepo.getEmployeebyEmail(email);
        int empID = authRepo.getEmployeeId(email);
        if(employee==null)return "Email is not associated with an employee";
        if(!Objects.equals(employee.getEmail(), email)||!Objects.equals(employee.getPassword(),pwd)){
            return "Credentials failed, password incorrect";
        }
        TicketRepository ticketRepo = new TicketRepository();
        List<Ticket> tickets = ticketRepo.getFilterTickets(empID,status);
        String jsonString;
        try {
            jsonString = map.writeValueAsString(tickets);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return jsonString;
    }
}
