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

# Geode OQL region example

This is a simple example that demonstrates putting a map of values into a region, checking the size, and retrieving the values.

This example assumes you have installed Java and Geode.

## Steps

1. From the `geode-examples/oql` directory, build the example and
   run unit tests

        $ ../gradlew build

2. Next start the locator and two servers

        $ gfsh run --file=scripts/start.gfsh

3. Run the example to create entries in the region

        $ ../gradlew run

4. Kill one of the servers

        $ gfsh -e "connect --locator=127.0.0.1[10334]" -e "stop server --name=server"

5. Run a gfsh query, and notice that all the entries are still available due to replication

        $ gfsh -e "connect --locator=127.0.0.1[10334]" -e "query --query='select e.key from /example-region.entries e'"

6. Shut down the system:

        $ gfsh run --file=scripts/stop.gfsh

## Switching the running example

Remember this section, we'll return here periodically to swap the running example as we proceed

		@Override
		  public void accept(Region<Integer, String> region) {
		    simpleQueryExample(region);  <-- this one is not commented
		    //keyValueQueryExample(region);
		    //groupingQueryExample(region);
		  }
		  
## Let's get started with a basic query
		
> In this example we are simply looking to retrieve the set of entities from the region so we have a broad query defined which we will run to see the results in the console

		SelectResults<String> results = region.query("SELECT * FROM /example-region");
		
1. Ensure the "simpleQueryExample" is uncommented

		simpleQueryExample(region);
		