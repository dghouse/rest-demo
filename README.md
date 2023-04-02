# Basic RESTFul Application

1. Spring Boot
2. SpringDoc
3. JPA / Hibernate
4. JUnit
5. ModelMapper
6. Flyway

This application is intended to demonstrate the integration of various server side technologies used to deliver a robust
RESTFul service.

The SpringDoc library is included and the documentation page can be viewed 
[here](http://localhost:8080/swagger-ui/index.html) once you have the application running.

While this application includes an h2 database, this is only as a stand-in for an enterprise scale database
implementation.

The service classes, service tests, and controller tests have been largely abstracted. This is to reduce the amount of
boilerplate code and make it easier to achieve good test coverage. The included tests are not comprehensive. They are
meant to be a guide of how it could be done.

In the instance of the `ProvinceService.getProvinceByName` method I've tried to demonstrate "special case" that requires
the class to extend the abstracted class and that it's ok to do that. 

## How To Run

This application requires Java 17 as a minimum. There is a Gradle wrapper 

## Your Own Application

If you are about to start a new RESTFul API and you like what you see, you may find it useful to clone this application 
and remove or refactor what you don't need.

You could also update you local application by adding this repository as a remote and then pull in updates as needed.


## Potential Future Inclusions

It would be useful to include the following things:

1. Bilingual SpringDoc
2. Searching (Criteria API)
3. OAuth authentication
4. File storage (local or cloud)
5. Database versioning (Envers, JaVers)
6. Logging
