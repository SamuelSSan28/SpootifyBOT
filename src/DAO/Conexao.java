package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class Conexao {
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final String SCHEMA = "ufpimusic";
	private static final String URL = "jdbc:mysql://localhost/" + SCHEMA
			+ "?autoReconnect=true&useSSL=false";
	private static final String USER = "root";
	private static final String SENHA = "12345678";
	private static Connection conexoes[] = new Connection[20];
	private static boolean conectou = false;
	private static int posicao = 0;

	public static Connection getConnection() {
		if (posicao == 10) {
			posicao = 0;
		}

		if (!conectou) {
			try {
				Class.forName(DRIVER);
				for (int i = 0; i < 20; i++) {
					conexoes[i] = (Connection) DriverManager.getConnection(URL, USER, SENHA);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			conectou = true;
		}

		return conexoes[posicao++];
	}

}
