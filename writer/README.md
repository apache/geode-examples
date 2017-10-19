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

# Geode Cache Writer Example

This is a simple example that demonstrates the use of a cache writer to validate modifications to a region.

A cache writer is added to a region as the region is created. _Before_ an event (e.g., create a new entry, update an existing entry) occurs on that region, the cache writer has the appropriate handler method invoked, e.g., `beforeCreate()` for creating a new entry. This method invocation _can_ affect the operation on the region: if it throws `CacheWriterException` the operation is aborted.

In this example, a cache writer is installed that vets all of the creation events for the region for proper formatting of Social Security numbers. A number of entries are created in the region. The cache writer vets the supplied key for valid formatting. In other applications, the event could either be persisted to some other data store (i.e., write-ahead) or a notification about the activity could be sent via some other mechanism.

This example assumes you have installed Java and Geode.

## Steps

1. From the `geode-examples/writer` directory, build the example and
   run unit tests.

        $ ../gradlew build

2. Next start a locator, start a server, and create a region.

        $ gfsh run --file=scripts/start.gfsh

3. Run the example to add a cache listener, put entries into the region, and capture the events.

        $ ../gradlew run

4. Shut down the system.

        $ gfsh run --file=scripts/stop.gfsh
