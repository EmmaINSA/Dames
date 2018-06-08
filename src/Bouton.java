import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

/**
 * Classe qui gère le bouton JcJ (et aurait pu gérer le bouton JvIA s'il y avait une IA)
 * @version 2.0.1
 * @see Menu
 * @see PanelMenu
 * */
public class Bouton extends JButton implements MouseListener {

    private String fonction;
    private ImageIcon sprite_normal;
    private ImageIcon sprite_hovered;
    private Menu menu;

    Bouton(String fonction, Menu menu) {
        super();
        this.menu = menu;
        this.init(fonction);
    }

    private void init(String fonction) {
        this.fonction = fonction;
        if (fonction.equals("JcJ")) {
            try{
                this.sprite_normal = new ImageIcon(ImageIO.read(new File("Files/JcJ.png")));
                this.sprite_hovered = new ImageIcon(ImageIO.read(new File("Files/JcJ_hover.png")));
            }catch (IOException e){
                e.printStackTrace();
            }

        } else if (fonction.equals("IA")) {
            try{
                this.sprite_normal = new ImageIcon(ImageIO.read(new File("Files/JvIA.png")));
                this.sprite_hovered = new ImageIcon(ImageIO.read(new File("Files/JvIA_hover.png")));
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        addMouseListener(this);
        this.setIcon(this.sprite_normal);
        this.setMargin(new Insets(0, 0, 0, 0)); // vire les bordures
        this.setFocusPainted(false);
    }


    public void mouseClicked(MouseEvent event) { }

    public void mouseEntered(MouseEvent event) {
        this.setIcon(this.sprite_hovered);
        this.repaint();
    }

    public void mouseExited(MouseEvent event) {
        this.setIcon(this.sprite_normal);
        this.repaint();
    }

    public void mousePressed(MouseEvent event) {
        FenetreJeu game = new FenetreJeu(this.fonction.equals("IA"));
    }

    public void mouseReleased(MouseEvent event) { }

}