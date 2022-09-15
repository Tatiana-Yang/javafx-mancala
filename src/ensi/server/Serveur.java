package ensi.server;
import ensi.model.Jeu;
import ensi.model.Personne;
import ensi.model.PlateauJeu;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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
public class Serveur {

    private static Socket [] tabSockets = new Socket[2];
    private static Personne [] tabJoueurs = new Personne[2];
    private static int cpt = 0;
    private static PlateauJeu plateauJeu;
    private static Jeu jeu;
    public static int getCpt() {
        return cpt;
    }
    public static void setCpt(int cpt) {
        Serveur.cpt = cpt;
    }

    public static void main(String[] zero) {

        ServerSocket socketserver;
        Socket socketduserveur;
        //PlateauJeu plateauJeu = new PlateauJeu(null, null);


        try {
            socketserver = new ServerSocket(2009);
            System.out.println("Le Serveur est à l'écoute sur le port : " + socketserver.getLocalPort());

            // EN ATTENTE DE LA CONNEXION DE DEUX CLIENTS
            while(cpt < 2) {
                socketduserveur = socketserver.accept();
                System.out.println("Quelqu'un vient de se connecter !");

                ObjectInputStream ois = new ObjectInputStream(socketduserveur.getInputStream());
                Object [] response = null;
                try {
                    response = (Object[]) ois.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                if (response != null && response[0].equals("deconnexion-joueur")) {
                    try {
                        tabSockets[0].close();
                        tabJoueurs[0] = null;
                        tabSockets[0] = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    cpt -= 1;
                    System.out.println("Déconnexion du joueur");
                    System.out.println(cpt);
                }

                if (response != null && response[0].equals("connexion-joueur")) {
                    Personne pers = new Personne();
                    pers.setPseudo((String) response[1]);
                    pers.setLangue((String) response[2]);
                    pers.setIp("10.10.0." + cpt);
                    pers.setPort("2009");
                    System.out.println(pers);

                    OutputStream os = socketduserveur.getOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(os);
                    oos.writeObject(pers);  // envoie de l'objet
                    System.out.println("Données envoyées");

                    tabJoueurs[cpt] = pers;
                    tabSockets[cpt] = socketduserveur;
                    cpt += 1;

                    System.out.println("Connexion du joueur");
                    System.out.println(cpt);
                    for (int k = 0; k < cpt; k++) {
                        System.out.println(tabJoueurs[k].getPseudo());
                    }
                }
            }

            // CONSTRUCTION DU PLATEAU DE JEU
            plateauJeu = new PlateauJeu(tabJoueurs[0], tabJoueurs[1]);

            // Récupère le nombre de parties jouées jusqu'à présent
            if(!plateauJeu.checkEmptySauvegarde()) {
                plateauJeu = plateauJeu.chargerPartie();
            }

            // CONSTRUCTION DU JEU
            jeu = new Jeu(plateauJeu);

            // ENVOIE DU JEU AUX CLIENTS
            for(int i = 0; i < tabJoueurs.length; i++) {
                OutputStream os = tabSockets[i].getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);
                oos.writeObject(jeu);  // envoie de l'objet
            }

            Thread t1 = new Thread() {
                public void run() {
                    gestionClient(0);
                }
            };
            Thread t2 = new Thread() {
                public void run() {
                    gestionClient(1);
                }
            };
            t1.start();
            t2.start();

            //socketduserveur.close();
            //socketserver.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Cette méthode va pouvoir orchestrer toutes les communications faites avec le Client
     * Il va permettre l'envoie et la réception d'objet
     * @param index qui correspond à l'index du Client
     */
    public static void gestionClient(int index) {
        while(cpt > 0) {
            ObjectInputStream ois;
            Object [] response = null;
            try {
                ois = new ObjectInputStream(tabSockets[index].getInputStream());
                try {
                    response = (Object[]) ois.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (response != null && response[0].equals("deconnexion-joueur")) {
                try {
                    tabSockets[index].close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(tabJoueurs[index].getPseudo());
                for (int k = index; k < tabJoueurs.length - 1; k++) {
                    tabJoueurs[k] = tabJoueurs[k + 1];
                    tabSockets[k] = tabSockets[k + 1];
                }
                cpt -= 1;
                System.out.println("Déconnexion du joueur");
                System.out.println(cpt);
                for (int k = 0; k < cpt; k++) {
                    System.out.println(tabJoueurs[k].getPseudo());
                }
                //break;
            }

            if (response != null && response[0].equals("creer-partie")) {
                System.out.println("Création d'une partie");
                // initialise le plateau et on envoie le jeu
                plateauJeu.initialisation();
                plateauJeu.getCourant().setNbGrainesCapturees(0);
                plateauJeu.getAdverse().setNbGrainesCapturees(0);

                plateauJeu.getCourant().setPartieGagnee(0);
                plateauJeu.getAdverse().setPartieGagnee(0);
                jeu.setPlateauJeu(plateauJeu);
                jeu.setEnCours(true);
                for(int i = 0; i < tabJoueurs.length; i++) {
                    OutputStream os;
                    try {
                        os = tabSockets[i].getOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(os);
                        oos.writeObject(jeu);  // envoie de l'objet
                    } catch (IOException e) {
                        e.printStackTrace();
                        jeu.setEnCours(false);
                    }
                }
            }

            if (response != null && response[0].equals("creer-match")) {
                System.out.println("Création d'un match");
                // initialise le plateau et on envoie le jeu
                plateauJeu.initialisation();
                plateauJeu.setNbPartieJouee(0);
                jeu.setPlateauJeu(plateauJeu);
                jeu.setEnCours(true);
                for(int i = 0; i < tabJoueurs.length; i++) {
                    OutputStream os;
                    try {
                        os = tabSockets[i].getOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(os);
                        oos.writeObject(jeu);  // envoie de l'objet
                    } catch (IOException e) {
                        e.printStackTrace();
                        jeu.setEnCours(false);
                    }
                }
            }

            if (response != null && response[0].equals("charger-partie")) {
                System.out.println("Chargement d'une partie");
                try {
                    plateauJeu = plateauJeu.chargerPartie();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // initialise le plateau et on envoie le jeu
                jeu.setPlateauJeu(plateauJeu);
                jeu.setEnCours(true);
                for(int i = 0; i < tabJoueurs.length; i++) {
                    OutputStream os;
                    try {
                        os = tabSockets[i].getOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(os);
                        oos.writeObject(jeu);  // envoie de l'objet
                    } catch (IOException e) {
                        e.printStackTrace();
                        jeu.setEnCours(false);
                    }
                }
            }

            if (response != null && response[0].equals("jeu-choix")) {
                String indexReponse = String.valueOf(response[2]);
                jeu.getPlateauJeu().nourrir();

                int trouChoisi = Integer.parseInt(indexReponse);

                if(jeu.getPlateauJeu().getCourant().getTrou(trouChoisi).getAutorisation() && jeu.getPlateauJeu().getCourant().getTrou(trouChoisi).getNbGraines() != 0){

                    jeu.getPlateauJeu().distribution(trouChoisi);
                    jeu.getPlateauJeu().echangerJoueurs();
                }

                for(int i = 0; i < tabJoueurs.length; i++) {
                    OutputStream os;
                    try {
                        os = tabSockets[i].getOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(os);

                        // renvoie du jeu pour mise à jour
                        oos.writeObject(jeu);  // envoie de l'objet
                        System.out.println("Donnée envoyée");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (response != null && response[0].equals("stop-match")) {
                System.out.println("Stop le match");
                jeu = (Jeu) response[1];
                plateauJeu = jeu.getPlateauJeu();
                plateauJeu.initialisation();
                plateauJeu.resetSauvegardes();
                plateauJeu.setNbPartieJouee(0);
                jeu.setPlateauJeu(plateauJeu);
                int indextmp = (index + 1) % 2;
                OutputStream os;
                try {
                    os = tabSockets[indextmp].getOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(os);

                    // renvoie du jeu pour mise à jour
                    oos.writeObject(jeu);  // envoie de l'objet
                    System.out.println("Donnée envoyée");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (response != null && response[0].equals("recuperer-jeu")) {
                System.out.println("Récupère le jeu");
                OutputStream os;
                try {
                    os = tabSockets[index].getOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(os);

                    // renvoie du jeu pour mise à jour
                    oos.writeObject(jeu);  // envoie de l'objet
                    System.out.println("Donnée envoyée");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
