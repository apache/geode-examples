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

This example demonstrates the basic property of partitioning.  The basic
property of partitioning is that data entries are distributed across all
servers that host a region.  The distribution is like database sharding, except
that the distribution occurs automatically.

In this example, two servers host a single partitioned region.  There is no
redundancy, so that the basic property of partitioning may be observed.  The
example puts 10 entries into the region and prints them.  Because the region is
partitioned, its entries are distributed among the two servers hosting the
region.  Since there is no redundancy of the data within the region, when one
of the servers goes away, the entries hosted within that server are also gone;
the example demonstrates this.

This example assumes that Java and Geode are installed.

## Demonstration of Partitioning
1. Set directory ```geode-examples/partitioned``` to be the
current working directory.
Each step in this example specifies paths relative to that directory.

1. Build the example (with the `EmployeeKey` and `EmployeeData` classes)

        $ ../gradlew build

2. Run a script that starts a locator and two servers.  Each of the servers
hosts the partitioned region.  The example classes will be placed onto the
classpath when the script starts the servers.

        $ gfsh run --file=scripts/start.gfsh

3. Run the example to put 10 entries into the `example-region`. The data
will also be retrieved from the region and printed to the console.

        $ ../gradlew run

4. Run a `gfsh` command to see the contents of the region

        $ gfsh
        ...
        gfsh>connect --locators=127.0.0.1[10334]
        gfsh>query --query="select e.key from /example-region.entries e"
        ...

    Note that the quantity of entries may also be observed with `gfsh`:

        gfsh>describe region --name=example-region
        ..........................................................
        Name            : example-region
        Data Policy     : partition
        Hosting Members : server2
                          server1

        Non-Default Attributes Shared By Hosting Members  

         Type  |    Name     | Value
        ------ | ----------- | ---------
        Region | size        | 10
               | data-policy | PARTITION

    As an alternative, `gfsh` maybe used to identify how many entries
    are in the region on each server by looking at statistics.

        gfsh>show metrics --categories=partition --region=/example-region --member=server1

    Within the output, the result for `totalBucketSize` identifies the number
    of entries hosted on the specified server.  Vary the command to see
    statistics for both `server1` and `server2`.  Note that approximately half
    the entries will be on each server.  And, the quantity on each server may
    vary if the example is started over and run again.

5. The region entries are distributed across both servers.  Stop one of the servers

        $ gfsh
        ...
        gfsh>connect --locators=127.0.0.1[10334]
        gfsh>stop server --name=server1

6. Run the query a second time, and notice that all the entries hosted on
   `server1` are missing as expected.  Those hosted by the server that was stopped
    were lost.

        gfsh>query --query="select e.key from /example-region.entries e"

7. Shut down the cluster

        $ gfsh run --file=scripts/stop.gfsh

## Things to Get Right for Partitioned Regions

- Hashing distributes entries among buckets that reside on servers.  A good
hash code is important in order to spread the entries among buckets (and
therefore, among servers).

- Besides the hash code, `equals()` needs to be defined.

- A system that ought to not lose data if a system member goes down will use
redundancy in conjunction with partitioning in production systems.
