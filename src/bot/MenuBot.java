package bot;

import DAO.DAOMusica;
import DAO.DAOMusica.Node;
import DAO.DAOPlaylist;
import DAO.DAOPlaylist.Nodi;
import DAO.DAOPlaylist_Musica;
import DAO.DAOUsuario;
import bot.menu.MenuManager;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import bot.accessories.InlineKeyboardBuilder;
import com.sun.corba.se.impl.activation.CommandHandler;
import excecoes.MusicaNaoCadastrada;
import excecoes.PlaylistExistente;
import excecoes.PlaylistNaoExistente;
import excecoes.UsuarioJaCadastrado;
import excecoes.UsuarioNaoCadastrado;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.telegram.telegrambots.api.objects.Message;
import ufpiMusic.Playlist;
import usuarios.Assinante;
import usuarios.Usuario;

/**
 * Created by bvn13 on 21.02.2018.
 */
public class MenuBot extends TelegramLongPollingBot {

    private int local = 0;
    /**
     * 1 - user 2 - senha 3 - Cadastrar Playlist 4 - Cadastrar Playlist
     *
     */
    private String user, senha;
    private String nomeP;
    private MenuManager menuManager = new MenuManager();
    private MenuManager menuL = new MenuManager();
    private Usuario usu;
    private int iM, iP;
    boolean login = false;
    ArrayList<Node> listaMusicas = null;
    ArrayList<Nodi> listaPlaylists = null;
    ArrayList<TelegramFlags> usuariosBot = new ArrayList<TelegramFlags>();

    public void init() {
        menuManager.setColumnsCount(2);

        menuManager.addMenuItem("Login", "login");
        menuManager.addMenuItem("Novo Usuario", "cadastrar");
        menuL.addMenuItem("Criar Playlist", "criarP");
        menuL.addMenuItem("Adicionar Musica à Playlist", "addP");
        menuL.addMenuItem("Listar Playlists", "listarP");
        menuL.addMenuItem("Alterar nome da Playlist", "alterarP");
        menuL.addMenuItem("Excluir Playlist", "excluirP");
        menuL.addMenuItem("Logout", "logout");

        menuManager.init();
        menuL.init();
    }

    private void replaceMessageWithText(long chatId, long messageId, String text) {
        EditMessageText newMessage = new EditMessageText()
                .setChatId(chatId)
                .setMessageId(Math.toIntExact(messageId))
                .setText(text);
        try {
            execute(newMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void replaceMessage(long chatId, long messageId, SendMessage message) {
        EditMessageText newMessage = new EditMessageText()
                .setChatId(chatId)
                .setMessageId(Math.toIntExact(messageId))
                .setText(message.getText())
                .setReplyMarkup((InlineKeyboardMarkup) message.getReplyMarkup());
        try {
            execute(newMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void mensagem(Update u, String msg) {
        SendMessage send = new SendMessage();
        send.setChatId(u.getMessage().getChatId());
        if (msg.equals("") || msg == null) {
            msg = "Algo errado!!";
        }
        send.setText(msg);
        try {
            execute(send);
        } catch (TelegramApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void mensagem(Long chat, String msg) {
        SendMessage send = new SendMessage();
        send.setChatId(chat);
        send.setText(msg);
        try {
            execute(send);
        } catch (TelegramApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    int retornaLocal(Long chat) {

        for (TelegramFlags t : usuariosBot) {
            if (t.telegramId == chat) {
                return t.local;
            }
        }

        TelegramFlags novo = new TelegramFlags(chat);
        usuariosBot.add(novo);

        return 0;
    }
    
    
    String retornaiP(Long chat) {

        for (TelegramFlags t : usuariosBot) {
            if (t.telegramId == chat) {
                return t.altera;
            }
        }

        return "";
    }

    boolean retornaLogin(Long chat) {

        for (TelegramFlags t : usuariosBot) {
            if (t.telegramId == chat) {
                return t.login;
            }
        }

        return false;
    }

    Usuario retornaUsuario(Long chat) {

        for (TelegramFlags t : usuariosBot) {
            if (t.telegramId == chat) {
                return t.usu;
            }
        }

        return null;
    }

    void mudaLocal(Long chat, int local) {

        for (TelegramFlags t : usuariosBot) {
            if (t.telegramId == chat) {
                t.local = local;
            }
        }

    }
    
    void mudaAltera(Long chat, String altera) {

        for (TelegramFlags t : usuariosBot) {
            if (t.telegramId == chat) {
                t.altera = altera;
            }
        }

    }

    String retornaSenha(Long chat) {

        for (TelegramFlags t : usuariosBot) {
            if (t.telegramId == chat) {
                return t.senha;
            }
        }

        return null;
    }

    void mudaEmail(Long chat, String email) {

        for (TelegramFlags t : usuariosBot) {
            if (t.telegramId == chat) {
                t.email = email;
            }
        }

    }

    String retornaEmail(Long chat) {

        for (TelegramFlags t : usuariosBot) {
            if (t.telegramId == chat) {
                return t.email;
            }
        }

        return null;
    }

    void mudaSenha(Long chat, String s) {

        for (TelegramFlags t : usuariosBot) {
            if (t.telegramId == chat) {
                t.senha = s;
            }
        }

    }

    void mudaLogin(Long chat, boolean login) {

        for (TelegramFlags t : usuariosBot) {
            if (t.telegramId == chat) {
                t.login = login;
            }
        }

    }

    void mudaUsuario(Long chat, Usuario u) {

        for (TelegramFlags t : usuariosBot) {
            if (t.telegramId == chat) {
                t.usu = u;
            }
        }

    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            Long now = update.getMessage().getChatId();
            local = retornaLocal(now);

            if (local == 1) {
                user = update.getMessage().getText();
                mudaEmail(now, user);
                mensagem(update, "Informe sua senha: ");
                mudaLocal(now, 2);
            }else if (local == 2) {
                try {
                    senha = update.getMessage().getText();
                    mudaSenha(now, senha);
                    System.out.println("User: " + retornaEmail(now) + " Senha: " + retornaSenha(now));
                    Usuario u = new DAOUsuario().login(retornaEmail(now), retornaSenha(now));
                    mudaUsuario(now, u);
                    mensagem(update, "Logado com Sucesso !!");
                    mensagem(update, "Bem Vindo(a) " + u.getNome() + " \n Digite '/menu' para ver as opções ");
                    mudaLogin(now, true);
                    mudaLocal(now, 20);
                } catch (UsuarioNaoCadastrado ex) {
                    mensagem(update, "ERRO: email ou senha invalidos !!");
                    mensagem(update, "Digite '/menu' para tentar novamente");
                    mudaLocal(now, 0);
                }
            } else if (local == 3) {
                nomeP = update.getMessage().getText();
                usu = retornaUsuario(now);
                System.out.println("USuario add play: " + usu.getIdentificador());
                Playlist p = new Playlist(usu.getIdentificador(), nomeP);

                try {
                    new DAOPlaylist().inserir(p);
                    mensagem(update, "Playlist Cadastrada com Sucesso !!");
                    mensagem(update, "Digite '/menu' para voltar as opções");
                    mudaLocal(now, 0);
                } catch (SQLException ex) {
                    Logger.getLogger(MenuBot.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(MenuBot.class.getName()).log(Level.SEVERE, null, ex);
                } catch (PlaylistExistente ex) {
                    mensagem(update, "ERRO: Playlist Existente !!");
                    mensagem(update, "Digite '/menu' para voltar as opções");
                    mudaLocal(now, 0);
                }

                mudaLocal(now, 0);
            } else if (local == 4) {
                iM = Integer.parseInt(update.getMessage().getText());
                usu = retornaUsuario(now);
                if (iM < 1 || iM > listaMusicas.size()) {
                    mensagem(now, "Valor Invalido");
                } else {
                    String a = listarPlaylists(usu.getIdentificador());
                    System.out.println("IdPlay: " + usu.getIdentificador());
                    if (listaPlaylists.size() > 0) {
                        mensagem(update, a);
                        mensagem(update, "Escolha a Playlist: ");
                        mudaLocal(now, 5);
                    } else {
                        mensagem(update, "Nenhuma Playlist Cadastrada!!\nVolte para o '/menu' para criar uma playlist");
                        mudaLocal(now, 0);
                    }
                }
            } else if (local == 5) {
                iP = Integer.parseInt(update.getMessage().getText());
                usu = retornaUsuario(now);
                if (iP < 1 || iP > listaPlaylists.size()) {
                    mensagem(now, "Valor Invalido");
                } else {
                    try {
                        new DAOPlaylist_Musica().inserir(usu.getIdentificador(), listaPlaylists.get(iP - 1).getPlay().getNome(), listaMusicas.get(iM - 1).getMusica().getIdUsu(),
                                listaMusicas.get(iM - 1).getMusica().getNome(), listaMusicas.get(iM - 1).getMusica().getEstilo(), listaMusicas.get(iM - 1).getMusica().getTempo_de_duracao());
                    } catch (SQLException ex) {
                        Logger.getLogger(MenuBot.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(MenuBot.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (PlaylistNaoExistente ex) {
                        System.out.println("Deu errado 1");
                        mudaLocal(now, 20);
                    } catch (UsuarioNaoCadastrado ex) {
                        System.out.println("Deu errado 2");
                        mudaLocal(now, 20);
                    }
                    mensagem(update, "Musica Adicionada com sucesso!! ");
                    mensagem(update, "Digite '/menu' para voltar as opções");
                    mudaLocal(now, 0);
                }
            }else if (local == 7) {
                iP = Integer.parseInt(update.getMessage().getText());
                usu = retornaUsuario(now);
                try {
                    new DAOPlaylist().removerPlaylist(usu.getIdentificador(), listaPlaylists.get(iP - 1).getPlay().getNome());
                } catch (SQLException ex) {
                    Logger.getLogger(MenuBot.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(MenuBot.class.getName()).log(Level.SEVERE, null, ex);
                }

                mensagem(update, "Playlist Removida com sucesso!! ");
                mensagem(update, "Digite '/menu' para voltar as opções");
                mudaLocal(now, 0);

            }else if (local == 8) {
                iP = Integer.parseInt(update.getMessage().getText());    
                mudaAltera(now,listaPlaylists.get(iP - 1).getPlay().getNome());
                mensagem(update, "Informe o novo Nome: ");
                mudaLocal(now, 9);

            }else if (local == 9) {
                String novoNome =  update.getMessage().getText();
                usu = retornaUsuario(now);
                String antigo = retornaiP(now);
                try {
                    new DAOPlaylist().alterarPlaylist(usu.getIdentificador(),antigo,novoNome);
                } catch (SQLException ex) {
                    Logger.getLogger(MenuBot.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(MenuBot.class.getName()).log(Level.SEVERE, null, ex);
                }

                mensagem(update, "Playlist Alterada com sucesso!! ");
                mensagem(update, "Digite '/menu' para voltar as opções");
                mudaLocal(now, 0);

            }else if (local == 10) {
                iP = Integer.parseInt(update.getMessage().getText());
                usu = retornaUsuario(now);
                if (iP == 0) {
                    mudaLocal(now, 0);
                    mensagem(update, "Digite '/menu' para voltar as opções");
                } else if (iP < 1 || iP > listaPlaylists.size()) {
                    mensagem(now, "Valor Invalido");
                } else {
                    String a = listarMusicasPlaylist(listaPlaylists.get(iP - 1).getPlay().getNome());
                    mensagem(now, a);
                    mensagem(update, "Digite '/menu' para voltar as opções");
                    mudaLocal(now, 0);
                }

            }else  if (local == 11) {
                user = update.getMessage().getText();
                mudaEmail(now, user);
                mensagem(update, "Informe sua senha: ");
                mudaLocal(now, 12);
                
            }else  if (local == 12) {
                senha = update.getMessage().getText();
                user = retornaEmail(now);
                String nome  =update.getMessage().getChat().getFirstName();
                String i = String.valueOf(update.getMessage().getChatId());
                Usuario u = new Assinante(i, nome, user,  senha);
                try {
                    new DAOUsuario().inserir(u);
                } catch (SQLException ex) {
                    Logger.getLogger(MenuBot.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(MenuBot.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UsuarioJaCadastrado ex) {
                    mensagem(update, "Este smartphone já está cadastrado !!!!");
                    mensagem(update, "Digite '/menu' para tentar novamente !!!");
                    mudaLocal(now, 0);
                    return;
                }
                mensagem(update, "Usuario Cadatrado com sucesso !!!");
                mudaLocal(now, 0);
            }

            if (update.getMessage().getText().equals("/start") || update.getMessage().getText().equals("Olá") || update.getMessage().getText().equals("Oi")) {
                mensagem(update, "Olá :) \n Digite '/menu' para entrar no Spootify ");
                mudaLocal(now, 0);
            } else if (update.getMessage().getText().equals("/menu") ) {
                long chatId = update.getMessage().getChatId();

                // lets render the menu
                InlineKeyboardBuilder builder = menuManager.createMenuForPage(0, true);
                if (retornaLogin(chatId)) {
                    builder = menuL.createMenuForPage(0, true);
                }

                builder.setChatId(chatId).setText("Escolha uma Opção:");
                SendMessage message = builder.build();

                try {
                    // Send the message
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            } else if (local == 0){
                mensagem(update, "Não Entendi");
            }

        } else if (update.hasCallbackQuery()) {

            // Set variables
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            String callData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            //local = retornaLocal(chatId);

            // here will be menu buttons callbacks
            if (callData.equals(MenuManager.CANCEL_ACTION)) {
                replaceMessageWithText(chatId, messageId, "Canceledo");
            }
            if (callData.equals("login")) {
                replaceMessageWithText(chatId, messageId, "Informe seu login: ");
                mudaLocal(chatId, 1);
            }
            if (callData.equals("cadastrar")) {
                replaceMessageWithText(chatId, messageId, "Informe seu email: ");
                mudaLocal(chatId, 11);
            }
            if (callData.equals("criarP")) {
                replaceMessageWithText(chatId, messageId, "Informe o nome da Playlist");
                mudaLocal(chatId, 3);
            }
            if (callData.equals("addP")) {
                String listaMusicas = listarMusicas();
                replaceMessageWithText(chatId, messageId, listaMusicas);
                mensagem(chatId, "Escolha uma Musica: ");
                mudaLocal(chatId, 4);
            }
            if (callData.equals("excluirP")) {
                usu = retornaUsuario(chatId);
                String listaPlaylits = listarPlaylists(usu.getIdentificador());
                replaceMessageWithText(chatId, messageId,listaPlaylits );
                mensagem(chatId, "Escolha a Playlist: ");  
                mudaLocal(chatId, 7);
            }
             if (callData.equals("alterarP")) {
                usu = retornaUsuario(chatId);
                String listaPlaylits = listarPlaylists(usu.getIdentificador());
                mensagem(chatId, "Escolha a Playlist: ");
                replaceMessageWithText(chatId, messageId, listaPlaylits);
                mudaLocal(chatId, 8);
            }
            if (callData.equals("listarP")) {
                usu = retornaUsuario(chatId);
                String a = listarPlaylists(usu.getIdentificador());
                if (!a.equals("")) {
                    replaceMessageWithText(chatId, messageId, a);
                    mensagem(chatId, "Escolha uma playlist pra ver suas musicas: ");
                    mensagem(chatId,"Ou digite [0] para Sair" );
                    mudaLocal(chatId, 10);
                } else {
                    replaceMessageWithText(chatId, messageId, "Nenuma Playlist Cadastrada!!");
                    mensagem(chatId, "Digite '/menu' para voltar as opções");
                }
            }
            if (callData.equals("logout")) {
                usu = retornaUsuario(chatId);
                replaceMessageWithText(chatId, messageId, "Tchau " + usu.getNome() + " !!!");
                mudaLogin(chatId, false);
                mudaLocal(chatId, 0);
            }

        }
    }

    @Override
    public String getBotUsername() {
        // Return bot username
        // If bot username is @MyAmazingBot, it must return 'MyAmazingBot'
        return "SpootifySABot";
    }

    @Override
    public String getBotToken() {
        // Return bot token from BotFather
        return "857874652:AAGi5CR5wkn229T2PMyKR9unLIgzmmnR3Cw";
    }

    private String listarMusicas() {
        try {
            listaMusicas = new DAOMusica().listarMusicas();
        } catch (MusicaNaoCadastrada ex) {
            Logger.getLogger(MenuBot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UsuarioNaoCadastrado ex) {
            Logger.getLogger(MenuBot.class.getName()).log(Level.SEVERE, null, ex);
        }
        String ret = "";
        if (listaMusicas.size() == 0) {
            ret = "Nenhuma Musica";
            return ret;
        }
        for (Node listaMusica : listaMusicas) {
            ret += listaMusica.getTexto();
        }

        return ret;
    }

    private String listarMusicasPlaylist(String nome) {
        String ret = "";
        Nodi p = null;
        for (Nodi plays : listaPlaylists) {
            if (plays.getPlay().getNome().equals(nome)) {
                p = plays;
            }
        }

        if (p.getPlay().getListaMusicas().size() > 0) {
            ret += p.getMusicas();
        } else {
            ret = "Nenhuma Música nessa PlayList!!";
        }

        return ret;
    }

    private String listarPlaylists(String id) {
        System.out.println("Id do usuario: " + id);
        try {
            listaPlaylists = new DAOPlaylist().listarPlays(id);
        } catch (PlaylistNaoExistente ex) {
            Logger.getLogger(MenuBot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UsuarioNaoCadastrado ex) {
            Logger.getLogger(MenuBot.class.getName()).log(Level.SEVERE, null, ex);
        }

        String ret = "";
        for (Nodi lp : listaPlaylists) {
            System.out.println("TextoP:" + lp.getTexto());
            ret += lp.getTexto();
        }

        return ret;
    }

}
