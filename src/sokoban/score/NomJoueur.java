/**
 * 
 */
package sokoban.score;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author sidney
 *
 */
public class NomJoueur {

	protected String nom;
	
	public NomJoueur() {
		
	}
	
	public NomJoueur(String nom) {
		this.nom = nom;
	}
	
	/**
	 * Cree une fenêtre avant le debut de la partie qui demande le nom du joueur
	 */
	public void demandeNom() {
		
		Stage saisieNom = new Stage();
		VBox vb = new VBox(10);
		vb.setAlignment(Pos.CENTER);
		
		Label label = new Label("Saisir votre nom d'utilisateur");
		vb.getChildren().add(label);
		
		TextField saisie = new TextField();
		vb.getChildren().add(saisie);
		saisie.setMaxWidth(200);
		
		Button validation = new Button("Valider");
		vb.getChildren().add(validation);

		validation.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				saisieNom.close();
				nom = saisie.getText();
			}
		});
		
		Scene scene = new Scene(vb,300,200);
		saisieNom.setScene(scene);
		saisieNom.showAndWait();
	}

	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}
}
