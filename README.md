# CSV Normalization: Truss Engineering Problem

The CSV Normalizer is used to normalize values in a provided CSV and write to an output CSV. Non UTF-8 characters are replaced with the Unicode Replacement Character. Invalid data (i.e. invalid dates or timespans) causes the row not to be included in the output CSV and specific error information is output to stderr. More information about normalization criteria can be found in the full [Truss Interview prompt](https://github.com/trussworks/truss-interview).

## Getting Started

Download the Java/Maven project from the git repository. The project includes a prebuilt executable jar file in the root directory titled `normalizer-1.0.jar`. Normalization of CSV files can be done using this executable. The executable can be rebuilt and used with the instructions in the below 'Building the Executable' section.

## Prerequisites

This project was designed to be ran on Ubuntu 16.04 LTS and requires Java to be installed. To install java on Ubuntu, from the terminal run:

```
sudo apt install default-jre
```

## Usage

In a terminal, navigate to the location of the executable normalizer jar file in the root of the git project. This executable expects to receive a CSV file on stdin and writes a normalized CSV to stdout. Normalization can be run with the command:

```
java -jar normalizer-1.0.jar < [input CSV] > [output CSV]
```

For Example, the command to normalize a file titled _sample.csv_ located in the same directory as the executable jar and output a normalized CSV file titled _normalizedSample.csv_ is:

```
java -jar normalizer-1.0.jar < sample.csv > normalizedSample.csv
```

## Advanced Usage

Running the unit tests and rebuilding the executable requires the use of Maven and Java JDK. This is not necessary if only running the prebuilt executable jar. To install Maven and Java JDK on Ubuntu, execute:

```
sudo apt install default-jdk
sudo apt install maven
```

### Unit Tests

To run the unit tests in the terminal, navigate to the project root and run the command:

```
mvn test
```

### Building the Executable

To build the executable jar with all dependencies included, navigate in the terminal to the project root and run the command:

```
mvn assembly:assembly
```

The executable jar will be rebuilt in the project root and will be named `normalizer-1.0-jar-with-dependencies.jar`.

## Authors

Patrick Dickey