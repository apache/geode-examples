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

import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;

public class Example implements Consumer<Region<EmployeeKey, EmployeeData>> {
  public static void main(String[] args) {
    // connect to the locator using default port 10334
    ClientCache cache = new ClientCacheFactory().addPoolLocator("127.0.0.1", 10334)
        .set("log-level", "WARN").create();

    // create a local region that matches the server region
    Region<EmployeeKey, EmployeeData> region = cache
        .<EmployeeKey, EmployeeData>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY)
        .create("example-region");

    new Example().accept(region);
    cache.close();
  }

  @Override
  public void accept(Region<EmployeeKey, EmployeeData> region) {
    // insert values into the region
    Random r = new Random();
    String[] names =
        "Alex Able,Bertie Bell,Kris Call,Dale Driver,Frankie Forth,Jamie Jive,Morgan Minnow,Pat Puts,Ricky Reliable,Taylor Tack"
            .split(",");
    Arrays.stream(names).forEach(name -> {
      EmployeeKey key = new EmployeeKey(name, r.nextInt());
      EmployeeData val = new EmployeeData(key, r.nextInt(), 40);
      region.put(key, val);
    });

    // count the values in the region
    int inserted = region.keySetOnServer().size();
    System.out.println(String.format("Counted %d keys in region %s", inserted, region.getName()));

    // fetch the values in the region
    region.keySetOnServer().forEach(key -> System.out.println(region.get(key)));
  }
}
