/**
 * @author Emma
 * @version 1.0
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

    Pion(boolean color, int[] pos){
        this.setColor(color);
        this.setPos(pos);
    }

    /**
     * --- A ECRIRE ---
     * Déplace le pion vers la droite PAR RAPPORT A SON COTE
     * (si c'est le pion du joueur en face, il recule & va à gauche par rapport à l'autre joueur)
     */
    public void moveRight(){
        // à écrire
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
     * @return str*/
    @Override
    public String toString() {
        String str = super.toString();  // à modifier si vous avez des idées
        return str;
    }
}
