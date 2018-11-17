package pdn.Models;

import java.io.Serializable;
import java.util.List;

public class Personne implements Serializable {
    
    private Integer numeroAuthorisation;
    private List<Message> messages;
    private String date;
    private String nom;
    private String prenom;
    private String email;
    
    public Personne(){}
    
    public Integer getNumeroAuthoristion(){
        return numeroAuthorisation;
    }
    public void setNumeroAuthorisation(Integer num){
        this.numeroAuthorisation = num;
    }
    
    public List<Message> getMessages(){
        return messages;
    }
    public void setMessages(List<Message> m){
        this.messages = m;
    }
    
    public String getDate(){
        return date;
    }
    public void setDate(String date){
        this.date = date;
    }
    
    public String getNom(){
        return nom;
    }
    public void setNom(String nom){
        this.nom = nom;
    }
    
    public String getPrenom(){
        return prenom;
    }
    public void setPrenom(String prenom){
        this.prenom = prenom;
    }
    
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    
}
