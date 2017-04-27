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
package org.apache.geode.examples.loader;

import java.util.Arrays;
import java.util.function.Consumer;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;

public class Example implements Consumer<Region<String, String>> {
  private static final String[] AUTHORS =
      ("Anton Chekhov,C. J. Cherryh,Dorothy Parker,Douglas Adams,Emily Dickinson,"
          + "Ernest Hemingway,F. Scott Fitzgerald,Henry David Thoreau,Henry Wadsworth Longfellow,"
          + "Herman Melville,Jean-Paul Sartre,Mark Twain,Orson Scott Card,Ray Bradbury,Robert Benchley,"
          + "Somerset Maugham,Stephen King,Terry Pratchett,Ursula K. Le Guin,William Faulkner")
              .split(",");

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

  @Override
  public void accept(Region<String, String> region) {
    // initial fetch invokes the cache loader
    {
      long start = System.currentTimeMillis();
      Arrays.stream(AUTHORS)
          .forEach(author -> System.out.println(author + ": " + region.get(author)));

      long elapsed = System.currentTimeMillis() - start;
      System.out.println(
          String.format("\n\nLoaded %d definitions in %d ms\n\n", AUTHORS.length, elapsed));
    }

    // fetch from cache, really fast!
    {
      long start = System.currentTimeMillis();
      Arrays.stream(AUTHORS)
          .forEach(author -> System.out.println(author + ": " + region.get(author)));

      long elapsed = System.currentTimeMillis() - start;
      System.out.println(
          String.format("\n\nFetched %d cached definitions in %d ms\n\n", AUTHORS.length, elapsed));
    }
  }
}
