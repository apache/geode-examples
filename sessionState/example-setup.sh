#!/bin/bash

GEODE_LOCATION=${1%/}
CATALINA_LOCATION=${CATALINA_HOME%/}

cp $GEODE_LOCATION/lib/antlr-2.7.7.jar $CATALINA_LOCATION/lib/
cp $GEODE_LOCATION/lib/commons-io-2.6.jar $CATALINA_LOCATION/lib/
cp $GEODE_LOCATION/lib/commons-lang3-3.8.1.jar $CATALINA_LOCATION/lib/
cp $GEODE_LOCATION/lib/commons-validator-1.6.jar $CATALINA_LOCATION/lib/
cp $GEODE_LOCATION/lib/fastutil-8.2.2.jar $CATALINA_LOCATION/lib/
cp $GEODE_LOCATION/lib/geode-core-1.9.0.jar $CATALINA_LOCATION/lib/
cp $GEODE_LOCATION/lib/javax.transaction-api-1.3.jar $CATALINA_LOCATION/lib/
cp $GEODE_LOCATION/lib/jgroups-3.6.14.Final.jar $CATALINA_LOCATION/lib/
cp $GEODE_LOCATION/lib/log4j-api-2.11.1.jar $CATALINA_LOCATION/lib/
cp $GEODE_LOCATION/lib/log4j-core-2.11.1.jar $CATALINA_LOCATION/lib/
cp $GEODE_LOCATION/lib/log4j-jul-2.11.1.jar $CATALINA_LOCATION/lib/
cp $GEODE_LOCATION/lib/shiro-core-1.4.0.jar $CATALINA_LOCATION/lib/
cp $GEODE_LOCATION/lib/geode-common-1.9.0.jar $CATALINA_LOCATION/lib/
cp $GEODE_LOCATION/lib/geode-management-1.9.0.jar $CATALINA_LOCATION/lib/
cp $GEODE_LOCATION/lib/micrometer-core-1.1.3.jar $CATALINA_LOCATION/lib/

unzip -o $1/tools/Modules/Apache_Geode_Modules-1.9.0-Tomcat.zip -d $CATALINA_HOME/

GEODE_LOCATION=${1%/}
CATALINA_LOCATION=${CATALINA_HOME%/}

export CLASSPATH=$CATALINA_HOME/lib/*

sh $GEODE_LOCATION/bin/gfsh "start locator --name=l1"

sh $GEODE_LOCATION/bin/gfsh "start server --name=server1 --locators=localhost[10334] --server-port=0 \
    --classpath=$CLASSPATH"

./gradlew build

rm -rf $CATALINA_HOME/webapps/SessionStateDemo*
cp build/libs/SessionStateDemo-1.0-SNAPSHOT.war $CATALINA_LOCATION/webapps/SessionStateDemo.war

