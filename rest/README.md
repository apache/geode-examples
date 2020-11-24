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

# Geode REST API example

This is a simple example that demonstrates putting values into a
replicated region and retrieving the values using the Geode REST API. For enabling the REST API you can follow the official [document](https://geode.apache.org/docs/guide/19/rest_apps/setup_config.html#setup_config_enabling_rest).

This example assumes you have installed Java and Geode.

## Steps

1. From the `geode-examples/rest` directory, build the example and
   run unit tests

        $ ../gradlew build

2. Next start the locator and two servers

        $ gfsh run --file=scripts/start.gfsh

3. Run the example to create and get entries using HTTP Java Client from the region

        $ ../gradlew run
        
4. Shut down the system:

        $ gfsh run --file=scripts/stop.gfsh
