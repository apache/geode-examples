 #
 # Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 # agreements. See the NOTICE file distributed with this work for additional information regarding
 # copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 # "License"); you may not use this file except in compliance with the License. You may obtain a
 # copy of the License at
 #
 # http://www.apache.org/licenses/LICENSE-2.0
 #
 # Unless required by applicable law or agreed to in writing, software distributed under the License
 # is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 # or implied. See the License for the specific language governing permissions and limitations under
 # the License.
 #

#!/bin/bash

GEODE_LOCATION=${1%/}
CATALINA_LOCATION=${CATALINA_HOME%/}

#Copy all nessessary lib files into tomcats lib directory
cp $GEODE_LOCATION/lib/commons-io-*.jar $CATALINA_LOCATION/lib/
cp $GEODE_LOCATION/lib/commons-lang3-*.jar $CATALINA_LOCATION/lib/
cp $GEODE_LOCATION/lib/commons-validator-*.jar $CATALINA_LOCATION/lib/
cp $GEODE_LOCATION/lib/fastutil-*.jar $CATALINA_LOCATION/lib/
cp $GEODE_LOCATION/lib/geode-core-*.jar $CATALINA_LOCATION/lib/
cp $GEODE_LOCATION/lib/geode-logging-*.jar $CATALINA_LOCATION/lib/
cp $GEODE_LOCATION/lib/javax.transaction-api-*.jar $CATALINA_LOCATION/lib/
cp $GEODE_LOCATION/lib/jgroups-*.jar $CATALINA_LOCATION/lib/
cp $GEODE_LOCATION/lib/log4j-api-*.jar $CATALINA_LOCATION/lib/
cp $GEODE_LOCATION/lib/log4j-core-*.jar $CATALINA_LOCATION/lib/
cp $GEODE_LOCATION/lib/log4j-jul-*.jar $CATALINA_LOCATION/lib/
cp $GEODE_LOCATION/lib/shiro-core-*.jar $CATALINA_LOCATION/lib/
cp $GEODE_LOCATION/lib/geode-common-*.jar $CATALINA_LOCATION/lib/
cp $GEODE_LOCATION/lib/geode-management-*.jar $CATALINA_LOCATION/lib/
cp $GEODE_LOCATION/lib/geode-tcp-server-*.jar $CATALINA_LOCATION/lib/
cp $GEODE_LOCATION/lib/geode-membership-*.jar $CATALINA_LOCATION/lib/
cp $GEODE_LOCATION/lib/micrometer-core-*.jar $CATALINA_LOCATION/lib/
cp $CATALINA_HOME/bin/tomcat-juli.jar $CATALINA_HOME/lib/

#Unzip Tomcat Module conf/lib files into tomcats conf/lib directories
unzip -o $1/tools/Modules/Apache_Geode_Modules-*-Tomcat.zip -d $CATALINA_HOME/

export CLASSPATH=$CATALINA_HOME/lib/*

sh $GEODE_LOCATION/bin/gfsh "start locator --name=l1"

sh $GEODE_LOCATION/bin/gfsh "start server --name=server1 --locators=localhost[10334] --server-port=0 \
    --classpath=$CLASSPATH"

#Build sample webapp
cd ../webapp/
./gradlew build
cd ../scripts/

#Place freshly built version of webapp into Tomcats webapps directory
rm -rf $CATALINA_HOME/webapps/SessionStateDemo*
cp ../webapp/build/libs/SessionStateDemo-1.0-SNAPSHOT.war $CATALINA_LOCATION/webapps/SessionStateDemo.war

