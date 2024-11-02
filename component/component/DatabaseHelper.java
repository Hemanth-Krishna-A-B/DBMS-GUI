package component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
     final String URL = "jdbc:mysql://localhost:******";
     final String USER = "*****";
     final String PASSWORD = "******";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    public List<Mail> getMailForParcel(String mid) {
        List<Mail> mailList = new ArrayList<>();
        String query = "SELECT * FROM mail WHERE mid = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, mid);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Mail mail = new Mail(
                    rs.getInt("mid"),
                    rs.getInt("track_id"),
                    rs.getDouble("weight"),
                    rs.getString("ser_no"),
                    rs.getInt("cid"),
                    rs.getString("rec_name"),
                    rs.getString("rec_add"),
                    rs.getString("rec_pin"),
                    rs.getString("sender_name"),
                    rs.getInt("sender_id")
                );
                mailList.add(mail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mailList;
    }
   

    public boolean checkUserIdExists(String empId) {
        String query = "SELECT emp_id FROM employee WHERE emp_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, empId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet getTableData(String query) {
        try {
            Connection conn = getConnection();
            Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<Mail> getAllMails() {
        List<Mail> mailList = new ArrayList<>();
        try {
            Connection connection = getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM mail");
            while (rs.next()) {
                Mail mail = new Mail(rs.getInt("mid"), rs.getInt("track_Id"), rs.getDouble("weight"), 
                                     rs.getString("ser_No"), rs.getInt("cid"), rs.getString("rec_Name"), 
                                     rs.getString("rec_Add"), rs.getString("rec_Pin"), rs.getString("sender_Name"), 
                                     rs.getInt("sender_Id"));
                mailList.add(mail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mailList;
    }

    

    public void updateParcel(Mail mail) {
        try {
            Connection connection = getConnection();
            String query = "UPDATE mail SET weight = ?, ser_No = ?, recName = ?, recAdd = ?, recPin = ?, senderName = ? WHERE mid = ? AND track_Id = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setDouble(1, mail.getWeight());
            pstmt.setString(2, mail.getSerNo());
            pstmt.setString(3, mail.getRecName());
            pstmt.setString(4, mail.getRecAdd());
            pstmt.setString(5, mail.getRecPin());
            pstmt.setString(6, mail.getSenderName());
            pstmt.setInt(7, mail.getMid());
            pstmt.setInt(8, mail.getTrackId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void closeResources(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            try {
                if (resource != null) {
                    resource.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Parcel getParcel(String mid, String trackId) {
        String query = "SELECT * FROM mail WHERE mid = ? AND track_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setString(1, mid);
            statement.setString(2, trackId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Parcel(
                    resultSet.getString("mid"),
                    resultSet.getString("track_id"),
                    resultSet.getString("weight"),
                    resultSet.getString("ser_no"),
                    resultSet.getString("rec_name"),
                    resultSet.getString("rec_add"),
                    resultSet.getString("rec_pin"),
                    resultSet.getString("sender_name"),
                    resultSet.getString("sender_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateParcel(Parcel parcel) {
        String query = "UPDATE mail SET weight = ?, ser_no = ?, rec_name = ?, rec_add = ?, rec_pin = ?, sender_name = ?, sender_id = ? WHERE mid = ? AND track_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setString(1, parcel.getWeight());
            statement.setString(2, parcel.getSerNo());
            statement.setString(3, parcel.getRecName());
            statement.setString(4, parcel.getRecAdd());
            statement.setString(5, parcel.getRecPin());
            statement.setString(6, parcel.getSenderName());
            statement.setString(7, parcel.getSenderId());
            statement.setString(8, parcel.getMid());
            statement.setString(9, parcel.getTrackId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
