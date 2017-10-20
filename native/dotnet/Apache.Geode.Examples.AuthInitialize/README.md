# AuthInitialize Example
This example shows how to create and register a custom `IAuthIntialize` authentication
handler. 

## Prerequisites
* Install [Apache Geode](https://geode.apache.org)
* Build and install [Apache Geode Native](https://github.com/apache/geode-native)

## Running
* Start Geode Server and create region.
  ```
  gfsh>start locator --name=locator
  gfsh>start server --name=server
  gfsh>create region --name=region --type=PARTITION
  ```
* Execute `Apache.Geode.Examples.AuthInitialize.exe`.
  
  output:
  ```
  ExampleAuthInitialize::ExampleAuthInitialize called
  ExampleAuthInitialize::GetCredentials called
  a = 1
  b = 2
  ```
