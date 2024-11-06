# Googol

### Description
This is simplified version of a webcrawler and web browser

### Dependencies
- Java: latest
- Gradle: latest

### Installation
1. Clone the repository
2. Run the following command in the root directory of the project:
```bash
#windows
.\gradlew.bat gateway:run -q

#linux
./gradlew gateway:run -q
```
3. After the gateway is running clients, downloaders and barrels can be started

```bash
#windows
.\gradlew.bat barrel:run -q
.\gradlew.bat dowloader:run -q
.\gradlew.bat client:run -q
.\gradlew.bat webclient:run -q

#linux
./gradlew barrel:run -q
./gradlew downloader:run -q
./gradlew client:run -q
./gradlew webclient:run -q
```

Downloaders and clients can be started and stopped whenever. Barrels should be started before downloaders and clients.

4. To use in multiple machines, change the RMI_ADDRESS in ./config.properties to <ip of the server computer>:1099