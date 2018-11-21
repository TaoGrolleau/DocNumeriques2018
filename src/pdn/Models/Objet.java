package pdn.Models;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class Objet implements Serializable {
    
    private long idObjet;
    private List<Description> descriptions;
    private String nom;
    private String type;
    
    public Objet(){}
    

}
