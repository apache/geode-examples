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
package org.apache.geode_examples.expiration;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

public class Example {
  private static final DateFormat ISO_8601_TIMESTAMP_FORMAT =
      new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

  public static void main(String[] args) {
    // connect to the locator using default port 10334
    ClientCache cache = new ClientCacheFactory().addPoolLocator("127.0.0.1", 10334)
        .set("log-level", "WARN").create();

    Example example = new Example();

    // create a local region that matches the server region
    ClientRegionFactory<Integer, String> clientRegionFactory =
        cache.createClientRegionFactory(ClientRegionShortcut.PROXY);
    Region<Integer, String> region = clientRegionFactory.create("example-region");

    example.insertValues(region, example.generateIntegers(10));
    example.monitorEntries(region);

    cache.close();
  }

  private Collection<Integer> generateIntegers(int upperLimit) {
    IntStream stream = new Random().ints(0, upperLimit);
    Iterator<Integer> iterator = stream.iterator();
    Set<Integer> integers = new LinkedHashSet<>();
    while (iterator.hasNext() && integers.size() < upperLimit) {
      integers.add(iterator.next());
    }
    return integers;
  }

  void insertValues(Region<Integer, String> region, Collection<Integer> integers) {
    Map values = new HashMap<Integer, String>();
    for (Integer i : integers) {
      values.put(i, i.toString());
    }
    region.putAll(values);
    System.out.println(
        ISO_8601_TIMESTAMP_FORMAT.format(new Date()) + "\tInserted " + values.size() + " values.");
  }

  public void monitorEntries(Region<Integer, String> region) {
    while (0 < region.sizeOnServer()) {
      try {
        Thread.sleep(1000);
        System.out.println(ISO_8601_TIMESTAMP_FORMAT.format(new Date()) + "\tThe region now has "
            + region.sizeOnServer() + " entries.");
      } catch (InterruptedException ie) {
        // NOP
      }
    }
  }
}
