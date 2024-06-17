package ai.detector;

public class FichierProjet {
    private String nom;
    private String chemin;
    private String texte;
    private String type;
    private double scoreIA;

    public FichierProjet(String nom, String chemin, String texte) {
        this.nom = nom;
        this.chemin = chemin;
        this.texte = texte;
        this.type = getTypeFromExtension(nom);
        this.scoreIA = Math.random(); // Juste pour la démonstration
    }

    private String getTypeFromExtension(String nom) {
        String extension = "";
        int i = nom.lastIndexOf('.');
        if (i > 0) {
            extension = nom.substring(i + 1);
        }
        return extension;
    }

    public String getNom() {
        return nom;
    }

    public String getChemin() {
        return chemin;
    }

    public String getTexte() {
        return texte;
    }

    public String getType() {
        return type;
    }

    public double getScoreIA() {
        return scoreIA;
    }

    public void setScoreIA(double scoreIA) {
        this.scoreIA = scoreIA;
    }

    public String toString() {
        return nom;
    }
}

