/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usuarios;

import java.util.ArrayList;

/**
 *
 * @author Samuel Santos
 */
public class Banda extends Usuario{
    ArrayList<Artista> componentes = new ArrayList<Artista>();

    public ArrayList<Artista> getComponentes() {
        return componentes;
    }
    
    public Banda(String identificador, String nome, String email, String senha,ArrayList<Artista> componentes) {
        super(identificador, nome, email, senha);
        this.componentes = componentes;
    }

    
    
}
