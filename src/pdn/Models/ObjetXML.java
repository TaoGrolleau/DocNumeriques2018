/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdn.Models;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ObjetXML {

    String idFichier;
    String nomEm;
    String nomRecepteur;
    String numAuthorisation;
    Date signatureAuthorisation;
    int dureeValidite;
    String mailDestinataire;
    String mailExpediteur;

    String pathFichier;

    List<Message> messages;

    public ObjetXML() {
        messages = new ArrayList<>();
        this.setIdFichier(String.valueOf(Math.random()));
        
    }

    public static void CreateXmlFile(ObjetXML message) {

    }

    public static void CreateXmlHeader(ObjetXML message) {
        
        String header = "";
    }

    public static void CreateXmlMessage(ObjetXML message) {

    }

    public static void CreateXmlProp(ObjetXML message) {

    }

}
