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
package org.apache.geode.examples.security;

import static org.apache.geode.distributed.ConfigurationProperties.SECURITY_CLIENT_AUTH_INIT;

import java.util.Arrays;
import java.util.Properties;

import org.apache.logging.log4j.Logger;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.cache.client.ServerOperationException;
import org.apache.geode.internal.logging.LogService;
import org.apache.geode.security.NotAuthorizedException;

public class Example {
  private static Logger logger = LogService.getLogger();
  private static final String REGION1 = "region1";
  private static final String REGION2 = "region2";

  // We use the data present in the loader example
  private static final String[] AUTHORS =
      ("Anton Chekhov,C. J. Cherryh,Dorothy Parker,Douglas Adams,Emily Dickinson,"
          + "Ernest Hemingway,F. Scott Fitzgerald,Henry David Thoreau,Henry Wadsworth Longfellow,"
          + "Herman Melville,Jean-Paul Sartre,Mark Twain,Orson Scott Card,Ray Bradbury,Robert Benchley,"
          + "Somerset Maugham,Stephen King,Terry Pratchett,Ursula K. Le Guin,William Faulkner")
              .split(",");

  private String username;
  private String password;
  private final ClientCache cache;


  private Example(String username, String password) {
    this.username = username;
    this.password = password;
    Properties props = new Properties();
    props.setProperty(ExampleAuthInit.USER_NAME, this.username);
    props.setProperty(ExampleAuthInit.PASSWORD, this.password);
    props.setProperty(SECURITY_CLIENT_AUTH_INIT, ExampleAuthInit.class.getName());
    // connect to the locator using default port 10334
    cache = new ClientCacheFactory(props).setPoolSubscriptionEnabled(true)
        .addPoolLocator("localhost", 10334).create();
  }

  /** A far-from-comprehensive example of integrated security in action. */
  public static void main(String[] args) {
    printCurrentData();

    getDataExample("dataReader", false);
    getDataExample("dataWriter", true);

    copyDataExample("dataWriter", true);
    copyDataExample("dataReader", true);
    copyDataExample("dataAdmin", false);

    printCurrentData();
  }

  /** Prints the data currently present in both /region1 and /region2 */
  private static void printCurrentData() {
    Example example = new Example("super-user", "123");
    Region<String, String> region1 =
        example.cache.<String, String>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY)
            .create(REGION1);
    logger.info("======\nContents of /region1:");
    example.getData(region1, false);
    Region<String, String> region2 =
        example.cache.<String, String>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY)
            .create(REGION2);
    logger.info("======\nContents of /region2:");
    example.getData(region2, false);

    example.cache.close(true);
  }


  /**
   * Attempt to fetch and print the data in /region1 using the authorization of the
   * provided @username.
   */
  private static void getDataExample(String username, boolean expectAuthFailure) {
    logger.info(String.format("Starting get example with user '%s', expecting %s", username,
        expectAuthFailure ? "authorization failure." : "success."));

    Example example = new Example(username, "123");

    Region<String, String> region =
        example.cache.<String, String>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY)
            .create(REGION1);

    example.getData(region, expectAuthFailure);

    example.cache.close(true);
  }

  /** Attempt to print the data present in the given @region. */
  private void getData(Region<String, String> region, boolean expectAuthFailure) {
    try {
      Arrays.stream(AUTHORS).forEach(author -> logger.info(author + ": " + region.get(author)));
    } catch (ServerOperationException e) {
      if (e.getCause() instanceof NotAuthorizedException && expectAuthFailure) {
        logger.info("Got ServerOperationExpectation, caused by expected NotAuthorizedException");
      } else {
        throw e;
      }
    }
  }

  /**
   * Attempt to copy data from /region1 to /region2 using the authorization of the
   * provided @username.
   */
  private static void copyDataExample(String username, boolean expectAuthFailure) {
    logger.info(String.format("Starting copy example with user '%s', expecting %s", username,
        expectAuthFailure ? "authorization failure." : "success."));
    Example example = new Example(username, "123");

    Region<String, String> region1 =
        example.cache.<String, String>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY)
            .create(REGION1);

    Region<String, String> region2 =
        example.cache.<String, String>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY)
            .create(REGION2);

    try {
      Arrays.stream(AUTHORS).forEach(author -> region2.put(author, region1.get(author)));
    } catch (ServerOperationException e) {
      if (e.getCause() instanceof NotAuthorizedException && expectAuthFailure) {
        logger.info("Got ServerOperationExpectation, caused by expected NotAuthorizedException");
      } else {
        throw e;
      }
    }

    example.cache.close(true);
  }

}
