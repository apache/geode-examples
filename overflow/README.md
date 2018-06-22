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

# Geode Overflow Example

This examples demonstrates how to use Geode's Overflow to disk.

In this example, a server hosts a partitioned region that stores strings. 
The example shows entry values being overflowed to disk and removed from memory.

This example assumes that Geode is installed.

## Set up the region
1. Set directory ```geode-examples/overflow``` to be the
current working directory.
Each step in this example specifies paths relative to that directory.

2. Run a script that starts a locator and two servers. The script
then creates the ```example-region``` region and puts 4 entries.

        $ gfsh run --file=scripts/start.gfsh
        
Note that both the region size and `totalEntriesOnlyOnDisk` are 0 before we put any entries.

3. Shut down the cluster

        $ gfsh run --file=scripts/stop.gfsh
        
4. In the output of the second `show metrics --region=example-region` command, notice the line
 
`diskstore | totalEntriesOnlyOnDisk       | 2`

This shows that 2 values have been overflowed to disk and are no longer in memory.
Notice that the size from the second `describe region --name=example-region`
is still 4 

`Region   | size                   | 4`

Because all the keys remain in memory.
The entries are still accessible.


