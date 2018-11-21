package pdn;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import pdn.dataAccess.PersonneDAO;

public class MainViewController implements Initializable {

    @FXML
    private TextField txt_path;
    @FXML
    private Button btn_path;
    @FXML
    private ListView contactListView;
    @FXML
    private Label messageMainTitle;
    
    ListProperty<String> contactListProperty = new SimpleListProperty<>();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Lance le scrupteur de fichier
        btn_path.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("You clicked me!");
                String newPath = txt_path.getText();
                System.out.println(newPath);

                if (DirectoryWatch.threadFileWatcher != null && DirectoryWatch.threadFileWatcher.isAlive()) {
                    DirectoryWatch.threadFileWatcher.interrupt();
                }
                try {
                    DirectoryWatch.StartFileWatcherThread(newPath);
                } catch (Exception ex) {
                    System.out.println("Error: Cannot start the thread => " + ex.getMessage());
                }
            }
        });
        
        List<String> contacts = new ArrayList<>();
        PersonneDAO.getAllPersonne()
                .forEach(personne -> {
                    contacts.add(personne.getPrenom() + " " + personne.getNom());
                });
        contactListProperty.set(FXCollections.observableArrayList(contacts));
        contactListView.itemsProperty().bind(contactListProperty);
        contactListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                messageMainTitle.setText(newValue);
            }
        });

        //Utiliser la méthode suivante lorsqu'on ferme définitivement la fenêtre
        //closeDatabaseConnection();
    }

}
