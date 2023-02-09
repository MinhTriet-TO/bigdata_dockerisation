# Des remarques pour effectuer TP3

- Le Dockerfile version complète est suffisante pour réaliser ce TP
- L'installation d'image et le lancement des conteneurs sont expliqués dans le fichier Readme.
- Une fois placé dans le conteneur _hadoop-bash_, lancez la commande suivante:
```bash
./start-hadoop.sh
```
Et le cluster devrait être fonctionnel. Les exos WordCount & Weather fonctionnent bien, tout en suivant le sujet de TP précédemment écrit.  

**Attention**: Ne lisez pas le contenu du répertoire _output_ quand vous êtes dans le conteneur, vous aurez un message: 'Connection time out'. A la place, une fois le job MapReduce est fini, récupérez le résultat depuis HDFS vers le conteneur avec:
```bash
hdfs dfs -get /output .
```
Ensuite, transférez le résultat vers la machine locale avec:
```bash
podman cp nom_du_conteneur:/root/output .
```
Et vous pourrez afficher le contenu depuis la machine locale.