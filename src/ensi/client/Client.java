package ensi.client;

import ensi.model.Jeu;
import ensi.model.Personne;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Paths;
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
public class Client extends Application {

    private String adresse = null;
    private int port = -1;
    private static Socket socket;
    private static Personne pers = new Personne();
    private static Personne adversaire = new Personne();
    private static Jeu jeu = null;
    private static String langue = "fr";

    private static MediaPlayer musicPlayer;
    private static MediaPlayer soundPlayer;


    // ACCESSEURS et MUTATEURS

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public static Personne getPers() {
        return pers;
    }

    public static void setPers(Personne personne) {
        pers = personne;
    }

    public static Personne getAdversaire() {
        return adversaire;
    }

    public static void setAdversaire(Personne adverse) {
        adversaire = adverse;
    }


    public static Jeu getJeu() {
        return jeu;
    }

    public static void setJeu(Jeu jeu) {
        Client.jeu = jeu;
    }

    public static String getLangue() {
        return langue;
    }

    public static void setLangue(String langue) {
        Client.langue = langue;
    }




    // METHODES

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Locale currentLocale = Locale.getDefault();
        Locale locale = new Locale("fr_FR");
        ResourceBundle bundle = ResourceBundle.getBundle("ensi.client.ressources.language", locale);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/ConnexionClientView.fxml"), bundle);
        primaryStage.setTitle("Mancala");
        //primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.show();
    }

    @Override
    public void stop() {
        if(jeu != null) {
            System.out.println("Fermeture de la session");
            ObjectOutputStream oos;
            try {
                oos = new ObjectOutputStream(getSocket().getOutputStream());
                Object [] objet = new Object[2];
                objet[0] = "deconnexion-joueur";
                objet[1] = getPers().getPseudo();
                oos.writeObject(objet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Cette méthode permet de demander au Serveur de ce connecter et attends sa réponse
     * @param pseudo
     * @throws Exception
     */
    public void runClient(String pseudo) throws Exception {
        socket = new Socket(getAdresse(), getPort());
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            Object [] objet = new Object[3];
            objet[0] = "connexion-joueur";
            objet[1] = pseudo;
            objet[2] = langue;
            oos.writeObject(objet);
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream is = socket.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);

        pers = (Personne) ois.readObject();
    }

    /**
     * Cette méthode récupère le Plateau Jeu envoyé par le Serveur
     */
    public static void recupererJeu() {
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            Object [] objet = new Object[3];
            objet[0] = "recuperer-jeu";
            objet[1] = null;
            oos.writeObject(objet);
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream is;
        try {
            is = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            Jeu newJeu;
            try {
                newJeu = (Jeu) ois.readObject();// envoie de l'objet
                setJeu(newJeu);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cette méthode reçoit le Plateau Jeu et modifie l'attribut du client
     */
    public static void wait4joueurMenu() {
        InputStream is;
        try {
            is = Client.getSocket().getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);

            Jeu newJeu;    // lecture de l'objet
            try {
                newJeu = (Jeu) ois.readObject();
                Client.setJeu(newJeu);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Le Client reçoit le Jeu du Serveur
     * @return un boolean si la partie est en cours il retourne true, false sinon
     */
    public static boolean wait4update() {
        boolean returnValue = false;
        InputStream is;
        try {
            is = Client.getSocket().getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);

            Jeu newJeu;     // lecture de l'objet
            try {
                newJeu = (Jeu) ois.readObject();

                // Le match en cours est stoppé
                if(Client.getJeu().isEnCours() && !newJeu.isEnCours()) {
                    returnValue = true;
                }
                Client.setJeu(newJeu);
                return returnValue;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cette méthode permet d'initialiser le joueur adverse du Client
     */
    public static void initializeAdverse(){
        //On initialise le joueur adverse
        if(getPers().getPseudo().equals(getJeu().getPlateauJeu().getJoueur1().getPseudo())) {
            setAdversaire(getJeu().getPlateauJeu().getJoueur2());
        } else {
            setAdversaire(getJeu().getPlateauJeu().getJoueur1());
        }
    }

    /**
     * musiqueDeFond met en marche le son présent dans les ressources
     */
    public static void musiqueDeFond() {
        if(getPers().getMusique()) {
            String audio = "src/ensi/ressources/Moon-Wisper.mp3";
            Media media = new Media(Paths.get(audio).toUri().toString());
            musicPlayer = new MediaPlayer(media);
            musicPlayer.setOnEndOfMedia(() -> musicPlayer.seek(Duration.ZERO));     // en boucle
            musicPlayer.play();
        } else {
            musicPlayer.stop();
        }
    }

    /**
     * bruitageAigu fait un son aigu lorsque la méthode est appelé
     */
    public static void bruitageAigu() {
        if(getPers().getBruitage()) {
            String audio = "src/ensi/ressources/bip-aigu.wav";
            Media media = new Media(Paths.get(audio).toUri().toString());
            soundPlayer = new MediaPlayer(media);
            soundPlayer.play();
        }
    }

    /**
     * bruitageGrave fait un son grave lorsque la méthode est appelé
     */
    public static void bruitageGrave() {
        if(getPers().getBruitage()) {
            String audio = "src/ensi/ressources/bip-grave.wav";
            Media media = new Media(Paths.get(audio).toUri().toString());
            soundPlayer = new MediaPlayer(media);
            soundPlayer.play();
        }
    }

    public static void main(String[] zero) {
        launch(zero);
    }
}
