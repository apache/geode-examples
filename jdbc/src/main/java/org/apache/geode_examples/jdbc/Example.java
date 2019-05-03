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
package org.apache.geode_examples.jdbc;

import java.util.HashSet;
import java.util.Set;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;

public class Example {
  private final Region<Long, Parent> region;

  public Example(Region<Long, Parent> region) {
    this.region = region;
  }

  public static void main(String[] args) {
    // connect to the locator using default port 10334
    ClientCache cache =
        new ClientCacheFactory().addPoolLocator("127.0.0.1", 10334)
            .setPdxSerializer(
                new ReflectionBasedAutoSerializer("org.apache.geode_examples.jdbc.Parent"))
            .create();

    // create a local region that connects to the server region
    Region<Long, Parent> region =
        cache.<Long, Parent>createClientRegionFactory(ClientRegionShortcut.PROXY).create("Parent");
    System.out.println("Region=" + region.getFullPath());

    Example example = new Example(region);

    // Put entry in Parent region to verify it propagates to the external RDBMS table
    System.out.println("Adding an entry into Parent region");
    Long key = Long.valueOf(1);
    region.put(key, new Parent(key, "Parent_1", Double.valueOf(123456789.0)));

    // Get an entry from Parent region that will trigger the cache loader to
    // retrieve the entry from the external table
    System.out.println(
        "Getting key=2, if JDBC Connector is configured, it will retrieve data from external data source");
    key = Long.valueOf(2);
    region.get(key);

    // Print the current entries in the region
    System.out.println("All entries currently in Parent region");
    example.printValues(example.getKeys());

    cache.close();
  }

  Set<Long> getKeys() {
    return new HashSet<>(region.keySetOnServer());
  }

  void printValues(Set<Long> values) {
    values.forEach(key -> System.out.println(String.format("%d:%s", key, region.get(key))));
  }

}
