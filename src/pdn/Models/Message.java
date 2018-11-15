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
    
}
