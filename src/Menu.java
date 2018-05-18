import javax.swing.*;
import java.awt.*;

public class Menu extends JFrame {

    private PanelMenu PAN = new PanelMenu();
    private int[] windowSize = {600,600};


    Menu(){
        this.init();
    }

    private void init(){
        this.setTitle("Menu");
        this.setSize(this.windowSize[0], this.windowSize[1]);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setContentPane(this.PAN);
        this.setVisible(true);
    }

}
