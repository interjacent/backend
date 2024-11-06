# interjacent backend
This repository contains the source code of interjacent's backend.

## Building and running
Make sure you have Java 17 installed, then run the following command to launch the backend:

```shell
./gradlew bootRun
```

Alternately, build a jar file separately:

```shell
./gradlew bootJar
```

You'll find the jar file in `./build/libs` (use the one without the `plain` suffix).

When run, an HTTP server will be started on port 8080 by default.