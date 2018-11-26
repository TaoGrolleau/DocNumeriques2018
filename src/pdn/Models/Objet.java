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
    
    @Override
    public String toString(){
        return "Nom Objet: "+ this.nom + ", Type: " + this.type + ", Paramètres :" + this.descriptions;
    }

}
