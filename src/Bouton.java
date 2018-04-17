import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JButton;

public class Bouton extends JButton {

    private String name;

    public Bouton(String string){
        super(string);
        this.name = string;
    }

    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        GradientPaint gd = new GradientPaint(0,0, Color.RED,30,30, Color.cyan, true);
        g2d.setPaint(gd);
        g2d.fillRect(0,0,this.getWidth(), this.getHeight());
    }
}
