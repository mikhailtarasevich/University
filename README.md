# University - pet project

Hello, I would like to introduce you to my pet project—a CRUD application
aimed at digitalizing the university studying process.

The application is structured into three layers. The first layer is the DAO layer, responsible for retrieving
data from the database.

The second layer is the Service layer, which handles the internal logic of the application. It manages
backend validation, transaction management, and interacts with the DAO layer to retrieve data.

The third layer is the UI layer, responsible for receiving HTTP requests from users and mapping them to the
relevant views, typically HTML pages. The UI layer adheres to the principles of the REST architecture.

## Run the application

To run the application (you need to have Docker installed on your computer),
please [download the docker-compose](https://drive.google.com/file/d/1YlVt0-E5jza_Ner0DUBRFg5-uTWhAGgF/view?usp=share_link)
file. Open a terminal in the folder containing the file and
execute the command:

    docker-compose up

Open the link [http://localhost:8080](http://localhost:8888) in a web browser.

# Libraries and Frameworks

During the development of this project, several libraries, frameworks, and technologies were studied and
utilized. Here are some of the key ones:

- Spring Core
- Spring MVC
- Spring JDBC
- Spring Security
- Hibernate
- Spring Data JPA
- Spring Boot
- Postgres
- Thymeleaf
- Junit jupiter
- Mockito
- Mapstruct
- Lombok
- Log4j
- Docker