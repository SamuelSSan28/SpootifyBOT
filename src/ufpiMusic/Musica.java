/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ufpiMusic;

import java.sql.Time;
import java.util.Date;

/**
 *
 * @author Samuel Santos
 */
public class Musica {
    private String nome;
    //private Artista art;
    private String idUsu;
    private Date data_de_lancamento;
    private int tempo_de_duracao;
    private String estilo,link;
        
    //idUsu  nomeMusica estilo link duracao data 
    public Musica(String idUsu,String nome, String estilo,String link, int duracao, Date lancamento) {
        this.idUsu = idUsu;
        this.nome = nome;
        this.data_de_lancamento = lancamento;
        this.tempo_de_duracao = duracao;
        this.estilo = estilo;
        this.link = link;
    }

    public String getIdUsu() {
        return idUsu;
    }

    public void setIdUsu(String idUsu) {
        this.idUsu = idUsu;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    

    public Date getData_de_lancamento() {
		return data_de_lancamento;
	}

	public void setData_de_lancamento(Date data_de_lancamento) {
		this.data_de_lancamento = data_de_lancamento;
	}

	public int getTempo_de_duracao() {
		return tempo_de_duracao;
	}

	public void setTempo_de_duracao(int tempo_de_duracao) {
		this.tempo_de_duracao = tempo_de_duracao;
	}

	public String getEstilo() {
        return estilo;
    }

    public void setEstilo(String estilo) {
        this.estilo = estilo;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
    
    
    
}
