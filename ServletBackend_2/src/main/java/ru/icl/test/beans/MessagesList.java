package ru.icl.test.beans;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.icl.test.db.Database;

public class MessagesList {
    
    private ArrayList<Messages> messagesList = new ArrayList<Messages>();

    public ArrayList<Messages> getMessages() {
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        String sql = "select m.id, m.clientid, u.fname, m.message "
                    + "from messages m, users u "
                    + "where u.clientid = m.clientid";
        try {
            conn = Database.getConnection();

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {                
                Messages msg = new Messages();
                msg.setId(rs.getInt(1));                
                msg.setClientid(rs.getString(2));                
                msg.setFname(rs.getString(3));                
                msg.setMessage(rs.getString(4));                                
                messagesList.add(msg);
                
                System.out.println(rs.getInt(1) 
                        + "\t" + rs.getString(2)
                        + "\t" + rs.getString(3)
                        + "\t" + rs.getString(4));
            }

        } catch (SQLException ex) {
            Logger.getLogger(MessagesList.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (stmt!=null) stmt.close();
                if (rs!=null)rs.close();
                if (conn!=null)conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(MessagesList.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return messagesList;
    }

    public ArrayList<Messages> getMessagesList() {
        if (!messagesList.isEmpty()) {
            return messagesList;
        } else {
            return getMessages();
        }
    }
}
