package sokoban;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import sokoban.score.LienScoreBD;

public class Sauvegarde {
	public static void sauvegarder(String nom, int niveau, int nbPas) throws SQLException {
		LienScoreBD conn = new LienScoreBD();
		Connection connection = conn.connectBD();
		Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt.executeQuery("SELECT niveau, nbPas FROM scoresave WHERE nom = '"+nom+"'");
		if(rs.next()) {
			System.out.println(niveau+" "+nbPas);
			stmt.executeUpdate("UPDATE scoresave SET niveau = "+niveau+", nbPas = "+nbPas+" WHERE nom = '"+nom+"'");
			
		}
		else {
			stmt.executeUpdate("INSERT INTO `scoresave` VALUES ('"+nom+"', "+niveau+", "+nbPas+")");
		}
	}
}
