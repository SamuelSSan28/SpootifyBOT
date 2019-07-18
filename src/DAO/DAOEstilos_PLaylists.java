package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import excecoes.MusicaNaoCadastrada;
import excecoes.PlaylistNaoExistente;
import excecoes.UsuarioNaoCadastrado;
import ufpiMusic.Musica;
import ufpiMusic.Playlist;
import usuarios.Usuario;

public class DAOEstilos_PLaylists {

	public void inserir(String idUsu, String nomeLista, String estilo) throws SQLException, ClassNotFoundException, UsuarioNaoCadastrado {
		Connection con;
		con = Conexao.getConnection();
		
		
		try {
			pesquisarPlaylists( idUsu,  nomeLista,  estilo);
		} catch (PlaylistNaoExistente e) {
			String sql = "INSERT INTO estilosplaylists(usuario,playlist,estilo)"
					+ " VALUES(?,?,?)";
			PreparedStatement stmt = (PreparedStatement) con.prepareStatement(sql);

			stmt.setString(1, idUsu);
			stmt.setString(2, nomeLista);
			stmt.setString(3, estilo);

			stmt.execute(); //executa comando   
			stmt.close();
		}
		
		


}
	
	
	public  Playlist pesquisarPlaylists(String id, String nomeLista, String estilo) throws PlaylistNaoExistente, UsuarioNaoCadastrado {
		Connection con;
		ArrayList<Playlist> plays = new ArrayList<Playlist>();
		DAOPlaylist daoP = new DAOPlaylist();
	    con = Conexao.getConnection();
	    Statement st;
	    try {
	        st = con.createStatement();
	        String cmd = "select * from estilosplaylists where usuario = '"+id+"' AND playlist  = '"+ nomeLista+"' AND  estilo = '" + estilo +"'"; //idMusica,nome,email,senha,tipo,idBanda
	        ResultSet rs = st.executeQuery(cmd);
	        if (rs.next()) {
	        	String idUsu = rs.getString("usuario");
	        	String nomeP = rs.getString("playlist");
	        	Playlist p = daoP.pesquisarPor(idUsu, nomeP);
	        	
	        	return p;
	           
	        }
	       
	        
	        
	    } catch (SQLException ex) {
	        Logger.getLogger(DAOMusica.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    	
	    throw new PlaylistNaoExistente();
		
		
	}
	
	
	
	public  ArrayList<Playlist> pesquisarEstilo_Playlists(String estilo) throws PlaylistNaoExistente, UsuarioNaoCadastrado {
		Connection con;
		ArrayList<Playlist> plays = new ArrayList<Playlist>();
		DAOPlaylist daoP = new DAOPlaylist();
	    con = Conexao.getConnection();
	    Statement st;
	    try {
	        st = con.createStatement();
	        String cmd = "select * from estilosplaylists where estilo = '" + estilo +"'"; //idMusica,nome,email,senha,tipo,idBanda
	        ResultSet rs = st.executeQuery(cmd);
	        while (rs.next()) {
	        	String idUsu = rs.getString("usuario");
	        	String nomeP = rs.getString("playlist");
	        	Playlist p = daoP.pesquisarPor(idUsu, nomeP);
	        	
	        	plays.add(p);
	           
	        }
	       
	        
	        
	    } catch (SQLException ex) {
	        Logger.getLogger(DAOMusica.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    	
	    return plays;
		
		
	}
	
	
	 public void removerTodos() throws ClassNotFoundException, SQLException {
			Connection con = Conexao.getConnection();
	        Statement st = con.createStatement();
	    	    String cmd = "delete from estilosplaylists"; 
	    	    st.execute(cmd);
		}
	 
	 public void removerEstiloPlaylist(String nomeEstilo) throws ClassNotFoundException, SQLException {
			Connection con = Conexao.getConnection();
	        Statement st = con.createStatement();
	    	    String cmd = "delete from estilosplaylists where estilo = '"+ nomeEstilo  +"'"; 
	    	    st.execute(cmd);
		}
	
	
	
	
	
	
}
