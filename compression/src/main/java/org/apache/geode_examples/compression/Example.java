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
package org.apache.geode_examples.compression;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.*;

public class Example {
  private final Region<Integer, String> region;
  private static final String POOL_NAME = "client-pool";

  public Example(Region<Integer, String> region) {
    this.region = region;
  }

  public static void main(String[] args) {
    ClientCache cache = new ClientCacheFactory().set("log-level", "WARN").create();
    // connect to the locator using default port 10334
    PoolFactory poolFactory = PoolManager.createFactory();
    poolFactory.addLocator("127.0.0.1", 10334);
    poolFactory.create(POOL_NAME);

    // create a local region that matches the server region
    Region<Integer, String> region =
        cache.<Integer, String>createClientRegionFactory(ClientRegionShortcut.PROXY)
            .setPoolName(POOL_NAME).create("example-region");

    Example example = new Example(region);
    example.putValues(10);
    example.printValues(example.getValues());

    cache.close();
  }

  Set<Integer> getValues() {
    return new HashSet<>(region.keySetOnServer());
  }

  void putValues(int upperLimit) {
    IntStream.rangeClosed(1, upperLimit).forEach(i -> region.put(i, "value" + i));
  }

  void printValues(Set<Integer> values) {
    values.forEach(key -> System.out.println(String.format("%d:%s", key, region.get(key))));
  }
}
