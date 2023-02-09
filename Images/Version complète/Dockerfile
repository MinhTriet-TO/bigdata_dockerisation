FROM ubuntu:18.04

ENV DEBIAN_FRONTEND noninteractive

LABEL org.opencontainers.image.authors="alexandre.odin.nlh@outlook.fr"

WORKDIR /home/bgd

# Installation des paquets nécessaires
RUN apt-get update && apt-get install -y openssh-server openjdk-8-jdk wget vim nodejs npm

# Installation Hadoop 2.7.2
RUN wget https://archive.apache.org/dist/hadoop/common/hadoop-2.7.2/hadoop-2.7.2.tar.gz && \
    tar -xzvf hadoop-2.7.2.tar.gz && \
    mv hadoop-2.7.2 /usr/local/hadoop && \
    rm hadoop-2.7.2.tar.gz

# Installation MongoDB 
RUN apt-get update && \
    apt-get install -y mongodb && \
    service mongodb start  

# Installallation Spark 3.2.3
RUN wget https://dlcdn.apache.org/spark/spark-3.2.3/spark-3.2.3-bin-hadoop2.7.tgz && \
    tar -xzvf spark-3.2.3-bin-hadoop2.7.tgz && \
    mv spark-3.2.3-bin-hadoop2.7 /usr/local/spark && \
    rm spark-3.2.3-bin-hadoop2.7.tgz

# Installation Kafka 2.11-1.0.2
RUN wget https://archive.apache.org/dist/kafka/1.0.2/kafka_2.11-1.0.2.tgz && \
    tar -xzvf kafka_2.11-1.0.2.tgz && \
    mv kafka_2.11-1.0.2 /usr/local/kafka && \
    rm kafka_2.11-1.0.2.tgz

# Déclarations des variables d'environnement
ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 
ENV HADOOP_HOME=/usr/local/hadoop 
ENV SPARK_HOME=/usr/local/spark
ENV KAFKA_HOME=/usr/local/kafka
ENV HADOOP_CONF_DIR=/usr/local/hadoop/etc/hadoop
ENV LD_LIBRARY_PATH=/usr/local/hadoop/lib/native:$LD_LIBRARY_PATH
ENV PATH=$PATH:/usr/local/hadoop/bin:/usr/local/hadoop/sbin:/usr/local/spark/bin:/usr/local/kafka/bin

# Configuration SSH sans clés
RUN ssh-keygen -t rsa -f ~/.ssh/id_rsa -P '' && \
    cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys

RUN mkdir -p /home/bgd/hdfs/namenode && \
    mkdir -p /home/bgd/hdfs/datanode && \
    mkdir $HADOOP_HOME/logs

COPY config/* /tmp/

RUN mv /tmp/ssh_config ~/.ssh/config && \
    mv /tmp/hadoop-env.sh /usr/local/hadoop/etc/hadoop/hadoop-env.sh && \
    mv /tmp/hdfs-site.xml $HADOOP_HOME/etc/hadoop/hdfs-site.xml && \
    mv /tmp/core-site.xml $HADOOP_HOME/etc/hadoop/core-site.xml && \
    mv /tmp/mapred-site.xml $HADOOP_HOME/etc/hadoop/mapred-site.xml && \
    mv /tmp/yarn-site.xml $HADOOP_HOME/etc/hadoop/yarn-site.xml && \
    mv /tmp/slaves $HADOOP_HOME/etc/hadoop/slaves && \
    mv /tmp/start-kafka-zookeeper.sh /home/bgd/start-kafka-zookeeper.sh && \
    mv /tmp/start-hadoop.sh /home/bgd/start-hadoop.sh && \
    mv /tmp/run-wordcount.sh /home/bgd/run-wordcount.sh && \
    mv /tmp/book.txt /home/bgd/book.txt && \
    mv /tmp/english_words.txt /home/bgd/english_words.txt && \
    mv /tmp/restaurants.json /home/bgd/restaurants.json && \
    mv /tmp/spark-defaults.conf $SPARK_HOME/conf/spark-defaults.conf

RUN chmod +x /home/bgd/start-hadoop.sh && \
    chmod +x /home/bgd/start-kafka-zookeeper.sh && \
    chmod +x /home/bgd/run-wordcount.sh && \
    chmod +x $HADOOP_HOME/sbin/start-dfs.sh && \
    chmod +x $HADOOP_HOME/sbin/start-yarn.sh 

# Format namenode
RUN /usr/local/hadoop/bin/hdfs namenode -format

CMD [ "sh", "-c", "service ssh start; bash"]


