package ai.detector;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.*;
import java.nio.file.*;
import java.util.*;


public class DevoreurIO {

    /**
     * Sauvegarde les projets, les noms des fichiers et les informations nécessaires dans un fichier .CSV
     * @param projets
     * @param chemin
     * @throws IOException
     */
    public static void sauvegarderProjetsEnCSV(List<Projet> projets, String chemin) throws IOException {

        //try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(chemin), StandardCharsets.UTF_8)) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(chemin))) {
            // Écrire l'en-tête
            writer.write("Projet, NomFichier, Chemin, Texte\n");

            for (Projet projet : projets) {
                // Ecrit les informations des fichiers pour chaque projet
                for (FichierProjet fichier : projet.getFichiers()) {
                    String projetNom = projet.getNom();
                    String fichierNom = fichier.getNom();
                    String fichierChemin = fichier.getChemin();
                    String fichierTexte = escapeCSV(fichier.getTexte());

                    // Ajoute une ligne au CSV
                    writer.write(projetNom + ", " + fichierNom + ", " + fichierChemin + ", " + fichierTexte + "\n");
                }
            }
        }
    }

    /**
     * Charge les projets depuis un fichier .CSV
     * @param chemin : Le chemin du fichier .CSV
     * @return : Retourne la liste des projets
     * @throws IOException
     */
    public static List<Projet> chargerProjetsDepuisCSV(String chemin) {
        List<Projet> projets = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(chemin))) {
            // Lit la première ligne (header) et n'en fait rien
            String[] read = reader.readNext();

            // Tant qu'il y a des lignes à lire..
            while ((read = reader.readNext()) != null) {
                // Lit l'information et la stocke dans des variables
                String projetNom = read[0];
                String fichierNom = read[1];
                String fichierChemin = read[2];
                String fichierTexte = read[3];

                // Chercher si le projet existe déjà
                Projet projet = trouverProjetParNom(projets, projetNom);
                if (projet == null) {
                    // Crée un nouveau projet s'il n'existe pas
                    projet = new Projet(projetNom);
                    projets.add(projet);
                }

                // Crée un nouveau fichier avec les variables définies et l'ajoute au projet
                FichierProjet fichierProjet = new FichierProjet(fichierNom, fichierChemin, fichierTexte);
                projet.addFichier(fichierProjet);
            }
        } catch (CsvValidationException e) {
            System.err.println("Erreur de validation du CSV: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erreur d'entrée/sortie: " + e.getMessage());
        }

        return projets;
    }


    /**
     * Neutralise les " , \n pour encapsuler dans un CSV
     * @param value
     * @return
     */
    public static String escapeCSV(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }

    /**
     * Cherche un projet par son nom dans la liste des projets
     * @param projets : La liste des projets
     * @param nom : Le nom du projet à identifier
     * @return Retourne null si le projet n'a pas été trouvé
     */
    private static Projet trouverProjetParNom(List<Projet> projets, String nom) {
        for (Projet projet : projets) {
            if (projet.getNom().equals(nom)) {
                return projet;
            }
        }
        return null;
    }

    /**
     *  Charge l'ensemble des projets contenus dans un dossier
     * @param dossierRacine : Le dossier dans lequel les projets seront cherchés
     * @return : Retourne la liste des projets identifiés
     */
    public static List<Projet> chargerProjetsDepuisDossier(String dossierRacine) throws IOException {
        List<Projet> projets = new ArrayList<>();
        Files.list(Paths.get(dossierRacine)).filter(Files::isDirectory).forEach(dir -> {

            // Charge le projet à partir de son chemin
            Projet projet = chargerProjet(dir);

            // Ajoute le projet à la liste des projets
            projets.add(projet);
        });
        return projets;
    }

    /**
     * Charge un projet à partir de son chemin
     * @param dir : le chemin du projet
     * @return le projet
     */
    public static Projet chargerProjet(Path dir) {

        Projet projet = new Projet(dir.getFileName().toString());
        try {
            Files.walk(dir).filter(Files::isRegularFile).forEach(file -> {
                try {
                    String content = new String(Files.readAllBytes(file));
                    FichierProjet fichierProjet = new FichierProjet(file.getFileName().toString(), file.toString(), content);
                    projet.addFichier(fichierProjet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return projet;
    }

}
