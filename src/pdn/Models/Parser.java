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
            fichier.setIdFichier(document.getElementsByTagName("FicID").item(0).getNodeValue());
            fichier.setNomEm(document.getElementsByTagName("NmIE").item(0).getNodeValue());
            fichier.setNomRecepteur(document.getElementsByTagName("NmIR").item(0).getNodeValue());
            fichier.setNumAuthorisation(document.getElementsByTagName("NumAuto").item(0).getNodeValue());
            fichier.setSignatureAuthorisation((Date) new SimpleDateFormat("dd/MM/yyyy").parse(document.getElementsByTagName("DtOfSgtAuto").item(0).getNodeValue()));
            fichier.setDureeValidite(Integer.parseInt(document.getElementsByTagName("DureeValidAuto").item(0).getNodeValue()));
            fichier.setMailDestinataire(document.getElementsByTagName("MailDest").item(0).getNodeValue());
            fichier.setMailExpediteur(document.getElementsByTagName("MailExp").item(0).getNodeValue());
            fichier.setPathFichier(path);
            NodeList liste = document.getElementsByTagName("Message");
            ArrayList<Integer> listeI = new ArrayList<>();
            for(int i = 0; i < liste.getLength(); i++){
                listeI.add(Integer.parseInt(liste.item(i).getAttributes().getNamedItem("MsgId").toString()));
            }
            fichier.setMessagesId(listeI);
            Node dernierMessage = document.getElementsByTagName("Message").item(liste.getLength()-1);

        } catch (ParserConfigurationException | SAXException | IOException | ParseException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
