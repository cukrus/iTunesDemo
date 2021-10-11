# iTunesDemo
Demo of iTunes API for top 5 albums of favorite artist app

# Assignment:
We want to add functionality to our mobile apps called “Top 5 albums of my favourite artist”.
A user can select one favourite artist and see top 5 albums of that artist. The app will have two pages:
* Search and save a favourite artist
* View top 5 albums of a selected favourite artist
We ask you to create a web service that the mobile apps will consume and serve data for the two pages.

# Technical constraints:
* iTunes allows calling them only 100 times per hour, whereas we have 30 million users / month. Though right now we only expect ~100 
users using this service.
* How and when top5 albums are refreshed is up to you to decide
* Endpoints should return Content-Type application/json with jackson models
* For the simplicity of the task, user id can come as a query parameter or a header in a non-secure way
* You can use in-memory storage for persistent data
* Must-use stack: Java 11 or Kotlin
* Recommended stack: Spring Boot, JUnit

# iTunes API in a nutshell:
* Searching for artists by keyword: POST https://itunes.apple.com/search?entity=allArtist&term=abba 
* Getting top5 albums by AMG artist id: POST https://itunes.apple.com/lookup?amgArtistId=3492&entity=album&limit=5 
* More: https://affiliate.itunes.apple.com/resources/documentation/itunes-store-web-service-search-api

# Build and run instructions:
* build and run - ./mvnw spring-boot:run