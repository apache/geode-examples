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
package org.apache.geode_examples.partitioned;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;

public class Example {
  private final Region<EmployeeKey, EmployeeData> region;

  public Example(Region<EmployeeKey, EmployeeData> region) {
    this.region = region;
  }

  public static void main(String[] args) {
    // connect to the locator using default port 10334
    ClientCache cache = new ClientCacheFactory().addPoolLocator("127.0.0.1", 10334)
        .set("log-level", "WARN").create();

    // create a local region that matches the server region
    Region<EmployeeKey, EmployeeData> region =
        cache.<EmployeeKey, EmployeeData>createClientRegionFactory(ClientRegionShortcut.PROXY)
            .create("example-region");

    Example example = new Example(region);
    example.insertValues(new String[] {"Alex Able", "Bertie Bell", "Chris Call", "Dale Driver",
        "Frankie Forth", "Jamie Jive", "Morgan Minnow", "Pat Pearson", "Ricky Reliable",
        "Taylor Tack", "Zelda Zankowski"});
    example.printValues(example.getValues());

    cache.close();
  }

  Set<EmployeeKey> getValues() {
    return new HashSet<>(region.keySetOnServer());
  }

  void insertValues(String[] names) {
    Random r = new Random();
    Arrays.stream(names).forEach(name -> {
      EmployeeKey key = new EmployeeKey(name, 1 + r.nextInt(1000000));
      EmployeeData val = new EmployeeData(key, 50000 + r.nextInt(100000), 40);
      region.put(key, val);
    });
  }

  void printValues(Set<EmployeeKey> values) {
    values.forEach(key -> System.out.println(key.getName() + " -> " + region.get(key)));
  }
}
