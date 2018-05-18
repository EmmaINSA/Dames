import javax.swing.*;
import java.awt.*;

public class PanelMenu extends JPanel {

    private Bouton boutonJcJ = new Bouton("JcJ");
    private Bouton boutonVsIA = new Bouton("IA");

    private Image bg;       // ?


    PanelMenu(){
        this.init();
    }

    private void init(){
        // marche pas
        this.add(this.boutonJcJ);
        this.add(this.boutonVsIA);
    }

/*    @Override
    public void paintComponent(Graphics g) {

    }*/
}
