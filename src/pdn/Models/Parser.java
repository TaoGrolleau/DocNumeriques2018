package pdn.Models;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import pdn.dataAccess.DescriptionDAO;
import pdn.dataAccess.MessageDAO;
import pdn.dataAccess.ObjetDAO;
import pdn.dataAccess.PersonneDAO;

public class Parser {

    private ObjetXML fichier;
    private boolean noAuth;

    public Parser() {

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
            if (document.getElementsByTagName("FicID").item(0) != null) {
                this.fichier.setIdFichier(document.getElementsByTagName("FicID").item(0).getTextContent());
            } else {
                throw new IOException();
            }
            if (document.getElementsByTagName("NmIE").item(0) != null) {
                this.fichier.setNomEm(document.getElementsByTagName("NmIE").item(0).getTextContent());
            } else {
                throw new IOException();
            }
            if (document.getElementsByTagName("NmIR").item(0) != null) {
                this.fichier.setNomRecepteur(document.getElementsByTagName("NmIR").item(0).getTextContent());
            } else {
                throw new IOException();
            }
            if (document.getElementsByTagName("NumAuto").item(0) != null || Integer.parseInt(document.getElementsByTagName("NumAuto").item(0).getTextContent()) != 0) {
                this.fichier.setNumAuthorisation(document.getElementsByTagName("NumAuto").item(0).getTextContent());
            } else {
                this.noAuth = true;
            }
            if (document.getElementsByTagName("DureeValidAuto").item(0) != null && !this.noAuth) {
                this.fichier.setDureeValidite(Integer.parseInt(document.getElementsByTagName("DureeValidAuto").item(0).getTextContent()));
            } else if (!this.noAuth) {
                throw new IOException();
            }
            if (document.getElementsByTagName("MailDest").item(0) != null) {
                this.fichier.setMailDestinataire(document.getElementsByTagName("MailDest").item(0).getTextContent());
            } else {
                throw new IOException();
            }
            if (document.getElementsByTagName("MailExp").item(0) != null) {
                this.fichier.setMailExpediteur(document.getElementsByTagName("MailExp").item(0).getTextContent());
            } else {
                throw new IOException();
            }
            this.fichier.setPathFichier(path);
            NodeList liste = document.getElementsByTagName("Message");
            Element messageEnCours;
            Message message;
            Element type;
            if (this.noAuth) {
                message = new Message();
                messageEnCours = (Element) liste.item(0);
                if (messageEnCours.getElementsByTagName("Dte").item(0) != null) {
                    message.setDateMessage((Date) new SimpleDateFormat("dd/mm/yyyy").parse(messageEnCours.getElementsByTagName("Dte").item(0).getTextContent()));
                } else {
                    throw new IOException();
                }
                if (messageEnCours.getElementsByTagName("DureeValideMsg").item(0) != null) {
                    if (Integer.parseInt(messageEnCours.getElementsByTagName("DureeValideMsg").item(0).getTextContent()) > 93) {
                        throw new IOException();
                    }
                    message.setDureeValiditeMessage(Integer.parseInt(messageEnCours.getElementsByTagName("DureeValideMsg").item(0).getTextContent()));
                } else {
                    throw new IOException();
                }
                Calendar today = Calendar.getInstance();
                Calendar c = Calendar.getInstance();
                c.setTime(message.getDateMessage());
                c.add(Calendar.DATE, message.getDureeValiditeMessage());
                if (today.before(c)) {
                    throw new IOException();
                }
                if (messageEnCours.hasAttribute("MsgId")) {
                    message.setIdMessage(Integer.parseInt(messageEnCours.getAttribute("MsgId")));
                } else {
                    throw new IOException();
                }
                if (messageEnCours.getElementsByTagName("Dmd").item(0) != null) {
                    message.setTypeMessage("Dmd");
                    type = (Element) messageEnCours.getElementsByTagName("Dmd").item(0);
                    if (type.getElementsByTagName("DescDmd").item(0) != null) {
                        message.setDescriptionDemande(type.getElementsByTagName("DescDmd").item(0).getTextContent());
                    } else {
                        throw new IOException();
                    }
                    if (type.getElementsByTagName("DateDebut").item(0) != null) {
                        message.setDebutDemande((Date) new SimpleDateFormat("dd/MM/yyyy").parse(type.getElementsByTagName("DateDebut").item(0).getTextContent()));
                    } else {
                        throw new IOException();
                    }
                    if (type.getElementsByTagName("DateFin").item(0) != null) {
                        message.setFinDemande((Date) new SimpleDateFormat("dd/MM/yyyy").parse(type.getElementsByTagName("DateFin").item(0).getTextContent()));
                    } else {
                        throw new IOException();
                    }
                } else {
                    throw new IOException();
                }
                this.fichier.getMessages().add(message);
            } else {
                for (int k = 0; k < liste.getLength(); k++) {
                    message = new Message();
                    messageEnCours = (Element) liste.item(k);
                    if (!messageEnCours.hasAttribute("MsgId")) {
                        throw new IOException();
                    }
                    message.setIdMessage(Integer.parseInt(messageEnCours.getAttribute("MsgId")));
                    if (messageEnCours.hasAttribute("ReponseA")) {
                        message.setIdMessageParent(Integer.parseInt(messageEnCours.getAttribute("ReponseA")));
                    }
                    if (messageEnCours.getElementsByTagName("Dte").item(0) != null) {
                        message.setDateMessage((Date) new SimpleDateFormat("dd/mm/yyyy").parse(messageEnCours.getElementsByTagName("Dte").item(0).getTextContent()));
                    } else {
                        throw new IOException();
                    }
                    if (messageEnCours.getElementsByTagName("DureeValideMsg").item(0) != null) {
                        if (Integer.parseInt(messageEnCours.getElementsByTagName("DureeValideMsg").item(0).getTextContent()) > 93) {
                            throw new IOException();
                        }
                        message.setDureeValiditeMessage(Integer.parseInt(messageEnCours.getElementsByTagName("DureeValideMsg").item(0).getTextContent()));
                    } else {
                        throw new IOException();
                    }
                    if (messageEnCours.getElementsByTagName("Prop").item(0) != null) {
                        if (document.getElementsByTagName("DtOfSgtAuto").item(0) != null && !this.noAuth) {
                            this.fichier.setSignatureAuthorisation((Date) new SimpleDateFormat("dd/MM/yyyy").parse(document.getElementsByTagName("DtOfSgtAuto").item(0).getTextContent()));
                        } else if (!this.noAuth) {
                            throw new IOException();
                        }
                        message.setTypeMessage("Prop");
                        type = (Element) messageEnCours.getElementsByTagName("Prop").item(0);
                        message.setTitreProposition(type.getElementsByTagName("TitreP").item(0).getTextContent());
                        if (type.getElementsByTagName("Offre").item(0) == null) {
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
                            if (objet == null) {
                                throw new IOException();
                            }
                            if (objet.getElementsByTagName("NomObjet").item(0) == null) {
                                throw new IOException();
                            }
                            o.setNom(objet.getElementsByTagName("NomObjet").item(0).getTextContent());
                            if (objet.getElementsByTagName("Type").item(0) == null) {
                                throw new IOException();
                            }
                            o.setType(objet.getElementsByTagName("Type").item(0).getTextContent());
                            descriptions = new ArrayList<>();

                            parametres = objet.getElementsByTagName("Parametre");
                            for (int j = 0; j < parametres.getLength(); j++) {
                                Element e = (Element) parametres.item(i);
                                if (e == null) {
                                    throw new IOException();
                                }
                                description = new Description();
                                if (e.getElementsByTagName("Nom").item(0) == null) {
                                    throw new IOException();
                                }
                                description.setNom(e.getElementsByTagName("Nom").item(0).getTextContent());
                                if (e.getElementsByTagName("Valeur").item(0) == null) {
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

                        if (type.getElementsByTagName("Demande").item(0) == null) {
                            throw new IOException();
                        }
                        NodeList prop = type.getElementsByTagName("Demande").item(0).getChildNodes();
                        for (int i = 0; i < prop.getLength(); i++) {
                            objet = (Element) prop.item(i);
                            if (objet == null) {
                                throw new IOException();
                            }
                            if (objet.getElementsByTagName("NomObjet").item(0) == null) {
                                throw new IOException();
                            }
                            o.setNom(objet.getElementsByTagName("NomObjet").item(0).getTextContent());
                            if (objet.getElementsByTagName("Type").item(0) == null) {
                                throw new IOException();
                            }
                            o.setType(objet.getElementsByTagName("Type").item(0).getTextContent());
                            descriptions = new ArrayList<>();
                            parametres = objet.getElementsByTagName("Parametre");
                            for (int j = 0; j < parametres.getLength(); j++) {
                                Element e = (Element) parametres.item(i);
                                if (e == null) {
                                    throw new IOException();
                                }
                                description = new Description();
                                if (e.getElementsByTagName("Nom").item(0) == null) {
                                    throw new IOException();
                                }
                                description.setNom(e.getElementsByTagName("Nom").item(0).getTextContent());
                                if (e.getElementsByTagName("Valeur").item(0) == null) {
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
                    if (messageEnCours.getElementsByTagName("Auth").item(0) != null) {
                        if (document.getElementsByTagName("DtOfSgtAuto").item(0) != null && !this.noAuth) {
                            this.fichier.setSignatureAuthorisation((Date) new SimpleDateFormat("dd/MM/yyyy").parse(document.getElementsByTagName("DtOfSgtAuto").item(0).getTextContent()));
                        } else if (!this.noAuth) {
                            throw new IOException();
                        }
                        message.setTypeMessage("Auth");
                        type = (Element) messageEnCours.getElementsByTagName("Auth").item(0);
                        type = (Element) type.getElementsByTagName("Rep").item(0);
                        message.setAcceptAuthorisation(type.getElementsByTagName("AccAuth").item(0).getTextContent());

                        message.setAcceptAuthorisation(type.getElementsByTagName("RefAuth").item(0).getTextContent());

                    }
                    if (messageEnCours.getElementsByTagName("Dmd").item(0) != null) {
                        if (document.getElementsByTagName("DtOfSgtAuto").item(0) != null && !this.noAuth) {
                            this.fichier.setSignatureAuthorisation((Date) new SimpleDateFormat("dd/MM/yyyy").parse(document.getElementsByTagName("DtOfSgtAuto").item(0).getTextContent()));
                        } else if (!this.noAuth) {
                            throw new IOException();
                        }
                        message.setTypeMessage("Dmd");
                        type = (Element) messageEnCours.getElementsByTagName("Dmd").item(0);

                        message.setDescriptionDemande(type.getElementsByTagName("DescDmd").item(0).getTextContent());
                        if (type.getElementsByTagName("DateDebut").item(0) == null) {
                            throw new IOException();
                        }
                        message.setDebutDemande((Date) new SimpleDateFormat("dd/MM/yyyy").parse(type.getElementsByTagName("DateDebut").item(0).getTextContent()));
                        if (type.getElementsByTagName("DateFin").item(0) == null) {
                            throw new IOException();
                        }
                        message.setFinDemande((Date) new SimpleDateFormat("dd/MM/yyyy").parse(type.getElementsByTagName("DateFin").item(0).getTextContent()));
                    }
                    if (messageEnCours.getElementsByTagName("Accep").item(0) != null) {
                        if (document.getElementsByTagName("DtOfSgtAuto").item(0) != null && !this.noAuth) {
                            this.fichier.setSignatureAuthorisation((Date) new SimpleDateFormat("dd/MM/yyyy").parse(document.getElementsByTagName("DtOfSgtAuto").item(0).getTextContent()));
                        } else if (!this.noAuth) {
                            throw new IOException();
                        }
                        message.setTypeMessage("Accep");
                        type = (Element) messageEnCours.getElementsByTagName("Accep").item(0);
                        if (type.getElementsByTagName("MessageValid").item(0) != null) {
                            message.setMessageReponse(type.getElementsByTagName("MessageValid").item(0).getTextContent());
                        } else {
                            type = (Element) messageEnCours.getElementsByTagName("Prop").item(0);

                            message.setTitreProposition(type.getElementsByTagName("TitreP").item(0).getTextContent());
                            if (type.getElementsByTagName("Offre").item(0) == null) {
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
                                if (objet == null) {
                                    throw new IOException();
                                }
                                if (objet.getElementsByTagName("NomObjet").item(0) == null) {
                                    throw new IOException();
                                }
                                o.setNom(objet.getElementsByTagName("NomObjet").item(0).getTextContent());
                                if (objet.getElementsByTagName("Type").item(0) == null) {
                                    throw new IOException();
                                }
                                o.setType(objet.getElementsByTagName("Type").item(0).getTextContent());
                                descriptions = new ArrayList<>();
                                parametres = objet.getElementsByTagName("Parametre");
                                for (int j = 0; j < parametres.getLength(); j++) {
                                    Element e = (Element) parametres.item(i);
                                    if (e == null) {
                                        throw new IOException();
                                    }
                                    description = new Description();
                                    if (e.getElementsByTagName("Nom").item(0) == null) {
                                        throw new IOException();
                                    }
                                    description.setNom(e.getElementsByTagName("Nom").item(0).getTextContent());
                                    if (e.getElementsByTagName("Valeur").item(0) == null) {
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
                            if (type.getElementsByTagName("Demande").item(0) == null) {
                                throw new IOException();
                            }
                            NodeList prop = type.getElementsByTagName("Demande").item(0).getChildNodes();
                            for (int i = 0; i < prop.getLength(); i++) {
                                objet = (Element) prop.item(i);
                                if (objet == null) {
                                    throw new IOException();
                                }
                                if (objet.getElementsByTagName("NomObjet").item(0) == null) {
                                    throw new IOException();
                                }
                                o.setNom(objet.getElementsByTagName("NomObjet").item(0).getTextContent());
                                if (objet.getElementsByTagName("Type").item(0) == null) {
                                    throw new IOException();
                                }
                                o.setType(objet.getElementsByTagName("Type").item(0).getTextContent());
                                descriptions = new ArrayList<>();
                                parametres = objet.getElementsByTagName("Parametre");
                                for (int j = 0; j < parametres.getLength(); j++) {
                                    Element e = (Element) parametres.item(i);
                                    if (e == null) {
                                        throw new IOException();
                                    }
                                    description = new Description();
                                    if (e.getElementsByTagName("Nom").item(0) == null) {
                                        throw new IOException();
                                    }
                                    description.setNom(e.getElementsByTagName("Nom").item(0).getTextContent());
                                    if (e.getElementsByTagName("Valeur").item(0) == null) {
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
            if (!this.XmlABaseDeDonnee()) {
                throw new IOException();
            }
        } catch (ParserConfigurationException | SAXException | IOException | ParseException ex) {
            xml.delete();
        }

    }

    public boolean XmlABaseDeDonnee() { //true si l'insertion a fonctionne, false si il faut supprimer le fichier
        Personne personne = PersonneDAO.getPersonneWithName(this.fichier.getNomEm());
        ArrayList<Message> messages;
        int idDescritpion, idObjet;
        if (personne != null && !noAuth) {
            messages = (ArrayList<Message>) personne.getMessages();
            for (Message message : this.fichier.getMessages()) {
                if (!messages.contains(message)) {
                    MessageDAO.insererMessage(message.getIdMessage(), message.getIdMessageParent(), message.getTypeMessage());
                    MessageDAO.associerMessagePersonne(message.getIdMessage(), personne.getNumeroAuthorisation());
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
            String[] nomComplet = this.fichier.getNomEm().split(" ");
            int idPersonne = PersonneDAO.insererPersonne(nomComplet[0], nomComplet[1], this.fichier.getMailExpediteur(), this.fichier.getSignatureAuthorisation().toString());
            Message message = this.fichier.getMessages().get(0);
            MessageDAO.insererMessage(message.getIdMessage(), message.getIdMessageParent(), message.getTypeMessage());
            MessageDAO.associerMessagePersonne(message.getIdMessage(), idPersonne);
        } else {
            return false;
        }
        return true;
    }

}
