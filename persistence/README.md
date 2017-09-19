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

# Geode Persistence Example

This is a simple example that demonstrates persistent regions.

The data for a region resides in memory in the JVM for the server. When a region is persistent, the data for that region is also preserved in a disk store. The disk store uses a directory in a file system to save the operations on regions. Unlike non-persistent regions, the data for a persistent region is available even after a period where no servers for that region are running.

This example assumes you have installed Java and Geode.

## Steps

1. From the `geode-examples/persistence` directory, build the example, and
   run unit tests.

        $ ../gradlew build

2. Next start a locator, start a server, create a disk store, and create a persistent region.

        $ gfsh run --file=scripts/start.gfsh

3. Run the example to increment an entry the region.

        $ ../gradlew run

4. Observe that the first time an initial value is used.

        Initialized counter to 0
        Incremented counter to 1

5. Restart down the server.

        $ gfsh run --file=scripts/restart.gfsh

6. Run the example to increment an entry the region.

        $ ../gradlew run

7. Observe that the second time the previous value is used.

        Retrieved counter of 1
        Incremented counter to 2

8. Shut down the system.

        $ gfsh run --file=scripts/stop.gfsh
