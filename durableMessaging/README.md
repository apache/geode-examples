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

# Geode Durable Messaging Example

This example demonstrates Apache Geode's Durable Messaging feature.
Use durable messaging for subscriptions that you need maintained for your clients even when your clients are down or disconnected.
You can configure any of your event subscriptions as durable. Events for durable queries and subscriptions are saved in a queue when the client
is disconnected and played back when the client reconnects. Other queries and subscriptions are removed from the queue.

The example performs the following tasks to demonstrate durable messaging:

1. Create a client cache with durable messaging enabled
2. Register interest in all keys in the example region with durable messaging enabled
3. Close the client cache, simulating a disconnection
4. Start a second client, and do puts while the first client is down
5. Restart the first client, and observe that the create events in the durable queue are delivered.  A simple cache listener is used to print output to the terminal as create events are received.  If interested, see [Cache Listeners](listener/README.md) for more details on how cache listeners work.

This example assumes you have installed Java and Geode.

## Steps

1. From the `geode-examples/durableMessaging` directory, build the example.

        $ ../gradlew build

2. Next start a locator, start a server, and create a region.

        $ gfsh run --file=scripts/start.gfsh

3. Run the example to demonstrate durable messaging.

        $ ../gradlew run

4. Shut down the server.

        $ gfsh run --file=scripts/stop.gfsh
