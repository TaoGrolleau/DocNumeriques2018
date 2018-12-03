package pdn;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import pdn.Models.Description;
import pdn.Models.Message;
import pdn.Models.Objet;
import pdn.Models.ObjetXML;
import pdn.Models.Personne;
import pdn.dataAccess.DescriptionDAO;
import pdn.dataAccess.MessageDAO;
import pdn.dataAccess.ObjetDAO;
import pdn.dataAccess.PersonneDAO;

public class Parser {

    private ObjetXML fichier;
    private boolean noAuth;

    public Parser() {

    }

    public void test(String path) {
        System.out.println(path);
    }

    public void parsingFichier(String path) {
        this.noAuth = false;
        this.fichier = new ObjetXML();
        DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
        DocumentBuilder constructeur;
        File xml = new File(path);
        try {
            if (xml.length() > 5000) {
                throw new IOException();
            }
            constructeur = fabrique.newDocumentBuilder();
            Document document = (Document) constructeur.parse(xml);
            System.out.println("Fichier conforme a la DTD...");
            if (!document.getElementsByTagName("FicID").item(0).getTextContent().equals("")) {
                this.fichier.setIdFichier(document.getElementsByTagName("FicID").item(0).getTextContent());
            } else {
                System.out.print("Pas d'ID fichier");
                throw new IOException();
            }
            if (!document.getElementsByTagName("NmIE").item(0).getTextContent().equals("")) {
                this.fichier.setNomEm(document.getElementsByTagName("NmIE").item(0).getTextContent());
            } else {
                System.out.print("Pas de nom d'expéditeur");
                throw new IOException();
            }
            if (!document.getElementsByTagName("NmIR").item(0).getTextContent().equals("")) {
                this.fichier.setNomRecepteur(document.getElementsByTagName("NmIR").item(0).getTextContent());
            } else {
                System.out.print("Pas de nom de destinataire");
                throw new IOException();
            }
            if (!document.getElementsByTagName("NumAuto").item(0).getTextContent().equals("null")) {
                this.fichier.setNumAuthorisation(document.getElementsByTagName("NumAuto").item(0).getTextContent());
            } else {
                System.out.println("c'est une demande d'autorisation");
                this.noAuth = true;
            }
            if (!document.getElementsByTagName("DureeValidAuto").item(0).getTextContent().equals("null") && !this.noAuth) {
                this.fichier.setDureeValidite(Integer.parseInt(document.getElementsByTagName("DureeValidAuto").item(0).getTextContent().trim()));
            } else if (!this.noAuth) {
                System.out.print("Pas de duree de validite");
                throw new IOException();
            }
            if (!document.getElementsByTagName("MailDest").item(0).getTextContent().equals("")) {
                this.fichier.setMailDestinataire(document.getElementsByTagName("MailDest").item(0).getTextContent());
            } else {
                System.out.print("Pas de mail destinataire");
                throw new IOException();
            }
            if (!document.getElementsByTagName("MailExp").item(0).getTextContent().equals("")) {
                this.fichier.setMailExpediteur(document.getElementsByTagName("MailExp").item(0).getTextContent());
            } else {
                System.out.print("Pas de mail expediteur");
                throw new IOException();
            }
            this.fichier.setPathFichier(path);
            NodeList liste = document.getElementsByTagName("Message");
            Element messageEnCours;
            Message message;
            Element type;
            String date;
            String duree;
            int tmp;
            System.out.println("Messages");
            if (this.noAuth) {
                System.out.println("demande d'autorisation");
                message = new Message();
                messageEnCours = (Element) liste.item(0);
                if (!messageEnCours.getElementsByTagName("Dte").item(0).getTextContent().equals("")) {
                    date = messageEnCours.getElementsByTagName("Dte").item(0).getTextContent();
                    message.setDateMessage(date);
                } else {
                    System.out.println("Pas de date");
                    throw new IOException();
                }
                if (!messageEnCours.getElementsByTagName("DureeValideMsg").item(0).getTextContent().equals("")) {
                    duree = messageEnCours.getElementsByTagName("DureeValideMsg").item(0).getTextContent();
                    tmp = Integer.parseInt(duree.trim());
                    if (tmp > 93) {
                        System.out.println("duree superieure a 3 mois");
                        throw new IOException();
                    }
                    duree = messageEnCours.getElementsByTagName("DureeValideMsg").item(0).getTextContent();
                    message.setDureeValiditeMessage(Integer.parseInt(duree.trim()));
                } else {
                    System.out.println("Pas de duree validite");
                    throw new IOException();
                }
                Calendar today = Calendar.getInstance();
                Calendar c = Calendar.getInstance();
                c.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(message.getDateMessage()));
                c.add(Calendar.DATE, message.getDureeValiditeMessage());
                if (today.after(c)) {
                    System.out.println("Message perimé");
                    throw new IOException();
                }
                if (messageEnCours.hasAttribute("MsgId")) {
                    message.setIdMessage(Integer.parseInt(messageEnCours.getAttribute("MsgId").trim()));
                }
                System.out.println("ajout du message au fichier");
                this.fichier.getMessages().add(message);
            } else {
                for (int k = 0; k < liste.getLength(); k++) {
                    message = new Message();
                    messageEnCours = (Element) liste.item(k);
                    if (!messageEnCours.hasAttribute("MsgId")) {
                        throw new IOException();
                    } else {
                        message.setIdMessage(Integer.parseInt(messageEnCours.getAttribute("MsgId").trim()));
                    }
                    if (messageEnCours.hasAttribute("ReponseA")) {
                        if (!messageEnCours.getAttribute("ReponseA").trim().equals("null")) {
                            message.setIdMessageParent(Integer.parseInt(messageEnCours.getAttribute("ReponseA").trim()));
                        }
                    }
                    if (!messageEnCours.getElementsByTagName("Dte").item(0).getTextContent().equals("")) {
                        message.setDateMessage(messageEnCours.getElementsByTagName("Dte").item(0).getTextContent());
                    } else {
                        throw new IOException();
                    }
                    if (!messageEnCours.getElementsByTagName("DureeValideMsg").item(0).getTextContent().equals("")) {
                        if (Integer.parseInt(messageEnCours.getElementsByTagName("DureeValideMsg").item(0).getTextContent().trim()) > 93) {
                            throw new IOException();
                        }
                        message.setDureeValiditeMessage(Integer.parseInt(messageEnCours.getElementsByTagName("DureeValideMsg").item(0).getTextContent().trim()));
                    } else {
                        throw new IOException();
                    }
                    if (!messageEnCours.getElementsByTagName("Prop").item(0).getTextContent().equals("")) {
                        if (!document.getElementsByTagName("DtOfSgtAuto").item(0).getTextContent().equals("") && !this.noAuth) {
                            this.fichier.setSignatureAuthorisation(document.getElementsByTagName("DtOfSgtAuto").item(0).getTextContent());
                        } else if (!this.noAuth) {
                            throw new IOException();
                        }
                        message.setTypeMessage("Prop");
                        type = (Element) messageEnCours.getElementsByTagName("Prop").item(0);
                        message.setTitreProposition(type.getElementsByTagName("TitreP").item(0).getTextContent());
                        if (type.getElementsByTagName("Offre").item(0).getTextContent().equals("")) {
                            throw new IOException();
                        }
                        NodeList offre = type.getElementsByTagName("Offre").item(0).getChildNodes();
                        ArrayList<Objet> p = new ArrayList<>();
                        Objet o = new Objet();
                        Element objet;
                        ArrayList<Description> descriptions;
                        Description description;
                        NodeList parametres;
                        for (int i = 0; i < offre.getLength(); i++) {
                            objet = (Element) offre.item(i);
                            if (objet.getTextContent().equals("")) {
                                throw new IOException();
                            }
                            if (objet.getElementsByTagName("NomObjet").item(0).getTextContent().equals("")) {
                                throw new IOException();
                            }
                            o.setNom(objet.getElementsByTagName("NomObjet").item(0).getTextContent());
                            if (objet.getElementsByTagName("Type").item(0).getTextContent().equals("")) {
                                throw new IOException();
                            }
                            o.setType(objet.getElementsByTagName("Type").item(0).getTextContent());
                            descriptions = new ArrayList<>();

                            parametres = objet.getElementsByTagName("Parametre");
                            for (int j = 0; j < parametres.getLength(); j++) {
                                Element e = (Element) parametres.item(i);
                                if (e.getTextContent().equals("")) {
                                    throw new IOException();
                                }
                                description = new Description();
                                if (e.getElementsByTagName("Nom").item(0).getTextContent().equals("")) {
                                    throw new IOException();
                                }
                                description.setNom(e.getElementsByTagName("Nom").item(0).getTextContent());
                                if (e.getElementsByTagName("Valeur").item(0).getTextContent().equals("")) {
                                    throw new IOException();
                                }
                                description.setValeur(e.getElementsByTagName("Valeur").item(0).getTextContent());
                                descriptions.add(description);
                            }
                            o.setDescriptions(descriptions);
                            p.add(o);
                        }
                        message.setObjetsProposed(p);
                        p = new ArrayList<>();

                        if (type.getElementsByTagName("Demande").item(0).getTextContent().equals("")) {
                            throw new IOException();
                        }
                        NodeList prop = type.getElementsByTagName("Demande").item(0).getChildNodes();
                        for (int i = 0; i < prop.getLength(); i++) {
                            objet = (Element) prop.item(i);
                            if (objet.getTextContent().equals("")) {
                                throw new IOException();
                            }
                            if (objet.getElementsByTagName("NomObjet").item(0).getTextContent().equals("")) {
                                throw new IOException();
                            }
                            o.setNom(objet.getElementsByTagName("NomObjet").item(0).getTextContent());
                            if (objet.getElementsByTagName("Type").item(0).getTextContent().equals("")) {
                                throw new IOException();
                            }
                            o.setType(objet.getElementsByTagName("Type").item(0).getTextContent());
                            descriptions = new ArrayList<>();
                            parametres = objet.getElementsByTagName("Parametre");
                            for (int j = 0; j < parametres.getLength(); j++) {
                                Element e = (Element) parametres.item(i);
                                if (e.getTextContent().equals("")) {
                                    throw new IOException();
                                }
                                description = new Description();
                                if (e.getElementsByTagName("Nom").item(0).getTextContent().equals("")) {
                                    throw new IOException();
                                }
                                description.setNom(e.getElementsByTagName("Nom").item(0).getTextContent());
                                if (e.getElementsByTagName("Valeur").item(0).getTextContent().equals("")) {
                                    throw new IOException();
                                }
                                description.setValeur(e.getElementsByTagName("Valeur").item(0).getTextContent());
                                descriptions.add(description);
                            }
                            o.setDescriptions(descriptions);
                            p.add(o);
                        }
                        message.setObjetsAsked(p);
                    }
                    if (!messageEnCours.getElementsByTagName("Auth").item(0).getTextContent().equals("")) {
                        if (!document.getElementsByTagName("DtOfSgtAuto").item(0).getTextContent().equals("") && !this.noAuth) {
                            this.fichier.setSignatureAuthorisation(document.getElementsByTagName("DtOfSgtAuto").item(0).getTextContent());
                        } else if (!this.noAuth) {
                            throw new IOException();
                        }
                        message.setTypeMessage("Auth");
                        type = (Element) messageEnCours.getElementsByTagName("Auth").item(0);
                        type = (Element) type.getElementsByTagName("Rep").item(0);
                        message.setAcceptAuthorisation(type.getElementsByTagName("AccAuth").item(0).getTextContent());

                        message.setAcceptAuthorisation(type.getElementsByTagName("RefAuth").item(0).getTextContent());

                    }
                    if (!messageEnCours.getElementsByTagName("Dmd").item(0).getTextContent().equals("")) {
                        if (!document.getElementsByTagName("DtOfSgtAuto").item(0).getTextContent().equals("") && !this.noAuth) {
                            this.fichier.setSignatureAuthorisation(document.getElementsByTagName("DtOfSgtAuto").item(0).getTextContent());
                        } else if (!this.noAuth) {
                            throw new IOException();
                        }
                        message.setTypeMessage("Dmd");
                        type = (Element) messageEnCours.getElementsByTagName("Dmd").item(0);

                        message.setDescriptionDemande(type.getElementsByTagName("DescDmd").item(0).getTextContent());
                        if (type.getElementsByTagName("DateDebut").item(0).getTextContent().equals("")) {
                            throw new IOException();
                        }
                        message.setDebutDemande((Date) new SimpleDateFormat("dd/MM/yyyy").parse(type.getElementsByTagName("DateDebut").item(0).getTextContent()));
                        if (type.getElementsByTagName("DateFin").item(0).getTextContent().equals("")) {
                            throw new IOException();
                        }
                        message.setFinDemande((Date) new SimpleDateFormat("dd/MM/yyyy").parse(type.getElementsByTagName("DateFin").item(0).getTextContent()));
                    }
                    if (!messageEnCours.getElementsByTagName("Accep").item(0).getTextContent().equals("")) {
                        if (!document.getElementsByTagName("DtOfSgtAuto").item(0).getTextContent().equals("") && !this.noAuth) {
                            this.fichier.setSignatureAuthorisation(document.getElementsByTagName("DtOfSgtAuto").item(0).getTextContent());
                        } else if (!this.noAuth) {
                            throw new IOException();
                        }
                        message.setTypeMessage("Accep");
                        type = (Element) messageEnCours.getElementsByTagName("Accep").item(0);
                        if (!type.getElementsByTagName("MessageValid").item(0).getTextContent().equals("")) {
                            message.setMessageReponse(type.getElementsByTagName("MessageValid").item(0).getTextContent());
                        } else {
                            type = (Element) messageEnCours.getElementsByTagName("Prop").item(0);

                            message.setTitreProposition(type.getElementsByTagName("TitreP").item(0).getTextContent());
                            if (type.getElementsByTagName("Offre").item(0).getTextContent().equals("")) {
                                throw new IOException();
                            }
                            NodeList offre = type.getElementsByTagName("Offre").item(0).getChildNodes();
                            ArrayList<Objet> p = new ArrayList<>();
                            Objet o = new Objet();
                            Element objet;
                            ArrayList<Description> descriptions;
                            Description description;
                            NodeList parametres;
                            for (int i = 0; i < offre.getLength(); i++) {
                                objet = (Element) offre.item(i);
                                if (objet.getTextContent().equals("")) {
                                    throw new IOException();
                                }
                                if (objet.getElementsByTagName("NomObjet").item(0).getTextContent().equals("")) {
                                    throw new IOException();
                                }
                                o.setNom(objet.getElementsByTagName("NomObjet").item(0).getTextContent());
                                if (objet.getElementsByTagName("Type").item(0).getTextContent().equals("")) {
                                    throw new IOException();
                                }
                                o.setType(objet.getElementsByTagName("Type").item(0).getTextContent());
                                descriptions = new ArrayList<>();
                                parametres = objet.getElementsByTagName("Parametre");
                                for (int j = 0; j < parametres.getLength(); j++) {
                                    Element e = (Element) parametres.item(i);
                                    if (e.getTextContent().equals("")) {
                                        throw new IOException();
                                    }
                                    description = new Description();
                                    if (e.getElementsByTagName("Nom").item(0).getTextContent().equals("")) {
                                        throw new IOException();
                                    }
                                    description.setNom(e.getElementsByTagName("Nom").item(0).getTextContent());
                                    if (e.getElementsByTagName("Valeur").item(0).getTextContent().equals("")) {
                                        throw new IOException();
                                    }
                                    description.setValeur(e.getElementsByTagName("Valeur").item(0).getTextContent());
                                    descriptions.add(description);
                                }
                                o.setDescriptions(descriptions);
                                p.add(o);
                            }
                            message.setObjetsProposed(p);
                            p = new ArrayList<>();
                            if (type.getElementsByTagName("Demande").item(0).getTextContent().equals("")) {
                                throw new IOException();
                            }
                            NodeList prop = type.getElementsByTagName("Demande").item(0).getChildNodes();
                            for (int i = 0; i < prop.getLength(); i++) {
                                objet = (Element) prop.item(i);
                                if (objet.getTextContent().equals("")) {
                                    throw new IOException();
                                }
                                if (objet.getElementsByTagName("NomObjet").item(0).getTextContent().equals("")) {
                                    throw new IOException();
                                }
                                o.setNom(objet.getElementsByTagName("NomObjet").item(0).getTextContent());
                                if (objet.getElementsByTagName("Type").item(0).getTextContent().equals("")) {
                                    throw new IOException();
                                }
                                o.setType(objet.getElementsByTagName("Type").item(0).getTextContent());
                                descriptions = new ArrayList<>();
                                parametres = objet.getElementsByTagName("Parametre");
                                for (int j = 0; j < parametres.getLength(); j++) {
                                    Element e = (Element) parametres.item(i);
                                    if (e.getTextContent().equals("")) {
                                        throw new IOException();
                                    }
                                    description = new Description();
                                    if (e.getElementsByTagName("Nom").item(0).getTextContent().equals("")) {
                                        throw new IOException();
                                    }
                                    description.setNom(e.getElementsByTagName("Nom").item(0).getTextContent());
                                    if (e.getElementsByTagName("Valeur").item(0).getTextContent().equals("")) {
                                        throw new IOException();
                                    }
                                    description.setValeur(e.getElementsByTagName("Valeur").item(0).getTextContent());
                                    descriptions.add(description);
                                }
                                o.setDescriptions(descriptions);
                                p.add(o);
                            }
                            message.setObjetsAsked(p);
                        }
                    }
                    this.fichier.getMessages().add(message);
                }
            }
            System.out.println("fichier conforme, insertion en base");
            if (!this.XmlABaseDeDonnee()) {
                throw new IOException();
            }
        } catch (ParserConfigurationException | SAXException | IOException | ParseException ex) {
            System.out.println("fichier non conforme : supprimé");
            try {
                Files.delete(xml.toPath());
            } catch (IOException ex1) {
                Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        System.out.println("Parsing terminé");
    }

    public boolean XmlABaseDeDonnee() { //true si l'insertion a fonctionne, false si il faut supprimer le fichier
        Personne personne = PersonneDAO.getPersonneWithName(this.fichier.getNomEm());
        ArrayList<Message> messages;
        int idDescritpion, idObjet;
        int idMessage;
        if (personne != null && !noAuth) {
            messages = (ArrayList<Message>) personne.getMessages();
            for (Message message : this.fichier.getMessages()) {
                if (!messages.contains(message)) {
                    idMessage = MessageDAO.insererMessage(message.getIdMessageParent(), message.getTypeMessage());
                    MessageDAO.associerMessagePersonne(idMessage, personne.getNumeroAuthorisation());
                    for (Objet o : message.getObjetsProposed()) {
                        idObjet = ObjetDAO.insererObjet(o.getNom(), o.getType());
                        for (Description d : o.getDescriptions()) {
                            idDescritpion = DescriptionDAO.insererDescription(d.getNom(), d.getValeur());
                            ObjetDAO.associerObjetDescription(idObjet, idDescritpion);
                        }
                        ObjetDAO.associerMessageDemande(message.getIdMessage(), idObjet);
                    }
                    for (Objet o : message.getObjetsAsked()) {
                        idObjet = ObjetDAO.insererObjet(o.getNom(), o.getType());
                        for (Description d : o.getDescriptions()) {
                            idDescritpion = DescriptionDAO.insererDescription(d.getNom(), d.getValeur());
                            ObjetDAO.associerObjetDescription(idObjet, idDescritpion);
                        }
                        ObjetDAO.associerMessageDon(message.getIdMessage(), idObjet);
                    }
                }
            }
        } else if (noAuth) {
            System.out.println("demande d'autorisation");
            String[] nomComplet = this.fichier.getNomEm().split(" ");
            String dateSignatureAutorisation = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            int idPersonne = PersonneDAO.insererPersonne(nomComplet[0], nomComplet[1], this.fichier.getMailExpediteur(), dateSignatureAutorisation);
            Message message = this.fichier.getMessages().get(0);
            if (message.getIdMessageParent() == 0) {
                idMessage = MessageDAO.insererMessage(null, message.getTypeMessage());
            }
            else{
                idMessage = MessageDAO.insererMessage(message.getIdMessageParent(), message.getTypeMessage());
            }
            MessageDAO.associerMessagePersonne(idMessage, idPersonne);
        } else {
            return false;
        }
        System.out.print("Insertion en base réussie");
        return true;
    }

}
