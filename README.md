# MiniHackerNews-RESTFul-API
RESTful API for managing simple text news posts, supporting create, update, read, delete, upvote, and downvote operations. 

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


Database Optimization: As your application grows, database performance becomes critical. Make sure you're using appropriate indexes, caching mechanisms, and database connection pooling. You can also consider using Spring's support for database transactions.

DTOs and Model Mapping: Use Data Transfer Objects (DTOs) to separate your entity models from what's exposed in your API. This gives you better control over what data is sent to the client and prevents over-fetching.

Async Processing: If some operations are time-consuming, consider using asynchronous processing with Spring's @Async and CompletableFuture to improve the responsiveness of your API.

Dependency Injection: Ensure you're following best practices for dependency injection using Spring's @Autowired or constructor injection. Avoid using new to create instances of services or repositories.

Dockerization: Consider containerizing your application using Docker. This can make it easier to deploy and manage in different environments.

security Scanning: Regularly scan your application for security vulnerabilities using tools like OWASP ZAP or SonarQube.