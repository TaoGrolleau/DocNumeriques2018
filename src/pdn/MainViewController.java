package pdn;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import pdn.Models.Description;
import pdn.Models.Objet;
import pdn.Models.ObjetXML;
import pdn.Models.Proposition;
import pdn.dataAccess.PersonneDAO;

public class MainViewController implements Initializable {

    @FXML
    private TextField txt_path;
    @FXML
    private TextField pathMessageCreated;
    @FXML
    private TextField nameSender;
    @FXML
    private TextField emailSender;
    @FXML
    private TextField nameContact;
    @FXML
    private TextField emailContact;
    @FXML
    private TextField titleProposition;
    @FXML
    private TextField nameObjectProposed;
    @FXML
    private TextField typeObjectProposed;
    @FXML
    private TextField nameParameterObjectProposed;
    @FXML
    private TextField valueParameterObjectProposed;
    @FXML
    private TextField nameObjectAsked;
    @FXML
    private TextField typeObjectAsked;
    @FXML
    private TextField nameParameterObjectAsked;
    @FXML
    private TextField valueParameterObjectAsked;

    @FXML
    private Button btn_path;
    @FXML
    private Button btn_addParameterObjectProposed;
    @FXML
    private Button btn_addObjectProposed;
    @FXML
    private Button btn_addObjectAsked;
    @FXML
    private Button btn_addParameterObjectAsked;
    @FXML
    private Button btn_addProposition;
    @FXML
    private Button btn_createFile;

    @FXML
    private ToggleGroup radiobtn_typeMessage;

    @FXML
    private ListView contactListView;
    @FXML
    private ListView list_objetProposed;
    @FXML
    private ListView list_objetAsked;
    @FXML
    private ListView list_Propositions;

    @FXML
    private Label messageMainTitle;

    ListProperty<String> contactListProperty = new SimpleListProperty<>();

    List<Description> listParameterObjetProposed = new ArrayList<>();
    List<Description> listParameterObjetAsked = new ArrayList<>();

    List<Objet> listObjetProposed = new ArrayList<>();
    List<Objet> listObjetAsked = new ArrayList<>();

    List<Proposition> propositions = new ArrayList<>();

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

        //Ajoute un paramètre à l'objet proposé
        btn_addParameterObjectProposed.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Description newParameter = new Description();
                newParameter.setNom(nameParameterObjectProposed.getText());
                newParameter.setValeur(valueParameterObjectProposed.getText());
                listParameterObjetProposed.add(newParameter);
                nameParameterObjectProposed.setText("");
                valueParameterObjectProposed.setText("");
                System.out.println(listParameterObjetProposed.toString());
            }
        });

        //Ajoute un paramètre à l'objet demandé
        btn_addParameterObjectAsked.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Description newParameter = new Description();
                newParameter.setNom(nameParameterObjectAsked.getText());
                newParameter.setValeur(valueParameterObjectAsked.getText());
                listParameterObjetAsked.add(newParameter);
                nameParameterObjectAsked.setText("");
                valueParameterObjectAsked.setText("");
                System.out.println(listParameterObjetAsked.toString());
            }
        });

        //Ajoute un objet proposé
        btn_addObjectProposed.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Objet newObjectProposed = new Objet();
                List<Description> objectDescriptions = new ArrayList<>();
                objectDescriptions.addAll(listParameterObjetProposed);

                String field_nameObjetProposed = nameObjectProposed.getText();
                String field_typeObjetProposed = typeObjectProposed.getText();

                newObjectProposed.setNom(field_nameObjetProposed);
                newObjectProposed.setType(field_typeObjetProposed);
                newObjectProposed.setDescriptions(objectDescriptions);

                listObjetProposed.add(newObjectProposed);

                listParameterObjetProposed.clear();
                System.out.println(listObjetProposed.toString());
            }
        });

        //Ajoute un objet demandé
        btn_addObjectAsked.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Objet newObjetAsked = new Objet();
                List<Description> objectDescriptions = new ArrayList<>();
                objectDescriptions.addAll(listParameterObjetAsked);

                String field_nameObjectAsked = nameObjectAsked.getText();
                String field_typeObjetAsked = typeObjectAsked.getText();

                newObjetAsked.setNom(field_nameObjectAsked);
                newObjetAsked.setType(field_typeObjetAsked);
                newObjetAsked.setDescriptions(objectDescriptions);

                listObjetAsked.add(newObjetAsked);
                System.out.println(listObjetAsked.toString());
                listParameterObjetAsked.clear();
            }
        });

        //Ajoute une proposition
        btn_addProposition.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Proposition newproposition = new Proposition();

                List<Objet> objectsProposed = new ArrayList<>();
                objectsProposed.addAll(listObjetProposed);
                List<Objet> objectsAsked = new ArrayList<>();
                objectsAsked.addAll(listObjetAsked);

                newproposition.setObjetsProposed(objectsProposed);
                newproposition.setObjetsAsked(objectsAsked);

                propositions.add(newproposition);
                listObjetProposed.clear();
                listObjetAsked.clear();
                System.out.println(propositions.toString());
            }
        });

        //Le message prend les infos qu'à rentré l'utilisateur
        btn_createFile.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ObjetXML message = new ObjetXML();
                List<Proposition> finalProposition = new ArrayList<>();
                finalProposition.addAll(propositions);

                String field_pathFile = pathMessageCreated.getText();

                String field_nameSender = nameSender.getText();
                String field_mailSender = emailSender.getText();
                String field_nameContact = nameContact.getText();
                String field_mailContact = emailContact.getText();

                String typeMessage = radiobtn_typeMessage.getSelectedToggle().toString();

                String field_titleProposition = titleProposition.getText();

                message.setPathFichier(field_pathFile);

                message.setNomEm(field_nameSender);
                message.setMailExpediteur(field_mailSender);
                message.setNomRecepteur(field_nameContact);
                message.setMailDestinataire(field_mailContact);

                message.setTypeMessage(typeMessage);

                message.setTitreProposition(field_titleProposition);
                message.setPropositions(finalProposition);

                propositions.clear();

                System.out.println(message.toString());
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
