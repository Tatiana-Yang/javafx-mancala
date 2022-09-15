package ensi.client.controllers;

import ensi.client.Client;
import ensi.model.Jeu;
import ensi.model.Personne;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
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
public class Plateau implements Initializable {

    @FXML
    private GridPane grilleTrous;

    @FXML
    private MenuBar menuBtn;

    @FXML
    private MenuItem menuRegles;

    @FXML
    private MenuItem menuSysteme;

    @FXML
    public Text titre;

    @FXML
    public Text tourText;

    @FXML
    public Text scoreAdverse;

    @FXML
    public Text joueur;

    @FXML
    public Text adversaire;

    @FXML
    private Text adversaireNom;

    @FXML
    public Text scoreJoueur;

    @FXML
    private Text totalParties;

    @FXML
    private Text partiesJoueur1;

    @FXML
    private Text partiesJoueur2;

    @FXML
    private Text partiesJouees;

    @FXML
    private Text partiesGagneesJoueur;

    @FXML
    private Text partiesGagneesAdverse;

    @FXML
    public Text trouJoueur1;

    @FXML
    public Text trouJoueur2;

    @FXML
    public Text trouJoueur3;

    @FXML
    public Text trouJoueur4;

    @FXML
    public Text trouJoueur5;

    @FXML
    public Text trouJoueur6;

    @FXML
    public Text trouAdversaire1;

    @FXML
    public Text trouAdversaire2;

    @FXML
    public Text trouAdversaire3;

    @FXML
    public Text trouAdversaire4;

    @FXML
    public Text trouAdversaire5;

    @FXML
    public Text trouAdversaire6;

    @FXML
    public Pane paneJoueur1;

    @FXML
    public Pane paneJoueur2;

    @FXML
    public Pane paneJoueur3;

    @FXML
    public Pane paneJoueur4;

    @FXML
    public Pane paneJoueur5;

    @FXML
    public Pane paneJoueur6;


    @FXML
    private Button abandonButton;



    private Text[] listeTrouJoueur;
    private Text[] listeTrouAdversaire;

    public Plateau(){
        this.listeTrouJoueur = new Text[6];
        this.listeTrouAdversaire = new Text[6];
    }

    //METHODE

    @FXML
    public void setScoreJoueur(int score) {
        scoreJoueur.setText(String.valueOf(score));
    }

    @FXML
    public void setScoreAdverse(int score) {
        scoreAdverse.setText(String.valueOf(score));
    }

    /**
     * Cette fonction appelle la méthode ouvrirMenu afin d'ouvrir la vue MenuMancala
     */
    @FXML
    void ouvreMancalaMenu() {
        this.ouvrirMenu("MenuMancala");
    }

    /**
     * Cette fonction appelle la méthode ouvrirMenu afin d'ouvrir la vue MenuOptions
     */
    @FXML
    void ouvreOptionsMenu() {
        this.ouvrirMenu("MenuOptions");
    }

    /**
     * Cette fonction appelle la méthode ouvrirMenu afin d'ouvrir la vue MenuRegles
     */
    @FXML
    void ouvreReglesMenu() {
        this.ouvrirMenu("MenuRegles");
    }

    /**
     * Cette fonction appelle la méthode ouvrirMenu afin d'ouvrir la vue MenuSysteme
     */
    @FXML
    void ouvreSystemeMenu() {
        this.ouvrirMenu("MenuSysteme");
    }

    @FXML
    void handleAnbandonButton(){
        Client.getPers().setAbandon(true);

    }

    /**
     * Cette méthode permet l'ouverture de vue
     * @param nom de la vue qui souhaite être ouvert
     */
    private void ouvrirMenu(String nom) {
        if(Client.getJeu().getPlateauJeu().getCourant().getPseudo().equals(Client.getPers().getPseudo())) {
            String langue = Client.getLangue() + "_" + Client.getLangue().toUpperCase();
            Locale locale = new Locale(langue);
            ResourceBundle bundle = ResourceBundle.getBundle("ensi.client.ressources.language", locale);

            Stage stage = (Stage) titre.getScene().getWindow();

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
     * Cette fonction permet d'initialiser les listes et donc de rajouter tous les trous dans la liste qui lui est attribué
     */
    public void initialiseListes(){
        this.listeTrouJoueur[0] = trouJoueur1;
        this.listeTrouJoueur[1] = trouJoueur2;
        this.listeTrouJoueur[2] = trouJoueur3;
        this.listeTrouJoueur[3] = trouJoueur4;
        this.listeTrouJoueur[4] = trouJoueur5;
        this.listeTrouJoueur[5] = trouJoueur6;

        this.listeTrouAdversaire[0] = trouAdversaire6;
        this.listeTrouAdversaire[1] = trouAdversaire5;
        this.listeTrouAdversaire[2] = trouAdversaire4;
        this.listeTrouAdversaire[3] = trouAdversaire3;
        this.listeTrouAdversaire[4] = trouAdversaire2;
        this.listeTrouAdversaire[5] = trouAdversaire1;
    }

    /**
     * Cette methode met à jour l'affichage du nombre de graines présent dans les trous ainsi que le nombre de graine récupéré
     */
    public void miseAjourTrou(){
        if(estJoueur1()){
            for(int i = 0; i < listeTrouJoueur.length; i++){
                this.listeTrouJoueur[i].setText(String.valueOf(Client.getJeu().getPlateauJeu().getJoueur1().getTrou(i).getNbGraines()));
                this.listeTrouAdversaire[i].setText(String.valueOf(Client.getJeu().getPlateauJeu().getJoueur2().getTrou(i).getNbGraines()));
                Client.getPers().setNbGrainesCapturees(Client.getJeu().getPlateauJeu().getJoueur1().getNbGrainesCapturees());
                Client.getAdversaire().setNbGrainesCapturees(Client.getJeu().getPlateauJeu().getJoueur2().getNbGrainesCapturees());
            }
        }
        else{
            for(int i = 0; i < listeTrouJoueur.length; i++){
                this.listeTrouJoueur[i].setText(String.valueOf(Client.getJeu().getPlateauJeu().getJoueur2().getTrou(i).getNbGraines()));
                this.listeTrouAdversaire[i].setText(String.valueOf(Client.getJeu().getPlateauJeu().getJoueur1().getTrou(i).getNbGraines()));
                Client.getPers().setNbGrainesCapturees(Client.getJeu().getPlateauJeu().getJoueur2().getNbGrainesCapturees());
                Client.getAdversaire().setNbGrainesCapturees(Client.getJeu().getPlateauJeu().getJoueur1().getNbGrainesCapturees());
            }
        }
        setScoreJoueur(Client.getPers().getNbGrainesCapturees());
        setScoreAdverse(Client.getAdversaire().getNbGrainesCapturees());
        if(Client.getJeu().getPlateauJeu().getNbGrainePlateau() <= 10){
            if(Client.getPers() == Client.getJeu().getPlateauJeu().getCourant()){
                abandonButton.setVisible(true);
            }
            if(Client.getJeu().getPlateauJeu().getCourant().getAbandon() && Client.getJeu().getPlateauJeu().getAdverse().getPseudo().equals(Client.getPers().getPseudo())){
                fenetreAbandon();
            }
        }
        finPartie();
    }

    /**
     * Cette méthode permet de vérifier si une partie est fini et affiche la fenêtre de victoire, de défaite ou nulle au Client
     */
    public void finPartie(){
        if(Client.getJeu().getPlateauJeu().arret()){
            Client.getJeu().getPlateauJeu().misAJourPoint();
            Personne gagnant = Client.getJeu().getPlateauJeu().gagnant();
            if(gagnant == null){
                fenetreNulle();
            }
            else if (gagnant.getPseudo().equals(Client.getPers().getPseudo())){
                fenetreGagnant();
            }
            else if (!gagnant.getPseudo().equals(Client.getPers().getPseudo())){
                fenetrePerdant();
            }
        }
    }

    /**
     * Cette méthode ouvre une seconde fenêtre pour informer le joueur que son adversaire souhaite abandonner la partie
     */
    public void fenetreAbandon(){
        Label affichageInfo = new Label("Votre adversaire souhaite abandonner la partie ! \n voulez-vous abandonner ?\n");
        Button boutonOui = new Button("Oui");
        Button boutonNon = new Button("Non");

        Pane secondaryLayout = new Pane();

        affichageInfo.setLayoutX(100);
        affichageInfo.setLayoutY(77);
        boutonOui.setLayoutX(100);
        boutonOui.setLayoutY(192);
        boutonNon.setLayoutX(346);
        boutonNon.setLayoutY(192);

        secondaryLayout.getChildren().add(affichageInfo);
        secondaryLayout.getChildren().add(boutonOui);
        secondaryLayout.getChildren().add(boutonNon);
        Scene secondScene = new Scene(secondaryLayout, 500,300);

        Stage newWindow = new Stage();
        newWindow.setTitle("Souhaitez-vous abandonner ?");
        newWindow.setScene(secondScene);


        boutonOui.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Client.getPers().setAbandon(true);
                newWindow.close();
            }
        });
        boutonNon.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                newWindow.close();
            }
        });

        newWindow.show();
    }

    /**
     * Cette fenêtre ouvre la fenêtre qui informe le joueur de sa victoire
     */
    public void fenetreGagnant(){
        Stage newWindow = new Stage();
        Label affichageGagnant= new Label("BRAVO, " + Client.getJeu().getPlateauJeu().gagnant().getPseudo() + " a gagné !\n");
        newWindow.setTitle("Gagnant de la partie !");
        Label menu = new Label("Retour au menu");
        Button boutonMenu = new Button("Menu");
        Button boutonQuitter = new Button("Quitter");
        boutonMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ouvreMancalaMenu();
                newWindow.close();
            }
        });
        boutonQuitter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Client.getJeu().getPlateauJeu().sauvegarde();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.exit(1);
            }
        });

        affichageGagnant.setLayoutX(100);
        affichageGagnant.setLayoutY(77);
        menu.setLayoutX(100);
        menu.setLayoutY(124);
        boutonMenu.setLayoutX(100);
        boutonMenu.setLayoutY(192);
        boutonQuitter.setLayoutX(346);
        boutonQuitter.setLayoutY(192);

        Pane secondaryLayout = new Pane();
        secondaryLayout.getChildren().add(affichageGagnant);
        secondaryLayout.getChildren().add(menu);
        secondaryLayout.getChildren().add(boutonMenu);
        secondaryLayout.getChildren().add(boutonQuitter);
        Scene secondScene = new Scene(secondaryLayout, 500,300);

        newWindow.setScene(secondScene);

        newWindow.show();
    }

    /**
     * Cette fenêtre informe le joueur de sa défaite
     */
    public void fenetrePerdant(){
        Stage newWindow = new Stage();
        Label affichageGagnant= new Label("Dommage votre adversaire a gagné !\n");
        newWindow.setTitle("Perdant de la partie !");
        Label menu = new Label("Retour au menu");
        Button boutonMenu = new Button("Menu");
        Button boutonQuitter = new Button("Quitter");
        boutonMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ouvreMancalaMenu();
                newWindow.close();
            }
        });
        boutonQuitter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Client.getJeu().getPlateauJeu().sauvegarde();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.exit(1);
            }
        });

        affichageGagnant.setLayoutX(100);
        affichageGagnant.setLayoutY(77);
        menu.setLayoutX(100);
        menu.setLayoutY(124);
        boutonMenu.setLayoutX(100);
        boutonMenu.setLayoutY(192);
        boutonQuitter.setLayoutX(346);
        boutonQuitter.setLayoutY(192);

        Pane secondaryLayout = new Pane();
        secondaryLayout.getChildren().add(affichageGagnant);
        secondaryLayout.getChildren().add(menu);
        secondaryLayout.getChildren().add(boutonMenu);
        secondaryLayout.getChildren().add(boutonQuitter);
        Scene secondScene = new Scene(secondaryLayout, 500,300);

        newWindow.setScene(secondScene);

        newWindow.show();
    }

    /**
     * Cette fenêtre informe le joueur qu'il s'agit d'une partie nulle
     */
    public void fenetreNulle(){
        Stage newWindow = new Stage();
        Label affichageGagnant = new Label("Partie nulle !");
        newWindow.setTitle("Partie nulle !");

        Label menu = new Label("Retour au menu");
        Button boutonMenu = new Button("Menu");
        Button boutonQuitter = new Button("Quitter");
        boutonMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ouvreMancalaMenu();
                newWindow.close();
            }
        });
        boutonQuitter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Client.getJeu().getPlateauJeu().sauvegarde();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.exit(1);
            }
        });

        affichageGagnant.setLayoutX(100);
        affichageGagnant.setLayoutY(77);
        menu.setLayoutX(100);
        menu.setLayoutY(124);
        boutonMenu.setLayoutX(100);
        boutonMenu.setLayoutY(192);
        boutonQuitter.setLayoutX(346);
        boutonQuitter.setLayoutY(192);

        Pane secondaryLayout = new Pane();
        secondaryLayout.getChildren().add(affichageGagnant);
        secondaryLayout.getChildren().add(menu);
        secondaryLayout.getChildren().add(boutonMenu);
        secondaryLayout.getChildren().add(boutonQuitter);

        Scene secondScene = new Scene(secondaryLayout, 500,300);

        newWindow.setScene(secondScene);

        newWindow.show();
    }

    public boolean estJoueur1(){
        return Client.getPers().getPseudo().equals(Client.getJeu().getPlateauJeu().getJoueur1().getPseudo());
    }

    /**
     * Cette methode reçoit le plateau de jeu qui est envoyé par le serveur
     */
    public void receptionPlateau(){
        InputStream is;
        Jeu jeu;
        try {
            is = Client.getSocket().getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            jeu = (Jeu) ois.readObject();
            Client.setJeu(jeu);

            miseAjourTrou();
            Client.getJeu().getPlateauJeu().nourrir();
            joueur.setText(Client.getPers().getPseudo());
            adversaire.setText(Client.getAdversaire().getPseudo());
            scoreJoueur.setText(String.valueOf(Client.getPers().getNbGrainesCapturees()));
            scoreAdverse.setText(String.valueOf(Client.getAdversaire().getNbGrainesCapturees()));
            menuBtn.setOpacity(0);

            String langue = Client.getLangue() + "_" + Client.getLangue().toUpperCase();
            Locale locale = new Locale(langue);
            ResourceBundle bundle = ResourceBundle.getBundle("ensi.client.ressources.language", locale);
            if(Client.getJeu().getPlateauJeu().getCourant().getPseudo().equals(Client.getPers().getPseudo())) {
                tourText.setText(bundle.getString("tourTextMe"));
            } else {
                tourText.setText(bundle.getString("tourTextEnnemie"));
            }
        } catch (IOException | ClassNotFoundException exc) {
            exc.printStackTrace();
        }
    }

    /**
     * Ces méthodes appellent la méthode choixTrou et mettent l'index du trou en paramêtre
     */
    public void choixTrou1() {
        choixTrou(0);
    }

    public void choixTrou2() {
        choixTrou(1);
    }

    public void choixTrou3() {
        choixTrou(2);
    }

    public void choixTrou4() {
        choixTrou(3);
    }

    public void choixTrou5() {
        choixTrou(4);
    }

    public void choixTrou6() {
        choixTrou(5);
    }

    /**
     * Si le joueur est bien le joueur courant alors lors du choix de son trou il informe le serveur
     * @param numero correspond à l'index du trou choisi
     */
    public void choixTrou(int numero) {
        if(Client.getPers().getPseudo().equals(Client.getJeu().getPlateauJeu().getCourant().getPseudo())
                && Client.getJeu().getPlateauJeu().getCourant().getTrou(numero).getAutorisation()) {
            Client.bruitageAigu();
            ObjectOutputStream oos;
            try {
                oos = new ObjectOutputStream(Client.getSocket().getOutputStream());
                Object[] objet = new Object[3];
                objet[0] = "jeu-choix";
                objet[1] = Client.getPers().getPseudo();
                objet[2] = String.valueOf(numero);
                oos.writeObject(objet);
                receptionPlateau();
                wait4update();

            } catch (IOException exc) {
                exc.printStackTrace();
            }
        }
        else{
            Client.bruitageGrave();
        }
    }

    /**
     * Met à jour la vue du plateau de chaque client
     */
    public void wait4update() {
        new Thread(() -> {
            boolean stop = Client.wait4update();
                /*Platform.runLater(() -> {
                    String langue = Client.getLangue() + "_" + Client.getLangue().toUpperCase();
                    Locale locale = new Locale(langue);
                    ResourceBundle bundle = ResourceBundle.getBundle("ensi.client.ressources.language", locale);
                    Stage stage = (Stage) joueur.getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/PlateauView.fxml"), bundle);
                    try {
                        stage.setScene(new Scene(loader.load()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });*/
            if(stop) {
                Client.recupererJeu();
                Platform.runLater(() -> {
                    String langue = Client.getLangue() + "_" + Client.getLangue().toUpperCase();
                    Locale locale = new Locale(langue);
                    ResourceBundle bundle = ResourceBundle.getBundle("ensi.client.ressources.language", locale);
                    Stage stage = (Stage) joueur.getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/MenuPrincipalView.fxml"), bundle);
                    try {
                        stage.setScene(new Scene(loader.load()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            miseAjourTrou();
            Client.getJeu().getPlateauJeu().nourrir();
            joueur.setText(Client.getPers().getPseudo());
            adversaire.setText(Client.getAdversaire().getPseudo());
            scoreJoueur.setText(String.valueOf(Client.getPers().getNbGrainesCapturees()));
            scoreAdverse.setText(String.valueOf(Client.getAdversaire().getNbGrainesCapturees()));
            menuBtn.setOpacity(1);
            finPartie();

            String langue = Client.getLangue() + "_" + Client.getLangue().toUpperCase();
            Locale locale = new Locale(langue);
            ResourceBundle bundle = ResourceBundle.getBundle("ensi.client.ressources.language", locale);
            if(Client.getJeu().getPlateauJeu().getCourant().getPseudo().equals(Client.getPers().getPseudo())) {
                tourText.setText(bundle.getString("tourTextMe"));
            } else {
                tourText.setText(bundle.getString("tourTextEnnemie"));
            }
        }).start();
    }

    /**
     * Cette méthode est appelé à chaque ouverture d'une vue du Plateau
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String langue = Client.getLangue() + "_" + Client.getLangue().toUpperCase();
        Locale locale = new Locale(langue);
        ResourceBundle bundle = ResourceBundle.getBundle("ensi.client.ressources.language", locale);

        menuRegles.setText(bundle.getString("regles"));
        menuSysteme.setText(bundle.getString("systeme"));
        partiesGagneesAdverse.setText(bundle.getString("partiesGagnees"));
        partiesGagneesJoueur.setText(bundle.getString("partiesGagnees"));
        partiesJouees.setText(bundle.getString("partiesJouees"));
        adversaireNom.setText(bundle.getString("monAdverse"));

        abandonButton.setVisible(false);

        if(Client.getAdversaire().getPseudo() == null) {
            //Client.setAdversaire(Client.getJeu().getPlateauJeu().getCourant());
            // OR
            if(Client.getJeu().getPlateauJeu().getJoueur1().getPseudo().equals(Client.getPers().getPseudo())) {
                Client.setAdversaire(Client.getJeu().getPlateauJeu().getJoueur2());
            } else {
                Client.setAdversaire(Client.getJeu().getPlateauJeu().getJoueur1());
            }
        }

        joueur.setText(Client.getPers().getPseudo());
        adversaire.setText(Client.getAdversaire().getPseudo());
        scoreJoueur.setText(String.valueOf(Client.getPers().getNbGrainesCapturees()));
        scoreAdverse.setText(String.valueOf(Client.getAdversaire().getNbGrainesCapturees()));
        if(Client.getJeu().getPlateauJeu().getCourant().getPseudo().equals(Client.getPers().getPseudo())) {
            tourText.setText(bundle.getString("tourTextMe"));
        } else {
            tourText.setText(bundle.getString("tourTextEnnemie"));
        }
        initialiseListes();
        miseAjourTrou();
        Client.getJeu().getPlateauJeu().nourrir();

        partiesJoueur1.setText(String.valueOf(Client.getPers().getPartieGagnee()));
        partiesJoueur2.setText(String.valueOf(Client.getAdversaire().getPartieGagnee()));
        totalParties.setText(String.valueOf(Client.getJeu().getPlateauJeu().getNbPartieJouee()));


        if(Client.getJeu().getPlateauJeu().getCourant().getPseudo().equals(Client.getAdversaire().getPseudo())) {
            menuBtn.setOpacity(0);
            wait4update();
            miseAjourTrou();
            finPartie();
        }
    }
}