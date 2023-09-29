# issProject2023
Group project for the university course "Ingegneria dei sistemi software" Prof. Antonio Natali

# Index
- [Sprint table](#sprint-table)
- [How to install](#how-to-install)

# Sprint table

| Sprint                                                                                                                                                  | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                 | TODO                                                                                             |
|---------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------|
| [Sprint0](https://htmlpreview.github.io/?https://github.com/LEOB3TA/ColdStorageService-Project/blob/main/Sprint0Reviewed/userDocs/sprint0Reviewed.html) | esplicitare la natura (POJO / servzi / attori …) dei (macro)componenti del sistema e sulle interazioni tra questi che sono esplicitamente definite o deducibili dal testo dei requisiti, creare una lista di priorità dei requisiti, ponendo al primo posto quelli che coprono il core-business, delineare una prima ipotesi di sprint e una prima valutazione dei tempi previsti per ogni sprint, definire il goal dello sprint1 e creare un nuovo progetto per lo sprint1 |                                                                                                  |
| [Sprint1](https://htmlpreview.github.io/?https://github.com/LEOB3TA/ColdStorageService-Project/blob/main/Sprint1/userDocs/sprint1.html)                 | Core business dell'applicazione: <ul> <li>Transport trolley e interfacciamento con il basic robot (Gianmiriano Porrazzo)</li><li>Cold storage service (Christian Galeone)</li><li>Simulatore dei driver (Leonardo Focardi)</li></ul>                                                                                                                                                                                                                                        |  |
| [Sprint2](https://htmlpreview.github.io/?https://github.com/LEOB3TA/ColdStorageService-Project/blob/main/Sprint2/userDocs/sprint2.html)                 | Implementazione uso  Sonar e Led                                                                                                                                                                                                                                                                                                                                                                                                                                            |  |

# How to install

## Dependencies
- **Java 11**
- **Docker**
- **Docker compose**

## Instructions
### Mode 1:
- Download the zip inside release
- Use the script startPrototype-***.sh/bat to execute the prototype
### Mode 2:
You have to install **Gradle** on your computer

- Download the source code from release
- Unzip the source code
- Start the basicrobot
```bash
cd ColdStorageService-Project/Sprint1/unibo.basicrobot23
docker-compose -f webbasicrobot23.yaml up
```
- start all the other components of the prototype
```bash
cd ColdStorageService-Project/Sprint1/unibo.prototipo0
./gradlew run
```

