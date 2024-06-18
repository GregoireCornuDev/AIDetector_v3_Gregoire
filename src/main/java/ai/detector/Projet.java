package ai.detector;

import java.util.ArrayList;
import java.util.List;

public class Projet {
    private String nom;
    //private String auteur = "Inconnu"; Cette variable de classe n'est plus utilisée
    private List<FichierProjet> fichiers = new ArrayList<>();

    public Projet(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    //public String getAuteur() { return auteur; }

    //public void setAuteur(String auteur) { this.auteur = auteur; }

    public List<FichierProjet> getFichiers() {
        return fichiers;
    }

    public void addFichier(FichierProjet fichier) {
        fichiers.add(fichier);
    }

    public String toString() {
        return nom;
    }

}

