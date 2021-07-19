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

# Geode Session State Example using Tomcat

This is a simple example which demonstrates how to setup and use the Session Management Module for Tomcat.

This example assumes you have Geode and Java installed. It also assumes you have local copy of Tomcat Downloaded. 
It was designed and tested with Geode 1.9.0 and Tomcat 9, and while the session features will work with other combinations
you may need to make some changes to the setup if you're using other versions. For more information about how to set up
the tomcat module with your version of Tomcat and Geode see the official documentation at: 
`https://geode.apache.org/docs/guide/19/tools_modules/http_session_mgmt/tomcat_installing_the_module.html`

##Steps

1. Set the environment Variable $CATALINA_HOME to point at the root directory of your local Tomcat installation. This is a 
Tomcat convention, so in some cases this may have already been set.

2. Find the configuration files located at $CATALINA_HOME/conf/. To the server.xml file add the line:

  ```
<Listener className="org.apache.geode.modules.session.catalina.ClientServerCacheLifecycleListener"/>
  ```

  and to the file context.xml add the line:

  ```
<Manager className="org.apache.geode.modules.session.catalina.Tomcat9DeltaSessionManager"/> 
  ```

3. Run the setup script:

  ```
  cd scripts
  ./example-setup.sh <root directory of Geode install>
  ```

Specify the root directory of your local Geode installation. Make sure you have no local Geode cluster running, as this step will start
a new local cluster to manage our Session States. This can be done by running gfsh from your geode installation and running a `connect`
command with no parameters. If a cluster is found, use the shutdown command to stop the cluster before continuing.

4. Run the tomcat startup script located at $CATALINA_HOME/bin/startup.sh. You should now be able to reach the example webapp by entering
the following URL into your browser:
```
localhost:8080/SessionStateDemo/index
```

5. You should now be able to see details about your current session on the page. You can add key/value pairs to your session and get them
back with the available input prompts. 
