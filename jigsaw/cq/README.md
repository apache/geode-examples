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

# Geode with Jigsaw Continuous Query Example

This example is a duplicate of the `geode-examples/cq` example,
excepting that this example uses the "Jigsaw" modules introduced by Java9.

See the aforementioned example's `README.md` for more information.

The intent of this example is to demonstrate how to configure a simple `module-info.java` to require
Geode (and bug-related transitive requirements of Log4j and SQL) and the configuration within
the `build.gradle`.

Note that some dependencies are injected from the root project's `build.gradle`.
