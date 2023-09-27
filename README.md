# sykmelder-statistikk-kafka 


## Technologies used
* Kotlin
* Ktor
* Gradle
* Junit
* Docker

#### Requirements

* JDK 17


## Getting started
### Building the application
#### Compile and package application
To build locally and run the integration tests you can simply run
``` shell
./gradlew shadowJar
```
or on windows
``` shell
gradlew.bat shadowJar
```

### Upgrading the gradle wrapper
Find the newest version of gradle here: https://gradle.org/releases/ Then run this command:

``` shell
./gradlew wrapper --gradle-version $gradleVersjon
```

### Contact

This project is maintained by [navikt/teamsykmelding](CODEOWNERS)

Questions and/or feature requests?
Please create an [issue](https://github.com/navikt/sykmelder-statistikk-kafka/issues)

If you work in [@navikt](https://github.com/navikt) you can reach us at the Slack
channel [#team-sykmelding](https://nav-it.slack.com/archives/CMA3XV997)
