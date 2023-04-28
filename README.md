# NOTIFICATIONS PROJECT

## REQUIREMENTS
- JDK 15+
- Kotlin 1.8.0+
- NodeJS 18+
- Docker / Docker Compose


## BACK END INSTALLATION
Make sure that the docker deamon is running and go to `./notifications-sever/docker` and run the next command:

```
# docker-compose -f mysql-compose.yml up -d
```

This will create a image of a MySQL database and populate it with some fake data.

Go back to `./notifications-sever` and run:

```
#  gradle clean build bootRun
```

This will start the server on `http://localhost:8080`

## FRONT END INSTALLATION

Go to `./notifications-react` and run:
```
#  yarn
```
to install npm dependencies needed.

To run the application run the next command:
```
#  yarn dev
```

This will start the front app in `http://localhost:5174/`
