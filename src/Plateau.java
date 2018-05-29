import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

/**
 * @author Emma
 * @version 1.4.1
 *
 * Classe définissant le plateau de jeu, contenant les pions
 * ainsi que ses attributs (à définir, certainement sprite, position, les fonctions de l'affichage,
 * et les fonctions logiques du jeu)
 *
 * @see Pion
 * */

public class Plateau extends JPanel implements MouseListener{

    private Pion[][] matrice = new Pion[10][10];      // 10x10

    private boolean joueur = true;    // actualisé à chaque tour, true = blanc, false = noir

    private Image sprite;

    private final int[] POSPREMIERPION = {50,50};
    private final int TAILLECASE = 60;        // avec le pixel de bordure

    private int[] selected = null;  // coord du pion sélectionné


    Plateau(){
        try{
            this.sprite = ImageIO.read(new File("Files/platal.png"));
        }catch(IOException e){
            e.printStackTrace();
        }

        this.initPlateau();

        this.addMouseListener(this);        // pour récupérer les clics & co
    }



    /**
     * Actualise la position d'un pion dans la matrice
     * ET dans ses coordonnées perso
     * */
    public void bougePion(int[] depart, int[] arrivee){
        matrice[arrivee[0]][arrivee[1]] = matrice[depart[0]][depart[1]];    // on copie le pion dans la case cible
        matrice[depart[0]][depart[1]] = null;   // on vide la case de depart
        matrice[arrivee[0]][arrivee[1]].setPos(arrivee);
        this.repaint();
        System.out.println(this.dansCampAdverse(matrice[arrivee[1]][arrivee[0]], arrivee));
    }

    /**
     * --- A ECRIRE/DEBUG ---
     * Renvoie si le pion arrive dans le camp adverse, donc devient une dame
     * */
    public boolean dansCampAdverse(Pion pion, int[] coord){
//        return (pion.isWhite() && coord[1]==0)||(!pion.isWhite() && coord[1]==9);
        return false;
    }

    /**
     * Initialise le plateau avec les pions au bon endroit
     * @since 1.1
     * */
    public void initPlateau(){
        for (int i=0; i<10; i++){
            if (i<4) {      // les pions noirs
                for (int j = (i+1)%2; j < 10; j+=2) {
                    this.matrice[j][i]=new Pion(Pion.BLACK, j, i);
                }
            }else if(i>5){      // les pions blancs
                for (int j = (i+1)%2; j<10; j+=2){
                    this.matrice[j][i]=new Pion(Pion.WHITE, j, i);
                }
            }
        }
    }


    /**
     * MouseListener
     * @since 1.2
     * */
    public void mouseClicked(MouseEvent e){}
    public void mousePressed(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}

    /**
     * Définit les actions à effectuer une fois que la souris est relachée
     * @param e l'objet du clic
     * */
    public void mouseReleased(MouseEvent e){

        int x = e.getX();
        int y = e.getY();

        if (this.dansPlateau(x,y)){     // si on a cliqué dans la grille

            int[] caseClic = this.caseClic(x,y);
//            System.out.println(clic[0]+", "+clic[1]);

            if(this.pionDansCase(caseClic[0],caseClic[1]) && this.selected==null){
//                if(this.canMove(caseClic)) -> L'idéal
                    this.selected=caseClic;
            }else if (this.selected!=null && this.canMove(selected, caseClic)){
                this.bougePion(selected, caseClic);
                this.selected = null;
            }

        }
    }

    /**
     * La jolie fonction qui teste si le mouvement demandé par l'utilisateur est autorisé
     * @param destination -> la case sur laquelle on aimerait aller
     * @param pion -> la case où se situe le pion sélectionné
     * @return canmove -> true si mouvement autorisé, sinon false */
    private boolean canMove(int[] pion, int[] destination){
        boolean canmove = false;

        if(this.matrice[destination[0]][destination[1]]!=null){
            return false;
        }

        // la condition la plus longue que j'aie jamais vue x)
        if(this.matrice[pion[0]][pion[1]].isWhite()         // pion blanc
                && !this.matrice[pion[0]][pion[1]].isDame()
                && this.matrice[destination[0]][destination[1]]==null && (pion[1]>0 && destination[1] == pion[1]-1
                && ((pion[0]>0 && destination[0] == pion[0]+1) || (pion[0]<9 && destination[0] == pion[0]-1)))
                || (pion[1]>1 && destination[1] == pion[1]-2
                && ((pion[0]>1 && this.matrice[destination[0]-1][destination[1]-1] != null
                && !this.matrice[destination[0]-1][destination[1]-1].isWhite() && destination[0] == pion[0]-2)
                || (pion[0]<8 && this.matrice[destination[0]-1][destination[1]+1] != null
                && !this.matrice[destination[0]-1][destination[1]+1].isWhite() && destination[0] == pion[0]+2)))){
            canmove = true;
        }//pion blanc

        if(!this.matrice[pion[0]][pion[1]].isWhite() && !this.matrice[pion[0]][pion[1]].isDame()
                && this.matrice[destination[0]][destination[1]]==null && (pion[1]<9 && destination[1] == pion[1]+1
                && ((pion[0]>0 && destination[0] == pion[0]+1) || (pion[0]<9 && destination[0] == pion[0]-1)))
                || (pion[1]<8 && destination[1] == pion[1]+2 && ((pion[0]>1
                && this.matrice[destination[0]-1][destination[1]-1] != null
                && this.matrice[destination[0]-1][destination[1]-1].isWhite() && destination[0] == pion[0]-2)
                || (pion[0]<8 && this.matrice[destination[0]-1][destination[1]+1] != null
                && this.matrice[destination[0]-1][destination[1]+1].isWhite() && destination[0] == pion[0]+2)))){
            canmove = true;
        }//pion noir

        if(this.matrice[pion[0]][pion[1]].isDame() && this.matrice[destination[0]][destination[1]]==null){
            int l = pion[0];
            int k = pion[1];

            if(destination[0]>pion[0] && destination[1]>pion[1]){
                while(k<destination[0] && l<destination[1]){
                    k++;
                    l++;
                    if(this.matrice[k][l] == null || (this.matrice[k][l] != null && this.matrice[k+1][l+1] == null && destination[0] == k+1 && destination[1] == l+1)){
                        canmove = true;
                    }else {
                        return false;
                    }
                }//while1 : ++
            }

            if(destination[0]>pion[0] && destination[1]<pion[1]){
                while(k<destination[0] && l>destination[1]){
                    k++;
                    l--;
                    if(this.matrice[k][l] == null|| (this.matrice[k][l] != null && this.matrice[k+1][l-1] == null && destination[0] == k+1 && destination[1] == l-1)){
                        canmove = true;
                    }else {
                        return false;
                    }
                }//while2 : +-
            }

            if(destination[0]<pion[0] && destination[1]>pion[1]){
                while(k>destination[0] && l<destination[1]){
                    k--;
                    l++;
                    if(this.matrice[k][l] == null || (this.matrice[k][l] != null && this.matrice[k-1][l+1] == null && destination[0] == k-1 && destination[1] == l+1)){
                        canmove = true;
                    }else {
                        return false;
                    }
                }//while3 : -+
            }

            if(destination[0]<pion[0] && destination[1]<pion[1]){
                while(k>destination[0] && l>destination[1]){
                    k--;
                    l--;
                    if(this.matrice[k][l] == null || (this.matrice[k][l] != null && this.matrice[k-1][l-1] == null && destination[0] == k-1 && destination[1] == l-1)){
                        canmove = true;
                    }else {
                        return false;
                    }
                }//while4 : --
            }
        }//dame



        return canmove;
    }
    /**
     * Renvoie true si le clic effectué est sur la grille du plateau
     * @param x -> coordonnée horizontale du clic en px
     * @param y -> coordonnée verticale du clic en px
     * */
    private boolean dansPlateau(int x, int y){
        return (x>=POSPREMIERPION[1] && x<=POSPREMIERPION[1]+(TAILLECASE*10)
                && y>=POSPREMIERPION[0] && y<=POSPREMIERPION[0]+(TAILLECASE*10));

    }

    /**
     * Renvoie les coordonnées de la case sur laquelle on a cliqué
     * @param x -> coord x
     * @param y -> coord y
     * @return [x,y]*/
    private int[] caseClic(int x, int y){
        int[] coord = new int[2];
        coord[0] = (x-this.POSPREMIERPION[1])/TAILLECASE;
        coord[1] = (y-this.POSPREMIERPION[0])/TAILLECASE;
        return coord;
    }

    /**
     * */
    private boolean pionDansCase(int x, int y){
        return (this.matrice[x][y]!=null);
    }




    /**
     * Le joyeux bazar de l'affichage --- RIP smiley 2018-2018 ---
     * @version 1.1
     * @since 1.1
     * */
    public void paintComponent(Graphics g){
        g.drawImage(this.sprite, 0,0,this);     // platal

        for(int i=0; i<10; i++){
            for (int j=0; j<10; j++){
                try{
                    g.drawImage(matrice[i][j].getSprite(), matrice[i][j].getPos()[0]*this.TAILLECASE + this.POSPREMIERPION[0],
                            matrice[i][j].getPos()[1]*this.TAILLECASE + this.POSPREMIERPION[1], this);
                }catch (NullPointerException e){
//                    System.out.println("Case vide");    // pour le débug, à virer
                }
            }
        }
    }



    /**
     * Pour afficher le contenu du plateau
     * @since 1.1
     * */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<10; i++){
            for (int j=0; j<10; j++){
                try{
                    sb.append(this.matrice[i][j].toString());
                }catch (NullPointerException e){
                    sb.append("Pointeur null ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }



    /**
     * @author Ian
     * renvoie un tableau contenant les toutes les prises obligatoire du joueur mis en parametre
     *
     * */
    public int[][] priseObligatoire () { //joueur noir : false , joueur blanc : true

        /*
          On a une ligne par pion (20 pions/dames max par joueur donc 20 lignes)
          Les deux premières cases de la ligne sont les coordonnées du pion/dame,
          les suivantes les coordonnées des cases d'arrivée si prise obligatoire il y a
          On a au maximun 4 prises obligatoires possibles (cas d'une dame) donc il y a 2+2*4=10 cases par lignes
         */

        int[][] coordonees = new int[20][10];
        int a = 0;

        for(int i = 0 ; i<coordonees.length ; i++){
            for(int j = 0 ; j<coordonees[i].length ; j++){
                coordonees[i][j] = 10;
            }
        }

        for(int i = 0 ; i<this.matrice.length ; i++){
            for (int j = 0 ; j<this.matrice[i].length ; j++){

                //pour un pion
                if(this.matrice[i][j] != null && this.matrice[i][j].isWhite() == joueur && !this.matrice[i][j].isDame()){ //si il y a un pion de la couleur du joueur
                    if(joueur){//joueur blanc
                        if(i>1 && this.matrice[i-1][j-1] != null && this.matrice[i-1][j-1].isWhite() != joueur && this.matrice[i-2][j-2] == null){
                            coordonees[a][0] = i;
                            coordonees[a][1] = j;
                            coordonees[a][2] = i-1;
                            coordonees[a][3] = j-1;
                        }//if il y a un pion adverse en diag à gauche et une case libre derière

                        if(i<6 && this.matrice[i+1][j-1] != null && this.matrice[i+1][j-1].isWhite() != joueur && this.matrice[i+2][j-2] == null){
                            coordonees[a][0] = i;
                            coordonees[a][1] = j;
                            if(coordonees[a][2] == 10){
                                coordonees[a][2] = i+1;
                                coordonees[a][3] = j-1;
                            }else{
                                coordonees[a][4] = i+1;
                                coordonees[a][5] = j-1;
                            }

                        }//if il y a un pion adverse en diag à droite et une case libre derière
                    }//if joueur blanc

                    if(!joueur){//joueur noir
                        if(i>1 && this.matrice[i-1][j+1] != null && this.matrice[i-1][j+1].isWhite() != joueur && this.matrice[i-2][j+2] == null){
                            coordonees[a][0] = i;
                            coordonees[a][1] = j;
                            coordonees[a][2] = i-1;
                            coordonees[a][3] = j+1;
                        }//if il y a un pion adverse en diag à gauche et une case libre derière

                        if(i<6 && this.matrice[i+1][j+1] != null && this.matrice[i+1][j+1].isWhite() != joueur && this.matrice[i+2][j+2] == null){
                            coordonees[a][0] = i;
                            coordonees[a][1] = j;
                            if(coordonees[a][2] == 10){
                                coordonees[a][2] = i+1;
                                coordonees[a][3] = j+1;
                            }else{
                                coordonees[a][4] = i+1;
                                coordonees[a][5] = j+1;
                            }
                        }//if il y a un pion adverse en diag à droite et une case libre derière
                    }//if joueur noir
                }//if pion bonne couleur

                //pour une damme
                if(this.matrice[i][j] != null && this.matrice[i][j].isWhite() == joueur && this.matrice[i][j].isDame()){ //si il y a une dame de la couleur du joueur
                    int k = i;
                    int l = j;

                    while(k<9 && l<9){
                        k++;
                        l++;
                        if(this.matrice[k][l] != null && this.matrice[k][l].isWhite() != joueur && this.matrice[k+1][l+1] == null){
                            coordonees[a][0] = i;
                            coordonees[a][1] = j;

                            int m = 2;
                            while(coordonees[a][m] != 10){
                                m++;
                            }

                            coordonees[a][m] = k+1;
                            coordonees[a][m+1] = l+1;

                            k=9;
                            l=9;

                        }
                        if(this.matrice[k][l] != null && this.matrice[k][l].isWhite() == joueur){
                            k=9;
                            l=9;
                        }
                    }//while1 : ++

                    k=i;
                    l=j;

                    while(k<9 && l>0){
                        k++;
                        l--;
                        if(this.matrice[k][l] != null && this.matrice[k][l].isWhite() != joueur && this.matrice[k+1][l-1] == null){
                            coordonees[a][0] = i;
                            coordonees[a][1] = j;

                            int m = 2;
                            while(coordonees[a][m] != 10){
                                m++;
                            }

                            coordonees[a][2] = k+1;
                            coordonees[a][3] = l-1;

                            k=9;
                            l=0;
                        }
                        if(this.matrice[k][l] != null && this.matrice[k][l].isWhite() == joueur){
                            k=9;
                            l=0;
                        }
                    }//while2 : +-

                    k=i;
                    l=j;

                    while(k>0 && l<9){
                        k--;
                        l++;
                        if(this.matrice[k][l] != null && this.matrice[k][l].isWhite() != joueur && this.matrice[k-1][l+1] == null){
                            coordonees[a][0] = i;
                            coordonees[a][1] = j;

                            int m = 2;
                            while(coordonees[a][m] != 10){
                                m++;
                            }

                            coordonees[a][2] = k-1;
                            coordonees[a][3] = l+1;

                            k=0;
                            l=9;
                        }
                        if(this.matrice[k][l] != null && this.matrice[k][l].isWhite() == joueur){
                            k=0;
                            l=9;
                        }
                    }//while3 : -+

                    k=i;
                    l=j;

                    while(k>0 && l>0){
                        k--;
                        l--;
                        if(this.matrice[k][l] != null && this.matrice[k][l].isWhite() != joueur && this.matrice[k-1][l-1] == null){
                            coordonees[a][0] = i;
                            coordonees[a][1] = j;

                            int m = 2;
                            while(coordonees[a][m] != 10){
                                m++;
                            }

                            coordonees[a][2] = k-1;
                            coordonees[a][3] = l-1;

                            k=0;
                            l=0;
                        }
                        if(this.matrice[k][l] != null && this.matrice[k][l].isWhite() == joueur){
                            k=0;
                            l=0;
                        }
                    }//while4 : --


                }//if dame bonne couleur

                a++;


            }//for j
        }//for i

        return coordonees;

    }//priseObligatoirePion

}
