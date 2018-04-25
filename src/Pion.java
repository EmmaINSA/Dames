import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author Emma
 * @version 1.1
 *
 * Classe utilisée pour tous les pions (dames ou non, qq soit leur couleur)
 *
 * */


public class Pion {

    // des constantes pour la lisibilité & la simplicité
    public static final boolean BLACK = false;
    public static final boolean WHITE = true;

    private boolean dame = false;
    private int[] pos;
    private boolean color;
    private Image sprite;

    Pion(boolean color, int x, int y){
        this.setColor(color);

        try{
            if (color == BLACK) {
                this.sprite = ImageIO.read(new File("Files/pion_noir.png"));
            }else{
                this.sprite = ImageIO.read(new File("Files/pion_blanc.png"));
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        int[] pos = {x,y};
        this.setPos(pos);
    }

    Pion(boolean color, int[] pos){
        this.setColor(color);

        try{
            if (color == BLACK) {
                this.sprite = ImageIO.read(new File("Files/pion_noir.png"));
            }else{
                this.sprite = ImageIO.read(new File("Files/pion_blanc.png"));
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        this.setPos(pos);
    }

    /**
     * --- A ECRIRE ---
     * @param coord -> les coordonnées du pion à tester dans la matrice
     * @return bool
     * */
    public boolean canMoveLeft(int[] coord){
        boolean bool = false;
        // à écrire

        return bool;
    }

    /**
     * Définit la couleur du pion (noir ou blanc)
     * Appelé une seule fois
     * @param color -> booléen : false = noir, true = blanc
     * */
    public void setColor(boolean color){
        this.color = color;
    }

    /**
     * Définit la position du pion sur la matrice
     * @param pos  -> la position du pion par rapport à la case haut-gauche (0,0)
     * */
    public void setPos(int[] pos) {
        this.pos = pos;
    }

    /**
     * Transforme le pion en une dame
     * */
    public void setDame() {
        this.dame = true;
        // load sprite ?
    }


    /**
     * Getter de la position du pion
     * @return pos
     * */
    public int[] getPos() {
        return pos;
    }

    public boolean isWhite() {
        return color;
    }

    public boolean isDame() {
        return dame;
    }

    /**
     * --- A ECRIRE SI CA VOUS FAIT PLAISIR ---
     * Sert plus au débug qu'autre chose (on va pas print les pions hein)
     * @return str
     * */
    @Override
    public String toString() {
        String str = super.toString();  // à modifier si vous avez des idées
        return str;
    }

    /**
     * @since 1.1
     * */
    public Image getSprite() {
        return sprite;
    }


}
