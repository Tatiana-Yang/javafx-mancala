package ensi.client.controllers;

import ensi.client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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
public class ConnexionClient extends Client {

    @FXML
    private TextField adresseField;

    @FXML
    private TextField portField;

    @FXML
    private TextField nomField;

    @FXML
    private Text adresseLabel;

    @FXML
    private Text portLabel;

    @FXML
    private Text pseudoLabel;

    @FXML
    private Button connexionBtn;

    @FXML
    private Pane languagePane;

    @FXML
    private Button frBtn;

    @FXML
    private Button enBtn;

    /**
     * Cette fonction permet la modification de la langue utilisé
     * @param event evenement appelé
     */
    @FXML
    void changeLangue(ActionEvent event) {
        String nomBtn = ((Button)event.getSource()).getId();
        String langue = "fr_FR";
        if(nomBtn.startsWith("en")) {
            langue = "en_EN";
        }

        Client.setLangue(langue.split("_")[0]);

        Locale locale = new Locale(langue);
        ResourceBundle bundle = ResourceBundle.getBundle("ensi.client.ressources.language", locale);
        adresseLabel.setText(bundle.getString("adresse"));
        portLabel.setText(bundle.getString("port"));
        pseudoLabel.setText(bundle.getString("pseudo"));
        connexionBtn.setText(bundle.getString("connexion"));
    }

    /**
     * Permet de nous connecter au serveur en récupérant son adresse et son port
     * @param event
     */
    @FXML
    void connecterServeur(ActionEvent event) {
        setAdresse(adresseField.getText());
        setPort(Integer.parseInt(portField.getText()));
        String pseudo = nomField.getText();
        try {
            runClient(pseudo);
            // Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)event.getSource()).getScene().getWindow();
            // OR
            Locale locale = new Locale(Client.getLangue());
            ResourceBundle bundle = ResourceBundle.getBundle("ensi.client.ressources.language", locale);
            Stage stage = (Stage) adresseField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/MenuPrincipalView.fxml"), bundle);
            stage.setScene(new Scene(loader.load()));

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
