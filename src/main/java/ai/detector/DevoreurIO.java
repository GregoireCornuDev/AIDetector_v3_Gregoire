package ai.detector;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class DevoreurIO {
    public static void sauvegarderProjetsEnCSV(List<Projet> projets, String chemin) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(chemin))) {
            writer.write("Nom,Auteur,Nombre de Fichiers\n");
            for (Projet projet : projets) {
                // Cé bon non ? 
                writer.write(String.format("%s,%s,%d\n", projet.getNom(), projet.getAuteur(), projet.getFichiers().size()));
            }
        }
    }

    public static List<Projet> chargerProjetsDepuisCSV(String chemin) throws IOException {
        List<Projet> projets = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(chemin))) {
            String line;

            reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Projet projet = new Projet(parts[0]);
                projet.setAuteur(parts[1]);
                projets.add(projet);
            }
        }
        return projets;
    }

    public static List<Projet> chargerProjetsDepuisDossier(String dossierRacine) throws IOException {
        List<Projet> projets = new ArrayList<>();
        Files.list(Paths.get(dossierRacine)).filter(Files::isDirectory).forEach(dir -> {
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
            projets.add(projet);
        });
        return projets;
    }
}
