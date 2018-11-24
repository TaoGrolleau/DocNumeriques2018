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
import pdn.Models.Message;
import pdn.Models.Objet;
import pdn.Models.ObjetXML;
import pdn.Models.Personne;
import pdn.dataAccess.DescriptionDAO;
import pdn.dataAccess.MessageDAO;
import pdn.dataAccess.ObjetDAO;
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
    private Button btn_answerMessage;
    @FXML
    private Button btn_CounterProposalMessage;
    @FXML
    private Button btn_markRead;

    @FXML
    private ToggleGroup radiobtn_typeMessage;

    @FXML
    private ListView contactListView;
    @FXML
    private ListView transactionListView;
    @FXML
    private ListView messageListView;
    @FXML
    private ListView contacts;
    @FXML
    private ListView list_objetProposed;
    @FXML
    private ListView list_objetAsked;
    @FXML
    private ListView list_Propositions;

    @FXML
    private Label messageMainTitle;

    ListProperty<String> contactListProperty = new SimpleListProperty<>();
    ListProperty<String> messageListProperty = new SimpleListProperty<>();
    ListProperty<String> transactionListProperty = new SimpleListProperty<>();

    List<Description> listParameterObjetProposed = new ArrayList<>();
    List<Description> listParameterObjetAsked = new ArrayList<>();

    List<Objet> listObjetProposed = new ArrayList<>();
    List<Objet> listObjetAsked = new ArrayList<>();

    List<Message> messages = new ArrayList<>();

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
                Message newMessage = new Message();

                List<Objet> objectsProposed = new ArrayList<>();
                objectsProposed.addAll(listObjetProposed);
                List<Objet> objectsAsked = new ArrayList<>();
                objectsAsked.addAll(listObjetAsked);

                String typeMessage = radiobtn_typeMessage.getSelectedToggle().toString();

                String field_titleProposition = titleProposition.getText();

                newMessage.setObjetsProposed(objectsProposed);
                newMessage.setObjetsAsked(objectsAsked);
                
                newMessage.setTypeMessage(typeMessage);
                newMessage.setTitreProposition(field_titleProposition);

                messages.add(newMessage);
                listObjetProposed.clear();
                listObjetAsked.clear();
                System.out.println(messages.toString());
            }
        });

        //Le message prend les infos qu'à rentré l'utilisateur
        btn_createFile.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ObjetXML objetXml = new ObjetXML();
                List<Message> finalMessages = new ArrayList<>();
                finalMessages.addAll(messages);

                String field_pathFile = pathMessageCreated.getText();

                String field_nameSender = nameSender.getText();
                String field_mailSender = emailSender.getText();
                String field_nameContact = nameContact.getText();
                String field_mailContact = emailContact.getText();

                objetXml.setPathFichier(field_pathFile);

                objetXml.setNomEm(field_nameSender);
                objetXml.setMailExpediteur(field_mailSender);
                objetXml.setNomRecepteur(field_nameContact);
                objetXml.setMailDestinataire(field_mailContact);

                objetXml.setMessages(finalMessages);
                objetXml.CreateXmlFile(objetXml);
                messages.clear();

                System.out.println(objetXml.toString());
            }
        });

        List<String> contactsList = new ArrayList<>();
        PersonneDAO.getAllPersonne()
                .forEach(personne -> {
                    contactsList.add(personne.getPrenom() + " " + personne.getNom());
                });
        contactListProperty.set(FXCollections.observableArrayList(contactsList));
        contacts.itemsProperty().bind(contactListProperty);
        contactListView.itemsProperty().bind(contactListProperty);
        contactListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                messageMainTitle.setText(newValue);
                Personne personneSelected = PersonneDAO.getPersonneWithName(newValue);
                List<Message> messages = MessageDAO.getAllMessageForPersonne(personneSelected.getNumeroAuthoristion());
                List<String> messageList = new ArrayList<>();
                messages.forEach(m -> {
                    Boolean noDemande = true;
                    Boolean noDon = true;
                    StringBuilder format = new StringBuilder();
                    m.setObjetsProposed(ObjetDAO.getAllObjetDonForMessage(m.getIdMessage()));
                    if (!m.getObjetsProposed().isEmpty()) {
                        noDemande = false;
                        m.getObjetsProposed().forEach(o -> {
                            o.setDescriptions(DescriptionDAO.getAllDescriptionForObjetId(o.getIdObjet()));
                        });
                        format.append("Don : ");
                        m.getObjetsProposed().forEach(o -> {
                            format.append(o.getNom())
                                .append(" - ")
                                .append(o.getType());
                            if(!o.getDescriptions().isEmpty()){
                                o.getDescriptions().forEach(d -> {
                                    format.append(" - ")
                                        .append(d.toString());
                                });
                            }
                        });
                    }
                    m.setObjetsAsked(ObjetDAO.getAllObjetDemandeForMessage(m.getIdMessage()));
                    if (!m.getObjetsAsked().isEmpty()) {
                        noDon = false;
                        m.getObjetsAsked().forEach(o -> {
                            o.setDescriptions(DescriptionDAO.getAllDescriptionForObjetId(o.getIdObjet()));
                        });
                        format.append(" / Demande : ");
                        m.getObjetsAsked().forEach(o -> {
                            format.append(o.getNom())
                                .append(" - ")
                                .append(o.getType());
                            if(!o.getDescriptions().isEmpty()){
                                o.getDescriptions().forEach(d -> {
                                    format.append(" - ")
                                            .append(d.toString());
                                    });
                            }
                        });
                    }
                    if (noDemande && noDon) {
                        format.append("Demande d'authorisation");
                    }
                    messageList.add(format.toString());
                });
                messageListProperty.set(FXCollections.observableArrayList(messageList));
                messageListView.itemsProperty().bind(messageListProperty);

                List<Message> transactions = messages;
                List<String> transactionList = new ArrayList<>();
                transactions.forEach(t -> {
                    Boolean noDemande = true;
                    Boolean noDon = true;
                    StringBuilder format = new StringBuilder();
                    if (t.getStatut().equalsIgnoreCase(Message.STATUT_ACCEPTE)) {
                        if (!t.getObjetsAsked().isEmpty()) {
                            noDemande = false;
                            t.getObjetsAsked().forEach(o -> {
                                format.append("Demande : ")
                                        .append(o.getNom());
                            });
                        }
                        if (!t.getObjetsProposed().isEmpty()) {
                            noDon = false;
                            t.getObjetsProposed().forEach(o -> {
                                format.append(" / Don : ")
                                        .append(o.getNom());
                            });
                        }
                        if (!noDemande || !noDon) {
                            transactionList.add(format.toString());
                        }
                    }
                });
                transactionListProperty.set(FXCollections.observableArrayList(transactionList));
                transactionListView.itemsProperty().bind(transactionListProperty);
            
                Message lastMessage = messages.get(messages.size()-1);
                changeStateOfButtons(lastMessage);
                
                btn_answerMessage.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        lastMessage.setStatut(Message.STATUT_ACCEPTE);
                        MessageDAO.update(lastMessage);
                        changeStateOfButtons(lastMessage);
                        refreshTransactionList();
                        // appeler ma méthode de création du fichier XML
                    }

                    private void refreshTransactionList() {
                        List<Message> transactions = messages;
                        List<String> transactionList = new ArrayList<>();
                        transactions.forEach(t -> {
                            Boolean noDemande = true;
                            Boolean noDon = true;
                            StringBuilder format = new StringBuilder();
                            if (t.getStatut().equalsIgnoreCase(Message.STATUT_ACCEPTE)) {
                                if (!t.getObjetsAsked().isEmpty()) {
                                    noDemande = false;
                                    t.getObjetsAsked().forEach(o -> {
                                        format.append("Demande : ")
                                                .append(o.getNom());
                                    });
                                }
                                if (!t.getObjetsProposed().isEmpty()) {
                                    noDon = false;
                                    t.getObjetsProposed().forEach(o -> {
                                        format.append(" / Don : ")
                                                .append(o.getNom());
                                    });
                                }
                                if (!noDemande || !noDon) {
                                    transactionList.add(format.toString());
                                }
                            }
                        });
                        transactionListProperty.set(FXCollections.observableArrayList(transactionList));
                        transactionListView.itemsProperty().bind(transactionListProperty);
                    }
                });
                
                btn_CounterProposalMessage.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        lastMessage.setStatut(Message.STATUT_CONTRE_PROPOSE);
                        MessageDAO.update(lastMessage);
                        changeStateOfButtons(lastMessage);
                        // appeler ma méthode de création du fichier XML
                    }
                });
                
                btn_markRead.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        lastMessage.setStatut(Message.STATUT_EN_ATTENTE);
                        MessageDAO.update(lastMessage);
                        changeStateOfButtons(lastMessage);
                    }
                });
            }

            private void changeStateOfButtons(Message lastMessage) {
                if(lastMessage.getStatut().equalsIgnoreCase(Message.STATUT_NON_LU)){
                    btn_answerMessage.setDisable(true);
                    btn_markRead.setDisable(false);
                    btn_CounterProposalMessage.setDisable(true);
                } else if(lastMessage.getStatut().equalsIgnoreCase(Message.STATUT_EN_ATTENTE)){
                    btn_answerMessage.setDisable(false);
                    btn_markRead.setDisable(true);
                    btn_CounterProposalMessage.setDisable(false);;
                } else if(lastMessage.getStatut().equalsIgnoreCase(Message.STATUT_ACCEPTE)){
                    btn_answerMessage.setDisable(true);
                    btn_markRead.setDisable(true);
                    btn_CounterProposalMessage.setDisable(true);
                } else { //STATUT_CONTREPROPOSE
                    btn_answerMessage.setDisable(false);
                    btn_markRead.setDisable(false);
                    btn_CounterProposalMessage.setDisable(false);
                }
            }
        });

        //Utiliser la méthode suivante lorsqu'on ferme définitivement la fenêtre
        //closeDatabaseConnection();
    }

}
