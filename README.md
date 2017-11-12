# BSC Homework

Simple application that tracks payments

## Building and running

This project uses Java 8 and Maven (works with 3.5.2, but should work with 3.x).
To build:
```
    mvn package
```
To run after build:
```
    java -jar target/homework-1.0-SNAPSHOT-jar-with-dependencies.jar
```

See usage help:
```
    java -jar target/homework-1.0-SNAPSHOT-jar-with-dependencies.jar --help
```

Example of run with imported currency rates and payments from files:
```
    java -jar target/homework-1.0-SNAPSHOT-jar-with-dependencies.jar --payments examplePayments.txt --currencies exampleCurrencies.txt
```


## Authors

* **Petr Sýkora** 
