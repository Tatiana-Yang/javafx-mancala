package ensi.client.controllers;

import ensi.client.Client;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
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
public class MenuPrincipal implements Initializable {

    @FXML
    private Text joueur;

    @FXML
    private Text enAttenteJoueur2;

    @FXML
    private Text enAttenteJoueur1;

    @FXML
    private Button menuMancalaBtn;

    @FXML
    private Pane menuPane;

    @FXML
    private Pane enAttentePane;

    /**
     * Ouverture du Menu Mancala à partir du Menu Principal
     */
    @FXML
    void ouvreMancalaMenu() {
        if(Client.getJeu().getPlateauJeu().getCourant().getPseudo().equals(Client.getPers().getPseudo())) {
            this.ouvrirMenu("MenuMancala");
        }
    }

    /**
     * Ouverture du Menu Options à partir du Menu Principal
     */
    @FXML
    void ouvreOptionsMenu() {
        this.ouvrirMenu("MenuOptions");
    }

    /**
     * Ouverture du Menu Regles à partir du Menu Principal
     */
    @FXML
    void ouvreReglesMenu() {
        this.ouvrirMenu("MenuRegles");
    }

    /**
     * Ouverture du Menu Syemest à partir du Menu Principal
     */
    @FXML
    void ouvreSystemeMenu() {
        this.ouvrirMenu("MenuSysteme");
    }

    /**
     * Cette méthode permet l'ouverture d'une nouvelle vue fxml sur notre fenêtre déjà ouverte
     * @param nom Le nom de notre vue que nous voulons ouvrir
     */
    private void ouvrirMenu(String nom) {
        if(Client.getJeu() == null) {
            System.out.println("Erreur : Impossible d'ouvrir le menu.");
        } else {
            String langue = Client.getLangue() + "_" + Client.getLangue().toUpperCase();
            Locale locale = new Locale(langue);
            ResourceBundle bundle = ResourceBundle.getBundle("ensi.client.ressources.language", locale);
            Stage stage = (Stage) joueur.getScene().getWindow();

            Parent menu;
            try {
                menu = FXMLLoader.load(getClass().getResource("../views/" + nom + ".fxml"), bundle);
                stage.setScene(new Scene(menu));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Permet de changer la fenêtre à partir du paramètre
     * @param fenetre le nom de la fenêtre que nous voulons afficher
     */
    public void changeFenetre(String fenetre) {
        String langue = Client.getLangue() + "_" + Client.getLangue().toUpperCase();
        Locale locale = new Locale(langue);
        ResourceBundle bundle = ResourceBundle.getBundle("ensi.client.ressources.language", locale);
        Stage stage = (Stage) joueur.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/" + fenetre + ".fxml"), bundle);
        try {
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cette méthode initialise notre vue à son ouverture
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String langue = Client.getLangue() + "_" + Client.getLangue().toUpperCase();
        Locale locale = new Locale(langue);
        ResourceBundle bundle = ResourceBundle.getBundle("ensi.client.ressources.language", locale);
        joueur.setText(bundle.getString("bienvenueMessage") + " " + Client.getPers().getPseudo() + " !");
        if(Client.getJeu() == null) {
            menuPane.setOpacity(0);
            enAttenteJoueur2.setText(bundle.getString("enAttenteMenu"));

            new Thread(() -> {
                Client.wait4joueurMenu();
                Platform.runLater(() -> changeFenetre("MenuPrincipalView"));
            }).start();

            /*Thread t1 = new Thread() {
                public void run() {
                    Client.runMenu();
                    Stage stage = (Stage) joueur.getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/MenuPrincipalView.fxml"));
                    try {
                        stage.setScene(new Scene(loader.load()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            t1.start();*/
        } else {
            if(Client.getJeu().getPlateauJeu().getCourant().getPseudo().equals(Client.getPers().getPseudo())) {
                enAttentePane.setOpacity(0);
                enAttenteJoueur1.setText("");
            } else {
                enAttentePane.setOpacity(0);
                menuMancalaBtn.setOpacity(0);
                enAttenteJoueur1.setText(bundle.getString("enAttenteJoueur") + " " + Client.getJeu().getPlateauJeu().getCourant().getPseudo() + ".");

                new Thread(() -> {
                    Client.wait4joueurMenu();
                    Platform.runLater(() -> changeFenetre("PlateauView"));
                }).start();
            }
        }
    }
}
