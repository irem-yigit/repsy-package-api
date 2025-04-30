# Repsy Package API
<a name="readme-top"></a>
This project is a Spring Boot based REST API implementation that provides a minimal package repository system for the Repsy programming language. The project supports uploading and downloading packages required by the Repsy package manager.

<details>
  <summary>Table of Contents</summary>
  <ol>
    <li><a href="#about-the-project">About the Project</a></li>
    <li><a href="#technologies">Technologies</a></li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#requirements">Requirements</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#api-test">API Test</a></li>
    <li><a href="#api-endpoints">API Endpoints</a></li>
  </ol>
</details>

##  About the Project 

This project is a backend API implementation that supports file upload and download operations.
Files can be stored with two different strategies:

* On File System

* On Object Storage using MinIO

The metadata of each uploaded file is saved in the PostgreSQL database.


