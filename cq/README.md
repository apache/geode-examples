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

This is a simple example that demonstrates using a
[continuous query](https://geode.apache.org/docs/guide/11/developing/continuous_querying/chapter_overview.html).
Continuous queries use OQL to determine which events result in notification of registered listeners.

Since multiple clients can use the same region, which may also be replicated or partitioned across
multiple servers, data changes from one client may not necessarily immediately manifest in
other clients. A simple but inefficient method to react to data changes is for a client to
periodically get the values from the region. A more elegant, and more efficient, method is for a
client listen to a continuous query to be notified when a value changes.

This example simulates a multiplayer game. The example application populates a region with a handful
of characters with arbitrary hit points, creates a continuous query to which it listens  to be
notified of changes in hit points, and then runs a game engine in a separate process. The engine
randomly adds or removes hit points from each character until there are no characters remaining with
hit points. The example application prints updates to the console to describe the current state of
the game as powered by the separate, engine application.

## Steps

1. From the `geode-examples/cq` directory, build the example and
   run unit tests.

        $ ../gradlew build

2. Next start a locator, start a server, and create a region.

        $ gfsh run --file=scripts/start.gfsh

3. Run the example to asynchronously modify entries in the region and observes those modifications.

        $ ../gradlew run

4. Shut down the system.

        $ gfsh run --file=scripts/stop.gfsh
