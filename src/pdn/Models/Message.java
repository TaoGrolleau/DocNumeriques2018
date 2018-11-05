package pdn.Models;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "message")
public class Message implements Serializable {
    
    public static final String STATUT_NON_LU = "non_lu";
    public static final String STATUT_EN_ATTENTE = "en_attente";
    public static final String STATUT_CONTRE_PROPOSE = "contre_propose";
    public static final String STATUT_ACCEPTE = "accepte";
    
    @Id
    @Column(name = "id_message", unique = true, nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long idMessage;
    
    @OneToOne(fetch = FetchType.LAZY)
    @Column(name = "id_message_parent", nullable = true)
    private long idMessageParent;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_objet", nullable = true)
    @Column(name = "objet_demande", nullable = true)
    private Objet objetDemande;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_objet", nullable = true)
    @Column(name = "objet_donne", nullable = true)
    private Objet objetDonne;
    
    @Column(name = "statut", nullable = false)
    private String statut;
    
    public Message(){}
    
}
