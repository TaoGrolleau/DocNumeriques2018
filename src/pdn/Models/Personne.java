package pdn.Models;

import java.io.Serializable;
import java.util.List;

public class Personne implements Serializable {
    
    private long numeroAuthorisation;
    
    private List<Message> messages;
    
    private String date;
    
    private String prenom;
    
    private String email;
}
