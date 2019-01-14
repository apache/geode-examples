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

[<img src="https://geode.apache.org/img/apache_geode_logo.png" align="center"/>](http://geode.apache.org)

[![Build Status](https://travis-ci.org.apache.geode_examples.svg?branch=develop)](https://travis-ci.org.apache.geode_examples) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

# Apache Geode examples

This is the home of Apache Geode examples that are bundled with the project.
Contributions<sup>[2]</sup> and corrections are welcome. Please talk to us
about your suggestions at [dev@geode.apache.org](mailto:dev@geode.apache.org)
or submit a [pull request](https://github.com/apache/geode/pull/new/develop).

# Apache Geode Version

Check out the master branch to run examples against the latest geode release.

## Running an example

The gradle build will automatically download and install a Geode release in the
`build` directory. You can run an example with the following gradle targets:

* `build` - compiles the example and runs unit tests
* `start` - initializes the Geode cluster
* `run` - runs the example Application
* `stop` - shuts down the cluster
* `runAll` - invokes start, run, stop

The commands you need to invoke will be given in the `README.md` file. Sample
usage:

    $ ./gradlew :replicated:start
    $ ./gradlew :replicated:run
    $ ./gradlew :replicated:stop
    $ ./gradlew runAll

## Catalog of examples

The following sections call out ready-made examples or new examples that could
be built. You may want to start your journey with the [Apache Geode in 15
minutes or
Less](http://geode.apache.org/docs/guide/13/getting_started/15_minute_quickstart_gfsh.html)
tutorial.

### Basics

*  [Replicated Region](replicated/README.md)
*  [Partitioned Region](partitioned/README.md)
*  [Put Multiple Values at Once](putall/README.md)
*  [Functions](functions/README.md)
*  [Persistence](persistence/README.md)
*  [OQL (Querying)](queries/README.md)

### Intermediate

*  [Serialization](serialization/README.md)
*  [Lucene Indexing](lucene/README.md)
*  [OQL Indexing](indexes/README.md)
*  [Cache Loader](loader/README.md)
*  [Cache Writer](writer/README.md)
*  [Cache Listeners](listener/README.md)
*  [Async Event Queues & Async Event Listeners](async/README.md)
*  [Continuous Querying](cq/README.md)
*  [Transaction](transaction/README.md)
*  [Eviction](eviction/README.md)
*  [Expiration](expiration/README.md)
*  [Overflow](overflow/README.md)
*  Security
*  Off-heap

### Advanced

*  [Lucene Spatial Indexing](luceneSpatial/README.md)
*  [WAN Gateway](wan/README.md)
*  [Durable Messaging for Subscriptions](durableMessaging/README.md)
*  Delta propagation
*  Network partition detection
*  D-lock
*  Compression
*  Resource manager
*  PDX Advanced

### Use cases, integrations and external examples

This section has self-contained little projects that illustrate a use case or
an integration with other projects.

*  SpringBoot Application
*  HTTP Session replication
*  Redis
*  Memcached
*  Spark Connector

## Adding a new example

Follow this approach to add a new example:

* Create a subdirectory with a descriptive name like `cache-writer`
* Create a `README.md` file in the example subproject to walk the user through the tutorial
* Create a Java class with a main method in the `org.apache.geode_examples.$name.Example` class
* Create a cluster initialization script in `scripts/start.gfsh`
* Create a cluster shutdown script in `scripts/stop.gfsh`
* Modify the top-level `settings.gradle` file to include subproject
* Modify this `README.md` file to include the new example in the catalog of examples

The scripts should contain `gfsh` commands for starting locators, servers, and
creating regions--everything that the example program will need to use. Where
appropriate you should also add unit tests. To customize the build you can add
a `build.gradle` file.

Verify that the examples build by executing `./gradlew runAll` from the root directory.
Note that the build may fail if you do not add ASF license headers or use the
correct formatting. You can fix formatting with `./gradlew spotlessApply`.

## References

- [1]  [https://cwiki.apache.org/confluence/display/GEODE/Criteria+for+Code+Submissions](https://cwiki.apache.org/confluence/display/GEODE/Criteria+for+Code+Submissions)
- [2]  [https://cwiki.apache.org/confluence/display/GEODE/How+to+Contribute](https://cwiki.apache.org/confluence/display/GEODE/How+to+Contribute)
- [3]  [http://www.apache.org/licenses/#clas](http://www.apache.org/licenses/#clas)

## Export Control

This distribution includes cryptographic software.
The country in which you currently reside may have restrictions
on the import, possession, use, and/or re-export to another country,
of encryption software. BEFORE using any encryption software,
please check your country's laws, regulations and policies
concerning the import, possession, or use, and re-export of
encryption software, to see if this is permitted.
See <http://www.wassenaar.org/> for more information.

The U.S. Government Department of Commerce, Bureau of Industry and Security (BIS),
has classified this software as Export Commodity Control Number (ECCN) 5D002.C.1,
which includes information security software using or performing
cryptographic functions with asymmetric algorithms.
The form and manner of this Apache Software Foundation distribution makes
it eligible for export under the License Exception
ENC Technology Software Unrestricted (TSU) exception
(see the BIS Export Administration Regulations, Section 740.13)
for both object code and source code.

The following provides more details on the included cryptographic software:

* Apache Geode is designed to be used with
  [Java Secure Socket Extension](https://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html) (JSSE) and
  [Java Cryptography Extension](http://docs.oracle.com/javase/8/docs/technotes/guides/security/crypto/CryptoSpec.html) (JCE).
  The [JCE Unlimited Strength Jurisdiction Policy](http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html)
  may need to be installed separately to use keystore passwords with 7 or more characters.
* Apache Geode links to and uses [OpenSSL](https://www.openssl.org/) ciphers.
