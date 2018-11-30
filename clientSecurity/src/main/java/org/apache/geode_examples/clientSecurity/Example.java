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
package org.apache.geode_examples.clientSecurity;

import java.util.Properties;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.Logger;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.internal.logging.LogService;

public class Example implements AutoCloseable {
  private static final Logger logger = LogService.getLogger();

  private static final String REGION1 = "region1";
  private static final String REGION2 = "region2";

  // Some example data
  private static final String AUTHOR_GROSSMAN = "Grossman";
  private static final String BOOK_BY_GROSSMAN = "Soon I Will Be Invincible";

  private static final String AUTHOR_ROTHFUSS = "Rothfuss";
  private static final String BOOK_BY_ROTHFUSS = "The Name of the Wind";

  private static final String AUTHOR_LYNCH = "Lynch";
  private static final String BOOK_BY_LYNCH = "The Lies of Locke Lamora";

  private static final String AUTHOR_SCALZI = "Scalzi";
  private static final String BOOK_BY_SCALZI = "Old Man's War";

  private static final String AUTHOR_SANDERSON = "Sanderson";
  private static final String BOOK_BY_SANDERSON = "The Way of Kings";

  private static final String AUTHOR_ABERCROMBIE = "Abercrombie";
  private static final String BOOK_BY_ABERCROMBIE = "The Blade Itself";

  // Each example will have its own proxy for the cache and both regions.
  private final ClientCache cache;
  private final Region<String, String> region1;
  private final Region<String, String> region2;

  private Example(String username) {
    Properties props = new Properties();
    props.setProperty("security-username", username);
    props.setProperty("security-client-auth-init", ExampleAuthInit.class.getName());
    props.setProperty("ssl-enabled-components", "all");
    props.setProperty("ssl-keystore", "keystore.jks");
    props.setProperty("ssl-keystore-password", "password");
    props.setProperty("ssl-truststore", "truststore.jks");
    props.setProperty("ssl-truststore-password", "password");

    // connect to the locator using default port 10334
    cache = new ClientCacheFactory(props).setPoolSubscriptionEnabled(true)
        .addPoolLocator("localhost", 10334).create();
    region1 = cache.<String, String>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY)
        .create(REGION1);
    region2 = cache.<String, String>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY)
        .create(REGION2);
  }

  public static void main(String[] args) throws Exception {
    adminUserCanPutAndGetEverywhere();
    writeOnlyUserCannotGet();
    readOnlyUserCannotPut();
    regionUserIsRestrictedByRegion();
  }

  private static void adminUserCanPutAndGetEverywhere() throws Exception {
    String valueFromRegion;
    try (Example example = new Example("superUser")) {
      // All puts and gets should pass
      example.region1.put(AUTHOR_ABERCROMBIE, BOOK_BY_ABERCROMBIE);
      example.region2.put(AUTHOR_GROSSMAN, BOOK_BY_GROSSMAN);

      valueFromRegion = example.region1.get(AUTHOR_ABERCROMBIE);
      Validate.isTrue(BOOK_BY_ABERCROMBIE.equals(valueFromRegion));

      valueFromRegion = example.region2.get(AUTHOR_GROSSMAN);
      Validate.isTrue(BOOK_BY_GROSSMAN.equals(valueFromRegion));
    }
  }

  private static void writeOnlyUserCannotGet() {
    try (Example example = new Example("dataWriter")) {
      // Writes to any region should pass
      example.region1.put(AUTHOR_LYNCH, BOOK_BY_LYNCH);
      example.region2.put(AUTHOR_ROTHFUSS, BOOK_BY_ROTHFUSS);

      // This will fail since dataWriter does not have DATA:READ
      example.region1.get(AUTHOR_LYNCH);
    } catch (Exception e) {
      logger.error("This exception should be caused by NotAuthorizedException", e);
    }
  }

  private static void readOnlyUserCannotPut() {
    try (Example example = new Example("dataReader")) {
      // This will pass
      example.region1.get(AUTHOR_LYNCH);
      example.region2.get(AUTHOR_ROTHFUSS);

      // This will fail since dataReader does not have DATA:WRITE
      example.region1.put(AUTHOR_SANDERSON, BOOK_BY_SANDERSON);
    } catch (Exception e) {
      logger.error("This exception should be caused by NotAuthorizedException", e);
    }
  }

  private static void regionUserIsRestrictedByRegion() {
    try (Example example = new Example("region1dataAdmin")) {
      // This user can read and write only in region1
      example.region1.put(AUTHOR_SANDERSON, BOOK_BY_SANDERSON);
      String valueFromRegion = example.region1.get(AUTHOR_SANDERSON);
      Validate.isTrue(BOOK_BY_SANDERSON.equals(valueFromRegion));

      // This will fail since dataReader does not have DATA:WRITE:region2
      example.region2.put(AUTHOR_SCALZI, BOOK_BY_SCALZI);
    } catch (Exception e) {
      logger.error("This exception should be caused by NotAuthorizedException", e);
    }
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
