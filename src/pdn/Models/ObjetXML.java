/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdn.Models;

import java.sql.Date;
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
    int numAuthorisation;
    Date signatureAuthorisation;
    int dureeValidite;
    String mailDestinataire;
    String mailExpediteur;
    
    List<Integer> messagesId;
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
    
    List<Integer> objetId;
    String nomObjet;
    String typeObjet;
    List<Integer> parametresId;
    String nomParam;
    String valeurParam;
    
    public ObjetXML(){
        
    }
    
    
}
