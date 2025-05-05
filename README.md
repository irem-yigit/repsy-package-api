# Repsy Package API
<a name="readme-top"></a>
This project is a Spring Boot based REST API implementation that provides a minimal package repository system for the Repsy programming language. The project supports uploading and downloading packages required by the Repsy package manager.

<details>
  <summary>Table of Contents</summary>
  <ol>
    <li><a href="#about-the-project">About the Project</a></li>
    <li><a href="#features">Features</a></li>
    <li><a href="#technologies">Technologies</a></li>
   <li><a href="#project-structure">Project Structure</a></li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#requirements">Requirements</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#api-test">API Test</a></li>
    <li><a href="#api-endpoints">API Endpoints</a></li>
    <li><a href="#important-notes">Important Notes</a></li>
  </ol>
</details>

##  About the Project 

This project is a backend API implementation that supports file upload and download operations.
Files can be stored with two different strategies:

* On File System

* On Object Storage using MinIO

The metadata of each uploaded file is saved in the PostgreSQL database.

## Features

* **Deployment REST API Endpoints:**
  * URL: `/{packageName}/{version}`: Package deployment endpoint.
  * Enables the upload of package.rep (binary zip file) and meta.json (package metadata) files.
  * The uploaded meta.json file is verified and saved to the PostgreSQL database.
  * Files are saved to MinIO or file system depending on the selected storage strategy.
* **Download REST Endpoint:**
  * URL: `/{packageName}/{version}/{fileName}`: Package download endpoint.
  * Enables the download of uploaded files.
* **Storage Layer:**
  * Packages and metadata can be stored in both the file system and in the object store using MinIO.
  * The storage method can be configured via `application.properties` or an environment variable.
* **Database:**
  * PostgreSQL database is used to store metadata of deployed packages.
* **Docker Support:**
  * The application is packaged as a Docker container and configured to work with PostgreSQL and MinIO services using docker-compose.
* **Technical Details:**
  * Developed using Java LTS version (Java 17) and Spring Boot.
  * Packaged as a Docker container.


## Technologies 

- **Java 17:** Main programming language used for backend logic.
- **Spring Boot v3.4.4:** Used for rapid development of RESTful services.
- **Spring Data JPA:** A Spring module that simplifies database operations.
- **PostgreSQL:** Used as a database management system.
- **Maven (For PostgreSQL ve MinIO):** Project dependency management and compilation operations.
- **Docker & Docker Compose:** Used to run the application and MySQL database as a container.
- **Swagger / Postman:** Testable API

## Project Structure

    📦 repsy-assignment/
    ├── 📂 file-system-storage # storage-file-system module
    ├── 📂 main-api-app/ # Main Spring Boot Application
    ├────├── 📂 storage/
    ├────────├── 📂 files # Storage files      
    ├── 📂 object-storage # storage-object-storage module
    ├── 📂 storage-common # Common interface
    ├── 📄 docker-compose.yml # Docker settings
    └── 📄 README.md # Documentation

* Main Spring Boot Application → **main-api-app/** 

   - REST API endpoints

   - Business logic and services

* Storage Strategy Libraries

   - storage-file-system module → **file-system-storage/** 

   - storage-object-storage module → **object-storage/** 

   - These libraries are deployed to the Repsy private Maven repository and are used by the main application.

* Dockerfile and docker-compose.yml

   - Enables all services (application, PostgreSQL, MinIO) to stand up easily.

## Getting Started

### Requirements

To run the project, you must have the following software installed on your system:

- Java 17 or later
- Maven 3.8 or later
- Docker and Docker Compose (For PostgreSQL ve MinIO)
- IntelliJ IDEA or another IDE
- PostgreSQL or another compatible SQL database
- Swagger or Postman

### Installation

1. **Clone the project:**

   ```bash
   git clone https://github.com/irem-yigit/repsy-package-api.git
   ```

2. **Configure the database:**

    - Create a database named `repsy` (this step is not necessary if running with Docker).
    - Update the `application.properties` file in the `src/main/resources` folder according to your database information.

3. **Run the following services with Docker:**

   → PostgreSQL
    ```bash
    docker run --name repsy-postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=repsy -p 5432:5432 -d postgres
   ```
   → MinIO
   ```bash
    docker run -p 9000:9000 -p 9001:9001 --name repsy-minio \
      ```
   - -e "MINIO_ROOT_USER=minio" \
   - -e "MINIO_ROOT_PASSWORD=minio123" \
   - quay.io/minio/minio server /data --console-address ":9001"

4. **Build the project with Maven:**

   ```bash
   mvn clean install
   ```

5. **Run the Spring Boot application:**

   ```bash
   mvn spring-boot:run
   ```

   Once the application is launched, you can start using the APIs.

6. **Running with Docker:**

   To run the application with Docker, you can use the `docker-compose.yml` file located in the root directory of the project:

   ```bash
   docker-compose up --build
   ```
<p align="right"><a href="#readme-top">Back to the Top ↑ </a></p>

## API Test

**Swagger URL:**

   ```bash
   http://localhost:8080/swagger-ui/index.html#/v3/api-docs
   ```
## API Endpoints

### The Repsy Package API offers the following endpoints:

#### Download API

- `GET /download/{filename}`    : Downloads file by file name 
- `GET /download/{id}`    : Downloads file by ID

#### Upload API

- `POST /upload`           : 
- `GET /upload/all`        :

#### Deploy API 

- `POST /{packageName}/{version}` : 

## Important Notes

* If PostgreSQL connection is not available, check the Docker PostgreSQL container.

* You can access the MinIO interface via http://localhost:9001 with minio/minio123.

* If you are running it for the first time, you should create a bucket named repsy on MinIO.
