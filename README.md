# Testdatageneratoren kotlin-compiler-server

## Om Testdatageneratoren - overordnet beskrivelse av løsningen

*Testdatageneratoren* er en webapplikasjon som kan brukes for å generere syntetiske testdata til bruk i utvikling og
testing. Applikasjonen gir brukeren mulighet til å skrive en spesifikasjon av de testdataene som ønskes generert.
Spesifikasjonen blir validert mot forretningsregler og gyldig input, og deretter genereres et xml-dokument med data som
samsvarer med den angitte spesifikasjonen, samt eventuelle felter som har blitt automatisk fylt ut
(f.eks. fødselsnummer, organisasjonsnummer, beløp) i den grad brukeren ikke angir disse feltene selv i spesifikasjonen.

Testdataene som genereres er syntetiske, og er basert på test-personer og -enheter som hentes fra *Tenor*.

Denne aktuelle løsningen som er publisert på Github er en forenklet og nedkortet versjon av den Testdatageneratoren som
brukes internt i Skatteetaten, og denne løsningen er derfor ment å brukes kun som et utgangspunkt og som eksempelkode
for andre som ønsker å lage og videreutvikle en tilsvarende løsning innenfor sitt domene. Denne forenklede versjonen
gir mulighet for å generere dokumenter kun av typen "SaldoRente", men koden kan utvides til å generere en rekke andre
dokumenttyper.

Testdatageneratoren består av tre komponenter:

- [tdg-frontend](https://github.com/Skatteetaten/tdg-frontend)
- [tdg-kotlin-compiler-server](https://github.com/Skatteetaten/tdg-kotlin-compiler-server)
- [tdg-backend](https://github.com/Skatteetaten/tdg-backend)

### Funksjonalitet og flyt mellom komponenter

- **tdg-frontend** er brukergrensesnittet som lar brukeren definere en spesifikasjon for testdata som skal genereres.
  Spesifikasjonen skrives som et internt utviklet DSL (kalt "TestData Spesifikasjonsspråk"/TDSS) basert på Kotlin.
  TDSS'en er definert i *tdg-backend*. Frontenden gjør kall til tdg-kotlin-compiler-server for løpende validering av
  spesifikasjonen, og fra frontenden sendes deretter spesifikasjonen til tdg-backend.
- **tdg-backend** består av to moduler.
  - **_tdss_** definerer domenespråket (TDSS) og bestemmer hva som er lov å spesifisere i frontenden.
  - **_testdatagenerator_** tar imot spesifikasjonen, genererer testdata i xml-format og returnerer denne til
    frontenden.
- **tdg-kotlin-compiler-server** er en tjeneste som gir highlighting, autocomplete og feilmeldinger for spesifikasjonen
  som skrives i frontenden. Valideringen gjøres mot tdss-modulen i *tdg-backend*. *tdg-kotlin-compiler-server* er en forket
  versjon av [Jetbrains sin Kotlin compiler server](https://github.com/JetBrains/kotlin-compiler-server), som deretter
  har blitt tilpasset Skatteetatens domene.


*testdatagenerator* og *tdg-kotlin-compiler-server* bruker *tdss* som bibliotek. Innsendingene er tekst i TDSS streng-format,
som kan komme fra frontend eller HTTP-klienter. *korrelasjonsId* brukes for å hente assosierte dokumenter via HTTP.

![Testdata flyt](docs/flytdiagram.png)

## Hvordan TDSS er lagt til i prosjektet

Legg til i [dependencies/build.gradle.kts](dependencies/build.gradle.kts):

    kotlinDependency("no.skatteetaten.rst:tdss:${tdssVersjon}")

Legg til i [build.gradle.kts](build.gradle.kts):

    implementation("no.skatteetaten.rst:tdss:${tdssVersjon}")

Legg til egen logikk i [CompletionProvider.kt](src/main/kotlin/com/compiler/server/compiler/components/CompletionProvider.kt)

### For kjøring lokalt

Hvis man ikke har et remote repository som distribuerer *tdss*,
så må *tdg-backend* bygges først, slik at det lages et lokalt snapshot av *tdss*.

I [gradle.properties](gradle.properties) må man sette `tdssVersjon=1.0-SNAPSHOT`.


## Testing med IntelliJ HTTP Syntaks

[**kcs-dev.http**](http/kcs-dev.http) kan brukes til å teste ut *kotlin-compiler-server* ved hjelp av *IntelliJ* sin HTTP-syntaks.


# ____________________________________________
# README fra Jetbrains sin [kotlin-compiler-server](https://github.com/JetBrains/kotlin-compiler-server)
# ____________________________________________

# Kotlin compiler server

[![official JetBrains project](https://jb.gg/badges/official-plastic.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub)
![Build status](https://buildserver.labs.intellij.net/app/rest/builds/buildType:id:Kotlin_KotlinSites_Deployments_PlayKotlinlangOrg_Backend_BuildMaster/statusIcon.svg)
![Java CI](https://github.com/JetBrains/kotlin-compiler-server/workflows/Java%20CI/badge.svg)
![TC status](https://img.shields.io/teamcity/build/s/Kotlin_KotlinPlayground_KotlinCompilerServer_Build?label=TeamCity%20build)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.7.20-orange.svg) ](https://kotlinlang.org/)
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)

A REST server for compiling and executing Kotlin code.
The server provides the API for [Kotlin Playground](https://github.com/JetBrains/kotlin-playground) library.

## How to start :checkered_flag:

### Simple Spring Boot application

Download Kotlin dependencies and build an executor before starting the server:

```shell script
$ ./gradlew build -x test 
```

Start the Spring Boot project. The main class: `com.compiler.server.CompilerApplication`

### With Docker

To build the app inside a Docker container, run the following command from the project directory:
```shell
$ ./docker-image-build.sh
```

### From Amazon lambda

Based on [aws-serverless-container](https://github.com/awslabs/aws-serverless-java-container).

```shell script
$ ./gradlew buildLambda
```

Getting `.zip` file from `build/distributions`.

Lambda handler: `com.compiler.server.lambdas.StreamLambdaHandler::handleRequest`.

Publish your Lambda function: you can follow the instructions
in [AWS Lambda's documentation](https://docs.aws.amazon.com/lambda/latest/dg/lambda-java-how-to-create-deployment-package.html)
on how to package your function for deployment.

### From Kotless

Add [Kotless](https://github.com/JetBrains/kotless) and
remove [aws-serverless-container](https://github.com/awslabs/aws-serverless-java-container) =)

## API Documentation :page_with_curl:

Swagger url: http://localhost:8080/api-docs/swagger-ui/index.html

## How to add your dependencies to kotlin compiler :books:

Just put whatever you need as dependencies
to [build.gradle.kts](https://github.com/JetBrains/kotlin-compiler-server/blob/master/build.gradle.kts) via a
task called `kotlinDependency`:

```
 kotlinDependency "your dependency"
```

NOTE: If the library you're adding uses reflection, accesses the file system, or performs any other type of
security-sensitive operations, don't forget to
configure
the [executor.policy](https://github.com/JetBrains/kotlin-compiler-server/blob/master/executor.policy)
. [Click here](https://docs.oracle.com/javase/7/docs/technotes/guides/security/PolicyFiles.html) for more information
about *Java Security Policy*.

**How to set Java Security Policy in `executors.policy`**

If you want to configure a custom dependency, use the marker `@LIB_DIR@`:

```
grant codeBase "file:%%LIB_DIR%%/junit-4.12.jar"{
  permission java.lang.reflect.ReflectPermission "suppressAccessChecks";
  permission java.lang.RuntimePermission "setIO";
  permission java.io.FilePermission "<<ALL FILES>>", "read";
  permission java.lang.RuntimePermission "accessDeclaredMembers";
};
```

## CORS configuration

Set the environment variables

| ENV                               | Default value |
|-----------------------------------|---------------|
| ACCESS_CONTROL_ALLOW_ORIGIN_VALUE | *             |
| ACCESS_CONTROL_ALLOW_HEADER_VALUE | *             |

## Configure logging

We use `prod` spring active profile to stream logs as JSON format.
You can set the spring profile by supplying `-Dspring.profiles.active=prod` or set env variable `SPRING_PROFILES_ACTIVE` to `prod` value.

### Unsuccessful execution logs

In case of an unsuccessful execution in the standard output will be the event with INFO level:

```json
{
  "date_time": "31/Aug/2021:11:49:45 +03:00",
  "@version": "1",
  "message": "Code execution is complete.",
  "logger_name": "com.compiler.server.service.KotlinProjectExecutor",
  "thread_name": "http-nio-8080-exec-1",
  "level": "INFO",
  "level_value": 20000,
  "hasErrors": true,
  "confType": "JAVA",
  "kotlinVersion": "$kotlinVersion"
}
```

## Kotlin release guide :rocket:

1) Update the kotlin version
   in [libs.versions.toml](https://github.com/JetBrains/kotlin-compiler-server/blob/master/gradle/libs.versions.toml)
2) Make sure everything is going well via the task:

```shell script
$ ./gradlew build
```

3) Save branch with the name of the kotlin version. Pattern: `/^[0-9.]+$/`  (optional)
4) Bump version on GitHub [releases](https://github.com/JetBrains/kotlin-compiler-server/releases) (optional)

### Gradle Build Scans

[Gradle Build Scans](https://scans.gradle.com/) can provide insights into an Kotlin Compiler Server Build.
JetBrains runs a [Gradle Develocity server](https://ge.jetbrains.com/scans?search.rootProjectNames=kotlinx-atomicfu).
that can be used to automatically upload reports.

To automatically opt in add the following to `$GRADLE_USER_HOME/gradle.properties`.

```properties
org.jetbrains.kotlin.compiler.server.build.scan.enabled=true
# optionally provide a username that will be attached to each report
org.jetbrains..kotlin.compiler.server.build.scan.username=John Wick
```

Also you need to create an access key:
```bash
./gradlew provisionDevelocityAccessKey
```

A Build Scan may contain identifiable information. See the Terms of Use https://gradle.com/legal/terms-of-use/.
