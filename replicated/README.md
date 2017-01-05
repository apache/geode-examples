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

This is one of the most basic examples. 
Two servers host a replicated region.
The producer puts 50 entries into the replicated region. The consumer prints the number of entries in the region.

## Steps
1. From the ```geode-examples/replicated``` directory, start the locator and two servers:

        $ scripts/startAll.sh

2. Run the producer:

        $ ../gradlew run -Pmain=Producer
        ...
        ... 
        INFO: Done. Inserted 50 entries.

3. Run the consumer:

        $ ../gradlew run -Pmain=Consumer
        ...
        ...
        INFO: Done. 50 entries available on the server(s).

4. Kill one of the servers:

        $ $GEODE_HOME/bin/gfsh
        ...
        gfsh>connect
        gfsh>stop server --name=server1
        gfsh>quit

5. Run the consumer a second time, and notice that all the entries are still available due to replication: 

        $ ../gradlew run -Pmain=Consumer
        ...
        ...
        INFO: Done. 50 entries available on the server(s).

6. Shut down the system:

        $ scripts/stopAll.sh

This example is a simple demonstration of using gfsh and some basic Geode APIs,
as well how to write tests using mocks for Geode applications.
