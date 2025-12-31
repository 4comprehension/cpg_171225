# Movie Descriptions Service

A lightweight microservice for managing movie descriptions that delegates to the rental-store service.

## Overview

This service provides REST endpoints for managing movie descriptions. Descriptions are stored in the rental-store database and accessed through the rental-store API.

## Endpoints

### Add Description
```
POST /movie-descriptions
Content-Type: application/json

{
  "movieId": 42,
  "description": "A great movie"
}
```

### Update Description
```
PUT /movie-descriptions/{movieId}
Content-Type: application/json

{
  "movieId": 42,
  "description": "An updated description"
}
```

### Get Description
```
GET /movie-descriptions/{movieId}
```

Response:
```json
{
  "movieId": 42,
  "description": "A great movie"
}
```

## Configuration

Set the following property to point to your rental-store instance:

```properties
rentalstore.url=http://localhost:8080
```

## Building

```bash
mvn clean package
```

## Running

```bash
java -jar movie-descriptions-0.0.1-SNAPSHOT.jar
```

The service runs on port 8081 by default.
