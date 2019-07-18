/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import excecoes.EstiloJaCadastrado;
import excecoes.EstiloNaoCadastrado;
import excecoes.MusicaJaCadastrada;
import excecoes.MusicaNaoCadastrada;
import ufpiMusic.Musica;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Samuel Santos
 */
public class DAOEstilos {
	
	public void inserir(String e) throws EstiloJaCadastrado {
        Connection con;
        try {
            String v2 = pesquisarEstilo(e);
            throw new EstiloJaCadastrado();
        } catch (EstiloNaoCadastrado err) {
            con = Conexao.getConnection();
            String sql = "INSERT INTO estilos (nome)"
                    + " VALUES(?)";
            PreparedStatement stmt;
			try {
				stmt = (PreparedStatement) con.prepareStatement(sql);
				stmt.setString(1, e);
	            
	            stmt.execute(); //executa comando   
	            stmt.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
                        
        }
    }
	
	public void removerTodos() throws ClassNotFoundException, SQLException {
		Connection con = Conexao.getConnection();
        Statement st = con.createStatement();
    	    String cmd = "delete from estilos"; 
    	    st.execute(cmd);
	}
	
	public void removerEstilo(String nome) throws ClassNotFoundException, SQLException, EstiloNaoCadastrado {
		this.pesquisarEstilo(nome);
		
		Connection con = Conexao.getConnection();
        Statement st = con.createStatement();
    	    String cmd = "DELETE FROM estilos " + 
    	    					"WHERE nome = '"+nome+ "'";
    	    st.execute(cmd);
	}


    public String pesquisarEstilo(String nome) throws EstiloNaoCadastrado {
        Connection con;
        String e = "";

        con = Conexao.getConnection();
        Statement st;
        try {
            st = con.createStatement();

            String cmd = "select * from estilos where nome = '" + nome + "'"; //idMusica,nome,email,senha,tipo,idBanda
            ResultSet rs = st.executeQuery(cmd);
            if (rs.next()) {
            	e = rs.getString("nome");
                
            	return e;
            }

        } catch (SQLException ex) {
            Logger.getLogger(DAOMusica.class.getName()).log(Level.SEVERE, null, ex);
        }

        throw new EstiloNaoCadastrado();
    }
}
