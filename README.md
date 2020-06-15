# Project: Nasa image viewer

Using the API described here (https://api.nasa.gov/api.html ) build a project in GitHub that calls the Mars Rover API 
and selects a picture on a given day. We want your application to download and store each image locally.

### Acceptance Criteria
* Use list of dates below to pull the images were captured on that day by reading in a text file:
    * 02/27/17
    * June 2, 2018
    * Jul-13-2016
    * April 31, 2018
* Language needs to be Java.
* We should be able to run and build (if applicable) locally after you submit it
* Include relevant documentation (.MD, etc.) in the repo
* Bonus: Unit Tests, Static Analysis, Performance tests or any other things you feel are important for Deﬁnition of Done
* Bonus: Have the app display the image in a web browser
* Bonus: Have it run in a Docker or K8s (Preferable)

## Scope of Solution
This is a Spring Boot application written in Java 8 using RetroFit. It exposes a set of REST APIs that allow a user to retrieve images captured by any of the 3 current Mars rovers, on any date that they actually took photos, from the NASA open API.

Photos can be requested with just a date. If any of the rovers took photos on that day, they will be retrieved.
A text file is provided that you can add dates to for bulk processing.

When a valid image is requested, it will be downloaded if it does not already exist locally in the cache. Caching of images is done using the full file path.
* 301 redirects are handled when found.
* Images are stored locally in /tmp in the following structure:

           /tmp/{date:YYYY-MM-DD}/{rover name}/{image_name.jpg}
* NASA API key is configurable in application.properties

##### Known limitations and Areas for Improvement
* Dates are parsed and formatted according to ISO-8601 calendar system. An invalid date formatted properly (i.e. April 31,2018) will be converted to the nearest valid date as specified by ISO-8601 calendar system, in this case April 30, 2018. Dates can be handled only in the format of the examples given in the acceptance criteria. An invalid date formatted incorrectly or with illegal naming (i.e. Schmay 14, 1999) will throw an exception and return today's date.
* Downloading of images occurs one at a time. This is slow.
* There is currently no image browser served back to the user.
* Automated testing is incomplete.
* The imageDates.txt file location and name could be made user configurable
* The download location could be made user configurable.
* Date checking with respect to valid dates with photos.

### Prerequisites
* Java SDK 8
* Docker (Only if you wish to run as a Docker container)

## Installing
1. Download or clone this repository.
2. Open file src/resources/application.properties and add your NASA API key to the property nasaAPI.key

    `nasaAPI.key=PUTYOURKEYHERE123ABC`
3. (Optional) Open file src/resources/imageDates.txt and add dates if you would like.

### Build as a jar
To build the application as a stand-alone jar file, run the following command:

```bash
./gradlew build
```

### Build as a Docker container
To build as a docker container, run the following commands:

```./gradlew build```

```docker build . -t abreinig-nasa-app -f Dockerfile```


## Running as a stand-alone jar
After running `.gradlew build`, a jar file named `nasa-0.0.1-SNAPSHOT.jar` will be located in the `build/libs` directory.
To run the jar from the root of the project, use the following command:

```bash
java -jar build/libs/nasa-0.0.1-SNAPSHOT.jar
```

Spring Boot will start up a server on port **8080**.

The APIs will be accessible at `http://localhost:8080`

##Running as a Docker container:
After building the Docker container you can launch it with the following command:

```docker run -p 8080:8080 abreinig-nasa-app```

Docker will launch the container and expose port **8080**.

The APIs will be accessible at `http://localhost:8080`

## Exposed REST Endpoints

- Get all photos from all rovers from dates listed in text file imageDates.txt:
    - http://localhost:8080/photos
    
- Get all photos from a specific rover on a specific date:
    - http://localhost:8080/photos/{rover}/{YYYY-MM-DD}
        - example: http://localhost:8080/photos/curiosity/2012-08-07 
        
