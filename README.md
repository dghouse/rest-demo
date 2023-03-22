# Tech List

1. Spring Boot
2. SpringDoc
3. JPA / Hibernate
4. JUnit
5. ModelMapper
6. Flyway
7. Microsoft Excel Spreadsheet process with Apache POI

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



## Potential Inclusions

It would be useful to include the following things:

1. Bilingual SpringDoc
2. Searching (Criteria API)
3. OAuth authentication
4. File storage (local or cloud)
5. Database versioning (Envers, JaVers)
6. Logging
