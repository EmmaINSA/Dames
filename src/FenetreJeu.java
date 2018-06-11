import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * --- A ECRIRE ---
 * Classe définissant la fenêtre, càd tout ce qui est en rapport
 * avec ce qui sera affiché sur la fenêtre du jeu
 * @author Emma
 * @version 2.0.1
 * @since 1.0
 * @see Plateau
 * */

public class FenetreJeu extends JFrame{

    private Plateau plateau;
    private int[] size = {1000,740};
    private int[] pos = new int[2];
    private Image icone;

    /**
     * Constructeur appelé quand on commence le jeu à proprement parler
     * */
    FenetreJeu(){
        this.init();        // gère l'initialisation de la fenetre
    }

    private void init(){

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double screenSizeWidth = screenSize.getWidth();

        try {
            this.icone = ImageIO.read(new File("Files/icone.png"));
        }catch (IOException e){
            e.printStackTrace();
        }
        this.plateau = new Plateau();
        this.setTitle("Daaaames son !");
        this.setSize(this.size[0],this.size[1]);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setPos((int)screenSizeWidth/2 - this.size[0]/2,0);
        this.setLocation(this.pos[0], this.pos[1]);    // centré et +- joli
        this.setVisible(true);
        this.setAlwaysOnTop(false);
        this.setIconImage(this.icone);
        this.setContentPane(this.plateau);
    }

    private void setPos(int x, int y){
        this.pos[0]=x;
        this.pos[1]=y;
    }

}
