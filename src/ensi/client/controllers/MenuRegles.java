package ensi.client.controllers;

import ensi.client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
public class MenuRegles implements Initializable {

    @FXML
    private Button retourBtn;

    @FXML
    private Text title;

    @FXML
    private Text regles;

    /**
     * Permet de retourner à la page précédente, soit le menu principale
     * @param event
     */
    @FXML
    void retour(ActionEvent event) {

        String langue = Client.getLangue() + "_" + Client.getLangue().toUpperCase();
        Locale locale = new Locale(langue);
        ResourceBundle bundle = ResourceBundle.getBundle("ensi.client.ressources.language", locale);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

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
     * Permet d'initialiser notre vue
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String langue = Client.getLangue() + "_" + Client.getLangue().toUpperCase();
        Locale locale = new Locale(langue);
        ResourceBundle bundle = ResourceBundle.getBundle("ensi.client.ressources.language", locale);

        retourBtn.setText(bundle.getString("retour"));
        title.setText(bundle.getString("titreRegles"));
        regles.setText(bundle.getString("reglesText"));
    }
}
