/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdn.Models;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Celine
 */

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
    
    ArrayList<Integer> messagesId;
    int refMessage;
    Date dateMessage;
    int dureeValiditeMessage;
    String typeMessage;
    
        
    String AcceptAuthorisation;
    String RefAuthorisation;
  
    String MessageReponse;
    
    String descriptionDemande;
    Date debutDemande;
    Date finDemande;
    String titreProposition;
    
    List<Proposition> propositions;
    
    public ObjetXML(){
        messagesId = new ArrayList<>();
    }
 
    public static void CreateXmlFile(ObjetXML message) {
        
    }
    
}
