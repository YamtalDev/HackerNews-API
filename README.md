# Hacker News RESTFul API

## Project Overview

The Hacker News project aim to create a lightweight system similar to Hacker 
News web site that allows users to post text-based news, up vote/down vote posts, and view a 
list of top posts. RESTful API that support create, update, read, delete, up vote,
and down vote operations.

 ## CRUD Operations

Examples of CRUD (Create, Read, Update, Delete, Patch) operations for Hacker News API.

## Create a New Post

### Request

``` http
POST http://localhost:8080/api/news
Content-Type: application/json

{
    "postedBy": "User",
    "post": "This is a new post",
    "link": "https://some_website.com"
}

```


## Read a Post by ID
### Request

``` http
GET http://localhost:8080/api/news/1

HTTP/1.1 200 OK
Content-Type: application/json

{
    "postId": 1,
    "postedBy": "User",
    "post": "This is a new post",
    "link": "https://some_website.com",
    "timeElapsed": "just now",
    "votes": 0
}

```

## Update a Post by ID
### Request

``` http

PUT http://localhost:8080/api/news/1
Content-Type: application/json

{
    "postedBy": "Updated_user",
    "post": "Updated post content",
    "link": "https://updated_website.com"
}

```

## Delete a Post by ID
### Request

``` http

DELETE http://localhost:8080/api/news/1

HTTP/1.1 200 OK
Content-Type: text/plain

"Post deleted"

```

## Change a Post by ID (Partial Update)
### Request

``` http

PATCH http://localhost:8080/api/news/1
Content-Type: application/json

{
    "post": "Changed post content",
    "link": "https://changed-example.com"
}

```

## Get All Posts
### Request

``` http

GET http://localhost:8080/api/news

HTTP/1.1 200 OK
Content-Type: application/json

{
    "content": [
        {
            "postId": 1,
            "postedBy": "User",
            "post": "This is a new post",
            "link": "https://some_website.com",
            "timeElapsed": "just now",
            "votes": 0
        },
        {
            "More posts..."
        }
    ],
    "pageable": {
        "pageSize": 30,
        "pageNumber": 0
    }
}

```

## Get Top Posts
### Request
* The response will be a page with the top posts based on their score.

``` http

GET http://localhost:8080/api/news/top-posts

```

## downvote/upvote a Post by ID
### Request

``` http

PATCH http://localhost:8080/api/news/1/downvote
PATCH http://localhost:8080/api/news/1/upvote

# Response:

HTTP/1.1 200 OK
Content-Type: application/json

{
    "postId": 1,
    "postedBy": "User",
    "post": "This is a new post",
    "link": "https://some_website.com",
    "timeElapsed": "just now",
    "votes": 1                    # upvoted result
}

```

## Features

## Usage

To utilize this spring boot rest api application project, follow these steps:

### Prerequisites

Before you begin, ensure you have the following prerequisites installed on your system:

- **Java (JDK):** If you don't have Java installed, you can download and install it from the official website:
  - [Download Java for Windows](https://www.oracle.com/java/technologies/javase-downloads.html) (Windows)
  - [Download Java for macOS](https://www.oracle.com/java/technologies/javase-downloads.html) (macOS)
  - [Download Java for Linux](https://openjdk.java.net/install/) (Linux)

- **Maven:** If you don't have Maven installed, you can download and install it from the official website:
  - [Download Maven](https://maven.apache.org/download.cgi)

- **MySQL Database:** The project uses a MySQL database for data storage. Make sure you have MySQL installed and running on your system. You can download MySQL from the official website:
  - [Download MySQL](https://dev.mysql.com/downloads/mysql/)

### Installation

1. **Clone or Download the Repository:**
   You can clone this Git repository or download it as a ZIP file to your local machine.

``` shell
git clone https://github.com/YamtalDev/HackerNews-API.git

```





https://www.youtube.com/watch?v=PAQvxqocb6A&pp=ygUoc3ByaW5nIHJlc3QgYXBpIHVzaW5nIERUTyBmb3IgZWZmaWNpZW5jeQ%3D%3D

https://www.youtube.com/watch?v=ac12zNR-OsE&list=PLVuqGBBX_tP3KmownF68ifFmgPQt-ujBg

https://www.youtube.com/watch?v=S2s28PCg4M4
https://www.javaguides.net/2018/06/restful-api-design-best-practices.html

If i had more time i would implement the comments count and posting of comments.
Restrictions for 1 up vote and down vote at a time per user in a session.
use a schema.sql to provide data base migration in the app.
Indexing: Ensure that your database tables have appropriate indexes, especially on columns used in queries frequently. Indexes can significantly improve query performance.
think of how to catch the lower level exception of the data base instead of an if in the up/down vote.
think of a way to update the cache of the top most posts when the data is changing.
Implement a different algorithm to get the top most posts by adding post_id to of the next ranked posts and then getting those posts.

Database Optimization: As your application grows, database performance becomes critical. Make sure you're using appropriate indexes, caching mechanisms, and database connection pooling. You can also consider using Spring's support for database transactions.

DTOs and Model Mapping: Use Data Transfer Objects (DTOs) to separate your entity models from what's exposed in your API. This gives you better control over what data is sent to the client and prevents over-fetching.

Async Processing: If some operations are time-consuming, consider using asynchronous processing with Spring's @Async and CompletableFuture to improve the responsiveness of your API.

Dependency Injection: Ensure you're following best practices for dependency injection using Spring's @Autowired or constructor injection. Avoid using new to create instances of services or repositories.

Dockerization: Consider containerizing your application using Docker. This can make it easier to deploy and manage in different environments.

security Scanning: Regularly scan your application for security vulnerabilities using tools like OWASP ZAP or SonarQube.