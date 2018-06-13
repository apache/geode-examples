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

# Geode Continuous Query Example

This is a simple example that demonstrates Apache Geode's Continuous Queries(CQs) feature.  CQs allow clients to subscribe
to server-side events using a SQL-like query.  When a client registers a CQ, the client will receive all events that
modify the query results.

In this example, the client program will first register a CQ with the query 
`SELECT * FROM /example-region i where i > 70`. The region has keys and values that are both Integer types.
 
The program loops, randomly generating two integers to put on the server as the key and value.
 
If a value is either created or updated that is greater than 70, the above CQ will trigger the `RandomEventLister`,
which prints to stdout.

The client will generate data for 20 seconds, close the CQ and Cache, and then exit.

This example assumes you have installed Java and Geode.

## Steps

1. From the `geode-examples/cq` directory, build the example.

        $ ../gradlew build

2. Next start a locator, start a server, and create a region.

        $ gfsh run --file=scripts/start.gfsh

3. Run the example to demonstrate continues queries.

        $ ../gradlew run

4. Shut down the server.

        $ gfsh run --file=scripts/stop.gfsh
