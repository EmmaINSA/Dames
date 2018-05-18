import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class Bouton extends JButton implements MouseListener{

    private Color couleur = Color.BLUE;
    private String fonction;
    private String title = "Oh un bouton mal initialisé :)";
    private int x = 50;
    private int y = 50;
//    private int

    Bouton(String fonction){
        super();
        this.init(fonction);
    }

    private void init(String fonction){
        if(fonction.equals("JcJ")){
            this.title = "Lancer une partie joueur contre joueur";
            this.y = 100;
        }else if(fonction.equals("IA")){
            this.title = "Lancer une partie solo contre IA";
            this.y = 200;
        }
    }

    public void mouseClicked(MouseEvent event) { }

    public void mouseEntered(MouseEvent event) {
        this.couleur = Color.cyan;
    }

    public void mouseExited(MouseEvent event) {
        this.couleur = Color.BLUE;
    }

    public void mousePressed(MouseEvent event) { }

    public void mouseReleased(MouseEvent event) { }

    @Override
    public void paintComponent(Graphics g) {
//        g.fillRect();
    }
}
