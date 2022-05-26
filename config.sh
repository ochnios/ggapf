#!/bin/bash

# you should have downloaded project folder
# you should be in project folder

# 1. INSTALL JDK
sudo apt install openjdk-17-jdk

# 2. INSTALL MAVEN
# https://github.com/m-thirumal/installation_guide/blob/master/maven/upgrade_maven.md
# if you get 404 you should replace URL below with the url from the official Maven website
# you should replace also archive name
wget https://dlcdn.apache.org/maven/maven-3/3.8.5/binaries/apache-maven-3.8.5-bin.tar.gz
sudo mkdir -p /usr/local/apache-maven
sudo mv apache-maven-3.8.5-bin.tar.gz /usr/local/apache-maven
cd /usr/local/apache-maven
sudo tar -xzvf apache-maven-3.8.5-bin.tar.gz 

sudo echo "export M2_HOME=/usr/local/apache-maven/apache-maven-3.8.5" >> ~/.profile
sudo echo "export M2=$M2_HOME/bin" >> ~/.profile
sudo echo "export MAVEN_OPTS="-Xms256m -Xmx512m"" >> ~/.profile
sudo echo "export PATH=$M2:$PATH  " >> ~/.profile
source ~/.profile

# 3. COMPILE GGAPF
mvn compile

# 4. RUN GGAPF
mvn exec:java