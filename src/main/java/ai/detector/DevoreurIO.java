package ai.detector;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
//mport com.opencsv.exceptions.CsvValidationException;
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
            writer.write("Projet,NomFichier,Chemin,Type,ScoreIA\n");

            for (Projet projet : projets) {
                // Ecrit les informations des fichiers pour chaque projet
                for (FichierProjet fichier : projet.getFichiers()) {
                    String projetNom = projet.getNom();
                    String fichierNom = fichier.getNom();
                    String fichierChemin = fichier.getChemin();
                    String fichierTexte = fichier.getTexte();

                    // Ajoute une ligne au CSV
                    writer.write(projetNom + "," + fichierNom + "," + fichierChemin + "," + fichierTexte + "<fin-texte>");
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
    public static List<Projet> chargerProjetsDepuisCSV(String chemin) throws CsvValidationException, IOException {

        List<Projet> projets = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(chemin))) {
            String[] nextLine;

            // Passe la première ligne (header)
            reader.readLine();

            // Tant qu'il y a des lignes à lire..
            while ((nextLine = reader.readNext()) != null) {
                // Read the information and store them in variables
                String projetNom = nextLine[0];
                String fichierNom = nextLine[1];
                String fichierChemin = nextLine[2];
                String fichierTexte = nextLine[3];

                        // Lire jusqu'à rencontrer <fin-ligne>
                        StringBuilder recordBuilder = new StringBuilder(nextLine);
                        while (!nextLine.endsWith("<fin-ligne>") && (nextLine = reader.readNext()) != null) {
                            recordBuilder.append(nextLine);
                        }
                        String record = recordBuilder.toString().replace("<fin-ligne>", "");
                        // Traitez le record comme nécessaire
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Chercher si le projet existe déjà
                Projet projet = trouverProjetParNom(projets, projetNom);
                if (projet == null) {
                    // Crée un nouveau projet s'il n'existe pas
                    projet = new Projet(projetNom);
                    projets.add(projet);
                }

                // Crée un nouveau fichier et l'ajoute au projet
                FichierProjet fichierProjet = new FichierProjet(fichierNom, fichierChemin, fichierTexte);
                projet.addFichier(fichierProjet);
            }
        } catch (CsvValidationException | IOException e) {
            throw new RuntimeException(e);
        }
        return projets;
    } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
