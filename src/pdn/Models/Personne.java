package pdn.Models;

import java.io.Serializable;
import java.util.List;
import java.util.Date;
import lombok.Data;

@Data
public class Personne implements Serializable {
    
    public static final String PRENOM = "Eudes";
    public static final String NOM = "Delabuissonniere";
    public static final String EMAIL = "eudes.delabuissonniere@gmail.com";
    public static final String NOM_GLOBAL = "Eudes Delabuissonniere";
    
    private Integer numeroAuthorisation;
    private String signatureAuthorisation;
    private List<Message> messages;
    private String date;
    private String nom;
    private String prenom;
    private String email;
    
    public Personne(){}
    
    @Override
    public String toString(){
        return this.nom + " " + this.prenom;
    }
    
}
