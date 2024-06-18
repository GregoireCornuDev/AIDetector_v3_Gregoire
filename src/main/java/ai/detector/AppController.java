package ai.detector;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static ai.detector.DevoreurIO.chargerProjet;

public class AppController {
    @FXML private ListView<Projet> listeProjets;
    @FXML private ListView<FichierProjet> listeFichiers;
    @FXML private TextField nomFichierField;
    @FXML private TextField cheminFichierField;
    @FXML private TextField typeFichierField;
    @FXML private TextField scoreIAField;
    @FXML private MenuItem menuItemNouveau;
    @FXML private MenuItem menuItemQuitter;
    @FXML private Button btnCharger;
    @FXML private Button btnReanalyser;
    @FXML private Button btnAjouterProjet;
    @FXML private Button btnSupprimerProjet;
    @FXML private Button btnSauvegarderCSV;
    @FXML private Button btnChargerCSV;


    private ObservableList<Projet> projets = FXCollections.observableArrayList();

    /**
     * Initialise l'état de la fenêtre principale de la vue du programme
     */
    @FXML
    private void initialize() {


        // Si la liste des projets n'est pas un fichier vide (même si aucun projet n'est dans la liste)..
       // if (listeProjets != null) {

            listeProjets.setItems(projets);

            // Ajoute un listener pour la liste des projets
            listeProjets.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    listeFichiers.setItems(FXCollections.observableArrayList(newValue.getFichiers()));
                }
            });

            // Ajoute un listener pour la liste des fichiers
            listeFichiers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    updateFileDetails(newValue);
                }
            });

       // }

        // Ajout d'un listener sur le menu 'Nouveau'
        menuItemNouveau.setOnAction(event -> nouvelleFenetre());

        // Ajout d'un listener sur le menu 'Quitter'
        menuItemQuitter.setOnAction(event -> quitterApplication(event));

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

    /**
     * Controleur du bouton 'Charger depuis un dossier'
     */
    @FXML
    private void chargerDepuisDossier() {
        DirectoryChooser directoryChooser = new DirectoryChooser();

        // Ouvre une boîte de dialogue pour sélectionner le dossier et chemin des projets
        File selectedDirectory = directoryChooser.showDialog(null);

        // Si le dossier est non vide ..
        if (selectedDirectory != null) {
            try {
                // .. charge les projets depuis le dossier sélectionné
                List<Projet> loadedProjects = DevoreurIO.chargerProjetsDepuisDossier(selectedDirectory.getAbsolutePath());

                // Ajoute les projets dans la fenêtre des projets
                projets.setAll(loadedProjects);

                // Debug : affiche les noms des projets dans le terminal
                System.out.println("Liste des projets chargés :");
                for (Projet projet : projets) {
                    System.out.println("- " + projet.getNom());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Controleur du bouton 'Charger depuis un CSV'
     */
    @FXML
    private void chargerDepuisCSV() {
        FileChooser fileChooser = new FileChooser();

        // Ouvre une boîte de dialogue pour sélectionner un fichier CSV à importer
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {

            // Importe le fichier CSV correspondant au chemin et nom sélectionnés
            List<Projet> loadedProjects = DevoreurIO.chargerProjetsDepuisCSV(selectedFile.getAbsolutePath());
            projets.setAll(loadedProjects);
        }
    }

    /**
     * Controleur du bouton 'Sauvegarder en CSV'
     */
    @FXML
    private void sauvegarderEnCSV() {
        FileChooser fileChooser = new FileChooser();

        // Ouvre une boite de dialogue pour un chemin et un nom pour la sauvegarde
        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile != null) {
            try {
                // Sauvegarde l'état dans un fichier au chemin et nom indiqués
                DevoreurIO.sauvegarderProjetsEnCSV(projets, selectedFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Controleur du bouton 'Ajouter un projet'
     */
    @FXML
    private void ajouterProjet() {

        // Ouvre une boite de dialogue pour sélectionner un dossier
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(null);

        // Charge le projet correspondant au chemin du dossier
        Projet projet = chargerProjet(selectedDirectory.toPath());
        projets.add(projet);
    }

    /**
     * Controleur du bouton 'Supprimer un projet'
     */
    @FXML
    private void supprimerProjet() {

        // Identifie le projet sélectionné (null si aucun sélectionné)
        Projet selectedProjet = listeProjets.getSelectionModel().getSelectedItem();

        // S'il y a un projet sélectionné
        if (selectedProjet != null) {

            // Supprime le projet sélectionné
            projets.remove(selectedProjet);

            if (projets.isEmpty()) {
                // Si tous les projets sont supprimés, la liste des fichiers et les champs sont vidés
                listeFichiers.setItems(FXCollections.observableArrayList());
                effacerFichierInfos();
            } else {
                // Si la liste des projets n'est pas vide, affichez les fichiers du premier projet
                listeProjets.getSelectionModel().selectFirst();
            }
        }
    }

    @FXML
    private void nouvelleFenetre() {
        // Réinitialise la liste des projets et des fichiers
        listeProjets.getItems().clear();
        listeFichiers.getItems().clear();

        // Réinitialisation des champs de texte
        effacerFichierInfos();
    }

    /**
     * Supprime le contenu des champs
     */
    private void effacerFichierInfos() {
        nomFichierField.clear();
        cheminFichierField.clear();
        typeFichierField.clear();
        scoreIAField.clear();
    }


    /**
     * Controleur du bouton 'Réanalyser'
     */
    @FXML
    private void analyserFichier() {
        FichierProjet selectedFichier = listeFichiers.getSelectionModel().getSelectedItem();
        if (selectedFichier != null) {
            selectedFichier.setScoreIA(Math.random());
            updateFileDetails(selectedFichier);
        }
    }

    /**
     * Met à jour les champs d'infos sur le fichier et les rends non-éditables
     * @param fichier : Le fichier dont les informations doivent être affichés
     */
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

    /**
     * Ferme la fenêtre
     * @param event
     */
    @FXML
    private void quitterApplication(ActionEvent event) {
        Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        stage.close();
    }

}
