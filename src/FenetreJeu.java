import javax.swing.*;
import java.awt.*;

/**
 * --- A ECRIRE ---
 * Classe définissant la fenêtre, càd tout ce qui est en rapport
 * avec ce qui sera affiché sur la fenêtre du jeu
 * @author Emma
 * @version 1.3
 * @since 1.0
 * @see Plateau
 * */

public class FenetreJeu extends JFrame{

    private Plateau plateau = new Plateau();
    private int[] size = {716,740};     // totalement empirique mais pas dégueu
    private int[] pos = new int[2];
    private boolean gamemode;

    /**
     * Constructeur appelé quand on commence le jeu à proprement parler
     * @param gamemode -> false : JcJ
     *                 -> true : J vs IA
     * */
    FenetreJeu(boolean gamemode){
        this.init(gamemode);        // gère l'initialisation de la fenetre
    }

    FenetreJeu(int[] size, boolean gamemode){    // just in case
        this.init(gamemode);
        this.setSize(size);
    }

    private void init(boolean gamemode){

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double screenSizeWidth = screenSize.getWidth();

        this.gamemode = gamemode;
        this.setTitle("Jeu de dames");
        this.setSize(this.size[0],this.size[1]);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setPos((int)screenSizeWidth/2 - this.size[0]/2,0);
        this.setLocation(this.pos[0], this.pos[1]);    // centré et +- joli
        this.setVisible(true);
        this.setAlwaysOnTop(false);      // parce que.
        this.setContentPane(this.plateau);
    }

    private void setPos(int x, int y){
        this.pos[0]=x;
        this.pos[1]=y;
    }

    private void setSize(int[] size) {
        this.size = size;
    }

    public Plateau getPlateau(){
        return this.plateau;
    }
    // à remplir

}