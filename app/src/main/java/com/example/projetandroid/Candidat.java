package com.example.projetandroid;

public class Candidat {
    private String nom;
    private String telephone;
    private String offreMetier;

    public Candidat(String nom, String telephone, String offreMetier) {
        this.nom = nom;
        this.telephone = telephone;
        this.offreMetier = offreMetier;
    }

    public String getNom() {
        return nom;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getOffreMetier() {
        return offreMetier;
    }
}
