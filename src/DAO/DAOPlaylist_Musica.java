package DAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import excecoes.MusicaNaoCadastrada;
import excecoes.PlaylistExistente;
import excecoes.PlaylistNaoExistente;
import excecoes.UsuarioNaoCadastrado;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import ufpiMusic.Musica;
import ufpiMusic.Playlist;
import usuarios.Usuario;

public class DAOPlaylist_Musica {

	public void inserir(String idUsu, String nomeLista, String nomeAutor, String nomeMusica,String estilo, int d) throws SQLException, ClassNotFoundException, PlaylistNaoExistente, UsuarioNaoCadastrado {
		Connection con;
		con = Conexao.getConnection();
		new DAOEstilos_PLaylists().inserir(idUsu, nomeLista, estilo);
		String sql = "INSERT INTO musicasplaylists(usuario,playlist,musica,artista)"
				+ " VALUES(?,?,?,?)";
		PreparedStatement stmt = (PreparedStatement) con.prepareStatement(sql);

		stmt.setString(1, idUsu);
		stmt.setString(2, nomeLista);
		stmt.setString(3, nomeMusica);
		stmt.setString(4, nomeAutor);

		stmt.execute(); //executa comando   
		stmt.close();
		
		new DAOPlaylist().alterarDuracao(idUsu, nomeLista, d);
		new DAOEstilos_PLaylists().inserir(idUsu, nomeLista, estilo);

}
	
	public  Musica pesquisarMusica_Playlist(String idUsu, String nomeLista,String nomeAutor, String nomeMusica) throws PlaylistNaoExistente, UsuarioNaoCadastrado, MusicaNaoCadastrada {
		Connection con;
		DAOMusica daoMusica = new DAOMusica();
		Musica mus = null;

	    con = Conexao.getConnection();
	    Statement st;
	    try {
	        st = con.createStatement();
	        String cmd = "select * from musicasplaylists where usuario = '" + idUsu + "'" + "AND playlist = '"+nomeLista +"'" + " AND musica = '" + nomeMusica + "'  AND artista = '" + nomeAutor+"'"; //idMusica,nome,email,senha,tipo,idBanda
	        ResultSet rs = st.executeQuery(cmd);
	        if (rs.next()) {           
	           
					mus = daoMusica.pesquisarPor(nomeAutor, nomeMusica);
				
				 return mus;           
	           
	        }
	       

	        
	    } catch (SQLException ex) {
	        Logger.getLogger(DAOMusica.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    	
	   throw new MusicaNaoCadastrada();
		
		
	}
	
	
	
	
	
	public ArrayList<Playlist> pesquisarPlaylistMusica(String nomeAutor, String nomeMusica) throws PlaylistNaoExistente, UsuarioNaoCadastrado, MusicaNaoCadastrada {
		Connection con;
		DAOPlaylist daoP = new DAOPlaylist();
		Playlist p = null;
		ArrayList<Playlist> plays = new ArrayList<Playlist>();
		
	    con = Conexao.getConnection();
	    Statement st;
	    try {
	        st = con.createStatement();
	        String cmd = "select * from musicasplaylists where  musica = '" + nomeMusica + "'  AND artista = '" + nomeAutor+"'"; //idMusica,nome,email,senha,tipo,idBanda
	        ResultSet rs = st.executeQuery(cmd);
	        if (rs.next()) {           
	        		String usu = rs.getString("usuario");
	        		String nomeP = rs.getString("playlist");
					p = daoP.pesquisarPor(usu, nomeP);
				
					plays.add(p);
	           
	        }
	       

	        
	    } catch (SQLException ex) {
	        Logger.getLogger(DAOMusica.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    	
	  return plays;
		
		
	}
	
	
	
	

public  ArrayList<Musica> retornarMusicas_Playlist(String idUsu, String nomeLista) throws PlaylistNaoExistente, UsuarioNaoCadastrado {
	Connection con;
	DAOMusica daoMusica = new DAOMusica();
    ArrayList<Musica> m = new ArrayList<Musica>();

    con = Conexao.getConnection();
    Statement st;
    try {
        st = con.createStatement();
        String cmd = "select * from musicasplaylists where usuario = '" + idUsu + "'" + "AND playlist = '"+nomeLista +"'"; //idMusica,nome,email,senha,tipo,idBanda
        ResultSet rs = st.executeQuery(cmd);
        while (rs.next()) {
            String nomeMusica, nomeAutor;
            nomeMusica = rs.getString("musica");
            nomeAutor = rs.getString("artista");
            Musica mus = null;
			try {
				mus = daoMusica.pesquisarPor(nomeAutor, nomeMusica);
			} catch (MusicaNaoCadastrada | UsuarioNaoCadastrado e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            m.add(mus);
           
        }
       

        
    } catch (SQLException ex) {
        Logger.getLogger(DAOMusica.class.getName()).log(Level.SEVERE, null, ex);
    }
    	
    return m;
	
	
}


public void removerTodos() throws ClassNotFoundException, SQLException {
	Connection con = Conexao.getConnection();
    Statement st = con.createStatement();
	    String cmd = "delete from musicasplaylists"; 
	    st.execute(cmd);
}

public void removerMusicaDeTodasPlaylists(String artista, String nome) throws ClassNotFoundException, SQLException {
	Connection con = Conexao.getConnection();
    Statement st = con.createStatement();
	    String cmd = "DELETE FROM musicasplaylists " + 
							"WHERE artista = '"+artista+ "' AND musica = '"+nome +"'"; 
	    st.execute(cmd);
	   
}




}
