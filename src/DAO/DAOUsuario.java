package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import excecoes.UsuarioJaCadastrado;
import excecoes.UsuarioNaoCadastrado;
import java.sql.ResultSet;
import java.util.ArrayList;
import usuarios.Artista;
import usuarios.Assinante;
import usuarios.Banda;
import usuarios.Usuario;

public class DAOUsuario {
    private String idBanda = "0";
    
    public void inserir(Usuario u) throws SQLException, ClassNotFoundException, UsuarioJaCadastrado {
        Connection con;
        try {
            Usuario v2 = pesquisarPor(u.getIdentificador());
            throw new UsuarioJaCadastrado();
        } catch (UsuarioNaoCadastrado e) {
            con = Conexao.getConnection();
            String sql = "INSERT INTO usuarios(identificador,nome,email,senha,tipo,idBanda)"
                    + " VALUES(?,?,?,?,?,?)";
            PreparedStatement stmt = (PreparedStatement) con.prepareStatement(sql);

            stmt.setString(1, u.getIdentificador());
            stmt.setString(2, u.getNome());
            stmt.setString(3, u.getEmail());
            stmt.setString(4, u.getSenha());
            //-------------------------------------

            if (u instanceof Assinante) {
                stmt.setString(5, String.valueOf(1));
                stmt.setString(6, String.valueOf(0));
            } else if (u instanceof Artista) {
                stmt.setString(5, String.valueOf(2));
                stmt.setString(6, idBanda);
                idBanda = "0";
            } else if (u instanceof Banda) {
                Banda b = (Banda) u;
                stmt.setString(5, String.valueOf(3));
                stmt.setString(6, u.getIdentificador());
                for (Artista art : b.getComponentes()) {
                    this.idBanda = b.getIdentificador();
                    inserir(art);
                }
            }

            stmt.execute(); //executa comando   
            stmt.close();
        }
    }
    
    public void removerTodos() throws ClassNotFoundException, SQLException {
		Connection con = Conexao.getConnection();
        Statement st = con.createStatement();
    	    String cmd = "delete from usuarios"; 
    	    st.execute(cmd);
	}
    
    
    public void removerUsuario(String id) throws ClassNotFoundException, SQLException {
		Connection con = Conexao.getConnection();
        Statement st = con.createStatement();
    	    String cmd = "delete from usuarios where identificador = '"+id+"'"; 
    	    st.execute(cmd);
	}
    
    public void alterarID(String id1, String id2) throws SQLException {
		Connection con = Conexao.getConnection();
        Statement st = con.createStatement();
    	    String cmd = "UPDATE usuarios SET identificador = ? "
                               + " WHERE identificador = ?";
    	    
    	    PreparedStatement stmt = (PreparedStatement) con.prepareStatement(cmd);

    		stmt.setString(1, id2);
    		stmt.setString(2, id1);
    		
    		stmt.execute(); //executa comando   
    		stmt.close();
	}
	
    
    

    public Usuario pesquisarPor(String id) throws UsuarioNaoCadastrado {
        Connection con;
        Usuario u = null;
        try {
            con = Conexao.getConnection();
            Statement st = con.createStatement();
            String cmd = "select * from usuarios where identificador = '" + id+"'"; //idUsuario,nome,email,senha,tipo,idBanda
            ResultSet rs = st.executeQuery(cmd);
            if (rs.next()) {
                String  nome, email, senha, idBanda = "";
                int tipo;
                nome = rs.getString("nome");
                email = rs.getString("email");
                tipo = rs.getInt("tipo");
                senha = rs.getString("senha");
                idBanda = rs.getString("idBanda");
                ArrayList<Artista> arts = pesquisarBanda(idBanda);

                if (tipo == 1) {
                    u = new Assinante(id, nome, email, senha);
                } else if (tipo == 2) {
                    u = new Artista(id, nome, email, senha);
                } else if (tipo == 3) {
                    u = new Banda(id, nome, email, senha, arts);
                }

                return u;

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        throw new UsuarioNaoCadastrado();
    }

    public ArrayList<Artista>  pesquisarBanda(String idBanda) throws UsuarioNaoCadastrado {
        Connection con;
        Artista u = null;
        ArrayList<Artista> banda = new ArrayList<Artista>();
        try {
            con = Conexao.getConnection();
            Statement st = con.createStatement();
            String cmd = "select * from usuarios where idBanda = '" + idBanda+"' and tipo = " + 2; //idUsuario,nome,email,senha,tipo,idBanda
            ResultSet rs = st.executeQuery(cmd);
           while(rs.next()) {
                String id, nome, email, senha;
                int tipo;
                ArrayList<Artista> arts = new ArrayList<Artista>();
                id = rs.getString("identificador");
                nome = rs.getString("nome");
                email = rs.getString("email");
                tipo = Integer.parseInt(rs.getString("tipo"));
                senha = rs.getString("senha");
                idBanda = rs.getString("idBanda");

              
                u = new Artista(id, nome, email, senha);
               

                banda.add(u);
            }
           
           return banda;
           
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        throw new UsuarioNaoCadastrado();
    }

    public Usuario login(String email,String senha) throws UsuarioNaoCadastrado {
        Connection con;
        Usuario u = null;
        try {
            con = Conexao.getConnection();
            Statement st = con.createStatement();
            String cmd = "select * from usuarios where email = '" + email+"'" + " AND senha = '" + senha+"'"; //idUsuario,nome,email,senha,tipo,idBanda
            ResultSet rs = st.executeQuery(cmd);
            if (rs.next()) {
                String  nome, idBanda = "";
                int tipo;
                String id = rs.getString("identificador");
                nome = rs.getString("nome");
                email = rs.getString("email");
                tipo = Integer.parseInt(rs.getString("tipo"));
                senha = rs.getString("senha");
                idBanda = rs.getString("idBanda");
                ArrayList<Artista> arts = pesquisarBanda(idBanda);

                if (tipo == 1) {
                    u = new Assinante(id, nome, email, senha);
                } else if (tipo == 2) {
                    u = new Artista(id, nome, email, senha);
                } else if (tipo == 3) {
                    u = new Banda(id, nome, email, senha, arts);
                }

                return u;

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        throw new UsuarioNaoCadastrado();
    }

}
