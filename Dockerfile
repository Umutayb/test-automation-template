FROM sgrio/java-oracle
MAINTAINER Umutayb (umutaybora@gmail.com)
RUN apt-get update
RUN apt-get install -y maven
COPY pom.xml /usr/local/service/pom.xml
COPY src /usr/local/service/src
WORKDIR /usr/local/service/
RUN mvn package
CMD ["java","-cp","target/Web-Automation-0.5.0.jar"]