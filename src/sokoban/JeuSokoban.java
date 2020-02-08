package sokoban;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Optional;

//##########CHANGEMENT##########
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import sokoban.action.AbstractAction;
import sokoban.action.DeplacementPierre;
import sokoban.action.DeplacementSimple;
import sokoban.action.EliminationPierre;
//##########CHANGEMENT##########
import sokoban.score.AfficheScore;
import sokoban.score.ChoixPartie;
import sokoban.score.LienScoreBD;
import sokoban.score.NomJoueur;

/**
 * GÃ¨re le dÃ©roulement du jeu
 */
public class JeuSokoban {

    private final Pane gamePane;
    private final Label label;
    
	public int nbPas = 0;
	public int nbPasTotal = 0;
	
	private String nom;
	private NomJoueur n = new NomJoueur();
	
	public int niveau;
    private TerrainSokoban terrainSokoban;
    private int pierresRestantes;
    private int copieNbPas = 0;

    private LinkedList<AbstractAction> actions = new LinkedList<>();

    JeuSokoban(Pane gamePane, Label label, Button btnUndo, Button btnJeu, Button btnNiveau, Button saveQuit) {
    	n.demandeNom();
    	nom = n.getNom();
    	int i = ChoixPartie.affichage(nom);
    	
    	System.out.println("i = "+i);
    	niveau = 0;
        this.gamePane = gamePane;
        terrainSokoban = new TerrainSokoban(gamePane);
        this.label = label;
        saveQuit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
					Sauvegarde.sauvegarder(nom, niveau, copieNbPas);
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
            }
        });
        btnUndo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!actions.isEmpty()) {
                    AbstractAction c = actions.removeLast();
                    c.executeDefait(terrainSokoban);
                    if (c instanceof EliminationPierre) {
                        pierresRestantes++;
                        majLabel();
                    }
                }
            }
        });
        btnJeu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                niveau = 0;
                nbPas = 0;
                copieNbPas = 0;
                majLabel();
                initNiveau(niveau);
            }
        });
        btnNiveau.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	nbPas = 0;
                majLabel();
                initNiveau(niveau);
            }
        });
        gamePane.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
            	try {
                switch(event.getCode()) {
                    case LEFT:
						deplacement(new Coord(-1, 0));
                        break;
                    case RIGHT:
                        deplacement(new Coord(1, 0));
                        break;
                    case UP:
                        deplacement(new Coord(0, -1));
                        break;
                    case DOWN:
                        deplacement(new Coord(0, 1));
                        break;
                    default:
                        break;
                }
            	} catch (SQLException e) {
            		e.printStackTrace();
            	}
            }
        });
        if(i == 1) {
        	try {
				niveau = ChoixPartie.getNiveau(nom);
			} catch (SQLException e) {
				e.printStackTrace();
			}
        	try {
				copieNbPas = ChoixPartie.getNbPas(nom);
			} catch (SQLException e) {
				e.printStackTrace();
			}
        }
        	
        initNiveau(niveau);
    }

    /**
	 * @return the niveau
	 */
	public int getNiveau() {
		return niveau;
	}

	/**
     * Gï¿½re une commande de dÃ©placement
     * @throws SQLException 
     */
    public void deplacement(Coord depl) throws SQLException {
        Optional<AbstractAction> optionCommande = terrainSokoban.prepareDeplacement(depl);
        if (optionCommande.isPresent()) {
            AbstractAction c = optionCommande.get();
            actions.addLast(c);
            c.execute(terrainSokoban);
            if (c instanceof EliminationPierre) {
                pierresRestantes--;
                majLabel();
                if (pierresRestantes == 0) {
                    niveauSuivant();
                    nbPas = -1;
                }
            }
            if (c instanceof DeplacementSimple || c instanceof DeplacementPierre || c instanceof EliminationPierre) {
            	nbPas++;
            	majLabel();
            }
        }
    }

    private void initNiveau(int niveau) {
        actions.clear();
        terrainSokoban.initNiveau(Niveaux.getNiveau(niveau).getTerrain());
        pierresRestantes = Niveaux.getNiveau(niveau).getPierres();
        nbPas = 0;
        majLabel();
    }

	private void majLabel() {
        label.setText(String.format("Niveau : %d - Pierres Ã  éliminer : %d - Pas : %d", niveau+1, pierresRestantes, nbPas));
    }

	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}
	
	private void niveauSuivant() throws SQLException {
        if (niveau == Niveaux.nombreNiveau() - 1) {
            new Alert(Alert.AlertType.INFORMATION, "Vous avez terminé tous les niveaux").showAndWait();
            copieNbPas += nbPas;
            nbPasTotal += nbPas;
            LienScoreBD ajoutScore = new LienScoreBD();
            ajoutScore.transfertDonnees(nom,nbPasTotal);
            AfficheScore afficheScore = new AfficheScore(nbPasTotal);
        } else {
            niveau++;
            copieNbPas += nbPas;
            new Alert(Alert.AlertType.INFORMATION, "Vous avez terminé le niveau " + niveau).showAndWait();
            nbPasTotal += nbPas;
            initNiveau(niveau);
        }
    }
}
