package com.raven.service;

import com.raven.app.MessageType;
import com.raven.connection.DatabaseConnection;
import com.raven.model.Model_File;
import com.raven.model.Model_File_Receiver;
import com.raven.model.Model_File_Sender;
import com.raven.model.Model_Package_Sender;
import com.raven.model.Model_Receive_File;
import com.raven.model.Model_Receive_Image;
import com.raven.model.Model_Send_Message;
import com.raven.swing.blurHash.BlurHash;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class ServiceFIle {

    public ServiceFIle() {
        this.con = DatabaseConnection.getInstance().getConnection();
        this.fileReceivers = new HashMap<>();
        this.fileSenders= new HashMap<>();
    }

    public Model_File addFileReceiver(String fileExtension) throws SQLException {
        Model_File data;
        PreparedStatement p = con.prepareStatement(INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
        p.setString(1, fileExtension);
        p.execute();
        ResultSet r = p.getGeneratedKeys();
        r.next();
        int fileID = r.getInt(1);
        data = new Model_File(fileID, fileExtension);
        r.close();
        p.close();
        return data;
    }

    public void updateBlurHashDone(int fileID, String blurhash) throws SQLException {
        PreparedStatement p = con.prepareStatement(UPDATE_BLUR_HASH_DONE);
        p.setString(1, blurhash);
        p.setInt(2, fileID);
        p.execute();
        p.close();
    }

    public void updateDone(int fileID)  {
        try {
            PreparedStatement p = con.prepareStatement(UPDATE_DONE);
            p.setInt(1, fileID);
            p.execute();
            p.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
    public void updateDoneTBAll(String fileName , int fileID)  {
        try {
            PreparedStatement p = con.prepareStatement(UPDATE_DONE_TBALL);
            p.setString(1, fileName);
            p.setInt(2, fileID);
            p.execute();
            p.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void initFile(Model_File file, Model_Send_Message message) throws IOException {
        fileReceivers.put(file.getFileID(), new Model_File_Receiver(message, toFileObject(file)));
    }
    public Model_File getFile(int FileID) throws IOException, SQLException{
        PreparedStatement p= con.prepareStatement(GET_FILE_EXTENSION);
        p.setInt(1, FileID);
        ResultSet r=p.executeQuery();
        r.next();
        String fileExtension= r.getString(1);
        Model_File data= new Model_File(FileID, fileExtension);
        r.close();
        p.close();
        return data;
    }
    public synchronized Model_File initFile(int FileID) throws IOException, SQLException{
        Model_File file;
        if(!fileSenders.containsKey(FileID)){
            file=getFile(FileID);
            fileSenders.put(FileID, new Model_File_Sender(file, new File(PATH_FILE+FileID+file.getFileExtension())));
            
            
        }else{
            file=fileSenders.get(FileID).getData();
        }
        return file;
    }
    
    public long getFileSize(int fileID){
        return fileSenders.get(fileID).getFileSize();
    }
    
    public byte[] getFileData(long currentLength, int fileID) throws IOException, SQLException{
        initFile(fileID);
        return fileSenders.get(fileID).read(currentLength);
    }

    public void receiveFile(Model_Package_Sender dataPackage) throws IOException {
        if (!dataPackage.isFinish()) {
            fileReceivers.get(dataPackage.getFileID()).writeFile(dataPackage.getData());
        } else {
            fileReceivers.get(dataPackage.getFileID()).close();
        }
    }

    public Model_Send_Message closeFile(Model_Receive_Image dataImage) throws IOException, SQLException {
        Model_File_Receiver file = fileReceivers.get(dataImage.getFileID());
        if (file.getMessage().getMessageType() == MessageType.IMAGE.getValue()) {
            //  Image file
            //  So create blurhash image string
            file.getMessage().setText("");
            String blurhash = convertFileToBlurHash(file.getFile(), dataImage);
            updateBlurHashDone(dataImage.getFileID(), blurhash);
            updateDoneTBAll("image",dataImage.getFileID());
            
        } else {
            updateDone(dataImage.getFileID());
            updateDoneTBAll("image",dataImage.getFileID());
        }
        fileReceivers.remove(dataImage.getFileID());
        //  Get message to send to target client when file receive finish
        return file.getMessage();
    }
    public Model_Send_Message closeFile(Model_Receive_File dataFile) throws IOException, SQLException {
        Model_File_Receiver file = fileReceivers.get(dataFile.getFileID());
        if (file.getMessage().getMessageType() == MessageType.FILE.getValue()) {
            //  Image file
            //  So create blurhash image string
            file.getMessage().setText("");
            updateDoneTBAll(dataFile.getFileName(),dataFile.getFileID());
            
        } else {
            updateDone(dataFile.getFileID());
            updateDoneTBAll(dataFile.getFileName(),dataFile.getFileID());
        }
        fileReceivers.remove(dataFile.getFileID());
        //  Get message to send to target client when file receive finish
        return file.getMessage();
    }

    private String convertFileToBlurHash(File file, Model_Receive_Image dataImage) throws IOException {
        BufferedImage img = ImageIO.read(file);
        Dimension size = getAutoSize(new Dimension(img.getWidth(), img.getHeight()), new Dimension(200, 200));
        //  Convert image to small size
        BufferedImage newImage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newImage.createGraphics();
        g2.drawImage(img, 0, 0, size.width, size.height, null);
        String blurhash = BlurHash.encode(newImage);
        dataImage.setWidth(size.width);
        dataImage.setHeight(size.height);
        dataImage.setImage(blurhash);
        dataImage.setFileExtension(getExtensions(file.getName()));
        dataImage.setFileName(file.getName());
        return blurhash;
    }

    private Dimension getAutoSize(Dimension fromSize, Dimension toSize) {
        int w = toSize.width;
        int h = toSize.height;
        int iw = fromSize.width;
        int ih = fromSize.height;
        double xScale = (double) w / iw;
        double yScale = (double) h / ih;
        double scale = Math.min(xScale, yScale);
        int width = (int) (scale * iw);
        int height = (int) (scale * ih);
        return new Dimension(width, height);
    }
    private String getExtensions(String fileName) { //Trích xuất phần mở rộng của tên tệp.
        return fileName.substring(fileName.lastIndexOf("."), fileName.length());
    }

    private File toFileObject(Model_File file) {
        return new File(PATH_FILE + file.getFileID() + file.getFileExtension());
    }

    //  SQL
    private final String PATH_FILE = "server_data/";
    private final String INSERT = "insert into files_all (FileExtension) values (?)";
    private final String UPDATE_BLUR_HASH_DONE = "update files set BlurHash=?, `Status`='1' where FileID=? limit 1";
    private final String UPDATE_DONE = "update files set `Status`='1' where FileID=? limit 1";
    private final String UPDATE_DONE_TBALL = "update files_all set FileName=?,`Status`='1' where FileID=? limit 1";
    private final String GET_FILE_EXTENSION="select FileExtension from files where FileID=? limit 1";
    //  Instance
    private final Connection con;
    private final Map<Integer, Model_File_Sender> fileSenders;
    private final Map<Integer, Model_File_Receiver> fileReceivers;
    
    
}
