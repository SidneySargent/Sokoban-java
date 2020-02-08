package sokoban;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import sokoban.action.AbstractAction;
import sokoban.action.DeplacementPierre;
import sokoban.action.DeplacementSimple;
import sokoban.action.EliminationPierre;
import sokoban.elementjeu.AbstractElementJeu;
import sokoban.elementjeu.Joueur;
import sokoban.elementjeu.Mur;
import sokoban.elementjeu.Pierre;
import sokoban.elementjeu.Sortie;
import javafx.scene.layout.Pane;

/**
 * Gère le plateau de jeu
 */
public class TerrainSokoban {

    private Pane gamePane;

    private Joueur joueur;
    private List<Mur> murs;
    private List<Sortie> sorties;
    private List<Pierre> pierres;

    /**
     * Enregistre une référence vers le Pane devant afficher le terrain
     */
    public TerrainSokoban(Pane gamePane) {
        this.gamePane = gamePane;
    }

    /**
     * Prépare le terrain d'après un niveau de jeu
     */
    public void initNiveau(String niveau) {

        gamePane.getChildren().clear();
        murs = new ArrayList<>();
        sorties = new ArrayList<>();
        pierres = new ArrayList<>();

        int x = 0;
        int y = 0;

        for(char c : niveau.toCharArray()) {
            switch (c) {
                case '|' :
                    y++;
                    x=0;
                    break;
                case '@':
                    joueur = new Joueur(x, y, gamePane);
                    x++;
                    break;
                case '#':
                    murs.add(new Mur(x, y, gamePane));
                    x++;
                    break;
                case '*':
                    sorties.add(new Sortie(x, y, gamePane));
                    x++;
                    break;
                case '0':
                    pierres.add(new Pierre(x, y, gamePane));
                    x++;
                    break;
                default:
                    x++;
            }
        }

        // définition des dimensions du gamePane
        int w = x * AbstractElementJeu.width;
        int h = (y + 1) * AbstractElementJeu.width;
        gamePane.setMinSize(w, h);
        gamePane.setMaxSize(w, h);
    }

    /**
     * Retourne le cas échéant un objet Action correspondant au déplacement possible
     * selon le paramètre depl et l'état du plateau de jeu
     */
    Optional<AbstractAction> prepareDeplacement(Coord depl) {

        // carreau souhaité
        Coord coordJoueur = joueur.getCoord();
        Coord coordCible = coordJoueur.add(depl);
        Optional<AbstractElementJeu> cible = contenu(coordCible);

        if (!cible.isPresent()) {
            // déplace le joueur
            return Optional.of(new DeplacementSimple(coordJoueur, coordCible));
        } else {
            if (cible.get() instanceof Pierre) {
                // le joueur est susceptible de pousser une pierre
                Coord coordCiblePierre = coordCible.add(depl);
                Optional<AbstractElementJeu> ciblePierre = contenu(coordCiblePierre);
                if (!ciblePierre.isPresent()) {
                    return Optional.of(new DeplacementPierre(coordJoueur, coordCible, coordCiblePierre));
                } else if (ciblePierre.get() instanceof Sortie) {
                    return Optional.of(new EliminationPierre(coordJoueur, coordCible));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Recherche si un élément du jeu (autre que le joueur) se trouve à l'emplacement spécifié par les coordonnées
     */
    Optional<AbstractElementJeu> contenu(Coord coord) {
        for(Mur x: murs) {
            if (x.hasCoord(coord)) return Optional.of(x);
        }
        for(Sortie x: sorties) {
            if (x.hasCoord(coord)) return Optional.of(x);
        }
        for(Pierre x: pierres) {
            if (x.hasCoord(coord)) return Optional.of(x);
        }
        return Optional.empty();
    }

    public void deplaceJoueur(Coord cible) {
        joueur.setCoord(cible);
    }

    public void deplacePierre(Coord depart, Coord arrivee) {
        for(Pierre x: pierres) {
            if (x.hasCoord(depart)) {
                x.setCoord(arrivee);
                return;
            };
        }
    }

    public void cachePierre(Coord coord) {
        for(Pierre x: pierres) {
            if (x.hasCoord(coord)) {
                x.setCoord(null);
                return;
            };
        }
    }

    public void affichePierre(Coord coord) {
        for(Pierre x: pierres) {
            if (x.estCache()) {
                x.setCoord(coord);
                return;
            };
        }
    }

}
