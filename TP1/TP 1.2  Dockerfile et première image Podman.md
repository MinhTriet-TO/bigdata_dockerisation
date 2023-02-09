# TP1.2 : Dockerfile et première image Podman

## Etapes de création

### 1. Image de base
La première chose que vous devez faire est de créer un fichier nommé "Dockerfile", puis de définir dans celui-ci l'image que vous allez utiliser comme base, grâce à l'instruction `FROM`. Dans notre cas, nous allons utiliser une image de base Ubuntu 18.04. :

* `FROM ubuntu:18.04`	(`FROM` n'est utilisable qu'une seule fois dans un Dockerfile)

### 2. Répertoire de travail
Utilisez ensuite l'instruction `WORKDIR` qui permet de modifier le répertoire courant. La commande est équivalente à une commande cd en ligne de commande. L'ensemble des commandes qui suivront seront toutes exécutées depuis le répertoire défini.

* `WORKDIR /home/bgd`

### 3. Installation des paquets/services/bibliothèques essentiels
L’étape suivante consiste à installer les différentes bibliothèques/services dont nous allons avoir besoin pour faire tourner les différents logiciels installés par la suite. Pour cela, on utilise la commande RUN qui permet d’exécuter des commandes Linux. Tout d’abord, on fait une mise à jour des paquets (`apt-get update`) puis on installe les paquets qui nous intéressent : ***wget*** pour télécharger un fichier depuis une url, ***openssh*** qui permet de lancer un serveur en ssh dans le conteneur, ***openjdk*** pour l’environnement Java 8 puis ***nodejs*** pour lance un serveur NodeJs.

* `RUN apt-get update && apt-get install -y openssh-server openjdk-8-jdk wget vim nodejs npm`

### 4. Installation des logiciels
Maintenant que nous avons tout ces paquets/services sur le conteneur, on peut les utiliser pour installer nos logiciels. Premièrement, il faut télécharger l'archive d'Hadoop 2.7.3 à l'aide de ***wget*** avec cette adressse https://archive.apache.org/dist/hadoop/common/hadoop-2.7.2/hadoop-2.7.2.tar.gz. Ensuite, l'archive doit être décompressée avec la commande `tar -xzvf <nom-archive>`.
* Pour mettre plusieurs commandes dans un `RUN`, il faut ajouter `&& \` à la fin de la commande puis faire un retour à la ligne pour en ajouter une nouvelle.
  
Une fois l'archive décompressée, avec la commande ***mv***, déplacer le dossier obtenu dans ***/usr/local/hadoop*** puis supprimer l'archive.
Réalisez deux autres blocs `RUN` pour :
* **Spark** : https://dlcdn.apache.org/spark/spark-3.2.3/spark-3.2.3-bin-hadoop2.7.tgz (déplacer archive décompressée dans ***/usr/local/spark***)
* **Kafka** : https://archive.apache.org/dist/kafka/1.0.2/kafka_2.11-1.0.2.tgz (déplacer archive décompressée dans ***/usr/local/kafka***)

Pour MongoDB, l'installation est différente :

```
RUN apt-get update && \
    apt-get install -y mongodb && \
    service mongodb start
```
### 5. Variables d'environnement

Pour que l'environnement de travail du conteneur soit fonctionnel, il faut déclarer ses variables ([petit rappel](https://doc.ubuntu-fr.org/variables_d_environnement#les_variables_d_environnement)). Il faut définir les **HOME** des logiciels afin que l'OS du conteneur sache où trouver les logciels :
```
ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64/jre 
ENV HADOOP_HOME=/usr/local/hadoop 
ENV SPARK_HOME=/usr/local/spark
ENV KAFKA_HOME=/usr/local/kafka
```
Il faut aussi définir le chemin dans lequel se trouve la configuration d'Hadoop :
```
ENV HADOOP_CONF_DIR=/usr/local/hadoop/etc/hadoop
```
Lorsque vous tapez une commande, le système la cherche dans les dossiers spécifiés par la variable `PATH`, dans l'ordre où ils sont indiqués. Il faut donc spécifier les chemins vers Hadoop, Spark et Kakfa dans cette variable : 
```
ENV LD_LIBRARY_PATH=/usr/local/hadoop/lib/native:$LD_LIBRARY_PATH
ENV PATH=$PATH:/usr/local/hadoop/bin:/usr/local/hadoop/sbin:/usr/local/spark/bin:/usr/local/kafka/bin
```
### 6. Configuration du service ssh

Dans la suite des TPs, vous allez devoir installer et configurer un cluster Hadoop avec une master node et deux data nodes ce qui équivaut à trois conteneurs. Afin qu'ils puissent communiquer entre eux pour réaliser des tâches, il faut configurer le service ssh. Les commandes suivantes permettent de mettre en place un ssh sans clés :

```
RUN ssh-keygen -t rsa -f ~/.ssh/id_rsa -P '' && \
    cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
```
