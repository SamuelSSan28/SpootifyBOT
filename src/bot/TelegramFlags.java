
package bot;

import DAO.DAOMusica;
import DAO.DAOPlaylist;
import bot.menu.MenuManager;
import java.util.ArrayList;
import usuarios.Usuario;


public class TelegramFlags {
    long telegramId;

    public TelegramFlags(long telegramId) {
        this.telegramId = telegramId;
    }
    int local = 0  ;
    Usuario usu;
    String email,senha;
    String altera;
    int iM, iP;
    boolean login = false;
    ArrayList<DAOMusica.Node> listaMusicas = null;
    ArrayList<DAOPlaylist.Nodi> listaPlaylists = null;
    
}
