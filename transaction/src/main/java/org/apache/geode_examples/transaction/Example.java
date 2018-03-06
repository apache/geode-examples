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
package org.apache.geode_examples.transaction;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Example {
  public static final int INCREMENTS = 1000;

  public static final String REGION_NAME = "example-region";

  public static final String KEY = "counter";

  final Region<String, Integer> region;

  final Map<Integer, Process> children = new HashMap<>();

  static String constructJVMPath() {
    StringBuilder builder = new StringBuilder();
    builder.append(System.getProperty("java.home"));
    builder.append(File.separator);
    builder.append("bin");
    builder.append(File.separator);
    builder.append("java");
    if (System.getProperty("os.name").toLowerCase().contains("win")) {
      builder.append("w.exe");
    }
    return builder.toString();
  }

  public static void main(String[] args) {
    // connect to the locator using default port 10334
    ClientCache cache = new ClientCacheFactory().addPoolLocator("127.0.0.1", 10334)
        .set("log-level", "WARN").create();

    // create a local region that matches the server region
    ClientRegionFactory<String, Integer> clientRegionFactory =
        cache.createClientRegionFactory(ClientRegionShortcut.PROXY);
    Region<String, Integer> region = clientRegionFactory.create(REGION_NAME);

    Example example = new Example(region);
    example.initializeEntry();
    example.executeChildProcesses(5);

    cache.close();
  }

  Example(Region<String, Integer> region) {
    this.region = region;
  }

  void executeChildProcess(int id) {
    String[] command = new String[5];
    command[0] = constructJVMPath();
    command[1] = "-classpath";
    command[2] = System.getProperty("java.class.path") + ":build/libs/transaction.jar";
    command[3] = "org.apache.geode_examples.transaction.Incrementer";
    command[4] = Integer.toString(id);
    try {
      children.put(id, Runtime.getRuntime().exec(command));
      System.out.println("Executed child " + id);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  void executeChildProcesses(int numberOfIncrementers) {
    System.out.println("Expected value of counter: " + (numberOfIncrementers * INCREMENTS));

    for (int i = 0; i < numberOfIncrementers; ++i) {
      executeChildProcess(i + 1);
    }

    for (Map.Entry<Integer, Process> child : children.entrySet()) {
      System.out.println("Waiting for " + child.getKey() + "...");
      try {
        child.getValue().waitFor();
        System.out.println("Reaped child " + child.getKey());
      } catch (InterruptedException ie) {
        ie.printStackTrace();
      }
    }

    System.out.println("Actual value of counter:   " + region.get(KEY));
  }

  void initializeEntry() {
    region.put(KEY, 0);
  }
}
