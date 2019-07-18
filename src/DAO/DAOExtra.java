package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import ufpiMusic.Playlist;

public class DAOExtra {
	
	public void alterarDuracaoPlaylists(ArrayList<Playlist> plays, int d) throws SQLException {
		
		for (Playlist playlist : plays) {
			new DAOPlaylist().alterarDuracao(playlist.getIdUsu(), playlist.getNome(),d*(-1) );
		}
	}

}
