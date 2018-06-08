import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author Emma
 * @version 2.0.1
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
                this.sprite = ImageIO.read(new File("Files/pion_N.png"));
            }else{
                this.sprite = ImageIO.read(new File("Files/pion_B.png"));
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        int[] pos = {x,y};
        this.setPos(pos);
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
        try {
            this.sprite = ImageIO.read(new File((this.isWhite()) ? "Files/dame_B.png" : "Files/dame_N.png"));
        }catch (IOException e){
            e.printStackTrace();
        }
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
     * @since 1.1
     * */
    public Image getSprite() {
        return sprite;
    }


}
