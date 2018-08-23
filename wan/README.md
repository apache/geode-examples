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

# Geode WAN replication example

This example demonstrates Geode support for asynchronous WAN 
replication between clusters.  WAN replication allows remote Geode 
clusters to automatically keep their region data consistent through
the use of gateway senders and receivers. A gateway sender distributes 
region events to another, remote Geode cluster. A gateway receiver 
configures a physical connection for receiving region events from 
gateway senders in remote Geode clusters. The gateway senders and 
receivers can be configured in several different topologies based on 
specific business needs. For more information on example topologies 
and associated use cases see Geode documentation on 
[Multi-site WAN Configuration](http://geode.apache.org/docs/guide/topologies_and_comm/multi_site_configuration/chapter_overview.html)

In this example, two clusters are created on your local machine, each
with a unique distributed system id and the WAN gateway configured
for active-active, bidirectional region updates. The New York cluster (ny) 
has id=1 and the London cluster (ln) has id=2. Each cluster contains the same 
partitioned region (example-region) and each has parallel gateway senders, 
which means each server in the cluster will send data updates for 
the primary region buckets they hold.  Alternately, you can configure 
serial gateway senders, where only one server in each cluster sends all data 
updates across the WAN. Serial gateway senders are typically used for 
replicated regions or when the order of events between different keys in
a partitioned region needs to be preserved.

This example runs a single client that connects to the London cluster and 
puts 10 entries into the example-region and prints them.  After the client
app has run, both clusters will contain the data.

**Special Note**
The gfsh scripts and gradle tasks for this example do not follow the standard
used by other geode-examples in order to create 2 separate clusters. Due to
this, you must follow the steps outlined below, as this example will not
run using the runAll gradle task.

This example assumes that Java and Geode are installed.

## Steps

1. From the `geode-examples/wan` directory, build the client app example 

        $ ../gradlew build

2. Run the script that starts the London cluster (1 locator and 2 servers) and
   creates the gateway senders and receivers.  

        $ gfsh run --file=scripts/start-ln.gfsh

3. Run the script that starts the New York cluster (1 locator and 2 servers) and
   creates the gateway senders and receivers.  

        $ gfsh run --file=scripts/start-ny.gfsh

4. Run the script that creates the example-region in each cluster and associates the 
   gateway senders to this region.

        $ gfsh run --file=scripts/start-wan.gfsh

5. Run the client example app that connects to the London cluster and puts 10 entries 
into the `example-region`. The data will be automatically sent to the New York cluster,
as well as printed to the console.

        $ ../gradlew run

6. In one terminal, run a `gfsh` command, connect to the New York cluster, and verify
   the region contents

        $ gfsh
        ...
        Cluster-1 gfsh>connect --locator=localhost[10331]
        Cluster-1 gfsh>query --query="select e.key from /example-region.entries e"
        ...

7. In another terminal, run a `gfsh` command, connect to the London cluster, and verify
   the region contents

        $ gfsh
        ...
        Cluster-2 gfsh>connect --locator=localhost[10332]
        Cluster-2 gfsh>query --query="select e.key from /example-region.entries e"
        ...

8. Use other gfsh commands to learn statistics about the regions, gateway senders,
   and gateway receivers for each cluster.

        Cluster-1 gfsh>describe region --name=example-region
        Cluster-1 gfsh>list gateways

9. In the terminal connected to the New York cluster, put another entry in the region 
   and verify it is in the region on this cluster.

        Cluster-1 gfsh>put --key=20 --value="value20" --region=example-region
        Cluster-1 gfsh>query --query="select e.key from /example-region.entries e"

10. In the terminal connected to the London cluster, verify the new entry has also 
    been added to the region on this cluster.

        Cluster-2 gfsh>query --query="select e.key from /example-region.entries e"

11. Exit gfsh in each terminal and shutdown the cluster using the stop.gfsh script
 
        $ gfsh run --file=scripts/stop.gfsh

12. Clean up any generated directories and files.

    	$ ../gradlew cleanServer

