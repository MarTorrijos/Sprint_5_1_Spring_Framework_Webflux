# BlackJack - Reactive API with Spring Boot

This project is a practical implementation of a Blackjack game API built with Java and Spring Boot. The API connects to and manages data across both relational (MySQL) and non-relational (MongoDB) databases.

## Objectives

- Develop a reactive API in Java using Spring Boot

- Integrate both MySQL and MongoDB databases

- Apply unit testing and API documentation best practices


## Technologies used

- Java 21

- Spring boot 3.2.5

- MongoDB and MySQL

- JUnit and Mockito for tests

- Postman (for API testing)

- Gradle

***

## API Endpoints Overview

| Controller       | Method | Endpoint             | Description                       |
|------------------|--------|----------------------|---------------------------------|
| **Player**       | GET    | `/player/{id}`       | Get a player by ID               |
|                  | PUT    | `/player/{id}`       | Update a player by ID            |
|                  | DELETE | `/player/{id}`       | Delete a player by ID            |
|                  | POST   | `/player`            | Create a new player              |
|                  | GET    | `/player/ranking`    | Show ranking of games won       |
| **Turn**         | PUT    | `/game/{id}/stand`   | Player chooses to stand          |
|                  | POST   | `/game/{id}/hit`     | Deal a card to a player (hit)   |
| **Game**         | POST   | `/game/new`          | Create a new game                |
|                  | GET    | `/game`              | Get all games                   |
|                  | GET    | `/game/{id}`         | Get a game by ID                 |
|                  | DELETE | `/game/{id}`         | Delete a game by ID              |

For detailed request/response schemas and interactive testing, please visit the [Swagger UI](http://localhost:8080/swagger-ui.html).



## How to Run the Project

- Clone the repository or download the ZIP file. Then open the project in your preferred IDE and run the application.

    `git clone <repository-url>`


- Make sure MongoDB and MySQL are running, and configure the connection properties as needed in src/main/resources/application.yml