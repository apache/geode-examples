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

# Geode Lucene Spatial Indexing Example

This examples demonstrates how to use Geode's LuceneSerializer and LuceneQueryProvider APIs
to customize how Geode data is stored and indexed in Lucene.

In this example two servers host a partitioned region that stores train station stop information,
including GPS coordinates. The region has lucene index that allows spatial queries to be performed
against the data. The example shows how to do a spatial query to find nearby train stations.

This example assumes that Java and Geode are installed.

## Set up the Lucene index and region
1. Set directory ```geode-examples/luceneSpatial``` to be the
current working directory.
Each step in this example specifies paths relative to that directory.

2. Build the example

        $ ../gradlew build

3. Run a script that starts a locator and two servers, creates a Lucene index
called ```simpleIndex``` with a custom LuceneSerializer that indexes spatial data. The script
then creates the ```example-region``` region.

        $ gfsh run --file=scripts/start.gfsh

4. Run the example. This program adds data to the example-region, and then looks
for train stations with a 1 mile of a specific GPS coordinate. Look at Example.java to see
what this program does.


        $ ../gradlew run


5. Shut down the cluster

        $ gfsh run --file=scripts/stop.gfsh

6. Clean up any generated directories and files so this example can be rerun.
    
        $ ../gradlew cleanServer

