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
package org.apache.geode.examples.writer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.geode.cache.CacheWriterException;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.cache.client.ServerOperationException;

public class Example implements Consumer<Region<String, String>> {
  private List<String> names = new ArrayList<>();

  public static void main(String[] args) {
    // connect to the locator using default port 10334
    ClientCache cache = new ClientCacheFactory().addPoolLocator("127.0.0.1", 10334)
        .set("log-level", "WARN").create();

    // create a local region that matches the server region
    Region<String, String> region =
        cache.<String, String>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY)
            .create("example-region");

    new Example().accept(region);
    cache.close();
  }

  public List<String> getNames() {
    return names;
  }

  private void put(Region<String, String> region, String ssn, String name) {
    try {
      region.put(ssn, name);
      names.add(name);
    } catch (CacheWriterException | ServerOperationException e) {
      System.out.println("Invalid SSN: " + ssn);
    }
  }

  @Override
  public void accept(Region<String, String> region) {
    put(region, "123-45-6789", "Bart Simpson");
    put(region, "666-66-6666", "Bill Gates");
    put(region, "777-77-7777", "Raymond Babbitt");
    put(region, "8675309", "Jenny");
    put(region, "999-000-0000", "Blackberry");
  }
}
