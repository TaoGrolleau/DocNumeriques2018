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
            ResultSet rs = statement.executeQuery("SELECT distinct o.id_objet, o.nom, o.type "
                    + "FROM objet o, liste_demande_objet dem "
                    + "WHERE dem.id_demande_ref = " + idMessage + " "
                    + "AND dem.id_objet_demand_ref = o.id_objet;");
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
            ResultSet rs = statement.executeQuery("SELECT distinct o.id_objet, o.nom, o.type "
                    + "FROM objet o, liste_don_objet don "
                    + "WHERE don.id_don_ref = " + idMessage + " "
                    + "AND don.id_objet_don_ref = o.id_objet;");
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

    public static int insererObjet(String nom, String type) {
        Statement statement;
        ResultSet rs;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery("insert into objet (nom, type) values (" + nom + ", " + type + "); ");
            return rs.getInt("id_objet");
        } catch (SQLException ex) {
            Logger.getLogger(ObjetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public static void associerObjetDescription(int idObjet, int idDescription){
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.execute("insert into objet_description values (" + idObjet + ", " + idDescription + ");");
        } catch (SQLException ex) {
            Logger.getLogger(ObjetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void associerMessageDon(Long idMessage, int idObjet){
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.execute("insert into liste_don_objet values (" + idMessage + ", " + idObjet + ");");
        } catch (SQLException ex) {
            Logger.getLogger(ObjetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void associerMessageDemande(Long idMessage, int idObjet){
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.execute("insert into liste_demande_objet values (" + idMessage + ", " + idObjet + ");");
        } catch (SQLException ex) {
            Logger.getLogger(ObjetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
