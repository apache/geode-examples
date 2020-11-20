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
package org.apache.geode_examples.rest;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.IntStream;

public class Example {
  private final Region<Integer, String> region;

  public Example(Region<Integer, String> region) {
    this.region = region;
  }

  public static void main(String[] args) {
    // connect to the locator using default port 10334
    ClientCache cache = new ClientCacheFactory().addPoolLocator("127.0.0.1", 10334)
        .set("log-level", "WARN").create();

    // create a local region that matches the server region
    Region<Integer, String> region =
        cache.<Integer, String>createClientRegionFactory(ClientRegionShortcut.PROXY)
            .create("example-region");

    Example example = new Example(region);
    example.insertValues(10);
    example.printValues();

    cache.close();
  }


  void insertValues(int upperLimit) {
    IntStream.rangeClosed(1, upperLimit).forEach(i -> region.put(i, "value" + i));
  }

  void printValues() {
    // create http connection
    HttpURLConnection conn = getHttpURLConnection();
    try (BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())))) {
      String response;
      while ((response = br.readLine()) != null) {
        System.out.println(response);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      conn.disconnect();
    }
  }

  private HttpURLConnection getHttpURLConnection() {
    URL url;
    HttpURLConnection conn = null;
    try {
      url = new URL("http://localhost:8080/gemfire-api/v1/example-region?limit=ALL");
      conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Accept", "application/json");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return conn;
  }
}

