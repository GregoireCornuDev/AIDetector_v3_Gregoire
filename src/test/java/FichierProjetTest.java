import ai.detector.FichierProjet;
import org.junit.Test;
import static org.junit.Assert.*;


public class FichierProjetTest {

    @Test
    public void constructeurEtGettersTest() {
        // Arrange
        String nom = "fichier.txt";
        String chemin = "/ressources/ai/detector/fichier.txt";
        String texte = "texte";


        // Génère un nouveau fichier
        FichierProjet fichier = new FichierProjet(nom, chemin, texte);

        // Vérifie que le nom du fichier est correctement retourné
        assertEquals(nom, fichier.getNom());

        // Vérifie que le chemin du fichier est correctement retourné
        assertEquals(chemin, fichier.getChemin());

        // Vérifie que le contenu du fichier est correctement retourné
        assertEquals(texte, fichier.getTexte());

        // Vérifie que le type du fichier est correctement retourné
        assertEquals("txt", fichier.getType());

        // Vérifie que le score obtenu est bien un nombre entre 0 et 1
        assertTrue(fichier.getScoreIA() >= 0 && fichier.getScoreIA() <= 1);
    }

    @Test
    public void setScoreIATest() {
        // Arrange
        FichierProjet fichier = new FichierProjet("test.txt", "/test.txt", "Test content");

        // Attribue un scoreIA au fichier
        fichier.setScoreIA(0.75);

        // Vérifie que le score obtenu est bien le score passé
        assertEquals(0.75, fichier.getScoreIA(), 0.001); // Utilise delta pour tolérance numérique
    }
}
