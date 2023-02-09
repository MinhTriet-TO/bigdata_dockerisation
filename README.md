# PAO Conteneurisation des TPs de l'EC Big Data

## Objectifs

Le but de ce PAO est de faciliter l'installation et l'utilisation des logiciels nécessaires aux TPs de Big Data en utilisant la conteneurisation.
Ce gain de temps dans la mise en place permet d'ajouter des exercices supplémentaires aux TPs et donc permettre aux élèves de plus pratiquer. L'autre avantage de la conteneurisation est de s'affranchir des machines virtuelles qui sont lourdes en termes de stockages et de consommation de ressources.

## Image Docker/Podman

Après concertation, nous avons décidé de créer une seule image comportant toutes les briques logicielles de l'architecture lambda explicitée dans le cours : Hadoop, Spark, Kafka et MongoDB. La construction de cette image se fait par l'interface du Dockerfile. En effet, celui-ci contient toutes les commandes permettant de créer l'environnement des TPs : installations des bibliothèques et des logiciels, création des variables d'environnement, attributions des droits de certains des fichiers, etc.

### Création de l'image

Pour générer l'image, il faut se trouver dans le dossier contenant le Dockerfile puis ouvrir un terminal et lancer cette commande :

* `podman image build -t <nom_image>:<version> .` 

Ex : `podman image build -t tpbigdata:latest .` 

Vous pouvez néanmois, en cas de problèmes, tirer la dernière version que nous avons publiée sur [DockerHub](https://hub.docker.com/r/odinho/tpbigdata) de cette façon :

`podman image pull docker.io/odinho/tpbigdata:latest` pour la version **complète**.

`podman image pull docker.io/odinho/tpbigdata:eleves` pour la version **élève cad. sans les configurations des logiciels**.


Si vous utilisez cette méthode, le nom de l'image à utiliser sera `odinho/tpbigdata:latest` ou `odinho/tpbigdata:eleves`

### Lancement de l'image
#### Une seule node

Dans le cas des TPs où il ne faut qu'une seule "machine", il ne faut lancer qu'un seul conteneur :

* `podman run -itd -p <port> --name <nom_conteneur> --hostname <nom_conteneur> <nom_image>:<version>` (le port est facultatif)

Ex : `podman run -itd --name hadoop --hostname hadoop tpbigdata:latest `

Pour accéder au conteneur et à son environnement, il faut entrer la commande suivante :

* `podman exec -it <nom_conteneur> bash`

Ex : `podman exec -it hadoop bash`

Cela permet "d'être" dans le conteneur et pouvoir lancer des commandes Linux.

#### Cluster : un master et deux datanodes

Dans le cas du TP Cluster, il faut lancer trois conteneurs, un pour le master et deux pour les datanodes. Mais avant de les lancer, il faut créer un réseau Podman dans lequel les conteneurs vont pouvoir interagir. Pour cela, on utilise cette commande :

* `podman network create --driver=bridge <nom_réseau>`

Ex : `podman network create --driver=bridge hadoop`

Une fois ce réseau crée, on peut lancer les 3 conteneurs sur celui-ci avec l'option `--net` :

* `podman run -itd --net=hadoop -p 50070:50070 -p 8088:8088 --name hadoop-master --hostname hadoop-master tpbigdata:latest` . 
Le port 50070 permet d'accéder à l'interface web de HDFS et le port 8088 à l'interface web Yarn du cluster.

* `podman run -itd --net=hadoop -p 8040:8042 --name hadoop-slave1 --hostname hadoop-slave1 tpbigdata:latest` .
Le port 8040 permet d'accéder à l'interface web Yarn de la datanode 1.

* `podman run -itd --net=hadoop -p 8041:8042 --name hadoop-slave2 --hostname hadoop-slave2 tpbigdata:latest` .
Le port 8041 permet d'accéder à l'interface web Yarn de la datanode 2.

## Autres commandes usuelles

* Lister les images stockées en local : `podman image ls`

* Lister les conteneurs : `podman container ls`

* Stopper un conteneur : `podman container stop <nom_conteneur>`

* Supprimer un conteneur : `podman container rm <nom_conteneur>` . SUPPRIMER UN CONTENEUR NE SUPPRIME PAS L'IMAGE

* Lister les réseaux existants : `podman network ls`

* Supprimer un réseau précèdemment crée : `podman network rm <nom_réseau>`

* Copier un fichier de la machine local vers un conteneur : `podman cp <nom_fichier_local> <nom_conteneur>:<chemin>/<nom_fichier>`

* Copier un fichier d'un conteneur vers la machine locale : `podman cp <nom_conteneur>:<chemin>/<nom_fichier> <chemin>/<nom_fichier_local> `

[_Documentation complète de Podman_](https://docs.podman.io/en/latest/Commands.html)
## Versions actuelles des logiciels

* Ubuntu 18.04
* Haddop 2.7.2
* Spark 3.2.3
* Kafka 2.11-1.0.2
* MongoDB 3.6.3

## Références 

* https://github.com/liliasfaxi/hadoop-cluster-docker/
* https://insatunisia.github.io/TP-BigData/
* https://docs.podman.io/en/latest/Commands.html
