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
    private boolean tour = true;    // actualisé à chaque tour, true = blanc, false = noir
    private Image sprite, selectedSprite;

    private final int[] POSPREMIERPION = {50,50};
    private final int TAILLECASE = 60, PIONSPARLIGNE=7, NBERREURS=3;        // avec le pixel de bordure
    private final int TAILLEPLATAL = POSPREMIERPION[0]*2+10*TAILLECASE;


    private int[] selected = null;  // coord du pion sélectionné

    private Pion[] pionBmourus = new Pion[20], pionNmourus= new Pion[20];   // pour afficher les pions mangés sur le bord
    private int nbPionBmourus = 0, nbPionNmourus = 0, erreurs=0;

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

        /* ------
        TEST ZONE
        ---------*/


//        this.matrice[2][3].setDame();
        // prise obligatoire pions
        this.bougePion(6,3,7,4);
        this.bougePion(9,6,8,5);

        int [][] PO = this.formatePO(this.priseObligatoire());      // pour pas recalculer
        System.out.println("Prise obligatoire :");
        for (int i=0; i<PO.length; i++){
            System.out.println("Pion n°"+i+" : ");
            for (int j=0; j<PO[i].length; j++){
                System.out.print(Integer.toString(PO[i][j])+", ");
            }
        }

/*        // dansCampAdverse -> OK
        this.mangePion(5,0);
        this.bougePion(7,6,5,0);
        System.out.println(this.dansCampAdverse(5,0));*/
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

    public void bougePion(int cdepart, int ldepart, int carrivee, int larrivee){
        matrice[carrivee][larrivee] = matrice[cdepart][ldepart];    // on copie le pion dans la case cible
        matrice[cdepart][ldepart] = null;   // on vide la case de depart
        matrice[carrivee][larrivee].setPos(new int[] {carrivee, larrivee});
        this.repaint();
//        System.out.println(this.dansCampAdverse(matrice[arrivee[1]][arrivee[0]], arrivee));
    }


    /**
     * --- A ECRIRE/DEBUG ---
     * Renvoie si le pion arrive dans le camp adverse, donc devient une dame
     * */
    public boolean dansCampAdverse(int c, int l){
        if (this.matrice[c][l]!=null) {
            return (this.matrice[c][l].isWhite() && l == 0) || (!this.matrice[c][l].isWhite() && l == 9);
        }else{
            System.out.println("Oups ! Pas de pion en "+c+", "+l+" pour la fonction dansCampAdverse !");
            return false;
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

            int c = this.caseClic(x,y)[0];
            int l = this.caseClic(x,y)[1];
            System.out.println("Coord clic : "+c+", "+l);

            if (this.matrice[c][l]!= null){
                System.out.println("Moves possibles :");
                try {
                    for (int[] coord : canMove(c, l)) {
                        System.out.println(coord[0] + ", " + coord[1]);
                    }
                }catch (NullPointerException ex){
                    System.out.println("Pas de moves possibles");       // ?
                }
            }

//            if (this.selected==null){
//                if(this.matrice[c][l]!=null){       // si il y a un pion (sinon NullPointerException)
//                    if (this.matrice[c][l].isWhite()==this.tour && canMove(c,l)!=null){     // si on a cliqué sur un de ses pions
//
//                    }
//                }
//            }
/*

*/

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

            // --- pion ---
            if (!this.matrice[c][l].isDame()) {
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

            }

            // --- dame ---
            else {
                coord = new int[18][2];
                int i=1;

                // diag haut-gauche
                while(c-i>=0 && l-i>=0){        // tant qu'on est dans le plateau
                    if (this.matrice[c-i][l-i]==null){
                        coord[cmpt][0] = c-i;
                        coord[cmpt][1] = l-i;
                        i++;
                        cmpt++;
                    }else{      // on sort de la boucle si on rencontre un pion, car on ne peut pas aller plus loin
                        break;
                    }
                }

                i=1;        // reset le compteur
                // diag haut-droit
                while(c+i<10 && l-i>=0){
                    if (this.matrice[c+i][l-i]==null){
                        coord[cmpt][0] = c+i;
                        coord[cmpt][1] = l-i;
                        i++;
                        cmpt++;
                    }else{
                        break;
                    }
                }

                i=1;        // reset le compteur
                // diag bas-droit
                while(c+i<10 && l+i<10){
                    if (this.matrice[c+i][l+i]==null){
                        coord[cmpt][0] = c+i;
                        coord[cmpt][1] = l+i;
                        i++;
                        cmpt++;
                    }else{
                        break;
                    }
                }

                i=1;
                // diag bas-gauche
                while(c-i>=0 && l+i<10){
                    if (this.matrice[c-i][l+i]==null){
                        coord[cmpt][0] = c-i;
                        coord[cmpt][1] = l+i;
                        i++;
                        cmpt++;
                    }else{
                        break;
                    }
                }
            }

            return Arrays.copyOf(coord, cmpt);  // "coupe" le tableau pour avoir uniquement les valeurs utiles

        }else{
            System.out.println("Oups, pas de pion dans cette case, mauvais appel de la fonction canMove !");
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
        return (nbPionBmourus>=20 || nbPionNmourus>=20);        // on sait jamais
    }


    /**
     * PriseObligatoire 2.0
     * */
    private int[][] priseObligatoire(){
        int[][] coord = new int[20][16];     // 20 pions qui ont une prise obligatoire max,
        // pour chacun 4 prises obligatoires (si dame, sinon 2 si pion) * 4 coord par possibilité -> 16
        int pions=0, positions=0;       // compte le nb de pions avec prise obligatoire
        // et pour chacun (reset) le nb de positions possibles

        // initialisation du tableau à 10, valeur d'arrêt de la lecture du tableau
        for (int a=0; a<20;a++){
            for (int b=0; b<16 ; b++){
                coord[a][b]=10;
            }
        }

        for (int c=0; c<10; c+=2){      // opti du parcours de la matrice (uniquement cases jouables)
            for (int l=(c+1)%2; l<10; l+=2){// same
                positions = 0;
                if (this.matrice[c][l] != null){     // si il y a un pion (sinon NullPointerException)
                    if (this.matrice[c][l].isWhite()==this.tour){    // si c'est le pion du joueur

                        //pion
                        if ( ! this.matrice[c][l].isDame()){
                            //blancs
                            if (this.matrice[c][l].isWhite()){
                                // haut-gauche
                                if (c-2>=0 && l-2>=0){      // si on ne sort pas du platal
                                    if(this.matrice[c-1][l-1]!=null){
                                        if (this.matrice[c-1][l-1].isWhite()!=this.tour && this.matrice[c-2][l-2]==null){       // on peut manger
                                            coord[pions][positions*4]=c;
                                            coord[pions][1+positions*4]=l;
                                            coord[pions][2+positions*4]=c-2;
                                            coord[pions][3+positions*4]=l-2;
                                            positions++;
                                        }
                                    }
                                }

                                // haut-droit
                                if (c+2<10 && l-2>=0){
                                    if(this.matrice[c+1][l-1]!=null){
                                        if (this.matrice[c+1][l-1].isWhite()!=this.tour && this.matrice[c+2][l-2]==null){       // on peut manger
                                            coord[pions][positions*4]=c;
                                            coord[pions][1+positions*4]=l;
                                            coord[pions][2+positions*4]=c+2;
                                            coord[pions][3+positions*4]=l-2;
                                            positions++;
                                        }
                                    }
                                }

                                if (positions>0){       // si on a trouvé un pion avec prise obligatoire, reset + actu du compteur
                                    positions=0;
                                    pions++;
                                }
                            }

                            // noirs
                            if (! this.matrice[c][l].isWhite()){
                                // bas-gauche
                                if (c-2>=0 && l+2<10){  //
                                    if(this.matrice[c-1][l+1]!=null){
                                        if (this.matrice[c-1][l+1].isWhite()!=this.tour && this.matrice[c-2][l+2]==null){       // on peut manger
                                            coord[pions][positions*4]=c;
                                            coord[pions][1+positions*4]=l;
                                            coord[pions][2+positions*4]=c-2;
                                            coord[pions][3+positions*4]=l+2;
                                            positions++;
                                        }
                                    }
                                }

                                // bas-droit
                                if (c+2<10 && l+2<10){  //
                                    if(this.matrice[c+1][l+1]!=null){
                                        if (this.matrice[c+1][l+1].isWhite()!=this.tour && this.matrice[c+2][l+2]==null){       // on peut manger
                                            coord[pions][positions*4]=c;
                                            coord[pions][1+positions*4]=l;
                                            coord[pions][2+positions*4]=c+2;
                                            coord[pions][3+positions*4]=l+2;
                                            positions++;
                                        }
                                    }
                                }

                                if (positions>0){       // si on a trouvé un pion avec prise obligatoire, reset + actu du compteur
                                    pions++;
                                }
                            }

                        }else{      // si c'est une dame

                            boolean finDiago = false, trouve=false;
                            int dist = 0;
                            // diago haut-gauche
                            do{
                                if (c-dist>=0 && l-dist>=0) {
                                    if (this.matrice[c - dist][l - dist] == null) {

                                    }
                                    dist++;
                                }
                            }while(!finDiago);


                            /*
                            Pour chaque diago :

                            bool finDiago = false;
                            int dist = 0;
                            Tant que ()

                             */
                        }

                    }
                }

            }
        }

        return coord;
    }

    // utile
    private int[][] formatePO(int[][] PO){
        int i=0;
        while(PO[i][0]!=10){
            i++;
        }
        return Arrays.copyOf(PO, i);
    }


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
