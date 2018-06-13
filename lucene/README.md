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

# Geode Lucene Index Example

This example demonstrates the use of a simple Lucene index. Lucene provides
a powerful text search and analysis. 

In this example, two servers host a single partitioned region with entries
that represent employee information. The example indexes the first and last
names of employees.

This example assumes that Java and Geode are installed.

## Set up the Lucene index and region
1. Set directory ```geode-examples/lucene``` to be the
current working directory.
Each step in this example specifies paths relative to that directory.

2. Build the example (with `EmployeeData` class)

        $ ../gradlew build

3. Run a script that starts a locator and two servers, creates a Lucene index
called ```simpleIndex```, and then creates the ```example-region``` region.
A Lucene index must be created before creating the region.

        $ gfsh run --file=scripts/start.gfsh

4. Run the example to populate both the Lucene index and `example-region`. The data
will also be retrieved from the region and printed to the console.

        $ ../gradlew run

## Try ```gfsh``` commands that interact with the region and do Lucene searches
1. Run a `gfsh` command to see the contents of the region

        $ gfsh
        ...
        gfsh>connect --locator=localhost[10334]
        gfsh>query --query="select * from /example-region"
        ...

2. Try different Lucene searches for data in example-region

        gfsh> list lucene indexes

    Note that each server that holds partitioned data for this region has both the ```simpleIndex``` , ```analyzerIndex``` and the ```nestedObjectIndex```. Each Lucene index is stored as a co-located region with the partitioned data region.

     // Search for an exact name match
        gfsh>search lucene --name=simpleIndex --region=example-region --queryStrings="Jive" --defaultField=lastName

     // Search for last name using fuzzy logic: sounds like 'chive'
        gfsh>search lucene --name=simpleIndex --region=example-region --queryStrings="chive~" --defaultField=lastName

     // Do a compound search on first and last name using fuzzy sounds like logic
        gfsh>search lucene --name=simpleIndex --region=example-region --queryStrings="firstName:cat~ OR lastName:chive~" --defaultField=lastName

     // Do a compound search on last name and email using analyzerIndex
        gfsh>search lucene --name=analyzerIndex --region=example-region --queryStrings="lastName:hall~ AND email:Kris.Call@example.com" --defaultField=lastName

     // Do a compound search on nested object with both 5035330001 AND 5036430001 in contacts
     // Note: 5035330001 is the phone number of one of the contacts, 5036430001 is phone number of another contact. Since they are both contacts of this employee, it will lead to this employee. 
        gfsh>search lucene --name=nestedObjectIndex --region=/example-region --queryString="5035330001 AND 5036430001" --defaultField=contacts.phoneNumbers

     // If query on 5035330001 AND 5036430002, it will not find the person, because the 2 phone numbers belong to different people's contacts. 
        gfsh>search lucene --name=nestedObjectIndex --region=/example-region --queryString="5035330001 AND 5036430002" --defaultField=contacts.phoneNumbers

     // If query on 5035330001 OR 5036430002, it will find 2 people's entries
        gfsh>search lucene --name=nestedObjectIndex --region=/example-region --queryString="5035330001 OR 5036430002" --defaultField=contacts.phoneNumbers

3. Examine the Lucene index statistics

        gfsh>describe lucene index --name=simpleIndex --region=example-region

    Note the statistic show the fields that are indexed and the Lucene analyzer used for each field. In the next example we will specify a different Lucene analyzer for each field. Additional statistics listed are the number of documents (region entries) indexed, number of entries committed as well as the number of queries executed for each Lucene index.

4. Exit gfsh and shut down the cluster

        gfsh>exit
        $ gfsh run --file=scripts/stop.gfsh

5. Clean up any generated directories and files so this example can be rerun.
    
        $ ../gradlew cleanServer

