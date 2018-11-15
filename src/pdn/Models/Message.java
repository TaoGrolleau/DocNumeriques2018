package pdn.Models;

import java.io.Serializable;

public class Message implements Serializable {
    
    public static final String STATUT_NON_LU = "non_lu";
    public static final String STATUT_EN_ATTENTE = "en_attente";
    public static final String STATUT_CONTRE_PROPOSE = "contre_propose";
    public static final String STATUT_ACCEPTE = "accepte";
    
    private long idMessage;
    private long idMessageParent;
    private Objet objetDemande;
    private Objet objetDonne;
    private String statut;
    
    public Message(){}
    
    public long getIdMessage(){
        return idMessage;
    }
    public void setIdMessage(long id){
        this.idMessage = id;
    }
    
    public long getIdMessageParent(){
        return idMessageParent;
    }
    public void setIdMessageParent(long id){
        this.idMessageParent = id;
    }
    
    public Objet getObjetDemande(){
        return objetDemande;
    }
    public void setObjetDemande(Objet o){
        this.objetDemande = o;
    }
    
    public Objet getObjetDonne(){
        return objetDonne;
    }
    public void setObjetDonne(Objet o){
        this.objetDonne = o;
    }
    
    public String getStatut(){
        return statut;
    }
    public void setStatut(String s){
        this.statut = s;
    } 
}
