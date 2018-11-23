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
import org.w3c.dom.Node;
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
        try {
            constructeur = fabrique.newDocumentBuilder();
            File xml = new File(path);
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
            ArrayList<Integer> listeI = new ArrayList<>();
            for (int i = 0; i < liste.getLength(); i++) {
                listeI.add(Integer.parseInt(liste.item(i).getAttributes().getNamedItem("MsgId").getTextContent()));
            }
            fichier.setMessagesId(listeI);
            Element dernierMessage = (Element) document.getElementsByTagName("Message").item(liste.getLength() - 1);
            fichier.setRefMessage(Integer.parseInt(dernierMessage.getAttributes().getNamedItem("ReponseA").getTextContent()));
            fichier.setDateMessage((Date) new SimpleDateFormat("dd/MM/yyyy").parse(dernierMessage.getElementsByTagName("Dte").item(0).getTextContent()));
            fichier.setDureeValiditeMessage(Integer.parseInt(dernierMessage.getElementsByTagName("DureeValidMsg").item(0).getTextContent()));
            Element type;//faire un for pour tout les messages, pas seulement le dernier
            if (dernierMessage.getElementsByTagName("Prop").item(0) != null) {
                fichier.setTypeMessage("Prop");
                type = (Element) dernierMessage.getElementsByTagName("Prop").item(0);
                fichier.setMessageReponse(type.getElementsByTagName("TitreP").item(0).getTextContent());
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
                    for(int j=0; j < parametres.getLength();j++){
                        Element e = (Element) parametres.item(i);
                        description = new Description();
                        description.setNom(e.getElementsByTagName("Nom").item(0).getTextContent());
                        description.setValeur(e.getElementsByTagName("Valeur").item(0).getTextContent());
                        descriptions.add(description);
                    }
                    o.setDescriptions(descriptions);
                    p.add(o);
                }
                
                NodeList prop = type.getElementsByTagName("Demande").item(0).getChildNodes();;
             for (int i = 0; i < prop.getLength(); i++) {
                    objet = (Element) prop.item(i);
                    o.setNom(objet.getElementsByTagName("NomObjet").item(0).getTextContent());
                    o.setType(objet.getElementsByTagName("Type").item(0).getTextContent());
                    descriptions = new ArrayList<>();
                    parametres = objet.getElementsByTagName("Parametre");
                    for(int j=0; j < parametres.getLength();j++){
                        Element e = (Element) parametres.item(i);
                        description = new Description();
                        description.setNom(e.getElementsByTagName("Nom").item(0).getTextContent());
                        description.setValeur(e.getElementsByTagName("Valeur").item(0).getTextContent());
                        descriptions.add(description);
                    }
                    o.setDescriptions(descriptions);
                    p.add(o);
                }
            }
            if (dernierMessage.getElementsByTagName("Auth").item(0) != null) {
                fichier.setTypeMessage("Auth");
                type = (Element) dernierMessage.getElementsByTagName("Auth").item(0);
            }
            if (dernierMessage.getElementsByTagName("Dmd").item(0) != null) {
                fichier.setTypeMessage("Dmd");
                type = (Element) dernierMessage.getElementsByTagName("Dmd").item(0);
            }
            if (dernierMessage.getElementsByTagName("Accep").item(0) != null) {
                fichier.setTypeMessage("Accep");
                type = (Element) dernierMessage.getElementsByTagName("Accep").item(0);
            }

        } catch (ParserConfigurationException | SAXException | IOException | ParseException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
