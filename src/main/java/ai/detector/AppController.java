package ai.detector;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AppController {
    @FXML private ListView<Projet> listeProjets;
    @FXML private ListView<FichierProjet> listeFichiers;
    @FXML private TextField nomFichierField;
    @FXML private TextField cheminFichierField;
    @FXML private TextField typeFichierField;
    @FXML private TextField scoreIAField;
    @FXML private Button btnCharger;



    private ObservableList<Projet> projets = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        if (listeProjets != null) {
            listeProjets.setItems(projets);

            listeProjets.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    listeFichiers.setItems(FXCollections.observableArrayList(newValue.getFichiers()));
                }
            });

            //TODO: Ajouter le listener pour les fichiers aussi
            // et maj les détails avec la bonne fct.
        }

        // Ajout d'un listener sur le bouton
        btnCharger.setOnAction(event -> chargerDepuisDossier());

    }


    @FXML
    private void chargerDepuisDossier() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory != null) {
            try {
                List<Projet> loadedProjects = DevoreurIO.chargerProjetsDepuisDossier(selectedDirectory.getAbsolutePath());
                projets.setAll(loadedProjects);

                // Affichage des noms des projets
                System.out.println("Liste des projets chargés :");
                for (Projet projet : projets) {
                    System.out.println("- " + projet.getNom()); // Supposant que tu aies une méthode getNom() dans la classe Projet
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void chargerDepuisCSV() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                List<Projet> loadedProjects = DevoreurIO.chargerProjetsDepuisCSV(selectedFile.getAbsolutePath());
                projets.setAll(loadedProjects);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void sauvegarderEnCSV() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile != null) {
            try {
                DevoreurIO.sauvegarderProjetsEnCSV(projets, selectedFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void ajouterProjet() {
        // Heu, cé bien ça ki faut faire non ?
        Projet newProjet = new Projet("Nouveau Projet");
        projets.add(newProjet);
    }

    @FXML
    private void supprimerProjet() {
        Projet selectedProjet = listeProjets.getSelectionModel().getSelectedItem();
        if (selectedProjet != null) {
            projets.remove(selectedProjet);
        }
    }

    @FXML
    private void analyserFichier() {
        FichierProjet selectedFichier = listeFichiers.getSelectionModel().getSelectedItem();
        if (selectedFichier != null) {
            selectedFichier.setScoreIA(Math.random());
            updateFileDetails(selectedFichier);
        }
    }

    private void updateFileDetails(FichierProjet fichier) {
        nomFichierField.setText(fichier.getNom());
        // Ajouter chemin dans le bon text field
        // typeFichierField.setText(fichier .getType());
        scoreIAField.setText(String.valueOf(fichier.getScoreIA()));
    }

    @FXML private Label debugLabel;
}
