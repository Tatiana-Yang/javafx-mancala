package ensi.model;

import java.io.Serializable;
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

public class Jeu implements Serializable {

    private String version;
    private PlateauJeu plateauJeu;
    private boolean enCours;

    public Jeu() {
        this.version = "1.0";
        this.plateauJeu = null;
        this.enCours = false;
    }

    public Jeu(PlateauJeu plateauJeu) {
        this();
        this.plateauJeu = plateauJeu;
    }

    public String getVersion() {
        return this.version;
    }


    public PlateauJeu getPlateauJeu() {
        return plateauJeu;
    }

    public void setPlateauJeu(PlateauJeu plateauJeu) {
        this.plateauJeu = plateauJeu;
    }

    public boolean isEnCours() {
        return enCours;
    }

    public void setEnCours(boolean enCours) {
        this.enCours = enCours;
    }
}
