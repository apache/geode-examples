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
package org.apache.geode.examples.partitioned;

import java.util.*;
import java.util.logging.Logger;

import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;

public class Consumer {

  static final Logger logger = Logger.getAnonymousLogger();
  private Region region1;
  private final String locatorHost = System.getProperty("GEODE_LOCATOR_HOST", "localhost");
  private final int locatorPort = Integer.getInteger("GEODE_LOCATOR_PORT", 10334);
  protected static final String REGION1_NAME = "EmployeeRegion";

  public static void main(String[] args) {
    Consumer c = new Consumer();
    c.setUpRegion();
    c.printRegionContents();
  }


  public Consumer() {}

  public Consumer(Region r) {
    region1 = r;
  }


  public void setUpRegion() {
    ClientCache clientCache = new ClientCacheFactory().addPoolLocator(locatorHost, locatorPort)
        .set("log-level", "WARN").create();
    region1 =
        clientCache.<EmployeeKey, EmployeeData>createClientRegionFactory(ClientRegionShortcut.PROXY)
            .create(REGION1_NAME);
  }


  public void printRegionContents() {
    /* Print EmployeeRegion size and contents */
    Set<EmployeeKey> setOfKeys1 = region1.keySetOnServer();
    logger.info(setOfKeys1.size() + " entries in EmployeeRegion on the server(s).");
    if (setOfKeys1.size() > 0) {
      logger.info("Contents of EmployeeRegion:");
      for (EmployeeKey key : setOfKeys1) {
        logger.info(region1.get(key).toString());
      }
    }
  }


}
