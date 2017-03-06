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

import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;

import java.util.logging.Logger;

public class Producer {

  static final Logger logger = Logger.getAnonymousLogger();
  // protected ClientCache clientCache;
  private Region region1;
  private final String locatorHost = System.getProperty("GEODE_LOCATOR_HOST", "localhost");
  private final int locatorPort = Integer.getInteger("GEODE_LOCATOR_PORT", 10334);
  protected static final String REGION1_NAME = "EmployeeRegion";

  public static void main(String[] args) {
    Producer p = new Producer();
    p.setUpRegion();
    p.populateRegion();
  }


  public Producer() {

  }

  public Producer(Region r) {
    region1 = r;
  }

  public void setUpRegion() {
    ClientCache clientCache = new ClientCacheFactory().addPoolLocator(locatorHost, locatorPort)
        .set("log-level", "WARN").create();
    region1 =
        clientCache.<EmployeeKey, EmployeeData>createClientRegionFactory(ClientRegionShortcut.PROXY)
            .create(REGION1_NAME);
  }

  /* Put 10 entries into the region with a quality hashing function. */
  public void populateRegion() {

    EmployeeKey k1 = new EmployeeKey("Alex Able", 160);
    EmployeeData d1 = new EmployeeData(k1, 70000, 40);
    region1.put(k1, d1);

    EmployeeKey k2 = new EmployeeKey("Bertie Bell", 170);
    EmployeeData d2 = new EmployeeData(k2, 72000, 40);
    region1.put(k2, d2);

    EmployeeKey k3 = new EmployeeKey("Kris Call", 180);
    EmployeeData d3 = new EmployeeData(k3, 68000, 40);
    region1.put(k3, d3);

    EmployeeKey k4 = new EmployeeKey("Dale Driver", 190);
    EmployeeData d4 = new EmployeeData(k4, 81500, 36);
    region1.put(k4, d4);

    EmployeeKey k5 = new EmployeeKey("Frankie Forth", 201);
    EmployeeData d5 = new EmployeeData(k5, 45000, 40);
    region1.put(k5, d5);

    EmployeeKey k6 = new EmployeeKey("Jamie Jive", 220);
    EmployeeData d6 = new EmployeeData(k6, 56500, 40);
    region1.put(k6, d6);

    EmployeeKey k7 = new EmployeeKey("Morgan Minnow", 230);
    EmployeeData d7 = new EmployeeData(k7, 65000, 36);
    region1.put(k7, d7);

    EmployeeKey k8 = new EmployeeKey("Pat Puts", 240);
    EmployeeData d8 = new EmployeeData(k8, 67000, 40);
    region1.put(k8, d8);

    EmployeeKey k9 = new EmployeeKey("Ricky Reliable", 2500);
    EmployeeData d9 = new EmployeeData(k9, 71000, 40);
    region1.put(k9, d9);

    EmployeeKey k10 = new EmployeeKey("Taylor Tack", 260);
    EmployeeData d10 = new EmployeeData(k10, 70000, 40);
    region1.put(k10, d10);

    logger.info("Inserted 10 entries in EmployeeRegion.");
  }

}
