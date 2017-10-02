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
package org.apache.geode.examples.async;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Consumer;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.cache.client.PoolManager;

public class Example implements Consumer<Region<Integer, String>> {
  public static final String INCOMING_REGION_NAME = "incoming-region";
  public static final String OUTGOING_REGION_NAME = "outgoing-region";
  private final Queue<EntryEvent<String, String>> events;
  private final ExampleCacheListener cacheListener;

  public static void main(String[] args) {
    // connect to the locator using default port 10334
    ClientCache cache = new ClientCacheFactory().set("log-level", "WARN").create();

    final String poolName = "subscriptionPool";
    PoolManager.createFactory().addLocator("127.0.0.1", 10334).setSubscriptionEnabled(true)
        .setMinConnections(0).create(poolName);

    // create a local region that matches the server region
    final ClientRegionFactory<Integer, String> incomingRegionFactory =
        cache.<Integer, String>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY);
    Region<Integer, String> incomingRegion =
        incomingRegionFactory.setPoolName(poolName).create(INCOMING_REGION_NAME);

    // create another local region that matches the server region
    final Example example = new Example();
    final ClientRegionFactory<String, String> outgoingRegionFactory =
        cache.<String, String>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY);
    outgoingRegionFactory.addCacheListener(example.getCacheListener());
    Region<String, String> outgoingRegion =
        outgoingRegionFactory.setPoolName(poolName).create(OUTGOING_REGION_NAME);

    // register interest so the cache listener gets called
    outgoingRegion.registerInterestRegex(".*");

    example.accept(incomingRegion);
    cache.close();
  }

  private void printResults(Region<String, String> region) {
    if (region != null) {
      // Give the process a chance to work.
      try {
        Thread.sleep(500);
      } catch (InterruptedException ie) {
        // NOP
      }

      for (EntryEvent<String, String> event : cacheListener.getEvents()) {
        System.out.println(event.getKey() + " -> " + event.getNewValue());
      }
    }
  }

  Example() {
    events = new ArrayBlockingQueue<>(100, true);
    cacheListener = new ExampleCacheListener(events);
  }

  public ExampleCacheListener getCacheListener() {
    return cacheListener;
  }

  @Override
  public void accept(Region<Integer, String> region) {
    region.put(0, "that");
    region.put(1, "teh");
    region.put(2, "wil");
    region.put(3, "i");

    Cache cache = (Cache) region.getRegionService();
    if (cache != null) {
      printResults(cache.getRegion(OUTGOING_REGION_NAME));
    }
  }
}
