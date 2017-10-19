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

# Geode Asynchronous Event Queues & Listeners Example

This is a simple example that demonstrates asynchronous event queues and listeners.

An asynchronous event queue is an ordered collection of events that occurred on a region, e.g., create a new entry, update an existing entry. An asynchronous event listener has its method invoked from time to time with batches of events that have occurred previously. The method invocation occurs inside the JVM of the server and can _not_ affect the operation on the region.

In this example, an asynchronous event queue is created for the region of incoming words. An asynchronous event listener is specified for that asynchronous event queue. Whenever the `processEvents` method is invoked on the listener, it uses the Levenshtein distance for each word to perform simplistic spell-checking. The proposed revision is the put in the outgoing region. A cache listener is installed that captures all of the creation events for the outgoing region and displays the proposal on the terminal. In other applications, the asynchronous event listener could perform some other calculation on the data.

This example assumes you have installed Java and Geode.

## Steps

1. From the `geode-examples/async` directory, build the example, and
   run unit tests.

        $ ../gradlew build

2. Next start a locator, start two servers, create two regions, and deploy the asynchronous event listener.

        $ gfsh run --file=scripts/start.gfsh

3. Run the example to put entries into the incoming region and get entries from the outgoing region.

        $ ../gradlew run

4. Notice the output.

        that -> that
        teh -> the
        wil -> will
        i -> I

5. Shut down the system.

        $ gfsh run --file=scripts/stop.gfsh
