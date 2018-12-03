package pdn.Models;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import pdn.XMLFileFactory;

@Data
public class ObjetXML {

    private String idFichier;
    private String nomEm;
    private String nomRecepteur;
    private String numAuthorisation;
    private String signatureAuthorisation;
    private int dureeValidite;
    private String mailDestinataire;
    private String mailExpediteur;
    private String pathFichier;
    private List<Message> messages;

    public ObjetXML() {
        messages = new ArrayList<>();
        this.idFichier = "1";
        this.pathFichier = "./fichier.xml";
    }

    public void CreateXmlFile() throws FileNotFoundException, UnsupportedEncodingException {
        XMLFileFactory fileFactory = new XMLFileFactory();
        fileFactory.CreateXmlFile(this);
    }

}
