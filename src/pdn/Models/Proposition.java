/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdn.Models;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Celine
 */
@Data
public class Proposition {
    List<Objet> objetsProposed;
    List<Objet> objetsAsked;
    
    public Proposition(){
        objetsProposed = new ArrayList<>();
        objetsAsked = new ArrayList<>();
    }
    
}
