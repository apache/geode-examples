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

# Geode cache loader example

This is a simple example that demonstrates loading values using a
`CacheLoader`.  Invoking `Region.get()` causes the `CacheLoader` to
produce a value that is stored in the region.  This approach is
commonly used to fetch data from other systems like a database.

This example assumes you have installed Java and Geode.

## Steps

1. From the `geode-examples/loader` directory, build the example and
   run unit tests

        $ ../gradlew build

2. Next start the locator and two servers

        $ gfsh run --file=scripts/start.gfsh

3. Run the example to load the entries

        $ ../gradlew run

    The example fetches the entries twice.  The first retrieval is slow,
    simulating a network call.  Subsequent retrievals are much faster since the
    values are stored in the cache.  The loader logs requests into the Geode
    server logs.  You can find those at `build/server1/server1.log` or
    `build/server2/server2.log`.

4. Kill one of the servers

        $ gfsh -e "connect --locator=127.0.0.1[10334]" -e "stop server --name=server1"

5. Run the example a second time, and notice that all the entries are still available due to replication

        $ ../gradlew run 

6. Shut down the system:

        $ gfsh run --file=scripts/stop.gfsh
