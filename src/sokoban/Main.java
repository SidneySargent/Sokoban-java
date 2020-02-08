package sokoban;

import sokoban.elementjeu.AbstractElementJeu;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Initialisation de l'interface graphique et de la classe JeuSokoban
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        Pane gamePane = new Pane();

        StackPane stackPane = new StackPane(gamePane);
        int w = Niveaux.gameWidth * AbstractElementJeu.width;
        int h = Niveaux.gameHeight * AbstractElementJeu.width;
        stackPane.setMinSize(w, h);
        stackPane.setMaxSize(w, h);

        Button btnUndo = new Button("Annuler dépl.");
        btnUndo.setFocusTraversable(false);
        Button btnJeu = new Button("Réinit. jeu");
        btnJeu.setFocusTraversable(false);
        Button btnNiveau = new Button("Réinit. niveau");
        btnNiveau.setFocusTraversable(false);
        Button saveQuit = new Button("Sauvegarder et quitter");
        saveQuit.setFocusTraversable(false);
        HBox topPane = new HBox(5, btnUndo, btnJeu, btnNiveau, saveQuit);


        Label label = new Label();

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topPane);
        borderPane.setCenter(stackPane);
        borderPane.setBottom(label);

        Scene scene = new Scene(borderPane);

        JeuSokoban jeuSokoban = new JeuSokoban(gamePane, label, btnUndo, btnJeu, btnNiveau, saveQuit);

        Pane tabScore = new Pane();
        
        stage.setScene(scene);
        stage.setTitle("Sokoban");
        stage.show();

    }

}
