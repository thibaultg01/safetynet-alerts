# safetynet-alerts


## Objectif

Ce projet est une API REST développée en Java avec Spring Boot. Elle permet de fournir des informations essentielles aux services de secours à partir de données contenues dans un fichier `data.json`.


## Fonctionnalités

- Gérer les personnes, dossiers médicaux et casernes de pompiers (CRUD complet)
- Alerter les services de secours avec des endpoints spécifiques :
  - `/firestation`: Liste des personnes couvertes par une caserne
  - `/childAlert`: Liste des enfants à une adresse
  - `/phoneAlert`: Numéros de téléphone des habitants d’une zone
  - `/fire`: Infos médicales des habitants d’un foyer en cas d'incendie
  - `/flood/stations`: Infos pour plusieurs stations
  - `/personInfo`: Dossier complet d’une personne
  - `/communityEmail`: Emails des habitants d’une ville
  
  
 ## Architecture
  
  src/
├── controller/
├── dto/
├── model/
├── repository/
│ ├── impl/
├── service/
│ ├── impl/
├── utils/
└── resources/
├── data.json
├── application.properties
└── log4j2.xml


## Prérequis

- Java 17+
- Maven


## Commandes

mvn clean install
mvn spring-boot:run
