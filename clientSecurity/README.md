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
backed by a secured Geode cluster. It also demonstrates use of secure sockets (SSL) between all
members and between a client and a server.  This example assumes that Java is installed.

## Security Basics

Geode security is based on Apache Shiro.
Permissions are defined by

1. the resource accessed (`DATA` and `CLUSTER`)
2. the operation performed (`READ`, `WRITE`, or `MANAGE`)
3. the target for this resource operation (typically a region name)
4. the key for the target (e.g., the key of the region's key-value pair)
  
A single permission is represented by a `:`-separated string, e.g., `DATA:READ:region1:myKey`.

Permissions need not be fully specified.
Abridged permissions are hierarchical.
A permission of `CLUSTER` implies `CLUSTER:READ`, `CLUSTER:WRITE`, and `CLUSTER:MANAGE`,
 for all target regions and all key values.
Using wildcard annotation, a permission of `CLUSTER` is equivalent to `CLUSTER:*:*:*`.


In this example, four users with varying permissions attempt to read and write data
 in two regions.
* The `superUser` user has full permissions and may read and write to all regions.
* The `dataReader` user has `DATA:READ` permission, granting read access to all regions.
* The `dataWriter` user has `DATA:WRITE` permission, granting write access to all regions.
* The `region1dataAdmin` has permissions `DATA:READ:region1` and `DATA:WRITE:region1`,
 granting read and write access only to `/region1`.

For more information on what permission is required for a given operation,
 refer to the documentation.

## Required Implementations

  Two interfaces must be implemented to secure a Geode cluster: `AuthInitialize`
   and `SecurityManager`.
  
  Your implementation of `org.apache.geode.security.AuthInitialize` should handle the interaction
   with any existing security infrastructure (e.g., ldap).  In this example, we provide a trivial
   implementation in `org.apache.geode_examples.clientSecurity.ExampleAuthInit`.

  These credentials are then given to your implementation
   of `org.apache.geode.security.SecurityManager`
   to authenticate the user (i.e., to log in).
  The security manager also handles authorization of the authenticated user 
  for particular operations.
  How permissions are assigned to users is also determined by the security manager.
    In this example,
   we group permissions by *role*, and assign each user one or more roles in a JSON file.
  This file is located at `src/main/resources/example_security.json`.

## Demonstration of Security
1. Set directory `geode-examples/clientSecurity` to be the current working directory.
Each step in this example specifies paths relative to that directory.

2. Build the example

        $ ../gradlew build

3. Start a secure cluster consisting of one locator with two servers with two regions.
 Refer to `scripts/start.gfsh`.
 When starting a secure cluster, you must specify a *security manager*
  that implements authorization.
 In this example, we use the security manager
  `org.apache.geode.examples.clientSecurity.ExampleSecurityManager`.
 This security manager reads a JSON file that defines which roles are granted which permissions,
 as well as each user's username, password, and roles.
 The JSON is present in `src/main/resources/example_security.json`.
 You can execute the `scripts/start.gfsh` script with the command:
 
        $ ../gradlew start

4. Run the example.  Each user will attempt to put data to `/region1` and `/region2`,
 and then read data from `/region1` and `/region2`.  Unauthorized reads and writes throw
 exceptions caused by `NotAuthorizedException`, which we catch and print in this example.

        $ ../gradlew run

5. Stop the cluster using the script `scripts/stop.gfsh`.  
You can run this script with the command:

        $ ../gradlew stop

## Things to Get Right with Security

- Implement `org.apache.geode.security.AuthInitialize` to pass user credentials from any existing
 security infrastructure.

- Implement `org.apache.geode.security.SecurityManager` to handle user authentication
 and operation authorization.

- Specify the `SecurityManager` by the `security-manager` property of all locator and server
property files.  An unsecured member or a member secured by a different security manager will not
be allowed to join the cluster.

- If additional properties are required by your implementation of the security manager,
  these may be defined in your locator or server property files.
  For instance, our implementation also requires `security-json` to be defined.

