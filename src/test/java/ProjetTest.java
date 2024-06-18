import org.junit.Before;
import org.junit.Test;

import ai.detector.Projet;
import ai.detector.FichierProjet;

import java.util.List;

import static org.junit.Assert.*;

public class ProjetTest {

    private Projet projet;

    @Before
    public void setUp() {
        projet = new Projet("Projet Test");
    }

    @Test
    public void getNomTest() {

        // Vérifie que le nom du projet est correctement retourné
        assertEquals("Projet Test", projet.getNom());
    }

    @Test
    public void getFichiersTest() {
        // Vérifie que la liste des fichiers peut être vide
        assertTrue(projet.getFichiers().isEmpty());
    }

    @Test
    public void addFichierTest() {

        FichierProjet fichier1 = new FichierProjet("fichier1.txt", "/fichier1.txt", "Contenu fichier 1");
        FichierProjet fichier2 = new FichierProjet("fichier2.txt", "/fichier2.txt", "Contenu fichier 2");

        projet.addFichier(fichier1);
        projet.addFichier(fichier2);

        List<FichierProjet> fichiers = projet.getFichiers();

        // Vérifie que la liste des fichiers est correcte (2) et correctement retournée
        assertEquals(2, fichiers.size());
        assertTrue(fichiers.contains(fichier1));
        assertTrue(fichiers.contains(fichier2));
    }

    @Test
    public void toStringTest() {
        // Vérifie que la méthode retourne correctement le nom (méthode legacy, non utilisée en V.3
        assertEquals("Projet Test", projet.toString());
    }
}