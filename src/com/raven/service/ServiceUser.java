package com.raven.service;

import com.raven.connection.DatabaseConnection;
import com.raven.model.Model_Message;
import com.raven.model.Model_Register;
import com.raven.model.Model_User_Account;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceUser {

    public ServiceUser() {
        this.con = DatabaseConnection.getInstance().getConnection();
    }

    public Model_Message register(Model_Register data) {
        //  Check user exit
        Model_Message message = new Model_Message();
        try {
            PreparedStatement p = con.prepareStatement(CHECK_USER);
            p.setString(1, data.getUserName());
            ResultSet r = p.executeQuery();
            if (r.first()) {
                message.setAction(false);
                message.setMessage("User Already Exit");
            } else {
                message.setAction(true);
            }
            r.close();
            p.close();
            if (message.isAction()) {
                //  Insert User Register
                con.setAutoCommit(false);
                p = con.prepareStatement(INSERT_USER, PreparedStatement.RETURN_GENERATED_KEYS);
                p.setString(1, data.getUserName());
                p.setString(2, data.getPassword());
                p.execute();
                r= p.getGeneratedKeys();
                r.first();
                int UserID=r.getInt(1);
                r.close();
                p.close();      
                //Create user account
                p = con.prepareStatement(INSERT_USER_ACCOUNT);
                p.setInt(1, UserID);
                p.setString(2, data.getUserName());
                p.execute();
                con.commit();
                con.setAutoCommit(true);
                p.close();
                
                message.setAction(true);
                message.setMessage("Ok");
                message.setData(new Model_User_Account(UserID, data.getUserName(),"", "", true));
            }
        } catch (SQLException e) {
            message.setAction(false);
            message.setMessage("Server Error");
            try {
                if(con.getAutoCommit()==false){
                    con.rollback();
                    con.setAutoCommit(true);
                }
            } catch (Exception e1) {
                
            }
        }
        return message;
    }

    //  SQL
    private final String INSERT_USER = "insert into user (UserName, `Password`) values (?,?)";
    private final String CHECK_USER = "select UserID from user where UserName =? limit 1";
    private final String INSERT_USER_ACCOUNT="insert into user_account (UserID, UserName) value (?, ?)";
    //  Instance
    private final Connection con;
}
