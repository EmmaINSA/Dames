import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Classe qui gère le panneau du menu
 * @version 2.0.0
 * */

public class PanelMenu extends JPanel {

    private Bouton boutonJcJ = new Bouton("JcJ", this.parent);
    private Bouton boutonVsIA = new Bouton("IA", this.parent);      // inutile :'(
    private Image bg, titre;
    private Menu parent;
    private GridLayout layout;


    PanelMenu(Menu menu){
        this.parent = menu;
        this.init();
    }

    private void init(){
        try{
            this.bg = ImageIO.read(new File("Files/interdimensional_chessboard.jpg"));
            this.titre = ImageIO.read(new File("Files/titre.png"));
        }catch (IOException e){
            e.printStackTrace();
        }
        this.layout = new GridLayout(3, 1);
        this.layout.setVgap(50); // pixels d'écart entre les lignes du layout

        this.setLayout(this.layout);

        this.add(this.boutonJcJ);
        // pas d'IA finalement
//        this.add(this.boutonVsIA);
    }

    // gère les marges -> à définir en fonction de la taille de la fenêtre ?
    @Override
    public Insets getInsets() {
        Insets normal = super.getInsets();
        return new Insets(normal.top + 200, normal.left+100, normal.bottom, normal.right +100);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(this.bg,0,0,this);
        g.drawImage(this.titre, 100,0, 500, 370,this);      // bricolage ftw

    }
}
