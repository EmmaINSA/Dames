import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Classe qui gère la fenêtre du menu
 * @version 2.0.1
 * @see PanelMenu
 * */
public class Menu extends JFrame {

    private PanelMenu PAN = new PanelMenu(this);
    private int[] windowSize = {700,600};
    private Image icone;


    Menu(){
        this.init();
    }

    private void init(){
        try{
            this.icone = ImageIO.read(new File("Files/icone.png"));
        }catch (IOException e){
            e.printStackTrace();
        }

        this.setTitle("Daaaames son ! - Menu");
        this.setSize(this.windowSize[0], this.windowSize[1]);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setContentPane(this.PAN);
        this.setVisible(true);
        this.setIconImage(icone);
    }
}
