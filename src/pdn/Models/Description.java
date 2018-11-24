package pdn.Models;

import java.io.Serializable;
import lombok.Data;

@Data
public class Description implements Serializable {

    private long idDescription;
    private String nom;
    private String valeur;
    
    public Description(){}
    
    @Override
    public String toString(){
        return this.nom + " " + this.valeur;
    }

}
