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
        this.idFichier = "1";
        this.pathFichier = "./fichier.xml";
    }

    public void CreateXmlFile() throws FileNotFoundException, UnsupportedEncodingException {
        try (PrintWriter writer = new PrintWriter(pathFichier, "UTF-8")) {
            writer.println(CreateXmlHeader());
            writer.println(CreateXmlMessage(this));
            System.out.println(CreateXmlHeader() + CreateXmlMessage(this));
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
                + "<DtOfSgtAuto>" + signatureAuthorisation + "</DtOfSgtAuto>"
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
        for (Message mess : this.messages) {
            startmessage = startmessage + "<Message MsgId=" + mess.getIdMessage() + " ReponseA=" + mess.getIdMessageParent() + ">"
                    + "<Dte>" + mess.getDateMessage() + "</Dte>"
                    + "<DureeValideMsg>" + mess.getDureeValiditeMessage() + "</DureeValideMsg>";

            startmessage = startmessage + CreateXmlProp(mess);

            startmessage = startmessage + "</Message>";

            i++;
        }

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
                for (Objet o : m.getObjetsProposed()) {
                    result = result + "<NomObjet>" + o.getNom() + "</NomObjet>"
                            + "<Type>" + o.getType() + "</Type>"
                            + "<Description>";
                    for (Description desc : o.getDescriptions()) //À corriger
                    {
                        result = result + "<Parametre>"
                                + "<Nom>" + desc.getNom() + "</Nom>"
                                + "<Valeur>" + desc.getValeur() + "</Valeur>"
                                + "</Parametre>";
                    }
                }

                result = result + "</Description>";

                result = result + "</Offre><Demande>";
                for (Objet o : m.getObjetsProposed()) {
                    result = result + "<NomObjet>" + o.getNom() + "</NomObjet>"
                            + "<Type>" + o.getType() + "</Type>"
                            + "<Description>";
                    for (Description desc : o.getDescriptions()) {
                        //À corriger
                        result = result + "<Parametre>"
                                + "<Nom>" + desc.getNom() + "</Nom>"
                                + "<Valeur>" + desc.getValeur() + "</Valeur>"
                                + "</Parametre>";
                    }
                    result = result + "</Description>";
                }
                result = result + "</Demande></Prop>";
                break;
            //On est dans une Acceptation de prop

            case "Accep":
                //?
                result = result + "<Accep><MessageValid>Vu</MessageValid></Accep>";
                break;
            //On est dans une acceptation d'autorisation
            case "Auth":
                result = result + "<Auth><Rep>"
                        + m.getAcceptAuthorisation()
                        + m.getRefAuthorisation()
                        + "</Rep></Auth>";
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
