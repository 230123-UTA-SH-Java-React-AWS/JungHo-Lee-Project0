package com.revature.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
    //static ensures only one connection to DB the whole time
    private static Connection con;
    private ConnectionUtil(){
        con = null;
    }
    //method gives connection to DB or will give existing connection
    public static Connection getConnection(){
        //determine if already connected and if so, give current connection
        try {
            //if there is connection and it isn't closed
            if(con !=null && !con.isClosed()){
                return con;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //don't hardcode these variables, or they'll be pushed into repo
        String url, user, pass;

        url = System.getenv("url");
        user = System.getenv("user");
        pass = System.getenv("password");

        try {
            con = DriverManager.getConnection(url,user,pass);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Likely a password error");
        }
        return con;
    }
}
