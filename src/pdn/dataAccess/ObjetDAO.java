package pdn.dataAccess;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pdn.DatabaseController;
import static pdn.DatabaseController.connection;
import pdn.Models.Objet;

public class ObjetDAO extends DatabaseController {

    public static List<Objet> getAllObjetDemandeForMessage(long idMessage) {
        List<Objet> objetsDemande = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT distinct o.id_objet, o.nom, o.type " +
                    "FROM objet o, liste_demande_objet dem " +
                    "WHERE dem.id_demande_ref = " + idMessage + " " +
                    "AND dem.id_objet_demand_ref = o.id_objet;");
            while (rs.next()) {
                Objet objetDemande = new Objet();
                objetDemande.setIdObjet(rs.getInt("id_objet"));
                objetDemande.setNom(rs.getString("nom"));
                objetDemande.setType(rs.getString("type"));
                
                objetsDemande.add(objetDemande);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PersonneDAO.class.getName()).log(Level.SEVERE, "getAllObjetDemandeForMessage error", ex);
        }
        return objetsDemande;
    }

    public static List<Objet> getAllObjetDonForMessage(long idMessage) {
        List<Objet> objetsDonee = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT distinct o.id_objet, o.nom, o.type " +
                    "FROM objet o, liste_don_objet don " +
                    "WHERE don.id_don_ref = " + idMessage + " " +
                    "AND don.id_objet_don_ref = o.id_objet;");
            while (rs.next()) {
                Objet objetDon = new Objet();
                objetDon.setIdObjet(rs.getInt("id_objet"));
                objetDon.setNom(rs.getString("nom"));
                objetDon.setType(rs.getString("type"));
                objetDon.setDescriptions(DescriptionDAO.getAllDescriptionForObjetId(objetDon.getIdObjet()));
                
                objetsDonee.add(objetDon);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PersonneDAO.class.getName()).log(Level.SEVERE, "getAllObjetDonForMessage error", ex);
        }
        return objetsDonee;
    }
    
}
