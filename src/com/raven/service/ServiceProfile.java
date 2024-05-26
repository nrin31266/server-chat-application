package com.raven.service;

import com.raven.connection.DatabaseConnection;
import com.raven.model.Model_Profile;
import com.raven.model.Model_User_Account;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;

public class ServiceProfile {

    public ServiceProfile() {
        this.con = DatabaseConnection.getInstance().getConnection();
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
                byte[] imageBytes = r.getBytes("Image");
                if (imageBytes != null) {
                    String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
                    data.setImage(imageBase64);
                }

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
                byte[] coverArtBytes = r.getBytes("CoverArt");
                if (coverArtBytes != null) {
                    String coverArtBase64 = Base64.getEncoder().encodeToString(coverArtBytes);
                    data.setCoverArt(coverArtBase64);
                }

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

//Sql
    private final String SELECT_INFO
            = "select * from `user_account` where UserID=?";

    private final Connection con;
}
