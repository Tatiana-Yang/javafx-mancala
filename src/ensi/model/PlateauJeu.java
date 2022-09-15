package ensi.model;

import java.io.*;
import java.util.Random;
import java.util.Scanner;

/*
 * ENSICAEN
 * 6 Boulevard Maréchal Juin
 * F-14050 Caen Cedex
 *
 * This file is owned by ENSICAEN students. No portion of this
 * document may be reproduced, copied or revised without written
 * permission of the authors.
 */

/**
 * @author Mathilde LERAY mathilde.leray@ecole.ensicaen.fr
 * @author Tatiana YANG tatiana.yang@ecole.ensicaen.fr
 * @version 1.0 - 2021-06-20
 */

/**
 * PlateauJeu est notre classe qui nous permet de réaliser notre jeu Mancala
 */
public class PlateauJeu implements Serializable{

    private Personne courant;
    private Personne adverse;
    private Personne joueur1;
    private Personne joueur2;
    private int nbPartieJouee;
    private char etat;


    public PlateauJeu(Personne joueur1, Personne joueur2) {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
        this.nbPartieJouee = 0;
        this.etat = 'X';

        //this.courant = joueur1;
        //this.adverse = joueur2;

        //Choix du premier joueur
        Random random = new Random();
        int randInt = random.nextInt(100);
        randInt = 2; // A ENLEVER
        if(randInt % 2 == 0){
            setCourant(getJoueur1());
            setAdverse(getJoueur2());
        }
        else{
            setCourant(getJoueur2());
            setAdverse(getJoueur1());
        }
    }

    // ACCESSEURS & MUTATEURS

    public Personne getCourant() {
        return this.courant;
    }
    public Personne getAdverse() {
        return this.adverse;
    }

    public void setCourant(Personne joueur) {
        this.courant = joueur;
    }

    public void setAdverse(Personne joueur) {
        this.adverse = joueur;
    }

    public int getNbGrainePlateau() {
        int nbGraine = this.courant.getNbGrainePlateau() + this.adverse.getNbGrainePlateau();
        return nbGraine;
    }

    public int getNbPartieJouee(){
        return this.nbPartieJouee;
    }

    public void setNbPartieJouee(int nb){
        this.nbPartieJouee = nb;
    }

    public Personne getJoueur1(){
        return this.joueur1;
    }

    public Personne getJoueur2(){
        return this.joueur2;
    }

    // MÉTHODES

    /**
     * echangerJoueurs échange le joueur courant et le joueur adverse
     */
    public void echangerJoueurs() {
        Personne tmpJoueur = this.courant;
        this.courant = this.adverse;
        this.adverse = tmpJoueur;
    }

    /**
     * echangerJoueurs permet d'échanger la cible, si elle était autrefois égale au joueur
     * courrant on retourne le joueur adverse afin de le récupérer dans une variable
     * @param cible est une instance de Personne qui permet de connaitre le joueur dans lequel nous distribuons nos graines
     * @return la personne qui n'est pas égale à la cible
     */
    public Personne echangerJoueurs(Personne cible) {
        if (cible.getPseudo().equals(this.courant.getPseudo())) {
            this.courant = cible;
            return this.adverse;
        }
        this.adverse = cible;
        return this.courant;
    }

    /**
     * Cette fonction nourrir, permet d'autoriser ou non la distribution de graine dans un trou.
     * Si le joueur adverse ne contient aucune graine dans sa partie de plateau, nous devons jouer un coup qui va lui
     * permettre d'obtenir des graines de nouveaux
     */
    public void nourrir() {
        if (this.adverse.getNbGrainePlateau() == 0) {
            int compteurIndex = 0;
            for (Trou trou : this.courant.getTabTrou()) {
                int calcul = trou.getNbGraines() + compteurIndex;
                if (calcul - 6 < 0) {
                    trou.setAutorisation(false);
                } else {
                    trou.setAutorisation(true);
                }
                compteurIndex++;
            }
        }
        else{
            for (Trou trou : this.courant.getTabTrou()) {
                trou.setAutorisation(true);
            }
        }
    }

    /**
     * Cette fonction permet de savoir si nous affamons notre adversaire ou non, c'est à dire que nous prenons la totalité de ces graines
     * @param cible correspond au joueur à qui appartient le dernier trou que nous avons distribué
     * @param index correspond à l'index du dernier trou distribué
     * @return booléen, false si nous n'affamons pas notre adversaire et true dans l'autre cas
     */
    public boolean affamer(Personne cible, int index){
        if(!cible.getPseudo().equals(this.courant.getPseudo())){
            for(int i = index; i >= 0; i--){
                // Si tous les trous précédent ainsi que le dernier trous distribué ne sont pas égaux à 2 ou 3 alors nous n'affamons pas notre adversaire
                if(cible.getTrou(index).getNbGraines() != 2 && cible.getTrou(index).getNbGraines() != 3){
                    return false;
                }
            }
            for(int i = index+1; i < 6; i++){
                //Si tous les trous après le dernier trou distribué sont différent de 0 alors nous ne pouvons pas affamer notre adversaire
                if(cible.getTrou(i).getNbGraines() != 0){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * distribution permet de distribuer les graines présentent dans un trou et permet également de récupérer les graines
     * gagnées
     * @param index du trou contenant les graines que nous voulons distribuer
     */
    public void distribution(int index) {
        Personne cible = this.courant;
        int indexDepart = index;

        int nbGraines = this.courant.getTrou(index).getNbGraines();

        // vide le trou choisi par le joeur courant
        cible.getTrou(index).setNbGraines(0);

        // On distribue les graines
        while (nbGraines > 0) {
            index += 1;
            if (index > 5) {
                cible = echangerJoueurs(cible);
                index = 0;
            }

            if (index != indexDepart || !cible.getPseudo().equals(this.courant.getPseudo())) {
                cible.getTrou(index).incrementeNbGraines();
            }
            nbGraines -= 1;
        }

        Trou trouCourrant = cible.getTrou(index);
        int nbGrainesCourrant = trouCourrant.getNbGraines();

        /*On verifie que le nombre de graine est de 2 ou 3, ainsi que le dernier trou dans laquelle nous avons
        mis notre graine n'appartient pas au joueur courant, et pour finir nous vérifions que nous n'affamons pas notre adversaire
        */
        while ((nbGrainesCourrant == 2 || nbGrainesCourrant == 3) && !cible.getPseudo().equals(this.courant.getPseudo())
                && !affamer(cible, index)) {
            // Ajout des graines dans le trou au nombre de graine déjà capturé
            this.courant.ajoutGraineCapturees(nbGrainesCourrant);

            index -= 1;
            if (index < 0) {
                index = 5;
                cible = echangerJoueurs(cible);
            }
            trouCourrant.setNbGraines(0);
            trouCourrant = cible.getTrou(index);
            nbGrainesCourrant = trouCourrant.getNbGraines();

        }
    }

    /**
     * Cette methode permet d'initialiser les trous des joueurs au début d'une nouvelle partie
     */
    public void initialisation() {
        this.courant.initialiserTrous();
        this.adverse.initialiserTrous();
    }


    /**
     * Cette methode permet de vérifier si le fichier de sauvegarde est vide
     * @return booléen (true si fichier vide, false sinon)
     */
    public boolean checkEmptySauvegarde() {
        File file = new File("src/ensi/ressources/sauvegarde.txt");
        return file.length() == 0;
    }

    /**
     * Cette methode permet de vérifier si le fichier de sauvegarde correspond à un début de partie
     * @return booléen (true si début de partie, false sinon)
     */
    public boolean checkDebutSauvegarde() {
        for(Trou trou : this.joueur1.getTabTrou()) {
            System.out.println(trou.getNbGraines());
            if(trou.getNbGraines() != 4) {
                return false;
            }
        }
        for(Trou trou : this.joueur2.getTabTrou()) {
            if(trou.getNbGraines() != 4) {
                return false;
            }
        }
        return true;
    }

    /**
     * Vide et réinitialise tout le fichier de sauvegarde (remise à zéro)
     */
    public void resetSauvegardes() {
        FileWriter fichierSauvegarde;
        try {
            fichierSauvegarde = new FileWriter("src/ensi/ressources/sauvegarde.txt");
            fichierSauvegarde.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sauvegarde de la partie actuelle dans un fichier texte présent dans les ressources
     * @throws IOException
     */
    public void sauvegarde() throws IOException {
        FileWriter fichierSauvegarde = new FileWriter("src/ensi/ressources/sauvegarde.txt");
        fichierSauvegarde.write(this.nbPartieJouee + "\n");
        fichierSauvegarde.write(this.courant.getPseudo() + "\n");
        fichierSauvegarde.write(this.courant.getPartieGagnee() + "\n");
        fichierSauvegarde.write(this.courant.getNbGrainesCapturees() + "\n");
        fichierSauvegarde.write(this.courant.toString() + "\n");
        fichierSauvegarde.write(this.adverse.getPseudo() + "\n");
        fichierSauvegarde.write(this.adverse.getPartieGagnee() + "\n");
        fichierSauvegarde.write(this.adverse.getNbGrainesCapturees() + "\n");
        fichierSauvegarde.write(this.adverse.toString());

        fichierSauvegarde.close();
    }

    /**
     * Permet de charger une partie sauvegardé dans le fichier de sauvegarde
     * @return un Plateau de Jeu initialisé à partir des données présents dans le fichier de sauvegarde
     * @throws IOException
     */
    public PlateauJeu chargerPartie() throws IOException {
        FileInputStream fichier = new FileInputStream("src/ensi/ressources/sauvegarde.txt");
        Scanner scanner = new Scanner(fichier);
        PlateauJeu jeu = new PlateauJeu(this.joueur1, this.joueur2);

        while (scanner.hasNextLine()) {
            jeu.setNbPartieJouee(Integer.parseInt(scanner.nextLine()));
            String pseudo1 = scanner.nextLine();
            int partie1 = Integer.parseInt(scanner.nextLine());
            int nbGraines1 = Integer.parseInt(scanner.nextLine());
            String tabTrou1 = scanner.nextLine();

            String pseudo2 = scanner.nextLine();
            int partie2 = Integer.parseInt(scanner.nextLine());
            int nbGraines2 = Integer.parseInt(scanner.nextLine());
            String tabTrou2 = scanner.nextLine();

            if(pseudo1.equals(this.joueur1.getPseudo())) {
                this.joueur1.setPartieGagnee(partie1);
                this.joueur1.setNbGrainesCapturees(nbGraines1);
                int compteur = 0;
                for (int i = 0; i < tabTrou1.length(); i++) {
                    if (tabTrou1.charAt(i) != ' ') {
                        this.joueur1.setTrou(compteur, Integer.parseInt(String.valueOf(tabTrou1.charAt(i))));
                        compteur++;
                    }
                }

                this.joueur2.setPartieGagnee(partie2);
                this.joueur2.setNbGrainesCapturees(nbGraines2);
                compteur = 0;
                for (int i = 0; i < tabTrou2.length(); i++) {
                    if (tabTrou2.charAt(i) != ' ') {
                        this.joueur2.setTrou(compteur, Integer.parseInt(String.valueOf(tabTrou2.charAt(i))));
                        compteur++;
                    }
                }
            } else {
                this.joueur1.setPartieGagnee(partie2);
                this.joueur1.setNbGrainesCapturees(nbGraines2);
                int compteur = 0;
                for (int i = 0; i < tabTrou2.length(); i++) {
                    if (tabTrou2.charAt(i) != ' ') {
                        this.joueur1.setTrou(compteur, Integer.parseInt(String.valueOf(tabTrou2.charAt(i))));
                        compteur++;
                    }
                }

                this.joueur2.setPartieGagnee(partie1);
                this.joueur2.setNbGrainesCapturees(nbGraines1);
                compteur = 0;
                for (int i = 0; i < tabTrou1.length(); i++) {
                    if (tabTrou1.charAt(i) != ' ') {
                        this.joueur2.setTrou(compteur, Integer.parseInt(String.valueOf(tabTrou1.charAt(i))));
                        compteur++;
                    }
                }
            }

            /*jeu.setNbPartieJouee(Integer.parseInt(scanner.nextLine()));
            //jeu.joueur1.setPseudo(scanner.nextLine());
            scanner.nextLine();
            jeu.joueur1.setPartieGagnee(Integer.parseInt(scanner.nextLine()));
            jeu.joueur1.setNbGrainesCapturees(Integer.parseInt(scanner.nextLine()));
            String tabTrou = scanner.nextLine();
            int compteur = 0;
            for (int i = 0; i < tabTrou.length(); i++) {
                if (tabTrou.charAt(i) != ' ') {
                    jeu.joueur1.setTrou(compteur, Integer.parseInt(String.valueOf(tabTrou.charAt(i))));
                    compteur++;
                }
            }

            //jeu.joueur2.setPseudo(scanner.nextLine());
            scanner.nextLine();
            jeu.joueur2.setPartieGagnee(Integer.parseInt(scanner.nextLine()));
            jeu.joueur2.setNbGrainesCapturees(Integer.parseInt(scanner.nextLine()));
            String tabTrou2 = scanner.nextLine();
            compteur = 0;
            for (int i = 0; i < tabTrou2.length(); i++) {
                if (tabTrou2.charAt(i) != ' ') {
                    jeu.joueur2.setTrou(compteur, Integer.parseInt(String.valueOf(tabTrou2.charAt(i))));
                    compteur++;
                }
            }*/
        }
        fichier.close();
        return jeu;
    }

    /**
     * Permet le partage de graine présente sur le plaeau, après abandon des deux joueurs
     */
    public void partageGraine(){
        int nbGrainePlateau = getNbGrainePlateau();
        if(nbGrainePlateau <= 10){
            int moitie = (int) nbGrainePlateau/2;
            this.joueur1.ajoutGraineCapturees(moitie);
            this.joueur2.ajoutGraineCapturees(moitie);
        }
    }

    /**
     * Mise à jour du nombre de partie jouée, et du nombre de partie gagné pour chaque joueur
     */
    public void misAJourPoint() {
        if(gagnant() != null){
            gagnant().setPartieGagnee(gagnant().getPartieGagnee() + 1);
        }
        setNbPartieJouee(getNbPartieJouee() + 1);
    }

    /**
     * Permet de connaitre la fin d'un match, qui correspond à 6 parties jouées
     * @return un boolean qui nous permet de connaitre la fin d'un match ou non
     */
    public boolean finMatch(){
        if(this.nbPartieJouee == 6){
            return true;
        }
        return false;
    }

    /**
     * Permet de connaitre lorsque le joueur courant n'a plus de graine à jouer,
     * ou bien si nous ne pouvons pas nourrir notre adversaire
     * @return true pour l'arret de la pzrtie, false sinon
     */
    private boolean arretDebutant() {
        if (getCourant().getNbGrainePlateau() == 0) {
            return true;
        }
        int compteur = 0;
        // On compte le nombre de trou ou nous ne somme pas autorisé à déplacé
        for (Trou trou : getCourant().getTabTrou()) {
            if (!trou.getAutorisation()) {
                compteur += 1;
            }
        }
        if (compteur == getCourant().getTabTrou().length) {
            return true;
        }
        return false;
    }

    /**
     * La partie se termine lorsque l'un des deux joueurs a atteint 25 graines capturées
     * ou bien que l'un des deux joueurs abandonnent la partie
     * ou bien si le joueur ne peut pas nourrir son adversaire
     * ou bien qu'il y ait moins de 6 graines sur le plateau
     * @return true pour l'arret du jeu, false sinon
     */
    public boolean arret() {
        if (getJoueur1().getNbGrainesCapturees() >= 25 || getJoueur2().getNbGrainesCapturees() >= 25) {
            return true;
        }
        if (getJoueur1().getAbandon() || getJoueur2().getAbandon()) {
            return true;
        }
        if (getNbGrainePlateau() <= 6 && arretDebutant()) {
            return true;
        }
        if(getNbGrainePlateau() < 6){
            return true;
        }
        return false;
    }

    /**
     * Permer de savoir si l'un des deux joueurs à plus de 24 graines
     * @return true si oui, false sinon
     */
    private boolean plus24Graines() {
        return this.joueur1.getNbGrainesCapturees() > 24 || this.joueur2.getNbGrainesCapturees() > 24;
    }

    /**
     * Permet de connaitre le gagnant de la partie
     * @return le gagnant
     */
    public Personne gagnant() {
        if (getNbGrainePlateau() < 6 && !plus24Graines()) {
            return null;
        }
        if (getJoueur1().getAbandon() && getJoueur2().getAbandon()) {
            partageGraine();
            return joueurPlusGraine();
        } else if ((getJoueur1().getAbandon() || getJoueur2().getAbandon()) && getNbGrainePlateau() > 10) {
            if (getJoueur1().getAbandon()) {
                return this.joueur2;
            } else {
                return this.joueur1;
            }
        }
        if (getJoueur1().getNbGrainesCapturees() >= 25 || getJoueur2().getNbGrainesCapturees() >= 25) {
            return joueurPlusGraine();
        }
        return joueurPlusGraine();
    }

    /**
     * Cette fonction permet de connaitre qui est le joueur contenant le plus de graines
     * @return
     */
    private Personne joueurPlusGraine() {
        if (this.joueur1.getNbGrainesCapturees() < this.joueur2.getNbGrainesCapturees()) {
            return this.joueur2;
        } else if (this.joueur2.getNbGrainesCapturees() < this.joueur1.getNbGrainesCapturees()) {
            return this.joueur1;
        }
        return null;
    }
}