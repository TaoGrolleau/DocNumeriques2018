package pdn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
    private static final String URL_DATABASE = "jdbc:h2:~database";
    private static final String LOGIN_H2 = "sa";
    private static final String PASSWORD_H2 = "sa";
    
    private static Connection connection;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        //instanciateDatabase();
        
        Parent root = FXMLLoader.load(getClass().getResource("MainView.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }
    
    private void instanciateDatabase(){
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(URL_DATABASE, LOGIN_H2, PASSWORD_H2);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void closeDatabaseConnection(){
        try {
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
