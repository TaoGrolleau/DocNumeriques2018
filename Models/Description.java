package pdn.Models;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "description")
public class Description implements Serializable {

    @Id
    @Column(name = "id_description", unique = true, nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long idDescription;
    
    @Column(name = "nom")
    private String nom;
    
    @Column(name = "valeur")
    private String valeur;
    
    public Description(){}
    
}
