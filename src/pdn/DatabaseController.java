package pdn;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseController {

    public static Connection connection;
    public Boolean standardUtilisationMode = false;
    
    public void instanciateDatabase() {
        getConnection();
        try {
            createDatabaseIfNotExiste();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void getConnection(){
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DatabaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void createDatabaseIfNotExiste() throws SQLException {
        if(!databaseExiste()){
            File file = new File("./././database.db");
            createTables();
            if(!standardUtilisationMode){
                insertData();
            }
        } else {
            Logger.getLogger(DatabaseController.class.getName()).log(Level.INFO, "La BDD existe deja");
        }
    }

    private boolean databaseExiste() throws SQLException {
        boolean result = false;
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT name from sqlite_master WHERE type='table'");
        if(rs.next()){
            result = true;
        }
        return result;
    }

    private void createTables() {
        createTablePersonne();
        createTableMessage();
        createTableObjet();
        createTableDescription();
        createTableObjetDescription();
        createTableMessageAPersonne();
        createTableListeDonObjet();
        createTableListeDemandeObjet();
    }

    private void createTablePersonne() {
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.execute("create table personne (" +
            "    numero_auto INT PRIMARY KEY, " +
            "    date_autorisation VARCHAR(10), " +
            "    prenom VARCHAR(20), " +
            "    nom VARCHAR(20), " +
            "    email VARCHAR(100)" +
            ");");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseController.class.getName()).log(Level.SEVERE, null, "Impossible de creer la table personne :" + ex);
        }
    }

    private void createTableMessage() {
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.execute("create table message (" +
            "    id_message INT PRIMARY KEY, " +
            "    id_message_parent INT, /* Optionnel */" +
            "    statut VARCHAR(20), " +
            "    FOREIGN KEY (id_message_parent) REFERENCES message(id_message)" +
            ");");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseController.class.getName()).log(Level.SEVERE, null, "Impossible de creer la table message :" + ex);
        }
    }

    private void createTableObjet() {
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.execute("create table objet (" +
            "    id_objet INT PRIMARY KEY, " +
            "    nom VARCHAR (100), " +
            "    type VARCHAR(50)" +
            ");");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseController.class.getName()).log(Level.SEVERE, null, "Impossible de creer la table objet :" + ex);
        }
    }

    private void createTableDescription() {
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.execute("create table description (" +
            "    id_description INT PRIMARY KEY, " +
            "    nom VARCHAR(100), " +
            "    valeur VARCHAR(100)" +
            ");");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseController.class.getName()).log(Level.SEVERE, null, "Impossible de creer la table description :" + ex);
        }
    }

    private void createTableObjetDescription() {
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.execute("create table objet_description (" +
            "    id_objet_ref INT, " +
            "    id_description_ref INT, " +
            "    PRIMARY KEY (id_objet_ref, id_description_ref), " +
            "    FOREIGN KEY (id_objet_ref) REFERENCES objet(id_objet), " +
            "    FOREIGN KEY (id_description_ref) REFERENCES description(id_description)" +
            ");");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseController.class.getName()).log(Level.SEVERE, null, "Impossible de creer la table objet_description :" + ex);
        }
    }

    private void createTableMessageAPersonne() {
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.execute("create table messages_a_personne (" +
            "    id_personne_ref INT, " +
            "    id_message_ref INT, " +
            "    PRIMARY KEY (id_personne_ref, id_message_ref), " +
            "    FOREIGN KEY (id_personne_ref) REFERENCES personne(numero_auto), " +
            "    FOREIGN KEY (id_message_ref) REFERENCES message(id_message)" +
            ");");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseController.class.getName()).log(Level.SEVERE, null, "Impossible de creer la table message_a_personne :" + ex);
        }
    }

    private void createTableListeDonObjet() {
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.execute("create table liste_don_objet (" +
            "    id_don_ref INT, " +
            "    id_objet_don_ref INT, " +
            "    PRIMARY KEY (id_don_ref, id_objet_don_ref), " +
            "    FOREIGN KEY (id_don_ref) REFERENCES message(id_message), " +
            "    FOREIGN KEY (id_objet_don_ref) REFERENCES objet(id_objet)" +
            ");");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseController.class.getName()).log(Level.SEVERE, null, "Impossible de creer la table liste_don_objet :" + ex);
        }
    }

    private void createTableListeDemandeObjet() {
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.execute("create table liste_demande_objet (" +
            "    id_demande_ref INT, " +
            "    id_objet_demand_ref INT, " +
            "    PRIMARY KEY (id_demande_ref, id_objet_demand_ref), " +
            "    FOREIGN KEY (id_demande_ref) REFERENCES message(id_message), " +
            "    FOREIGN KEY (id_objet_demand_ref) REFERENCES objet(id_objet)" +
            ");");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseController.class.getName()).log(Level.SEVERE, null, "Impossible de creer la table liste_demande_objet :" + ex);
        }
    }

    private void insertData() {
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.execute("insert into personne values (1, '12/11/2018', 'Louis', 'Pasteur', 'louis.pasteur@domaine.com'); ");
            statement.execute("insert into personne values (2, '25/11/2018', 'Emile', 'Zola', 'emile.zola@domaine.com'); ");
            statement.execute("insert into personne values (3, '12/11/2018', 'Victor', 'Hugo', 'victor.hugo@domaine.com'); ");
            statement.execute("insert into personne values (4, '01/03/2017', 'Jean', 'Jores', 'jean.jores@domaine.com'); ");
            statement.execute("insert into personne values (5, '01/03/2017', 'Marie', 'Curie', 'marie.curie@domaine.com'); ");
            
            statement.execute("insert into objet values (1, 'Gomme', 'Matériel de dessin'); ");
            statement.execute("insert into objet values (2, 'Crayon', 'Matériel de dessin'); ");
            statement.execute("insert into objet values (3, 'Table', 'Décoration'); ");
            statement.execute("insert into objet values (4, 'Enceintes', 'Musique'); ");
            statement.execute("insert into objet values (5, 'Vélo', 'Sport'); ");
            
            statement.execute("insert into description values (1, 'Poids', '10g'); ");
            statement.execute("insert into description values (2, 'Taille', '5cm'); ");
            statement.execute("insert into description values (3, 'Longueur', '2m'); ");
            statement.execute("insert into description values (4, 'Couleur', 'Vert'); ");
            statement.execute("insert into description values (5, 'Puissance', '50W'); ");
            
            statement.execute("insert into message values (1, 1, 'en attente'); ");
            statement.execute("insert into message values (2, 1, 'accepte'); ");
            statement.execute("insert into message values (3, 2, 'contre propose'); ");
            statement.execute("insert into message values (4, 3, 'non lu'); ");
            
            statement.execute("insert into objet_description values (1, 1); ");
            statement.execute("insert into objet_description values (1, 2); ");
            statement.execute("insert into objet_description values (1, 3); ");
            statement.execute("insert into objet_description values (2, 4); ");
            statement.execute("insert into objet_description values (3, 3); ");
            statement.execute("insert into objet_description values (4, 5); ");
            statement.execute("insert into objet_description values (5, 4); ");
            
            statement.execute("insert into messages_a_personne values (1, 2); ");
            statement.execute("insert into messages_a_personne values (1, 3); ");
            statement.execute("insert into messages_a_personne values (2, 1); ");
            statement.execute("insert into messages_a_personne values (3, 4); ");
            
            statement.execute("insert into liste_don_objet values (2, 1); ");
            statement.execute("insert into liste_don_objet values (2, 2); ");
            statement.execute("insert into liste_don_objet values (3, 5); ");
            
            statement.execute("insert into liste_demande_objet values (2, 3); ");
            statement.execute("insert into liste_demande_objet values (2, 4); ");
            statement.execute("insert into liste_demande_objet values (3, 4);");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseController.class.getName()).log(Level.SEVERE, null, "Impossible d'inserer des donnees :" + ex);
        }
    }
}
