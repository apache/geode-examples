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
package org.apache.geode_examples.micrometerMetrics;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.IntStream;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;

public class Example {
  public static void main(String[] args) {
    addCacheEntries();
    verifyPrometheusEndpointsAreRunning();
  }

  private static void addCacheEntries() {
    // connect to the locator using default port
    ClientCache cache = new ClientCacheFactory().addPoolLocator("localhost", 10334)
        .set("log-level", "WARN").create();

    // create a local region that matches the server region
    Region<Integer, String> region =
        cache.<Integer, String>createClientRegionFactory(ClientRegionShortcut.PROXY)
            .create("example-region");

    // add entries to the region
    IntStream.rangeClosed(1, 10).forEach(i -> region.put(i, "value" + i));

    System.out.println(String.format("The entry count for region %s on the server is %d.",
        region.getName(), region.sizeOnServer()));

    cache.close();
  }

  private static void verifyPrometheusEndpointsAreRunning() {
    String[] endpoints = {"http://localhost:9914", "http://localhost:9915"};

    for (String endpoint : endpoints) {
      try {
        URL url = new URL(endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();

        if (HttpURLConnection.HTTP_OK != connection.getResponseCode()) {
          throw new IllegalStateException(
              "Prometheus endpoint returned status code " + connection.getResponseCode());
        }
      } catch (IOException e) {
        throw new IllegalStateException("Failed to connect to Prometheus endpoint", e);
      }

      System.out.println("A Prometheus endpoint is running at " + endpoint + ".");
    }
  }
}
