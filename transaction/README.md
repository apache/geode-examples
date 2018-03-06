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

# Geode Transaction Example

This is a simple example that demonstrates the use of [transactions](https://geode.apache.org/docs/guide/11/developing/transactions/working_with_transactions.html)
to protect consistency during concurrent access and modification of data. Since a region may
configured across multiple servers and multiple clients may interact with that region independent of
each other, data integrity relies on synchronization of modifications between all actors.

An example of how data can become inconsistent during concurrent interaction is as follows:
 1. Client A gets the value of a key.
 2. Client B gets the value of the same key.
 3. Client A puts a new value for the key based upon the original value.
 4. Client B puts a different new value for the key based upon the original value.
The final value for that key is based upon the original value, _not_ the updated value from Client
A. For the final value to contain all the calculations from both clients, both the access and the
modification of the value would need to happen as an atomic action across the region.

This example starts five child processes, each of which tries one thousand times to get the current
value of a counter, increment that value, and the put the incremented value back into the region.
To protect data consistency, the incrementing is abandoned and retried if another child has already
incremented the value _or_ if another child is simultaenously trying to increment the value. This
example, which should take about a dozen seconds, reports the final value of the counter to show
that all of the children's increments were consistently applied.

This example assumes you have installed Java and Geode.

## Steps

1. From the `geode-examples/transaction` directory, build the example and
   run unit tests.

        $ ../gradlew build

2. Next start a locator, start a server, and create a region.

        $ gfsh run --file=scripts/start.gfsh

3. Run the example to demonstrate transactions.

        $ ../gradlew run

4. Shut down the system.

        $ gfsh run --file=scripts/stop.gfsh
