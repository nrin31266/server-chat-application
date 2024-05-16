package com.raven.service;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.raven.model.Model_Client;
import com.raven.model.Model_Login;
import com.raven.model.Model_Message;
import com.raven.model.Model_Receive_Message;
import com.raven.model.Model_Register;
import com.raven.model.Model_Send_Message;
import com.raven.model.Model_User_Account;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

public class Service {

    private List<Model_Client> listClient;
    private static Service instance;
    private SocketIOServer server;
    private JTextArea textArea;
    private ServiceUser serviceUser;
    private final int PORT_NUMBER = 9999;

    public static Service getInstance(JTextArea textArea) {
        if (instance == null) {
            instance = new Service(textArea);
        }
        return instance;
    }

    private Service(JTextArea textArea) {
        this.textArea = textArea;
        serviceUser = new ServiceUser();
        listClient= new ArrayList<>();
    }

    public void startServer() {
        Configuration config = new Configuration();
        config.setPort(PORT_NUMBER);
        server = new SocketIOServer(config);
        server.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient sioc) {
                textArea.append("One client connected\n");
            }
        });
        server.addEventListener("register", Model_Register.class, new DataListener<Model_Register>() {
            @Override
            public void onData(SocketIOClient sioc, Model_Register t, AckRequest ar) throws Exception {
                Model_Message message = serviceUser.register(t);
                ar.sendAckData(message.isAction(), message.getMessage(), message.getData());
                if (message.isAction()) {
                    textArea.append("User has Register :" + t.getUserName() + " Pass :" + t.getPassword() + "\n");
                    server.getBroadcastOperations().sendEvent("list_user", (Model_User_Account) message.getData());
                    addClient(sioc,(Model_User_Account) message.getData());
                }
            }
        });
        server.addEventListener("list_user", Integer.class, new DataListener<Integer>() {
            @Override
            public void onData(SocketIOClient sioc, Integer userID, AckRequest ar) throws Exception {
                try {
                    List<Model_User_Account> list = serviceUser.getUser(userID);
                    sioc.sendEvent("list_user", list.toArray());
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
        });
        server.addEventListener("dangNhap", Model_Login.class, new DataListener<Model_Login>() {
            @Override
            public void onData(SocketIOClient sioc, Model_Login t, AckRequest ar){
                try {
                    // Gọi phương thức login của serviceUser để xác thực người dùng
                    Model_User_Account login = serviceUser.login(t);
                    // Kiểm tra kết quả đăng nhập
                    if (login != null) {
                        // Gửi phản hồi thành công cùng với thông tin tài khoản người dùng
                        ar.sendAckData(true, login);
                        addClient(sioc, login);
                        userConnent(login.getUserID());
                    } else {
                        // Gửi phản hồi thất bại
                        ar.sendAckData(false);
                        
                    }
                } catch (SQLException ex) {
                    System.err.println(ex);
                }
            }
        });
        server.addDisconnectListener(new DisconnectListener(){
            @Override
            public void onDisconnect(SocketIOClient sioc) {
                int userID= removeClient(sioc);
                if(userID!=0){
                    //Da xoa
                    userDisConnect(userID);
                }
            }
            
        });
        server.addEventListener("send_to_user", Model_Send_Message.class, new DataListener<Model_Send_Message>() {
            @Override
            public void onData(SocketIOClient sioc, Model_Send_Message t, AckRequest ar) throws Exception {
                System.err.println("Nhan dc tin nhan tu client");
                System.out.println(t.toString());
                sendToClient(t);
            }
        });
        server.start();
        textArea.append("Server has Start on port : " + PORT_NUMBER + "\n");
    }
    
    public void sendToClient(Model_Send_Message data){
        for(Model_Client c:listClient){
            if(c.getUser().getUserID()==data.getToUserID()){
                c.getClient().sendEvent("receive_ms", new Model_Receive_Message(data.getFromUserID(), data.getText()));
                break;
            }
        }
    }
    private void userConnent(int userID){
        server.getBroadcastOperations().sendEvent("user_status", userID, true);
    }
    private void userDisConnect(int userID){
        server.getBroadcastOperations().sendEvent("user_status", userID, false);
    }
    private void addClient(SocketIOClient client, Model_User_Account user){
        listClient.add(new Model_Client(client, user));
    }
    private int removeClient(SocketIOClient client){
        for (Model_Client d: listClient){
            if(d.getClient()==client){
                listClient.remove(d);
                return d.getUser().getUserID();
            }
        }
        return 0;
    }
    
    public List<Model_Client> getListClient() {
        return listClient;
    }
    
}
