package ensi.client.controllers;

import ensi.client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
public class MenuOptions implements Initializable {

    @FXML
    private Button retourBtn;

    @FXML
    private CheckBox bruitages;

    @FXML
    private CheckBox musique;

    /**
     * toggleBruitages met en marche le bruitage lorsqu'il est sélectionné dans la vue
     */
    @FXML
    void toggleBruitages() {
        if(bruitages.isSelected()) {
            Client.getPers().setBruitage(true);
        } else {
            Client.getPers().setBruitage(false);
        }
    }

    /**
     * toggleMusique met en marche la musique lorsqu'il est sélectionné dans la vue
     */
    @FXML
    void toggleMusique() {
        if(musique.isSelected()) {
            Client.getPers().setMusique(true);
        } else {
            Client.getPers().setMusique(false);
        }
        Client.musiqueDeFond();
    }

    /**
     * Cette fonction permet de retourner à la vue précédente
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String langue = Client.getLangue() + "_" + Client.getLangue().toUpperCase();
        Locale locale = new Locale(langue);
        ResourceBundle bundle = ResourceBundle.getBundle("ensi.client.ressources.language", locale);

        retourBtn.setText(bundle.getString("retour"));
        bruitages.setText(bundle.getString("allowBruitages"));
        musique.setText(bundle.getString("allowMusique"));

        musique.setSelected(Client.getPers().getMusique());
        bruitages.setSelected(Client.getPers().getBruitage());
    }
}
