package com.raven.service;

import com.raven.connection.DatabaseConnection;
import com.raven.model.Model_HistoryChat;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
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
            p.setInt(7, data.getFileID());
            p.setString(8, data.getFileName());
            p.setString(9, data.getFileSize());
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

    public List<Model_HistoryChat> getChatHistory(int senderID, int receiverID) {
        List<Model_HistoryChat> list = new ArrayList<>();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        try {
            PreparedStatement p = con.prepareStatement(SELECT);
            p.setInt(1, senderID);
            p.setInt(2, receiverID);
            p.setInt(3, receiverID);
            p.setInt(4, senderID);
            ResultSet r = p.executeQuery();
            while (r.next()) {
                int fromUser = r.getInt("SenderID");
                int toUser = r.getInt("ReceiverID");
                int type = r.getInt("Type");
                String txt = r.getString("Message");
                String senderFilePath = r.getString("FilePathSender");
                String receiverFilePath = r.getString("FilePathReceiver");
                int fileID=r.getInt("FileID");
                String fileName=r.getString("FileName");
                String fileSize=r.getString("FileSize");
                Timestamp timestamp = r.getTimestamp("Timestamp");

                // Convert Timestamp to String with only the time part
                String time = timeFormat.format(timestamp);
                
                Model_HistoryChat chat = new Model_HistoryChat(fromUser, toUser, type, txt, senderFilePath, receiverFilePath, fileID, fileName, fileSize);
                chat.setTime(time);
                list.add(chat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    //Sql
    private final String INSERT = "insert into `historychat` (SenderID,ReceiverID,Type,Message,FilePathSender,FilePathReceiver, FileID, FileName, FileSize) values(?,?,?,?,?,?,?,?,?)";

    private final String SELECT = "SELECT * FROM chat_application.historychat \n"
            + "WHERE (SenderId = ? AND ReceiverID = ?)\n"
            + "   OR (SenderId = ? AND ReceiverID = ?)\n"
            + "ORDER BY Timestamp desc\n"
            + "LIMIT 20;";

    private final Connection con;

}
