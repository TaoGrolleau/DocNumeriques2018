package pdn.Models;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "objet")
public class Objet implements Serializable {
    
    @Id
    @Column(name = "id_objet", unique = true, nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long idObjet;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_description", nullable = false)
    @Column(name = "id_description")
    private long idDescription;
    
    @Column(name = "nom")
    private String nom;
    
    @Column(name = "type")
    private String type;
    
    public Objet(){}
}
