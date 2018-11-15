package pdn.Models;

import java.io.Serializable;

public class Description implements Serializable {

    private long idDescription;
    private String nom;
    private String valeur;
    
    public Description(){}
    
    public long getIdDescription(){
        return idDescription;
    }
    public void setIdDescription(long id){
        this.idDescription = id;
    }
    
    public String getNom(){
        return nom;
    }
    public void setNom(String nom){
        this.nom = nom;
    }
    
    public String getValeur(){
        return valeur;
    }
    public void setValeur(String v){
        this.valeur = v;
    }
}
