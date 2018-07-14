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

# Another Geode Functions Example (i.e. functions2)

This is a slightly more advanced function example that explores region types and partition region redundancy settings.  The example uses a replicated region, regionOne, and a partition region, regionTwo, which has a redundancy of 1.  The gradle run task will first put 100 entries into regionOne.  Pressing Enter then runs the deployed function and copies the data from regionOne to regionTwo.  Using gfsh, one can query regionTwo and stop servers to better under how partitioned regions and redundancy works.

Like the first function example, we implement the function using the Java class `org.apache.geode_examples.functions2.Function`. Using the function context that is passed to a function's execution, the function can access the data from the region.

The `deploy` command in `gfsh` deploys all the functions in the specified JAR file. A function's implementation of `execute()` will run inside the server when `org.apache.geode.cache.execute.Execution.execute()` is invoked for that function.

This example assumes you have installed Java and Geode.

## Video

The video below is a full run through of the example.

https://www.youtube.com/watch?v=ZzU3JO0DsTs&t=762s

## Steps

1. From the `geode-examples/functions2` directory, build the example, and
   run unit tests.

        $ ../gradlew build

2. Next start a locator, start three servers, create the two regions, and deploy the function. Make sure that `deploy --jar=build/libs/functions2.jar` points to the correct location. This can depend on where `gfsh` is located.

        $ gfsh run --file=scripts/start.gfsh

3. Run the example to put entries into the `regionOne`, but first query `regionOne` to see that no data has been put in the region.

        gfsh> query --query="select * from /regionOne"

        $ ../gradlew run
        
        gfsh> query --query="select * from /regionOne" 

4. Examine `regionTwo` and copy over the data by `Pressing Enter to Continue.`

        gfsh> query --query="select * from /regionTwo"

        `Pressing Enter to Continue.`

         gfsh> query --query="select * from /regionTwo

5. Stop server1 to see that the data has a redundant copy.

        gfsh> stop server --name=server1

        gfsh> query --query="select * from /regionTwo"

6. Stop server2 to see that the data only had one redundant copy per the region's parameters.

        gfsh> stop server --name=server2

        gfsh> query --query="select * from /regionTwo

7. Shut down the system.

        $ gfsh run --file=scripts/stop.gfsh
