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

# Geode replicated region example

This is a simple example that demonstrates putting values into a
replicated region, checking the size, and retrieving the values.

This example assumes you have installed Java and Geode.

## Steps
1. From the ```geode-examples/replicated``` directory, start the locator and two servers

        $ gfsh run --file=scripts/start.gfsh

2. Run the example to create entries in the region

        $ ./gradlew run

3. Kill one of the servers

        $ gfsh -e "connect --locator=127.0.0.1[10334]" -e "stop server --name=server1"

4. Run the example a second time, and notice that all the entries are still available due to replication

        $ ./gradlew run 

5. Shut down the system:

        $ gfsh run --file=scripts/stop.gfsh
