package pdn;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Si la base de données existe on s'y connecte
        // Si elle n'existe pas on la crée et on insère des données si on n'est pas en mode utilisation standard
        DatabaseController databaseController = new DatabaseController();
        databaseController.instanciateDatabase();

        // Création du visuel
        Parent root = FXMLLoader.load(getClass().getResource("MainView.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
