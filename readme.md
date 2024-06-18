# Projet AI Detector V.3 (nom de version Gregoire)
Auteur : Grégoire Cornu (gcornu@enssat.fr)

Ce projet est une application Java pour la détection et la gestion de fichiers textuels dans des projets étudiants.

## Utilisation

### Menus
L'application AI Detector présente dans sa partie supérieur une barre de menus permettant de réinitialiser la fenêtre (Nouveau) ou de fermer l'application (Quitter) ; les autres fonctionnalités des menus sont pour le moment désactivées.

### Chargements et sauvegarde

Le bouton "Charger depuis un dossier" à droite des menus permet de charger des projets depuis un dossier sélectionné au moyen du bouton . A titre d'exemple, un dossier StudentProjects permet de tester l'application.

### Parties centrales de l'interface
Les données de projets, fichiers et informations peuvent être chargées depuis un fichier CSV au moyen du bouton "Charger depuis CSV" (ma_sauvegarde.csv fourni à titre d'exemple) ; une fois chargée (depuis un fichier CSV ou depuis un dossier), ces informations peuvent être sauvegardées dans un nouveau fichier CSV à partir du bouton "Sauvegarder CSV".
Pour ces trois boutons, un clique fait apparaitre une boite de dialogue permettant de sélectionner le chemin et le nom du dossier ou fichier.

Tous les dossiers contenus dans le dossier chargé seront assimilés à des projets uniques pouvant contenir des fichiers à tester. Les projets sont affichés dans la partie gauche de l'interface graphique de l'application.

Chaque projet peut contenir ou non, un ou plusieurs fichiers. Lorsqu'un dossier est sélectionné, a liste des fichiers contenus dans le projet apparaît dans la partie centrale de l'interface.

Si un fichier est sélectionné, un certain nombre d'informations sur le fichier sont affichées dans la partie droite de l'interface : nom du fichier, chemin du fichier, type du fichier et scoreIA. Le scoreIA sera prochainement obtenu au moyen d'une analyse complète et rigoureuse du texte contenu dans le fichier ; à ce stade du développement, un nombre décimal randomisé est attribué au fichier. Ce nombre peut être généré à nouveau au moyen du bouton "Réanalyser le fichier"

Dans la partie inférieures de l'interface, les boutons "Ajouter projet" et "Supprimer projet" permettent d'ajouter ou enlever des projets à la liste affichée dans le panneau droit.

## Evolutions depuis la V.2
Le passage de la V.2  la V.3 a été principalement l'occasion de la réécriture de l'interface graphique (app.fxml), la mise en place des écouteurs dans le contrôleur permettant de faire fonctionner ensemble l'interface graphique (la vue) et le modèle (la logique métier).
Quelques ajustements ont été fait dans le modèle, principalement dans les mécaniques DevoreurIO afin de gérer la lecture et l'écriture de fichiers CSV malgré la présence dans le contenu des fichiers de textes pouvant rendre leur constitution quelque peu complexe (la méthode escapeCSV aura permis d'encapsuler ces données proprement dans les fichiers CSV).

Dans la classe Projet, la variable Auteur et ses getter/setter ont été mis en commentaires car inutiles. La méthode toString a été conservée et même testée mais elle n'est pas utilisée dans cette version de l'application.
Les variables choisies et affichées sont redondantes : nom, type, chemin comportent à elles trois des informations qui auraient pu tenir en une ou deux variables.

La classe AppController était particulièrement incomplète, il a donc fallu la réécrire très largement.

### Tests Unitaires
Le présent code source contient 11 tests unitaires JUnit testant l'ensemble des classes et méthodes du modèle.

La V.2 ne contenait aucun test unitaire, ce qui n'est pas une pratique conseillée. Il manque à cette heure des tests pour la vue et le contrôleur ainsi que d'éventuels tests de bout en bout.

### Depôt Git
Afin de ne pas reproduire l'erreur de Dorel, ce projet a été sauvegardé depuis son commencement sur un espace GitHub, disponible à cette adresse :
https://github.com/GregoireCornuDev/AIDetector_v3_Gregoire 

### Temps passé
Interface graphique : 2h
Correction du code de la V.2, ajouts dans le controller et dans le modèle : 7h
Mise en place des tests unitaires : 1h
Rédaction du readme.md : 1h

# Licence

Ce projet est sous licence Libre MIT License.

