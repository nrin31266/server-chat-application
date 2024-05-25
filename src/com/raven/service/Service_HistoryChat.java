
package com.raven.service;

import com.raven.connection.DatabaseConnection;
import com.raven.model.Model_HistoryChat;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Service_HistoryChat {
    public Service_HistoryChat() {
        this.con = DatabaseConnection.getInstance().getConnection();
    }
    
public void addHistoryChat(Model_HistoryChat data) {
        PreparedStatement p = null;
        try {
            p = con.prepareStatement(INSERT);
            p.setInt(1, data.getFromUser());
            p.setInt(2, data.getToUser());
            p.setInt(3, data.getType());
            
            p.setString(4, data.getTxt());
            p.setString(5, data.getSenderFilePath());
            p.setString(6, data.getReceiverFilePath());
            
            p.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Service_HistoryChat.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Đảm bảo đóng PreparedStatement để tránh rò rỉ tài nguyên
            if (p != null) {
                try {
                    p.close();
                } catch (SQLException e) {
                    Logger.getLogger(Service_HistoryChat.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    } 
    
    //Sql
    private final String INSERT="insert into `historychat` (SenderID,ReceiverID,Type,Message,FilePathSender,FilePathReceiver) values(?,?,?,?,?,?)";
    
    private final Connection con;
    
    
}
