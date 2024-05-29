package com.raven.service;

import com.raven.connection.DatabaseConnection;
import com.raven.model.Model_Image_Update;
import com.raven.model.Model_Name_Update;
import com.raven.model.Model_Profile;
import com.raven.model.Model_Profile_Update;
import com.raven.model.Model_User_Account;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.imageio.ImageIO;
import net.coobird.thumbnailator.Thumbnails;

public class ServiceProfile {

    public ServiceProfile() {
        this.con = DatabaseConnection.getInstance().getConnection();
    }

    public String processImage(byte[] imageBytes) throws IOException {
        // Đọc ảnh từ mảng byte
        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
        BufferedImage originalImage = ImageIO.read(inputStream);

        // Thay đổi kích thước và nén ảnh
        BufferedImage resizedImage = Thumbnails.of(originalImage)
                .size(70, 70) // Thay đổi kích thước
                .outputQuality(0.7) // Chất lượng nén
                .asBufferedImage();

        // Ghi ảnh ra ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "jpg", outputStream);

        // Chuyển đổi ByteArrayOutputStream thành chuỗi Base64
        byte[] compressedImageBytes = outputStream.toByteArray();
        String base64Image = Base64.getEncoder().encodeToString(compressedImageBytes);

        return base64Image;
    }

    public String getImageAvt(int id) {
        try {
            PreparedStatement p = con.prepareStatement(SELECT_IMAGE);
            p.setInt(1, id);
            ResultSet r = p.executeQuery();
            if (r.next()) {
                byte[] imageBytes = r.getBytes("Image");
                if (imageBytes != null) {
                    String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
                    return imageBase64;
                }
            }
            p.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getImageCoverArt(int id) {
        try {
            PreparedStatement p = con.prepareStatement(SELECT_COVER_ART);
            p.setInt(1, id);
            ResultSet r = p.executeQuery();
            if (r.next()) {
                byte[] imageBytes = r.getBytes("CoverArt");
                if (imageBytes != null) {
                    String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
                    return imageBase64;
                }
            }
            p.close();
            p.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean updateProfile(Model_Profile_Update data) throws ParseException {
        if (data.getUserName() != null && !data.getUserName().isEmpty()) {
            try {
                PreparedStatement p = con.prepareStatement(UPDATE_USERNAME_1);
                p.setString(1, data.getUserName());
                p.setInt(2, data.getUserID());
                p.executeUpdate();
                p.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            try {
                PreparedStatement p = con.prepareStatement(UPDATE_USERNAME_2);
                p.setString(1, data.getUserName());
                p.setInt(2, data.getUserID());
                p.executeUpdate();
                p.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }

        }
        if (data.getGender() != null && !data.getGender().isEmpty()) {
            try {
                PreparedStatement p = con.prepareStatement(UPDATE_GENDER);
                p.setString(1, data.getGender());
                p.setInt(2, data.getUserID());
                p.executeUpdate();
                p.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        if (data.getPhoneNumber() != null && !data.getPhoneNumber().isEmpty()) {
            try {
                PreparedStatement p = con.prepareStatement(UPDATE_PHONE);
                p.setString(1, data.getPhoneNumber());
                p.setInt(2, data.getUserID());
                p.executeUpdate();
                p.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        if (data.getDate() != null && !data.getDate().isEmpty()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate = dateFormat.parse(data.getDate());
            java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());
            //
            try {
                PreparedStatement p = con.prepareStatement(UPDATE_DATE);
                p.setDate(1, sqlDate);
                p.setInt(2, data.getUserID());
                p.executeUpdate();
                p.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        if (data.getEmail() != null && !data.getEmail().isEmpty()) {

            try {
                PreparedStatement p = con.prepareStatement(UPDATE_EMAIL);
                p.setString(1, data.getEmail());
                p.setInt(2, data.getUserID());
                p.executeUpdate();
                p.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        if (data.getAddress() != null && !data.getAddress().isEmpty()) {
            try {
                PreparedStatement p = con.prepareStatement(UPDATE_ADDRESS);
                p.setString(1, data.getAddress());
                p.setInt(2, data.getUserID());
                p.executeUpdate();
                p.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public boolean updateCoverArt(int userID, byte[] data) {
        byte[] imageBytes;
        try {
            PreparedStatement p = con.prepareStatement(UPDATE_COVERART);
            p.setBytes(1, data);
            p.setInt(2, userID);
            p.executeUpdate();
            p.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateName(Model_Name_Update data) {

        try {
            PreparedStatement p = con.prepareStatement(UPDATE_NAME);

            p.setString(1, data.getName());
            p.setInt(2, data.getUserID());
            p.executeUpdate();
            p.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateAvatar(int userID, byte[] data) {
        try {
            PreparedStatement p = con.prepareStatement(UPDATE_IMAGE);
            p.setBytes(1, data);
            p.setInt(2, userID);

            p.executeUpdate();
            p.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Model_Profile getInfo(Model_User_Account d) {
        Model_Profile data = null;
        PreparedStatement p = null;
        ResultSet r = null;

        try {
            p = con.prepareStatement(SELECT_INFO);
            p.setInt(1, d.getUserID());
            r = p.executeQuery();

            if (r.next()) {
                data = new Model_Profile();
                data.setUserID(d.getUserID());
                data.setUserName(r.getString("UserName"));
                data.setGender(r.getString("Gender"));
                // Encode image to Base64 string if not null
//                byte[] imageBytes = r.getBytes("Image");
//                if (imageBytes != null) {
//                    String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
//                    data.setImage(imageBase64);
//              }
                data.setImage("");
                data.setImageString("ImageString"); // Consider setting a meaningful value
                // Parse and set status
                data.setStatus(r.getString("Status").equals("1"));
                data.setName(r.getString("Name"));
                data.setPhoneNumber(r.getString("PhoneNumber"));
                try {
                    java.sql.Date sqlDate = r.getDate("Date");
                    if (sqlDate != null) {
                        data.setDate(sqlDate.toString());
                    } else {
                        data.setDate(""); // Gán chuỗi trống nếu ngày sinh là NULL
                    }
                } catch (Exception e) {
                    data.setDate(""); // Gán chuỗi trống nếu có ngoại lệ xảy ra
                }
                data.setEmail(r.getString("Email"));
                // Encode cover art to Base64 string if not null
//                byte[] coverArtBytes = r.getBytes("CoverArt");
//                if (coverArtBytes != null) {
//                    String coverArtBase64 = Base64.getEncoder().encodeToString(coverArtBytes);
//                    data.setCoverArt(coverArtBase64);
//                }
                data.setCoverArt("");
                data.setAddress(r.getString("Address"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources to avoid potential memory leaks
            try {
                if (r != null) {
                    r.close();
                }
                if (p != null) {
                    p.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    public List<Model_Image_Update> createChunks(int userID, String imageData) {
        int chunkSize = 10000; // Kích thước mỗi chunk
        List<Model_Image_Update> chunks = new ArrayList<>();
//        chunks.add(new Model_Image_Update(userID, "", false));
        for (int i = 0; i < imageData.length(); i += chunkSize) {
            String chunk = imageData.substring(i, Math.min(imageData.length(), i + chunkSize));
            boolean isLastChunk = (i + chunkSize) >= imageData.length();
            chunks.add(new Model_Image_Update(userID, chunk, isLastChunk));
        }
        return chunks;
    }

//Sql
    private final String SELECT_INFO
            = "select * from `user_account` where UserID=?";
    private final String UPDATE_NAME
            = "update user_account set Name=? where UserID=?";
    private final String UPDATE_IMAGE
            = "update user_account set Image=? where UserID=?";
    private final String UPDATE_COVERART
            = "update user_account set CoverArt=? where UserID=?";
    private final String UPDATE_USERNAME_1
            = "update user_account set UserName=? where UserID=?";
    private final String UPDATE_USERNAME_2
            = "update user set UserName=? where UserID=?";
    private final String UPDATE_GENDER
            = "update user_account set Gender=? where UserID=?";
    private final String UPDATE_PHONE
            = "update user_account set PhoneNumber=? where UserID=?";
    private final String UPDATE_DATE
            = "update user_account set Date=? where UserID=?";
    private final String UPDATE_EMAIL
            = "update user_account set Email=? where UserID=?";
    private final String UPDATE_ADDRESS
            = "update user_account set Address=? where UserID=?";
    private final String SELECT_IMAGE
            = "select (Image) from `user_account` where UserID=?";
    private final String SELECT_COVER_ART
            = "select (CoverArt) from `user_account` where UserID=?";

    private final Connection con;
}
