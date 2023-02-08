package com.revature.repository;

import com.revature.model.Ticket;
import com.revature.utils.ConnectionUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketRepository {
    public String createNewTicket(BigDecimal amount, String description, int empID){
        String sql = "insert into tickets(tickstatus, tickamount, tickdesc, tickempid) values(?,?,?,?)";
        try(Connection con = ConnectionUtil.getConnection()){
            PreparedStatement prst = con.prepareStatement(sql);

            prst.setString(1,Ticket.Status.PENDING.toString());
            prst.setBigDecimal(2,amount);
            prst.setString(3,description);
            prst.setInt(4,empID);


            prst.execute();
        }catch (SQLException e){
            e.printStackTrace();
            return "Ticket couldn't be submitted";
        }
        return "Ticket successfully submitted";
    }

    public String finalizeTicketbyID(int ticketID, Ticket.Status newStatus) {
        if(newStatus==null)return "Invalid ticket status";
        if(newStatus==Ticket.Status.PENDING){
            return "Tickets cannot be made pending after finalization";
        }
        Ticket storedTicket=getTicketByID(ticketID);
        if(storedTicket==null)return "Ticket does not exist";
        if(storedTicket.status!= Ticket.Status.PENDING)return "Ticket has already been finalized";

        String sql = "update tickets set tickstatus=? where tickid=?";
        try(Connection con = ConnectionUtil.getConnection()){
            PreparedStatement prst = con.prepareStatement(sql);
            prst.setString(1,newStatus.toString());
            prst.setInt(2,ticketID);
            prst.execute();
        }catch (SQLException e){
            e.printStackTrace();
            return "Error has (likely) occurred in database";
        }
        return "Ticket update successful";
    }

    public Ticket getTicketByID(int ticketID) {
        List<Ticket> ticketList = new ArrayList<>();
        Ticket ticket = null;
        String sql = "select * from tickets where tickid = ?";
        try(Connection con = ConnectionUtil.getConnection()){
            PreparedStatement prst = con.prepareStatement(sql);
            prst.setInt(1,ticketID);
            ResultSet rs = prst.executeQuery();
            Ticket.Status status;
            while(rs.next()){
                String statusResult = rs.getString(2);
                if(statusResult.equalsIgnoreCase("pending")){
                    status= Ticket.Status.PENDING;
                }else if(statusResult.equalsIgnoreCase("approved")){
                    status= Ticket.Status.APPROVED;
                }else if(statusResult.equalsIgnoreCase("denied")){
                    status= Ticket.Status.DENIED;
                }else{
                    throw new IllegalStateException("Bad data in ticket table");
                }
                Ticket tkt = new Ticket(status,new BigDecimal(String.valueOf(rs.getBigDecimal(3))),
                    rs.getString(4),rs.getInt(5));
                ticketList.add(tkt);
            }
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        if(ticketList.size()>0) ticket=ticketList.get(0);
        return ticket;
    }

    public ArrayList<Ticket> getFilterTickets(int filterEmpID, Ticket.Status filterStatus){
        ArrayList<Ticket> tickets = new ArrayList<>();
        String sql ="select * from tickets";
        try(Connection con = ConnectionUtil.getConnection()){
            PreparedStatement prst = con.prepareStatement(sql);
            ResultSet rs = prst.executeQuery();
            Ticket.Status status;
            while (rs.next()){
                String statusResult = rs.getString(2);
                if(statusResult.equalsIgnoreCase("pending")){
                    status= Ticket.Status.PENDING;
                }else if(statusResult.equalsIgnoreCase("approved")){
                    status= Ticket.Status.APPROVED;
                }else if(statusResult.equalsIgnoreCase("denied")){
                    status= Ticket.Status.DENIED;
                }else{
                    throw new IllegalStateException("Bad data in ticket table");
                }
                Ticket tkt = new Ticket(status,new BigDecimal(String.valueOf(rs.getBigDecimal(3))),
                    rs.getString(4),rs.getInt(1));
                tickets.add(tkt);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for(int i = tickets.size()-1;i>=0;i--){
            if(filterEmpID != -1){
                if(tickets.get(i).getEmployeeID()!=filterEmpID){
                    tickets.remove(i);
                    continue;
                }
            }
            if(filterStatus!=null){
                if(tickets.get(i).status!=filterStatus){
                    tickets.remove(i);
                }
            }
        }
        return tickets;
    }
}
