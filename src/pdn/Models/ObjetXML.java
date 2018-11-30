/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdn.Models;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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

    public void CreateXmlFile(ObjetXML message) throws FileNotFoundException, UnsupportedEncodingException {
        try (PrintWriter writer = new PrintWriter(pathFichier, "UTF-8")) {
            writer.println(CreateXmlHeader());
            writer.println(CreateXmlMessage(message));
            System.out.println(CreateXmlHeader()+CreateXmlMessage(message));
        }
    }

    public String CreateXmlHeader() {
        String header = "<!DOCTYPE fichier SYSTEM \"./Groupe_dtd.dtd\">"
                + "<fichier>"
                + "<Header>"
                + "<FicID>" + idFichier + "</FicID>"
                + "<NmIE>" + nomEm + "</NmIE>"
                + "<NmIR>" + nomRecepteur + "</NmIR>"
                + "<NumAuto>" + numAuthorisation + "</NumAuto>"
                + "<DtOfSgtAuto>" + signatureAuthorisation.toString() + "</DtOfSgtAuto>"
                + "<DureeValidAuto>" + dureeValidite + "</DureeValidAuto>"
                + "<MailDest>" + mailDestinataire + "</MailDest>"
                + "<MailExp>" + mailExpediteur + "</MailExp>"
                + "</Header>"
                + "<Body>";
        return header;
    }

    public String CreateXmlMessage(ObjetXML message) {
        int i = 0;
        String startmessage = "<CollMess NbOfTxs=" + messages.size() + ">";
        do {
            startmessage = startmessage + "<Message MsgId=" + messages.get(i).getIdMessage() + ">"
                    + "<Dte>" + messages.get(i).getDateMessage() + "</Dte>"
                    + "<DureeValideMsg>" + messages.get(i).getDureeValiditeMessage() + "</DureeValideMsg>";

            startmessage = startmessage + CreateXmlProp(messages.get(i));

            startmessage = startmessage + "</Message>";

            i++;
        } while (messages.get(i) != null);

        return startmessage + "</CollMess></Body></fichier>";

    }

    public String CreateXmlProp(Message m) {
        int i = 0;
        String result = "";

        switch (m.typeMessage) {
            case "Prop":
                //On est dans une proposition
                result = result + "<Prop>"
                        + "<TitreP>" + m.getTitreProposition() + "</TitreP>"
                        + "<Offre>";
                do {
                    result = result + "<NomObjet>" + m.getObjetsProposed().get(i).getNom() + "</NomObjet>"
                            + "<Type>" + m.getObjetsProposed().get(i).getType() + "</Type>"
                            + "<Description>";
                    do {
                        //À corriger
                        result = result + "<Parametre>"
                                + "<Nom>" + m.getObjetsProposed().get(i).getDescriptions().toString() + "</Nom>"
                                + "<Valeur>" + m.getObjetsProposed().get(i).getDescriptions().toString() + "</Valeur>"
                                + "</Parametre>";
                    } while (m.getObjetsProposed().get(i).getDescriptions() != null);

                    result = result + "</Description>";
                } while (m.getObjetsProposed().get(i) != null);
                result = result + "</Offre><Demande>";
                do {
                    result = result + "<NomObjet>" + m.getObjetsProposed().get(i).getNom() + "</NomObjet>"
                            + "<Type>" + m.getObjetsProposed().get(i).getType() + "</Type>"
                            + "<Description>";
                    do {
                        //À corriger
                        result = result + "<Parametre>"
                                + "<Nom>" + m.getObjetsProposed().get(i).getDescriptions().toString()/*Care*/ + "</Nom>"
                                + "<Valeur>" + m.getObjetsProposed().get(i).getDescriptions().toString()/*Care*/ + "</Valeur>"
                                + "</Parametre>";
                    } while (m.getObjetsProposed().get(i).getDescriptions() != null);
                    result = result + "</Description>";
                } while (m.getObjetsAsked().get(i) != null);
                result = result + "</Demande></Prop>";
                break;
            //On est dans une Acceptation de prop
            case "Accep":
                //?
                result = result +"";
                break;
            //On est dans une acceptation d'autorisation
            case "Auth":
                result = result + "<Auth>"
                        + m.getAcceptAuthorisation()
                        + m.getRefAuthorisation()
                        + "</Auth>";
                break;
            case "Dmd":
                //On est dans une demande d'autorisation
                result = result + "<Dmd>"
                        + "<DescDmd>" + m.getDescriptionDemande() + "</DescDmd>"
                        + "<DateDebut>" + m.getDebutDemande().toString() + "</DateDebut>"
                        + "<DateFin>" + m.getFinDemande().toString() + "</DateFin>";
                break;
            default:
                break;
        }
        return result;
    }

}
