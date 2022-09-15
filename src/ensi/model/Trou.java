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

public class Trou implements Serializable {
    private int nbGraines;
    private boolean autorisation;

    // CONSTRUCTEUR
    public Trou() {
        this.nbGraines = 4;
        this.autorisation = true;
    }

    // ACCESSEURS et MUTATEURS
    public int getNbGraines() {
        return this.nbGraines;
    }

    public void setNbGraines(int nbGraines) {
        this.nbGraines = nbGraines;
    }

    public void incrementeNbGraines() {
        this.nbGraines += 1;
    }

    public boolean getAutorisation(){
        return this.autorisation;
    }

    public void setAutorisation(boolean bool){
        this.autorisation = bool;
    }

}

