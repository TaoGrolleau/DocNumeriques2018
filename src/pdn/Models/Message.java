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
    List<Objet> objetsProposed;
    List<Objet> objetsAsked;
    private String statut;
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

    public Message() {
        objetsProposed = new ArrayList<>();
        objetsAsked = new ArrayList<>();
    }

}
