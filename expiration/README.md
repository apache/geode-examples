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

# Geode Expiration Example

This is a simple example that demonstrates expiration of entries from a region. This can be used to
prevent stale entries from lingering in a region. This also allows control over the system resources
consumed by any given region.

A region is a collection of entries which are tuples of key and value. When statistics-gathering is
enabled, the region maintains access and modification times for each entry. With entry expiration
configured, the region will enforce time-to-live limits on entries. When the time since access or
modification exceeds the configured duration, the region will take an action to expire the entry.
The region can either destroy expired entries in their entirety or invalidate expired entries by
removing their values.

This example creates a region where the entries are destroyed after ten seconds without being
updated. The example first puts ten random integers into the region. Then the example loops,
printing the number of entries in the region, until the region is empty.

This example assumes you have installed Java and Geode.

## Steps

1. From the `geode-examples/expiration` directory, build the example and
   run unit tests.

        $ ../gradlew build

2. Next start a locator, start a server, and create a region.

        $ gfsh run --file=scripts/start.gfsh

3. Run the example to demonstrate expiration.

        $ ../gradlew run

4. Shut down the system.

        $ gfsh run --file=scripts/stop.gfsh
