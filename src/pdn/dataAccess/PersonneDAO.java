package pdn.dataAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pdn.DatabaseController;
import pdn.Models.Personne;

public class PersonneDAO extends DatabaseController {

    public static List<Personne> getAllPersonne() {
        List<Personne> personnes = new ArrayList<>();
        try {
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
        } catch (SQLException ex) {
            Logger.getLogger(PersonneDAO.class.getName()).log(Level.SEVERE, "getAllPersonne error", ex);
        }
        return personnes;
    }

    public static Personne getPersonneWithName(String newValue) {
        Personne personne = new Personne();
        String[] nomComplet = newValue.split(" ");
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * from personne "
                    + "WHERE prenom LIKE '%" + nomComplet[0]
                    + "' AND nom LIKE '%" + nomComplet[1] + "';");
            while (rs.next()) {

                personne.setNumeroAuthorisation(rs.getInt("numero_auto"));
                personne.setSignatureAuthorisation(rs.getString("date_autorisation"));
                personne.setNom(rs.getString("nom"));
                personne.setPrenom(rs.getString("prenom"));
                personne.setEmail(rs.getString("email"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PersonneDAO.class.getName()).log(Level.SEVERE, "getPersonneWithName error", ex);
        }
        return personne;
    }

    public static int insererPersonne(String prenom, String nom, String mail, String date_autorisation) {
        ResultSet rs;
        try {
            Statement statement = connection.createStatement();
            statement.execute("insert into personne (prenom, nom, email) values (\'" + prenom + "\', \'" + nom + "\', \'" + mail +"\');");
            rs = statement.executeQuery("select numero_auto from personne where prenom=\'" + prenom + "\' and nom=\'" + nom +"\' and email=\'" + mail + "\';");
            return rs.getInt("numero_auto");
        } catch (SQLException ex) {
            Logger.getLogger(PersonneDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
     public static void update(Personne p) {
        String sql = "UPDATE personne SET date_autorisation = ? AND prenom = ? AND nom = ? AND email = ? WHERE numero_auto = ?; ";
 
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
 
            pstmt.setString(1, p.getSignatureAuthorisation().toString());
            pstmt.setString(2, p.getPrenom());
            pstmt.setString(3, p.getNom());
            pstmt.setString(4, p.getEmail());
            pstmt.setInt(5, p.getNumeroAuthorisation());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PersonneDAO.class.getName()).log(Level.SEVERE, "update error", ex);
        }
    }
}