import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;


public class Bouton extends JButton implements MouseListener {

    private String fonction;
    private ImageIcon sprite_normal;
    private ImageIcon sprite_hovered;
    private int x = 100;
    private int y = 50;
    private int width = 500;
    private int height = 80;
    private Menu menu;

    private Image imagetest;


    Bouton(String fonction, Menu menu) {
        super();
        this.menu = menu;
        this.init(fonction);
    }

    private void init(String fonction) {
        this.fonction = fonction;
        if (fonction.equals("JcJ")) {
            this.y = 100;
            try{
                this.sprite_normal = new ImageIcon(ImageIO.read(new File("Files/JcJ.png")));
                this.sprite_hovered = new ImageIcon(ImageIO.read(new File("Files/JcJ_hover.png")));
            }catch (IOException e){
                e.printStackTrace();
            }

        } else if (fonction.equals("IA")) {
            this.y = 300;
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
        this.setLocation(50,500);       // 何 ??
        this.setFocusPainted(false);

        try{
            this.imagetest = ImageIO.read(new File("Files/JcJ.png"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public void mouseClicked(MouseEvent event) { }

    public void mouseEntered(MouseEvent event) {
//        System.out.println("Enter");
        this.setIcon(this.sprite_hovered);
        this.repaint();
    }

    public void mouseExited(MouseEvent event) {
//        System.out.println("Exit");
        this.setIcon(this.sprite_normal);
        this.repaint();
    }

    public void mousePressed(MouseEvent event) {
//        Game.start(this.fonction.equals("IA"));
        FenetreJeu game = new FenetreJeu(this.fonction.equals("IA"));
//        this.menu.kill();     // marche pas

    }


    public void mouseReleased(MouseEvent event) { }

}