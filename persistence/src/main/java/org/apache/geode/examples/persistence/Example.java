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
package org.apache.geode.examples.persistence;

import java.util.function.Consumer;

import org.apache.geode.cache.DiskStore;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;

public class Example implements Consumer<Region<String, Integer>> {
  static final String KEY = "counter";

  int counter;

  public static void main(String[] args) {
    // connect to the locator using default port 10334
    ClientCache cache = new ClientCacheFactory().addPoolLocator("127.0.0.1", 10334)
        .set("log-level", "WARN").create();

    // create a local region that matches the server region
    Region<String, Integer> region =
        cache.<String, Integer>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY)
            .create("example-region");

    new Example().accept(region);
    cache.close();
  }

  public Example() {
    counter = -1;
  }

  public int getCounter() {
    return counter;
  }

  @Override
  public void accept(Region<String, Integer> region) {
    if (region.containsKeyOnServer(KEY)) {
      counter = region.get(KEY);
      System.out.println("Retrieved counter of " + getCounter());
    } else {
      counter = 0;
      System.out.println("Initialized counter to " + getCounter());
    }

    ++counter;
    region.put(KEY, counter);

    counter = region.get(KEY);
    System.out.println("Incremented counter to " + getCounter());
  }
}
