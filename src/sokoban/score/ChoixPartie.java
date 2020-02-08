package sokoban.score;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ChoixPartie {
	
	private static int i = 0;
	
	/**
	 * Fenetre qui permet de choisir entre reprendre la partie precedente ou faire une nouvelle partie
	 * @param nom
	 * @return
	 */
	public static int affichage(String nom) {
		
		Stage partie = new Stage();
		partie.setTitle("sokoban");
		BorderPane pane = new BorderPane();
		Scene scene = new Scene(pane, 250, 320);
		Button newGame = new Button("Nouvelle partie");
		Button continuGame = new Button("Reprendre la partie");
		VBox principale = new VBox();
		
		newGame.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				partie.close();
			}
		});
		continuGame.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				partie.close();
				i = 1;
			}
		});
		
		partie.initModality(Modality.APPLICATION_MODAL);
		
		principale.getChildren().setAll(newGame, continuGame);
		pane.setCenter(principale);
		
		principale.setAlignment(Pos.CENTER);
		principale.setSpacing(30);
		
		partie.setScene(scene);
		partie.showAndWait();
		return i;
	}
	
	/**
	 * Permet de recuperer le niveau du joueur dans la table scoresave
	 * @param nom
	 * @return
	 * @throws SQLException
	 */
	public static int getNiveau(String nom) throws SQLException {
		LienScoreBD conn = new LienScoreBD();
		Connection connection = conn.connectBD();
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT niveau FROM scoresave WHERE nom = '"+nom+"'");
		if(rs.next()) {
			System.out.println(rs.getInt(1));
			return rs.getInt(1);
		}
		return 0;
	}
	
	/**
	 * Permet de recuperer le nombre de pas fait par le joueur, de la table scoresave de la BD
	 * @param nom
	 * @return
	 * @throws SQLException
	 */
	public static int getNbPas(String nom) throws SQLException {
		LienScoreBD conn = new LienScoreBD();
		Connection connection = conn.connectBD();
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT nbPas FROM scoresave WHERE nom = '"+nom+"'");
		if(rs.next()) {
			System.out.println(rs.getInt(1));
			return rs.getInt(1);
		}
		return 0;
	}
}
