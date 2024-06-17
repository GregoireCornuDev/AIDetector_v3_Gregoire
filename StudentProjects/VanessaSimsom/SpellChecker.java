import org.languagetool.JLanguageTool;
import org.languagetool.Language;
import org.languagetool.LanguageTool;
import org.languagetool.rules.RuleMatch;

import java.io.IOException;
import java.util.List;

public class SpellChecker {
    public static void main(String[] args) {
        // Création de l'outil de vérification orthographique pour la langue française
        Language lang = new Language("fr", "French");
        JLanguageTool langTool = new JLanguageTool(lang);

        // Texte à vérifier
        String texte = "Ce texte contient quelque fautes d'orthographe.";

        // Vérification orthographique
        try {
            List<RuleMatch> matches = langTool.check(texte);
            
            // Affichage des fautes d'orthographe détectées
            for (RuleMatch match : matches) {
                System.out.println("Faute d'orthographe détectée : " + match.getMessage() +
                                   " à la position " + match.getColumn() + " dans le texte.");
                System.out.println("Correction proposée : " + match.getSuggestedReplacements());
            }
            
            // Affichage du nombre total de fautes d'orthographe
            System.out.println("Nombre total de fautes d'orthographe : " + matches.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
