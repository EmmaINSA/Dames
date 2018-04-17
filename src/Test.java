import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Le main pour les tests
 * @author Emma
 * @version 1.0
 * */
public class Test {

    public static void main(String[] args) {
        Fenetre fenetre = new Fenetre();
        Bouton b = new Bouton("Coucou");
    }

}

/**
 * Une classe test pour les JFrame
 * @author Emma
 * @version 1.0
 * */
class Fenetre extends JFrame {

    Panneau panneau = new Panneau();
    Bouton b = new Bouton("Coucou !");

    Fenetre(){
        this.init();
        this.go();
    }


    /**
     * Initialise la fenêtre
     * Sera appelée par les différents constructeurs si besoin
     * @since 1.0
     * */
    private void init(){    // si jamais on voudra plusieurs constructeurs, + simple

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double screenSizeWidth = screenSize.getWidth();
        double screenSizeHeight = screenSize.getHeight();
        int windowWidth = 800;
        int windowHeight = 600;

        this.setTitle("Nouvelle fenêtre");
        this.setSize(800,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocation((int)screenSizeWidth/2 - windowWidth/2,20);    // centré et +- joli
        this.setVisible(true);
        this.setContentPane(this.panneau);
    }

    /**
     * Déplace le rond sur le panneau
     * @see Panneau
     * */
    private void bougeRond(){
        for (int i = 0; i<this.getHeight()-50; i++){
            panneau.setPosX(i);
            panneau.setPosY(i);
            panneau.repaint();

            try{
                Thread.sleep(20);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Déplace le rond sans qu'il sorte des bords de la fenêtre
     * Provient du tuto d'OpenClassrooms "Notre première fenêtre"
     * @author pas moi (cf OC)
     * */
    private void go(){
        //Les coordonnées de départ de notre rond
        int x = panneau.getPosX(), y = panneau.getPosY();
        //Le booléen pour savoir si l'on recule ou non sur l'axe x
        boolean backX = false;
        //Le booléen pour savoir si l'on recule ou non sur l'axe y
        boolean backY = false;

        //Dans cet exemple, j'utilise une boucle while
        //Vous verrez qu'elle fonctionne très bien
        while(true){
            //Si la coordonnée x est inférieure à 1, on avance
            if(x < 1)
                backX = false;

            //Si la coordonnée x est supérieure à la taille du Panneau moins la taille du rond, on recule
            if(x > panneau.getWidth()-50)
                backX = true;

            //Idem pour l'axe y
            if(y < 1)
                backY = false;
            if(y > panneau.getHeight()-50)
                backY = true;

            //Si on avance, on incrémente la coordonnée
            //backX est un booléen, donc !backX revient à écrire
            //if (backX == false)
            if(!backX)
                panneau.setPosX(++x);

                //Sinon, on décrémente
            else
                panneau.setPosX(--x);

            //Idem pour l'axe Y
            if(!backY)
                panneau.setPosY(++y);
            else
                panneau.setPosY(--y);

            //On redessine notre Panneau
            panneau.repaint();

            //Comme on dit : la pause s'impose ! Ici, trois millièmes de seconde
            try {
                Thread.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}


class Panneau extends JPanel {

    private int posX = 0;
    private int posY = 0;

    /**
     * Méthode appelée automatiquement de façon un peu obscure
     * pour afficher des trucs à l'écran
     * @param graphics -> le truc obscur que le système passe en param à la méthode
     * */
    public void paintComponent(Graphics graphics){      // ou paint(...)

        //On choisit une couleur de fond pour le rectangle
        graphics.setColor(Color.white);

        //On le dessine de sorte qu'il occupe toute la surface
        graphics.fillRect(0, 0, this.getWidth(), this.getHeight());

        try {
            Image image = ImageIO.read(new File("Files/afk.png"));
            graphics.drawImage(image, 0,0,this);
        } catch (IOException e){
            e.printStackTrace();
        }

//        graphics.fillOval(20,20,70,50);
        graphics.drawString("Bonjour !", 10, 10);

        graphics.setColor(Color.BLUE);
        graphics.fillOval(posX,posY, 50,50);

    }

    /**
     * Getters et setters de la position du rond
     * */

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }
}
