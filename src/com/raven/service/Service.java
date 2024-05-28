package com.raven.service;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.raven.app.MessageType;
import com.raven.model.Model_Client;
import com.raven.model.Model_File;
import com.raven.model.Model_HistoryChat;
import com.raven.model.Model_Image_Update;
import com.raven.model.Model_Login;
import com.raven.model.Model_Message;
import com.raven.model.Model_Name_Update;
import com.raven.model.Model_Package_Sender;
import com.raven.model.Model_Profile;
import com.raven.model.Model_Profile_Update;
import com.raven.model.Model_Receive_File;
import com.raven.model.Model_Receive_Image;
import com.raven.model.Model_Receive_Message;
import com.raven.model.Model_Register;
import com.raven.model.Model_Reques_File;
import com.raven.model.Model_Send_Message;
import com.raven.model.Model_User_Account;
import com.raven.model.UserIDToJSON;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JTextArea;

public class Service {

    private static Service instance;
    private SocketIOServer server;
    private ServiceUser serviceUser;
    private ServiceFIle serviceFile;
    private List<Model_Client> listClient;
    private JTextArea textArea;
    private final int PORT_NUMBER = 9999;
    private Service_HistoryChat serviceHistoryChat;
    private ServiceProfile serviceProfile;
    private Map<Integer, StringBuilder> userImageChunks = new ConcurrentHashMap<>();

    public static Service getInstance(JTextArea textArea) {
        if (instance == null) {
            instance = new Service(textArea);
        }
        return instance;
    }

    private Service(JTextArea textArea) {
        this.textArea = textArea;
        serviceUser = new ServiceUser();
        serviceFile = new ServiceFIle();
        listClient = new ArrayList<>();
        serviceHistoryChat = new Service_HistoryChat();
        serviceProfile = new ServiceProfile();
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
        server.addEventListener("get_image_cover", UserIDToJSON.class, new DataListener<UserIDToJSON>() {
            @Override
            public void onData(SocketIOClient client, UserIDToJSON data, AckRequest ar) throws Exception {
                System.out.println("Received event to get anh bia from client");

                // Lấy chuỗi base64 từ dịch vụ hoặc database
                String base64String = serviceProfile.getImageCoverArt(data.getUserID());
                System.err.println(base64String);

                // Tạo danh sách các chunks từ chuỗi base64
                List<Model_Image_Update> chunkList = serviceProfile.createChunks(data.getUserID(), base64String);

                // Gửi từng chunk tới client
                for (Model_Image_Update chunk : chunkList) {
                    client.sendEvent("get_cover_chunk", chunk);
                }
            }
        });
        server.addEventListener("get_image_avt", UserIDToJSON.class, new DataListener<UserIDToJSON>() {
            @Override
            public void onData(SocketIOClient client, UserIDToJSON data, AckRequest ackRequest) throws Exception {
                System.out.println("Received event to get avatar from client");

                // Lấy chuỗi base64 từ dịch vụ hoặc database
                String base64String = serviceProfile.getImageAvt(data.getUserID());
                System.err.println(base64String);

                // Tạo danh sách các chunks từ chuỗi base64
                List<Model_Image_Update> chunkList = serviceProfile.createChunks(data.getUserID(), base64String);

                // Gửi từng chunk tới client
                for (Model_Image_Update chunk : chunkList) {
                    client.sendEvent("image_avt_chunk", chunk);
                }
            }
        });

        server.addEventListener("update_name_profile", Model_Name_Update.class, new DataListener<Model_Name_Update>() {
            @Override
            public void onData(SocketIOClient sioc, Model_Name_Update t, AckRequest ar) throws Exception {
                boolean b = serviceProfile.updateName(t);
                if (b) {
                    ar.sendAckData(true);
                } else {
                    ar.sendAckData(false);
                }
            }

        });
        server.addEventListener("update_avatar", Model_Image_Update.class, new DataListener<Model_Image_Update>() {
            @Override
            public void onData(SocketIOClient sioc, Model_Image_Update data, AckRequest ackRequest) throws Exception {
                System.out.println("Received update cover art request from userID: " + data.getUserID());
                System.out.println("Received image data chunk length: " + data.getImageData().length());

                int userID = data.getUserID();
                userImageChunks.putIfAbsent(userID, new StringBuilder());
                userImageChunks.get(userID).append(data.getImageData());

                if (data.isLastChunk()) {
                    System.out.println("Received the last chunk for userID: " + userID);
                    String fullImageBase64 = userImageChunks.remove(userID).toString();
                    byte[] fullImageData = Base64.getDecoder().decode(fullImageBase64);
                    // Xử lý ảnh đầy đủ ở đây, ví dụ như lưu vào file
                    serviceProfile.updateAvatar(userID, fullImageData);
                    ackRequest.sendAckData(true);
                    userImageChunks.remove(userID);
                } else {
                    ackRequest.sendAckData(true);
                }
            }

        });
        server.addEventListener("update_coverart", Model_Image_Update.class, new DataListener<Model_Image_Update>() {
            @Override
            public void onData(SocketIOClient sioc, Model_Image_Update data, AckRequest ackRequest) throws Exception {
                System.out.println("Received update cover art request from userID: " + data.getUserID());
                System.out.println("Received image data chunk length: " + data.getImageData().length());

                int userID = data.getUserID();
                userImageChunks.putIfAbsent(userID, new StringBuilder());
                userImageChunks.get(userID).append(data.getImageData());

                if (data.isLastChunk()) {
                    System.out.println("Received the last chunk for userID: " + userID);
                    String fullImageBase64 = userImageChunks.remove(userID).toString();
                    byte[] fullImageData = Base64.getDecoder().decode(fullImageBase64);
                    // Xử lý ảnh đầy đủ ở đây, ví dụ như lưu vào file
                    serviceProfile.updateCoverArt(userID, fullImageData);
                    ackRequest.sendAckData(true);
                    userImageChunks.remove(userID);
                } else {
                    ackRequest.sendAckData(true);
                }
            }
        });

        server.addEventListener(
                "update_profile_profile", Model_Profile_Update.class,
                new DataListener<Model_Profile_Update>() {
            @Override
            public void onData(SocketIOClient sioc, Model_Profile_Update t,
                    AckRequest ar) throws Exception {
                System.out.println(t.toString());
                boolean b = serviceProfile.updateProfile(t);
                if (b) {
                    ar.sendAckData(true);
                } else {
                    ar.sendAckData(false);
                }
            }
        });

        server.addEventListener(
                "get_info", Model_User_Account.class,
                new DataListener<Model_User_Account>() {
            @Override
            public void onData(SocketIOClient sioc, Model_User_Account t,
                    AckRequest ar) throws Exception {
                System.out.println("Lay thong tin tk");
                Model_Profile data = serviceProfile.getInfo(t);
                System.out.println(data.toString());
                ar.sendAckData(data);
            }
        });
        server.addEventListener(
                "history", Model_HistoryChat.class,
                new DataListener<Model_HistoryChat>() {
            @Override
            public void onData(SocketIOClient sioc, Model_HistoryChat t,
                    AckRequest ar) throws Exception {

                serviceHistoryChat.addHistoryChat(t);
            }
        });
        server.addEventListener(
                "register", Model_Register.class,
                new DataListener<Model_Register>() {
            @Override
            public void onData(SocketIOClient sioc, Model_Register t,
                    AckRequest ar) throws Exception {
                Model_Message message = serviceUser.register(t);
                ar.sendAckData(message.isAction(), message.getMessage(), message.getData());
                if (message.isAction()) {
                    textArea.append("User has Register :" + t.getUserName() + " Pass :" + t.getPassword() + "\n");
                    server.getBroadcastOperations().sendEvent("list_user", (Model_User_Account) message.getData());
                    addClient(sioc, (Model_User_Account) message.getData());
                }
            }
        });
        server.addEventListener(
                "login", Model_Login.class,
                new DataListener<Model_Login>() {
            @Override
            public void onData(SocketIOClient sioc, Model_Login t,
                    AckRequest ar) throws Exception {
                Model_User_Account login = serviceUser.login(t);
                if (login != null) {
                    ar.sendAckData(true, login);
                    addClient(sioc, login);
                    userConnect(login.getUserID());
                } else {
                    ar.sendAckData(false);
                }
            }
        });
        server.addEventListener("user_updated_image", Model_Image_Update.class, new DataListener<Model_Image_Update>() {
            @Override
            public void onData(SocketIOClient sioc, Model_Image_Update t, AckRequest ar) throws Exception {
                // Lấy danh sách tất cả các client đang kết nối
                Collection<SocketIOClient> clients = server.getAllClients();

                // Gửi sự kiện cho từng client khác client gửi yêu cầu
                for (SocketIOClient client : clients) {
                    if (!client.equals(sioc)) { // Loại bỏ client gửi yêu cầu
                        client.sendEvent("user_updated_image", t);
                    }
                }
            }
        });

        server.addEventListener(
                "list_user", Integer.class,
                new DataListener<Integer>() {
            @Override
            public void onData(SocketIOClient sioc, Integer userID,
                    AckRequest ar) throws Exception {
                try {
                    List<Model_User_Account> list = serviceUser.getUser(userID);
                    sioc.sendEvent("list_user", list.toArray());
                } catch (SQLException e) {
                    System.err.println(e);
                }
            }
        }
        );
        server.addEventListener(
                "send_to_user", Model_Send_Message.class,
                new DataListener<Model_Send_Message>() {
            @Override
            public void onData(SocketIOClient sioc, Model_Send_Message t,
                    AckRequest ar) throws Exception {
                sendToClient(t, ar);
            }
        }
        );
        server.addEventListener(
                "send_file", Model_Package_Sender.class,
                new DataListener<Model_Package_Sender>() {
            @Override
            public void onData(SocketIOClient sioc, Model_Package_Sender t,
                    AckRequest ar) throws Exception {
                try {
                    serviceFile.receiveFile(t);
                    if (t.getType() == 4) {
                        if (t.isFinish()) {
                            ar.sendAckData(true);
                            Model_Receive_Image dataImage = new Model_Receive_Image();
                            dataImage.setFileID(t.getFileID());
                            Model_Send_Message message = serviceFile.closeFile(dataImage);
                            //  Send to client 'message'
                            sendTempFileToClient(message, dataImage);

                        } else {
                            ar.sendAckData(true);
                        }
                    } else if (t.getType() == 3) {
                        if (t.isFinish()) {
                            ar.sendAckData(true);

                            Model_Receive_File dataFile = new Model_Receive_File();
                            dataFile.setFileID(t.getFileID());

                            dataFile.setFileName(t.getFileName());
                            dataFile.setFileExtension(t.getFileExtension());
                            dataFile.setFileSize(t.getFileSize() + "");

                            Model_Send_Message message = serviceFile.closeFile(dataFile);
                            //  Send to client 'message'

                            sendTempFileToClient(message, dataFile);

                        } else {
                            ar.sendAckData(true);
                        }
                    }

                } catch (IOException | SQLException e) {
                    ar.sendAckData(false);
                    e.printStackTrace();
                }
            }
        }
        );
        server.addDisconnectListener(
                new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient sioc
            ) {
                int userID = removeClient(sioc);
                if (userID != 0) {
                    //  removed
                    userDisconnect(userID);
                }
            }
        }
        );
        //"get_file": Được gửi khi máy khách yêu cầu thông tin về một tệp cụ thể bằng cách truyền fileID. 
        //Khi sự kiện này được kích hoạt, máy chủ sẽ gửi lại thông tin về fileID, 
        //bao gồm phần mở rộng của tệp và kích thước của tệp.
        server.addEventListener(
                "get_file", Integer.class,
                new DataListener<Integer>() {
            @Override
            public void onData(SocketIOClient sioc, Integer t,
                    AckRequest ar) throws Exception {
                Model_File file = serviceFile.initFile(t);
                long fileSize = serviceFile.getFileSize(t);
                ar.sendAckData(file.getFileExtension(), fileSize);
            }
        }
        );
        //"reques_file": Được gửi khi máy khách yêu cầu dữ liệu của một phần cụ thể của tệp. 
        //Khi sự kiện này được kích hoạt, máy chủ sẽ trả lại dữ liệu tương ứng với phần được yêu cầu, 
        //hoặc không có gì nếu dữ liệu không có sẵn hoặc có lỗi xảy ra.
        server.addEventListener(
                "reques_file", Model_Reques_File.class,
                new DataListener<Model_Reques_File>() {
            @Override
            public void onData(SocketIOClient sioc, Model_Reques_File t,
                    AckRequest ar) throws Exception {
                byte[] data = serviceFile.getFileData(t.getCurrentLength(), t.getFileID());
                if (data != null) {
                    ar.sendAckData(data);
                } else {
                    ar.sendAckData();
                }
            }
        }
        );

        server.start();

        textArea.append(
                "Server has Start on port : " + PORT_NUMBER + "\n");
    }

    private void userConnect(int userID) {
        server.getBroadcastOperations().sendEvent("user_status", userID, true);
    }

    private void userDisconnect(int userID) {
        server.getBroadcastOperations().sendEvent("user_status", userID, false);
    }

    private void addClient(SocketIOClient client, Model_User_Account user) {
        listClient.add(new Model_Client(client, user));
    }

    private void sendToClient(Model_Send_Message data, AckRequest ar) {
        if (data.getMessageType() == MessageType.IMAGE.getValue()) {
            try {
                Model_File file = serviceFile.addFileReceiver(data.getText());
                serviceFile.initFile(file, data);
                ar.sendAckData(file.getFileID(), MessageType.IMAGE.getValue());
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        } else if (data.getMessageType() == MessageType.FILE.getValue()) {
            try {
                Model_File file = serviceFile.addFileReceiver(data.getText());
                serviceFile.initFile(file, data);
                ar.sendAckData(file.getFileID(), MessageType.FILE.getValue());
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        } else {
            for (Model_Client c : listClient) {
                if (c.getUser().getUserID() == data.getToUserID()) {
                    c.getClient().sendEvent("receive_ms", new Model_Receive_Message(data.getMessageType(), data.getFromUserID(), data.getText(), null, null));
                    break;
                }
            }
        }
    }

    private void sendTempFileToClient(Model_Send_Message data, Model_Receive_Image dataImage) {
        for (Model_Client c : listClient) {
            if (c.getUser().getUserID() == data.getToUserID()) {
                c.getClient().sendEvent("receive_ms", new Model_Receive_Message(data.getMessageType(), data.getFromUserID(), data.getText(), dataImage, null));
                break;
            }
        }
    }

    private void sendTempFileToClient(Model_Send_Message data, Model_Receive_File dataFile) {
        for (Model_Client c : listClient) {
            if (c.getUser().getUserID() == data.getToUserID()) {
                c.getClient().sendEvent("receive_ms", new Model_Receive_Message(data.getMessageType(), data.getFromUserID(), data.getText(), null, dataFile));
                break;
            }
        }
    }

    public int removeClient(SocketIOClient client) {
        for (Model_Client d : listClient) {
            if (d.getClient() == client) {
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
