package pdn;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import pdn.Models.Description;
import pdn.Models.Message;
import pdn.Models.Objet;
import pdn.Models.ObjetXML;

public class XMLFileFactory {

    public void CreateXmlFile(ObjetXML objetXML) throws FileNotFoundException, UnsupportedEncodingException {
        try (PrintWriter writer = new PrintWriter(objetXML.getPathFichier(), "UTF-8")) {
            writer.println(CreateXmlHeader(objetXML));
            writer.println(CreateXmlMessage(objetXML));
            System.out.println(CreateXmlHeader(objetXML) + CreateXmlMessage(objetXML));
        }
    }

    public String CreateXmlHeader(ObjetXML objetXML) {
        String header = "<!DOCTYPE fichier>"
                + "<fichier>"
                + "<Header>"
                + "<FicID>" + objetXML.getIdFichier() + "</FicID>"
                + "<NmIE>" + objetXML.getNomEm() + "</NmIE>"
                + "<NmIR>" + objetXML.getNomRecepteur() + "</NmIR>"
                + "<NumAuto>" + objetXML.getNumAuthorisation() + "</NumAuto>"
                + "<DtOfSgtAuto>" + objetXML.getSignatureAuthorisation() + "</DtOfSgtAuto>"
                + "<DureeValidAuto>" + objetXML.getDureeValidite() + "</DureeValidAuto>"
                + "<MailDest>" + objetXML.getMailDestinataire() + "</MailDest>"
                + "<MailExp>" + objetXML.getMailExpediteur() + "</MailExp>"
                + "</Header>"
                + "<Body>";
        return header;
    }

    public String CreateXmlMessage(ObjetXML message) {
        int i = 0;
        String startmessage = "<CollMess NbOfTxs=\"" + message.getMessages().size() + "\">";
        for (Message mess : message.getMessages()) {
            startmessage = startmessage + "<Message MsgId=\"" + mess.getIdMessage() + "\" ReponseA=\"" + mess.getIdMessageParent() + "\">"
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

        switch (m.getTypeMessage()) {
            case "Je veux donner des objets":
            case "Je veux demander des objets":
            case "Je veux faire un troc d'objets":
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
                for (Objet o : m.getObjetsAsked()) {
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
            case "Demande D'authorisation":
                //On est dans une demande d'autorisation
                result = result + "<Dmd>"
                        + "<DescDmd>" + m.getDescriptionDemande() + "</DescDmd>"
                        + "<DateDebut>" + m.getDebutDemande() + "</DateDebut>"
                        + "<DateFin>" + m.getFinDemande() + "</DateFin>"
                        + "</Dmd>";
                break;
            default:
                break;
        }
        return result;
    }
}
