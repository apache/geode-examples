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
package org.apache.geode_examples.eviction;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

public class Example {
  public static final int ITERATIONS = 20;

  public static void main(String[] args) {
    // connect to the locator using default port 10334
    ClientCache cache = new ClientCacheFactory().addPoolLocator("127.0.0.1", 10334)
        .set("log-level", "WARN").create();

    Example example = new Example();

    // create a local region that matches the server region
    ClientRegionFactory<Integer, String> clientRegionFactory =
        cache.createClientRegionFactory(ClientRegionShortcut.PROXY);
    Region<Integer, String> region = clientRegionFactory.create("example-region");

    example.putEntries(region);
    cache.close();
  }

  private Collection<Integer> generateIntegers() {
    IntStream stream = new Random().ints(0, ITERATIONS);
    Iterator<Integer> iterator = stream.iterator();
    Set<Integer> integers = new LinkedHashSet<>();
    while (iterator.hasNext() && integers.size() < ITERATIONS) {
      integers.add(iterator.next());
    }
    return integers;
  }

  public void putEntries(Region<Integer, String> region) {
    Collection<Integer> integers = generateIntegers();
    Iterator<Integer> iterator = integers.iterator();

    int created = 0;
    while (iterator.hasNext()) {
      Integer integer = iterator.next();
      region.put(integer, integer.toString());
      System.out.println("Added value for " + integer + "; the region now has "
          + region.sizeOnServer() + " entries.");
      ++created;
    }
  }
}
