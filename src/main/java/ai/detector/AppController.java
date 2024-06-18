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
import java.nio.file.Path;
import java.util.List;

import static ai.detector.DevoreurIO.chargerProjet;

public class AppController {
    @FXML private ListView<Projet> listeProjets;
    @FXML private ListView<FichierProjet> listeFichiers;
    @FXML private TextField nomFichierField;
    @FXML private TextField cheminFichierField;
    @FXML private TextField typeFichierField;
    @FXML private TextField scoreIAField;
    @FXML private Button btnCharger;
    @FXML private Button btnReanalyser;
    @FXML private Button btnAjouterProjet;
    @FXML private Button btnSupprimerProjet;
    @FXML private Button btnSauvegarderCSV;
    @FXML private Button btnChargerCSV;


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

            // Ajoute un listener pour listeFichiers
            listeFichiers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    updateFileDetails(newValue);
                }
            });

        }

        // Ajout d'un listener sur le bouton 'Charger'
        btnCharger.setOnAction(event -> chargerDepuisDossier());

        // Ajout d'un listener sur le bouton 'Réanalyser'
        btnReanalyser.setOnAction(event -> analyserFichier());

        // Ajout d'un listener sur le bouton 'Ajouter Projet'
        btnAjouterProjet.setOnAction(event -> ajouterProjet());

        // Ajout d'un listener sur le bouton 'Supprimer Projet'
        btnSupprimerProjet.setOnAction(event -> supprimerProjet());

        // Ajout d'un listener sur le bouton 'Sauvegarder en CSV'
        btnSauvegarderCSV.setOnAction(event -> sauvegarderEnCSV());

        // Ajout d'un listener sur le bouton 'Charger depuis CSV'
        btnChargerCSV.setOnAction(event -> chargerDepuisCSV());

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
                    System.out.println("- " + projet.getNom());
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

        // Ouvre une boite de dialogue pour sélectionner un dossier
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(null);

        // Charge le projet correspondant au chemin du dossier
        Projet projet = chargerProjet(selectedDirectory.toPath());
        projets.add(projet);

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
        nomFichierField.setEditable(false); // Non éditable

        cheminFichierField.setText(fichier.getChemin());
        cheminFichierField.setEditable(false); // Non éditable

        typeFichierField.setText(fichier.getType());
        typeFichierField.setEditable(false); // Non éditable

        scoreIAField.setText(String.valueOf(fichier.getScoreIA()));
        scoreIAField.setEditable(false); // Non éditable
    }

}
