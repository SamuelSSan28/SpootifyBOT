package DAO;

import excecoes.EstiloNaoCadastrado;
import excecoes.MusicaJaCadastrada;
import excecoes.MusicaNaoCadastrada;
import excecoes.UsuarioNaoCadastrado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import ufpiMusic.Musica;
import usuarios.Artista;
import usuarios.Assinante;
import usuarios.Banda;
import usuarios.Usuario;

public class DAOMusica {
    public class Node {
        private String texto;
        private Musica m;  // left and right subtrees
        
        public String getTexto(){
            return texto;
        }
        public Musica getMusica(){
            return m;
        }
        public Node(Musica mus, String t) {
            this.m = mus;
            this.texto = t;
        }
    }   
    public void inserir(Musica m) throws SQLException, ClassNotFoundException, MusicaJaCadastrada, UsuarioNaoCadastrado {
        Connection con;
        try {
            Musica v2 = pesquisarPor(m.getIdUsu(), m.getNome());
            throw new MusicaJaCadastrada();
        } catch (MusicaNaoCadastrada e) {
            con = Conexao.getConnection();
            String sql = "INSERT INTO musicas (nome,artista,lancamento,duracao,link,estilo)"
                    + " VALUES(?,?,?,?,?,?)";
            PreparedStatement stmt = (PreparedStatement) con.prepareStatement(sql);
            
            stmt.setString(1, m.getNome());
            stmt.setString(2, m.getIdUsu());
            stmt.setString(3, String.valueOf(m.getData_de_lancamento().getTime()));
            stmt.setString(4, String.valueOf(m.getTempo_de_duracao()));
            stmt.setString(5, m.getLink());
            stmt.setString(6, m.getEstilo());
            

            stmt.execute(); //executa comando   
            stmt.close();
        }
    }

    public Musica pesquisarPor(String idUsu, String nome) throws MusicaNaoCadastrada, UsuarioNaoCadastrado {
        Connection con;
        Musica m = null;
        con = Conexao.getConnection();
        Statement st;
        try {
            st = con.createStatement();

            String cmd = "select * from musicas where artista = '" + idUsu + "' AND nome = '" + nome + "'"; //idMusica,nome,email,senha,tipo,idBanda
            ResultSet rs = st.executeQuery(cmd);
            if (rs.next()) {
                String lancamento, link = "", estilo = "";
                int duracao = 0;
                Date d = new Date();     
                
                estilo = rs.getString("estilo");
                link = rs.getString("link");
                duracao = rs.getInt("duracao");
                d.setTime(rs.getLong("lancamento"));
               
                m = new Musica(idUsu, nome,estilo,link, duracao,d);
                
                return m;
            }    
        } catch (SQLException ex) {
            Logger.getLogger(DAOMusica.class.getName()).log(Level.SEVERE, null, ex);
        }

        throw new MusicaNaoCadastrada();
    }
    
    public ArrayList<Musica> pesquisarPorEstilo(String estilo)  {
        Connection con;
        ArrayList<Musica> m = new ArrayList<Musica>();
        con = Conexao.getConnection();
        Statement st;
        try {
            st = con.createStatement();

            String cmd = "select * from musicas where estilo = '" + estilo + "'"; //idMusica,nome,email,senha,tipo,idBanda
            ResultSet rs = st.executeQuery(cmd);
            while (rs.next()) {
                String lancamento, link = "";
                int duracao = 0;
                Date d = new Date();     
                String idUsu, nome;
                
                idUsu = rs.getString("artista");
                nome  = rs.getString("nome");
                estilo = rs.getString("estilo");
                link = rs.getString("link");
                duracao = rs.getInt("duracao");
                d.setTime(rs.getLong("lancamento"));
               
                Musica mus = new Musica(idUsu, nome,estilo,link, duracao,d);
                
                m.add(mus);
               
            }

            
        } catch (SQLException ex) {
            Logger.getLogger(DAOMusica.class.getName()).log(Level.SEVERE, null, ex);
        }
        	
        return m;
        
    }
    
    public ArrayList<Musica> pesquisarPorData(Long data)  {
        Connection con;
        ArrayList<Musica> m = new ArrayList<Musica>();
        con = Conexao.getConnection();
        Statement st;
        try {
            st = con.createStatement();

            String cmd = "select * from musicas where lancamento > " + data ; //idMusica,nome,email,senha,tipo,idBanda
            ResultSet rs = st.executeQuery(cmd);
            while (rs.next()) {
                String lancamento, link = "";
                int duracao = 0;
                Date d = new Date(rs.getLong("lancamento"));     
                String idUsu, nome;
                String estilo;
                
                idUsu = rs.getString("artista");
                nome  = rs.getString("nome");
                estilo = rs.getString("estilo");
                link = rs.getString("link");
                duracao = rs.getInt("duracao");
               
               
                Musica mus = new Musica(idUsu, nome,estilo,link, duracao,d);
                
                m.add(mus);
               
            }
            
            	

            
        } catch (SQLException ex) {
            Logger.getLogger(DAOMusica.class.getName()).log(Level.SEVERE, null, ex);
        }
        return m;
    }
    
    public ArrayList<Musica> pesquisarPorArtista(String idA)  {
        Connection con;
        ArrayList<Musica> m = new ArrayList<Musica>();
        con = Conexao.getConnection();
        Statement st;
        try {
            st = con.createStatement();

            String cmd = "select * from musicas where artista = '" + idA+"'"; //idMusica,nome,email,senha,tipo,idBanda
            ResultSet rs = st.executeQuery(cmd);
            while (rs.next()) {
                String lancamento, link = "";
                int duracao = 0;
                Date d = new Date();     
                String idUsu, nome;
                String estilo;
                
                idUsu = rs.getString("artista");
                nome  = rs.getString("nome");
                estilo = rs.getString("estilo");
                link = rs.getString("link");
                duracao = rs.getInt("duracao");
                d.setTime(rs.getLong("lancamento"));
               
                Musica mus = new Musica(idUsu, nome,estilo,link, duracao,d);
                
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
    	    String cmd = "delete from musicas"; 
    	    st.execute(cmd);
	}
    
    public void removerMusicaPorEstilo(String estilo) throws ClassNotFoundException, SQLException {
		Connection con = Conexao.getConnection();
        Statement st = con.createStatement();
    	    String cmd = "DELETE FROM musicas " + 
								"WHERE estilo = '"+estilo+ "'"; 
    	    st.execute(cmd);
	}
    
    
    
    public ArrayList<Node> listarMusicas() throws MusicaNaoCadastrada, UsuarioNaoCadastrado {
        Connection con;
        Musica m = null;
        con = Conexao.getConnection();
        Statement st;
        int ordem = 1;
        String lista = "Lista de Musicas:\n ";
         ArrayList<Node> nodes = new ArrayList<>();
        try {
            st = con.createStatement();

            String cmd = "select * from musicas "; //idMusica,nome,email,senha,tipo,idBanda
            ResultSet rs = st.executeQuery(cmd);
            while(rs.next()) {
                String lancamento, link = "", estilo = "";
                int duracao = 0;
                Date d = new Date();     
                String idUsu =rs.getString("artista");
                String artista =  new DAOUsuario().pesquisarPor(idUsu).getNome();
                String nome = rs.getString("nome");
                
                
                estilo = rs.getString("estilo");
                link = rs.getString("link");
                duracao = rs.getInt("duracao");
                d.setTime(rs.getLong("lancamento"));
                Musica mus = new Musica(idUsu, nome,estilo,link, duracao,d);
                lista = ordem + ". " + nome + " -- " +artista+" \n" ;
               Node n = new Node(mus, lista);
                ordem++;
                nodes.add(n);
            }    
        } catch (SQLException ex) {
            Logger.getLogger(DAOMusica.class.getName()).log(Level.SEVERE, null, ex);
        }

        return nodes;
    }
    

}
