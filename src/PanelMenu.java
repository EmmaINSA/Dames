import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class PanelMenu extends JPanel {

    private Bouton boutonJcJ = new Bouton("JcJ", this.parent);
    private Bouton boutonVsIA = new Bouton("IA", this.parent);
    private Image bg;
    private Menu parent;
    private GridLayout layout;


    PanelMenu(Menu menu){
        this.parent = menu;
        this.init();
    }

    private void init(){
        try{
            this.bg = ImageIO.read(new File("Files/interdimensional_chessboard.jpg"));
        }catch (IOException e){
            e.printStackTrace();
        }
        this.layout = new GridLayout(3, 1);
        this.layout.setVgap(50); // pixels d'écart entre les lignes du layout

        this.setLayout(this.layout);

        this.add(this.boutonJcJ);
        this.add(this.boutonVsIA);
    }

    // gère les marges -> à définir en fonction de la taille de la fenêtre ?
    @Override
    public Insets getInsets() {
        Insets normal = super.getInsets();
        return new Insets(normal.top + 200, normal.left+80, normal.bottom, normal.right +80);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(this.bg,0,0,this);

    }
}
