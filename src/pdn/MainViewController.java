package pdn;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TabPane;
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
    
    private final String PATH = "././createdFiles/file.xml";
    
    @FXML
    private TabPane tab_pane;
    
    @FXML
    private Tab tab_createFile;
    
    private SingleSelectionModel<Tab> tabSelect;

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

    RadioButton selectedtypeMessage;

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
                if (!(nameParameterObjectProposed.getText().isEmpty()) && !(valueParameterObjectProposed.getText().isEmpty())) {
                    listParameterObjetProposed.add(newParameter);
                    nameParameterObjectProposed.setText("");
                    valueParameterObjectProposed.setText("");
                }

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
                if (!(nameParameterObjectAsked.getText().isEmpty()) && !(valueParameterObjectAsked.getText().isEmpty())) {
                    listParameterObjetAsked.add(newParameter);
                    nameParameterObjectAsked.setText("");
                    valueParameterObjectAsked.setText("");
                }
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
                ObservableList<Objet> items = FXCollections.observableArrayList(listObjetProposed);
                list_objetProposed.setItems(items);
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
                listParameterObjetAsked.clear();

                System.out.println(listObjetAsked.toString());
                ObservableList<Objet> items = FXCollections.observableArrayList(listObjetAsked);
                list_objetAsked.setItems(items);
            }
        });

        //Ajoute une proposition
        btn_addProposition.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Message newMessage = new Message();
                Date dateSignature = new Date();
                selectedtypeMessage = (RadioButton) radiobtn_typeMessage.getSelectedToggle();

                List<Objet> objectsProposed = new ArrayList<>();
                objectsProposed.addAll(listObjetProposed);
                List<Objet> objectsAsked = new ArrayList<>();
                objectsAsked.addAll(listObjetAsked);

                String typeMessage = selectedtypeMessage.getText();

                String field_titleProposition = titleProposition.getText();

                newMessage.setObjetsProposed(objectsProposed);
                newMessage.setObjetsAsked(objectsAsked);

                newMessage.setDateMessage(dateSignature);
                newMessage.setDureeValiditeMessage(14);

                newMessage.setTypeMessage(typeMessage);
                newMessage.setTitreProposition(field_titleProposition);

                messages.add(newMessage);
                listObjetProposed.clear();
                listObjetAsked.clear();
                System.out.println(messages.toString());
                ObservableList<Message> items = FXCollections.observableArrayList(messages);
                list_Propositions.setItems(items);

            }
        });

        //Le message prend les infos qu'à rentré l'utilisateur
        btn_createFile.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ObjetXML objetXml = new ObjetXML();
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                Date dateSignature = new Date();
                selectedtypeMessage = (RadioButton) radiobtn_typeMessage.getSelectedToggle();

                if (selectedtypeMessage.getText().equals("Demande D'authorisation")) {
                    Message newMessage = new Message();
                    newMessage.setDateMessage(dateSignature);
                    newMessage.setDureeValiditeMessage(14);
                    newMessage.setTypeMessage(selectedtypeMessage.getText());
                    
                    String field_nameContact = nameContact.getText();
                    String field_mailContact = emailContact.getText();
                    objetXml.setNomRecepteur(field_nameContact);
                    objetXml.setMailDestinataire(field_mailContact);
                    messages.add(newMessage);
                }

                List<Message> finalMessages = new ArrayList<>();
                finalMessages.addAll(messages);

                String field_pathFile = pathMessageCreated.getText();
                objetXml.setNomEm(Personne.NOM_GLOBAL);
                objetXml.setMailExpediteur(Personne.EMAIL);

                if (!contacts.getSelectionModel().isEmpty()) {
                    Personne contact = PersonneDAO.getPersonneWithName(contacts.getSelectionModel().getSelectedItem().toString());
                    objetXml.setNomRecepteur(contact.getNom());
                    objetXml.setMailDestinataire(contact.getEmail());
                    objetXml.setNumAuthorisation(contact.getNumeroAuthorisation().toString());
                    objetXml.setSignatureAuthorisation(contact.getSignatureAuthorisation());
                }

                if (!field_pathFile.isEmpty()) {
                    objetXml.setPathFichier(field_pathFile);
                }

                objetXml.setDureeValidite(14);
                objetXml.setMessages(finalMessages);

                try {
                    objetXml.CreateXmlFile();
                } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                }
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
                List<Message> messages = MessageDAO.getAllMessageForPersonne(personneSelected.getNumeroAuthorisation());
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
                            if (!o.getDescriptions().isEmpty()) {
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
                            if (!o.getDescriptions().isEmpty()) {
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
                Message lastMessage;
                if (messages.size() > 0) {
                    lastMessage = messages.get(messages.size() - 1);
                    changeStateOfButtons(lastMessage);

                    btn_answerMessage.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            
                            ObjetXML objetXML = new ObjetXML();
                            try {
                                objetXML.setNomEm(Personne.NOM);
                                objetXML.setNomRecepteur(personneSelected.toString());
                                objetXML.setNumAuthorisation(personneSelected.getNumeroAuthorisation().toString());
                                objetXML.setSignatureAuthorisation(personneSelected.getSignatureAuthorisation());
                                objetXML.setDureeValidite(lastMessage.getDureeValiditeMessage());
                                objetXML.setMailDestinataire(personneSelected.getEmail());
                                objetXML.setMailExpediteur(Personne.EMAIL);
                                objetXML.setPathFichier(PATH);
                                Message reponse = new Message();
                                if(lastMessage.getTypeMessage().equalsIgnoreCase("Demande D'authorisation")){
                                    reponse.setTypeMessage("Auth");
                                } else {
                                    reponse.setTypeMessage("Accep");
                                }
                                reponse.setStatut(Message.STATUT_ACCEPTE);
                                messages.add(reponse);
                                objetXML.setMessages(messages);
                                objetXML.CreateXmlFile();
                                lastMessage.setStatut(Message.STATUT_ACCEPTE);
                                MessageDAO.update(lastMessage);
                                changeStateOfButtons(lastMessage);
                                refreshTransactionList();
                            } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                                Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
                            }
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
                            tab_pane.getSelectionModel().select(tab_createFile);
                            contacts.getSelectionModel().select(contactListView.getSelectionModel().getSelectedIndex());
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
            }

            private void changeStateOfButtons(Message lastMessage) {
                if (lastMessage.getStatut().equalsIgnoreCase(Message.STATUT_NON_LU)) {
                    btn_answerMessage.setDisable(true);
                    btn_markRead.setDisable(false);
                    btn_CounterProposalMessage.setDisable(true);
                } else if (lastMessage.getStatut().equalsIgnoreCase(Message.STATUT_EN_ATTENTE)) {
                    btn_answerMessage.setDisable(false);
                    btn_markRead.setDisable(true);
                    btn_CounterProposalMessage.setDisable(false);;
                } else if (lastMessage.getStatut().equalsIgnoreCase(Message.STATUT_ACCEPTE)) {
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
