package ensi.model;

import java.io.Serializable;
import java.util.ArrayList;

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

public class Personne implements Serializable {
    public String pseudo;
    public String ip;
    public String port;
    public ArrayList<String> action;
    private Trou[] tabTrou;
    private int nbGrainesCapturees;
    private boolean musique;
    private boolean bruitage;
    private int partieGagnee;
    private boolean abandon;
    private String langue;


    public Personne() {
        this.tabTrou = new Trou[6];

        this.tabTrou[0]=new Trou();
        this.tabTrou[1]=new Trou();
        this.tabTrou[2]=new Trou();
        this.tabTrou[3]=new Trou();
        this.tabTrou[4]=new Trou();
        this.tabTrou[5]=new Trou();

        this.nbGrainesCapturees = 0;
        this.partieGagnee = 0;

        this.musique = false;
        this.bruitage = true;
        this.abandon = false;
        this.langue = "fr";

        initialiserTrous();
    }

    @Override
    public String toString() {
        String message = "";
        for(Trou trou : this.tabTrou) {
            message += trou.getNbGraines() + " ";
        }
        return message;
    }

    // MUTATEUR ET ACCESSEUR DES ATTRIBUTS
    public void setPseudo(String p)
    {
        this.pseudo = p;
    }
    public void setIp(String ip)
    {
        this.ip = ip;
    }
    public void setPort(String port)
    {
        this.port = port;
    }
    public String getPseudo() {
        return pseudo;
    }
    public int getNbGrainesCapturees() {
        return this.nbGrainesCapturees;
    }
    public void setNbGrainesCapturees(int nbGrainesCapturees) {
        this.nbGrainesCapturees = nbGrainesCapturees;
    }
    public Trou[] getTabTrou() {
        return this.tabTrou;
    }
    public Trou getTrou(int index) {
        return this.tabTrou[index];
    }
    public void setTrou(int index, int nbGraines) {
        this.tabTrou[index].setNbGraines(nbGraines);
    }
    public Boolean getMusique() {
        return this.musique;
    }
    public void setMusique(Boolean musique) {
        this.musique = musique;
    }
    public Boolean getBruitage() {
        return this.bruitage;
    }
    public void setBruitage(Boolean bruitage) {
        this.bruitage = bruitage;
    }
    public String getLangue() {
        return langue;
    }
    public void setLangue(String langue) {
        this.langue = langue;
    }

    public int getNbGrainePlateau(){
        int nbGraine=0;
        for(Trou trou : this.tabTrou){
            nbGraine += trou.getNbGraines();
        }
        return nbGraine;
    }

    public boolean getAbandon(){
        return this.abandon;
    }

    public void setAbandon(boolean abandon){
        this.abandon = abandon;
    }

    public int getPartieGagnee(){
        return this.partieGagnee;
    }

    public void setPartieGagnee(int i){
        this.partieGagnee += i;
    }

    public void initialiserTrous(){
        int i;
        for(i = 0; i < 6; i++) {
            this.tabTrou[i] = new Trou();
        }
    }

    /**
     * Cette fonction permet d'ajouter directement des graines dans notre attribut nbGrainesCapturees
     * @param nbGraines le nombre de graine à ajouter dans notre attribut
     */
    public void ajoutGraineCapturees(int nbGraines){
        this.nbGrainesCapturees += nbGraines;
    }


}