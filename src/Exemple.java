    /**
     * Created by Hilaire on 10/04/2018.
     */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;

public class Exemple extends JFrame implements MouseListener {

    Ellipse2D.Double rond;

    public Exemple(){
        super();
        setSize(400,400);
        setVisible(true);
        rond = new Ellipse2D.Double(100,100,100,100);
        addMouseListener(this);
    }

    public void mouseClicked(MouseEvent e){}
    public void mousePressed(MouseEvent e){
        int x = e.getX();
        int y = e.getY();
        if (rond.contains(x,y)) System.out.println("ça marche !");
    }
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}


    public static void main(String[] a){
        Exemple e = new Exemple();
    }


    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.fill(rond);
    }
}
