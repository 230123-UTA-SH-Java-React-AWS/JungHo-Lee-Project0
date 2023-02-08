package com.revature.model;

import java.math.BigDecimal;

public class Ticket {
    public enum Status{PENDING, APPROVED, DENIED}
    public final Status status;
    public final  BigDecimal amount;
    public final String description;
    private final int employeeID;
    public int getEmployeeID(){
        return employeeID;
    }
    public Status getStatus(){
        return status;
    }
    public Ticket(Status status, BigDecimal amount, String description, int employeeID) {
        this.status=status;
        this.amount=amount;
        this.description=description;
        this.employeeID=employeeID;
    }
    @Override
    public String toString(){
        return "Ticket{" +
            "status=" + status +
            ", amount=" + amount +
            ", description='" + description + '\'' +
            ", employeeId=" + employeeID +
            '}';
    }
}
