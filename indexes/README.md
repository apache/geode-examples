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

# Geode OQL Indexing Example

This example demonstrates a compact range index for OQL queries on a region.

<!-- TODO SARGE
In this example, two servers host a single partitioned region with entries
that represent employee information.
The example does queries through the API and presents example queries
to be invoked through the `gfsh` command-line interface.
-->

This example assumes that Java and Geode are installed.

## Steps

1. From the `geode-examples/indexes` directory, build the example and
   run unit tests.

        $ ../gradlew build

2. Next start the locator and two servers.

        $ gfsh run --file=scripts/start.gfsh

3. Run the example to create indexes in the region.

        $ ../gradlew run

4. Shut down the system.

        $ gfsh run --file=scripts/stop.gfsh
