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

package org.apache.geode_examples.cq;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.cache.query.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;



public class Example {

  private ClientCache cache;
  private Region<Integer, Integer> region;
  private CqQuery randomTracker;

  private void init() throws CqException, RegionNotFoundException, CqExistsException {
    // init cache, region, and CQ

    // connect to the locator using default port 10334
    this.cache = connectToLocallyRunningGeode();


    // create a local region that matches the server region
    this.region = cache.<Integer, Integer>createClientRegionFactory(ClientRegionShortcut.PROXY)
        .create("example-region");

    this.randomTracker = this.startCQ(this.cache, this.region);
  }

  private void run() throws InterruptedException {

    this.startPuttingData(this.region);

  }

  private void close() throws CqException {

    // close the CQ and Cache
    this.randomTracker.close();
    this.cache.close();

  }


  public static void main(String[] args) throws Exception {

    Example mExample = new Example();

    mExample.init();

    mExample.run();

    mExample.close();

    System.out.println("\n---- So that is CQ's----\n");

  }

  private CqQuery startCQ(ClientCache cache, Region region)
      throws CqException, RegionNotFoundException, CqExistsException {
    // Get cache and queryService - refs to local cache and QueryService

    CqAttributesFactory cqf = new CqAttributesFactory();
    cqf.addCqListener(new RandomEventListener());
    CqAttributes cqa = cqf.create();

    String cqName = "randomTracker";

    String queryStr = "SELECT * FROM /example-region i where i > 70";

    QueryService queryService = region.getRegionService().getQueryService();
    CqQuery randomTracker = queryService.newCq(cqName, queryStr, cqa);
    randomTracker.execute();


    System.out.println("------- CQ is running\n");

    return randomTracker;
  }

  private void startPuttingData(Region region) throws InterruptedException {

    // Example will run for 20 second

    Stopwatch stopWatch = Stopwatch.createStarted();

    while (stopWatch.elapsed(TimeUnit.SECONDS) < 20) {

      // 500ms delay to make this easier to follow
      Thread.sleep(500);
      int randomKey = ThreadLocalRandom.current().nextInt(0, 99 + 1);
      int randomValue = ThreadLocalRandom.current().nextInt(0, 100 + 1);
      region.put(randomKey, randomValue);
      System.out.println("Key: " + randomKey + "     Value: " + randomValue);

    }

    stopWatch.stop();

  }

  private ClientCache connectToLocallyRunningGeode() {

    ClientCache cache = new ClientCacheFactory().addPoolLocator("127.0.0.1", 10334)
        .setPoolSubscriptionEnabled(true).set("log-level", "WARN").create();

    return cache;
  }

}
