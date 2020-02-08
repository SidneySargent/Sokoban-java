/**
 * 
 */
package sokoban.score;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;



/**
 * @author sidney
 *
 */
public class LienScoreBD {


	
	private String url="jdbc:mysql://localhost:3307/sokoban"
			+"?useSSL=false&useUnicode=true"
			+"&useJDBCCompliantTimezoneShift=true"
			+"&useLegacyDatetimeCode=false"
			+"&serverTimezone=UTC"
			+"&allowPublicKeyRetrieval=true";
			
	private String login = "root";
	private String password = "usbw";
	private Connection connection;
	
	public LienScoreBD() throws SQLException {
		
	}
	
	/**
	 * Verification de la connection à la BD
	 * @return Connection connection
	 * @throws SQLException
	 */
	public Connection connectBD() throws SQLException {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(url, login, password);
			System.out.println("Driver OK !");
		}
		catch(ClassNotFoundException e) {
			System.err.println("Erreur du chargement du driver");
			e.printStackTrace();
		}
		return connection;
	}
	
	/**
	 * Etabli la liaison entre la BD et l'affichage du score
	 * @return ArrayList l, résultat de la requête sous forme de liste.
	 * @throws SQLException
	 */
	public ArrayList tableScore() throws SQLException {

		LienScoreBD test = new LienScoreBD();
		
		Connection connection = test.connectBD();
		
		Statement stmt = connection.createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT nom, meilleurScore FROM joueur ORDER BY meilleurScore LIMIT 0,5");
		
		ArrayList l = new ArrayList();
		int n = 1;
		while (rs.next()) {
			l.add(n);
			n++;
			String st = rs.getString("nom");
			l.add(st);
			int id = rs.getInt("meilleurScore");
			l.add(id);
		}
		for (int i=0; i<l.size(); i++) {
			System.out.println(l.get(i));
		}
		return l;	
	}
	
	/**
	 * Met à jour la BD lorsque tous les niveau sont terminés
	 * @param nom
	 * @param nbPasTotal
	 * @throws SQLException
	 */
	public void transfertDonnees(String nom, int nbPasTotal) throws SQLException {
		
		LienScoreBD test = new LienScoreBD();
		Connection connection = test.connectBD();
		Statement stmt = connection.createStatement();

		ResultSet rs = stmt.executeQuery("SELECT * FROM joueur WHERE nom = '"+nom+"'");
		
		boolean vide = true;
		while(rs.next()) {
			vide = false;
		}
		if(vide == true) {
			String sql = "INSERT INTO joueur ( `nom`, `meilleurScore`)"
					+ " VALUES ('"+nom+"',"+nbPasTotal+")";
			int i = stmt.executeUpdate(sql);
		}
		else {
			rs.first();
			int idNom = rs.getInt("idNom");
			int scoreTotal = rs.getInt("meilleurScore");
			if(scoreTotal>nbPasTotal) {
				String sql = "UPDATE `joueur` SET `meilleurScore`="+nbPasTotal+" WHERE `idNom`="+idNom+"";
				int i = stmt.executeUpdate(sql);
			}
		}
	}
	
}
