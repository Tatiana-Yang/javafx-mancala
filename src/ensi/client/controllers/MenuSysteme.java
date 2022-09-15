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
public class MenuSysteme implements Initializable {

    @FXML
    private Text title;

    @FXML
    private Button retourBtn;

    @FXML
    private Text version;

    @FXML
    private Text versionJeu;

    @FXML
    private Text realisateurs;

    /**
     * est appelé pour retourner à la page précédente
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
     * est appelé lors de l'affichage d'une vue Systeme
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String langue = Client.getLangue() + "_" + Client.getLangue().toUpperCase();
        Locale locale = new Locale(langue);
        ResourceBundle bundle = ResourceBundle.getBundle("ensi.client.ressources.language", locale);

        retourBtn.setText(bundle.getString("retour"));
        title.setText(bundle.getString("titreSysteme"));
        version.setText(bundle.getString("version"));
        realisateurs.setText(bundle.getString("realisateurs"));

        versionJeu.setText(Client.getJeu().getVersion());
    }
}
