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
 * @version 2.0.0
 * Classe définissant le plateau de jeu, contenant les pions
 * ainsi que ses attributs (à définir, certainement sprite, position, les fonctions de l'affichage,
 * et les fonctions logiques du jeu)
 *
 * @see Pion
 * */

public class Plateau extends JPanel implements MouseListener{

    private Pion[][] matrice = new Pion[10][10];      // 10x10
    private boolean tour = true,        // actualisé à chaque tour, true = blanc, false = noir
            PO=false, fin = false, gamemode;
    private Image sprite, selectedSprite, haloR, haloJ, haloB, haloV, tourB, tourN,
            erreur_deplacement, erreur_mvtpossible, erreur_PO, erreur_selection, spriteErreur,
            victoire_B, victoire_N;

    private final int[] POSPREMIERPION = {50,50};
    private final int TAILLECASE = 60,      // avec le pixel de bordure
            PIONSPARLIGNE=7, NBERREURS=3;
    private final int TAILLEPLATAL = POSPREMIERPION[0]*2+10*TAILLECASE;


    private int[] selected = null;  // coord du pion sélectionné
    private int[][] mvtsPossibles = null;

    private Pion[] pionBmourus = new Pion[20], pionNmourus= new Pion[20];   // pour afficher les pions mangés sur le bord
    private int nbPionBmourus = 0, nbPionNmourus = 0, erreurs=0;


    Plateau(boolean gamemode){
        this.initPlateau(gamemode);
        this.addMouseListener(this);        // pour récupérer les clics & co
    }

    /**
     * Initialise le plateau avec les pions au bon endroit
     * @since 1.1
     * */
    private void initPlateau(boolean gamemode){
        // chargement des sprites
        try{
            this.sprite = ImageIO.read(new File("Files/platal.png"));
            this.selectedSprite = ImageIO.read(new File("Files/selected.png"));
            this.haloB = ImageIO.read(new File("Files/halo_bleu.png"));
            this.haloJ= ImageIO.read(new File("Files/halo_jaune.png"));
            this.haloR= ImageIO.read(new File("Files/halo_rouge.png"));
            this.haloV= ImageIO.read(new File("Files/halo_vert.png"));
            this.tourB = ImageIO.read(new File("Files/tour_B.png"));
            this.tourN = ImageIO.read(new File("Files/tour_N.png"));
            this.erreur_deplacement = ImageIO.read(new File("Files/erreur_deplacement.png"));
            this.erreur_mvtpossible = ImageIO.read(new File("Files/erreur_mvtpossible.png"));
            this.erreur_PO = ImageIO.read(new File("Files/erreur_PO.png"));
            this.erreur_selection = ImageIO.read(new File("Files/erreur_selection.png"));
            this.victoire_B = ImageIO.read(new File("Files/victoire_B.png"));
            this.victoire_N = ImageIO.read(new File("Files/victoire_N.png"));
        }catch(IOException e){
            e.printStackTrace();
        }
        this.gamemode = gamemode;

        // génération du plateau
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

        /*int[][] mvts = pionsBougeables();
        for (int i=0; i<mvts.length; i++){
            System.out.print("(");
            for (int j=0; j<2; j++){
                System.out.print(Integer.toString(mvts[i][j])+" ");
            }
            System.out.print(") ");
        }*/
    }

    /**
     * Actualise la position d'un pion dans la matrice
     * ET dans ses coordonnées perso (à enlever ?)
     * */
    private void bougePion(int cdepart, int ldepart, int carrivee, int larrivee){
        matrice[carrivee][larrivee] = matrice[cdepart][ldepart];    // on copie le pion dans la case cible
        matrice[cdepart][ldepart] = null;   // on vide la case de depart
        matrice[carrivee][larrivee].setPos(new int[] {carrivee, larrivee});
        this.repaint();
    }


    /**
     * --- A ECRIRE/DEBUG ---
     * Renvoie si le pion arrive dans le camp adverse, donc devient une dame
     * */
    public boolean dansCampAdverse(int c, int l){
        if (this.matrice[c][l]!=null) {
            return (this.matrice[c][l].isWhite() && l == 0) || (!this.matrice[c][l].isWhite() && l == 9);
        }else{
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

            if (selected==null){        // on a pas encore sélectionné de pion
                if (priseObligatoire()!=null){

                    if (this.dansPO(c,l)){
                        selected = new int[] {c,l};
                        mvtsPossibles = priseObligatoire(c,l);
                        PO = true;
                        erreurs = 0;
                    }else {
                        erreurs++;
                        spriteErreur = this.erreur_PO;
                    }
                }else if (matrice[c][l]!=null){     // si pas de PO
                    if (matrice[c][l].isWhite()==tour && canMove(c,l)!=null){       // si clic sur pion à soi qui peut bouger
                        selected = new int[] {c,l};
                        erreurs = 0;
                        mvtsPossibles = canMove(c,l);
                    }else {
                        erreurs++;
                        spriteErreur =  this.erreur_mvtpossible;
                    }
                }else {
                    erreurs++;
                    spriteErreur = this.erreur_selection;
                }

            }else{      // pion déjà sélectionné
                if (dansMvtsPossibles(c,l)){
                    bougePion(selected[0], selected[1], c,l);       // on déplace le pion
                    if (PO){        // si on a mangé un pion
                        mangePion(selected[0], selected[1],c,l);        // on mange le pion

                        if (priseObligatoire(c,l)!=null){      // s'il faut encore manger des pions

                            int[][] casesPO = priseObligatoire(c,l);

                            selected = new int[] {c,l};
                            mvtsPossibles = casesPO;
                            erreurs=0;
                        }else {     // plus de pions à manger, fin du tour
                            selected=null;
                            mvtsPossibles=null;
                            PO=false;
                            actualiseTour(c,l);
                        }
                    }else{
                        mvtsPossibles=null;
                        selected=null;
                        actualiseTour(c,l);
                    }
                }else{      // on a pas cliqué sur un mouvement possible
                    if (c==selected[0] && l==selected[1] && !PO){
                        selected = null;
                    }else {
                        erreurs++;
                        spriteErreur = this.erreur_deplacement;
//                        System.out.println("Déplacement impossible");
                    }
                }
            }
        }

        this.repaint();
    }

    private boolean dansMvtsPossibles(int c, int l){
        int[][] mvts = mvtsPossibles;
//        System.out.println("mvts possibles de la case "+selected[0]+", "+selected[1]);
        for (int i=0; i<mvts.length; i++){      // NullPointerException quand dame doit manger un pion en bas à gauche
//            System.out.print("("+mvts[i][0]+", "+mvts[i][1]+")");
            if (mvts[i][0]==c && mvts[i][1]==l){
                return true;
            }
        }
        return false;
    }

    private void actualiseTour(int carrivee, int larrivee){
        if (jeuFini()){
            finJeu();
        }
        erreurs=0;
        if (dansCampAdverse(carrivee,larrivee)){
            matrice[carrivee][larrivee].setDame();
        }

        PO = false;
        tour=!tour;

//        System.out.println("Changement de tour !");
    }

    private void finJeu(){
        fin=true;
    }

    /**
     * Update le platal quand on mange un pion
     * */
    private void mangePion(int c, int l){
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
//            System.out.println("Oups, on ne peut pas manger de pion à la case "+Integer.toString(l)+", "+Integer.toString(c));
        }
        this.repaint();
    }


    private void mangePion(int cdepart, int ldepart, int carrivee, int larrivee){
        if (! matrice[carrivee][larrivee].isDame()){      // pion a mangé
            mangePion((cdepart+carrivee)/2, (ldepart+larrivee)/2);      // pion mangé à la moyenne des coords du déplacement
        }else{      // dame : + rigolo
            if (carrivee<cdepart && larrivee<ldepart){      // HG
                mangePion(carrivee+1, larrivee+1);
            }else if (carrivee<cdepart && larrivee>ldepart){        // BG
                mangePion(carrivee+1, larrivee-1);
            }else if (carrivee>cdepart && larrivee<ldepart){        // HD
//                System.out.println("Mangepion HD dame");
                mangePion(carrivee-1, larrivee+1);
            }else {
                mangePion(carrivee-1, larrivee-1);      // BD
            }
        }
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
//            System.out.println("Oups, pas de pion dans cette case, mauvais appel de la fonction canMove !");
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
        int[][] coord = new int[20][10];     // 20 pions qui ont une prise obligatoire max,
        // pour chacun (2 coord) 4 prises obligatoires max (si dame, sinon 2 si pion) * 2 (coord par possibilité) -> 10
        int pions=0, positions;       // compte le nb de pions avec prise obligatoire
        // et pour chacun (reset) le nb de positions possibles

        for (int c=0; c<10; c++){      // opti du parcours de la matrice (uniquement cases jouables)
            for (int l=(c+1)%2; l<10; l+=2){    // same
                if (this.matrice[c][l] != null){     // si il y a un pion (sinon NullPointerException)
                    if (this.matrice[c][l].isWhite()==this.tour){    // si c'est le pion du joueur

                        positions=0;
                        /* ---
                        * Pion
                        * ----
                        * */
                        if ( ! this.matrice[c][l].isDame()){

                            // --- blancs ---
                            if (this.matrice[c][l].isWhite()){
                                // haut-gauche
                                if (c-2>=0 && l-2>=0){      // si on ne sort pas du platal
                                    if(this.matrice[c-1][l-1]!=null){
                                        if (this.matrice[c-1][l-1].isWhite()!=this.tour && this.matrice[c-2][l-2]==null){       // on peut manger
                                            coord[pions][0]=c;
                                            coord[pions][1]=l;
                                            coord[pions][2]=c-2;
                                            coord[pions][3]=l-2;
                                            positions++;
                                        }
                                    }
                                }

                                // haut-droit
                                if (c+2<10 && l-2>=0){
                                    if(this.matrice[c+1][l-1]!=null){
                                        if (this.matrice[c+1][l-1].isWhite()!=this.tour && this.matrice[c+2][l-2]==null){       // on peut manger
                                            if (positions==0) {
                                                coord[pions][0] = c;
                                                coord[pions][1]=l;
                                                coord[pions][2]=c+2;
                                                coord[pions][3]=l-2;
                                                positions++;
                                            }else {
                                                coord[pions][positions * 4] = c + 2;
                                                coord[pions][positions * 4 + 1] = l - 2;
                                                positions++;
                                            }
                                        }
                                    }
                                }

                                if (positions>0){       // si on a trouvé un pion avec prise obligatoire, reset + actu du compteur
                                    pions++;
                                }
                            }

                            // --- noirs ---
                            else{
                                // bas-gauche
                                if (c-2>=0 && l+2<10){  //
                                    if(this.matrice[c-1][l+1]!=null){
                                        if (this.matrice[c-1][l+1].isWhite()!=this.tour && this.matrice[c-2][l+2]==null){       // on peut manger
                                            coord[pions][0] = c;
                                            coord[pions][1]=l;
                                            coord[pions][2]=c-2;
                                            coord[pions][3]=l+2;
                                            positions++;
                                        }
                                    }
                                }

                                // bas-droit
                                if (c+2<10 && l+2<10){  //
                                    if(this.matrice[c+1][l+1]!=null){
                                        if (this.matrice[c+1][l+1].isWhite()!=this.tour && this.matrice[c+2][l+2]==null){       // on peut manger
                                            if (positions==0) {
                                                coord[pions][0] = c;
                                                coord[pions][1]=l;
                                                coord[pions][2]=c+2;
                                                coord[pions][3]=l+2;
                                                positions++;
                                            }else {
                                                coord[pions][positions * 4] = c + 2;
                                                coord[pions][positions * 4 + 1] = l + 2;
                                                positions++;
                                            }
                                        }
                                    }
                                }

                                if (positions>0){       // si on a trouvé un pion avec prise obligatoire, reset + actu du compteur
                                    pions++;
                                }
                            }

                        }else{      // si c'est une dame
                            /* ---
                            * Dame
                            * ----
                            * */

                            int dist = 1;

                            // diago haut-gauche
                            while(c-dist>0 && l-dist>0){    // la case derrière celle considérée ne dépasse pas du platal
                                if(this.matrice[c-dist][l-dist]!=null){ // on a trouvé un pion
                                    if (this.matrice[c-dist][l-dist].isWhite()!=this.tour && this.matrice[c-dist-1][l-dist-1]==null){
                                        // pion adverse & case derrière libre
                                        coord[pions][0]=c;
                                        coord[pions][1]=l;
                                        coord[pions][2]=c-dist-1;
                                        coord[pions][3]=l-dist-1;
                                        positions++;
                                    }
                                    break;
                                }else {
                                    dist++;
                                }
                            }

                            // diago haut-droit
                            dist=1;
                            while(c+dist<9 && l-dist>0) {    // la case derrière celle considérée ne dépasse pas du platal

                                if (this.matrice[c + dist][l - dist] != null) { // on a trouvé un pion
                                    if (this.matrice[c + dist][l - dist].isWhite() != this.tour && this.matrice[c + dist + 1][l - dist - 1] == null) {
                                        // pion adverse & case derrière libre
                                        if (positions==0) {
                                            coord[pions][0] = c;
                                            coord[pions][1]=l;
                                            coord[pions][2]=c+dist+1;
                                            coord[pions][3]=l-dist-1;
                                            positions++;
                                        }else {
                                            coord[pions][positions * 4] = c+dist+1;
                                            coord[pions][positions * 4 + 1] = l-dist-1;
                                            positions++;
                                        }
                                    }
                                    break;
                                } else {
                                    dist++;
                                }
                            }

                            // diago bas-droit
                            dist=1;
                            while(c+dist<9 && l+dist<9){    // la case derrière celle considérée ne dépasse pas du platal
                                if(this.matrice[c+dist][l+dist]!=null){ // on a trouvé un pion
                                    if (this.matrice[c+dist][l+dist].isWhite()!=this.tour && this.matrice[c+dist+1][l+dist+1]==null){
                                        // pion adverse & case derrière libre
                                        if (positions==0) {
                                            coord[pions][0] = c;
                                            coord[pions][1]=l;
                                            coord[pions][2]=c+dist+1;
                                            coord[pions][3]=l+dist+1;
                                            positions++;
                                        }else {
                                            coord[pions][positions * 4] = c+dist+1;
                                            coord[pions][positions * 4 + 1] = l+dist+1;
                                            positions++;
                                        }
                                    }
                                    break;
                                }else {
                                    dist++;
                                }
                            }

                            // diago bas-gauche
                            dist=1;
                            while(c-dist>0 && l+dist<9){    // la case derrière celle considérée ne dépasse pas du platal

                                if(this.matrice[c-dist][l+dist]!=null){ // on a trouvé un pion
                                    if (this.matrice[c-dist][l+dist].isWhite()!=this.tour && this.matrice[c-dist-1][l+dist+1]==null){
                                        // pion adverse & case derrière libre
                                        if (positions==0) {
                                            coord[pions][0] = c;
                                            coord[pions][1]=l;
                                            coord[pions][2]=c-dist-1;
                                            coord[pions][3]=l+dist+1;
                                            positions++;
                                        }else {
                                            coord[pions][positions * 4] = c-dist-1;
                                            coord[pions][positions * 4 + 1] = l+dist+1;
                                            positions++;
                                        }
                                        // pas de positions++ car inutile ici
                                    }
                                    break;
                                }else {
                                    dist++;
                                }
                            }
                            if (positions>0){       // si on a trouvé un pion avec prise obligatoire, reset + actu du compteur
                                pions++;
                            }
                        }
                    }
                }
            }
        }
        if (pions>0) {
            return coord;
        }else{
            return null;
        }
    }

    private int[][] pionsBougeables(){
        int[][] coord = new int[50][2] ;
        int pos = 0;
        for (int c=0; c<10; c++) {      // opti du parcours de la matrice (uniquement cases jouables)
            for (int l = (c + 1) % 2; l < 10; l += 2) {    // same
                if (matrice[c][l]!=null){
                    if (canMove(c,l)!=null){
                        coord[pos][0]=c;
                        coord[pos][1]=l;
                        pos++;
                    }
                }
            }
        }
        if (pos>0) {
            return Arrays.copyOf(coord, pos);
        }else {     // ne devrait techniquement pas arriver ?
            return null;
        }
    }

    private int[][] priseObligatoire(int c, int l){
        if (this.matrice[c][l]==null){
//            System.out.println("Oups ! pas de pion par ici pour PO !");
            return null;
        }else{
            if (this.matrice[c][l].isWhite() == tour){
                int positions = 0;
                int[][] coord;

                /*
                * ----
                * Dame
                * ----
                * */
                if (this.matrice[c][l].isDame()){

                    coord = new int[4][2];

                    int dist = 1;

                    // diago haut-gauche
                    while(c-dist>0 && l-dist>0){    // la case derrière celle considérée ne dépasse pas du platal
                        if(this.matrice[c-dist][l-dist]!=null){ // on a trouvé un pion
                            if (this.matrice[c-dist][l-dist].isWhite()!=this.tour && this.matrice[c-dist-1][l-dist-1]==null){
                                // pion adverse & case derrière libre
                                coord[0][0]=c-dist-1;
                                coord[0][1]=l-dist-1;
                                positions++;
                            }
                            break;
                        }else {
                            dist++;
                        }
                    }

                    // diago haut-droit
                    dist=1;
                    while(c+dist<9 && l-dist>0) {    // la case derrière celle considérée ne dépasse pas du platal

                        if (this.matrice[c + dist][l - dist] != null) { // on a trouvé un pion
                            if (this.matrice[c + dist][l - dist].isWhite() != this.tour && this.matrice[c + dist + 1][l - dist - 1] == null) {
                                // pion adverse & case derrière libre
                                coord[positions][0]=c+dist+1;
                                coord[positions][1]=l-dist-1;
                                positions++;
                            }
                            break;
                        } else {
                            dist++;
                        }
                    }

                    // diago bas-droit
                    dist=1;
                    while(c+dist<9 && l+dist<9){    // la case derrière celle considérée ne dépasse pas du platal
                        if(this.matrice[c+dist][l+dist]!=null){ // on a trouvé un pion
                            if (this.matrice[c+dist][l+dist].isWhite()!=this.tour && this.matrice[c+dist+1][l+dist+1]==null){
                                // pion adverse & case derrière libre
                                coord[positions][0]=c+dist+1;
                                coord[positions][1]=l+dist+1;
                                positions++;
                            }
                            break;
                        }else {
                            dist++;
                        }
                    }

                    // diago bas-gauche
                    dist=1;
                    while(c-dist>0 && l+dist<9){    // la case derrière celle considérée ne dépasse pas du platal

                        if(this.matrice[c-dist][l+dist]!=null){ // on a trouvé un pion
                            if (this.matrice[c-dist][l+dist].isWhite()!=this.tour && this.matrice[c-dist-1][l+dist+1]==null){
                                // pion adverse & case derrière libre
                                coord[positions][0]=c-dist-1;
                                coord[positions][1]=l+dist+1;
                                positions++;
                            }
                            break;
                        }else {
                            dist++;
                        }
                    }


                    // --- PION ---
                }else {
                    if (!this.PO) {

                            coord = new int[2][2];

                            //blancs
                            if (this.matrice[c][l].isWhite()) {
                                // haut-gauche
                                if (c - 2 >= 0 && l - 2 >= 0) {      // si on ne sort pas du platal
                                    if (this.matrice[c - 1][l - 1] != null) {
                                        if (this.matrice[c - 1][l - 1].isWhite() != this.tour && this.matrice[c - 2][l - 2] == null) {       // on peut manger
                                            coord[positions][0] = c - 2;
                                            coord[positions][1] = l - 2;
                                            positions++;
                                        }
                                    }
                                }

                                // haut-droit
                                if (c + 2 < 10 && l - 2 >= 0) {
                                    if (this.matrice[c + 1][l - 1] != null) {
                                        if (this.matrice[c + 1][l - 1].isWhite() != this.tour && this.matrice[c + 2][l - 2] == null) {       // on peut manger
                                            coord[positions][0] = c + 2;
                                            coord[positions][1] = l - 2;
                                            positions++;
                                        }
                                    }
                                }
                            }

                            // noirs
                            else {
                                // bas-gauche
                                if (c - 2 >= 0 && l + 2 < 10) {
                                    if (this.matrice[c - 1][l + 1] != null) {
                                        if (this.matrice[c - 1][l + 1].isWhite() != this.tour && this.matrice[c - 2][l + 2] == null) {       // on peut manger
                                            coord[positions][0] = c - 2;
                                            coord[positions][1] = l + 2;
                                            positions++;
                                        }
                                    }
                                }

                                // bas-droit
                                if (c + 2 < 10 && l + 2 < 10) {
                                    if (this.matrice[c + 1][l + 1] != null) {
                                        if (this.matrice[c + 1][l + 1].isWhite() != this.tour && this.matrice[c + 2][l + 2] == null) {       // on peut manger
                                            coord[positions][0] = c + 2;
                                            coord[positions][1] = l + 2;
                                            positions++;
                                        }
                                    }
                                }
                            }

                        }// fin !PO
                        else{       // si PO : prise dans tous les sens
                            coord = new int[4][2];
                            // haut-gauche
                            if (c - 2 >= 0 && l - 2 >= 0) {      // si on ne sort pas du platal
                                if (this.matrice[c - 1][l - 1] != null) {
                                    if (this.matrice[c - 1][l - 1].isWhite() != this.tour && this.matrice[c - 2][l - 2] == null) {       // on peut manger
                                        coord[positions][0] = c - 2;
                                        coord[positions][1] = l - 2;
                                        positions++;
                                    }
                                }
                            }
                            // haut-droit
                            else if (c + 2 < 10 && l - 2 >= 0) {
                                if (this.matrice[c + 1][l - 1] != null) {
                                    if (this.matrice[c + 1][l - 1].isWhite() != this.tour && this.matrice[c + 2][l - 2] == null) {       // on peut manger
                                        coord[positions][0] = c + 2;
                                        coord[positions][1] = l - 2;
                                        positions++;
                                    }
                                }
                            }
                            // bas-gauche
                            if (c - 2 >= 0 && l + 2 < 10) {
                                if (this.matrice[c - 1][l + 1] != null) {
                                    if (this.matrice[c - 1][l + 1].isWhite() != this.tour && this.matrice[c - 2][l + 2] == null) {       // on peut manger
                                        coord[positions][0] = c - 2;
                                        coord[positions][1] = l + 2;
                                        positions++;
                                    }
                                }
                            }
                            // bas-droit
                            if (c + 2 < 10 && l + 2 < 10) {
                                if (this.matrice[c + 1][l + 1] != null) {
                                    if (this.matrice[c + 1][l + 1].isWhite() != this.tour && this.matrice[c + 2][l + 2] == null) {       // on peut manger
                                        coord[positions][0] = c + 2;
                                        coord[positions][1] = l + 2;
                                        positions++;
                                    }
                                }
                            }
                        }
                    }// fin pion


                    if (positions>0)
                        return Arrays.copyOf(coord, positions);
                    else {
                        return null;
                    }

                } else {        // si on a appelé PO pour un pion adverse (but why tho ?)
//                    System.out.println("Pas touche au pion adverse ! (PO)");
                    return null;
            }
        }
    }


    private boolean dansPO(int c, int l){
        int[][] PO = this.priseObligatoire();       // pour pas recalculer
        for (int i=0; i<PO.length; i++){
            if (PO[i][0]==c && PO[i][1]==l){        // si le pion demandé est dans la liste des PO
                return true;
            }
        }
        return false;
    }

    /**
     * Le joyeux bazar de l'affichage --- RIP smiley 2018-2018 ---
     * @version 1.5
     * @since 1.1
     * */
    public void paintComponent(Graphics g) {

        if (!fin) {
            g.drawImage(this.sprite, 0, 0, this);     // platal

            // les pions du plateau
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    try {
                        g.drawImage(matrice[i][j].getSprite(), matrice[i][j].getPos()[0] * this.TAILLECASE + this.POSPREMIERPION[0],
                                matrice[i][j].getPos()[1] * this.TAILLECASE + this.POSPREMIERPION[1], this);
                    } catch (NullPointerException e) {        // un peu malpropre
                    }
                }
            }

            if (this.selected != null) {
                g.drawImage(this.selectedSprite, POSPREMIERPION[0] + this.selected[0] * this.TAILLECASE,
                        POSPREMIERPION[1] + this.selected[1] * this.TAILLECASE, this);
            }

            // les pions mourus
            for (int i = 0; i < nbPionBmourus; i++) {
                g.drawImage(this.pionBmourus[i].getSprite(), this.TAILLEPLATAL + 20 + (i % PIONSPARLIGNE) * 30,
                        this.POSPREMIERPION[1] + (i / PIONSPARLIGNE) * TAILLECASE, this);  // modulo à voir
            }
            for (int i = 0; i < nbPionNmourus; i++) {
                g.drawImage(this.pionNmourus[i].getSprite(), this.TAILLEPLATAL + 20 + (i % PIONSPARLIGNE) * 30,
                        this.TAILLEPLATAL - this.POSPREMIERPION[1] - (4 * this.TAILLECASE) + (i / PIONSPARLIGNE) * TAILLECASE, this);  // modulo à voir

            }

            // aide
            if (erreurs>=this.NBERREURS){
                if (selected !=null){
                    for (int[] pos : mvtsPossibles) {
                        g.drawImage((PO)? haloR : haloB, POSPREMIERPION[0] + pos[0] * TAILLECASE,
                                POSPREMIERPION[1] + pos[1] * TAILLECASE, this);
                    }
                }
                g.drawImage(this.spriteErreur, TAILLEPLATAL/2-300, TAILLEPLATAL/2-50, this);
            }

            // le tour actuel
            g.drawImage((tour)? tourB : tourN, TAILLEPLATAL + 80, TAILLEPLATAL/2 - 16, this);
        }else{
//            g.drawString("GG WP fin du jeu", 300,300);
            g.drawImage((!tour)? victoire_B : victoire_N, 0,0,this);
        }
    }
}
