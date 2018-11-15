package pdn.dataAccess;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import pdn.DatabaseController;
import pdn.Models.Personne;

public class PersonneDAO extends DatabaseController{
    
    public static List<Personne> getAllPersonne() throws SQLException{
        List<Personne> personnes = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * from personne");
        while (rs.next()) {

            Personne personne = new Personne();
            personne.setNumeroAuthorisation(rs.getInt("numero_auto"));
            personne.setDate(rs.getString("date_autorisation"));
            personne.setNom(rs.getString("nom"));
            personne.setPrenom(rs.getString("prenom"));
            personne.setEmail(rs.getString("email"));

            personnes.add(personne);
        }
        return personnes;
    }
}
