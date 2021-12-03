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

# Geode for Redis Example using Jedis Client

This example demonstrates simple operations on a Geode for Redis cluster using the Jedis Client.

In this example, two servers are started with geode-for-redis enabled, some data is added to the cluster.

This example assumes that Java and Geode are installed.

## Set up the cluster 
1. Set directory ```geode-examples/geodeForRedis``` to be the
current working directory.
Each step in this example specifies paths relative to that directory.

2. Build the example

        $ ../gradlew build

3. Run a script that starts a locator and two servers with geode-for-redis enabled.

        $ ../gradlew start

## Run the example program
Run the example to populate the cluster with some initial leaderboard data, increment 
the scores associated with each member, then remove the lowest scoring member, printing the
contents of the leaderboard at each step.

        $ ../gradlew run

## Optionally use the redis-cli to issue additional commands

1. If you do not have `redis-cli` installed, follow the "Installing Redis" instructions 
at: https://redis.io/topics/quickstart

2. Start the `redis-cli` in cluster mode, specifying the port used to start the geode-for-redis 
server:

        $ redis-cli -c -p 6379

3. Experiment with other commands:

        $ set stringKey aString
        OK
        
        $ get stringKey
        "aString"
        
        $ append stringKey WithAppendedData
        (integer) 23
        
        $ get stringKey
        "aStringWithAppendedData"
        
        $ del stringKey
        (integer) 1
        
        $ get stringKey
        (nil)

Other supported commands can be listed using the `COMMAND` command.

## Shut down the cluster and (optionally) clean up the directory
1. Shut down the cluster:

        $ ../gradlew stop

2. If desired, clean up the generated directories containing
logs:
    
        $ ../gradlew cleanServer
