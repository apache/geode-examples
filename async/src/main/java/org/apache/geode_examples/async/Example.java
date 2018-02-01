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
package org.apache.geode_examples.async;

import java.util.Arrays;
import java.util.List;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.cache.client.PoolManager;

public class Example {
  public static final String INCOMING_REGION_NAME = "incoming-region";
  public static final String OUTGOING_REGION_NAME = "outgoing-region";

  public static void main(String[] args) {
    // connect to the locator using default port 10334
    ClientCache cache = new ClientCacheFactory().set("log-level", "WARN").create();

    final String poolName = "subscriptionPool";
    PoolManager.createFactory().addLocator("127.0.0.1", 10334).setSubscriptionEnabled(true)
        .create(poolName);

    // create a local region that matches the server region
    final ClientRegionFactory<Integer, String> incomingRegionFactory =
        cache.<Integer, String>createClientRegionFactory(ClientRegionShortcut.PROXY);
    Region<Integer, String> incomingRegion =
        incomingRegionFactory.setPoolName(poolName).create(INCOMING_REGION_NAME);

    // create another local region that matches the server region
    final ClientRegionFactory<String, String> outgoingRegionFactory =
        cache.<String, String>createClientRegionFactory(ClientRegionShortcut.PROXY);
    Region<String, String> outgoingRegion =
        outgoingRegionFactory.setPoolName(poolName).create(OUTGOING_REGION_NAME);

    new Example().checkWords(incomingRegion, outgoingRegion,
        Arrays.asList(new String[] {"that", "teh", "wil", "i'"}));
    cache.close();
  }

  public void checkWords(Region<Integer, String> incomingRegion,
      Region<String, String> outgoingRegion, List<String> words) {
    int key = 0;
    for (String word : words) {
      incomingRegion.put(key++, word);
    }

    // Give the process a chance to work.
    while (outgoingRegion.sizeOnServer() < words.size()) {
      try {
        Thread.sleep(500);
      } catch (InterruptedException ie) {
        // NOP
      }
    }

    for (String candidate : outgoingRegion.keySetOnServer()) {
      System.out.println(candidate + " -> " + outgoingRegion.get(candidate));
    }
  }
}
