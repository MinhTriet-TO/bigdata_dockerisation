FROM ubuntu:18.04

ENV DEBIAN_FRONTEND noninteractive

LABEL org.opencontainers.image.authors="alexandre.odin.nlh@outlook.fr"

WORKDIR /home/bgd

# Installation des paquets nécessaires
RUN apt-get update && apt-get install -y openssh-server openjdk-8-jdk wget nano

# Installation Hadoop 2.7.2
RUN wget https://archive.apache.org/dist/hadoop/common/hadoop-2.7.2/hadoop-2.7.2.tar.gz && \
    tar -xzvf hadoop-2.7.2.tar.gz && \
    mv hadoop-2.7.2 /usr/local/hadoop && \
    rm hadoop-2.7.2.tar.gz

# Déclarations des variables d'environnement
ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64/jre/
ENV HADOOP_HOME=/usr/local/hadoop 
ENV HADOOP_CONF_DIR=/usr/local/hadoop/etc/hadoop
ENV LD_LIBRARY_PATH=/usr/local/hadoop/lib/native:$LD_LIBRARY_PATH
ENV PATH=$PATH:/usr/local/hadoop/bin:/usr/local/hadoop/sbin

# Ajouter les dossiers utils
ADD wordcount /home/bgd/java/wordcount
ADD input /home/bgd/input

CMD [ "sh", "-c", "service ssh start; bash"]