
/**
 * --- A ECRIRE ---
 * Là où on lance le jeu !
 * Contient la structure logique du jeu
 * @see Plateau
 * @author Emma
 * @version 1.1
 *
 */

public class Game {

    public static void main(String[] args) {

        Menu menu = new Menu();

//        FenetreJeu fenetre = new FenetreJeu(false);

    }

/*
    public static int[][] priseObligatoire (boolean joueur) { //joueur noir : false , joueur blanc : true
        int[][] coordonees = new int[2][4]; //on aura dans chaque colone de 4 case ligne du pion à jouer, colone du pion, ligne case, colone case
        int a = 0;

        for(int i = 0 ; i<Plateau.matrice.length ; i++){
            for (int j = 0 ; j<Plateau.matrice[].length ; j++){

                //pour un pion
                if(Plateau.matrice[i][j] != null && Plateau.matrice[i][j].color == joueur && !Plateau.matrice[i][j].isDame()){ //si il y a un pion de la couleur du joueur
                    if(joueur){//joueur blanc
                        if(j>1 && Plateau.matrice[i-1][j-1] != null && Plateau.matrice[i-1][j-1].color != joueur && Plateau.matrice[i-2][j-2] = null){
                            coordonees[a][0] = i;
                            coordonees[a][0] = j;
                            coordonees[a][0] = i-1;
                            coordonees[a][0] = j-1;
                            a++;
                        }//if il y a un pion adverse en diag à gauche et une case libre derière

                        if(j<6 && Plateau.matrice[i-1][j+1] != null && Plateau.matrice[i-1][j+1].color != joueur && Plateau.matrice[i-2][j+2] = null){
                            coordonees[a][0] = i;
                            coordonees[a][0] = j;
                            coordonees[a][0] = i-1;
                            coordonees[a][0] = j+1;
                            a++;
                        }//if il y a un pion adverse en diag à droite et une case libre derière
                    }//if joueur blanc

                    if(!joueur){//joueur noir
                        if(j>1 && Plateau.matrice[i+1][j-1] != null && Plateau.matrice[i+1][j-1].color != joueur && Plateau.matrice[i+2][j-2] = null){
                            coordonees[a][0] = i;
                            coordonees[a][0] = j;
                            coordonees[a][0] = i+1;
                            coordonees[a][0] = j-1;
                            a++;
                        }//if il y a un pion adverse en diag à gauche et une case libre derière

                        if(j<6 && Plateau.matrice[i+1][j+1] != null && Plateau.matrice[i+1][j+1].color != joueur && Plateau.matrice[i+2][j+2] = null){
                            coordonees[a][0] = i;
                            coordonees[a][0] = j;
                            coordonees[a][0] = i+1;
                            coordonees[a][0] = j+1;
                            a++;
                        }//if il y a un pion adverse en diag à droite et une case libre derière
                    }//if joueur noir
                }//if pion bonne couleur

                //pour une damme
                if(Plateau.matrice[i][j] != null && Plateau.matrice[i][j].color == joueur && Plateau.matrice[i][j].isDame()){ //si il y a une dame de la couleur du joueur

                }//if dame bonne couleur

            }//for j
        }//for i

        return coordonees;

    }//priseObligatoirePion*/

}
