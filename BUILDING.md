# Building this Release from Source

Build instructions differ slightly for Unix and Windows platforms.
All platforms require a Java installation, with JDK 1.8 or more recent version.

## Build from Source on Unix

1. Set the JAVA\_HOME environment variable.  For example:

    ```     
    JAVA_HOME=/usr/java/jdk1.8.0_60
    export JAVA_HOME
    ```
2. Download the project source from the Releases page at [Apache Geode] (http://geode.apache.org), and unpack the source code.
3. Within the directory containing the unpacked source code, build and run the tests:
    
    ```
    $ ./gradlew build
    ```

## Build from Source on Windows

1. Set the JAVA\_HOME environment variable.  For example:

    ```
    $ set JAVA_HOME="C:\Program Files\Java\jdk1.8.0_60"
    ```
2. Install Gradle, version 3.3 or a more recent version.
3. Download the project source from the Releases page at [Apache Geode] (http://geode.apache.org), and unpack the source code.
4. Within the folder containing the unpacked source code, build and run the tests:

    ```
    $ gradle build
    ```
