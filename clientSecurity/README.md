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

# Geode security example - Client

This example demonstrates basic command security and user authentication in a client application
backed by a secured Geode cluster.

In this example, three data users with varying permissions attempt to read and write data
 in two regions.
The `dataReader` user has `DATA:READ` permission, `dataWriter` has`DATA:WRITE` permission, and
 `region1dataAdmin` has permissions `DATA:READ:region1` and `DATA:WRITE:region1`, but no permissions
 that apply to `/region2`.
(`region1dataAdmin` also has `DATA:MANAGE:region1` permission, but this permissions is unused
 in this example.)

This example assumes that Java and Geode are installed.

## Demonstration of Security
1. Set directory `geode-examples/clientSecurity` to be the current working directory.
Each step in this example specifies paths relative to that directory.

2. Build the example

        $ ../gradlew build

3. Start a secure cluster consisting of one locator with two servers hosting two regions with
 the script `scripts/start.gfsh`.
In this example, we use the security manager `org.apache.geode.examples.clientSecurity.ExampleSecurityManager`.
This security manager reads a JSON file that defines which roles are granted which permissions,
 as well as each user's username, password, and roles.
The JSON is present in `src/main/resources/example_security.json`.
For convenience, you can run this script with the command:

        $ ../gradlew start

4. Run the example.  Each user will attempt to put data to `/region1` and `/region2`,
 and then read data from `/region1` and `/region2`.  Unauthorized reads and writes throw
 exceptions caused by `NotAuthorizedException`, which we catch and print in this example.

        $ ../gradlew run

5. Stop the cluster using the script `scripts/stop.gfsh`.
For convenience, you can run this script with the command:

        $ ../gradlew stop

## Things to Get Right with Security

- User authentication can be handled by any class that implements `SecurityManager`.

- Specify the `SecurityManager` by the `security-manager` property of all locator and server
property files.  Additional properties may be required by your choice of security manager -- for instance,
our implementation also requires `security-json` to be specified.

- Any class that implements `AuthInitialize` can handle the interaction between your Java code and
 the username and password passed to your `SecurityManager`.

- Refer to the documentation for explicit detailing of permissions required for each command.
