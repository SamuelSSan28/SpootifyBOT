/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usuarios;



import java.util.ArrayList;
import ufpiMusic.Playlist;

/**
 *
 * @author Samuel Santos
 */
public abstract class Usuario {
    private String  identificador;
    private String nome, email, senha;
    private ArrayList<Playlist> biblioteca = new ArrayList<Playlist>();

    public Usuario(String identificador, String nome, String email, String senha) {
        this.identificador = identificador;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }
    

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    public void addPlaylist(Playlist p){
        this.biblioteca.add(p);
    }
    
}
