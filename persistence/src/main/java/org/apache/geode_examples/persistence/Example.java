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
package org.apache.geode_examples.persistence;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;

public class Example {
  private static final String KEY = "counter";
  private final Region<String, Integer> region;

  public static void main(String[] args) {
    // connect to the locator using default port 10334
    ClientCache cache = new ClientCacheFactory().addPoolLocator("127.0.0.1", 10334)
        .set("log-level", "WARN").create();

    // create a local region that matches the server region
    Region<String, Integer> region =
        cache.<String, Integer>createClientRegionFactory(ClientRegionShortcut.PROXY)
            .create("example-region");

    Example example = new Example(region);
    final int previous = example.getCounter();
    example.increment();
    final int current = example.getCounter();
    System.out.println(previous + " -> " + current);

    cache.close();
  }

  public Example(Region<String, Integer> region) {
    this.region = region;
    if (!region.containsKeyOnServer(KEY)) {
      region.put(KEY, 0);
    }
  }

  public int getCounter() {
    return region.get(KEY);
  }

  public void increment() {
    region.put(KEY, region.get(KEY) + 1);
  }
}
