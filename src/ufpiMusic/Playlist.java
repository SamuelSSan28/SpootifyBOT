/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ufpiMusic;

import java.util.ArrayList;

/**
 *
 * @author Samuel Santos
 */
public class Playlist {
    private String idUsu;
    private ArrayList<Musica>listaMusicas = new ArrayList<Musica>();
    private String nome;
    private ArrayList<String> listaEstilos = new ArrayList<String>();
    private int duracaoTotal = 0;

    public Playlist(String id,String nome) {
        this.idUsu = id;
        this.nome = nome;
    }

    public String getIdUsu() {
        return idUsu;
    }

    public void setIdUsu(String idUsu) {
        this.idUsu = idUsu;
    }

    public ArrayList<Musica> getPlaylist() {
        return listaMusicas;
    }

    public void setListaMusicas(ArrayList<Musica> list) {
        for (Musica musica : list) {
			this.listaMusicas.add(musica);
		}
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    

    public void setListaEstilos(ArrayList<String> listaEstilos) {
        this.listaEstilos = listaEstilos;
    }

    public int getDuracaoTotal() {
        return this.duracaoTotal;
    }

    
    public ArrayList<Musica> getListaMusicas() {
		return listaMusicas;
	}

	public ArrayList<String> getListaEstilos() {
		return listaEstilos;
	}

	public void setDuracaoTotal(int duracaoTotal) {
		this.duracaoTotal = duracaoTotal;
	}

	public void addMusica(Musica m){
        listaMusicas.add(m);
        listaEstilos.add(m.getEstilo());
        //setDuracaoTotal();
    }
        
}
