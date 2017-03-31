<!--
Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements.  See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License.  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
-->

# Geode partitioned region example

This example demonstrates the basic property of partitioning.
The basic property of partitioning is that data entries are distributed 
across all servers that host a region.
The distribution is like database sharding, except that the distribution
occurs automatically. 

In this example,
two servers host a single partitioned region. 
There is no redundancy, so that the basic property of partitioning
may be observed.
The Producer code puts the 10 entries into the region.
The Consumer gets and prints the entries from the region.
Because the region is partitioned,
its entries are distributed among the two servers hosting the region.
Since there is no redundancy of the data within the region,
when one of the servers goes away,
the entries hosted within that server are also gone;
the example demonstrates this.

## Demonstration of Partitioning
1. Set directory ```geode-examples/partitioned``` to be the
current working directory.
Each step in this example specifies paths relative to that directory.

1. Build the jar (with the ```EmployeeKey``` and ```EmployeeData``` classes):

    ```
    $   ../gradlew build
    ```
1. Run a script that starts a locator and two servers.
The built JAR will be placed onto the classpath when the script 
starts the servers:

    ```
    $ scripts/startAll.sh
    ```
    Each of the servers hosts the partitioned region.
    
1. Run the producer to put 10 entries into the ```EmployeeRegion```.

    ```
    $ ../gradlew run -Pmain=Producer
    ...
    ... 
    INFO: Inserted 10 entries in EmployeeRegion.
    ```

1. There are several ways to see the contents of the region.
Run the consumer to get and print all 10 entries in the `EmployeeRegion`.

    ```
    $ ../gradlew run -Pmain=Consumer
    ...
    INFO: 10 entries in EmployeeRegion on the server(s).
    ...
    ```

    If desired, use a ```gfsh``` query to see contents of the region keys:

    ```
    $ $GEODE_HOME/bin/gfsh
    ...
    gfsh>connect
    gfsh>query --query="select e.key from /EmployeeRegion.entries e"
    ...
    ```

    Note that the quantity of entries may also be observed with `gfsh`:
 
    ```
    gfsh>describe region --name=EmployeeRegion
    ..........................................................
    Name            : EmployeeRegion
    Data Policy     : partition
    Hosting Members : server2
                      server1

    Non-Default Attributes Shared By Hosting Members  

     Type  |    Name     | Value
    ------ | ----------- | ---------
    Region | size        | 10
           | data-policy | PARTITION
    ```

    As an alternative, `gfsh` maybe used to identify how many entries
    are in the region on each server by looking at statistics.

    ```
    gfsh>show metrics --categories=partition --region=/EmployeeRegion --member=server1
    ```

    Within the output, the result for `totalBucketSize` identifies
    the number of entries hosted on the specified server.
    Vary the command to see statistics for both `server1` and `server2`.
    Note that approximately half the entries will be on each server.
    And, the quantity on each server may vary if the example is started
    over and run again.

1. The region entries are distributed across both servers.
Kill one of the servers:

    ```
    $ $GEODE_HOME/bin/gfsh
    ...
    gfsh>connect
    gfsh>stop server --name=server1
    gfsh>quit
    ```

1. Run the consumer a second time, and notice that approximately half of
the entries of the ```EmployeeRegion``` are still available on the 
remaining server.
Those hosted by the server that was stopped were lost.

    ```
    $ ../gradlew run -Pmain=Consumer
    ...
    ...
    INFO: 5 entries in EmployeeRegion on the server(s).
    ...
    ```

6. Shut down the system:

    ```
    $ scripts/stopAll.sh
    ```

## Things to Get Right for Partitioned Regions

- Hashing distributes entries among buckets that reside on servers.
A good hash code is important in order to spread the entries among buckets
(and therefore, among servers).

- Besides the hash code, equals() needs to be defined.

- A system that ought to not lose data if a system member goes down
will use redundancy in conjunction with partitioning
in production systems.
