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
import pdn.Models.Description;

public class DescriptionDAO extends DatabaseController {

    public static List<Description> getAllDescriptionForObjetId(long idObjet) {
        List<Description> descriptions = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT d.id_description, d.nom, d.valeur " +
                    "FROM objet_description od, description d " +
                    "WHERE od.id_objet_ref = " + idObjet + " " +
                    "AND od.id_description_ref = d.id_description;");
            while (rs.next()) {
                
                Description description = new Description();
                description.setIdDescription(rs.getInt("id_description"));
                description.setNom(rs.getString("nom"));
                description.setValeur(rs.getString("valeur"));
                
                descriptions.add(description);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PersonneDAO.class.getName()).log(Level.SEVERE, "getAllDescriptionForObjetId error", ex);
        }
        return descriptions;
    }
    
    public static int insererDescription(String nom, String valeur){
        Statement statement;
        ResultSet rs;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery("insert into description (nom, valeur) values (" + nom + ", " + valeur + "); ");
            return rs.getInt("id_description");
        } catch (SQLException ex) {
            Logger.getLogger(ObjetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
}
