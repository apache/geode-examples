# Cache Example
This is a very simple example showing how to create a `Cache` using the `CacheFactory`,
configure a `Pool` with a `PoolFactory`, and configure a `Region` with a `RegionFactory`.

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
* Execute `Apache.Geode.Examples.Cache.exe`.
  
  output:
  ```
  a = 1
  b = 2
  ```
