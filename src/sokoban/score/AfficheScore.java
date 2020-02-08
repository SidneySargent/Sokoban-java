/**
 * 
 */
package sokoban.score;

import java.sql.SQLException;
import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author sidney
 *
 */
public class AfficheScore{

	private LienScoreBD lien;
	private GridPane gridPane = new GridPane();
	private int re;
	
	/**
	 * Affiche une nouvelle fenetre comportant les 5 meilleurs joueurs avec leur score et affiche le score effectue par le joueur actuel.
	 * @param nbPasTotal
	 * @throws SQLException
	 */
	public AfficheScore(int nbPasTotal) throws SQLException {
		lien = new LienScoreBD();
		Stage score = new Stage();
		score.setTitle("Score :");
		ArrayList t = lien.tableScore();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(30);
		gridPane.setVgap(15);
		gridPane.add(new Label("Place"), 0, 0);
		gridPane.add(new Label("Nom"), 1, 0);
		gridPane.add(new Label("nb pas"), 2, 0);
		
		re = 1;
		for(int i = 0; i<t.size(); i++){
			gridPane.add(new Label(t.get(i)+"."), 0, re);
			t.remove(i);
			gridPane.add(new Label(t.get(i)+""), 1, re);
			t.remove(i);
			gridPane.add(new Label(t.get(i)+""), 2, re);
			re++;
		}
		
		gridPane.add(new Label("Votre score : "), 0, 6);
		gridPane.add(new Label(""+nbPasTotal), 1, 6);
		Scene scene = new Scene(gridPane,300,250);
		score.initModality(Modality.APPLICATION_MODAL);
		
		score.setScene(scene);
        score.showAndWait();
	}
	
}
