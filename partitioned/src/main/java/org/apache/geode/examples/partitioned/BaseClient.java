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

import java.util.logging.Logger;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;


public abstract class BaseClient {

  static final Logger logger = Logger.getAnonymousLogger();
  protected ClientCache clientCache;

  protected void setRegion1(Region region) {
    this.region1 = region;
  }

  protected void setRegion2(Region region) {
    this.region2 = region;
  }

  private Region region1;
  private Region region2;
  private final String locatorHost = System.getProperty("GEODE_LOCATOR_HOST", "localhost");
  private final int locatorPort = Integer.getInteger("GEODE_LOCATOR_PORT", 10334);
  protected static final String REGION1_NAME = "EmployeeRegion";
  protected static final String REGION2_NAME = "BadEmployeeRegion";
  static final int NUM_ENTRIES = 10;

  public BaseClient() {
    this.clientCache = getClientCache();
  }

  public BaseClient(ClientCache clientCache) {
    this.clientCache = clientCache;
  }

  protected Region getRegion1() {
    if (region1 == null) {
      region1 = getClientCache()
          .<EmployeeKey, EmployeeData>createClientRegionFactory(ClientRegionShortcut.PROXY)
          .create(REGION1_NAME);
    }
    return (region1);
  }

  protected Region getRegion2() {
    if (region2 == null) {
      region2 = getClientCache()
          .<BadEmployeeKey, EmployeeData>createClientRegionFactory(ClientRegionShortcut.PROXY)
          .create(REGION2_NAME);
    }
    return (region2);
  }

  protected ClientCache getClientCache() {
    if (clientCache == null) {
      clientCache = new ClientCacheFactory().addPoolLocator(locatorHost, locatorPort)
          .set("log-level", "WARN").create();
    }
    return clientCache;
  }
}
