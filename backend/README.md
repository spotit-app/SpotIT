# :floppy_disk: SpotIT Backend

<p align="center">
    <img src="https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white" alt="gradle">
    <img src="https://img.shields.io/badge/JUnit5-25A162.svg?style=for-the-badge&logo=JUnit5&logoColor=white" alt="junit5">
    <img src="https://img.shields.io/badge/Docker-2496ED.svg?style=for-the-badge&logo=Docker&logoColor=white" alt="docker">
</p>
<p align="center">
    <img src="https://img.shields.io/badge/Spring%20Boot-6DB33F.svg?style=for-the-badge&logo=Spring-Boot&logoColor=white" alt="spring-boot">
    <img src="https://img.shields.io/badge/Spring%20Security-6DB33F.svg?style=for-the-badge&logo=Spring-Security&logoColor=white" alt="spring-security">
    <img src="https://img.shields.io/badge/Auth0-EB5424.svg?style=for-the-badge&logo=Auth0&logoColor=white" alt="auth0">
</p>
<p align="center">
    <img src="https://img.shields.io/badge/PostgreSQL-4169E1.svg?style=for-the-badge&logo=PostgreSQL&logoColor=white" alt="mongodb">
    <img src="https://img.shields.io/badge/Redis-red?style=for-the-badge&logo=redis&logoColor=white" alt="redis">
</p>

## :wrench: Running backend locally

For local development and testing, backend uses `Docker` (and `Docker Compose`).
Once you have them installed, in order to run the app you have to execute the following command (inside the `backend` directory) to start the databases:

```shell
$ docker compose up
```

And then this command to start the backend:

```shell
$ gradle bootRun
```

By default, the app starts on the port $8080$.
In order to run all tests, execute this command:

```shell
$ gradle test
```
