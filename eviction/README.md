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

# Geode Eviction Example

This is a simple example that demonstrates eviction of entries from a region. This allows control
over the system resources consumed by any given region.

A region is a collection of entries which are tuples of key and value. Each entry requires memory
for the key object, the value object, and some overhead. Regions that contain a large number of
entries, entries of a large size, or both can consume enough system resources to impact overall
system performance, even for other regions.

A region can have eviction enabled to enforce an upper limit on either the total number of entries
_or_ the total amount of memory consumed by the entries. The region will then enforce the specified
limits on its in-memory resource consumption. When an operation would exceed those limits, the
region will take an action to assure that the limits will not be exceeded after the operation
completes. The region can either destroy one or more entries or overflow one or more entries to disk.

This example assumes you have installed Java and Geode.

## Steps

1. From the `geode-examples/eviction` directory, build the example and
   run unit tests.

        $ ../gradlew build

2. Next start a locator, start a server, and create a region.

        $ gfsh run --file=scripts/start.gfsh

3. Run the example to demonstrate eviction.

        $ ../gradlew run

4. Shut down the system.

        $ gfsh run --file=scripts/stop.gfsh
