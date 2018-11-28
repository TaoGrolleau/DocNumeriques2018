package pdn.Models;

import java.io.Serializable;
import java.util.List;
import java.util.Date;
import lombok.Data;

@Data
public class Personne implements Serializable {
    
    private Integer numeroAuthorisation;
    private Date signatureAuthorisation;
    private List<Message> messages;
    private String date;
    private String nom;
    private String prenom;
    private String email;
    
    public Personne(){}
    
}
