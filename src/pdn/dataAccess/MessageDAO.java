package pdn.dataAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pdn.DatabaseController;
import static pdn.DatabaseController.connection;
import pdn.Models.Message;

public class MessageDAO extends DatabaseController{

    public static List<Message> getAllMessageForPersonne(Integer numeroAuthoristion) {
        List<Message> messages = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT m.id_message, m.id_message_parent, m.statut " +
                    "FROM message m, messages_a_personne mp " +
                    "WHERE mp.id_personne_ref = " + numeroAuthoristion + " " +
                    "AND mp.id_message_ref = m.id_message;");
            while (rs.next()) {
                
                Message message = new Message();
                message.setIdMessage(rs.getInt("id_message"));
                message.setIdMessageParent(rs.getInt("id_message_parent"));
                message.setStatut(rs.getString("statut"));
                
                messages.add(message);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PersonneDAO.class.getName()).log(Level.SEVERE, "getAllMessageForPersonne error", ex);
        }
        return messages;
    }
    
     public static void update(Message m) {
        String sql = "UPDATE message SET statut = ? ; ";
 
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
 
            pstmt.setString(1, m.getStatut());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PersonneDAO.class.getName()).log(Level.SEVERE, "update error", ex);
        }
    }
    
}
