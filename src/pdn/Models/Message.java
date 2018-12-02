package pdn.Models;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class Message implements Serializable {

    public static final String STATUT_NON_LU = "non_lu";
    public static final String STATUT_EN_ATTENTE = "en_attente";
    public static final String STATUT_CONTRE_PROPOSE = "contre_propose";
    public static final String STATUT_ACCEPTE = "accepte";

    private long idMessage;
    private long idMessageParent;
    private List<Objet> objetsProposed;
    private List<Objet> objetsAsked;
    private String statut;
    private Date dateMessage;
    private int dureeValiditeMessage;
    private String typeMessage;
    private String AcceptAuthorisation;
    private String RefAuthorisation;
    private String MessageReponse;
    private String descriptionDemande;
    private Date debutDemande;
    private Date finDemande;
    private String titreProposition;

    public Message() {
        objetsProposed = new ArrayList<>();
        objetsAsked = new ArrayList<>();
    }

}
