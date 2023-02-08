package com.revature.model;

public class Employee {
    private int empID;
    private String name;
    private String email;
    private String password;
    private boolean isManager = false;

    //toString override allows terminal to display what is retrieved from database
    @Override
    public String toString(){
        return "Employee [name="+name+", email="+email+", password="+password+
            ", manager? "+isManager+"]";
    }

    public void setEmpID(int id){this.empID=id;}
    public int getEmpID(){return empID;}
    public void setName(String name){this.name=name;}
    public String getName(){return name;}
    public void setEmail(String email){this.email = email;}
    public String getEmail(){return email;}
    public void setPassword(String password){this.password=password;}
    public String getPassword(){return password;}

    public boolean isManager() {
        return isManager;
    }
    public void setManager(boolean manager) {
        this.isManager = manager;
    }
}
