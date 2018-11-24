package pdn.Models;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Parser {

    private ObjetXML fichier;

    public Parser() {

    }

    public void parsingFichier(String path) {
        fichier = new ObjetXML();
        DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
        DocumentBuilder constructeur;
        File xml = new File(path);
        try {
            constructeur = fabrique.newDocumentBuilder();
            Document document = (Document) constructeur.parse(xml);
            document.getDocumentElement().normalize();
            fichier.setIdFichier(document.getElementsByTagName("FicID").item(0).getTextContent());
            fichier.setNomEm(document.getElementsByTagName("NmIE").item(0).getTextContent());
            fichier.setNomRecepteur(document.getElementsByTagName("NmIR").item(0).getTextContent());
            fichier.setNumAuthorisation(document.getElementsByTagName("NumAuto").item(0).getTextContent());
            fichier.setSignatureAuthorisation((Date) new SimpleDateFormat("dd/MM/yyyy").parse(document.getElementsByTagName("DtOfSgtAuto").item(0).getTextContent()));
            fichier.setDureeValidite(Integer.parseInt(document.getElementsByTagName("DureeValidAuto").item(0).getTextContent()));
            fichier.setMailDestinataire(document.getElementsByTagName("MailDest").item(0).getTextContent());
            fichier.setMailExpediteur(document.getElementsByTagName("MailExp").item(0).getTextContent());
            fichier.setPathFichier(path);
            NodeList liste = document.getElementsByTagName("Message");
            Message message;
            Element messageEnCours;
            Element type;
            for (int k = 0; k < liste.getLength(); k++) {
                message = new Message();
                messageEnCours = (Element) liste.item(k);
                message.setIdMessage(Integer.parseInt(messageEnCours.getAttribute("MsgId")));
                if (messageEnCours.hasAttribute("ReponseA")) {
                    message.setIdMessageParent(Integer.parseInt(messageEnCours.getAttribute("ReponseA")));
                }
                message.setDateMessage((Date) new SimpleDateFormat("dd/mm/yyyy").parse(messageEnCours.getElementsByTagName("Dte").item(0).getTextContent()));
                message.setDureeValiditeMessage(Integer.parseInt(messageEnCours.getElementsByTagName("DureeValideMsg").item(0).getTextContent()));

                if (messageEnCours.getElementsByTagName("Prop").item(0) != null) {
                    message.setTypeMessage("Prop");
                    type = (Element) messageEnCours.getElementsByTagName("Prop").item(0);
                    message.setTitreProposition(type.getElementsByTagName("TitreP").item(0).getTextContent());
                    NodeList offre = type.getElementsByTagName("Offre").item(0).getChildNodes();
                    ArrayList<Objet> p = new ArrayList<>();
                    Objet o = new Objet();
                    Element objet;
                    ArrayList<Description> descriptions;
                    Description description;
                    NodeList parametres;// finir de traiter un message
                    for (int i = 0; i < offre.getLength(); i++) {
                        objet = (Element) offre.item(i);
                        o.setNom(objet.getElementsByTagName("NomObjet").item(0).getTextContent());
                        o.setType(objet.getElementsByTagName("Type").item(0).getTextContent());
                        descriptions = new ArrayList<>();
                        parametres = objet.getElementsByTagName("Parametre");
                        for (int j = 0; j < parametres.getLength(); j++) {
                            Element e = (Element) parametres.item(i);
                            description = new Description();
                            description.setNom(e.getElementsByTagName("Nom").item(0).getTextContent());
                            description.setValeur(e.getElementsByTagName("Valeur").item(0).getTextContent());
                            descriptions.add(description);
                        }
                        o.setDescriptions(descriptions);
                        p.add(o);
                    }
                    message.setObjetsProposed(p);
                    p = new ArrayList<>();

                    NodeList prop = type.getElementsByTagName("Demande").item(0).getChildNodes();
                    for (int i = 0; i < prop.getLength(); i++) {
                        objet = (Element) prop.item(i);
                        o.setNom(objet.getElementsByTagName("NomObjet").item(0).getTextContent());
                        o.setType(objet.getElementsByTagName("Type").item(0).getTextContent());
                        descriptions = new ArrayList<>();
                        parametres = objet.getElementsByTagName("Parametre");
                        for (int j = 0; j < parametres.getLength(); j++) {
                            Element e = (Element) parametres.item(i);
                            description = new Description();
                            description.setNom(e.getElementsByTagName("Nom").item(0).getTextContent());
                            description.setValeur(e.getElementsByTagName("Valeur").item(0).getTextContent());
                            descriptions.add(description);
                        }
                        o.setDescriptions(descriptions);
                        p.add(o);
                    }
                    message.setObjetsAsked(p);
                }
                if (messageEnCours.getElementsByTagName("Auth").item(0) != null) {
                    message.setTypeMessage("Auth");
                    type = (Element) messageEnCours.getElementsByTagName("Auth").item(0);
                    type = (Element) type.getElementsByTagName("Rep").item(0);
                    if (type.getElementsByTagName("AccAuth").item(0) != null) {
                        message.setAcceptAuthorisation(type.getElementsByTagName("AccAuth").item(0).getTextContent());
                    } else {
                        message.setAcceptAuthorisation(type.getElementsByTagName("RefAuth").item(0).getTextContent());
                    }
                }
                if (messageEnCours.getElementsByTagName("Dmd").item(0) != null) {
                    message.setTypeMessage("Dmd");
                    type = (Element) messageEnCours.getElementsByTagName("Dmd").item(0);
                    message.setDescriptionDemande(type.getElementsByTagName("DescDmd").item(0).getTextContent());
                    message.setDebutDemande((Date) new SimpleDateFormat("dd/MM/yyyy").parse(type.getElementsByTagName("DateDebut").item(0).getTextContent()));
                    message.setFinDemande((Date) new SimpleDateFormat("dd/MM/yyyy").parse(type.getElementsByTagName("DateFin").item(0).getTextContent()));
                }
                if (messageEnCours.getElementsByTagName("Accep").item(0) != null) {
                    message.setTypeMessage("Accep");
                    type = (Element) messageEnCours.getElementsByTagName("Accep").item(0);
                    if(type.getElementsByTagName("MessageValid").item(0) != null)
                        message.setMessageReponse(type.getElementsByTagName("MessageValid").item(0).getTextContent());
                    else{
                        type = (Element) messageEnCours.getElementsByTagName("Prop").item(0);
                    message.setTitreProposition(type.getElementsByTagName("TitreP").item(0).getTextContent());
                    NodeList offre = type.getElementsByTagName("Offre").item(0).getChildNodes();
                    ArrayList<Objet> p = new ArrayList<>();
                    Objet o = new Objet();
                    Element objet;
                    ArrayList<Description> descriptions;
                    Description description;
                    NodeList parametres;// finir de traiter un message
                    for (int i = 0; i < offre.getLength(); i++) {
                        objet = (Element) offre.item(i);
                        o.setNom(objet.getElementsByTagName("NomObjet").item(0).getTextContent());
                        o.setType(objet.getElementsByTagName("Type").item(0).getTextContent());
                        descriptions = new ArrayList<>();
                        parametres = objet.getElementsByTagName("Parametre");
                        for (int j = 0; j < parametres.getLength(); j++) {
                            Element e = (Element) parametres.item(i);
                            description = new Description();
                            description.setNom(e.getElementsByTagName("Nom").item(0).getTextContent());
                            description.setValeur(e.getElementsByTagName("Valeur").item(0).getTextContent());
                            descriptions.add(description);
                        }
                        o.setDescriptions(descriptions);
                        p.add(o);
                    }
                    message.setObjetsProposed(p);
                    p = new ArrayList<>();

                    NodeList prop = type.getElementsByTagName("Demande").item(0).getChildNodes();
                    for (int i = 0; i < prop.getLength(); i++) {
                        objet = (Element) prop.item(i);
                        o.setNom(objet.getElementsByTagName("NomObjet").item(0).getTextContent());
                        o.setType(objet.getElementsByTagName("Type").item(0).getTextContent());
                        descriptions = new ArrayList<>();
                        parametres = objet.getElementsByTagName("Parametre");
                        for (int j = 0; j < parametres.getLength(); j++) {
                            Element e = (Element) parametres.item(i);
                            description = new Description();
                            description.setNom(e.getElementsByTagName("Nom").item(0).getTextContent());
                            description.setValeur(e.getElementsByTagName("Valeur").item(0).getTextContent());
                            descriptions.add(description);
                        }
                        o.setDescriptions(descriptions);
                        p.add(o);
                    }
                    message.setObjetsAsked(p);
                    }
                }
                fichier.getMessages().add(message);
            }

        } catch (ParserConfigurationException | SAXException | IOException | ParseException ex) {
            xml.delete();
        }

    }
}
