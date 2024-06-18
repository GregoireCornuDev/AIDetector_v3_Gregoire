import org.junit.Before;
import org.junit.Test;

import ai.detector.DevoreurIO;
import ai.detector.Projet;
import ai.detector.FichierProjet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class DevoreurIOTest {

    private static final String PROJETS_DOSSIER = "test_projets";
    private static final String FICHIER_CSV = "test_sauvegarde.csv";
    private static final String PROJET = "test_projet";

    @Before
    public void setUp() throws IOException {

        // Crée un dossier de test avec des fichiers
        Files.createDirectories(Paths.get(PROJETS_DOSSIER, PROJET));
        Files.writeString(Paths.get(PROJETS_DOSSIER + "/" + PROJET, "fichier1.txt"), "Contenu fichier 1");
        Files.writeString(Paths.get(PROJETS_DOSSIER + "/" + PROJET, "fichier2.txt"), "Contenu fichier 2");
    }

    @Test
    public void sauvegarderProjetsEnCSVTest() throws IOException {

        // Crée un projet avec deux fichiers
        Projet projet = new Projet("Projet Test");
        projet.addFichier(new FichierProjet("fichier1.txt", "/fichier1.txt", "Contenu fichier 1"));
        projet.addFichier(new FichierProjet("fichier2.txt", "/fichier2.txt", "Contenu fichier 2"));

        // Sauvegarde le projet dans un fichier CSV
        DevoreurIO.sauvegarderProjetsEnCSV(List.of(projet), FICHIER_CSV);

        // Vérifie que le contenu du fichier CSV est correctement généré
        List<String> lines = Files.readAllLines(Paths.get(FICHIER_CSV));
        assertEquals("Projet, NomFichier, Chemin, Texte", lines.get(0));
        assertEquals("Projet Test, fichier1.txt, /fichier1.txt, Contenu fichier 1", lines.get(1));
        assertEquals("Projet Test, fichier2.txt, /fichier2.txt, Contenu fichier 2", lines.get(2));

        // Supprime le fichier CSV après le test
        Files.deleteIfExists(Paths.get(FICHIER_CSV));
    }

    @Test
    public void chargerProjetsDepuisCSVTest() throws IOException {
        // Création d'un fichier CSV de test
        String csvContent = "Projet, NomFichier, Chemin, Texte\n" +
                "Projet Test, fichier1.txt, /fichier1.txt, Contenu fichier 1\n" +
                "Projet Test, fichier2.txt, /fichier2.txt, Contenu fichier 2";
        Files.writeString(Paths.get(FICHIER_CSV), csvContent);

        // Chargement des projets depuis le fichier CSV
        List<Projet> projets = DevoreurIO.chargerProjetsDepuisCSV(FICHIER_CSV);

        // Vérification des projets chargés
        assertEquals(1, projets.size());
        Projet projet = projets.get(0);
        assertEquals("Projet Test", projet.getNom());
        assertEquals(2, projet.getFichiers().size());
        assertEquals(" fichier1.txt", projet.getFichiers().get(0).getNom());
        assertEquals(" fichier2.txt", projet.getFichiers().get(1).getNom());

        // Suppression du fichier CSV après le test
        Files.deleteIfExists(Paths.get(FICHIER_CSV));
    }

    @Test
    public void chargerProjetsDepuisDossierTest() throws IOException {
        // Charge des projets depuis un dossier de test
        List<Projet> projets = DevoreurIO.chargerProjetsDepuisDossier(PROJETS_DOSSIER);

        // Vérifie le projet chargé
        assertEquals(1, projets.size());
        Projet projet = projets.get(0);
        assertEquals("test_projet", projet.getNom());
        assertEquals(2, projet.getFichiers().size());
        assertEquals("fichier1.txt", projet.getFichiers().get(0).getNom());
        assertEquals("fichier2.txt", projet.getFichiers().get(1).getNom());

        // Supprime le dossier de test après le test
        Files.walk(Paths.get(PROJETS_DOSSIER + "/" + PROJET))
                .map(Path::toFile)
                .forEach(File::delete);
        Files.deleteIfExists(Paths.get(PROJETS_DOSSIER + "/" + PROJET));
    }

    @Test
    public void chargerProjetTest() throws IOException {
        // Chargement d'un projet depuis un dossier de test
        Projet projet = DevoreurIO.chargerProjet(Paths.get(PROJETS_DOSSIER + "/" + PROJET));

        // Vérification du projet chargé
        assertEquals("test_projet", projet.getNom());
        assertEquals(2, projet.getFichiers().size());
        assertEquals("fichier1.txt", projet.getFichiers().get(0).getNom());
        assertEquals("fichier2.txt", projet.getFichiers().get(1).getNom());

        // Suppression du dossier de test après le test
        Files.walk(Paths.get(PROJETS_DOSSIER + "/" + PROJET))
                .map(Path::toFile)
                .forEach(File::delete);
        Files.deleteIfExists(Paths.get(PROJETS_DOSSIER + "/" + PROJET));
    }

    @Test
    public void escapeCSVTest() {
        // Test d'échappement pour CSV
        assertEquals("\"Hello, World\"", DevoreurIO.escapeCSV("Hello, World"));
        assertEquals("\"Hello\"\"World\"", DevoreurIO.escapeCSV("Hello\"World"));
        assertEquals("NormalText", DevoreurIO.escapeCSV("NormalText"));
    }
}
