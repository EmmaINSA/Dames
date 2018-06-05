import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

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
    private Image sprite, selectedSprite;

    private final int[] POSPREMIERPION = {50,50};
    private final int TAILLECASE = 60, PIONSPARLIGNE=7, NBERREURS=3;        // avec le pixel de bordure
    private final int TAILLEPLATAL = POSPREMIERPION[0]*2+10*TAILLECASE;


    private int[] selected = null;  // coord du pion sélectionné

    private Pion[] pionBmourus = new Pion[20], pionNmourus= new Pion[20];   // pour afficher les pions mangés sur le bord
    private int nbPionBmourus = 0, nbPionNmourus = 0;

    // "poids" des cases : + la case est au bord, + elle est intéressante (défendre/faire une dame, position imprenable)
    // -- fun fact -- : matrice symétrique ! (codée avec les colonnes du plateau en ligne et pourtant même résultat final)
    // fonctionne
    private int[][] valeursCases = {
            {0,5,0,5,0,5,0,5,0,5},      // colonne 1
            {5,0,4,0,4,0,4,0,4,0},      // colonne 2
            {0,4,0,3,0,3,0,3,0,5},      // etc...
            {5,0,3,0,2,0,2,0,4,0},
            {0,4,0,2,0,1,0,3,0,5},
            {5,0,3,0,1,0,2,0,4,0},
            {0,4,0,2,0,2,0,3,0,5},
            {5,0,3,0,3,0,3,0,4,0},
            {0,4,0,4,0,4,0,4,0,5},
            {5,0,5,0,5,0,5,0,5,0}};    // hardcode ftw


    Plateau(){
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
//        System.out.println(this.dansCampAdverse(matrice[arrivee[1]][arrivee[0]], arrivee));
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
        try{
            this.sprite = ImageIO.read(new File("Files/platal.png"));
            this.selectedSprite = ImageIO.read(new File("Files/selected.png"));
        }catch(IOException e){
            e.printStackTrace();
        }

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

        // --- TEST ---
        // noirs
        this.mangePion(0,1);
        this.mangePion(0,3);
        this.mangePion(0,5);
        this.mangePion(0,7);
        this.mangePion(0,9);
        this.mangePion(1,0);
        this.mangePion(1,2);
        this.mangePion(1,4);
        this.mangePion(1,6);
        // blancs
        this.mangePion(6,1);
        this.mangePion(6,3);
        this.mangePion(6,5);
        this.mangePion(6,7);
        this.mangePion(6,9);
        this.mangePion(7,0);
        this.mangePion(7,2);
        this.mangePion(7,4);
        this.mangePion(7,6);
    }


    /**
     * MouseListener
     * @since 1.2
     * */
    public void mouseClicked(MouseEvent e){/*   // debug
        int[] coord = caseClic(e.getY(), e.getX());
        System.out.println(Integer.toString(valeursCases[coord[0]][coord[1]]));*/
    }
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
            System.out.println("Coord clic : "+caseClic[0]+", "+caseClic[1]);

            System.out.println("Moves possibles :");
            try {
                for (int[] coord : canMove(caseClic[0], caseClic[1])) {
                    System.out.println(coord[0] + ", " + coord[1]);
                }
            }catch (NullPointerException ex){
                System.out.println("Pas de moves possibles");
            }

           /* if(this.pionDansCase(caseClic[0],caseClic[1]) && this.selected==null){
//                if(this.canMove(caseClic)) -> L'idéal
                    this.selected=caseClic;
            }else if (this.selected!=null && this.canMove(selected, caseClic)){
                this.bougePion(selected, caseClic);
                System.out.println("nb pions\nblancs:"+nbPions(true)+"\nnoirs : "+nbPions(false));
                this.selected = null;
            }*/
        }
        this.repaint();
    }

    /**
     * Update le platal quand on mange un pion
     * */
    private void mangePion(int l, int c){
        if (this.matrice[c][l]!=null) {
            if (this.matrice[c][l].isWhite()) {
                this.pionBmourus[this.nbPionBmourus] = this.matrice[c][l];
                this.nbPionBmourus++;
            } else {
                this.pionNmourus[this.nbPionNmourus] = this.matrice[c][l];
                this.nbPionNmourus++;
            }
            this.matrice[c][l] = null;
        }else{
            System.out.println("Oups, on ne peut pas manger de pion à la case "+Integer.toString(l)+", "+Integer.toString(c));
        }
        this.repaint();
    }

    private int nbPions(boolean couleur){
        int nb = 0;
        for (int l=0; l<10; l++){
            for (int c=0; c<10; c++){
                if (matrice[l][c]!=null){
                    if (matrice[l][c].isWhite() == couleur){
                        nb++;
                    }
                }
            }
        }
        return nb;
    }


    /**
     * Le canMove 2.0
     * Ne teste pas les possibilités de manger les pions adverses
     * @param c la colonne du pion à tester
     * @param l la ligne du pion à tester
     * @return les coordonnées des cases où le déplacement est possible
     * -> si -1, alors pas de possibilité
     * */
    private int[][] canMove(int c, int l){
        if (this.matrice[c][l]!= null) {        // si il y a bien un pion dans la case
            int[][] coord;      // null
            int cmpt = 0;

            if (!this.matrice[c][l].isDame()) {     // pion
                coord = new int[2][2];      // 2 possibilités de déplacement max pour un pion

                if(this.matrice[c][l].isWhite()){     // blancs
                    // haut-gauche
                    if (c-1>=0 && l-1>=0){      // si on ne sort pas du platal -> pas de outOfBounds
                        if (matrice[c-1][l-1]==null){
                            coord[cmpt][0]=c-1;
                            coord[cmpt][1]=l-1;
                            cmpt++;
                        }
                    }
                    // haut-droit
                    if (c+1<10 && l-1>=0){
                        if (matrice[c+1][l-1]==null){
                            coord[cmpt][0]=c+1;
                            coord[cmpt][1]=l-1;
                            cmpt++;
                        }
                    }

                }else{      // noirs
                    // bas-gauche
                    if (c-1>=0 && l+1<10){
                        if (matrice[c-1][l+1]==null){
                            coord[cmpt][0]=c-1;
                            coord[cmpt][1]=l+1;
                            cmpt++;
                        }
                    }
                    // bas-droit
                    if (c+1<10 && l+1<10){
                        if (matrice[c+1][l+1]==null){
                            coord[cmpt][0]=c+1;
                            coord[cmpt][1]=l+1;
                            cmpt++;
                        }
                    }
                }

            } else {            // dame
                coord = new int[18][2];
                int i=0;

                // diag haut-gauche
                while(c-i>=0 && l-i>=0){        // tant qu'on est dans le plateau
                    if (this.matrice[c-i][l-i]==null){
                        coord[cmpt][0] = c-i;
                        coord[cmpt][1] = l-i;
                        cmpt++;
                    }else{      // on sort de la boucle si on rencontre un pion, car on ne peut pas aller plus loin
                        break;
                    }
                }

                i=0;        // reset le compteur
                // diag haut-droit
                while(c+i<10 && l-i>=0){
                    if (this.matrice[c+i][l-i]==null){
                        coord[cmpt][0] = c+i;
                        coord[cmpt][1] = l-i;
                        cmpt++;
                    }else{
                        break;
                    }
                }

                i=0;        // reset le compteur
                // diag bas-droit
                while(c+i<8 && l+i<10){
                    if (this.matrice[c+i][l+i]==null){
                        coord[cmpt][0] = c+i;
                        coord[cmpt][1] = l+i;
                        cmpt++;
                    }else{
                        break;
                    }
                }

                i=0;
                // diag bas-gauche
                while(c-i>=0 && l+i<10){
                    if (this.matrice[c-i][l+i]==null){
                        coord[cmpt][0] = c-i;
                        coord[cmpt][1] = l+i;
                        cmpt++;
                    }else{
                        break;
                    }
                }
            }
            return Arrays.copyOf(coord, cmpt);

        }else{
            System.out.println("Oups");
            return null;
        }
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
                    sb.append("Pointeur nul");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }


    private boolean jeuFini(){
        return (nbPionBmourus==20 || nbPionNmourus==20);
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

                //pour une dame
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


    // ---------------------
    // ---- CHANTIER IA ----
    // ---------------------

    /**
     * Algo min/max utilisé par l'IA pour déterminer le coup optimal à jouer
     * a faire : @param profondeur la profondeur de la recherche (récursivité)
     * @return les coordonnées de la prise optimale, dans l'ordre :
     * x pion, y pion, x case visée, y case visée
     * */
    // a def


    private int[] minimax(int profondeur){
        int xdepart=0 , ydepart=0, xarrivee=0, yarrivee=0;
        // blablabla
        return new int[] {xdepart, ydepart, xarrivee, yarrivee};
    }

    /**
     * Renvoie un score donné à la position actuelle du platal */
    private int score(){
        int score = 0;


        return score;
    }


    private int sommeValeursPions(boolean couleur){
        int somme = 0;
        for (int c=0; c<10; c++){
            for (int l=0; l<10; l++){
                if (this.matrice[c][l]!=null){
                    if (this.matrice[c][l].isWhite()==couleur){
                        somme+=this.valeursCases[c][l];
                    }
                }
            }
        }
        return somme;
    }





    /**
     * Le joyeux bazar de l'affichage --- RIP smiley 2018-2018 ---
     * @version 1.5
     * @since 1.1
     * */
    public void paintComponent(Graphics g){
        g.drawImage(this.sprite, 0,0,this);     // platal

        // les pions du plateau
        for(int i=0; i<10; i++){
            for (int j=0; j<10; j++){
                try{
                    g.drawImage(matrice[i][j].getSprite(), matrice[i][j].getPos()[0]*this.TAILLECASE + this.POSPREMIERPION[0],
                            matrice[i][j].getPos()[1]*this.TAILLECASE + this.POSPREMIERPION[1], this);
                }catch (NullPointerException e){        // un peu malpropre
                }
            }
        }

        if (this.selected !=null){
            g.drawImage(this.selectedSprite, POSPREMIERPION[0]+this.selected[0]*this.TAILLECASE,
                    POSPREMIERPION[1]+this.selected[1]*this.TAILLECASE, this);
        }

        // les pions mourus
        for (int i=0; i<nbPionBmourus; i++){
            g.drawImage(this.pionBmourus[i].getSprite(), this.TAILLEPLATAL+20+(i%PIONSPARLIGNE)*30,
                    this.POSPREMIERPION[1]+(i/PIONSPARLIGNE)*TAILLECASE, this);  // modulo à voir
        }
        for (int i=0; i<nbPionNmourus; i++){
            g.drawImage(this.pionNmourus[i].getSprite(), this.TAILLEPLATAL+20+(i%PIONSPARLIGNE)*30,
                    this.TAILLEPLATAL-this.POSPREMIERPION[1]-(4*this.TAILLECASE)+(i/PIONSPARLIGNE)*TAILLECASE, this);  // modulo à voir

        }
    }


}
