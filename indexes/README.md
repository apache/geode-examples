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

This example demonstrates an index for OQL
[queries](https://geode.apache.org/docs/guide/11/developing/query_index/query_index.html)
on a region.

A region can contain objects of arbitrary complexity, including objects that contain other objects.
The values of a region can be queried using
[OQL](https://geode.apache.org/docs/guide/11/developing/querying_basics/chapter_overview.html) and
OQL queries can reference fields in the objects in the region. Indexes can be created to improve
the performance of queries. Certain optimizations occur for top-level fields but indexes can also be
created for nested fields, i.e., fields of objects that are contained with the objects in the
region.

This example uses a mock database of passengers and flights stored in a single region. Since the
region contains passenger objects, the index on passenger name uses a top-level field.
Since flight code objects are contained within a passenger object, the index on airline code uses a
nested field. After randomly populating the mock database, this example shows the results of queries
that use no index, a top-level index, and a nested index.

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
