/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.geode.examples.clientSecurity;

import static org.apache.geode.distributed.ConfigurationProperties.SECURITY_CLIENT_AUTH_INIT;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.Logger;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.internal.logging.LogService;

public class Example implements AutoCloseable {
  private static Logger logger = LogService.getLogger();
  private static final String REGION1 = "region1";
  private static final String REGION2 = "region2";

  private static final Map<String, String> REGION1_EXAMPLE_DATA = new HashMap<>();
  private static final Map<String, String> REGION2_EXAMPLE_DATA = new HashMap<>();
  static {
    REGION1_EXAMPLE_DATA.put("'Soon I Will Be Invincible', Austin Grossman",
        "Once you get past a certain threshold, everyone's problems are the same: fortifying your island and hiding the heat signature from your fusion reactor.");
    REGION1_EXAMPLE_DATA.put("'The Name of the Wind', Patrick Rothfuss",
        "You have to be a bit of a liar to tell a story the right way.");
    REGION1_EXAMPLE_DATA.put("'The Lies of Locke Lamora', Scott Lynch",
        "You can't help being young, but it's past time that you stopped being stupid.");

    REGION2_EXAMPLE_DATA.put("'Old Man's War', John Scalzi",
        "I'm not insane, sir. I have a finely calibrated sense of acceptable risk.");
    REGION2_EXAMPLE_DATA.put("'The Way of Kings', Brandon Sanderson",
        "'Ah, the outdoors,' Shallan said. 'I visited that mythical place once.'");
    REGION2_EXAMPLE_DATA.put("'The Blade Itself', Joe Abercrombie",
        "But that was civilisation, so far as Logen could tell. People with nothing better to do, dreaming up ways to make easy things difficult.");
  }

  private final String username;
  private final String password;

  private final ClientCache cache;
  private final Region<String, String> region1;
  private final Region<String, String> region2;


  private Example(String username, String password) {
    this.username = username;
    this.password = password;

    Properties props = new Properties();
    props.setProperty(ExampleAuthInit.USER_NAME, username);
    props.setProperty(ExampleAuthInit.PASSWORD, password);
    props.setProperty(SECURITY_CLIENT_AUTH_INIT, ExampleAuthInit.class.getName());

    // This cache is a singleton and must be closed before another instance of Example
    // is instantiated with different credentials in the same VM.

    // connect to the locator using default port 10334
    cache = new ClientCacheFactory(props).setPoolSubscriptionEnabled(true)
        .addPoolLocator("localhost", 10334).create();
    region1 = cache.<String, String>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY)
        .create(REGION1);
    region2 = cache.<String, String>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY)
        .create(REGION2);
  }


  /**
   * A far-from-comprehensive example of integrated security in action.
   */
  public static void main(String[] args) throws Exception {

    // dataReader is allowed to get but not put data on both regions
    try (Example example = new Example("dataReader", "123")) {
      example.putDataExample();
      example.getDataExample();
    }

    // dataWriter is allowed to put data but will fail the get requests.
    try (Example example = new Example("dataWriter", "123")) {
      example.putDataExample();
      example.getDataExample();
    }

    // region1dataAdmin is allowed to both put and get data, but only in /region1
    try (Example example = new Example("region1dataAdmin", "123")) {
      example.putDataExample();
      example.getDataExample();
    }
  }

  /** Attempt to put REGION1_EXAMPLE_DATA into /region1 and REGION2_EXAMPLE_DATA into /region2 */
  private void putDataExample() {
    logger.info("Writing example data to /region1 as user " + username);
    try {
      region1.putAll(REGION1_EXAMPLE_DATA);
      logger.info("Writing complete.");
    } catch (Exception e) {
      logger.info("Writing to /region1 as user '" + username + "' threw an exception.", e);
    }

    logger.info("Writing example data to /region2 as user " + username);
    try {
      region2.putAll(REGION2_EXAMPLE_DATA);
      logger.info("Writing complete.");
    } catch (Exception e) {
      logger.info("Writing to /region2 as user '" + username + "' threw an exception.", e);
    }
  }


  /** Attempt to read data from both /region1 and /region2 */
  private void getDataExample() {
    logger.info("Reading data from /region1 as user " + username);
    try {
      region1.getAll(REGION1_EXAMPLE_DATA.keySet()).forEach(this::logEntry);
    } catch (Exception e) {
      logger.info("Reading from /region1 as user '" + username + "' threw an exception.", e);
    }

    logger.info("Reading data from /region2 as user " + username);
    try {
      region2.getAll(REGION2_EXAMPLE_DATA.keySet()).forEach(this::logEntry);
    } catch (Exception e) {
      logger.info("Reading from /region2 as user '" + username + "' threw an exception.", e);
    }
  }

  private void logEntry(String key, String value) {
    logger.info(String.format("\n\"%s\"\n -- %s", value, key));

  }

  /**
   * We use AutoCloseable examples to guarantee the cache closes. Failure to close the cache would
   * cause failures when attempting to run with a new user.
   */
  @Override
  public void close() throws Exception {
    cache.close();
  }
}
