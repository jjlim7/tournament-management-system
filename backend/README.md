# Backend

## Environment Setup
Create `.env` file based on the provided `.env.sample` file
```sh
POSTGRES_HOST=
POSTGRES_PORT=
POSTGRES_USER=
POSTGRES_PASSWORD=
POSTGRES_DB=
POSTGRES_URL=jdbc:postgresql://${POSTGRES_HOST}:${POSTGRES_PORT}/${POSTGRES_DB}

SPRING_PROFILES_ACTIVE=docker
```

## Running on Docker
Ensure Docker Desktop is installed. [<u>Download here</u>](https://www.docker.com/products/docker-desktop/)

1. Build and Up all docker containers
    ```sh
    docker-compose up --build -d
    ```
2. Teardown all containers
    ```sh
   docker-compose down -v
    ```
3. Check logs of specific docker container (_or you can check via Docker Desktop_)
    ```sh
   # E.g. docker-compose logs -f backend-matchmaking-service-1
   docker-compose logs -f <container-name>
    ```
