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
package org.apache.geode.examples.replicated;

import java.util.function.Consumer;
import java.util.stream.IntStream;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;

public class Example implements Consumer<Region<Integer, String>> {
  public static void main(String[] args) {
    // connect to the locator using default port 10334
    ClientCache cache = new ClientCacheFactory().addPoolLocator("127.0.0.1", 10334)
        .set("log-level", "WARN").create();

    // create a local region that matches the server region
    Region<Integer, String> region =
        cache.<Integer, String>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY)
            .create("example-region");

    new Example().accept(region);
    cache.close();
  }

  @Override
  public void accept(Region<Integer, String> region) {
    // insert values into the region
    int count = 10;
    IntStream.range(0, count).forEach(i -> region.put(i, "value" + i));
    System.out
        .println(String.format("Inserted %d entries into region %s", count, region.getName()));

    // count the values in the region
    int inserted = region.keySetOnServer().size();
    System.out.println(String.format("Counted %d keys in region %s", inserted, region.getName()));

    // fetch the values in the region
    region.keySetOnServer()
        .forEach(key -> System.out.println(String.format("%d:%s", key, region.get(key))));
  }
}
