package com.example.projetandroid;

public class Offre {
    private int id;
    private String entreprise;
    private String typeMetiers;
    private String formationDemande;
    private int offreSalaire;
    private String competences;
    private String description;

    public Offre(int id, String entreprise, String typeMetiers, String formationDemande, int offreSalaire, String competences, String description) {
        this.id = id;
        this.entreprise = entreprise;
        this.typeMetiers = typeMetiers;
        this.formationDemande = formationDemande;
        this.offreSalaire = offreSalaire;
        this.competences = competences;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getEntreprise() {
        return entreprise;
    }

    public String getPoste() { // This should be getTypeMetiers, but we keep it for the adapter
        return typeMetiers;
    }

    public int getVue() { // Placeholder
        return 0;
    }

    public int getPrix() { // This should be getOffreSalaire, but we keep it for the adapter
        return offreSalaire;
    }

    // Correct getters
    public String getTypeMetiers() {
        return typeMetiers;
    }

    public String getFormationDemande() {
        return formationDemande;
    }

    public int getOffreSalaire() {
        return offreSalaire;
    }

    public String getCompetences() {
        return competences;
    }

    public String getDescription() {
        return description;
    }
}
