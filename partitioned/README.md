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

This example demonstrates the basic property of partitioning, as well
as what can go wrong with partitioned regions. 
The example is presented in two parts.
The first part shows partitioning, and the second part demonstrates
what can go wrong with a flawed implementation.

The basic property of partitioning is that data entries are distributed 
across all servers that host a region.
The distribution is like database sharding, except that the distribution
occurs automatically. It is also similar to data striping on disks,
except that the distribution is not based on hardware.

In this example,
two servers host two partitioned regions. 
There is no redundancy, so that the basic property of partitioning
may be observed.
The Producer code puts the 10 entries into one of the two
regions.
The Consumer gets and prints the entries from one of the two regions.
The regions are partitioned,
so the entries are distributed among the two servers hosting the region.
Since there is no redundancy of the data within the region,
when one of the servers goes away,
the entries hosted within that server are also gone.

The two regions are the same, except for the hash code implementation.
Part 1 of this example shows partitioning, and uses only the region
called `EmployeeRegion`.
Part 2 of this example shows what can go wrong with a partitioned
region if a bad hashing function is implemented for the region keys.
It uses only the region called `BadEmployeeRegion`.

## Part 1: Partitioning
1. Set directory ```geode-examples/partitioned``` to be the
current working directory.
Each step in this example specifies paths relative to that directory.

1. Build the jar (with the ```EmployeeKey```, ```BadEmployeeKey```, 
and ```EmployeeData``` classes):

    ```
    $   ../gradlew build
    ```
1. Run a script that starts a locator and two servers.
The built JAR will be placed onto the classpath when the script 
starts the servers:

    ```
    $ scripts/startAll.sh
    ```
    Each of the servers hosts both partitioned regions.
    
1. Run the producer to put 10 entries into the ```EmployeeRegion```.
The argument specification identifies the region.

    ```
    $ ../gradlew run -Pmain=Producer -Pargs=EmployeeRegion
    ...
    ... 
    INFO: Inserted 10 entries in EmployeeRegion.
    ```

1. There are several ways to see the contents of the region.
Run the consumer to get and print all 10 entries in the `EmployeeRegion`.
The argument specification identifies the region.

    ```
    $ ../gradlew run -Pmain=Consumer -Pargs=EmployeeRegion
    ...
    INFO: 10 entries in EmployeeRegion on the server(s).
    ...
    ```

    To see contents of the region keys, use a ```gfsh``` query:

    ```
    $ $GEODE_HOME/bin/gfsh
    ...
    gfsh>connect
    gfsh>query --query="select e.key from /EmployeeRegion.entries e"
    ...
    ```


    Or, to see contents of the region values, use a ```gfsh``` query:

    ```
    gfsh>query --query="select * from /EmployeeRegion"
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
    there are for each region on each server by looking at statistics.

    ```
    gfsh>show metrics --categories=partition --region=/EmployeeRegion --member=server1
    ```

    Within the output, the result for `totalBucketSize` identifies
    the number of entries hosted on the specified server.
    Vary the command to see statistics for both `server1` and `server2`.

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
    $ ../gradlew run -Pmain=Consumer -Pargs=EmployeeRegion
    ...
    ...
    INFO: 5 entries in EmployeeRegion on the server(s).
    ...
    ```

6. Shut down the system:

    ```
    $ scripts/stopAll.sh
    ```

## Part 2: What Can Go Wrong

Hashing distributes entries among buckets that reside on servers.
A good hash code is important in order to spread the entries among buckets.

The ```EmployeeRegion``` used in Part 1 of this example has a good hashing
function.
Region entries are well distributed among buckets
(and therefore, among servers).
The ```BadEmployeeRegion``` used in this part of the example
has a pointedly poor hash code implementation,
to illustrate what can go wrong.
The hash code is so bad that all entries in the
```BadEmployeeRegion``` end up in the same bucket.
Because of this,
one of the servers will host all the entries,
while the other server will not have any of the entries.

Here are the steps to run through this example again, 
using ```BadEmployeeRegion``` instead of ```EmployeeRegion```.
This part assumes that you have already built the JAR (step 2 in Part 1 
of this example).

1. Set directory ```geode-examples/partitioned``` to be the
current working directory.
Each step in this example specifies paths relative to that directory.

1. The logs for the locator and two servers were not removed as part
of the system shut down in Part 1.  Part 2 of the example works fine
without removing the logs, but the `startAll.sh` script output will
note that the regions are already hosted as the servers are started.
To start Part 2 from scratch, remove the logs and their directories:

    ```
    $ rm -r locator1 server1 server2
    ```

1. Run a script that starts a locator and two servers.
The built JAR will be placed onto the classpath when the script 
starts the servers:

    ```
    $ scripts/startAll.sh
    ```

1. Run the producer to put 10 entries into the ```BadEmployeeRegion```.
The argument specification identifies the region.

    ```
    $ ../gradlew run -Pmain=Producer -Pargs=BadEmployeeRegion
    ... 
    INFO: Inserted 10 entries in BadEmployeeRegion.
    ```

1.  Run the consumer to get and print all 10 entries in the
`BadEmployeeRegion`.  The argument specification identifies the region.
Alternatively, use one of the `gfsh` commands 
(given in Part 1 of this example)
to verify that the servers are hosting the 10 entries in the region.

    ```
    $ ../gradlew run -Pmain=Consumer -Pargs=BadEmployeeRegion
    ...
    INFO: 10 entries in EmployeeRegion on the server(s).
    ...
    ```

1.  Kill one of the servers:

    ```
    $ $GEODE_HOME/bin/gfsh
    ...
    gfsh>connect
    gfsh>stop server --name=server1
    gfsh>quit
    ```

1. Run the consumer or a `gfsh` query, and notice that either all or
none of the entries of the ```BadEmployeeRegion``` are still available on the 
remaining server.
Those hosted by the server that was stopped were lost.

    ```
    $ ../gradlew run -Pmain=Consumer -Pargs=BadEmployeeRegion
    ...
    ...
    INFO: 5 entries in BadEmployeeRegion on the server(s).
    ...
    ```

6. Shut down the system:

    ```
    $ scripts/stopAll.sh
    ```

## Partitioning Example Takeaways

1. Partitioned regions distribute region entries across buckets within servers.
A robust system will use redundancy in conjunction with partitioning
in production systems,
so that data is not lost if a server goes down.

2. A proper hashcode is important for distributing entries across buckets.
Not demonstrated in this example, but a proper equals method is also
required.

