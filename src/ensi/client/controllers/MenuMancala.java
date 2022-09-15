package ensi.client.controllers;

import ensi.client.Client;
import ensi.model.Jeu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

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
public class MenuMancala implements Initializable {

    @FXML
    private Button retourBtn;

    @FXML
    private Pane inGamePane;

    @FXML
    private Button stopMatchBtn;

    @FXML
    private Button abandonnerPartieBtn;

    @FXML
    private Button savePartieBtn;

    @FXML
    private Pane notInGamePane;

    @FXML
    private Button nouveauMatchBtn;

    @FXML
    private Button loadPartie;

    @FXML
    void abandonnerPartie(ActionEvent event) {
        Client.getPers().setAbandon(true);
    }

    /**
     * Permet de modifier notre scene en ouvrant notre plateau
     */
    @FXML
    void nouveauMatch() {
        ObjectOutputStream oos;
        if(Client.getJeu().getPlateauJeu().checkEmptySauvegarde()) {
            try {
                oos = new ObjectOutputStream(Client.getSocket().getOutputStream());
                Object [] objet = new Object[2];
                objet[0] = "creer-match";
                objet[1] = null;
                oos.writeObject(objet);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Le fichier de sauvegarde n'est pas vide
        else {
            if(Client.getJeu().getPlateauJeu().checkDebutSauvegarde()) {
                try {
                    oos = new ObjectOutputStream(Client.getSocket().getOutputStream());
                    Object [] objet = new Object[2];
                    objet[0] = "charger-partie";
                    objet[1] = null;
                    oos.writeObject(objet);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    oos = new ObjectOutputStream(Client.getSocket().getOutputStream());
                    Object [] objet = new Object[2];
                    objet[0] = "creer-partie";
                    objet[1] = null;
                    oos.writeObject(objet);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        InputStream is;
        Jeu jeu = new Jeu();
        try {
            is = Client.getSocket().getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            jeu = (Jeu) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Client.setJeu(jeu);
        Client.initializeAdverse();

        String langue = Client.getLangue() + "_" + Client.getLangue().toUpperCase();
        Locale locale = new Locale(langue);
        ResourceBundle bundle = ResourceBundle.getBundle("ensi.client.ressources.language", locale);
        Stage stage = (Stage) retourBtn.getScene().getWindow();
        //Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/PlateauView.fxml"), bundle);
        try {
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Permet de charger une partie
     */
    @FXML
    void loadPartie() {
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(Client.getSocket().getOutputStream());
            Object [] objet = new Object[2];
            objet[0] = "charger-partie";
            objet[1] = null;
            oos.writeObject(objet);

        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream is;
        Jeu jeu = new Jeu();
        try {
            is = Client.getSocket().getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            jeu = (Jeu) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Client.setJeu(jeu);
        Client.initializeAdverse();

        String langue = Client.getLangue() + "_" + Client.getLangue().toUpperCase();
        Locale locale = new Locale(langue);
        ResourceBundle bundle = ResourceBundle.getBundle("ensi.client.ressources.language", locale);
        Stage stage = (Stage) retourBtn.getScene().getWindow();
        //Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/PlateauView.fxml"), bundle);
        try {
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Permet de retourner à notre vue précédente
     */
    @FXML
    void retour() {
        String langue = Client.getLangue() + "_" + Client.getLangue().toUpperCase();
        Locale locale = new Locale(langue);
        ResourceBundle bundle = ResourceBundle.getBundle("ensi.client.ressources.language", locale);
        Stage stage = (Stage) retourBtn.getScene().getWindow();

        try {
            FXMLLoader loader;
            if(Client.getJeu().isEnCours()) {
                loader = new FXMLLoader(getClass().getResource("../views/PlateauView.fxml"), bundle);
            } else {
                loader = new FXMLLoader(getClass().getResource("../views/MenuPrincipalView.fxml"), bundle);
            }
            stage.setScene(new Scene(loader.load()));
            //setMenu(loader.getController());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fait appel à la sauvegarde du jeu
     */
    @FXML
    void sauvegarderPartie() {
        try {
            Client.getJeu().getPlateauJeu().sauvegarde();
        } catch (IOException e) {
            e.printStackTrace();
        }

        retour();
    }

    /**
     * Elle permet d'arrêter le match
     */
    @FXML
    void stopMatch() {
        Client.getJeu().setEnCours(false);
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(Client.getSocket().getOutputStream());
            Object [] objet = new Object[2];
            objet[0] = "stop-match";
            objet[1] = Client.getJeu();
            oos.writeObject(objet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Client.recupererJeu();
        retour();
    }

    /**
     * Cette méthode est appelée lors de l'initialisation de la fenêtre
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String langue = Client.getLangue() + "_" + Client.getLangue().toUpperCase();
        Locale locale = new Locale(langue);
        ResourceBundle bundle = ResourceBundle.getBundle("ensi.client.ressources.language", locale);
        retourBtn.setText(bundle.getString("retour"));

        // Le jeu est en cours
        if (Client.getJeu().isEnCours()) {
            notInGamePane.setOpacity(0);
            savePartieBtn.setText(bundle.getString("savePartie"));
            stopMatchBtn.setText(bundle.getString("stopMatch"));
            abandonnerPartieBtn.setText(bundle.getString("abandonPartie"));
        }
        // Le jeu n'est pas en cours
        else {
            inGamePane.setOpacity(0);
            // Le fichier de sauvegarde est vide
            if(Client.getJeu().getPlateauJeu().checkEmptySauvegarde()) {
                nouveauMatchBtn.setText(bundle.getString("newMatch"));
                loadPartie.setOpacity(0);
            }
            // Le fichier de sauvegarde n'est pas vide
            else {
                nouveauMatchBtn.setText(bundle.getString("newPartie"));
                if(Client.getJeu().getPlateauJeu().checkDebutSauvegarde()) {
                    loadPartie.setOpacity(0);
                } else {
                    loadPartie.setText(bundle.getString("playPartie"));
                }
            }
        }
    }
}
