package pdn.Models;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "transaction")
public class Personne implements Serializable {
    
    @Id
    @Column(name = "numero_authorisation")
    private long numeroAuthorisation;
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy="id_message")
    private List<Message> messages;
    
    @Column(name = "date")
    private String date;
    
    @Column(name = "prenom")
    private String prenom;
    
    @Column(name = "email")
    private String email;
}
