Contenue de l'archive :

- images        dossier des ressources utilisé par l'application client.
- client.jar    exécutable du client.
- readme.txt    explication d'installation du fonctionnement du programme.
- script.sql    script de création et de peuplement de la base de donnée.
- server.jar    exécutable du serveur.


1) Prérequis :

- Logiciel XAMPP pour la base de donnée, télécharger la version correspondant à son système d'exploitation depuis le lien suivant : https://www.apachefriends.org/download.html

- OpenJdk 11 ou version supérieur à télécharger sur le site suivant : https://jdk.java.net/java-se-ri/11

2) Installation :

Pour l'installation de XAMPP et OpenJdk, suivre la documentation sur les sites données ci-dessus.
Pour initaliser la base donnée il suffit après avoir installer XAMPP, lancer l'interface graphique et démarrer les serveurs MySQL Database et Apache Web Server de se rendre à l'aide d'un navigateur web sur le lien suivant : http://localhost/phpmyadmin/ crée une nouvelle base de donnée (eventuellement créer un compte utilisateur si vous le souhaitez), cliquer dessus et aller dans l'onglet import (ou importer) puis cliquer sur Browse (ou parcourir) et sélectionner script.sql fourni dans l'archive et cliquer sur GO en bas à droite, vous devriez maintenant les tables s'afficher sous le nom de votre base de donnée.

3) Exécution Serveur :

Pour la suite il suffit simplement de lancer l'exécutable server.jar, il demandera d'abord de se connecter à la base de données, il faudra donc fournir le lien de connexion, l'utilisateur que vous avez choisi dans phpMyAdmin (par défaut root) et son mot de passe (par défaut aucun).

les informations à transmettre sont de la forme :
    URL : jdbc:mysql://localhost:3306/test   : avec test le nom de la base de donnée.
    USERNAME : root                          : le nom d'utilisateur de la base de donnée.
    PASSWORD :                               : son mot de passe (par défaut vide pour l'utilisateur "root")

ensuite une fois la connexion avec la base de donnée réussi : on arrive sur l'interface principal assez intuitive avec deux onglets "Users" et "Groups" et respectivement des boutons Add, Edit et Delete pour chacun et la liste des de tout les utilisateurs (respectivement groupes) au milieu.

4) Exécution Client :

il faut mettre lancer l'exécutable client.jar dans le meme dossier ou se trouve le dossier images ensuite pareil rien de plus simple il suffit de se connecter avec les identifiants d'un utilisateur (voir si dessous quelques exemples d'identifiants qui fonctionne) et utiliser l'application normalement.

5) Quelques identifiants de connexions :

tous les mots de passes des utilisateurs fourni par le script sql sont "pass"
seule leur identifiants vont différer, voici quelque uns :

username    type
km01        campus
bb03        campus
pm01        service
pm02        service
ch01        service
