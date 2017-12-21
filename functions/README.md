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

# Geode Functions Example

This is a simple example that calculates which integers in the region are prime numbers. The determination of the primes occurs inside the server by way of a function and the client displays the primes on the console.

The data for a region resides in memory in the JVM for the server. Functions provide a means for calculations to be performed on the data inside the server, avoiding the transfer of the data. For large values, the latency of transferring the data from the server to the client can be large enough to adversely impact performance. Additionally, the server may have access to resources that are unavailable to the client.

A function is implemented by creating a Java class that implements `org.apache.geode_examples.functions.Function`. Using the function context that is passed to a function's execution, the function can access the data from the region. The function's results can be returned from the server to the client using the result sender.

The `deploy` command in `gfsh` deploys all the functions in the specified JAR file. A function's implementation of `execute()` will be run inside the server when `org.apache.geode.cache.execute.Execution.execute()` is invoked for that function.

This example assumes you have installed Java and Geode.

## Steps

1. From the `geode-examples/functions` directory, build the example, and
   run unit tests.

        $ ../gradlew build

2. Next start a locator, start two servers, create a region, and deploy the function.

        $ gfsh run --file=scripts/start.gfsh

3. Run the example to put entries into the region.

        $ ../gradlew run

4. Observe the prime numbers.

        The primes in the range from 1 to 100 are:
        [[1, 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97]]


5. Shut down the system.

        $ gfsh run --file=scripts/stop.gfsh
