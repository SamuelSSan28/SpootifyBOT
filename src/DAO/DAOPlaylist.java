package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import excecoes.PlaylistExistente;
import excecoes.PlaylistNaoExistente;
import excecoes.UsuarioNaoCadastrado;
import ufpiMusic.Musica;
import ufpiMusic.Playlist;

public class DAOPlaylist {

    public class Nodi{

        private String texto;
        private Playlist p;  // left and right subtrees

        public String getTexto() {
            return texto;
        }
        
        public String getMusicas() {
            String musicas = "Lista de Musicas:\n";
            int ordem = 1;
            for (Musica m : p.getListaMusicas()) {
                musicas += (ordem++)+". " + m.getNome()+"\n";
            }         
            return musicas;
        }

        public Playlist getPlay() {
            return p;
        }

        public Nodi(Playlist play, String t) {
            this.p = play;
            this.texto = t;
        }
    }

    public void inserir(Playlist c) throws SQLException, ClassNotFoundException, PlaylistExistente {
        Connection con;
        try {
            Playlist p = pesquisarPor(c.getIdUsu(), c.getNome());
            throw new PlaylistExistente();
        } catch (PlaylistNaoExistente e1) {
            con = Conexao.getConnection();
            String sql = "INSERT INTO playlists(usuario,nome,duracao)"
                    + " VALUES(?,?,?)";
            PreparedStatement stmt = (PreparedStatement) con.prepareStatement(sql);

            stmt.setString(1, c.getIdUsu());
            stmt.setString(2, c.getNome());
            stmt.setInt(3, 0);
            stmt.execute();
        }
    }

    public void alterarDuracao(String id, String nome, int d) throws SQLException {
        Connection con = Conexao.getConnection();
        Statement st = con.createStatement();
        String cmd = "UPDATE playlists SET duracao = duracao + ? "
                + " WHERE usuario = ?  AND nome = ?";

        PreparedStatement stmt = (PreparedStatement) con.prepareStatement(cmd);

        stmt.setInt(1, d);
        stmt.setString(2, id);
        stmt.setString(3, nome);

        stmt.execute(); //executa comando   
        stmt.close();
    }

    public void moverPlaylists(String id1, String id2) throws SQLException {
        Connection con = Conexao.getConnection();
        Statement st = con.createStatement();
        String cmd = "UPDATE playlists SET usuario = ? "
                + " WHERE usuario = ?";

        PreparedStatement stmt = (PreparedStatement) con.prepareStatement(cmd);

        stmt.setString(1, id2);
        stmt.setString(2, id1);

        stmt.execute(); //executa comando   
        stmt.close();
        
    }

    public void removerTodos() throws ClassNotFoundException, SQLException {
        Connection con = Conexao.getConnection();
        Statement st = con.createStatement();
        String cmd = "delete from playlists";
        st.execute(cmd);
    }

    public void removerPlaylist(String id, String nome) throws ClassNotFoundException, SQLException {
        Connection con = Conexao.getConnection();
        Statement st = con.createStatement();
        String cmd = "delete from playlists where usuario = '" + id + "' AND nome = '" + nome + "'";
        st.execute(cmd);
    }
    
    public void alterarPlaylist(String id, String nome,String novoNome) throws ClassNotFoundException, SQLException {
        Connection con = Conexao.getConnection();
        Statement st = con.createStatement();
        String cmd = "UPDATE playlists SET nome = ? "
                               + " WHERE nome = ?";
    	    
    	    PreparedStatement stmt = (PreparedStatement) con.prepareStatement(cmd);

    		stmt.setString(1, novoNome);
    		stmt.setString(2, nome);
    		
    		stmt.execute(); //executa comando   
    		stmt.close();
    }
    
    public void moverMusicas_Playlists(String nome, String novoNome) throws SQLException {
		Connection con = Conexao.getConnection();
        Statement st = con.createStatement();
    	    String cmd = "UPDATE musicasplaylists SET playlist = ? "
                               + " WHERE playlist = ?";
    	    
    	    PreparedStatement stmt = (PreparedStatement) con.prepareStatement(cmd);

    		stmt.setString(1, novoNome);
    		stmt.setString(2, nome);
    		
    		stmt.execute(); //executa comando   
    		stmt.close();
	}
    

    public Playlist pesquisarPor(String idUsu, String nomeP) throws PlaylistNaoExistente {
        Connection con;
        DAOMusica daoM = new DAOMusica();
        try {
            con = Conexao.getConnection();
            Statement st = con.createStatement();
            String cmd = "select * from playlists where usuario = '" + idUsu + "' AND nome = '" + nomeP + "'";
            ResultSet rs = st.executeQuery(cmd);
            if (rs.next()) {
                String n = rs.getString("nome");
                int dur = rs.getInt("duracao");
                DAOPlaylist_Musica daoPM = new DAOPlaylist_Musica();
                ArrayList<Musica> m = daoPM.retornarMusicas_Playlist(idUsu, n);
                Playlist c = new Playlist(idUsu, n);
                c.setListaMusicas(m);
                c.setDuracaoTotal(dur);

                return c;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        throw new PlaylistNaoExistente();
    }
    
    public ArrayList<Nodi> listarPlays(String id) throws PlaylistNaoExistente, UsuarioNaoCadastrado {
        Connection con;
        Playlist p = null;
        con = Conexao.getConnection();
        Statement st;
        int ordem = 1;
        String lista = "Lista de Musicas:\n ";
         ArrayList<Nodi> nodes = new ArrayList<>();
        try {
            st = con.createStatement();

            String cmd = "select * from playlists where usuario = '"+id+"'"; //idMusica,nome,email,senha,tipo,idBanda
            ResultSet rs = st.executeQuery(cmd);
            while(rs.next()) {
                String nome = rs.getString("nome");
                
               
                Playlist c = new Playlist(id, nome);
                c.setListaMusicas(new DAOPlaylist_Musica().retornarMusicas_Playlist(id, nome));
                lista = ordem + ". " + nome +"\n" ;
                Nodi n = new Nodi(c, lista);
                
                ordem++;
                nodes.add(n);
            }    
        } catch (SQLException ex) {
           // Logger.getLogger(DAOMusica.class.getName()).log(Level.SEVERE, null, ex);
        }

        return nodes;
    }

}
