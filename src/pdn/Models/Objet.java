package pdn.Models;

import java.io.Serializable;
import java.util.List;

public class Objet implements Serializable {
    
    private long idObjet;
    private List<Description> descriptions;
    private String nom;
    private String type;
    
    public Objet(){}
    
    public long getIdObjet(){
        return idObjet;
    }
    public void setIdObjet(long id){
        this.idObjet = id;
    }
    
    public List<Description> getDescriptions(){
        return descriptions;
    }
    public void setDescription(List<Description> d){
        this.descriptions = d;
    }
    
    public String getNom(){
        return nom;
    }
    public void setNom(String n){
        this.nom = n;
    }
    
    public String getType(){
        return type;
    }
    public void setType(String t){
        this.type = t;
    } 
}
